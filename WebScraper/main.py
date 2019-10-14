from api import *

if __name__ == '__main__':
    # mg = MovieGraph()
    # mg.read_from_scraped_data("data/actors.json", "data/movies.json")
    # mg.graph.store_to_json("data/graph.json")
    # print(mg.get_grossing('Brubaker'))
    # print(mg.get_movies_of_actor('Morgan Freeman'))
    # print(mg.get_actors_of_movie('Brubaker'))
    # print(mg.get_top_x_grossing_actors(10))
    # print(mg.get_oldest_x_actors(10))
    # print(mg.get_movies_by_year(1980))
    # print(mg.get_actors_by_year(1937))

    set_up_data_source('data/data.json')
    app.run(debug=True)
