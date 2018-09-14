package hurricane;

import java.util.LinkedList;

public class Arrangements {
	protected int [] arrange;
	
	public Arrangements(int s) {
		arrange = new int[s];
	}
	
	public Arrangements(Arrangements a) {
		arrange = new int[a.arrange.length];
		for (int i = 0; i < arrange.length; ++i) {
			arrange[i] = a.arrange[i];
		}
	}
	
	public boolean sumIs(int t) {
		int sum = 0;
		for (int i = 0; i < arrange.length; ++i) {
			sum += arrange[i];
		}
		
		return sum == t;
	}
	
	public static LinkedList<Arrangements> getArrangements(int total, int size) {

		LinkedList<Arrangements> arr = new LinkedList<Arrangements>();
		
		Arrangements a = new Arrangements(size);
		for (int i = 0; i < a.arrange.length; ++i) {
			a.arrange[i] = 0;
		}
		boolean overflow = false;
		while (!overflow) {
			
			if (a.sumIs(total)) {
				arr.add(new Arrangements(a));
			}
			
			overflow = true;
			for (int i = a.arrange.length-1; i >= 0 && overflow; --i) {
				
				if (a.arrange[i] == total) {
					a.arrange[i] = 0;
					overflow = true;
				} else {
					++a.arrange[i];
					overflow = false;
				}
			}
			
		}
		return arr;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LinkedList<Arrangements> arr = Arrangements.getArrangements(10,8);
		
		System.out.println(arr.size());
	}

}
