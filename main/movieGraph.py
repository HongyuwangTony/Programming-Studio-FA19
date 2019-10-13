from datetime import datetime, date, timedelta

from graph import *


class MovieGraph(object):
    """ A Wrapper Class wrapping graph class, specifically for storing movies and actors

    Actors and Movies are separately stored in two different dictionaries (name/title -> Node)

    NOTE: Since actors are stored before movies, nodes of actors would have lower ident
    """
    def __init__(self):
        """Constructor of MovieGraph Class
        """
        self.graph = Graph()
        self.actors = defaultdict(Node)
        self.movies = defaultdict(Node)

    def read_from_scraped_data(self, actors_file: str, movies_file: str):
        """Reads actors and movies from scraped data, which is stored in the given files

        By default, actors_file should be "data/actors.json" and movies_file should be "data/movies.json"

        Args:
            actors_file: The name of the file storing actors
            movies_file: The name of the file storing movies
        """
        with open(actors_file, "r") as f:
            actors = json.load(f)
            f.close()
        with open(movies_file, "r") as f:
            movies = json.load(f)
            f.close()

        dict_actors = defaultdict(Node)
        dict_movies = defaultdict(Node)
        dict_cast = defaultdict(list)

        # Adds actors as nodes
        for actor in actors:
            actor_id = actor['id']
            del actor['id']
            del actor['movies']
            node = self.graph.add_node(actor)
            self.actors.update({actor['name']: node})
            dict_actors.update({actor_id: node})

        # Adds movies as nodes
        for movie in movies:
            movie_id = movie['id']
            list_actors = movie['actors']
            del movie['id']
            del movie['actors']
            node = self.graph.add_node(movie)
            self.movies.update({movie['title']: node})
            dict_movies.update({movie_id: node})
            if len(list_actors) > 0:
                dict_cast.update({movie_id: list_actors})

        # Adds edges between actors and movies
        for movie_id, list_actors in dict_cast.items():
            node_movie = dict_movies[movie_id]
            list_actors = list(filter(lambda x: x in dict_actors, list_actors))
            list_actors = sorted(list_actors,
                                 key=lambda x: datetime.strptime(dict_actors[x].attrs['birth'], '%Y-%m-%d'))
            grossing_avg = float(node_movie.attrs['grossing']) / len(list_actors)
            index_mid = (len(list_actors) - 1.0) / 2.0
            for index, actor_id in enumerate(list_actors):
                weight = grossing_avg + (index - index_mid) * 1000
                self.graph.add_edge(node_movie, dict_actors[actor_id], weight)

    def custom_read_from_scraped_data(self, file_name: str):
        """Reads actors and movies from scraped data, which is stored in the given files

        Args:
            file_name: The name of the file storing actors
        """
        with open(file_name, "r") as f:
            dataset = json.load(f)
            f.close()

        actor_to_movie = defaultdict(list)
        movie_to_actor = defaultdict(list)
        for data in dataset:
            for name, attrs in data.items():
                json_class = attrs.pop('json_class')
                if json_class == 'Actor':
                    actor_to_movie.update({name: attrs.pop('movies')})
                    # birth = date(date.today().year - attrs.pop('age'), date.today().month, date.today().day).\
                    #         strftime("%Y-%m-%d")
                    # attrs.update({'birth': birth})
                    self.actors.update({name: self.graph.add_node(attrs)})
                elif json_class == 'Movie':
                    movie_to_actor.update({name: attrs.pop('actors')})
                    self.movies.update({name: self.graph.add_node(attrs)})

        for movie_name, list_actors in movie_to_actor.items():
            node_movie = self.movies[movie_name]
            list_actors = list(filter(lambda x: x in self.actors, list_actors))
            if len(list_actors) == 0:
                continue
            list_actors = sorted(list_actors, key=lambda x: self.actors.get(x).attrs['age'])
            grossing_avg = float(node_movie.attrs['box_office']) / len(list_actors)
            index_mid = (len(list_actors) - 1.0) / 2.0
            for index, actor_name in enumerate(list_actors):
                weight = grossing_avg + (index - index_mid)
                self.graph.add_edge(node_movie, self.actors[actor_name], weight)

    def get_grossing(self, movie_title: str) -> float:
        """Query Function: Finds how much a movie has grossed

        Args:
            movie_title: The title of the given movie for query

        Returns:
            The grossing value of the given movie
        """
        if movie_title not in self.movies:
            return 0.0
        return float(self.movies[movie_title].attrs['grossing'])

    def get_movies_of_actor(self, actor_name: str) -> List[str]:
        """Query Function: Lists which movies an actor has worked in

        Args:
            actor_name: The name of the given actor

        Returns:
            A list of movies that the given actor has worked in
        """
        if actor_name not in self.actors:
            return []
        nodes_movie = self.graph.adjacent_nodes(self.actors[actor_name])
        return [str(node_movie.attrs['title']) for node_movie in nodes_movie]

    def get_actors_of_movie(self, movie_title: str) -> List[str]:
        """Query Function: Lists which actors worked in a movie

        Args:
            movie_title: The title of the given movie

        Returns:
            A list of actors that worked in the given movie
        """
        if movie_title not in self.movies:
            return []
        nodes_actors = self.graph.adjacent_nodes(self.movies[movie_title])
        return [str(node_actors.attrs['name']) for node_actors in nodes_actors]

    def get_top_x_grossing_actors(self, num: int) -> List[str]:
        """Query Function: Lists the top X actors with the most total grossing value

        Args:
            num: The maximum number of top actors queried

        Returns:
            A list of top X actors with the most total grossing value
        """
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
        """Query Function: Lists the oldest X actors

        Args:
            num: The maximum number of oldest actors queried

        Returns:
            A list of the oldest X actors
        """
        if num <= 0:
            return []
        dict_ages = defaultdict(datetime)
        for node_actor in self.actors.values():
            dict_ages.update({node_actor.attrs['name']: datetime.strptime(node_actor.attrs['birth'], "%Y-%m-%d")})
        return [pair[0] for pair in sorted(dict_ages.items(), key=lambda kv: kv[1])[:num]]

    def get_movies_by_year(self, year: int) -> List[str]:
        """Query Function: Lists all the movies for a given year

        Args:
            year: The given year for movies

        Returns:
            A list of all the movies for the given year
        """
        res = []
        for node_movie in self.movies.values():
            if int(node_movie.attrs['year']) == year:
                res.append(node_movie.attrs['title'])
        return res

    def get_actors_by_year(self, year: int) -> List[str]:
        """Query Function: Lists all the actors for a given year

        Args:
            year: The given year for actors

        Returns:
            A list of all the actors borned in the given year
        """
        res = []
        for node_actor in self.actors.values():
            if int(node_actor.attrs['birth'].split('-')[0]) == year:
                res.append(node_actor.attrs['name'])
        return res
