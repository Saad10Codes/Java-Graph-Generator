# Java-Graph-Generator
Uses Antlr and Python to parse Java and generate code graphs

## Framework
Antlr returns a Java parser and lexer written in Python along with a parser visitor 
and a parser listener. both visitor and listener can be used to parse the tree with different uses. 
The listener just responds to events when a rule is being entered or exited while the visitor gives 
more control to the user over the tree walking logic. I use listener here since I don't need control 
over the parsing logic and the listener will make sure every node is visited so I don't need to worry 
about walking the tree. 

##### Sample function for visitor 
```python
    def visitCompilationUnit(self, ctx:JavaParser.CompilationUnitContext):
        return self.visitChildren(ctx)
```

##### Sample function for listener
```python
    def enterCompilationUnit(self, ctx:JavaParser.CompilationUnitContext):
        pass
```
Both the classes can be extended and overwritten but visitor gives more control 
by letting the user decide by calling ``` visitChildren(ctx)``` function or visit a 
sibling or parent. 

#### Custom Listener

The ``` CustomListener ``` class extends the ``` JavaParserListener ```
and overrides the skeleton functions to perform operations when the listener 
responds to an event. 
There are different functions triggered when the walker enters a block or a statement. 
These functions then return the current context ``` ctx ``` that has been entered by the 
walker. Similarly, there are also functions which return when a ``` ctx ``` has been exited 
by the walker. 

Now I maintain a stack that stores the current context before moving into another to keep 
track of the hierarchy. As the nodes are visited the relationships are made based on the current
context and the parent context. This helps also in representing a single entity only once
in the graphs even if the entity is used in different hierarchies. 

### GraphGenerator 
The graph generator is where the action happens. It takes as input, directory locations, traverses the list, 
checks for any zip files, unzips them and then generates a list of java files and their names. This list is 
later used to get access to each file, parse the java code and generate a parse tree and then walk the tree 
to generate the graphs and store them as a dictionary, with the file name serving as key. There is also a function 
to store the graphs as a pickle file for persistent storage. 
These graphs later, can be used for any purpose as the generation and storage is completely decoupled but **Diarmuid O'Donoghue's** Map2Graphs
can be used to draw inference from the graphs, like comparison of the graphs or generation of CSV of similarity between the graphs or even
generation of a dynamic HTML representation of the graphs. Below is an example code along with its HTML. 
(Note: GitHub readme doesn't allow dynamic HTML, so I will drop a link to it)

```java
class pattern
{
    public static void main(String[] args)
  {
    int size = 0;
        Character c;
        System.out.println();
        size = 5;
        int i, j, k;
        for (i = 0; i < size + 1; i++) { for (j = size; j > i; j--) {
                System.out.print(" ");
            }
            for (k = 0; k < (2 * i - 1); k++) {
                System.out.print("*");
            }
            System.out.println();
          }
    }
    }
```

![Graph representation for the code](./PlottedGraphs/java_program_to_print_the_following_pattern_on_the_console_12.html)


[Class Diagram](./files/JavaGraphGeneratorClassDiagram.drawio.svg)


