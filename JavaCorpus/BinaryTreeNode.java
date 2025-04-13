package com.benlakey.examples.datastructures;

public class BinaryTreeNode<TKey extends Comparable<TKey>, TValue> {
	
	private TKey key;
	private TValue value;
	private BinaryTreeNode<TKey, TValue> left;
	private BinaryTreeNode<TKey, TValue> right;
	
	public BinaryTreeNode(TKey key, TValue value) {
		this.key = key;
		this.value = value;
	}

	public TKey getKey() {
		return this.key;
	}

	public void setKey(TKey key) {
		this.key = key;
	}

	public BinaryTreeNode<TKey, TValue> getLeft() {
		return this.left;
	}

	public void setLeft(BinaryTreeNode<TKey, TValue> left) {
		this.left = left;
	}

	public BinaryTreeNode<TKey, TValue> getRight() {
		return this.right;
	}

	public void setRight(BinaryTreeNode<TKey, TValue> right) {
		this.right = right;
	}

	public TValue getValue() {
		return value;
	}

	public void setValue(TValue value) {
		this.value = value;
	}
	
}