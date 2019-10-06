import json
import logging
from collections import defaultdict

logger = logging.getLogger('logger')
file_failure_actor = open("urlFailure_actor.txt", "a")
file_failure_movie = open("urlFailure_movie.txt", "a")
set_failure_actor = set()
set_failure_movie = set()
set_urls_actor = defaultdict(int)
set_urls_movie = defaultdict(int)
next_id_actor = 1
next_id_movie = 1
actors = []
movies = []
actor_to_movie = defaultdict(set)
movie_to_actor = defaultdict(set)


def init_logger():
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
    with open("urlFailure_actor.txt", "r") as f:
        line = f.readline()
        while line:
            set_failure_actor.add(line.strip())
            line = f.readline()
    with open("urlFailure_movie.txt", "r") as f:
        line = f.readline()
        while line:
            set_failure_movie.add(line.strip())
            line = f.readline()


def log_cannot_scrape_actor(url):
    if url not in set_failure_actor:
        set_failure_actor.add(url)
        # set_urls_actor.update({url: -1})
        file_failure_actor.write(url + '\n')


def log_cannot_scrape_movie(url):
    if url not in set_failure_movie:
        set_failure_movie.add(url)
        # set_urls_movie.update({url: -1})
        file_failure_movie.write(url + '\n')


def log_actor(actor):
    global next_id_actor
    actors.append(actor)
    if actor['url'] not in set_urls_actor:
        set_urls_actor.update({actor['url']: next_id_actor})
        next_id_actor += 1


def log_movie(movie):
    global next_id_movie
    movies.append(movie)
    if movie['url'] not in set_urls_movie:
        set_urls_movie.update({movie['url']: next_id_movie})
        next_id_movie += 1


def connect(actor, movie):
    actor_to_movie[actor['url']].add(movie['url'])
    movie_to_actor[movie['url']].add(actor['url'])


def store_data_to_file():
    file_failure_actor.close()
    file_failure_movie.close()

    for actor in actors:
        set_movies = actor_to_movie[actor['url']]
        actor.update({'id': set_urls_actor[actor['url']]})
        actor.update({'movies': []})
        for url_movie in set_movies:
            if url_movie in set_urls_movie:
                movie_id = set_urls_movie[url_movie]
                if movie_id == -1:
                    continue
                actor['movies'].append(movie_id)
        del actor['url']

    for movie in movies:
        set_actors = movie_to_actor[movie['url']]
        movie.update({'id': set_urls_movie[movie['url']]})
        movie.update({'actors': []})
        for url_actor in set_actors:
            if url_actor in set_urls_actor:
                actor_id = set_urls_actor[url_actor]
                if actor_id == -1:
                    continue
                movie['actors'].append(actor_id)
        del movie['url']

    with open('actors.json', 'w', encoding='utf-8') as f:
        json.dump(actors, f, ensure_ascii=False, indent=4)
        f.close()
    with open('movies.json', 'w', encoding='utf-8') as f:
        json.dump(movies, f, ensure_ascii=False, indent=4)
        f.close()
