from typing import List, Optional

import requests
import re
from bs4 import BeautifulSoup

from logger import *


class MovieScraper:
    """Scraper specifically to scrape movie pages
    """

    def __init__(self):
        """Constructor of Movie Scraper
        Initializes the url and Beautiful Soup object to be None
        """
        self.url = None
        self.soup = None

    def prepare_for_movie(self, url: str) -> bool:
        """Prepares for scraping the given movie
        Initializes the url and Beautiful Soup object

        Args:
            url: The url of the actor to be prepared to be scraped

        Returns:
            True if Beautiful Soup object is correctly setup
        """
        self.url = url
        try:
            res = requests.get(url)
            self.soup = BeautifulSoup(res.content, 'html5lib')
            return True
        except requests.exceptions.ConnectionError:
            return False

    @staticmethod
    def _get_actors_from_columns(title_cast: BeautifulSoup) -> List[Dict]:
        """Gets a list of actors that acts in the movie from a div of columns

        Args:
            title_cast: The title tag containing "Cast"

        Returns:
            A list of actor object with url and name
        """
        logger.info('Trial 1: Suppose the actors are listed in columns')

        # Columns either in div or ul
        cols_actors = title_cast.findNext('div', {'class': 'div-col columns column-width'})
        if cols_actors is None:
            cols_actors = title_cast.findNext('ul')
        if cols_actors is None:
            return []

        actors_scraped = []
        for li in cols_actors.findAll('li'):

            # Suppose name is either stored in a tag or before "as"
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

    def get_actors(self) -> List[Dict]:
        """Gets a list of actors that acts in the movie

        Returns:
            A list of actors that acts in the movie
        """
        logger.info('Start scraping movie on ' + self.url)

        # Finds the title tag with of cast
        title_cast = self.soup.find('span', {'id': 'Cast'})
        if title_cast is None:
            logger.warning('Failure: get_actors(): Cannot find tag with title "Cast"')
            log_cannot_scrape_movie(self.url)
            return []

        # Trial: Suppose the actors are stored in columns
        actors_scraped = self._get_actors_from_columns(title_cast)
        if len(actors_scraped) != 0:
            logger.info('Success. Scraped %d actors.', len(actors_scraped))
            return actors_scraped

        # Failure: No actor can be scraped from this movie
        logger.warning('Failure: get_actors()')
        log_cannot_scrape_movie(self.url)
        return actors_scraped

    def get_grossing(self) -> Optional[float]:
        """Gets the grossing value of the movie

        Returns:
            The grossing value represented as a floating point value in the unit of dollar
        """
        tag_box_office = self.soup.find('th', text=re.compile('.*Box office.*'), attrs={'scope': 'row'})
        try:
            grossing_text = str(tag_box_office.findNext('td').text)

            # Suppose the grossing value is in the unit of dollar
            if not grossing_text.startswith('$'):
                return None

            # Drops the superscript
            grossing_text = grossing_text[1:].split('[')[0]
            grossing_split = grossing_text.split()
            unit = 1  # The default unit is one dollar. Searches for million/billion to update the unit
            if len(grossing_split) > 1:
                if grossing_split[1].startswith('million'):
                    unit = 10**6
                elif grossing_split[1].startswith('billion'):
                    unit = 10**9
            return float(grossing_split[0].replace(',', '')) * unit  # Escapes commas in the grossing value
        except ValueError:  # Might caused by float() conversion
            return None
        except AttributeError:  # Might caused by calling the function / using the attribute of None type
            return None
