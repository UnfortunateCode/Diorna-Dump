package Gnomish;

/** 
 * A quick test of the cubic TTC system. In SubCubes, every possible sub-cube 
 * 8 points, each face is 1/4 of a TTC.
 * 
 *     f - - - h
 *   / |     / |
 * d - - - g   |
 * |   |   |   |
 * |   c - | - e
 * | /     | /
 * a - - - b
 * 
 * Spoilers: 
 * 00000000 cube
 * 00000001 corner cut out
 * 00000011 one side
 * 00000110 one facial diagonal
 * 00000111 one facial triangle
 * 00001110 pyramid w/ corner base and triangle around pyramid's point
 * 00001111 two facing corners
 * 00010111 two faces
 * 00011000 opposite corner diagonal (through center)
 * 00011001 inner triangle with 1 side, 1 facial diagonal, and 1 opposite corner diagonal
 * 00011011 two zagged tetrahedrons (facial triangle, vertex and point become point and vertex of another facial triangle
 * 00011110 two parallel facial triangles connecting to opposite corners
 * 00111010 two intersecting squares
 * 01110001 intersecting pyramids
 *
 * @author UnfortunateCode
 */
public class SubCubes {

	public static void main(String[] args) {
		
		System.out.println("#'s become binary abcdefgh, a connects to b c and d, abc square to e, acd-f adb-g, h is opposite a");

		boolean[] arr = new boolean[256];
		int[] digits = new int[8];
		int temp;
		int[] rotations = new int[48];
		
		for (int i = 0; i < 256; ++i) {
			arr[i] = true;
		}
		
		/*
		 * Run through every combination of two sets of connected points, and determine every
		 * rotation that can be made from it. Disable those rotations/reflections from being checked.
		 */
		for (int i = 0; i < 256; ++i) {
			if (arr[i]) {
				temp = i;
				for (int j = 0; j < 8; ++j) {
					if (temp >= Math.pow(2,7-j)) {
						temp -= (int)Math.pow(2, 7-j);
						digits[j] = 1;
					} else {
						digits[j] = 0;
					}
				}
				
				System.out.println(digits[0] + "" + digits[1] + "" + digits[2] + "" + digits[3] + "" + digits[4] + "" + digits[5] + "" + digits[6] + "" + digits[7]);
				
				// Rotations
				rotations = getValues(digits[0],digits[1],digits[2],digits[3],digits[4],digits[5],digits[6],digits[7]);
				for (int k = 0; k < 48; ++k) {
					
					arr[rotations[k]] = false;
				}
				
				// Flip for reflection and reflection's rotations
				for (int j = 0; j < 8; ++j) {
					digits[j] = 1-digits[j];
				}
				rotations = getValues(digits[0],digits[1],digits[2],digits[3],digits[4],digits[5],digits[6],digits[7]);
				for (int k = 0; k < 48; ++k) {
					
					arr[rotations[k]] = false;
				}
				
				// Since a value can be its own rotation/reflection (see 00000000, the full cube), 
				// it may have been set to false. Reset to true. Useful if we do anything with this in the future,
				// otherwise it doesn't matter as this case was already printed above.
				arr[i] = true;
			}
		}
		
		
	}
	
	/**
	 * Turns 8 digits into their decimal form. Parameters should be 0 or 1, but this
	 * is not checked.
	 * 
	 * @param a the 128s digit
	 * @param b the 64s digit
	 * @param c the 32s digit
	 * @param d the 16s digit
	 * @param e the 8s digit
	 * @param f the 4s digit
	 * @param g the 2s digit
	 * @param h the 1s digit
	 * @return the sumproduct of the digits and the digit place (the decimal value of the binary digits)
	 */
	public static int getValue(int a, int b, int c, int d, int e, int f, int g, int h) {
		return a*128+b*64+c*32+d*16+e*8+f*4+g*2+h;
	}
	
	/**
	 * Performs all rotations around each corner.
	 * 
	 * @param a the set that the bottom front left corner is in
	 * @param b the set that the bottom front right corner is in
	 * @param c the set that the bottom rear left corner is in
	 * @param d the set that the top front left corner is in
	 * @param e the set that the bottom rear right corner is in
	 * @param f the set that the top rear left corner is in
	 * @param g the set that the top front right corner is in
	 * @param h the set that the top rear right corner is in
	 * @return all values associated with the rotations around each corner when placed in the bottom front left position including reflections
	 */
	public static int[] getValues(int a, int b, int c, int d, int e, int f, int g, int h) {
		int[] values = new int[48];
		int[] subset = new int[6];
		int place = -1;
		
		
		subset = getRotations(a,b,c,d,e,f,g,h);
		for (int i = 0; i < 6; ++i) {
			values[++place] = subset[i];
		}
		
		subset = getRotations(h,g,f,e,d,c,b,a);
		for (int i = 0; i < 6; ++i) {
			values[++place] = subset[i];
		}
		
		subset = getRotations(b,a,e,g,c,h,d,f);
		for (int i = 0; i < 6; ++i) {
			values[++place] = subset[i];
		}
		
		subset = getRotations(f,d,h,c,g,e,a,b);
		for (int i = 0; i < 6; ++i) {
			values[++place] = subset[i];
		}
		
		subset = getRotations(c,a,e,f,b,h,d,g);
		for (int i = 0; i < 6; ++i) {
			values[++place] = subset[i];
		}
		
		subset = getRotations(g,d,h,b,f,e,a,c);
		for (int i = 0; i < 6; ++i) {
			values[++place] = subset[i];
		}
		
		subset = getRotations(d,a,g,f,b,h,c,e);
		for (int i = 0; i < 6; ++i) {
			values[++place] = subset[i];
		}
		
		subset = getRotations(e,c,h,b,f,g,a,d);
		for (int i = 0; i < 6; ++i) {
			values[++place] = subset[i];
		}
		
		return values;
		
	}
	
	/**
	 * Rotates around the bottom front left corner (a) with reflections and puts together
	 * a list of the values associated with that orientation.
	 * 
	 * @param a the set that the bottom front left corner is in
	 * @param b the set that the bottom front right corner is in
	 * @param c the set that the bottom rear left corner is in
	 * @param d the set that the top front left corner is in
	 * @param e the set that the bottom rear right corner is in
	 * @param f the set that the top rear left corner is in
	 * @param g the set that the top front right corner is in
	 * @param h the set that the top rear right corner is in
	 * @return the value of the three spins around the a-h axis, as well as the spins of the reflection
	 */
	public static int[] getRotations(int a, int b, int c, int d, int e, int f, int g, int h) {
		int[] set = new int[6];
		
		set[0] = getValue(a,b,c,d,e,f,g,h);
		set[1] = getValue(a,c,d,b,f,g,e,h);
		set[2] = getValue(a,d,b,c,g,e,f,h);
		
		set[3] = getValue(a,d,c,b,f,e,g,h);
		set[4] = getValue(a,c,b,d,e,g,f,h);
		set[5] = getValue(a,b,d,c,g,f,e,h);
		
		return set;
	}

}
