import java.util.HashMap;
import java.util.LinkedList;

import edu.princeton.cs.algs4.Bag;

/**
 * Use 26-way tries. based on ver 0.7, Using Node for DFS search: for every
 * recursive call, instead of starting all over again from root new version of
 * DFS continues searching from current node. Search ends till a null node is
 * hit.
 * 
 * Note: Need to check if node is NULL or MARKED first in DFS since node with Q
 * need to go one step further to node U if Node Q itself is already NULL,
 * node.next[] will throw a NullPointerException after Node Q is update to Node
 * U, a second NULL check is a must. Average run-time of 10000 random board is
 * 1.05 (s).
 * 
 * @sean @1.0
 */
public class BoggleSolver {
	private Node root;
	private BoggleBoard board;
	private boolean[][] marked;
	private int col, row;
	private HashMap<String, Integer> st;

	private Bag<String> result;
	private LinkedList<Node> nodeToReset;

	// Initializes the data structure using the given array of strings as the
	// dictionary.
	// (You can assume each word in the dictionary contains only the uppercase
	// letters A through Z.)
	public BoggleSolver(String[] dictionary) {
		root = new Node();
		st = new HashMap<String, Integer>();

		for (int i = 0; i < dictionary.length; i++) {
			put(dictionary[i]);
			st.put(dictionary[i], i);
		}

	}

	private static class Node {
		private int val = 0;
		private Node[] next = new Node[26];
	}

	private void put(String key) {
		root = put(root, key, 0);
	}

	private Node put(Node x, String key, int d) {
		if (x == null)
			x = new Node();
		if (d == key.length()) {
			x.val = 1;
			return x;
		}
		int c = key.charAt(d) - 'A';
		x.next[c] = put(x.next[c], key, d + 1);
		return x;
	}

	// Returns the set of all valid words in the given Boggle board, as an Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		this.board = board;
		this.row = board.rows();
		this.col = board.cols();

		marked = new boolean[row][col];
		result = new Bag<>();
		nodeToReset = new LinkedList<>();

		for (int i = 0; i < row; i++)
			for (int j = 0; j < col; j++) {

				dfs(root.next[board.getLetter(i, j) - 'A'], i, j, "");
			}

		for (Node n : nodeToReset)
			n.val = 1;

		return result;
	}

	private void dfs(Node node, int r, int c, String curWord) {
		if (node == null || marked[r][c])
			return; // check if next node in tries is null

		char curChar = board.getLetter(r, c);

		if (curChar == 'Q')
			node = node.next['U' - 'A'];
		if (node == null)
			return; // second check since node of 'Q' is moved onto node 'U'

		if (curChar == 'Q')
			curWord += "QU";
		else
			curWord += curChar;

		marked[r][c] = true;

		if (node.val == 1) {
			nodeToReset.add(node);
			node.val = 2;
			if (curWord.length() > 2)
				result.add(curWord);
		}

		if (r - 1 >= 0 && c - 1 >= 0)
			dfs(node.next[board.getLetter(r - 1, c - 1) - 'A'], r - 1, c - 1, curWord);
		if (r - 1 >= 0)
			dfs(node.next[board.getLetter(r - 1, c) - 'A'], r - 1, c, curWord);
		if (r - 1 >= 0 && c + 1 < col)
			dfs(node.next[board.getLetter(r - 1, c + 1) - 'A'], r - 1, c + 1, curWord);
		if (c - 1 >= 0)
			dfs(node.next[board.getLetter(r, c - 1) - 'A'], r, c - 1, curWord);
		if (c + 1 < col)
			dfs(node.next[board.getLetter(r, c + 1) - 'A'], r, c + 1, curWord);
		if (r + 1 < row && c - 1 >= 0)
			dfs(node.next[board.getLetter(r + 1, c - 1) - 'A'], r + 1, c - 1, curWord);
		if (r + 1 < row)
			dfs(node.next[board.getLetter(r + 1, c) - 'A'], r + 1, c, curWord);
		if (r + 1 < row && c + 1 < col)
			dfs(node.next[board.getLetter(r + 1, c + 1) - 'A'], r + 1, c + 1, curWord);

		marked[r][c] = false;
		return;
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

}
