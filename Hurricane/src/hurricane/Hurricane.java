package hurricane;

import java.util.LinkedList;
import java.util.TreeSet;

public class Hurricane {
	public static final int H = 0;
	public static final int A = 1;
	public static final int B = 2;
	public static final int F = 3;
	public static final int D = 4;
	public static final int E = 5;
	public static final int W = 6;
	public static final int I = 7;

	private int ring;

	public Hurricane() {
		ring = 0;
	}

	public boolean incr() {
		if (ring == 7) {
			ring = 0;
			return true;
		} else {
			++ring;
			return false;
		}
	}

	public String toString() {
		switch(ring){
		case H:
			return "H";
		case A:
			return "A";
		case B:
			return "B";
		case F:
			return "F";
		case D:
			return "D";
		case E:
			return "E";
		case W:
			return "W";
		case I:
			return "I";
		}
		return "N";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Hurricane[] spell = new Hurricane[10];
		int[] num = new int[11];
		int s, countinc;
		int[] elem, baseElem;
		elem = new int[8];
		baseElem = new int[8];
		int basecounter = 0;
		LinkedList<Arrangements> bases = Arrangements.getArrangements(10,8);
		Arrangements arr;
		int[] numspells = new int[bases.size()];

		int i, count = 0;
		for (i = 0; i < numspells.length; ++i) {
			numspells[i] = 0;
		}
		for (i = 0; i < 10; ++i) {
			spell[i] = new Hurricane();
			num[i] = 0;
		}
		/*
for (basecounter = 0; basecounter < bases.size(); ++basecounter) {
	arr = bases.get(basecounter);
	System.out.print("[ ");
	for (int k = 0; k < 8; ++k) {
		elem[k] = arr.arrange[k];
		System.out.print(elem[k] + " ");
	}
	System.out.print("]: ");*/

		count = 0;
		countinc = 1;
		for (i = 0; i < 10; ++i) {
			spell[i] = new Hurricane();
			num[i] = 0;
		}
		//System.out.print(basew + "" + basea + "" + baseb + ": ");
		//System.out.println("Casting every set of 10 rings of W, A, and B");
		//System.out.println("With powerspell of " + basew + ", " + basea + ", " + baseb);
		boolean overflow = false;
		while (!overflow) {

			++count;
			if (count % countinc == 0) {
				System.err.println(count);
				countinc *= 10;
			}
			for (basecounter = 0; basecounter < bases.size(); ++basecounter) {
				arr = bases.get(basecounter);
				for (int k = 0; k < 8; ++k) {
					elem[k] = arr.arrange[k];
				}

				i = 0;
				s = 0;
				for (int k = 0; k < 8; ++k) {
					elem[k] = arr.arrange[k];
				}
				for (i = 0; i < 10; ++i) {
					if (elem[spell[i].ring] == 0) {
						for (int k = 0; k < 8; ++k) {
							elem[k] = arr.arrange[k];
						}
						s = 0;
					} else {
						--elem[spell[i].ring];
						++s;
					}
				}
				if (s == 10) {
					++numspells[basecounter];
					break;
				}
			}
			overflow = true;
			for (i = 9; i >= 0 && overflow; --i) {
				overflow = spell[i].incr();
			}

		}
		
		count = 0;
		for (basecounter = 0; basecounter < numspells.length; ++basecounter) {
			count += numspells[basecounter];
		}
		for (basecounter = 0; basecounter < bases.size(); ++basecounter) {
			arr = bases.get(basecounter);
			System.out.print("[ ");
			for (int k = 0; k < 8; ++k) {
				System.out.print(arr.arrange[k] + " ");
			}
			System.out.print("]: ");
			System.out.print((Math.round(((10000.0*(double)numspells[basecounter])/(double)count))/100.0) + "% ");
			System.out.println(numspells[basecounter]);
			
		}

		//for (i = 0; i < 11; ++i) {
		//			System.out.println(i + ": " + num[i] + " times or " + ((100.0*(double)num[i])/(double)count) + "%");
		//}
		System.out.print((Math.round(((10000.0*(double)num[10])/(double)count))/100.0) + "% ");
		//		System.out.println(count + " total sets of 10 cast.\n");

		//		System.out.println("To cast every incarnation of Hurricane,");
		//		System.out.println("you need this number of Nspells");
		/*LinkedList<String> ts, ts9, ts10;
		ts9 = new LinkedList<String>();
		ts10 = new LinkedList<String>();
		String temp;
		for (i = 9; i < 10; ++i) {
			spell = new Hurricane[i];
			for (int j = 0; j < i; ++j) {
				spell[j] = new Hurricane();
			}
			ts = new LinkedList<String>();


			for (int k = 0; k < 8; ++k) {
				elem[k] = arr.arrange[k];
			}
			overflow = false;
			boolean valid = true;
			while (!overflow) {
				valid = true;
				for (int k = 0; k < 8; ++k) {
					elem[k] = arr.arrange[k];
				}
				temp = "";
				for (int j = 0; j < i && valid; ++j) {
					if (elem[spell[j].ring] == 0) {
						valid = false;
						continue;
					} else {
						--elem[spell[j].ring];
						temp += spell[j];
					}

				}
				//System.out.println();
				if (valid) {
					ts.add(temp);
				}

				overflow = true;
				for (int j = spell.length-1; j >= 0 && overflow; --j) {
					overflow = spell[j].incr();
				}

			}

//			System.out.println(i + ": " + ts.size() + " spells.");
			if (i == 9) {
				System.out.println(ts.size());
			}
			/*for (int j = 0; j < ts.size(); ++j) {
				System.out.println(ts.get(j));
			}

			if (i == 9) {
				ts9 = ts;
			}

			if (i == 10) {
				ts10 = ts;
			}*/
		/*
		for (int j = 0; j < ts9.size(); ++j) {
			System.out.println(ts9.get(j) + " vs " + ts10.get(j).substring(0,9));
		}*/

	}
}
