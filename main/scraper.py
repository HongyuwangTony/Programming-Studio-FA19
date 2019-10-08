from queue import Queue
from threading import Thread
import time

from actorScraper import ActorScraper
from movieScraper import MovieScraper
from logger import *


class Scraper(object):
    """Scraper class that runs the whole scraping task
    """
    def __init__(self, start_point: Dict):
        """Constructor of Scraper given the start point

        Args:
            start_point: The start actor/movie to scrape
        """
        self._actor_scraper = ActorScraper()
        self._movie_scraper = MovieScraper()
        self._num_movies = 0
        self._num_actors = 0
        self._q_actors = Queue()
        self._q_movies = Queue()
        self._init_queue(start_point)

    def _init_queue(self, start_point: Dict):
        """Initiates the scraping queues of actors and movies with the given start point

        Args:
            start_point: The start actor/movie to scrape
        """
        start_type = start_point['type']
        del start_point['type']
        if start_type == 'actor':
            self._q_actors.put(start_point)
            self._num_actors += 1
        else:
            self._q_movies.put(start_point)
            self._num_movies += 1

    def run(self, limit_actor: int, limit_movie: int):
        """Runs the actor and movie scraper in two different threads, until they reach the given limits
        Stops if the two limits are both reached
        Stores the data and clears the garbage after running

        Args:
            limit_actor: The upper limit of the number of actors to scrape
            limit_movie: The upper limit of the number of movies to scrape
        """
        t1 = Thread(target=self._start_scrape_actor, args=(limit_actor, limit_movie))
        t2 = Thread(target=self._start_scrape_movie, args=(limit_actor, limit_movie))
        t1.start()
        t2.start()

        # Waits till both threads are terminated and executes the storage and garbage clear
        t1.join()
        t2.join()
        store_data_to_file()
        self._q_actors.task_done()
        self._q_movies.task_done()

    def _start_scrape_actor(self, limit_actor: int, limit_movie: int):
        """Starts scraping actors, triggered by the first thread

        Args:
            limit_actor: The upper limit of the number of actors to scrape
            limit_movie: The upper limit of the number of movies to scrape
        """
        while self._num_actors < limit_actor or self._num_movies < limit_movie:
            item = self._q_actors.get()
            if item is not None:
                self._scrape_actor(item)

    def _start_scrape_movie(self, limit_actor: int, limit_movie: int):
        """Starts scraping movies, triggered by the second thread

        Args:
            limit_actor: The upper limit of the number of actors to scrape
            limit_movie: The upper limit of the number of movies to scrape
        """
        while self._num_actors < limit_actor or self._num_movies < limit_movie:
            item = self._q_movies.get()
            if item is not None:
                self._scrape_movie(item)

    def _scrape_actor(self, actor: Dict):
        """Scrapes one given actor's information

        Args:
            actor: The given actor to scrape
        """
        self._actor_scraper.prepare_for_actor(actor['url'])

        # Scrapes movies that 'actor' acts in
        movies_scraped = self._actor_scraper.get_movies()
        if len(movies_scraped) == 0:
            return

        # Pushes valid movies into the movie queue
        count_new_movies = 0
        for movie in movies_scraped:
            if movie['url'] is not None and \
                    movie['url'] not in dict_urls_movie and \
                    movie['url'] not in set_failure_movie:
                count_new_movies += 1
                self._q_movies.put(movie)
                connect(actor, movie)
        logger.info('%d new movies are added to the queue.', count_new_movies)

        # Scrapes the birth of 'actor'
        birth = self._actor_scraper.get_birth()
        if birth is None:
            return

        # Stores and records the successfully scraped actor
        actor.update({'birth': birth})
        log_actor(actor)
        self._num_actors += 1
        logger.info('Scraped %d actors so far.\n', self._num_actors)

    def _scrape_movie(self, movie: Dict):
        """Scrapes one given movie's information

        Args:
            movie: The given movie to scrape
        """
        self._movie_scraper.prepare_for_movie(movie['url'])

        # Scrapes actors that acts in 'movie'
        actors_scraped = self._movie_scraper.get_actors()
        if len(actors_scraped) == 0:
            return

        # Pushes valid actors into the actor queue
        count_new_actors = 0
        for actor in actors_scraped:
            actor_url = actor['url']
            if actor_url is not None and \
                    actor_url not in dict_urls_actor and \
                    actor_url not in set_failure_actor:
                count_new_actors += 1
                self._q_actors.put(actor)
                connect(actor, movie)
        logger.info('%d new actors are added to the queue.', count_new_actors)

        # Scrapes the grossing value of 'movie'
        grossing = self._movie_scraper.get_grossing()
        if grossing is None:
            return

        # Stores and records the successfully scraped movie
        movie.update({'grossing': grossing})
        log_movie(movie)
        self._num_movies += 1
        logger.info('Scraped %d movies so far.\n', self._num_movies)


# Main function of scraper
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
