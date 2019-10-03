import requests
import re
from bs4 import BeautifulSoup
from logger import Logger


class ActorScraper:
    def __init__(self, url):
        self.logger = Logger()
        self.url = url

        res = requests.get(url)
        self.soup = BeautifulSoup(res.content, 'html5lib')

    @staticmethod
    def get_films_from_columns(title_film):
        form_films = title_film.findNext('div', {"class": "div-col columns column-width"})
        if form_films is None:
            return None
        films = []
        for li in form_films.findAll('li'):
            tag_film_title = li.find('a')
            if tag_film_title is None:
                print("get_films_from_columns: No <a> tag in <li>: ", li)
                continue
            print("https://en.wikipedia.org" + tag_film_title["href"])
            print(tag_film_title.text)
            print(re.search(r" \(([0-9]+)\)", li.find(text=True, recursive=False)).group(1))
        return films

    def get_films(self):
        title_film = self.soup.find("span", {"id": "Filmography"})
        if title_film is None:
            self.logger.log_cannot_scrape(self.url)
            return None
        form_films = self.get_films_from_columns(title_film)
        if form_films is None:
            form_films = title_film.findNext('div', {"class": "wikitable sortable jquery-tablesorter"})
        print(form_films)


URL = "https://en.wikipedia.org/wiki/Morgan_Freeman"
ActorScraper(URL).get_films()
