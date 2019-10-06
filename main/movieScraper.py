import requests
from bs4 import BeautifulSoup
from logger import *
import re


class MovieScraper:
    def __init__(self):
        self.url = None
        self.soup = None

    def prepare_for_movie(self, url):
        self.url = url
        res = requests.get(url)
        self.soup = BeautifulSoup(res.content, 'html5lib')

    @staticmethod
    def get_actors_from_columns(title_cast):
        logger.info('Trial 1: Suppose the actors are listed in columns')

        cols_actors = title_cast.findNext('div', {'class': 'div-col columns column-width'})
        if cols_actors is None:
            cols_actors = title_cast.findNext('ul')
        if cols_actors is None:
            return None
        actors_scraped = []
        for li in cols_actors.findAll('li'):
            tag_actor_name = li.find('a')
            if tag_actor_name is not None:
                if 'href' not in tag_actor_name.attrs:
                    logger.debug('get_actors_from_columns: No href tag in <a>: ', tag_actor_name)
                    continue
                name = tag_actor_name.text
                if name is None:
                    logger.debug('get_actors_from_columns: No name in <a>: ', tag_actor_name)
                    continue
                url = 'https://en.wikipedia.org' + tag_actor_name['href']
            else:
                name = str(li.text)
                name = name.split(" as")[0]
                if name is None:
                    logger.debug('get_actors_from_columns: No name in <i>: ', tag_actor_name)
                    continue
                url = None
            actors_scraped.append({
                'url': url,
                'name': name
            })
        return actors_scraped

    def get_actors(self):
        logger.info('Start scraping movie on ' + self.url)

        title_cast = self.soup.find('span', {'id': 'Cast'})
        if title_cast is None:
            logger.warning('Failure: get_actors(): Cannot find tag with title "Cast"')
            log_cannot_scrape_movie(self.url)
            return None
        actors_scraped = self.get_actors_from_columns(title_cast)
        if actors_scraped is not None:
            logger.info('Success. Scraped %d actors.', len(actors_scraped))
            return actors_scraped
        logger.warning('Failure: get_actors()')
        log_cannot_scrape_movie(self.url)
        return actors_scraped

    def get_grossing(self):
        tag_box_office = self.soup.find('th', text=re.compile('.*Box office.*'), attrs={'scope': 'row'})
        if tag_box_office is not None:
            grossing_text = str(tag_box_office.findNext('td').text)
            if not grossing_text.startswith('$'):
                return None
            try:
                grossing_text = grossing_text[1:].split('[')[0]
                grossing_split = grossing_text.split()
                unit = 1
                if len(grossing_split) > 1:
                    if grossing_split[1].startswith('million'):
                        unit = 10**6
                    elif grossing_split[1].startswith('billion'):
                        unit = 10**9
                return float(grossing_split[0].replace(',', '')) * unit
            except ValueError:
                return None
            except AttributeError:
                return None
        else:
            return None


# s = MovieScraper()
# s.prepare_for_movie('https://en.wikipedia.org/wiki/The_Dark_Knight_(film)')
# print(s.get_grossing())
