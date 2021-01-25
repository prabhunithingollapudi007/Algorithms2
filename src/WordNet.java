import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

public class WordNet {

	// private variables
	private Digraph digraph;
	private SAP sap;
	private ST<Integer, String> st; // contains id and rest of string
	private ST<String, Bag<Integer>> rst; // contains noun and synonyms

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null)
			throw new IllegalArgumentException();

		st = new ST<>();
		rst = new ST<>();

		// building synsets
		In in = new In(synsets);
		for (String line : in.readAllLines()) {
			if (line == null)
				continue;

			List<String> splitted = Arrays.asList(line.split(","));
			Integer synsetIndex = Integer.parseInt(splitted.get(0));
			st.put(synsetIndex, splitted.get(1) + ", " + splitted.get(2));
			// added to id table

			//adding to nouns table
			String synset = splitted.get(1);
			String[] synsetSynonyms = synset.split(" ");
			for(int i = 0; i < synsetSynonyms.length; i++) {
				synsetSynonyms[i] = synsetSynonyms[i].trim();
				Bag<Integer> tempBag = rst.get(synsetSynonyms[i]);
				if(tempBag == null) {
					tempBag = new Bag<Integer>();
					tempBag.add(synsetIndex);
					rst.put(synsetSynonyms[i], tempBag);
				}
				else
					tempBag.add(synsetIndex);
			}

		}

		// creating digraph and adding hypernyms
		in = new In(hypernyms);
		digraph = new Digraph(st.size());

		for (String line : in.readAllLines()) {
			if (line == null)
				continue;
			List<String> splitted = Arrays.asList(line.split(","));
			int synsetIndex = Integer.parseInt(splitted.get(0));
			for (int i = 1; i < splitted.size(); i++) {
				digraph.addEdge(synsetIndex, Integer.parseInt(splitted.get(i).trim()));
			}
		}

		DirectedCycle directedCycle = new DirectedCycle(digraph);
		if (directedCycle.hasCycle())
			throw new IllegalArgumentException();
		sap = new SAP(digraph);
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return rst.keys();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		if (word == null)
			throw new IllegalArgumentException();
		return rst.contains(word);

	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {

		if (nounA == null || nounB == null)
			throw new IllegalArgumentException();
		if ((!isNoun(nounA)) || (!isNoun(nounB)))
			throw new IllegalArgumentException();

		Bag<Integer> nounAIndex = rst.get(nounA);
		Bag<Integer> nounBIndex = rst.get(nounB);

		return sap.length(nounAIndex, nounBIndex);

	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA
	// and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (nounA == null || nounB == null)
			throw new IllegalArgumentException();
		if ((!rst.contains(nounA)) || (!rst.contains(nounB)))
			throw new IllegalArgumentException();

		Bag<Integer> nounAIndex = rst.get(nounA);
		Bag<Integer> nounBIndex = rst.get(nounB);

		return st.get(sap.ancestor(nounAIndex, nounBIndex));

	}

	// do unit testing of this class
	public static void main(String[] args) {

	}

}
