
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {

	final private Digraph digraph;
	private int ancestor;

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		if (G == null)
			throw new IllegalArgumentException();
		digraph = G;

	}
	
	private void check(int v) {
		if(v < 0 || v >= digraph.V())
			throw new IllegalArgumentException();
		if(Integer.valueOf(v) == null)
			throw new IllegalArgumentException();
	}
	
	private void check(int v, int w) {
		if(v < 0 || v >= digraph.V())
			throw new IllegalArgumentException();
		if(w < 0 || w >= digraph.V())
			throw new IllegalArgumentException();
		if(Integer.valueOf(v) == null)
			throw new IllegalArgumentException();
		if(Integer.valueOf(w) == null)
			throw new IllegalArgumentException();
	}
	
	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {

		check(v, w);
		ancestor = -1;
		BreadthFirstDirectedPaths pathsv = new BreadthFirstDirectedPaths(digraph, v);
		BreadthFirstDirectedPaths pathsw = new BreadthFirstDirectedPaths(digraph, w);
		
		if(v == w) {
			ancestor = v;
			return 0;
		}
		
		int MINPATH = Integer.MAX_VALUE;
		
		for(int i = 0; i < digraph.V(); i++) {
			if(pathsv.hasPathTo(i) && pathsw.hasPathTo(i)){
       		 int dist = pathsv.distTo(i) + pathsw.distTo(i);
       		 if (dist < MINPATH) { 
       			 MINPATH = dist;
       			 ancestor = i;
       		 }
       	 }	
		}
		
		return MINPATH == Integer.MAX_VALUE ? -1 : MINPATH;
		
	}

	// a common ancestor of v and w that participates in a shortest ancestral path;
	// -1 if no such path
	public int ancestor(int v, int w) {
		length(v, w);
		return ancestor;
	}

	

	// length of shortest ancestral path between any vertex in v and any vertex in
	// w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null)
			throw new IllegalArgumentException();
		for(int i: v) 
			check(i);
		for(int i: w) 
			check(i);
		ancestor = -1;
		BreadthFirstDirectedPaths pathsv = new BreadthFirstDirectedPaths(digraph, v);
		BreadthFirstDirectedPaths pathsw = new BreadthFirstDirectedPaths(digraph, w);
		
		int MINPATH = Integer.MAX_VALUE;
		
		for(int i = 0; i < digraph.V(); i++) {
			if(pathsv.hasPathTo(i) && pathsw.hasPathTo(i)){
       		 int dist = pathsv.distTo(i) + pathsw.distTo(i);
       		 if (dist < MINPATH) { 
       			 MINPATH = dist;
       			 ancestor = i;
       		 }
       	 }	
		}
		
		return MINPATH == Integer.MAX_VALUE ? -1 : MINPATH;
		
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such
	// path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		length(v, w);
		return ancestor;

	}

	// do unit testing of this class
	public static void main(String[] args) {

	}

}