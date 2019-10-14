from unittest import TestCase

from graph import Graph


class TestGraph(TestCase):
    def setUp(self):
        self.graph = Graph()
        self.n1 = self.graph.add_node({"name": 1})
        self.n2 = self.graph.add_node({"name": 2})
        self.n3 = self.graph.add_node({"name": 3})
        self.graph.add_edge(self.n1, self.n2, 4)
        self.assertEqual(3, len(self.graph.get_nodes()))
        self.assertEqual(1, len(self.graph.get_edges()))
        self.assertTupleEqual((True, 4), self.graph.get_edge_weight(self.n1, self.n2))
        self.assertEqual(1, len(self.graph.adjacent_nodes(self.n1)))
        self.assertEqual(0, len(self.graph.adjacent_nodes(self.n3)))

    def test_remove_node(self):
        self.assertTrue(self.graph.remove_node(self.n1))
        self.assertEqual(2, len(self.graph.get_nodes()))
        self.assertEqual(0, len(self.graph.get_edges()))
        self.assertFalse(self.graph.add_edge(self.n1, self.n2, 4))  # Adds edge with non-existent node

        self.assertFalse(self.graph.get_edge_weight(self.n1, self.n2)[0])
        self.assertFalse(self.graph.remove_node(self.n1))  # Cannot remove non-existent node

    def test_remove_edge(self):
        self.assertTrue(self.graph.remove_edge(self.n1, self.n2))
        self.assertEqual(3, len(self.graph.get_nodes()))
        self.assertEqual(0, len(self.graph.get_edges()))

        self.assertFalse(self.graph.get_edge_weight(self.n1, self.n2)[0])
        self.assertFalse(self.graph.remove_edge(self.n1, self.n2))  # Cannot remove non-existent edge

        self.assertTrue(self.graph.remove_node(self.n1))
        self.assertFalse(self.graph.remove_edge(self.n1, self.n2))

    def test_adjacent_nodes(self):
        self.assertTrue(self.graph.remove_node(self.n1))
        self.assertIsNone(self.graph.adjacent_nodes(self.n1))

    def test_encode_decode(self):
        self.assertTrue(self.graph.store_to_json("data/graph_temp.json"))
        self.assertTrue(self.graph.read_from_json_file("data/graph_temp.json"))
        self.assertEqual(3, len(self.graph.get_nodes()))
        self.assertEqual(1, len(self.graph.get_edges()))
        self.assertEqual(1, len(self.graph.adjacent_nodes(self.n1)))
        self.assertEqual(0, len(self.graph.adjacent_nodes(self.n3)))

        self.assertFalse(self.graph.read_from_json_file(""))
        self.assertFalse(self.graph.store_to_json(""))
        self.assertFalse(self.graph.read_from_json({}))
