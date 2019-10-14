from unittest import TestCase
from api import *


class TestAPI(TestCase):
    def setUp(self):
        set_up_data_source('data/data_test.json')
        self.app = app.test_client()
        self.app.testing = True

    def test_handle_actors_by_name(self):
        # Tests normal GET
        resp = self.app.get('/actors/Bruce_Willis')
        self.assertEqual(resp.status_code, 200)
        data = json.loads(resp.data)
        self.assertEqual(data['age'], 61)

        # Tests using PUT to change name and age
        resp = self.app.put('/actors/Bruce_Willis',
                            data=json.dumps({'name': 'Bruce Willis New', 'age': 66}),
                            content_type='application/json')
        self.assertEqual(resp.status_code, 200)
        resp = self.app.get('/actors/Bruce_Willis')
        self.assertEqual(resp.status_code, 400)
        resp = self.app.get('/actors/Bruce_Willis_New')
        self.assertEqual(resp.status_code, 200)
        data = json.loads(resp.data)
        self.assertEqual(data['age'], 66)

        # Tests DELETE
        resp = self.app.delete('/actors/Bruce_Willis_New')
        self.assertEqual(resp.status_code, 200)
        resp = self.app.get('/actors/Bruce_Willis_New')
        self.assertEqual(resp.status_code, 400)

    def test_handle_movies_by_name(self):
        # Tests normal GET
        resp = self.app.get('/movies/The_Siege')
        self.assertEqual(resp.status_code, 200)
        data = json.loads(resp.data)
        self.assertEqual(data['box_office'], 116)

        # Tests using PUT to change name and box office
        resp = self.app.put('/movies/The_Siege',
                            data=json.dumps({'name': 'The Siege New', 'box_office': 1000}),
                            content_type='application/json')
        self.assertEqual(resp.status_code, 200)
        resp = self.app.get('/movies/The_Siege')
        self.assertEqual(resp.status_code, 400)
        resp = self.app.get('/movies/The_Siege_New')
        self.assertEqual(resp.status_code, 200)
        data = json.loads(resp.data)
        self.assertEqual(data['box_office'], 1000)

        # Tests DELETE
        resp = self.app.delete('/movies/The_Siege_New')
        self.assertEqual(resp.status_code, 200)
        resp = self.app.get('/movies/The_Siege_New')
        self.assertEqual(resp.status_code, 400)

    def test_handle_actors_by_attr(self):
        # Tests POST and simple GET
        resp = self.app.post('/actors',
                             data=json.dumps({'name': 'Hongyu Wang', 'age': 61}),
                             content_type='application/json')
        self.assertEqual(resp.status_code, 201)
        resp = self.app.get('/actors?name=Hongyu&age=61')
        self.assertEqual(resp.status_code, 200)
        data = json.loads(resp.data)
        self.assertEqual(len(data), 1)
        self.assertEqual(data[0]['age'], 61)

        # Tests and or
        resp = self.app.get('/actors?name=Bruce|name=Hongyu&age=61')
        self.assertEqual(resp.status_code, 200)
        data = json.loads(resp.data)
        self.assertEqual(len(data), 2)

        # Tests Non-existent field
        resp = self.app.get('/actors?box_office=0')
        self.assertEqual(resp.status_code, 200)
        data = json.loads(resp.data)
        self.assertEqual(len(data), 0)

        # Tests Bad Request
        resp = self.app.get('/actors?name=Bruce|nameHongyu')
        self.assertEqual(resp.status_code, 400)

    def test_handle_movies_by_attr(self):
        # Tests POST and simple GET
        resp = self.app.post('/movies',
                             data=json.dumps({'name': 'The CS 242', 'box_office': 672}),
                             content_type='application/json')
        self.assertEqual(resp.status_code, 201)
        resp = self.app.get('/movies?name=CS&box_office=672')
        self.assertEqual(resp.status_code, 200)
        data = json.loads(resp.data)
        self.assertEqual(len(data), 1)
        self.assertEqual(data[0]['box_office'], 672)

        # Tests and or
        resp = self.app.get('/movies?name=of|name=The&box_office=672')
        self.assertEqual(resp.status_code, 200)
        data = json.loads(resp.data)
        self.assertEqual(len(data), 2)

        # Tests Non-existent field
        resp = self.app.get('/movies?age=0')
        self.assertEqual(resp.status_code, 200)
        data = json.loads(resp.data)
        self.assertEqual(len(data), 0)

        # Tests Bad Request
        resp = self.app.get('/movies?name=of|nameThe')
        self.assertEqual(resp.status_code, 400)
