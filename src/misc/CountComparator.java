package misc;

import java.util.Comparator;

public class CountComparator implements Comparator<WordCountValue> {
	boolean descending = false;
	
	public CountComparator() {
	}
	
	public CountComparator(boolean descending) {
		this.descending = descending;
	}
	
	@Override
	public int compare(WordCountValue o1, WordCountValue o2) {
		if (descending)
			return o2.getCount() - o1.getCount();
		return o1.getCount() - o2.getCount(); 
	}
}
