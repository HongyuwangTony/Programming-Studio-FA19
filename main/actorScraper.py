import requests
import re
from bs4 import BeautifulSoup
from logger import Logger


class ActorScraper:
    def __init__(self, url):
        self.logger = Logger()
        self.url = None
        self.soup = None
        if url is not None:
            self.prepare_for_actor(url)

    def prepare_for_actor(self, url):
        self.url = url
        res = requests.get(url)
        self.soup = BeautifulSoup(res.content, 'html5lib')

    @staticmethod
    def get_films_from_columns(title_film):
        print('Trial 1: Suppose the films are listed in columns')

        form_films = title_film.findNext('div', {'class': 'div-col columns column-width'})
        if form_films is None:
            return None
        films = []
        for li in form_films.findAll('li'):
            tag_film_title = li.find('a')
            if tag_film_title is not None:
                if 'href' not in tag_film_title.attrs:
                    print('get_films_from_columns: No href tag in <a>: ', tag_film_title)
                    continue
                title = tag_film_title.text
                if title is None:
                    print('get_films_from_columns: No title in <a>: ', tag_film_title)
                    continue
                url = 'https://en.wikipedia.org' + tag_film_title['href']
            else:
                tag_film_title = li.find('i')
                url = None
                title = tag_film_title.text
                if title is None:
                    print('get_films_from_columns: No title in <i>: ', tag_film_title)
                    continue
            year = re.search(r' \(([0-9]+)\)', li.text)
            if year is None:
                print('get_films_from_columns: No year in <li>: ', li)
            else:
                year = int(year.group(1))
            films.append({
                'url': url,
                'title': title,
                'year': year
            })
        return films

    def get_films(self):
        print('Start scraping ' + self.url)

        title_film = self.soup.find('span', {'id': 'Filmography'})
        if title_film is None:
            print('Failure: get_films: Cannot find tag with title "Filmography"\n')
            self.logger.log_cannot_scrape(self.url)
            return None
        # Trial 1: If the films are listed in columns
        films = self.get_films_from_columns(title_film)
        if films is not None:
            print('Trial 1: Success. Scraped', len(films), 'films.\n')
            return films
        print('Trial 1: Failure')
        # Trial 2: If the films are shown in a form
        films = title_film.findNext('div', {'class': 'wikitable sortable jquery-tablesorter'})
        print('\n')
        return films


ActorScraper('https://en.wikipedia.org/wiki/Morgan_Freeman').get_films()
ActorScraper('https://en.wikipedia.org/wiki/Matt_Clark_(actor)').get_films()
ActorScraper('https://en.wikipedia.org/wiki/Robert_Redford').get_films()
ActorScraper('https://en.wikipedia.org/wiki/Jane_Alexander').get_films()
ActorScraper('https://en.wikipedia.org/wiki/Murray_Hamilton').get_films()
ActorScraper('https://en.wikipedia.org/wiki/David_Keith').get_films()
