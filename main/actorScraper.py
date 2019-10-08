import requests
import re
from typing import Optional, List
from bs4 import BeautifulSoup

from logger import *


class ActorScraper(object):
    """Scraper specifically to scrape actor pages
    """

    def __init__(self):
        """Constructor of Actor Scraper
        Initializes the url and Beautiful Soup object to be None
        """
        self.url = None
        self.soup = None

    def prepare_for_actor(self, url: str):
        """Prepares for scraping the given actor
        Initializes the url and Beautiful Soup object

        Args:
            url: The url of the actor to be prepared to be scraped
        """
        self.url = url
        res = requests.get(url)
        self.soup = BeautifulSoup(res.content, 'html5lib')

    @staticmethod
    def _get_movies_from_columns(title_movie: BeautifulSoup) -> List[Dict]:
        """Gets a list of movies the actor acts in from a div of columns

        Args:
            title_movie: The title tag containing "Filmography"

        Returns:
            A list of movie object with url, title and year
        """
        logger.info('Trial 1: Suppose the movies are listed in columns')

        cols_movies = title_movie.findNext('div', {'class': 'div-col columns column-width'})
        if cols_movies is None:  # Columns not found
            return []

        movies_scraped = []
        for li in cols_movies.findAll('li'):
            # Scrapes the title and url of the movie
            tag_movie_title = li.find('a')
            if tag_movie_title is not None:  # If the tag contains href
                if 'href' not in tag_movie_title.attrs:
                    logger.debug('get_movies_from_columns: No href tag in <a>: ' + str(tag_movie_title))
                    continue
                title = tag_movie_title.text
                if title is None:
                    logger.debug('get_movies_from_columns: No title in <a>: ' + str(tag_movie_title))
                    continue
                url = 'https://en.wikipedia.org' + tag_movie_title['href']

            else:  # If the tag doesn't contain href, thus no link is available
                tag_movie_title = li.find('i')
                url = None
                title = tag_movie_title.text
                if title is None:
                    logger.debug('get_movies_from_columns: No title in <i>: ' + str(tag_movie_title))
                    continue

            # Scrapes the year of the movie
            year = re.search(r' \(([0-9]+)', li.text)  # Searches for (Number
            if year is None:
                logger.debug('get_movies_from_columns: No year in <li>: ' + str(li))
            else:
                year = int(year.group(1))

            movies_scraped.append({
                'url': url,
                'title': title,
                'year': year
            })

        return movies_scraped

    @staticmethod
    def _get_movies_from_table(title_movie: BeautifulSoup) -> List[Dict]:
        """Gets a list of movies the actor acts in from a table

        Args:
            title_movie: The title tag containing "Filmography"

        Returns:
            A list of movie object with url, title and year
        """
        logger.info('Trial 2: Suppose the movies are listed in a table')

        table_movies = title_movie.findNext('table', {'class': re.compile('.*wikitable*')})
        if table_movies is None:  # If the table tag with class wikitable is not found
            return []

        movies_scraped = []
        row_span_rem = 0  # Remaining effective rows of prev_year
        prev_year = 0  # Stores the year of multi-line rows

        for tr in table_movies.find('tbody').findAll('tr')[1:]:  # Suppose the first row contains attributes
            first_tag = tr.find('td')
            if first_tag is None:
                logger.debug('get_movies_from_columns: No year in <tr>: ' + str(tr))
                continue

            try:
                # Scrapes the year of the movie and finds the next tag
                if row_span_rem > 0:  # If this row is contained by a list of movies of the given year
                    year = prev_year
                    row_span_rem -= 1
                    tag_movie_title = first_tag.find('i')
                else:
                    year = int(first_tag.text)
                    if 'rowspan' in first_tag.attrs:  # If the multi-line row starts
                        row_span_rem = int(first_tag['rowspan']) - 1
                        prev_year = year
                    tag_movie_title = first_tag.findNext('td').find('i')

                # Scrapes the title and url of the movie
                url = None
                if tag_movie_title.find('a') is not None:
                    tag_movie_title = tag_movie_title.find('a')
                    if 'href' in tag_movie_title.attrs:
                        url = 'https://en.wikipedia.org' + tag_movie_title['href']
                title = tag_movie_title.text

            except ValueError:  # Might caused by int() conversion
                logger.debug('get_movies_from_columns: Unexpected column for year')
                return []
            except AttributeError:  # Might caused by calling function or extracting attribute from None object
                logger.debug('get_movies_from_columns: Unexpected title tag')
                continue
            if title is None:
                logger.debug('get_movies_from_columns: No title found in the tag')
                continue

            movies_scraped.append({
                'url': url,
                'title': title,
                'year': year
            })

        return movies_scraped

    def get_movies(self) -> List[Dict]:
        """Gets a list of movies the actor acts in

        Returns:
            A list of movies the actor acts in
        """
        logger.info('Start scraping movies for actor on ' + self.url)

        # Finds the title tag of movie list
        title_movie = self.soup.find('span', {'id': re.compile('.*[fF]ilmography.*')})
        if title_movie is None:
            logger.warning('Failure: get_movies(): Cannot find tag with title "Filmography"')
            log_cannot_scrape_actor(self.url)
            return []

        # Trial 1: If the movies are listed in columns
        movies_scraped = self._get_movies_from_columns(title_movie)
        if len(movies_scraped) != 0:
            logger.info('Trial 1: Success. Scraped %d movies', len(movies_scraped))
            return movies_scraped

        # Trial 2: If the movies are shown in a table
        movies_scraped = self._get_movies_from_table(title_movie)
        if len(movies_scraped) != 0:
            logger.info('Trial 2: Success. Scraped %d movies.', len(movies_scraped))
            return movies_scraped

        # Failure: No movie can be scraped from this actor
        logger.warning('Failure: get_movies()')
        log_cannot_scrape_actor(self.url)
        return []

    def get_birth(self) -> Optional[str]:
        """Gets the birth date for the given actor

        Returns:
            A formatted string indicating the birth date
        """
        logger.info('Start scraping birthdate for actor on ' + self.url)
        try:
            tag_birth = self.soup.find('span', {'class': re.compile('.*bday.*')})
            return tag_birth.text
        except AttributeError:
            logger.warning('Failure: get_birth()')
            return None
