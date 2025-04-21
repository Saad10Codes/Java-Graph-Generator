import sys
import networkx as nx
from antlr4 import *
from antlr4.InputStream import InputStream
import inspect
import pickle
import os

from JavaLexer import JavaLexer
from JavaParser import JavaParser
from JavaParserListener import JavaParserListener
from JavaParserVisitor import JavaParserVisitor


class CustomListener(JavaParserListener):
    def __init__(self):
        self.stack = []
        self.identifier_table = []
        self.graph = nx.MultiDiGraph()
        self.graph.graph['Graphid'] = 'MyGraph'
        self.keyword_dict = ["for", "if", "else", "while", "switch", "try", "catch", "finally"]
        self.keyword_dict = {keyword: 0 for keyword in self.keyword_dict}
        self.entered_class_flag = False
        # print(self.keyword_dict)

    def isInsideContext(self, ctx, ancestor):
        # Traverse upwards in the parse tree to check if a for statement is an ancestor
        parent = ctx.parentCtx
        while parent is not None:
            if isinstance(parent, JavaParser.ForInitContext):
                return True
            parent = parent.parentCtx
        return False

    def enterCompilationUnit(self, ctx: JavaParser.CompilationUnitContext):
        self.stack.append('compilation_unit')

    def enterClassDeclaration(self, ctx: JavaParser.ClassDeclarationContext):
        class_name = 'CLASS:' + ctx.children[1].getText()
        self.stack.append(class_name)
        self.graph.add_node(class_name)
        self.entered_class_flag = True

    def enterMethodDeclaration(self, ctx: JavaParser.MethodDeclarationContext):
        # print("Enter method")
        for child in ctx.children:
            if isinstance(child, JavaParser.IdentifierContext):
                method_name = 'METHOD:' + child.getText()
                self.stack.append(method_name)
                self.graph.add_node(method_name, label=method_name)
                self.graph.add_edge(self.stack[-2], method_name, label='contains')
                # print("APPENDING METHOD AND CLASS",self.stack[-1],child.getText())
                print(self.stack)

    # Exit a parse tree produced by JavaParser#methodDeclaration.
    # def exitMethodDeclaration(self, ctx:JavaParser.MethodDeclarationContext):
    # print("exit method declaration")

    # Enter a parse tree produced by JavaParser#methodBody.
    # def enterMethodBody(self, ctx:JavaParser.MethodBodyContext):
    # print("enter method body")

    # Exit a parse tree produced by JavaParser#methodBody.
    # def exitMethodBody(self, ctx:JavaParser.MethodBodyContext):
    # print("exit method body")

    # ## Statement is a child of blockStatement

    # Enter a parse tree produced by JavaParser#blockStatement.
    def enterStatement(self, ctx: JavaParser.BlockStatementContext):
        # print("Entering statement")
        for child in ctx.children:
            if child.getText() in ["for", "if", "else", "while", "switch", "try", "catch", "finally"]:
                self.keyword_dict[child.getText()] += 1
                block_node = child.getText() + '_' + str(self.keyword_dict[child.getText()])
                self.graph.add_edge(self.stack[-1], block_node, label="contains")
                self.stack.append(block_node)
                self.graph.nodes[block_node]['label'] = block_node

    # Exit a parse tree produced by JavaParser#blockStatement.
    # def exitStatement(self, ctx:JavaParser.BlockStatementContext):
    # print("Exiting statement")

    def enterBlock(self, ctx: JavaParser.BlockContext):
        print(self.stack[-1], "block entered")

    def enterForControl(self, ctx: JavaParser.ForControlContext):
        if isinstance(ctx.children[0], JavaParser.EnhancedForControlContext):
            self.graph.add_edge(self.stack[-1], 'enhanced_for', label='condition')
        else:
            if len(ctx.children) >= 3:
                self.graph.add_edge(self.stack[-1], ctx.children[2].getText(), label='condition')
                self.graph.nodes[ctx.children[2].getText()]['label'] = ctx.children[2].getText()

    def enterExpression(self, ctx: JavaParser.ExpressionContext):
        new_node = ''
        if hasattr(ctx, 'bop') and ctx.bop != None:
            # print("The bop of this expression is",ctx.bop.text, ctx.getText())
            if ctx.bop.text == '=':
                new_node = 'assign::' + ctx.children[0].getText() + ':' + ctx.children[2].getText()
            if ctx.bop.text == '+=':
                new_node = 'increment::' + ctx.children[0].getText() + ':' + ctx.children[2].getText()
            if ctx.bop.text == '-=':
                new_node = 'decrement::' + ctx.children[0].getText() + ':' + ctx.children[2].getText()
            if ctx.bop.text == '*=':
                new_node = 'multiply::' + ctx.children[0].getText() + ':' + ctx.children[2].getText()
            if ctx.bop.text == '/=':
                new_node = 'divide::' + ctx.children[0].getText() + ':' + ctx.children[2].getText()
            if ctx.bop.text == '%=':
                new_node = 'mod::' + ctx.children[0].getText() + ':' + ctx.children[2].getText()
            if ctx.bop.text == '==':
                new_node = 'compareEqualTo::' + ctx.children[0].getText() + ':' + ctx.children[2].getText()
            if ctx.bop.text == '<=':
                new_node = 'compareLessThanOrEqual::' + ctx.children[0].getText() + ':' + ctx.children[2].getText()
            if ctx.bop.text == '<':
                new_node = 'compareLessThan::' + ctx.children[0].getText() + ':' + ctx.children[2].getText()
            if ctx.bop.text == '>=':
                new_node = 'compareGreaterThanOrEqual::' + ctx.children[0].getText() + ':' + ctx.children[2].getText()
            if ctx.bop.text == '>':
                new_node = 'compareGreaterThanOrEqual::' + ctx.children[0].getText() + ':' + ctx.children[2].getText()

            if new_node != '':
                self.graph.add_edge(self.stack[-1], new_node, label='contains')
                self.graph.nodes[new_node]['label'] = new_node

        if hasattr(ctx, 'postfix') and ctx.postfix != None:
            if ctx.postfix.text == '++':
                new_node = 'increment::' + ctx.children[0].getText() + ':1'
            if ctx.postfix.text == '--':
                new_node = 'decrement::' + ctx.children[0].getText() + ':1'

            if new_node != '':
                self.graph.add_edge(self.stack[-1], new_node, label='contains')
                self.graph.nodes[new_node]['label'] = new_node

        if hasattr(ctx, 'prefix') and ctx.prefix != None:
            if ctx.prefix.text == '++':
                new_node = 'increment::' + ctx.children[0].getText() + ':1'
            if ctx.prefix.text == '--':
                new_node = 'decrement::' + ctx.children[0].getText() + ':1'
            if new_node != '':
                self.graph.add_edge(self.stack[-1], new_node, label='contains')
                self.graph.nodes[new_node]['label'] = new_node
        # if new_node!='':
        #     print('adding node new_node',new_node)

    def enterParExpression(self, ctx: JavaParser.ParExpressionContext):
        if ctx.parentCtx.children[0].getText() in ['if', 'while', 'switch', 'catch']:
            if (self.stack[-1] == 'else'):
                self.graph.add_edge(self.stack[-2], ctx.getText().lstrip('(').rstrip(')'), label='condition')
                self.graph.nodes[ctx.getText().lstrip('(').rstrip(')')]['label'] = ctx.getText().lstrip('(').rstrip(')')
                # print('adding node',ctx.getText().lstrip('(').rstrip(')'))
            else:
                self.graph.add_edge(self.stack[-1], ctx.getText().lstrip('(').rstrip(')'), label='condition')
                self.graph.nodes[ctx.getText().lstrip('(').rstrip(')')]['label'] = ctx.getText().lstrip('(').rstrip(')')
                # print('adding node',ctx.getText().lstrip('(').rstrip(')'))

    # Exit a parse tree produced by JavaParser#block.
    def exitBlock(self, ctx: JavaParser.BlockContext):
        if len(self.stack) > 2:
            self.stack.pop()

    def enterIdentifier(self, ctx: JavaParser.IdentifierContext):
        # if self.isInsideContext(ctx,JavaParser.ForInitContext):
        #     self.graph.add_edge(self.stack[-1],ctx.getText(),label='contains')

        flag = False
        j = 0
        # print(ctx.parentCtx.getText())
        if not isinstance(ctx.parentCtx, JavaParser.ClassDeclarationContext) and not isinstance(ctx.parentCtx,
                                                                                                JavaParser.MethodDeclarationContext):
            for i in self.identifier_table:
                if ctx.getText() == i:
                    flag = True
                    break
                j += 1

            if flag == False:
                self.identifier_table.append(ctx.getText())
                print(self.stack)
                if not self.graph.has_edge(self.stack[-1], ctx.getText()):
                    self.graph.add_edge(self.stack[-1], ctx.getText(), label='contains')
                    self.graph.nodes[ctx.getText()]['label'] = ctx.getText()
            else:
                if not self.graph.has_edge(self.stack[-1], self.identifier_table[j]):
                    self.graph.add_edge(self.stack[-1], self.identifier_table[j], label='contains')
                    self.graph.nodes[self.identifier_table[j]]['label'] = self.identifier_table[j]

    def enterLocalVariableDeclaration(self, ctx: JavaParser.LocalVariableDeclarationContext):
        # get the variable type
        var_type = ctx.children[0].getText()

        # list containing the list of variable declarator instances
        declarator_list = []

        # list containing the list of all variables with identifier and value if present
        var_list = []

        # append the child to declarator_list if it is a varibale declarator instance
        for child in ctx.children[1].children:
            if isinstance(child, JavaParser.VariableDeclaratorContext):
                declarator_list.append(child)

        for dec in declarator_list:
            # append the initial value of the variable if present else ignore.
            # attach the string with the var type
            if isinstance(dec.children[-1], JavaParser.VariableInitializerContext):
                var_list.append(dec.children[0].getText())
                # +'_'+var_type+':'+dec.children[-1].getText()
            else:
                var_list.append(dec.children[0].getText())
                # var_type+'_'+
        for var in var_list:
            self.graph.add_edge(self.stack[-1], var, label='contains')
            self.graph.nodes[var]['label'] = var
            self.identifier_table.append(var)
            # print('adding node',var)

    def enterFormalParameter(self, ctx: JavaParser.FormalParameterContext):
        var_type = ctx.children[0].getText()
        var_name = ctx.children[-1].getText()
        self.graph.add_edge(self.stack[-1], var_name, label='parameter')
        # +'_'+var_type
        self.identifier_table.append(var_name)
        self.graph.nodes[var_name]['label'] = var_name
        # print('adding node',var_name)

    def enterMethodCall(self, ctx: JavaParser.MethodCallContext):
        called_method = ctx.children[0].getText()
        if '.' in ctx.parentCtx.getText():
            called_method += '_from:' + ctx.parentCtx.children[0].getText()
        self.graph.add_edge(self.stack[-1], called_method, label='contains')
        self.graph.nodes[called_method]['label'] = called_method
        print('adding node', called_method)
        # print("METHOD CALL!!!!!!",ctx.children[1].getText(),ctx.children[1].children[1].getText())
        arguments = ctx.children[1].children[1].getText().split(',')
        for arg in arguments:
            self.graph.add_edge(called_method, arg, label='parameters')
            self.graph.nodes[arg]['label'] = arg
            print('adding node', arg)



