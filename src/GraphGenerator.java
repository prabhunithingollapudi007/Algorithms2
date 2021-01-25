import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;

public class GraphGenerator {
	private Picture picture;
	private double[][] energies;
	private EdgeWeightedDigraph digraph;

	public GraphGenerator(Picture picture, double[][] energies) {
		this.picture = picture;
		this.energies = energies;
		this.generateGraph();
	}

	private void generateGraph() {
		int height = picture.height(), width = picture.width();
		digraph = new EdgeWeightedDigraph(width * height + 2);
		DirectedEdge de;

		// add source
		for (int col = 0; col < width; col++) {
			de = new DirectedEdge(0, col + 1, 10);
			digraph.addEdge(de);
		}
		// add last dummy node
		for (int col = 0; col < width; col++) {
			de = new DirectedEdge((height - 1) * picture.width() + col + 1, height * width + 1, 10);
			digraph.addEdge(de);
		}

		if (height == 1) {
			;
		}

		else if (width == 1) {
			for(int row = 0; row < height; row++) {
				de = new DirectedEdge(row + 1, row + 2, energies[row][0]);
			}
		} else {
			// add individual vertices
			for (int row = 0; row < height - 1; row++) {
				for (int col = 0; col < width; col++) {
					for (Pair p : getAdjacent(row, col)) {
						de = new DirectedEdge(get1D(row, col) + 1, get1D(p.getX(), p.getY()) + 1,
								energies[p.getX()][p.getY()]);
						digraph.addEdge(de);
					}
				}
			}
		}

	}

	private int get1D(int row, int col) {

		return row * picture.width() + col;
	}

	class Pair {
		private int x, y;

		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}
	}

	private Pair[] getAdjacent(int row, int col) {
		Pair temp[];
		if (col == 0) {
			temp = new Pair[2];
			temp[0] = new Pair(row + 1, col);
			temp[1] = new Pair(row + 1, col + 1);
			return temp;
		} else if (col == picture.width() - 1) {
			temp = new Pair[2];
			temp[0] = new Pair(row + 1, col - 1);
			temp[1] = new Pair(row + 1, col);
			return temp;
		} else {
			temp = new Pair[3];
			temp[0] = new Pair(row + 1, col - 1);
			temp[1] = new Pair(row + 1, col);
			temp[2] = new Pair(row + 1, col + 1);
			return temp;
		}
	}

	public EdgeWeightedDigraph getGraph() {
		// TODO Auto-generated method stub
		return digraph;
	}

}
