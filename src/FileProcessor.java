import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

import misc.CountComparator;
import misc.WordCountValue;
import collections.WordTrie;

public class FileProcessor extends Thread {
	public static int processorCount = 4;
	public static int totalFrequentWords = 10; // Top 10 words.
	ArrayDeque<String> wordQueue;
	
	WordTrie trie;
	private int currWordCount = 0;
	private WordCountValue[] topWords;
	
	public void run() {
		trie = new WordTrie();
		wordQueue = new ArrayDeque<String>();
		while (true) {
			//System.out.println(Thread.currentThread().getName() + " -> Word size is " + wordQueue.size());
			if (wordQueue.size() > 0) {
				String word = wordQueue.pollFirst();
				//System.out.println(Thread.currentThread().getName() + " -> Current Word is " + word);
				if (word == null) continue;
				if (word.equals("$@$Done$@$")) {
					//System.out.println("Running preprocess for " + Thread.currentThread().getName());
					preProcess();
					break;
				}
				
				addWordToTrie(word);
			}
		}
	}
	
	public void addWordToQueue(String word) {
		wordQueue.add(word);
	}
	
	public void addWordToTrie(String word) {
		trie.add(word);
	}
	
	public synchronized void preProcess() {
		System.out.println("Pre process happening for " + Thread.currentThread().getName());
		ArrayList<WordCountValue> words = trie.fetchAllWords();
		PriorityQueue<WordCountValue> minHeap = new PriorityQueue<WordCountValue>(totalFrequentWords, new CountComparator());
		for (WordCountValue word : words) {
			addToHeap(minHeap, word);
		}
		
		topWords = new WordCountValue[totalFrequentWords];
		minHeap.toArray(topWords);
		Arrays.sort(topWords, new CountComparator(true));
		
		Thread t = Thread.currentThread();
		StringBuffer buffer = new StringBuffer();
		buffer.append(t.getName()).append("->").append(topWords.length).append(" ==> ");
		for(WordCountValue topWord : topWords) {
			buffer.append(topWord.getWord()).append(":").append(topWord.getCount()).append("  ::  ");
		}
		System.out.println(buffer.toString());
		Thread.currentThread().notifyAll();
		
	}
	
	public WordCountValue getNextFrequentWord() {
		if (topWords.length == 0 || currWordCount >= topWords.length) return null;
		WordCountValue value = topWords[currWordCount];
		++currWordCount;
		
		Thread t = Thread.currentThread();
		return value;
	}
	
	public static void main(String[] args) {
		//String fileLocation = "/Users/guru/Documents/Personal/Files/Sandeep-Reco.txt";
		String fileLocation = "/Users/guru/Documents/Personal/Study/Problems/Innovacx/Description.txt";
		BufferedReader reader = null;
		
		FileProcessor[] threads = new FileProcessor[processorCount];
		for (int i = 0; i < processorCount; ++i) {
			System.out.println("Init Thread " + i);
			FileProcessor thread = new FileProcessor();
			thread.start();
			while (thread.wordQueue == null) {
				System.out.println("Waiting for init to complete for " + i);
				continue;
			}
			threads[i] = thread;
		}
		
		try {
		    reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileLocation)));
		    for (String line; (line = reader.readLine()) != null;) {
		    	String[] words = line.split(" ");
		    	// MAP
		    	// Distribute the processing to individual threads(machines in a real world).
		    	for (String word : words) {
		    		int processor = Math.abs(word.hashCode())%processorCount;
		    		threads[processor].addWordToQueue(word);
		    	}
		    }
		    
		    for (FileProcessor thread : threads) {
		    	System.out.println(Thread.currentThread().getName() + " Adding done for " + thread.getName());
	    		thread.addWordToQueue("$@$Done$@$");
	    	}
		    
		    for (FileProcessor thread : threads) {
		    	System.out.println("Waiting on " + thread.getName());
		    	synchronized(FileProcessor.class) {
			    	while (thread.topWords == null) {
			    		try {
							Thread.currentThread().wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		/*try {
							sleep(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							continue;
						}*/
			    	}
		    	}
		    	System.out.println("Process finished " + thread.getName());
		    }
		    
		    // REDUCE
		    // Consolidate all frequent words from all threads.
	    	// Do merge sort on the top words in each thread.
		    PriorityQueue<WordCountValue> finalWords = new PriorityQueue<WordCountValue>(totalFrequentWords, new CountComparator());
	    	for (FileProcessor proc: threads) {
	    		// TODO: Further Optimization can be done.
	    		WordCountValue nextWord = null; 
	    		do {
	    			nextWord = proc.getNextFrequentWord();
		    		if (nextWord != null)
		    			addToHeap(finalWords, nextWord);
	    		} while (nextWord != null);
		    }
	    
		    // Finally print all the words in ascending order for readability purpose.
		    WordCountValue[] frequentWords = new WordCountValue[totalFrequentWords];
			finalWords.toArray(frequentWords);
			Arrays.sort(frequentWords, new CountComparator(true));
			System.out.println("Results are:");
			for (WordCountValue word : frequentWords) {
				System.out.println(word.getWord() + " : " + word.getCount() + "\n");
			}
			
		} catch(IOException e) {
			// Take appropriate exception.
		} finally {
		    //writer.close();
			close(reader);
		    
		}
	}

	private static void addToHeap(PriorityQueue<WordCountValue> minHeap, WordCountValue word) {
		if (minHeap.size() == totalFrequentWords && minHeap.peek().getCount() < word.getCount()) {
			minHeap.poll();
		}
		if (minHeap.size() < totalFrequentWords) {
			minHeap.add(word);
		}
	}
	
	private static void close(Closeable c) {
		try {
			c.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
