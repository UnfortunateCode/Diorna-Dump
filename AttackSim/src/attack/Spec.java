package attack;

import java.util.LinkedList;

public class Spec {

	public static int currSpecID = -1;
	int ID;
	int num;
	int topLevel;
	int maxLevel;
	LinkedList<Spec> connects;
	LinkedList<Spec> attacks;
	LinkedList<Mob> byLevel;
	Map mm;
	
	public Spec(Map m) {
		mm = m;
		ID = mm.specs.size();
		
		mm.specs.add(this);
		num = 1;
		connects = new LinkedList<Spec>();
		attacks = new LinkedList<Spec>();
		byLevel = new LinkedList<Mob>();
		topLevel = -1;
		maxLevel = 0;
	}
	public String toString() {
		return "" + ID;
	}
	public void atLevel(int l) {
		topLevel = l;
	}
}
