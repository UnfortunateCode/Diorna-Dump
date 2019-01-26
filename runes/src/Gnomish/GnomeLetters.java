package Gnomish;

import runes.TicTacLetters;

public class GnomeLetters {
	private static GnomeLetters instance = null;
    
	private class GLetter implements Comparable<GLetter> {

		public int[][][] G;
		public String letter;

		public GLetter (int[][][] rune)throws IndexOutOfBoundsException, ArithmeticException  {
			if (rune.length != 3 || rune[0].length != 3 || rune[0][0].length != 3) {
				throw new IndexOutOfBoundsException("9 elements expected, " + (rune.length * rune[0].length) + " elements received");
			}

			G = GnomeLetters.maxRotation(copyOf(rune));
			letter = "";
		}

		public GLetter (int[][][] rune, String s) throws IndexOutOfBoundsException, ArithmeticException {
			if (rune.length != 3 || rune[0].length != 3 || rune[0][0].length != 3) {
				throw new IndexOutOfBoundsException("9 elements expected, " + (rune.length * rune[0].length) + " elements received");
			}

			G = GnomeLetters.maxRotation(copyOf(rune));
			letter = s;
		}



		public boolean equals(GLetter base) {
			for  (int i = 0; i < base.G.length; ++i) {
				for (int j = 0; j < base.G[0].length; ++j) {
					for (int k = 0; k < base.G[0][0].length; ++k) {
						if (G[i][j][k] != base.G[i][j][k]) {
							return false;
						}
					}
				}
			}

			return true;
		}

		@Override
		public int compareTo(GLetter base) {
			int comp;

			comp = (int)TicTacLetters.getValue(base.G[0]);

			if (comp != 0) {
				return comp;
			}

			comp = (int)TicTacLetters.getValue(base.G[1]);

			if (comp != 0) {
				return comp;
			}

			return (int)TicTacLetters.getValue(base.G[2]);

		}

	}

	
	public static GnomeLetters getInstance() {
		if (instance == null) {
			instance = new GnomeLetters();
		}
		
		return instance;
	}
	public static int[][][] copyOf(int[][][] base) {
		int[][][] copy = new int[base.length][base[0].length][base[0][0].length];

		for  (int i = 0; i < base.length; ++i) {
			for (int j = 0; j < base[0].length; ++j) {
				for (int k = 0; k < base[0][0].length; ++k) {
					copy[i][j][k] = base[i][j][k];
				}
			}
		}

		return copy;
	}

	public static int[][][] maxRotation(int[][][] rune) throws ArithmeticException, IllegalArgumentException, IndexOutOfBoundsException {
		int[][][] oriented = orient(rune); // Source of all the exceptions
		
			
		
		
		// Orient the bottom layer, if it has multiple max rotations, then use the second layer, finally use the top 
		// layer. If two orientations have all the layers with the same value, then they are identical.
		
		
		return oriented;
	}

	/**
	 * Ensure that only one outer face has 5 points set to 1 (rotated to bottom), and the others have 4,
	 * the orientation with 5 points has a 4 point center, with the others having 5 points.
	 * 
	 * Note: all of these may be reversed, in which case all of the 1s and 0s need to be swapped.
	 * 
	 * @param rune the 3*3*3 cube filled with only 1s and 0s
	 * @return
	 * @throws ArithmeticException
	 */
	public static int[][][] orient(int[][][] rune) throws ArithmeticException, IllegalArgumentException, IndexOutOfBoundsException {
		int[][][] oriented = new int[3][3][3];
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				for (int k = 0; k < 3; ++k) {
					oriented[i][j][k] = rune[i][j][k];
				}
			}
		}
		
		int[][][] planes;
		
		try {
			planes = getPlanes(oriented);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Parameter was not a filled out:\n" + print3Array(rune));
		} 
		// planes will have the faces [Bottom, BtT, Top, Front, FtB, Back, Left, LtR, Right]
		
		
		int num4 = 0, num5 = 0;
		int[] orders = new int[6];
		int[] indices = {0,6,3,2,8,5};
		String[] indNames = {"Bottom", "Left", "Front", "Top", "Right", "Back"};
		
		for (int i = 0; i < 6; ++i) {
			orders[i] = TicTacLetters.getOrder(planes[indices[i]]);
			
			if (orders[i] == 4) {
				++num4;
			} else if (orders[i] == 5) {
				++num5;
			} else {
				throw new ArithmeticException(indNames[i] + " layer has order: " + orders[i] + "\n" + print3Array(rune));
			}
		}
		
		if (num4 == 1) {
			return orient(swap(rune));
		}
		
		if (num5 != 1) {
			throw new ArithmeticException("Faces have invalid configuration\n" + print3Array(rune));
		}
		
		
		if (orders[0] == 5) { // Bottom
			oriented =  oriented;
		} else if (orders[1] == 5) { // Left
			oriented =  frontDown(leftForward(oriented));
		} else if (orders[2] == 5) { // Front
			oriented =  frontDown(oriented);
		} else if (orders[3] == 5) { // Top
			oriented =  frontDown(frontDown(oriented));
		} else if (orders[4] == 5) { // Right
			oriented =  frontDown(leftForward(leftToRight(oriented)));
		} else if (orders[5] == 5) { // Back
			oriented =  frontDown(leftForward(leftForward(oriented)));
		} else {
			throw new ArithmeticException("Faces have invalid configuration\n" + print3Array(rune));
		}
		
		// The only face with 5 1s is now on the bottom. Check that the JK middle is 4, and that
		// the other two are 5
		
		
			
	}
	
	public static int[][][] leftForward(int[][][] rune) {
		int[][][] oriented = new int[rune.length][rune[0][0].length][rune[0].length];
		for (int i = 0; i < rune.length; ++i) {
			for (int j = 0; j < rune[0].length; ++j) {
				for (int k = 0; k < rune[0][0].length; ++k) {
					oriented[i][rune[0][0].length - 1 - k][j] = rune[i][j][k];
				}
			}
		}
		
		return oriented;
	}
	
	public static int[][][] frontDown(int[][][] rune) {
		int[][][] oriented = new int[rune[0].length][rune.length][rune[0][0].length];
		for (int i = 0; i < rune.length; ++i) {
			for (int j = 0; j < rune[0].length; ++j) {
				for (int k = 0; k < rune[0][0].length; ++k) {
					oriented[rune[0].length - 1 - j][i][k] = rune[i][j][k];
				}
			}
		}
		
		return oriented;
	}
	
	public static int[][][] leftToRight(int[][][] rune) {
		int[][][] oriented = new int[rune.length][rune[0].length][rune[0][0].length];
		for (int i = 0; i < rune.length; ++i) {
			for (int j = 0; j < rune[0].length; ++j) {
				for (int k = 0; k < rune[0][0].length; ++k) {
					oriented[i][j][rune[0][0].length - 1 - k] = rune[i][j][k];
				}
			}
		}
		
		return oriented;
	}

	public static int[][][] swap (int[][][] rune) {
		int[][][] swapped = new int[rune.length][][];
		
		for (int i = 0; i < rune.length; ++i) {
			swapped[i] = new int[rune[i].length][];
			for (int j = 0; j < rune[i].length; ++j) {
				swapped[i][j] = new int[rune[i][j].length];
				for (int k = 0; k < rune[i][j].length; ++k) {
					if (rune[i][j][k] == 0) {
						swapped[i][j][k] = 1;
					} else {
						swapped[i][j][k] = 0;
					}
				}
			}
		}
		
		return swapped;
		  
	}

	/**
	 * Retrieves each 2-orthotope (x*y array) from every orientation of a
	 * 3-orthotope (filled x*y*z array). For an X*Y*Z array, the first
	 * X elements of the result will be each Y*Z array. The next Y elements
	 * will be each Z*X array. The final Z elements will be each X*Y array.
	 * 
	 * Note, this function does not preserve strict ordering, only topological 
	 * similitude, subject to rotations and reflection. This is due to the different
	 * interpretations of viewing a 3d array.
	 * 
	 * @param rune the 3-orthotope to strip into planes
	 * @return an array lists of every plane, holding each index static in order (1st, 2nd, 3rd) 
	 * @throws IndexOutOfBoundsException when the input is not a 3-orthotope
	 */
	public static int[][][] getPlanes(int[][][] rune) throws IndexOutOfBoundsException {

		int iL = rune.length, jL = rune[0].length, kL = rune[0][0].length;

		int[][][] planes = new int[iL + jL + kL][][];

		int [][] bottomToTop = new int[jL][kL];
		int [][] frontToBack = new int[kL][iL];
		int [][] leftToRight = new int[iL][jL];

		int planeIndex = -1;
		int i, j, k;

		// Bottom to Top (viewing from Bottom, Front is up)
		for (i = 0; i < iL; ++i) {
			for (j = 0; j < jL; ++j) {
				for (k = 0; k < kL; ++k) {
					bottomToTop[j][k] = rune[i][j][k];
				}

				planes[++planeIndex] = TicTacLetters.copyOf(bottomToTop);
			}
		}

		// Front to Back (viewing from Front, Left is up)
		for (j = 0; j < jL; ++j) {
			for (k = 0; k < kL; ++k) {
				for (i = 0; i < iL; ++i) {
					frontToBack[k][i] = rune[i][j][k];
				}

				planes[++planeIndex] = TicTacLetters.copyOf(frontToBack);
			}
		}

		// Left to Right (viewing from Left, Bottom is up)
		for (k = 0; k < kL; ++k) {
			for (i = 0; i < iL; ++i) {
				for (j = 0; j < jL; ++j) {
					leftToRight[i][j] = rune[i][j][k];
				}

				planes[++planeIndex] = TicTacLetters.copyOf(leftToRight);
			}
		}




		return planes;
	}
	
	public static String print3Array(int[][][] array) {
		String s = "";
		for (int i = 0; i < array.length; ++i) {
			for (int j = 0; j < array[i].length; ++j) {
				for (int k = 0; k < array[i][j].length; ++k) {
					s += array[i][j][k] + " ";
				}
				s += "\n";
			}
			s += "\n";
		}
		
		return s;
	}
	public static void main(String[] args) {


	}

}
