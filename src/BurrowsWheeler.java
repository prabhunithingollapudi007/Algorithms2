import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * 
 */

/**
 * @author Nithin
 *
 */
public class BurrowsWheeler {

	private static String inputString = "";

	// apply Burrows-Wheeler transform,
	// reading from standard input and writing to standard output
	public static void transform() {
//		readInputs();
//		int id;
//		CircularSuffixArray csa = new CircularSuffixArray(inputString);
//		String ret = "";
//		for (int i = 0; i < inputString.length(); i++) {
//			id = csa.index(i);
//			if (id == 0) {
//				BinaryStdOut.write(i, 32);
//				// System.out.print(i + " ");
//				ret += inputString.charAt(inputString.length() - 1);
//			} else {
//				ret += inputString.charAt(id - 1);
//			}
//		}
//		for (int i = 0; i < ret.length(); i++) {
//			BinaryStdOut.write(ret.charAt(i), 8);
//			// System.out.print(Integer.toHexString(ret.charAt(i)) + " ");
//		}
		
		String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int first = 0;
        while (first < csa.length() && csa.index(first) != 0) {
            first++;
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < csa.length(); i++) {
            BinaryStdOut.write(s.charAt((csa.index(i) + s.length() - 1) % s.length()), 8);
        }
        BinaryStdOut.close();
	}

	// apply Burrows-Wheeler inverse transform,
	// reading from standard input and writing to standard output
	public static void inverseTransform() {
		int f = BinaryStdIn.readInt();
		inputString = BinaryStdIn.readString();
		int len = inputString.length();
		
		
		 int[] count = new int[257], next = new int[len];
	        for (int i = 0; i < len; i++)
	            count[inputString.charAt(i) + 1]++;
	        for (int i = 1; i < 256 + 1; i++)
	            count[i] += count[i - 1];
	        for (int i = 0; i < len; i++)
	            next[count[inputString.charAt(i)]++] = i;
	        for (int i = next[f], c = 0; c < len; i = next[i], c++)
	            BinaryStdOut.write(inputString.charAt(i), 8);
	        BinaryStdOut.close();
	}

	// if args[0] is "-", apply Burrows-Wheeler transform
	// if args[0] is "+", apply Burrows-Wheeler inverse transform
	public static void main(String[] args) {
		if(args.length != 1)
			throw new IllegalArgumentException();
		if (args[0].equals("-"))
			BurrowsWheeler.transform();
		else
			BurrowsWheeler.inverseTransform();
	}

	private static void readInputs() {
		// inputString = "CADABRA!ABRA";

//		inputString = "";
//		while (!BinaryStdIn.isEmpty()) {
//			inputString += BinaryStdIn.readChar();
//		}
		inputString = BinaryStdIn.readString();
	}
}
