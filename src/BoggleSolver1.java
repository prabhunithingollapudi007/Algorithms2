import java.util.ArrayList;
import java.util.HashMap;

import edu.princeton.cs.algs4.Bag;

public class BoggleSolver1 {

	private HashMap<String, Integer> st;
	private ArrayList<Pair>[] charLkp;

	// (You can assume each word in the dictionary contains only the uppercase
	// letters A through Z.)
	@SuppressWarnings("unchecked")
	public BoggleSolver1(String[] dictionary) {
		st = new HashMap<String, Integer>();

		charLkp = (ArrayList<BoggleSolver1.Pair>[]) new ArrayList[26];
		for (int i = 0; i < 26; i++)
			charLkp[i] = new ArrayList<Pair>();
		int i = 0;
		for (String word : dictionary) {
			st.put(word, i++);
		}
	}

	// Returns the set of all valid words in the given Boggle board, as an Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		Bag<String> validWords = new Bag<String>();
		int rows = board.rows(), cols = board.cols();
		// fill look up for board
		fillLkp(board);

		for (String word : st.keySet()) {
			if (isValidWord(word, rows, cols)) {
				validWords.add(word);
			}
		}

		return validWords;
	}

	private boolean isValidWord(String word, int m, int n) {
		int l = 0;
		boolean retFlag = false;
		boolean[][] visited = new boolean[m][n];
		boolean nothing = true;
		for (int i = 0; i < word.length(); i++) {
			if (nothing)
				break;
			for (Pair b : charLkp[(int) word.charAt(i)]) {

				if (visited[b.i][b.j])
					continue;
				
				
				
				visited[b.i][b.j] = true;
				nothing = false;
			}
		}
		if (l < 3)
			return false;
		return retFlag;
	}


	private void fillLkp(BoggleBoard board) {

		ArrayList<Pair> temp = new ArrayList<>();
		for (int i = 0; i < board.rows(); i++) {
			for (int j = 0; j < board.cols(); j++) {
				int c = (int) board.getLetter(i, j) - 65;
				temp = charLkp[c];
				temp.add(new Pair(i, j));
				charLkp[c] = temp;
			}
		}

	}

	// Returns the score of the given word if it is in the dictionary, zero
	// otherwise.
	// (You can assume the word contains only the uppercase letters A through Z.)
	public int scoreOf(String word) {

		if (word == null || word.length() < 3 || !st.containsKey(word))
			return 0;
		int le = word.length();
		if (le == 3 || le == 4)
			return 1;
		else if (le == 5)
			return 2;
		else if (le == 6)
			return 3;
		else if (le == 7)
			return 5;
		else
			return 11;

	}

	private class Pair {
		public int i, j;

		public Pair(int i, int j) {
			this.i = i;
			this.j = j;
		}

		@Override
		public String toString() {
			return "Pair [i=" + i + ", j=" + j + "]";
		}

	}
}
