import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

import misc.*;
import collections.WordTrie;

public class TestTrie {

	static int size = 4;
	public static void main(String args[]){
		WordTrie myTrie = new WordTrie();
		myTrie.add("there");
		myTrie.add("their");
		myTrie.add("the");
		myTrie.add("an");
		myTrie.add("answer");
		myTrie.add("the");
		myTrie.add("an");
		myTrie.add("any");
		myTrie.add("ant");
		myTrie.add("bye");
		myTrie.add("by");
		myTrie.add("by");
		myTrie.add("an");
		
		ArrayList<WordCountValue> words = myTrie.fetchAllWords();
		for (WordCountValue word : words) {
			System.out.println(word.getWord() + " - " + word.getCount());
		}
		
		PriorityQueue<WordCountValue> minHeap = new PriorityQueue<WordCountValue>(size, new CountComparator());
		for (WordCountValue word : words) {
			if (minHeap.size() == size && minHeap.peek().getCount() < word.getCount()) {
				minHeap.poll();
			}
			if (minHeap.size() < size) {
				minHeap.add(word);
			}
		}
		
		WordCountValue[] topWords = new WordCountValue[size];
		minHeap.toArray(topWords);
		Arrays.sort(topWords, new CountComparator(true));
		for (WordCountValue word : topWords) {
			System.out.println(word.getWord() + " - " + word.getCount());
		}
	}
	
}
