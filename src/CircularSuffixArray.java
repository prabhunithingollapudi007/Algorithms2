import java.util.Arrays;

/**
 * 
 */

/**
 * @author Nithin
 *
 */
public class CircularSuffixArray {

	private final String s;
	private String[] sufArray;
	private String[] sorted;
	private final int len;
	// circular suffix array of s
	public CircularSuffixArray(String s) {
		if (s == null)
			throw new IllegalArgumentException();
		this.s = s;
		this.len = s.length();
		this.sufArray = new String[len];
		this.sorted = new String[len];
		fillSuffixArray();
	}

	private void fillSuffixArray() {
		for(int i = 0; i < len; i++) {
			sufArray[i] = s.substring(i	, len) + s.substring(0, i);
			sorted[i] = s.substring(i	, len) + s.substring(0, i);
		}
		Arrays.sort(sorted);
	}
	
	// length of s
	public int length() {
		return len;

	}

	// returns index of ith sorted suffix
	public int index(int i) {
		if (i < 0 || i >= len)
			throw new IllegalArgumentException();
		for(int j = 0; j < len; j++) {
			if(sorted[i].equals(sufArray[j]))
				return j;
		}
		return 0;
	}

	// unit testing (required)
	public static void main(String[] args) {
		/*
		 * CircularSuffixArray c = new CircularSuffixArray("test"); assert c.length() !=
		 * 4 : "wrong output";
		 */
	}

	@Override
	public String toString() {
		return "CircularSuffixArray [s=" + s + ", sufArray=" + Arrays.toString(sufArray) + ", len=" + len + "]";
	}

}
