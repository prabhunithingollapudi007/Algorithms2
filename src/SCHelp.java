
public class SCHelp {
	
	public int getMin(int temp, int minIndex, double[][] energies) {
		int min = minIndex;
		
		if (minIndex == 0) {
			if (energies[temp + 1][minIndex] < energies[temp + 1][minIndex + 1])
				min = minIndex;
			else
				min = minIndex + 1;
		} else if (minIndex == energies[0].length - 1) {
			if (energies[temp + 1][minIndex] < energies[temp + 1][minIndex - 1])
				min = minIndex;
			else
				min = minIndex - 1;
		} else {
			if ((energies[temp + 1][minIndex] < energies[temp + 1][minIndex + 1]
					&& (energies[temp + 1][minIndex] < energies[temp + 1][minIndex - 1])))
				min = minIndex;
			else if ((energies[temp + 1][minIndex - 1] < energies[temp + 1][minIndex + 1]
					&& (energies[temp + 1][minIndex - 1] < energies[temp + 1][minIndex])))
				min = minIndex - 1;
			else
				min = minIndex + 1;
		}

		return min;
	}

	public int findRowMin(double[][] energies, int index) {
		int ret = 0;
		for (int i = 0; i < energies[0].length; i++) {
			if (energies[index][i] < energies[index][ret]) {
				ret = i;
			}
		}

		return ret;
	}

}
