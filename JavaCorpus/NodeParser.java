package com.benlakey.examples.datastructures;

public interface NodeParser<TKey extends Comparable<TKey>, TValue> {

	BinaryTreeNode<TKey, TValue> parse(String nodeToken);
	
}
