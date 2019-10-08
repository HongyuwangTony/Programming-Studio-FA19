import json
import logging
from collections import defaultdict
from typing import Dict

"""
I/O Helper Python File, controlling all the inputs and outputs from/to files during scraping
"""

logger = logging.getLogger('logger')

file_failure_actor = open("data/urlFailure_actor.txt", "a")
file_failure_movie = open("data/urlFailure_movie.txt", "a")
set_failure_actor, set_failure_movie = set(), set()  # Sets indicating failed to be scraped

actors, movies = list(), list()  # Scraped actors/movies
next_id_actor, next_id_movie = 1, 1  # Unused id of actor/movie
dict_urls_actor, dict_urls_movie = defaultdict(int), defaultdict(int)  # Dictonaries from url to id

# Two-way dictionaries of relationship between actors and movies
actor_to_movie, movie_to_actor = defaultdict(set), defaultdict(set)


def init_logger():
    """Initiates the logger for the entire scraping task
    """
    logger.setLevel(logging.DEBUG)

    formatter = logging.Formatter('%(asctime)s - %(levelname)s: %(message)s')

    handler_console = logging.StreamHandler()
    handler_console.setLevel(logging.INFO)
    handler_console.setFormatter(formatter)
    logger.addHandler(handler_console)

    handler_output = logging.FileHandler('scraper.log', 'w')
    handler_output.setLevel(logging.DEBUG)
    handler_output.setFormatter(formatter)
    logger.addHandler(handler_output)


def init_failure_detection():
    """Initiates failed urls of scraping
    """
    with open("data/urlFailure_actor.txt", "r") as f:
        line = f.readline()
        while line:
            set_failure_actor.add(line.strip())
            line = f.readline()
    with open("data/urlFailure_movie.txt", "r") as f:
        line = f.readline()
        while line:
            set_failure_movie.add(line.strip())
            line = f.readline()


def log_cannot_scrape_actor(url: str):
    """Logs the scraper cannot scrape the actor of the given url

    Args:
        url: The url for the given actor
    """
    if url not in set_failure_actor:
        set_failure_actor.add(url)
        file_failure_actor.write(url + '\n')


def log_cannot_scrape_movie(url: str):
    """Logs the scraper cannot scrape the movie of the given url

    Args:
        url: The url for the given movie
    """
    if url not in set_failure_movie:
        set_failure_movie.add(url)
        file_failure_movie.write(url + '\n')


def log_actor(actor: Dict):
    """Logs the actor successfully scraped

    Args:
        actor: The actor successfully scraped by the ActorScraper
    """
    global next_id_actor
    actors.append(actor)
    if actor['url'] not in dict_urls_actor:
        dict_urls_actor.update({actor['url']: next_id_actor})
        next_id_actor += 1


def log_movie(movie: Dict):
    """Logs the movie successfully scraped

    Args:
        movie: The movie successfully scraped by the MovieScraper
    """
    global next_id_movie
    movies.append(movie)
    if movie['url'] not in dict_urls_movie:
        dict_urls_movie.update({movie['url']: next_id_movie})
        next_id_movie += 1


def connect(actor, movie):
    """Connects the actor's url with the movie's url in the two-way dictionaries

    Args:
        actor: The actor that acts in 'movie'
        movie: The movie that 'actor' acts in
    """
    actor_to_movie[actor['url']].add(movie['url'])
    movie_to_actor[movie['url']].add(actor['url'])


def store_data_to_file():
    """Stores the successfully scraped data to actors.json and movies.json
    Each actor has a unique id, a name, a birth date, and a list of movies' ids
    Each movie has a unique id, a title, a year, a grossing value, and a list of actors' ids
    """
    # Closes failure records
    file_failure_actor.close()
    file_failure_movie.close()

    # Translates actors' urls into ids and the movies' urls into their movie ids
    for actor in actors:
        set_movies = actor_to_movie[actor['url']]
        actor.update({'id': dict_urls_actor[actor['url']]})
        actor.update({'movies': []})
        for url_movie in set_movies:
            if url_movie in dict_urls_movie:
                movie_id = dict_urls_movie[url_movie]
                if movie_id == -1:
                    continue
                actor['movies'].append(movie_id)
        del actor['url']

    # Translates movies' urls into ids and the actors' urls into their actor ids
    for movie in movies:
        set_actors = movie_to_actor[movie['url']]
        movie.update({'id': dict_urls_movie[movie['url']]})
        movie.update({'actors': []})
        for url_actor in set_actors:
            if url_actor in dict_urls_actor:
                actor_id = dict_urls_actor[url_actor]
                if actor_id == -1:
                    continue
                movie['actors'].append(actor_id)
        del movie['url']

    # Translates python dictionary list into json
    with open('data/actors.json', 'w', encoding='utf-8') as f:
        json.dump(actors, f, ensure_ascii=False, indent=4)
        f.close()
    with open('data/movies.json', 'w', encoding='utf-8') as f:
        json.dump(movies, f, ensure_ascii=False, indent=4)
        f.close()
