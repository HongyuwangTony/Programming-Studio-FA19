from unittest import TestCase

from movieGraph import MovieGraph


class TestMovieGraph(TestCase):
    def setUp(self):
        self.movieGraph = MovieGraph()
        self.movieGraph.read_from_scraped_data("data/actors_test.json", "data/movies_test.json")

    def test_get_grossing(self):
        self.assertEqual(37121708, int(self.movieGraph.get_grossing('Brubaker')))

    def test_get_movies_of_actor(self):
        self.assertCountEqual(
            ['Lean on Me', 'Johnny Handsome', 'Robin Hood: Prince of Thieves', 'Unforgiven',
             'The Shawshank Redemption', 'Outbreak', 'Seven', 'Chain Reaction', 'Moll Flanders',
             'Amistad', 'Kiss the Girls', 'Deep Impact', 'Nurse Betty', 'Along Came a Spider',
             'The Sum of All Fears', 'High Crimes', 'Bruce Almighty', 'Million Dollar Baby',
             'Unleashed', 'An Unfinished Life', 'Batman Begins', 'Lucky Number Slevin', '10 Items or Less',
             'Evan Almighty', 'Gone, Baby, Gone', 'The Bucket List', 'Wanted', 'The Dark Knight', 'Invictus',
             'RED', 'Dolphin Tale', 'The Dark Knight Rises', 'The Magic of Belle Isle', 'Olympus Has Fallen',
             'Oblivion', 'Now You See Me', 'Last Vegas', 'The Lego Movie', 'Transcendence', 'Lucy', 'Dolphin Tale 2',
             '5 Flights Up', 'Momentum', 'Ted 2', 'London Has Fallen', 'Now You See Me 2', 'Going In Style',
             'Just Getting Started', 'The Nutcracker and the Four Realms', 'Angel Has Fallen', 'Feast of Love',
             'Brubaker', 'Marie', 'That Was Then... This Is Now', 'Street Smart', 'Glory', 'Driving Miss Daisy'],
            self.movieGraph.get_movies_of_actor('Morgan Freeman')
        )

    def test_get_actors_of_movie(self):
        self.assertCountEqual(
            ['Morgan Freeman', 'Yaphet Kotto', 'Jane Alexander', 'Murray Hamilton', 'David Keith', 'Matt Clark',
             'Richard Ward', 'M. Emmet Walsh', 'Albert Salmi', 'Val Avery', 'Joe Spinell'],
            self.movieGraph.get_actors_of_movie('Brubaker')
        )

    def test_get_top_x_grossing_actors(self):
        self.assertListEqual(
            ['Morgan Freeman', 'M. Emmet Walsh', 'Jeff Daniels', 'Jane Alexander', 'Sissy Spacek', 'Richard Schiff',
             'Keith Szarabajka', 'David Keith', 'Val Avery', 'Matt Clark'],
            self.movieGraph.get_top_x_grossing_actors(10)
        )

    def test_get_oldest_x_actors(self):
        self.assertListEqual(
            ['Jessica Tandy', 'Helen Martin', 'Richard Ward', 'Kumar Pallana', 'Alan North', 'Richard Marner',
             'James Whitmore', 'Murray Hamilton', 'Val Avery', 'Allan Rich'],
            self.movieGraph.get_oldest_x_actors(10)
        )

    def test_get_movies_by_year(self):
        self.assertListEqual(
            ['Brubaker', 'Raise the Titanic', 'Ordinary People', 'Caddyshack', 'Gloria', 'Cruising', 'Maniac',
             "Coal Miner's Daughter", 'Heart Beat'],
            self.movieGraph.get_movies_by_year(1980)
        )

    def test_get_actors_by_year(self):
        self.assertListEqual(
            ['Morgan Freeman', 'Barbara Babcock', 'Dustin Hoffman', 'Yevgeni Lazarev', 'Paul Collins'],
            self.movieGraph.get_actors_by_year(1937)
        )
