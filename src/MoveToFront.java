import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * 
 */

/**
 * @author Nithin
 *
 */
public class MoveToFront {

	// apply move-to-front encoding, reading from standard input and writing to
	// standard output

	//private static String inputString = "";

	public static void encode() {
//		readInputs();
//		int l;
//		char c;
//		// inputString = "ABRACADABRA!";
//
//		String ref = "";
//		int[] lkp = new int[256];
//		for (int i = 0; i < 256; i++) {
//			lkp[i] = i;
//			ref += (char) i;
//		}
//		for (int i = 0; i < inputString.length(); i++) {
//			c = inputString.charAt(i);
//			l = lkp[c];
//			// System.out.print(Integer.toHexString(l) + " ");
//			BinaryStdOut.write(l, 8);
//			for (int j = 0; j < l; j++) {
//				lkp[ref.charAt(j)]++;
//			}
//
//			ref = c + ref.substring(0, l) + ref.substring(l + 1, ref.length());
//
//			lkp[c] = 0;
//		}
		
		char[] chars = lkpGenerator();
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            char tmpin, count, tmpout;
            for (count = 0, tmpout = chars[0]; ch != chars[count]; count++) {
                tmpin = chars[count];
                chars[count] = tmpout;
                tmpout = tmpin;
            }
            chars[count] = tmpout;
            BinaryStdOut.write(count);
            chars[0] = ch;
        }
        BinaryStdOut.close();
	}

	// apply move-to-front decoding, reading from standard input and writing to
	// standard output
//	public static void decode() {
//		readInputs();
//		int l;
//		char c;
//		int temp;
//		// inputString = "ABR";
//
//		String ref = "";
//		int[] lkp = new int[256];
//		for (int i = 0; i < 256; i++) {
//			lkp[i] = i;
//			ref += (char) i;
//		}
//
//		for (int i = 0; i < inputString.length(); i++) {
//			c = inputString.charAt(i);
//			// temp = Integer.parseInt(inputString.charAt(i) +""+ inputString.charAt(i +
//			// 1));
//
//			l = ref.charAt(c);
//			// System.out.print(ref.charAt(l) + " ");
//			BinaryStdOut.write(ref.charAt(l), 8);
//
//			// System.out.print(Integer.toHexString(ref.charAt(l)) + " ");
//			for (int j = 0; j < l; j++) {
//				lkp[ref.charAt(j)]++;
//			}
//
//			ref = c + ref.substring(0, l) + ref.substring(l + 1, ref.length());
//
//			lkp[c] = 0;
//		}
//	}

	
	public static void decode() {
		char[] lkp = lkpGenerator();
//		readInputs();
//		char ch;
//		char l;
//		for(int i = 0; i < inputString.length(); i++) {
//			ch = inputString.charAt(i);
//			l = lkp[ch];
//			//BinaryStdOut.write(l, 8);
//			System.out.print(lkp[ch] + " ");
//			while(ch > 0) {
//				lkp[ch] = lkp[ch--];
//			}
//			lkp[0] = ch;
//		}
		
		while (!BinaryStdIn.isEmpty()) {
            char count = BinaryStdIn.readChar();
            BinaryStdOut.write(lkp[count], 8);
            char index = lkp[count];
            while (count > 0) {
                lkp[count] = lkp[--count];
            }
            lkp[0] = index;
        }
        BinaryStdOut.close();
		
	}

	private static char[] lkpGenerator() {
		char[] lkp = new char[256];
		for(char i = 0; i < 256; i++) {
			lkp[i] = i; 
		}
		return lkp;
	}
	
	//
//	// if args[0] is "-", apply move-to-front encoding
	// if args[0] is "+", apply move-to-front decoding
	public static void main(String[] args) {

		if (args[0].equals("-"))
			MoveToFront.encode();
		else
			MoveToFront.decode();

	}

	private static void readInputs() {
		 //inputString = "ABRDE&";
		
		
		/*
		 * inputString = ""; while (!BinaryStdIn.isEmpty()) { inputString +=
		 * BinaryStdIn.readChar(); }
		 */

	}
}
