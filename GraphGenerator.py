import sys
import zipfile
from zipfile import ZipFile

import networkx as nx
from antlr4 import *
import pickle
import os
from antlr4.FileStream import FileStream

from CustomListener import CustomListener
from JavaLexer import JavaLexer
from JavaParser import JavaParser
from JavaParserVisitor import JavaParserVisitor

class JavaParseGraph(JavaParserVisitor):

    def __init__(self ,directories, output_path="GeneratedGraphs"):
        self.directories =directories
        self.locations=self.list_java_files(self.directories)
        self.graphs =dict()
        script_dir= os.path.dirname(os.path.abspath(__file__))
        self.final_output_path=os.path.join(script_dir,f"{output_path}.pkl")

    def list_java_files(self ,directories):
        java_files=[]
        keys=[]
        for dir_name in directories:
            directory=dir_name
            if zipfile.is_zipfile(dir_name):
                base_name=os.path.splitext(os.path.basename(dir_name))[0]
                directory=os.path.join(os.path.dirname(dir_name),base_name)
                with zipfile.ZipFile(dir_name,"r") as zf:
                    zf.extractall(directory)
            for f in os.listdir(directory):
                if os.path.isfile(os.path.join(directory, f)) and f.endswith('.java'):
                    key=os.path.splitext(f)[0]
                    java_files.append((key,os.path.join(directory, f)))

        return java_files


    def parse_tree_generator(self):
        for key,location in self.locations:
            try:
                print('USING', location)
                input_code = FileStream(location, encoding='utf-8')  # Specify encoding to handle non-ASCII characters
                lexer = JavaLexer(input_code)
                stream = CommonTokenStream(lexer)
                parser = JavaParser(stream)
                tree = parser.compilationUnit()
                # print(tree.toStringTree(recog=parser))

                walker = ParseTreeWalker()
                customWalker = CustomListener()
                walker.walk(customWalker, tree)

                self.graphs[key]=customWalker.graph #Using graph as a dictionary
                # self.graphs.append(customWalker.graph) #Using graph as a list
                # print(customWalker.stack, list(customWalker.graph.edges(data=True)))
                # print(customWalker.identifier_table)

            except UnicodeDecodeError as e:
                print(f"UnicodeDecodeError: {e}. Skipping file {location}.")
            except Exception as e:
                print(f"An error occurred: {e}. Skipping file {location}.")
        print('All graphs have been generated')
        return self.graphs

    def parse_tree_pickle_generator(self):
        graphs=self.parse_tree_generator()
        with open(self.final_output_path, "wb") as fout:
            pickle.dump(graphs,fout)



