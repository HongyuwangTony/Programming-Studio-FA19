from actorScraper import ActorScraper
from movieScraper import MovieScraper
from logger import Logger
from graph import Graph
from queue import Queue


class Scraper:
    def __init__(self, start_point):
        self.logger = Logger()
        self.actorScraper = ActorScraper(self.logger, None)
        self.movieScraper = MovieScraper(self.logger, None)
        self.graph = Graph()
        self.q = Queue()
        self.q.put(start_point)
        if start_point['type'] == 'actor':
            self.graph.store_actor(start_point['url'])
        else:
            self.graph.store_movie(start_point['url'])

    def start(self):
        while not self.q.empty():
            item = self.q.get()
            if item['url'] is None:
                continue
            if item['type'] == 'actor':
                self.scrape_actor(item['url'])
            else:
                self.scrape_movie(item['url'])

    def scrape_actor(self, url):
        self.actorScraper.prepare_for_actor(url)
        movies = self.actorScraper.get_movies()
        count_new_movies = 0
        if movies is None:
            return
        for movie in movies:
            if movie['url'] is not None and self.graph.store_movie(movie['url']):
                count_new_movies += 1
                self.q.put({'type': 'movie', 'url': movie['url']})
                self.logger.log_movie(movie)
        print(count_new_movies, 'new movies are added to the graph\n')

    def scrape_movie(self, url):
        self.movieScraper.prepare_for_movie(url)
        actors = self.movieScraper.get_actors()
        count_new_actors = 0
        if actors is None:
            return
        for actor in actors:
            if actor['url'] is not None and self.graph.store_actor(actor['url']):
                count_new_actors += 1
                self.q.put({'type': 'actor', 'url': actor['url']})
                self.logger.log_actor(actor)
        print(count_new_actors, 'new actors are added to the graph\n')


Scraper({'type': 'actor', 'url': 'https://en.wikipedia.org/wiki/Morgan_Freeman'}).start()
