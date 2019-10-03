class Logger:
    def __init__(self):
        self.file = open("urlFailure.txt", "a")

    def __del__(self):
        self.file.close()

    def log_cannot_scrape(self, url):
        self.file.write(url + '\n')
