class Graph:
    def __init__(self):
        self.actor_urls = set()
        self.movie_urls = set()

    def store_actor(self, url):
        if url in self.actor_urls:
            return False
        else:
            self.actor_urls.add(url)
            return True

    def store_movie(self, url):
        if url in self.movie_urls:
            return False
        else:
            self.movie_urls.add(url)
            return True
