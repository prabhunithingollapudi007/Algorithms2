import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

	private Picture picture;

	private double[][] energies;
	private SCHelp help;

	public SeamCarver(Picture picture) {
		if (picture == null)
			throw new IllegalArgumentException();
		
		this.picture = new Picture(picture);
		this.energies = new double[picture.height()][picture.width()];

		this.help = new SCHelp();
		
		// setting colors and energies
		for (int row = 0; row < picture.height(); row++) {
			for (int col = 0; col < picture.width(); col++) {
				if (row == 0 || row == picture.height() - 1)
					energies[row][col] = 1000;
				else if (col == 0 || col == picture.width() - 1)
					energies[row][col] = 1000;
				else
					energies[row][col] = getEnergies(row, col);
			}
		}
	}

	private double getEnergies(int row, int col) {
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

	private double[][] invertedEnergies(double[][] energies){
		double[][] iE = new double[picture.width()][picture.height()];
		for (int row = 0; row < picture.width(); row++) {
			for (int col = 0; col < picture.height(); col++) {
				iE[row][col] = energies[col][row];
			}
		}
		return iE;
	}
	
	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		int ret[] = new int[picture.width()];
		if (picture.width() == 1 || picture.height() == 1) {
			ret[0] = 0;
			return ret;
		}
		double[][] iE = invertedEnergies(energies);
		int minIndex = help.findRowMin(iE, 1);
		ret[0] = minIndex;
		ret[1] = minIndex;

		for (int temp = 1; temp < picture.width() - 1; temp++) {
			minIndex = help.getMin(temp, minIndex, iE);
			ret[temp + 1] = minIndex;
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
		
		int minIndex = help.findRowMin(energies, 1);
		ret[0] = minIndex;
		ret[1] = minIndex;

		for (int temp = 1; temp < picture.height() - 1; temp++) {
			minIndex = help.getMin(temp, minIndex, energies);
			ret[temp + 1] = minIndex;
		}
		return ret;
	}

	
	private void changeEnergies() {
		for (int row = 0; row < picture.height(); row++) {
			for (int col = 0; col < picture.width(); col++) {
				if (row == 0 || row == picture.height() - 1)
					energies[row][col] = 1000;
				else if (col == 0 || col == picture.width() - 1)
					energies[row][col] = 1000;
				else
					energies[row][col] = getEnergies(row, col);
			}
		}
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
		// change energies;
		this.changeEnergies();
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
		this.changeEnergies();
	}

	// unit testing (optional)
	public static void main(String[] args) {

	}

	@Override
	public String toString() {
		String ret = "SeamCarver [picture=" + picture + "]\n" ;
		for (int i = 0; i < energies.length; i++) {
			for (int j = 0; j < energies[0].length; j++) {
				ret += " " + energies[i][j];
			}
			ret += "\n";
		}
		return ret;
	}

}
