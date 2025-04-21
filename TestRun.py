from GraphUsage import GraphUsage
from GraphGenerator import JavaParseGraph
import os

graph_gen=JavaParseGraph(['SingleProgram'])
graph_gen.parse_tree_pickle_generator()
graph_user=GraphUsage(graph_gen)
graph_user.plot_graph()