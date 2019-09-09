/**
* Shell Lab
* CS 241 - Fall 2018
*/
#include <unistd.h>
#include <stdio.h>
#include <stdbool.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <ctype.h>
#include <string.h>

#include "format.h"
#include "shell.h"
#include "vector.h"

typedef struct process {
    char *command;
    char *status;
    pid_t pid;
} process;

/*****************************  Static Variables  *****************************/
static char *buffer = NULL;
static char *currDirectory = NULL;
static pid_t currPid = 0;
static pid_t process_foreground = -1;
static char *history_fileName = NULL;
static vector *history = NULL;
static vector *processes = NULL;

/**********************  Helper functions of processes  **********************/
process* process_constructor(char*, char*, pid_t);
void process_destructor(process*);
int find_process_with_pid(pid_t);
void end_processes(void);

/*****************************  Signal Handlers  *****************************/
void handler_int(int);
void check_child(int);

/******************************  Run Commands  ******************************/
int run_command(char *, bool);
int handle_logical_operators(char *);
int handle_builtin_command(char *, bool);
char** split_command(const char *);
void destory_argv(char **);

/****************************  Built-in Commands  ****************************/
int change_directory(const char *);
void show_history(void);
int run_history(int);
int run_history_with_prefix(const char *);
void ps(void);
int kill_pid(pid_t);
int stop_pid(pid_t);
int cont_pid(pid_t);
int exit_shell(int);

/************************  Flag Handlers (-h and -f)  ************************/
void run_command_from_file(const char *);
void read_history_from_file(char *);
void write_history_to_file();

/************************  Logic Operator (&& || ;)  ************************/
int logic_and(char *, char *);
int logic_or(char *, char *);
int logic_separator(char *, char *);

/******************************  Main Function  ******************************/
int shell(int argc, char *argv[]) {
    signal(SIGINT, handler_int);
    signal(SIGCHLD, check_child);
    if(argc > 5) {
        print_usage();
        exit(EXIT_FAILURE);
    }
    currDirectory = get_current_dir_name();
    currPid = getpid();
    history = string_vector_create();
    processes = shallow_vector_create();
    char *command_file = NULL;
    char *history_file = NULL;
    bool isValid = false;
    if(argc > 1) {
        int ret_getopt = 0;
        while ((ret_getopt = getopt(argc, argv, "h:f:")) != -1) {
            switch (ret_getopt) {
                case 'h': // -h flag
                    isValid = true;
                    history_file = optarg;
                    break;
                case 'f': // -f flag
                    isValid = true;
                    command_file = optarg;
                    break;
                default:
                    isValid = false;
            }
        }
    }
    if(argc > 1) {
        if(!isValid) {
            print_usage();
            exit_shell(EXIT_FAILURE);
        }
    }
    if(history_file) read_history_from_file(history_file);
    if(command_file) run_command_from_file(command_file);
    size_t capacity = 0;
    ssize_t ret_getline = 0;
    print_prompt(currDirectory, currPid);
    while( (ret_getline = getline(&buffer, &capacity, stdin)) != -1) {
        if(ret_getline > 0 && buffer[ret_getline - 1] == '\n') {
            buffer[ret_getline - 1] = '\0';
        }
        if(run_command(buffer, true) == EXIT_FAILURE) {
            free(buffer);
            exit_shell(EXIT_FAILURE);
        }
        print_prompt(currDirectory, currPid);
    }
    if(buffer) free(buffer);
    buffer = NULL;
    return exit_shell(EXIT_SUCCESS);
}

/**********************  Helper functions of processes  **********************/
/**
 *  Constructs the process struct with given arguments.
 *
 *  @param command The command of the given process
 *  @param status The status of the given process
 *  @param pid The pid of the given process
 *
 *  @return the pointer of the constructed process on the heap
 */
process* process_constructor(char* command, char* status, pid_t pid) {
    process *ret = calloc(1, sizeof(process));
    ret->command = strdup(command);
    ret->status = status;
    ret->pid = pid;
    return ret;
}

/**
 *  Destructs the process struct where the given pointer points to.
 *
 *  @param process The process pointer to be destructed
 */
void process_destructor(process* process) {
    if(!process) return;
    if(process->command) free(process->command);
    free(process);
}

/**
 *  Finds the shell with the given pid.
 *
 *  @param pid The pid of the process to find
 *
 *  @return The index of the processes stored statically
 *          Or -1 if the process is not found
 */
int find_process_with_pid(pid_t pid) {
    size_t processes_size = vector_size(processes);
    for(size_t i = 0; i < processes_size; i++) {
        process *process = vector_get(processes, i);
        if(process->pid == pid) return i;
    }
    return -1;
}

/**
 *  Ends all the processes in the program.
 */
void end_processes() {
    size_t processes_size = vector_size(processes);
    for(size_t i = 0; i < processes_size; i++) {
        process *process = vector_get(processes, i);
        print_killed_process(process->pid, process->command);
        kill(process->pid, SIGTERM);
        vector_erase(processes, i);
        process_destructor(process);
    }
}

/*****************************  Signal Handlers  *****************************/
/**
 *  Kill the foreground process if it exists.
 *  Handles the SIGINT signal.
 *
 *  @param signal The signal parameter corresponded to SIGINT
 */
void handler_int(int signal) {
    if(process_foreground == -1) return; // no foreground process
    kill(process_foreground, SIGTERM);
    process_foreground = -1;
}

/**
 *  End the process when a child probably exits.
 *  Handles the SIGCHLD signal.
 *
 *  @param signal The signal parameter corresponded to SIGCHLD
 */
void check_child(int signal) {
    (void)signal;
    int status;
    pid_t pid_child;
    bool failure = false;
    while( (pid_child = waitpid((pid_t) -1, &status, WNOHANG)) > 0) { // handle that multiple children exit at the same time
        int index = find_process_with_pid(pid_child);
        if(index < 0) continue;
        if(WIFEXITED(status)) {
            process_destructor(vector_get(processes, index));
            vector_erase(processes, index); // erase the backgrounding process from the vector
            if(WEXITSTATUS(status) == EXIT_FAILURE) {
                failure = true;
            }
        }
    }
    if(failure) {
        free(buffer);
        exit_shell(EXIT_FAILURE); // exit the program immediately
    }
}

/******************************  Run Commands  ******************************/
/**
 *  Run the given command.
 *
 *  @param command The content of command to run
 *  @param record The flag that determines whether to put the command in the history
 *
 *  @return 0(EXIT_SUCCESS) The command is running normally
 *          1(EXIT_FAILURE) The command requires the program to exit immediately
 *          -2 The command is running with error
 */
int run_command(char *command, bool record) {
    /** handle logical operators **/
    if(record){
       int ret_logical_operators = handle_logical_operators(command);
       if(ret_logical_operators != -1) return ret_logical_operators;
    }

    /** handle built-in commands **/
    // exit is responsible for memory free.
    int ret_builtin_command = handle_builtin_command(command, record);
    if(ret_builtin_command != -1) return ret_builtin_command;

    /** handle external commands **/
    /** runs only if the two previous return value is -1 **/
    if(record) vector_push_back(history, (void *)command);
    pid_t pid_child = fork();
    if(pid_child < 0) { // fork failed
        print_fork_failed();
        return EXIT_FAILURE;
    }
    else if(pid_child == 0) {  // child
        char **argv_in = split_command(command);
        int ret_execvp = execvp(argv_in[0], argv_in);
        if(ret_execvp < 0) { // exec failed
            print_exec_failed(command);
            destory_argv(argv_in); argv_in = NULL;
            exit(EXIT_FAILURE);
        }
        destory_argv(argv_in); argv_in = NULL;
        print_exec_failed(command);
        exit(EXIT_FAILURE); // not reachable
    }
    else { // parent
        print_command_executed(pid_child);
        if(command[strlen(command) - 1] == '&') {
            if(setpgid(pid_child, pid_child) == -1) { // setpgid failed
                print_setpgid_failed();
                return EXIT_FAILURE;
            }
            vector_push_back(processes, process_constructor(command, STATUS_RUNNING, pid_child));
            int status;
            waitpid(pid_child, &status, WNOHANG); // not waiting child
            return EXIT_SUCCESS;
        }
        else {
            process_foreground = pid_child;
            int status;
            int ret_waitpid = waitpid(pid_child, &status, 0); // wait until child exits
            process_foreground = -1;
            if(ret_waitpid < 0) { // wait failed
                print_wait_failed();
                return EXIT_FAILURE;
            }
            if(WEXITSTATUS(status) == EXIT_FAILURE) {
                return EXIT_FAILURE; // exit the program immediately, caused by exec failure
            }
            return EXIT_SUCCESS;
        }
    }
}

/**
 *  Judge whether the command contains logical operators.
 *  If so, run it with operators.
 *
 *  @param command The content of command to judge and run
 *
 *  @return 0(EXIT_SUCCESS) The command is running normally
 *          1(EXIT_FAILURE) The command requires the program to exit immediately
 *          -1 The command does not logical operators
 */
int handle_logical_operators(char *command) {
    char *ret_strstr = NULL;
    char *command1 = NULL;
    char *command2 = NULL;
    /** logical and **/
    ret_strstr = strstr(command, " && ");
    if(ret_strstr) {
        vector_push_back(history, (void *)command);
        int first_len = ret_strstr - command;
        command1 = strndup(command, first_len);
        command2 = strdup(ret_strstr + 4);
        int ret = logic_and(command1, command2);
        free(command1);
        free(command2);
        if(ret == EXIT_FAILURE) return EXIT_FAILURE;
        return EXIT_SUCCESS;
    }
    /** logical or **/
    ret_strstr = strstr(command, " || ");
    if(ret_strstr) {
        vector_push_back(history, (void *)command);
        int first_len = ret_strstr - command;
        command1 = strndup(command, first_len);
        command2 = strdup(ret_strstr + 4);
        int ret = logic_or(command1, command2);
        free(command1);
        free(command2);
        if(ret == EXIT_FAILURE) return EXIT_FAILURE;
        return EXIT_SUCCESS;
    }
    /** logical separator **/
    ret_strstr = strstr(command, "; ");
    if(ret_strstr) {
        vector_push_back(history, (void *)command);
        int first_len = ret_strstr - command;
        command1 = strndup(command, first_len);
        command2 = strdup(ret_strstr + 2);
        int ret = logic_separator(command1, command2);
        free(command1);
        free(command2);
        if(ret == EXIT_FAILURE) return EXIT_FAILURE;
        return EXIT_SUCCESS;
    }
    return -1;
}

/**
 *  Judge whether the command is a built-in command.
 *  If so, run it.
 *
 *  @param command The content of command to judge and run
 *
 *  @return 0(EXIT_SUCCESS) The command is running normally
 *          1(EXIT_FAILURE) The command requires the program to exit immediately
 *          -1 The command is not a built-in command
 *          -2 The command is running with error
 */
int handle_builtin_command(char *command, bool record) {
    /** !history **/
    if(!strcmp(command, "!history")) {
        show_history();
        return EXIT_SUCCESS;
    }
    /** ps **/
    if(!strcmp(command, "ps")) {
        if(record) vector_push_back(history, (void *)command);
        ps();
        return EXIT_SUCCESS;
    }
    /** exit **/
    if(!strcmp(command, "exit")) {
        free(command);
        return exit_shell(EXIT_SUCCESS);
    }
    if(!strcmp(command, "#")) {
        print_invalid_command(command);
        return EXIT_SUCCESS;
    }
    if(!strcmp(command, "cd ") || !strcmp(command, "kill") ||
      !strcmp(command, "stop") || !strcmp(command, "cont")) {
        if(record) vector_push_back(history, (void *)command);
        print_invalid_command(command);
        return EXIT_SUCCESS;
    }
    int ret_sscanf = 0;
    /** kill <pid>, stop <pid>, cont<pid> **/
    if(strlen(command) > 5) {
        int pid;
        ret_sscanf = sscanf(command, "kill %d", &pid);
        if(ret_sscanf > 0){
            if(record) vector_push_back(history, (void *)command);
            return kill_pid((int)pid);
        }
        ret_sscanf = sscanf(command, "stop %d", &pid);
        if(ret_sscanf > 0){
            if(record) vector_push_back(history, (void *)command);
            return stop_pid((int)pid);
        }
        ret_sscanf = sscanf(command, "cont %d", &pid);
        if(ret_sscanf > 0){
            if(record) vector_push_back(history, (void *)command);
            return cont_pid((int)pid);
        }
    }
    /** cd <path> **/
    if(strlen(command) > 3) {
        char *temp = (char *)calloc(strlen(command) - 2, sizeof(char));
        ret_sscanf = sscanf(command, "cd %s", temp);
        if(ret_sscanf > 0) {
            if(record) vector_push_back(history, (void *)command);
            int ret = change_directory(temp);
            free(temp);
            return ret;
        }
        free(temp);
    }
    /** #<n> **/
    if(strlen(command) > 1) {
        int index;
        ret_sscanf = sscanf(command, "#%d", &index);
        if(ret_sscanf > 0) return run_history(index);
    }
    /** !<prefix> **/
    if(!strcmp(command, "!")) return run_history_with_prefix("");
    char *temp = (char *)calloc(strlen(command), sizeof(char));
    ret_sscanf = sscanf(command, "!%s", temp);
    if(ret_sscanf > 0) {
        int ret = run_history_with_prefix(temp);
        free(temp);
        return ret;
    }
    free(temp);
    /** external commands **/
    return -1;
}

/**
 *  Split the command by spaces and handles backgrounding as well.
 *
 *  @param command The content of command to split
 *
 *  @return The pointer that points to a string array allocated on the heap
 */
char** split_command(const char *command) {
    // handles backgrounding ("&" or " &")
    size_t command_len = strlen(command);
    if(command_len > 0 && command[command_len - 1] == '&') {
        command_len--;
        if(command_len > 0 && command[command_len - 1] == ' ') {
            command_len--;
        }
    }
    // add '\0' to separate the arguments
    char *command_cpy = strndup(command, command_len);
    int argc_in = 1;
    for(size_t i = 0; i < command_len; i++) {
        if(command_cpy[i] == ' ') {
            command_cpy[i] = 0;
            argc_in++;
        }
    }
    // copy arguments on the string array allocated on the heap
    int currIndex = 0;
    char **ret = (char **) calloc(argc_in + 1, sizeof(char *));
    for(int i = 0; i < argc_in; i++) {
        ret[i] = strdup(command_cpy + currIndex);
        currIndex += (strlen(ret[i]) + 1);
    }
    ret[argc_in] = NULL;
    free(command_cpy);
    return ret;
}

/**
 *  Destroy and free the arguments created by split_command().
 *
 *  @param argv_in The pointer that points to the string array of arguments allocated on the heap
 */
void destory_argv(char** argv_in) {
    if(!argv_in) return;
    char **it = argv_in;
    while(*it) {
        free(*it); *it = NULL;
        it++;
    }
    free(argv_in);
}

/****************************  Built-in Commands  ****************************/
/**
 *  Change current directory to the given path.
 *
 *  @param path The destination path to locate
 *
 *  @return 0(EXIT_SUCCESS) The directory has been successfully changed to the given path
 *          -2 The path is invalid
 */
int change_directory(const char *path) {
    // TODO current or outside path
    if(strlen(path) == 0 || chdir(path) < 0) {
        print_no_directory(path);
        return -2;
    }
    free(currDirectory);
    currDirectory = get_current_dir_name();
    return EXIT_SUCCESS;
}

/**
 *  Show the history of input commands so far.
 *  Print the history line-by-line.
 */
void show_history() {
    size_t history_size = vector_size(history);
    for(size_t i = 0; i < history_size; i++) {
        print_history_line(i, vector_get(history, i));
    }
}

/**
 *  Run the history by the given index.
 *
 *  @param index The index of history to run
 *
 *  @return 0(EXIT_SUCCESS) This command in the history runs normally
 *          1(EXIT_FAILURE) This command in the history needs to exit the program immediately
 *          -2 The index is invalid
 */
int run_history(int index) {
    size_t history_size = vector_size(history);
    if(index >= (int)history_size) {
        print_invalid_index();
        return -2;
    }
    char *command = vector_get(history, index);
    print_command(command);
    if(run_command(command, true) == EXIT_FAILURE) return EXIT_FAILURE;
    return EXIT_SUCCESS;
}

/**
 *  Run the history by the given prefix.
 *
 *  @param prefix The prefix of command to be fit
 *
 *  @return 0(EXIT_SUCCESS) This command in the history runs normally
 *          1(EXIT_FAILURE) This command in the history needs to exit the program immediately
 *          -2 No history matches with the prefix
 */
int run_history_with_prefix(const char *prefix) {
    size_t history_size = vector_size(history);
    // Runs the last command
    if(strlen(prefix) == 0) {
        if(history_size == 0) {
            print_no_history_match();
            return -2;
        }
        else {
            char *command = *vector_back(history);
            print_command(command);
            if(run_command(command, true) == EXIT_FAILURE) return EXIT_FAILURE;
            return EXIT_SUCCESS;
        }
    }
    // Runs the last command by the given prefix
    size_t prefix_len = strlen(prefix);
    char *str_check = calloc(prefix_len, sizeof(char));
    for(size_t i = history_size - 1; true; i--) {
        char *command = vector_get(history, i);
        strncpy(str_check, command, prefix_len);
        if(!strcmp(str_check, prefix)) {
            print_command(command);
            free(str_check);
            if(run_command(command, true) == EXIT_FAILURE) return EXIT_FAILURE;
            return EXIT_SUCCESS;
        }
        if(i == 0) break; // prevent size_t underflow
    }
    free(str_check);
    print_no_history_match();
    return -2;
}

/**
 *  Show the processes currently in the program.
 *  Print the processes line-by-line.
 */
void ps() {
    print_process_info(STATUS_RUNNING, currPid, "./shell"); // print the current process
    size_t processes_size = vector_size(processes);
    for(size_t i = 0; i < processes_size; i++) {
        process *process = vector_get(processes, i);
        print_process_info(process->status, process->pid, process->command);
    }
}

/**
 *  Kill the process by the given pid.
 *
 *  @param pid The pid of the process to kill
 *
 *  @return 0(EXIT_SUCCESS) The process has been found and killed
 *          -2 No process matches with the pid
 */
int kill_pid(pid_t pid) {
    int index = find_process_with_pid(pid);
    if(index < 0){
        print_no_process_found(pid);
        return -2;
    }
    process *process = vector_get(processes, index);
    kill(pid, SIGTERM);
    print_killed_process(pid, process->command);
    vector_erase(processes, index);
    return EXIT_SUCCESS;
}

/**
 *  Stop the running process by the given pid.
 *
 *  @param pid The pid of the process to stop
 *
 *  @return 0(EXIT_SUCCESS) The process has been found and stopped
 *          -2 No process matches with the pid
 */
int stop_pid(pid_t pid) {
    int index = find_process_with_pid(pid);
    if(index < 0){
        print_no_process_found(pid);
        return -2;
    }
    process *process = vector_get(processes, index);
    kill(pid, SIGSTOP);
    print_stopped_process(pid, process->command);
    process->status = STATUS_STOPPED;
    return EXIT_SUCCESS;
}

/**
 *  Continue the stopped process by the given pid.
 *
 *  @param pid The pid of the process to continue
 *
 *  @return 0(EXIT_SUCCESS) The process has been found and continued
 *          -2 No process matches with the pid or the process is running.
 */
int cont_pid(pid_t pid) {
    int index = find_process_with_pid(pid);
    if(index < 0){
        print_no_process_found(pid);
        return -2;
    }
    process *process = vector_get(processes, index);
    if(!strcmp(process->status, STATUS_RUNNING)) return -2;
    kill(pid, SIGCONT);
    process->status = STATUS_RUNNING;
    return EXIT_SUCCESS;
}

/**
 *  Exits the shell with the given status.
 *
 *  @param status The status required at exit
 *
 *  @return There shall be no return of this function
 */
int exit_shell(int status) {
    // handles -h and loads the current history
    if(history_fileName) write_history_to_file();
    // ends all the processes in the program
    end_processes();
    // handles memory leak
    free(currDirectory);
    vector_destroy(history);
    vector_destroy(processes);
    exit(status);
}


/************************  Flag Handlers (-h and -f)  ************************/
/**
 *  Runs the command from the given file.
 *
 *  @param fileName The file name that contains the commands to run
 */
void run_command_from_file(const char *fileName) {
    FILE *inputFile = fopen(fileName, "r");
    if(!inputFile) {
        print_script_file_error();
        exit_shell(EXIT_FAILURE);
    }
    // char *buffer = NULL;
    size_t capacity = 0;
    ssize_t ret_getline = 0;
    while( (ret_getline = getline(&buffer, &capacity, inputFile)) != -1) {
        if(ret_getline > 0 && buffer[ret_getline - 1] == '\n') {
            buffer[ret_getline - 1] = '\0';
        }
        print_prompt(currDirectory, currPid);
        print_command(buffer);
        if(run_command(buffer, true) == EXIT_FAILURE) {
            free(buffer);
            exit_shell(EXIT_FAILURE);
        }
    }
    if(buffer) free(buffer);
    buffer = NULL;
    fclose(inputFile);
    exit_shell(EXIT_SUCCESS);
}

/**
 *  Reads the history from the given file.
 *
 *  @param fileName The file name that contains the history to read and load
 */
void read_history_from_file(char *fileName) {
    history_fileName = get_full_path(fileName);
    FILE *history_file = fopen(history_fileName, "r");
    if(!history_file) return; // file doesn't exist currently
    size_t capacity = 0;
    ssize_t ret_getline = 0;
    while( (ret_getline = getline(&buffer, &capacity, history_file)) != -1) {
        if(ret_getline > 0 && buffer[ret_getline - 1] == '\n') {
            buffer[ret_getline - 1] = '\0';
        }
        vector_push_back(history, buffer);
    }
    if(buffer) free(buffer);
    buffer = NULL;
    fclose(history_file);
}

/**
 *  Writes the history to the input file.
 *  Free the history-related memory.
 */
void write_history_to_file() {
    FILE *history_file = fopen(history_fileName, "w");
    size_t history_size = vector_size(history);
    for(size_t i = 0; i < history_size; i++) {
        fprintf(history_file, "%s\n", vector_get(history, i));
    }
    fclose(history_file);
    free(history_fileName);
}

/************************  Logic Operator (&& || ;)  ************************/
/**
 *  Handles the logic and of the two commands.
 *  The second command runs only if the first command runs normally.
 *
 *  @param command1 The first command of logic
 *  @param command2 The second command of logic
 *
 *  @return 0(EXIT_SUCCESS) The first command runs with error or the second command runs normally
 *          1(EXIT_FAILURE) The either command needs to exit the program immediately
 *          -2 The second command runs with error
 */
int logic_and(char *command1, char *command2) {
    int ret_command1 = run_command(command1, false);
    if(ret_command1 == EXIT_FAILURE) return EXIT_FAILURE; // command1 needs to exit
    else if(ret_command1 < 0) return EXIT_SUCCESS; // command1 failure
    return run_command(command2, false);
}

/**
 *  Handles the logic or of the two commands.
 *  The second command runs only if the first command runs with error.
 *
 *  @param command1 The first command of logic
 *  @param command2 The second command of logic
 *
 *  @return 0(EXIT_SUCCESS) The either command runs normally
 *          1(EXIT_FAILURE) The either command needs to exit the program immediately
 *          -2 The second command runs with error
 */
int logic_or(char *command1, char *command2) {
    int ret_command1 = run_command(command1, false);
    if(ret_command1 == EXIT_SUCCESS) return EXIT_SUCCESS; // command1 success
    return run_command(command2, false);
}

/**
 *  Handles the logic separator of the two commands.
 *  The two commands run sequentially.
 *
 *  @param command1 The first command of logic
 *  @param command2 The second command of logic
 *
 *  @return 0(EXIT_SUCCESS) Both two commands run normally
 *          1(EXIT_FAILURE) The either command needs to exit the program immediately
 */
int logic_separator(char *command1, char *command2) {
    int ret_command1 = run_command(command1, false);
    int ret_command2 = run_command(command2, false);
    if(ret_command1 == EXIT_FAILURE || ret_command2 == EXIT_FAILURE) return EXIT_FAILURE;
    return EXIT_SUCCESS;
}

/**
 *  Bug Report:
 *    valgrind with exec failed causes uninitialised value error
 */
