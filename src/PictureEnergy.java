import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class PictureEnergy {

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
		if ( (row == picture.height() - 1) || (col == picture.width() - 1))
			return true;
		return false;
	}


	public double[][] getEnergies() {
		// TODO Auto-generated method stub
		return energies;
	}
	
}
