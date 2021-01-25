
import java.util.Arrays;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;

public class BaseballElimination {

	private final int nOT;
	private final Team[] teams;
	private final ST<String, Integer> lkp;
	private final int last;
	private int cf;

	public BaseballElimination(String filename) {
		In in = new In(filename);
		this.nOT = in.readInt();
		teams = new Team[nOT];
		lkp = new ST<>();
		int t = 0;
		
		for (String line : in.readAllLines()) {
			if (line.length() == 0)
				continue;

			String team[] = line.trim().split("\\s+");
			String name = team[0];

			int w = Integer.parseInt(team[1]), l = Integer.parseInt(team[2]), r = Integer.parseInt(team[3]);
			int g[] = new int[nOT];
			for (int i = 0; i < nOT; i++) {
				g[i] = Integer.parseInt(team[4 + i]);
			}
			teams[t++] = new Team(name, w, l, r, g);
			lkp.put(name, t - 1);
		}
		last = (nOT * (nOT - 1)) / 2 + nOT + 1;
		cf = 0;
	}

	public int numberOfTeams() {
		return nOT;
	}

	public Iterable<String> teams() {
		Queue<String> temp = new Queue<String>();

		for (int t = 0; t < nOT; t++) {
			temp.enqueue(teams[t].name);
		}
		return temp;
	}

	public int wins(String team) {
		if (!lkp.contains(team))
			throw new IllegalArgumentException();

		int t = lkp.get(team);
		return teams[t].w;
	}

	public int losses(String team) {
		if (!lkp.contains(team))
			throw new IllegalArgumentException();

		int t = lkp.get(team);
		return teams[t].l;
	}

	public int remaining(String team) {
		if (!lkp.contains(team))
			throw new IllegalArgumentException();

		int t = lkp.get(team);
		return teams[t].r;
	}

	public int against(String team1, String team2) {

		if (!lkp.contains(team1))
			throw new IllegalArgumentException();
		if (!lkp.contains(team2))
			throw new IllegalArgumentException();

		int t1 = lkp.get(team1);
		int t2 = lkp.get(team2);
		return teams[t1].g[t2];
	}

	public boolean isEliminated(String team) {
		if (!lkp.contains(team))
			throw new IllegalArgumentException();

		int t = lkp.get(team);

		if (isTrivialEliminated(t)) {
			return true;
		}

		FlowNetwork fn = getFlowNetwork(t);
		//System.out.print(fn);
		FordFulkerson ff = new FordFulkerson(fn, last - 1, last);
		//System.out.println(cf + " " + ff.value());

		return cf != ff.value();
	}

	private boolean isTrivialEliminated(int t) {
		for (int i = 0; i < nOT; i++) {
			if (i == t)
				continue;
			if (teams[t].w + teams[t].r < teams[i].w)
				return true;
		}
		return false;
	}

	private FlowNetwork getFlowNetwork(int t) {
		FlowNetwork fn = new FlowNetwork(last + 1);
		int src = last - 1;
		int cur = 0;
		int step = (nOT * (nOT - 1)) / 2;
		cf = 0;
		// first and second layers

		for (int i = 0; i < nOT; i++) {

			for (int j = i + 1; j < nOT; j++) {
				cur++;
				if (i == t || j == t) {
					continue;
				}

				// System.out.println("i: " + i + " j: " + j + " cur: " + cur + "\n");
				fn.addEdge(new FlowEdge(src, cur - 1, teams[i].g[j]));
				fn.addEdge(new FlowEdge(cur - 1, step + i, Double.POSITIVE_INFINITY));
				fn.addEdge(new FlowEdge(cur - 1, step + j, Double.POSITIVE_INFINITY));
				cf += teams[i].g[j];
			}
		}
		// last layer
		for (int i = 0; i < nOT; i++) {
			if (i == t)
				continue;

			fn.addEdge(new FlowEdge(step + i, last, teams[t].w + teams[t].r - teams[i].w));
		}
		return fn;
	}

	/*
	 * private void deActivateEdges(FlowNetwork fn, int v) { int step = (nOT * (nOT
	 * - 1)) / 2 + 1; int cur = v + 1;
	 * 
	 * int i = v; // first and second layers for (int j = i + 1; j < nOT; j++) {
	 * fn.addEdge(new FlowEdge(0, cur++, Double.POSITIVE_INFINITY)); fn.addEdge(new
	 * FlowEdge(cur - 1, step + i, Double.POSITIVE_INFINITY)); fn.addEdge(new
	 * FlowEdge(cur - 1, step + j, Double.POSITIVE_INFINITY)); }
	 * 
	 * // last layer
	 * 
	 * for (i = 0; i < nOT - 1; i++) { fn.addEdge(new FlowEdge(step + i, last, 0));
	 * } }
	 * 
	 * private void activateEdges(FlowNetwork fn, int v) { int step = (nOT * (nOT -
	 * 1)) / 2 + 1; int cur = v + 1; int i = v; // first and second layers for (int
	 * j = i + 1; j < nOT; j++) { fn.addEdge(new FlowEdge(0, cur++, 0));
	 * fn.addEdge(new FlowEdge(cur - 1, step + i, 0)); fn.addEdge(new FlowEdge(cur -
	 * 1, step + j, 0)); }
	 * 
	 * // last layer
	 * 
	 * for (i = 0; i < nOT - 1; i++) { if (v == i) fn.addEdge(new FlowEdge(step + i,
	 * last, 0)); else fn.addEdge(new FlowEdge(step + i, last, teams[v].w +
	 * teams[v].r - teams[i].w)); } }
	 */

	public Iterable<String> certificateOfElimination(String team) {
		if (!lkp.contains(team))
			throw new IllegalArgumentException();

		Queue<String> temp = new Queue<>();
		return temp;
	}

	private class Team {
		public String name;
		public int w, l, r;
		public int[] g;

		public Team(String name, int w, int l, int r, int[] g) {
			this.name = name;
			this.w = w;
			this.l = l;
			this.r = r;
			this.g = g;
		}

		@Override
		public String toString() {
			String ret = "\nTeam [name=" + name + ", w=" + w + ", l=" + l + ", r=" + r + "] ";

			for (int i = 0; i < g.length; i++) {
				ret += g[i];
			}
			return ret;
		}

	}

	@Override
	public String toString() {
		String ret = "BaseballElimination [teams=" + Arrays.toString(teams) + "]\n";

		return ret;
	}
}
