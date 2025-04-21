import pickle
import os
import shutil
from Map2Graphs import ShowGraphs
from GraphGenerator import JavaParseGraph

class GraphUsage:
    def __init__(self, graph_generator, graph_path=None):
        self.graph_generator=graph_generator
        self.graph_map=dict()
        path=graph_path
        if path is None:
            path=graph_generator.final_output_path
        with open(path,'rb') as f:
             self.graph_map=pickle.load(f)

        self.graph_value_list=list(self.graph_map.values())
        self.graph_key_list=list(self.graph_map.keys())

    def plot_graph(self, graph_name=None):
        if graph_name is not None:
            graph=self.graph_map[graph_name]

        else:
            graph=self.graph_value_list[0]
            graph_name=self.graph_key_list[0]

        ShowGraphs.show_graph_pyvis_big_nodes(graph)
        current_dir=os.path.dirname(os.path.abspath(__file__))
        relative_dir=os.path.join('Map2Graphs','FDG', 'MyGraph Z.html')
        source_file=os.path.join(current_dir,relative_dir)

        destination_folder=os.path.join(current_dir, 'PlottedGraphs')
        os.makedirs(destination_folder, exist_ok=True)

        destination=os.path.join(destination_folder,f'{graph_name}.html')

        shutil.move(source_file,destination)
