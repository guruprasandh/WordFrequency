package collections;

import java.util.ArrayList;
import java.util.HashMap;

import misc.WordCountValue;

public class WordTrie {
	private Node root; 
	
	public WordTrie() {
		root = new Node();
	}
	
	public Node getTrie() {
		return root;
	}

	public void add(String word) {
		Node root = this.root;
		HashMap<Character, Node> children = root.getChildren();
		
		for (int i = 0; i < word.length(); ++i) {
			char c = word.charAt(i);
			Node child;
			if (children.containsKey(c)) {
				child = children.get(c);
			} else {
				child = new Node(c);
				children.put(c, child);
			}
			
			children = child.getChildren();
			if (i == word.length()-1) {
				child.setCount(child.getCount()+1);
			}
		}
	}
	
	public ArrayList<WordCountValue> fetchAllWords() {
		Node root = this.root;
		ArrayList<WordCountValue> words = new ArrayList<WordCountValue>();
		
		dfs(root, "", words);
		return words;
	}
	
	public void dfs(Node node, String currWord, ArrayList<WordCountValue> words) {
		HashMap<Character, Node> children = node.getChildren();
		for (Node child : children.values()) {
			if (child.isLeaf()) {
				WordCountValue value = new WordCountValue(currWord + child.getCurrChar(), child.getCount());
				words.add(value);
			}
			dfs(child, currWord + child.getCurrChar(), words);
		}
	}
	
}
