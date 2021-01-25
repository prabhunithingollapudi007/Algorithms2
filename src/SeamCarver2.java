
import java.awt.Color;

import edu.princeton.cs.algs4.AcyclicSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver2 {
	// create a seam carver object based on the given picture
	private EdgeWeightedDigraph edgeWeightedDigraph;
	private EdgeWeightedDigraph iedgeWeightedDigraph;

	private Picture picture;
	private Picture iPicture;

	private double[][] energies;

	private AcyclicSP dsp;
	private AcyclicSP iDsp;

	public SeamCarver2(Picture picture) {
		if (picture == null)
			throw new IllegalArgumentException();
		this.picture = new Picture(picture);
		changeStates();
	}

	private void changeStates() {

		this.iPicture = invertedPicture();

		this.energies = new PictureEnergy(picture).getEnergies();

		this.edgeWeightedDigraph = new GraphGenerator(picture, energies).getGraph();
		this.iedgeWeightedDigraph = new GraphGenerator(iPicture, invertedEnergies()).getGraph();

		this.dsp = new AcyclicSP(edgeWeightedDigraph, 0);
		this.iDsp = new AcyclicSP(iedgeWeightedDigraph, 0);
	}
	private double[][] invertedEnergies(){
		double[][] iE = new double[picture.width()][picture.height()];
		for (int row = 0; row < picture.width(); row++) {
			for (int col = 0; col < picture.height(); col++) {
				iE[row][col] = energies[col][row];
			}
		}
		return iE;
	}
	
	private Picture invertedPicture() {
		Picture iP = new Picture(picture.height(), picture.width());
		for (int row = 0; row < picture.height(); row++) {
			for (int col = 0; col < picture.width(); col++) {
				iP.set(row, col, picture.get(col, row));
			}
		}
		return iP;
	}

	// current picture
	public Picture picture() {
		return new Picture(picture);
	}

	// width of current picture
	public int width() {
		return picture.width();

	}

	// height of current picture
	public int height() {
		return picture.height();
	}

	// energy of pixel at column x and row y
	public double energy(int x, int y) {
		if (x < 0 || x >= picture.width())
			throw new IllegalArgumentException();
		if (y < 0 || y >= picture.height())
			throw new IllegalArgumentException();

		return energies[y][x];

	}

	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		int ret[] = new int[picture.width()];
		if (picture.width() == 1 || picture.height() == 1) {
			ret[0] = 0;
			return ret;
		}
		int i = 0;
		for (DirectedEdge e : iDsp.pathTo(picture.width() * picture.height() + 1)) {
			if (e.from() == 0)
				continue;
			// ret[i++] = picture.height() - (e.from() - 1) % picture.height() - 1;
			ret[i++] = (e.from() - 1) % picture.height();
		}
		return ret;

	}

	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		int ret[] = new int[picture.height()];
		if (picture.height() == 1 || picture.width() == 1) {
			ret[0] = 0;
			return ret;
		}
		int i = 0;
		for (DirectedEdge e : dsp.pathTo(picture.width() * picture.height() + 1)) {
			if (e.from() == 0)
				continue;
			ret[i++] = (e.from() - 1) % picture.width();
		}
		return ret;
	}

	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		if (seam == null)
			throw new IllegalArgumentException();
		if (seam.length != picture.width())
			throw new IllegalArgumentException();
		if (inValidEntries(seam, false))
			throw new IllegalArgumentException();

		Picture p = new Picture(picture.width(), picture.height() - 1);
		int step;
		for (int col = 0; col < picture.width(); col++) {
			step = 0;
			for (int row = 0; row < picture.height() - 1; row++) {
				if (row == seam[col])
					step = 1;
				p.set(col, row, picture.get(col, row + step));
			}
		}
		this.picture = p;
		//this.changeStates();
	}

	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		if (seam == null)
			throw new IllegalArgumentException();
		if (seam.length != picture.height())
			throw new IllegalArgumentException();

		if (inValidEntries(seam, true))
			throw new IllegalArgumentException();

		Picture p = new Picture(picture.width() - 1, picture.height());
		int step;
		for (int row = 0; row < picture.height(); row++) {
			step = 0;
			for (int col = 0; col < picture.width() - 1; col++) {
				if (col == seam[row])
					step = 1;
				p.set(col, row, picture.get(col + step, row));
			}
		}
		this.picture = p;
		//this.changeStates();
	}

	private boolean inValidEntries(int[] seam, boolean isVertical) {
		int prev = seam[0];

		for (int i = 0; i < seam.length; i++) {
			if (seam[i] < 0)
				return true;
			if ((seam[i] - prev > 1) || (seam[i] - prev < -1))
				return true;

			if (isVertical) {
				if (seam[i] >= picture.width())
					return true;
			} else {
				if (seam[i] >= picture.height())
					return true;
			}

			prev = seam[i];
		}

		return false;
	}

	// unit testing (optional)
	public static void main(String[] args) {

	}

	/*
	 * @Override public String toString() { String ret = "SeamCarver [picture=" +
	 * picture + "]\n"; for (int i = 0; i < energies.length; i++) { for (int j = 0;
	 * j < energies[0].length; j++) { ret += " " + energies[i][j]; } ret += "\n"; }
	 * return ret; }
	 */
	private class PictureEnergy {

		private Picture picture;
		private double[][] energies;

		public PictureEnergy(Picture picture) {
			this.picture = picture;
			this.energies = new double[picture.height()][picture.width()];
			calculateEnergies();
		}

		private void calculateEnergies() {
			int width = picture.width();
			int height = picture.height();
			for (int row = 0; row < height; row++) {
				for (int col = 0; col < width; col++) {
					if (isBorder(row, col))
						energies[row][col] = 1000;
					else
						energies[row][col] = getEnergy(row, col);

				}
			}
		}

		private double getEnergy(int row, int col) {
			double energy = 0;

			Color left = picture.get(col - 1, row);
			Color right = picture.get(col + 1, row);

			double rx = left.getRed() - right.getRed();
			double gx = left.getGreen() - right.getGreen();
			double bx = left.getBlue() - right.getBlue();
			energy = rx * rx + gx * gx + bx * bx;

			left = picture.get(col, row - 1);
			right = picture.get(col, row + 1);

			rx = left.getRed() - right.getRed();
			gx = left.getGreen() - right.getGreen();
			bx = left.getBlue() - right.getBlue();
			energy += (rx * rx + gx * gx + bx * bx);

			return Math.sqrt(energy);

		}

		private boolean isBorder(int row, int col) {
			if (row == 0 || col == 0)
				return true;
			if ((row == picture.height() - 1) || (col == picture.width() - 1))
				return true;
			return false;
		}

		public double[][] getEnergies() {
			// TODO Auto-generated method stub
			return energies;
		}

	}

}
