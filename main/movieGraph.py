from datetime import datetime

from graph import *


class MovieGraph(object):
    def __init__(self):
        self.graph = Graph()
        self.actors = defaultdict(Node)
        self.movies = defaultdict(Node)

    def read_from_scraped_data(self, actors_file: str, movies_file: str):
        with open(actors_file, "r") as f:
            actors = json.load(f)
            f.close()
        with open(movies_file, "r") as f:
            movies = json.load(f)
            f.close()

        dict_actors = defaultdict(Node)
        dict_movies = defaultdict(Node)
        dict_acts_in = defaultdict(list)

        # Adds actors as nodes
        for actor in actors:
            actor_id = actor['id']
            list_movies = actor['movies']
            del actor['id']
            del actor['movies']
            node = self.graph.add_node(actor)
            self.actors.update({actor['name']: node})
            dict_actors.update({actor_id: node})
            dict_acts_in.update({actor_id: list_movies})

        # Adds movies as nodes
        for movie in movies:
            movie_id = movie['id']
            del movie['id']
            del movie['actors']
            node = self.graph.add_node(movie)
            self.movies.update({movie['title']: node})
            dict_movies.update({movie_id: node})

        # Adds edges between actors and movies
        for actor_id, list_movies in dict_acts_in.items():
            node_actor = dict_actors[actor_id]
            for movie_id in list_movies:
                if movie_id not in dict_movies:
                    continue
                node_movie = dict_movies[movie_id]
                self.graph.add_edge(node_actor, node_movie)

    def get_grossing(self, movie_title: str) -> float:
        if movie_title not in self.movies:
            return 0.0
        return float(self.movies[movie_title].attrs['grossing'])

    def get_movies_of_actor(self, actor_name: str) -> List[str]:
        if actor_name not in self.actors:
            return []
        nodes_movie = self.graph.adjacent_nodes(self.actors[actor_name])
        return [str(node_movie.attrs['title']) for node_movie in nodes_movie]

    def get_actors_of_movie(self, movie_title: str) -> List[str]:
        if movie_title not in self.movies:
            return []
        nodes_actors = self.graph.adjacent_nodes(self.movies[movie_title])
        return [str(node_actors.attrs['name']) for node_actors in nodes_actors]

    def get_top_x_grossing_actors(self, num: int) -> List[str]:
        if num <= 0:
            return []
        dict_grossing = defaultdict(float)
        for node_actor in self.actors.values():
            total_grossing = 0.0
            for node_movie in self.graph.adjacent_nodes(node_actor):
                total_grossing += float(node_movie.attrs['grossing'])
            dict_grossing.update({node_actor.attrs['name']: total_grossing})
        return [pair[0] for pair in sorted(dict_grossing.items(), key=lambda kv: kv[1], reverse=True)[:num]]

    def get_oldest_x_actors(self, num: int) -> List[str]:
        if num <= 0:
            return []
        dict_ages = defaultdict(datetime)
        for node_actor in self.actors.values():
            dict_ages.update({node_actor.attrs['name']: datetime.strptime(node_actor.attrs['birth'], "%Y-%m-%d")})
        return [pair[0] for pair in sorted(dict_ages.items(), key=lambda kv: kv[1])[:num]]

    def get_movies_by_year(self, year: int) -> List[str]:
        res = []
        for node_movie in self.movies.values():
            if int(node_movie.attrs['year']) == year:
                res.append(node_movie.attrs['title'])
        return res

    def get_actors_by_year(self, year: int) -> List[str]:
        res = []
        for node_actor in self.actors.values():
            if int(node_actor.attrs['birth'].split('-')[0]) == year:
                res.append(node_actor.attrs['name'])
        return res


mg = MovieGraph()
mg.read_from_scraped_data("data/actors.json", "data/movies.json")
print(mg.get_grossing('Brubaker'))
print(mg.get_movies_of_actor('Morgan Freeman'))
print(mg.get_actors_of_movie('Brubaker'))
print(mg.get_top_x_grossing_actors(10))
print(mg.get_oldest_x_actors(10))
print(mg.get_movies_by_year(1980))
print(mg.get_actors_by_year(1937))
