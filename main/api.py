from flask import Flask, jsonify, request
from movieGraph import *


mg = MovieGraph()
app = Flask(__name__)


def set_up_data_source(file_name: str):
    """Sets up the data source from the given file

    Args:
        file_name: The file that stores actors and movies
    """
    mg.custom_read_from_scraped_data(file_name)


def check_if_node_satisfy(node: Node, cond: Tuple[str, str]) -> bool:
    """Checks if the node satisfy the given condition

    Args:
        node: The node for checking
        cond: The condition to check

    Returns:
        True if the node satisfy the given condition
    """
    if cond[0] not in node.attrs:
        return False
    if cond[0] == 'name':
        return cond[1] in node.attrs['name']
    else:
        return cond[1] == str(node.attrs[cond[0]])


def filter_nodes_by_attr(nodes: List[Node], args: dict) -> Optional[List[Node]]:
    """Filters nodes by their attributes

    NOTE: or binds more tightly than and

    Args:
        nodes: The node for filtering
        args: The args to filter

    Returns:
        The list of nodes that satisfies the conditions
    """
    list_cond_and = []
    for k, v in args.items():
        list_cond = str(v).split('|')
        list_cond_or = [(k, list_cond[0])]
        for cond_or in list_cond[1:]:
            pair = cond_or.split('=')
            if len(pair) != 2:
                return None
            list_cond_or.append((pair[0], pair[1]))
        list_cond_and.append(list_cond_or)

    res = nodes.values()
    for list_cond_or in list_cond_and:
        res = list(filter(lambda node: any([check_if_node_satisfy(node, cond) for cond in list_cond_or]), res))
    return res


def serialize_node(node: Node, attr_missing: str) -> Dict:
    """Serializes the given node into a dictionary

    Args:
        node: The node to serialize
        attr_missing: The attribute to add

    Returns:
        The dictionary after serialization
    """
    res = dict(node.attrs)
    lis = [node.attrs['name'] for node in mg.graph.adjacent_nodes(node)]
    res.update({attr_missing: lis})
    return res


@app.route('/actors', methods=['GET', 'POST', 'DELETE'])
def handle_actors_by_attr():
    """Handles actors by the given attribute(s)

    Returns:
        An HTTP response
    """
    resp = jsonify({'message': 'Bad Request'})
    resp.status_code = 400

    if request.method == 'POST':
        data = json.loads(request.data)
        if 'name' in data:
            mg.actors.update({data['name']: mg.graph.add_node(data)})
            resp = jsonify({'message': 'Actor Successfully Created'})
            resp.status_code = 201
    else:
        # Handles and (&) or (|) logic
        res = filter_nodes_by_attr(mg.actors, request.args)

        if res is not None:
            if request.method == 'GET':
                resp = jsonify([serialize_node(node, 'movies') for node in res])
            elif request.method == 'DELETE':
                for actor_node in res:
                    mg.graph.remove_node(actor_node)
                    mg.actors.pop(actor_node.attrs['name'])
                resp = jsonify({'message': 'Actors Successfully Deleted'})
            resp.status_code = 200

    return resp


@app.route('/movies', methods=['GET', 'POST', 'DELETE'])
def handle_movies_by_attr():
    """Handles movies by the given attribute(s)

    Returns:
        An HTTP response
    """
    resp = jsonify({'message': 'Bad Request'})
    resp.status_code = 400

    if request.method == 'POST':
        data = json.loads(request.data)
        if 'name' in data:
            mg.movies.update({data['name']: mg.graph.add_node(data)})
            resp = jsonify({'message': 'Movie Successfully Created'})
            resp.status_code = 201
    else:
        # Handles and (&) or (|) logic
        res = filter_nodes_by_attr(mg.movies, request.args)

        if res is not None:
            if request.method == 'GET':
                resp = jsonify([serialize_node(node, 'actors') for node in res])
            elif request.method == 'DELETE':
                for movie_node in res:
                    mg.graph.remove_node(movie_node)
                    mg.movies.pop(movie_node.attrs['name'])
                resp = jsonify({'message': 'Movies Successfully Deleted'})

    return resp


@app.route('/actors/<name>', methods=['GET', 'PUT', 'DELETE'])
def handle_actors_by_name(name):
    """Handles actors by the given name

    Args:
        name: The name to filter

    Returns:
        An HTTP response
    """
    name = name.replace('_', ' ')
    for actor_name, actor_node in mg.actors.items():
        if actor_name == name:
            res = {}
            if request.method == 'GET':
                res = serialize_node(actor_node, 'movies')
            elif request.method == 'DELETE':
                mg.graph.remove_node(actor_node)
                mg.actors.pop(actor_name)
                res.update({'message': 'Actor Successfully Deleted'})
            elif request.method == 'PUT':
                data = json.loads(request.data)
                actor_node.attrs.update(data)
                if 'name' in data:
                    mg.actors.update({data['name']: mg.actors.pop(name)})
                res.update({'message': 'Actor Successfully Updated'})
            resp = jsonify(res)
            resp.status_code = 200
            return resp
    resp = jsonify({'message': 'Actor Not Found'})
    resp.status_code = 400
    return resp


@app.route('/movies/<name>', methods=['GET', 'PUT', 'DELETE'])
def handle_movies_by_name(name):
    """Handles movies by the given name

    Args:
        name: The name to filter

    Returns:
        An HTTP response
    """
    name = name.replace('_', ' ')
    for movie_name, movie_node in mg.movies.items():
        if movie_name == name:
            res = {}
            if request.method == 'GET':
                res = serialize_node(movie_node, 'actors')
            elif request.method == 'DELETE':
                mg.graph.remove_node(movie_node)
                mg.movies.pop(movie_name)
                res.update({'message': 'Movie Successfully Deleted'})
            elif request.method == 'PUT':
                data = json.loads(request.data)
                movie_node.attrs.update(data)
                if 'name' in data:
                    mg.movies.update({data['name']: mg.movies.pop(name)})
                res.update({'message': 'Movie Successfully Updated'})
            resp = jsonify(res)
            resp.status_code = 200
            return resp
    resp = jsonify({'message': 'Movie Not Found'})
    resp.status_code = 400
    return resp
