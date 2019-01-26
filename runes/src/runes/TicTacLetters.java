package runes;

import java.util.LinkedList;
import java.util.TreeSet;

/**
 * TicTacLetters transforms 3x3 binary matrices into their corresponding letters.
 * 
 * Likewise, this class handles comparisons to determine if rotations or reflections
 * are the same letter.
 * 
 *
 * @author UnfortunateCode
 */
public class TicTacLetters {
	private static TicTacLetters instance = null;
	
	private class TTLetter implements Comparable<TTLetter> {
		public int[][] TT;
		public String letter;
		
		public TTLetter (int[][] rune)throws IndexOutOfBoundsException  {
			if (rune.length != 3 || rune[0].length != 3) {
				throw new IndexOutOfBoundsException("9 elements expected, " + (rune.length * rune[0].length) + " elements received");
			}
			
			TT = TicTacLetters.maxRotation(TicTacLetters.copyOf(rune));
			letter = "";
		}
		
		public TTLetter (int[][] rune, String s) throws IndexOutOfBoundsException {
			if (rune.length != 3 || rune[0].length != 3) {
				throw new IndexOutOfBoundsException("9 elements expected, " + (rune.length * rune[0].length) + " elements received");
			}
			
			/*System.out.println("Rune " + s + ": ");
			for (int k = 0; k < 3; ++k) {
				for (int j = 0; j < 3; ++j) {
					System.out.print(rune[k][j]);
				}
				System.out.println();
			}
			System.out.println();*/
			
			TT = TicTacLetters.maxRotation(copyOf(rune));
			letter = s;
			
			//System.out.println(this);
		}
		
		public void setLetter(String s) {
			letter = s;
		}
		
		public boolean equals(TTLetter ttletter) {
			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 3; ++j) {
					if (TT[i][j] != ttletter.TT[i][j]) {
						return false;
					}
				}
			}
			
			return true;
		}

		@Override
		public int compareTo(TTLetter ttletter) {

			return (int)(TicTacLetters.getValue(TT) - TicTacLetters.getValue(ttletter.TT));
		}
		
		public String toString() {
			String s = letter + "\n";
			
			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 3; ++j) {
					s += TT[i][j];
				}
				s += "\n";
			}
			
			return s;
		}
	}
	
	public static int[][] copyOf(int[][] base) {
		int[][] copy = new int[base.length][base[0].length];
		
		for  (int i = 0; i < base.length; ++i) {
			for (int j = 0; j < base[0].length; ++j) {
				copy[i][j] = base[i][j];
			}
		}
		
		return copy;
	}
	
	private TreeSet<TTLetter> letters = new TreeSet<>();
	
	/**
	 * Sets up the names of each 3x3 array of 1s and 0s.
	 * 
	 */
	public static TicTacLetters getInstance() {
		if (instance == null) {
			instance = new TicTacLetters();
		}
		
		return instance;
	}
	
	/*
	 * Builds the library of names for the various runes
	 */
	private TicTacLetters() {
		letters.add(new TTLetter(toMatrix(1,1,0,0,1,1,0,0,1),"H"));
		letters.add(new TTLetter(toMatrix(1,0,1,1,1,1,0,0,0),"A"));
		letters.add(new TTLetter(toMatrix(1,1,1,1,0,1,0,0,0),"B"));
		letters.add(new TTLetter(toMatrix(1,0,1,0,0,1,1,1,0),"F"));
		letters.add(new TTLetter(toMatrix(1,1,0,1,1,1,0,0,0),"D"));
		letters.add(new TTLetter(toMatrix(0,0,1,1,1,0,1,1,0),"E"));
		letters.add(new TTLetter(toMatrix(1,1,0,0,1,0,1,0,1),"W"));
		letters.add(new TTLetter(toMatrix(1,1,0,1,0,1,0,1,0),"I"));
		

		letters.add(new TTLetter(toMatrix(0,1,1,0,1,1,0,0,1),"Q"));
		letters.add(new TTLetter(toMatrix(1,0,1,1,0,0,1,1,0),"L"));
		letters.add(new TTLetter(toMatrix(1,0,0,1,0,1,1,0,1),"P"));
		letters.add(new TTLetter(toMatrix(0,1,0,1,0,1,1,0,1),"M"));
		letters.add(new TTLetter(toMatrix(0,1,0,1,1,1,1,0,0),"G"));
		letters.add(new TTLetter(toMatrix(1,1,1,0,1,0,1,0,0),"V"));
		letters.add(new TTLetter(toMatrix(1,1,1,1,0,0,1,0,0),"S"));
		letters.add(new TTLetter(toMatrix(1,1,1,1,0,0,0,1,0),"R"));
		

		letters.add(new TTLetter(toMatrix(1,1,0,0,1,1,1,0,1),"H`"));
		letters.add(new TTLetter(toMatrix(1,0,1,1,1,1,1,0,0),"A`"));
		letters.add(new TTLetter(toMatrix(0,1,0,1,1,1,1,0,1),"B`"));
		letters.add(new TTLetter(toMatrix(1,0,1,0,0,1,1,1,1),"F`"));
		letters.add(new TTLetter(toMatrix(1,1,0,1,1,1,0,0,1),"D`"));
		letters.add(new TTLetter(toMatrix(0,0,1,1,1,0,1,1,1),"E`"));
		letters.add(new TTLetter(toMatrix(0,0,1,1,0,1,1,1,1),"W`"));
		letters.add(new TTLetter(toMatrix(1,1,0,1,0,1,0,1,1),"I`"));
		

		letters.add(new TTLetter(toMatrix(1,1,1,0,1,1,0,0,1),"Q`"));
		letters.add(new TTLetter(toMatrix(1,1,0,1,1,0,1,1,0),"L`"));
		letters.add(new TTLetter(toMatrix(1,0,1,1,0,1,1,0,1),"P`"));
		letters.add(new TTLetter(toMatrix(1,1,0,1,0,1,1,0,1),"M`"));
		letters.add(new TTLetter(toMatrix(1,1,0,1,1,1,1,0,0),"G`"));
		letters.add(new TTLetter(toMatrix(1,1,1,0,1,0,1,0,1),"V`"));
		letters.add(new TTLetter(toMatrix(0,1,0,1,0,1,1,1,1),"S`"));
		letters.add(new TTLetter(toMatrix(1,1,0,1,1,1,0,1,0),"R`"));
		

		letters.add(new TTLetter(toMatrix(1,1,1,0,1,1,1,0,1),"(HQ)"));
		letters.add(new TTLetter(toMatrix(1,0,1,1,1,1,1,0,1),"(AL)"));
		letters.add(new TTLetter(toMatrix(1,1,1,1,0,1,1,0,1),"(BP)"));
		letters.add(new TTLetter(toMatrix(1,1,1,1,0,1,1,1,0),"(FM)"));
		letters.add(new TTLetter(toMatrix(1,1,1,1,1,1,0,0,1),"(DG)"));
		letters.add(new TTLetter(toMatrix(0,1,0,1,1,1,1,1,1),"(EV)"));
		letters.add(new TTLetter(toMatrix(1,1,0,0,1,1,1,1,1),"(WS)"));
		letters.add(new TTLetter(toMatrix(1,1,0,1,1,1,0,1,1),"(IR)"));
		

		letters.add(new TTLetter(toMatrix(0,1,0,1,1,0,1,0,1),"J"));
		letters.add(new TTLetter(toMatrix(0,0,1,1,1,1,1,0,0),"N"));
		letters.add(new TTLetter(toMatrix(1,0,1,0,0,0,1,1,1),"U"));
		letters.add(new TTLetter(toMatrix(1,0,1,0,1,0,1,0,1),"X"));
		

		letters.add(new TTLetter(toMatrix(0,1,0,1,1,1,0,1,0),"O"));
		letters.add(new TTLetter(toMatrix(1,1,1,1,0,1,1,1,1),"O`"));
		letters.add(new TTLetter(toMatrix(1,1,0,1,0,1,0,0,1),"K"));
		letters.add(new TTLetter(toMatrix(1,0,1,1,1,1,1,1,1),"K`"));
		letters.add(new TTLetter(toMatrix(1,1,1,0,1,0,0,1,0),"T"));
		letters.add(new TTLetter(toMatrix(0,1,1,1,1,1,1,1,1),"T`"));
		
		letters.add(new TTLetter(toMatrix(1,1,1,1,1,1,1,1,1),"Z"));
	}
	
	public String get(int a, int b, int c, int d, int e, int f, int g, int h, int i) {
		TTLetter ttletter = new TTLetter(toMatrix(a,b,c,d,e,f,g,h,i));
		
		if (letters.contains(ttletter)) {
			ttletter = letters.floor(ttletter);
			return ttletter.letter;
		}
		
		return null;
	}
	
	/**
	 * Puts 9 elements into a 3x3 matrix.
	 * 
	 * @return the 3x3 matrix
	 */
	public static int[][] toMatrix(int a, int b, int c, int d, int e, int f, int g, int h, int i) {
		int[][] matrix = new int[3][3];
		
		matrix[0][0] = a;
		matrix[0][1] = b;
		matrix[0][2] = c;
		matrix[1][0] = d;
		matrix[1][1] = e;
		matrix[1][2] = f;
		matrix[2][0] = g;
		matrix[2][1] = h;
		matrix[2][2] = i;
		
		/*for (int k = 0; k < 3; ++k) {
			for (int j = 0; j < 3; ++j) {
				System.out.print(matrix[k][j]);
			}
			System.out.println();
		}*/
		
		return matrix;
	}
	
	/**
	 * Converts a 9 element array into a 3x3 array
	 * 
	 * @param nineElem the 9 element array
	 * @return the 3x3 element array
	 * @throws IndexOutOfBoundsException when nineElem does not have exactly 9 elements
	 */
	public static int[][] to3x3Matrix(int[] nineElem) throws IndexOutOfBoundsException {
		if (nineElem.length != 9) {
			throw new IndexOutOfBoundsException("9 elements expected, " + nineElem.length + " elements received");
		}
		int[][] matrix = new int[3][3];
		
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				matrix[i][j] = nineElem[i*3+j];
			}
		}
		return matrix;
	}
	
	/**
	 * Finds the rotation/reflection that has the most
	 * non-zero elements at the beginning/earlier. Converts
	 * non-zero elements into 1.
	 * 
	 * @param base the array to be rotated/reflected
	 * @return the rotation/reflection with the most 1s early
	 */
	public static int[][] maxRotation(int[][] base) {
		int[][] rotation = copyOf(base);
		int sum = 0;
		
		
		
		for (int i = 0; i < rotation.length; ++i) {
			for (int j = 0; j < rotation[0].length; ++j) {
				if (rotation[i][j] != 0) {
					rotation[i][j] = 1;
					++sum;
				}
			}
		}
		
		if (sum < ((rotation.length * rotation[0].length) / 2.0f)) {
			for (int i = 0; i < rotation.length; ++i) {
				for (int j = 0; j < rotation[0].length; ++j) {
					rotation[i][j] = 1 - rotation[i][j];
						
				}
			}
		}

		int[][] max = copyOf(rotation);
		long maxValue = getValue(rotation);
		long tempValue;
		
		for (int j = 0; j < 2; ++j) {
			for (int i = 0; i < 3+j; ++i) {
				rotation = rotateCW(rotation); 
				tempValue = getValue(rotation);

				if (tempValue > maxValue) {
					max = copyOf(rotation);
					maxValue = tempValue;
				}
			}
			if (j == 0) {
				rotation = flip(rotation);
			}
		}

		return max;
	}
	
	/**
	 * Rotates an array clockwise: in a 3x4 array,
	 * the [0][0] value moves to [0][3], and the [0][3] value
	 * moves to [3][2]
	 * 
	 * @param base the array to be rotated
	 * @return the rotated array
	 */
	public static int[][] rotateCW(int[][] base) {
		int heightIndex = base.length - 1;
		int[][] rotation = new int[base[0].length][base.length];
		
		for (int i = 0; i < base.length; ++i) {
			for (int j = 0; j < base[0].length; ++j) {
				rotation[j][heightIndex - i] = base[i][j];
			}
		}
		
		return rotation;
	}
	
	/**
	 * Flips an array vertically (top becomes bottom)
	 * 
	 * @param base the array to be flipped
	 * @return the flipped array
	 */
	public static int[][] flip(int[][] base) {
		int heightIndex = base.length - 1;
		int[][] rotation = new int[base.length][base[0].length];
		
		for (int i = 0; i < base.length; ++i) {
			for (int j = 0; j < base[0].length; ++j) {
				rotation[heightIndex - i][j] = base[i][j];
			}
		}
		
		return rotation;
	}
	
	/**
	 * Assigns each position in the 2d array with a power of 2, starting
	 * at the max value at [0][0], and reducing to 2^0 at the end. If the
	 * position contains anything besides 0, the power of 2 is added to the 
	 * value.
	 * 
	 * @param base the array to be converted
	 * @return a higher value for more non-zero entries at the top
	 */
	public static long getValue(int[][] base) {
		long value = 0;
		int ivalue = base.length - 1;
		int mult = base[0].length;
		int jvalue = mult - 1;
		
		for (int i = 0; i < base.length; ++i) {
			for (int j = 0; j < base[0].length; ++j) {
				if (base[i][j] != 0) {
					value += (int)Math.pow(2, (ivalue - i)*mult + (jvalue - j));
				} 
			}
		}
		
		return value;
	}
	
	/**
	 * 
	 * @param base the array to be ordered
	 * @return the number of non-zero elements
	 */
	public static int getOrder(int[][] base) {
		int sum = 0;
		
		for (int i = 0; i < base.length; ++i) {
			for (int j = 0; j < base[0].length; ++j) {
				if (base[i][j] != 0) {
					sum += 1;
				} 
			}
		}
		
		return sum;
	}

	public static void main(String[] args) {
		TicTacLetters tt = TicTacLetters.getInstance();
		
		
		/*System.out.println("010100111 is the letter: " + tt.get(0,1,0,1,0,0,1,1,1));
		
		for (TTLetter t : tt.letters) {
			System.out.println(t);
		}
		
		TTLetter ttletter = tt.new TTLetter(toMatrix(0,1,0,1,0,0,1,1,1),"Bob");
		System.out.println(ttletter);*/
		
		/*for (int a = 0; a < 2; ++a) {
			for (int b = 0; b < 2; ++b) {
				for (int c = 0; c < 2; ++c) {
					for (int d = 0; d < 2; ++d) {
						for (int e = 0; e < 2; ++e) {
							for (int f = 0; f < 2; ++f) {
								for (int g = 0; g < 2; ++g) {
									for (int h = 0; h < 2; ++h) {
										for (int i = 0; i < 2; ++i) {
											if (tt.get(a,b,c,d,e,f,g,h,i) == null) {
												System.out.println(tt.new TTLetter(toMatrix(a,b,c,d,e,f,g,h,i),"Fail"));
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}*/
	}
}
