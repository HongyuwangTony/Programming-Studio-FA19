from actorScraper import ActorScraper
from movieScraper import MovieScraper
from logger import *
from queue import Queue
import threading
import time


class Scraper:
    def __init__(self, start_point):
        self.actorScraper = ActorScraper()
        self.movieScraper = MovieScraper()
        self.num_movies = 0
        self.num_actors = 0
        self.q_actors = Queue()
        self.q_movies = Queue()
        self.init_queue(start_point)

    def init_queue(self, start_point):
        start_type = start_point['type']
        del start_point['type']
        if start_type == 'actor':
            self.q_actors.put(start_point)
            self.num_actors += 1
        else:
            self.q_movies.put(start_point)
            self.num_movies += 1

    def run(self, limit_actor, limit_movie):
        t1 = threading.Thread(target=self.start_scrape_actor, args=(limit_actor, limit_movie))
        t2 = threading.Thread(target=self.start_scrape_movie, args=(limit_actor, limit_movie))
        t1.start()
        t2.start()
        t1.join()
        t2.join()
        store_data_to_file()
        self.q_actors.task_done()
        self.q_movies.task_done()

    def start_scrape_actor(self, limit_actor, limit_movie):
        while self.num_actors < limit_actor or self.num_movies < limit_movie:
            item = self.q_actors.get()
            if item is not None:
                self.scrape_actor(item)

    def start_scrape_movie(self, limit_actor, limit_movie):
        while self.num_actors < limit_actor or self.num_movies < limit_movie:
            item = self.q_movies.get()
            if item is not None:
                self.scrape_movie(item)

    def scrape_actor(self, actor):
        self.actorScraper.prepare_for_actor(actor['url'])

        movies_scraped = self.actorScraper.get_movies()
        count_new_movies = 0
        if movies_scraped is None:
            return
        for movie in movies_scraped:
            if movie['url'] is not None and \
                    movie['url'] not in set_urls_movie and \
                    movie['url'] not in set_failure_movie:
                count_new_movies += 1
                self.q_movies.put(movie)
                connect(actor, movie)
        logger.info('%d new movies are added to the queue.', count_new_movies)

        birth = self.actorScraper.get_birth()
        if birth is None:
            return
        actor.update({'birth': birth})
        log_actor(actor)
        self.num_actors += 1

        logger.info('Scraped %d actors so far.\n', self.num_actors)

    def scrape_movie(self, movie):
        self.movieScraper.prepare_for_movie(movie['url'])

        actors_scraped = self.movieScraper.get_actors()
        count_new_actors = 0
        if actors_scraped is None:
            return
        for actor in actors_scraped:
            if actor['url'] is not None and \
                    actor['url'] not in set_urls_actor and \
                    actor['url'] not in set_failure_actor:
                count_new_actors += 1
                self.q_actors.put(actor)
                connect(actor, movie)
        logger.info('%d new actors are added to the queue.', count_new_actors)

        grossing = self.movieScraper.get_grossing()
        if grossing is None:
            return
        movie.update({'grossing': grossing})
        log_movie(movie)
        self.num_movies += 1

        logger.info('Scraped %d movies so far.\n', self.num_movies)


if __name__ == '__main__':
    init_logger()
    scraper = Scraper({
        'type': 'actor',
        'name': 'Morgan Freeman',
        'url': 'https://en.wikipedia.org/wiki/Morgan_Freeman'
    })
    start_time = time.time()
    logger.info('Scraper start.')
    scraper.run(250, 125)
    end_time = time.time()
    logger.info('Scraper done in %s.', time.strftime('%H:%M:%S', time.gmtime(end_time - start_time)))
