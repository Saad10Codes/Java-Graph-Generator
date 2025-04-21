from GraphUsage import GraphUsage
from GraphGenerator import GraphGenerator
import os

graph_gen=GraphGenerator('SingleProgram')
graph_gen.parse_tree_pickle_generator()
graph_user=GraphUsage(graph_gen)
graph_user.plot_graph()