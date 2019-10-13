from movieGraph import MovieGraph, defaultdict
import matplotlib.pyplot as plt

mg = MovieGraph()
mg.custom_read_from_scraped_data('data/data.json')


def analyze_hub():
    dict_actors_connected = defaultdict(int)
    for actor_name, actor_node in mg.actors.items():
        for movie_node in mg.graph.adjacent_nodes(actor_node):
            num_actors_connected = len(mg.graph.adjacent_nodes(movie_node)) - 1
            dict_actors_connected[actor_name] += num_actors_connected
    res = sorted(dict_actors_connected.items(), key=lambda kv: kv[1], reverse=True)
    print(res)
    print('Top 5 Hub Actors:', res[:5])

    x_val = range(len(res))
    y_val = [x[1] for x in res]
    plt.bar(x_val, y_val)
    plt.show()


def analyze_age_group():
    grossing_of_age = defaultdict(int)
    for actor_name, actor_node in mg.actors.items():
        grossing_of_age[actor_node.attrs['age']] += actor_node.attrs['total_gross']
    res = sorted(grossing_of_age.items(), key=lambda kv: kv[1], reverse=True)
    print(res)
    print('Top 5 Age Groups:', res[:5])

    x_val = [x[0] for x in res]
    y_val = [x[1] for x in res]
    plt.bar(x_val, y_val)
    plt.title('Bar Chart: Age Group vs. Grossing Value')
    plt.xlabel('Age')
    plt.ylabel('Grossing Value')
    plt.show()
    plt.scatter(x_val, y_val)
    plt.title('Scatter Plot: Age Group vs. Grossing Value')
    plt.xlabel('Age')
    plt.ylabel('Grossing Value')
    plt.show()


if __name__ == '__main__':
    analyze_hub()
    analyze_age_group()
