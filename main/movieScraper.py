import requests
from bs4 import BeautifulSoup
from logger import Logger


class MovieScraper:
    def __init__(self, logger, url):
        self.logger = logger
        self.url = None
        self.soup = None
        if url is not None:
            self.prepare_for_movie(url)

    def prepare_for_movie(self, url):
        self.url = url
        res = requests.get(url)
        self.soup = BeautifulSoup(res.content, 'html5lib')

    @staticmethod
    def get_actors_from_columns(title_cast):
        print('Trial 1: Suppose the actors are listed in columns')

        cols_actors = title_cast.findNext('div', {'class': 'div-col columns column-width'})
        if cols_actors is None:
            cols_actors = title_cast.findNext('ul')
        if cols_actors is None:
            return None
        actors = []
        for li in cols_actors.findAll('li'):
            tag_actor_name = li.find('a')
            if tag_actor_name is not None:
                if 'href' not in tag_actor_name.attrs:
                    print('get_actors_from_columns: No href tag in <a>: ', tag_actor_name)
                    continue
                name = tag_actor_name.text
                if name is None:
                    print('get_actors_from_columns: No name in <a>: ', tag_actor_name)
                    continue
                url = 'https://en.wikipedia.org' + tag_actor_name['href']
            else:
                name = str(li.text)
                name = name.split(" as")[0]
                if name is None:
                    print('get_actors_from_columns: No name in <i>: ', tag_actor_name)
                    continue
            actors.append({
                'url': url,
                'name': name
            })
        return actors

    def get_actors(self):
        print('Start scraping movie on', self.url)

        title_cast = self.soup.find('span', {'id': 'Cast'})
        if title_cast is None:
            print('Failure: get_films: Cannot find tag with title "Cast"\n')
            self.logger.log_cannot_scrape(self.url)
            return None
        actors = self.get_actors_from_columns(title_cast)
        if actors is not None:
            print('Success. Scraped', len(actors), 'actors.')
            return actors
        print('Failure\n')
        self.logger.log_cannot_scrape(self.url)
        return actors
