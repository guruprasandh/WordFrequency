package collections;

import java.util.HashMap;

public class Node {
	private char currChar;
	private int count = 0;
	private HashMap<Character, Node> children = new HashMap<Character, Node>();

	public char getCurrChar() {
		return currChar;
	}

	public void setCurrChar(char c) {
		this.currChar = c;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Node() {	
	}
	
	public Node(Character c) {
		this.currChar = c;
	}
	
	public HashMap<Character, Node> getChildren() {
		return children;
	}

	public void add(char c) {
		this.children.put(c, new Node(c));
	}
	
	public boolean isLeaf() {
		return count > 0;
	}
	
}
