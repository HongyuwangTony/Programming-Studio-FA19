class Logger:
    def __init__(self):
        self.set = set()
        with open("urlFailure.txt", "r") as f:
            line = f.readline()
            while line:
                self.set.add(line.strip())
                line = f.readline()
        self.file = open("urlFailure.txt", "a")

    def __del__(self):
        self.file.close()

    def log_cannot_scrape(self, url):
        if url not in self.set:
            self.set.add(url)
            self.file.write(url + '\n')
