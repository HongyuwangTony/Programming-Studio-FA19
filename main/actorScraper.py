import requests
import re
from bs4 import BeautifulSoup
from logger import *


class ActorScraper:
    def __init__(self):
        self.url = None
        self.soup = None

    def prepare_for_actor(self, url):
        self.url = url
        res = requests.get(url)
        self.soup = BeautifulSoup(res.content, 'html5lib')

    @staticmethod
    def get_movies_from_columns(title_movie):
        logger.info('Trial 1: Suppose the movies are listed in columns')

        cols_movies = title_movie.findNext('div', {'class': 'div-col columns column-width'})
        if cols_movies is None:
            return None
        movies_scraped = []
        for li in cols_movies.findAll('li'):
            tag_movie_title = li.find('a')
            if tag_movie_title is not None:
                if 'href' not in tag_movie_title.attrs:
                    logger.debug('get_movies_from_columns: No href tag in <a>: ' + str(tag_movie_title))
                    continue
                title = tag_movie_title.text
                if title is None:
                    logger.debug('get_movies_from_columns: No title in <a>: ' + str(tag_movie_title))
                    continue
                url = 'https://en.wikipedia.org' + tag_movie_title['href']
            else:
                tag_movie_title = li.find('i')
                url = None
                title = tag_movie_title.text
                if title is None:
                    logger.debug('get_movies_from_columns: No title in <i>: ' + str(tag_movie_title))
                    continue
            year = re.search(r' \(([0-9]+)', li.text)
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
    def get_movies_from_table(title_movie):
        logger.info('Trial 2: Suppose the movies are listed in a table')

        table_movies = title_movie.findNext('table', {'class': re.compile('.*wikitable*')})
        if table_movies is None:
            return None
        movies_scraped = []
        row_span_rem = 0
        prev_year = 0
        for tr in table_movies.find('tbody').findAll('tr')[1:]:
            first_tag = tr.find('td')
            if first_tag is None:
                logger.debug('get_movies_from_columns: No year in <tr>: ' + str(tr))
                continue
            try:
                if row_span_rem > 0:
                    year = prev_year
                    row_span_rem -= 1
                    tag_movie_title = first_tag.find('i')
                else:
                    year = int(first_tag.text)
                    if 'rowspan' in first_tag.attrs:
                        row_span_rem = int(first_tag['rowspan']) - 1
                        prev_year = year
                    tag_movie_title = first_tag.findNext('td').find('i')

                url = None
                if tag_movie_title.find('a') is not None:
                    tag_movie_title = tag_movie_title.find('a')
                    if 'href' in tag_movie_title.attrs:
                        url = 'https://en.wikipedia.org' + tag_movie_title['href']
                title = tag_movie_title.text

            except ValueError:
                logger.debug('get_movies_from_columns: Unexpected column for year')
                return None
            except AttributeError:
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

    def get_movies(self):
        logger.info('Start scraping movies for actor on ' + self.url)

        title_movie = self.soup.find('span', {'id': re.compile('.*[fF]ilmography.*')})
        if title_movie is None:
            logger.warning('Failure: get_movies(): Cannot find tag with title "Filmography"')
            log_cannot_scrape_actor(self.url)
            return None

        # Trial 1: If the movies are listed in columns
        movies_scraped = self.get_movies_from_columns(title_movie)
        if movies_scraped is not None:
            logger.info('Trial 1: Success. Scraped %d movies', len(movies_scraped))
            return movies_scraped

        # Trial 2: If the movies are shown in a table
        movies_scraped = self.get_movies_from_table(title_movie)
        if movies_scraped is not None:
            logger.info('Trial 2: Success. Scraped %d movies.', len(movies_scraped))
            return movies_scraped
        logger.warning('Failure: get_movies()')
        log_cannot_scrape_actor(self.url)
        return movies_scraped

    def get_birth(self):
        logger.info('Start scraping birthdate for actor on ' + self.url)
        try:
            tag_birth = self.soup.find('span', {'class': re.compile('.*bday.*')})
            return tag_birth.text
        except AttributeError:
            logger.warning('Failure: get_birth()')
            return None
