import requests
import re
from bs4 import BeautifulSoup
from logger import Logger


class ActorScraper:
    def __init__(self, logger, url):
        self.logger = logger
        self.url = None
        self.soup = None
        if url is not None:
            self.prepare_for_actor(url)

    def prepare_for_actor(self, url):
        self.url = url
        res = requests.get(url)
        self.soup = BeautifulSoup(res.content, 'html5lib')

    @staticmethod
    def get_movies_from_columns(title_movie):
        print('Trial 1: Suppose the movies are listed in columns')

        cols_movies = title_movie.findNext('div', {'class': 'div-col columns column-width'})
        if cols_movies is None:
            return None
        movies = []
        for li in cols_movies.findAll('li'):
            tag_movie_title = li.find('a')
            if tag_movie_title is not None:
                if 'href' not in tag_movie_title.attrs:
                    print('get_movies_from_columns: No href tag in <a>: ', tag_movie_title)
                    continue
                title = tag_movie_title.text
                if title is None:
                    print('get_movies_from_columns: No title in <a>: ', tag_movie_title)
                    continue
                url = 'https://en.wikipedia.org' + tag_movie_title['href']
            else:
                tag_movie_title = li.find('i')
                url = None
                title = tag_movie_title.text
                if title is None:
                    print('get_movies_from_columns: No title in <i>: ', tag_movie_title)
                    continue
            year = re.search(r' \(([0-9]+)\)', li.text)
            if year is None:
                print('get_movies_from_columns: No year in <li>: ', li)
            else:
                year = int(year.group(1))
            movies.append({
                'url': url,
                'title': title,
                'year': year
            })
        return movies

    @staticmethod
    def get_movies_from_table(title_movie):
        print('Trial 2: Suppose the movies are listed in a table')

        table_movies = title_movie.findNext('table', {'class': re.compile('.*wikitable*')})
        if table_movies is None:
            return None
        movies = []
        row_span_rem = 0
        prev_year = 0
        table_movies.find('tbody').find('tr')
        for tr in table_movies.find('tbody').findAll('tr')[1:]:
            first_tag = tr.find('td')
            if first_tag is None:
                print('get_movies_from_columns: No year in <tr>: ', tr)
                continue
            else:
                if row_span_rem > 0:
                    year = prev_year
                    row_span_rem -= 1
                    tag_movie_title = first_tag.find('i')
                else:
                    try:
                        year = int(first_tag.text)
                    except ValueError:
                        print('get_movies_from_columns: First column is not year')
                        return None
                    if 'rowspan' in first_tag.attrs:
                        row_span_rem = int(first_tag['rowspan']) - 1
                        prev_year = year
                    tag_movie_title = first_tag.findNext('td').find('i')
                if tag_movie_title.find('a') is not None:
                    tag_movie_title = tag_movie_title.find('a')
                    if 'href' not in tag_movie_title.attrs:
                        print('get_movies_from_columns: No href tag in <a>: ', tag_movie_title)
                        continue
                    title = tag_movie_title.text
                    if title is None:
                        print('get_movies_from_columns: No title in <a>: ', tag_movie_title)
                        continue
                    url = 'https://en.wikipedia.org' + tag_movie_title['href']
                else:
                    url = None
                    title = tag_movie_title.text
                    if title is None:
                        print('get_movies_from_columns: No title in <i>: ', tag_movie_title)
                        continue
            movies.append({
                'url': url,
                'title': title,
                'year': year
            })
        return movies

    def get_movies(self):
        print('Start scraping actor on', self.url)

        # title_movie = self.soup.find('span', {'id': 'Filmography'})
        title_movie = self.soup.find('span', {'id': re.compile('.*[fF]ilmography.*')})
        if title_movie is None:
            print('Failure: get_movies: Cannot find tag with title "Filmography"\n')
            self.logger.log_cannot_scrape(self.url)
            return None

        # Trial 1: If the movies are listed in columns
        movies = self.get_movies_from_columns(title_movie)
        if movies is not None:
            print('Trial 1: Success. Scraped', len(movies), 'movies.')
            return movies

        # Trial 2: If the movies are shown in a table
        movies = self.get_movies_from_table(title_movie)
        if movies is not None:
            print('Trial 2: Success. Scraped', len(movies), 'movies.')
            return movies
        print('Failure\n')
        self.logger.log_cannot_scrape(self.url)
        return movies


# ActorScraper(Logger(), 'https://en.wikipedia.org/wiki/Francis_X._McCarthy').get_movies()
# ActorScraper(Logger(), 'https://en.wikipedia.org/wiki/William_Hall,_Jr.').get_movies()
# ActorScraper(Logger(), 'https://en.wikipedia.org/wiki/Alan_North').get_movies()
# ActorScraper(Logger(), 'https://en.wikipedia.org/wiki/Karina_Arroyave').get_movies()
# ActorScraper(Logger(), 'https://en.wikipedia.org/wiki/Cathy_Murphy').get_movies()
