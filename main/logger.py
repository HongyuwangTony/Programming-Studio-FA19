import json


class Logger:
    def __init__(self):
        self.set = set()
        with open("urlFailure.txt", "r") as f:
            line = f.readline()
            while line:
                self.set.add(line.strip())
                line = f.readline()
        self.file = open("urlFailure.txt", "a")
        self.actors = []
        self.movies = []

    def __del__(self):
        self.file.close()
        with open('actors.json', 'w', encoding='utf-8') as f:
            json.dump(self.actors, f, ensure_ascii=False, indent=4)
        with open('movies.json', 'w', encoding='utf-8') as f:
            json.dump(self.movies, f, ensure_ascii=False, indent=4)

    def log_cannot_scrape(self, url):
        if url not in self.set:
            self.set.add(url)
            self.file.write(url + '\n')

    def log_actor(self, actor):
        self.actors.append(actor)
        print('Actors: ', len(self.actors), ', Movies: ', len(self.movies))

    def log_movie(self, movie):
        self.movies.append(movie)
        print('Actors: ', len(self.actors), ', Movies: ', len(self.movies))
