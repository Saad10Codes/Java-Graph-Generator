package com.benlakey.examples.datastructures;

public interface BinaryTreeNodeVisitor<TKey extends Comparable<TKey>, TValue> {

	void visit(BinaryTreeNode<TKey, TValue> node);
	
}
