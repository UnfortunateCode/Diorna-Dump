package attack;

public class Mob {
	public final static double LUCK_CHANCE = 0.05;
	public final static double LVL_CAP = 100.0;
	public final static int DODGED = 0;
	public final static int MISS = 1;
	public final static int HIT = 2;
	public final static int SCORED = 3;
	public final static double MINV0 = 0.0;
	public final static double MINVL = 0.95;
	public final static double MINP0 = 0.1;
	public final static double MINPL = 0.98;
	public final static double MAXV0 = 0.5;
	public final static double MAXVL = 1.0;
	public final static double MAXP0 = 0.6;
	public final static double MAXPL = 1.02;
	public final static double DAMAGE_LIMIT = 10000.0;
	public final static double SPEED_LIMIT = 125.0;
	public final static double MAXSPD = 75.0;
	public final static double SPDMOD = 25.0;

	int Resist, Luck, Magic, Quickness, Strength, Precision, Guard, Vanish;
	int R, L, M, Q, S, P, G, V;
	int HP, numAtks, maxHP;
	int delay = 0;
	int level;
	Spec species = null;


	public Mob() {
		Resist = 0;
		Luck = 0;
		Magic = 0;
		Quickness = 0;
		Strength = 0;
		Precision = 0;
		Guard = 0;
		Vanish = 0;
		R = L = M = Q = S = P = G = V = 0;
		HP = 0;
		numAtks = 1;
		maxHP = 60;
	} 
	public Mob (int numPoints) {
		Resist = 0;
		Luck = 0;
		Magic = 0;
		Quickness = 0;
		Strength = 0;
		Precision = 0;
		Guard = 0;
		Vanish = 0;
		R = L = M = Q = S = P = G = V = 0;
		HP = 0;
		numAtks = 1;
		maxHP = numPoints;

		while (numPoints > 0) {
			switch ((int)(8 * Math.random())) {
			case 0:
				++R;
				break;
			case 1: 
				++L;
				break;
			case 2:
				++M;
				break;
			case 3:
				++Q;
				break;
			case 4:
				++S;
				break;
			case 5:
				++P;
				break;
			case 6:
				++G;
				break;
			case 7:
				++V;
				break;
			}
			--numPoints;
		}
		Resist = eval(R);
		Luck = eval(L);
		Magic = eval(M);
		Quickness = eval(Q);
		Strength = eval(S);
		Precision = eval(P);
		Guard = eval(G);
		Vanish = eval(V);
	}
	public Mob(int r, int l, int m, int q, int s, int p, int g, int v) {
		R = r;
		L = l;
		M = m;
		Q = q;
		S = s;
		P = p;
		G = g;
		V = v;
		Resist = eval(R);
		Luck = eval(L);
		Magic = eval(M);
		Quickness = eval(Q);
		Strength = eval(S);
		Precision = eval(P);
		Guard = eval(G);
		Vanish = eval(V);
		HP = 0;
		numAtks = 1;
		maxHP = r+l+m+q+s+p+g+v;
	}

	public Mob(Mob mob) {
		R = mob.R;
		L = mob.L;
		M = mob.M;
		Q = mob.Q;
		S = mob.S;
		P = mob.P;
		G = mob.G;
		V = mob.V;

		Resist = eval(R);
		Luck = eval(L);
		Magic = eval(M);
		Quickness = eval(Q);
		Strength = eval(S);
		Precision = eval(P);
		Guard = eval(G);
		Vanish = eval(V);
		HP = 0;
		numAtks = 1;
		maxHP = R+L+M+Q+S+P+G+V;
	}

	public Mob(Mob m1, Mob m2) {
		int f;
		if (Math.random() < 0.5) {
			f = 0;
		} else {
			f = 1;
		}
		if ((m1.R + m2.R) % 2 == 1) {
			R = f + (m1.R + m2.R) / 2;
			f = 1 - f;
		} else {
			R = (m1.R + m2.R) / 2; 
		}
		if ((m1.L + m2.L) % 2 == 1) {
			L = f + (m1.L + m2.L) / 2;
			f = 1 - f;
		} else {
			L = (m1.L+ m2.L) / 2; 
		}
		if ((m1.M + m2.M) % 2 == 1) {
			M = f + (m1.M + m2.M) / 2;
			f = 1 - f;
		} else {
			M = (m1.M + m2.M) / 2; 
		}
		if ((m1.Q + m2.Q) % 2 == 1) {
			Q = f + (m1.Q + m2.Q) / 2;
			f = 1 - f;
		} else {
			Q = (m1.Q + m2.Q) / 2; 
		}
		if ((m1.S + m2.S) % 2 == 1) {
			S = f + (m1.S + m2.S) / 2;
			f = 1 - f;
		} else {
			S = (m1.S + m2.S) / 2; 
		}
		if ((m1.P + m2.P) % 2 == 1) {
			P = f + (m1.P + m2.P) / 2;
			f = 1 - f;
		} else {
			P = (m1.P + m2.P) / 2; 
		}
		if ((m1.G + m2.G) % 2 == 1) {
			G = f + (m1.G + m2.G) / 2;
			f = 1 - f;
		} else {
			G = (m1.G + m2.G) / 2; 
		}
		if ((m1.V + m2.V) % 2 == 1) {
			V = f + (m1.V + m2.V) / 2;
			f = 1 - f;
		} else {
			V = (m1.V + m2.V) / 2; 
		}
		Resist = eval(R);
		Luck = eval(L);
		Magic = eval(M);
		Quickness = eval(Q);
		Strength = eval(S);
		Precision = eval(P);
		Guard = eval(G);
		Vanish = eval(V);
		HP = 0;
		numAtks = 1;
		maxHP = R+L+M+Q+S+P+G+V;
	}
	public void mutate() {
		int amount = 0;
		double r = Math.random();
		switch ((int)(8 * Math.random())) {
		case 0:
			amount = (int)(R * r);
			R -= amount;
			break;
		case 1: 
			amount = (int)(L * r);
			L -= amount;
			break;
		case 2:
			amount = (int)(M * r);
			M -= amount;
			break;
		case 3:
			amount = (int)(Q * r);
			Q -= amount;
			break;
		case 4:
			amount = (int)(S * r);
			S -= amount;
			break;
		case 5:
			amount = (int)(P * r);
			P -= amount;
			break;
		case 6:
			amount = (int)(G * r);
			G -= amount;
			break;
		case 7:
			amount = (int)(V * r);
			V -= amount;
			break;
		}

		switch ((int)(8 * Math.random())) {
		case 0:
			R += amount;
			break;
		case 1: 
			L += amount;
			break;
		case 2:
			M += amount;
			break;
		case 3:
			Q += amount;
			break;
		case 4:
			S += amount;
			break;
		case 5:
			P += amount;
			break;
		case 6:
			G += amount;
			break;
		case 7:
			V += amount;
			break;
		}
		Resist = eval(R);
		Luck = eval(L);
		Magic = eval(M);
		Quickness = eval(Q);
		Strength = eval(S);
		Precision = eval(P);
		Guard = eval(G);
		Vanish = eval(V);
	}

	public int eval(int points) {
		int p = points;
		int d = 1;
		int a = 0;
		while (p > a + 5) {
			a += 5;
			++d; 
			p -= a;
		}
		return p / d + a;
	}
	
	public int req(int lvl) {

		return (1+(lvl-1)/5)*(1+(lvl-1)%5)+5*(((lvl-1)/5)*(1+(lvl-1)/5)/2);
	}
	public boolean lucky() {
		double l = (Luck / LVL_CAP) * Math.log((Luck * Math.E / LVL_CAP) + (LVL_CAP - Luck) / LVL_CAP);
		l += (Luck / LVL_CAP) * Math.log((Vanish * Math.E / LVL_CAP) + (LVL_CAP - Vanish) / LVL_CAP);
		return 2*Math.random() < l;
	}
	
	public boolean useMagic(Mob b) {
		if (Magic > Strength) { 
			return ((Magic - Strength)+(Magic - b.Resist) - (Strength - b.Guard) > 0 );
		} else {
			return ((Strength - Magic) + (Strength - b.Guard) + (Magic - b.Resist) <= 0 );
		}
	}

	public int hits (Mob mob) {
		double rP = Math.random(), rV = Math.random();
		if (rP > (1-LUCK_CHANCE) || rV > (1-LUCK_CHANCE)) {
			if (rP > rV) {
				return SCORED;
			} else {
				return DODGED;
			}
		}
		double mP = (MINPL - MINP0) * Math.log((Precision * Math.E / LVL_CAP) + (LVL_CAP - Precision) / LVL_CAP) + MINP0;
		double MP = (MAXPL - MAXP0) * Math.log((Precision * Math.E / LVL_CAP) + (LVL_CAP - Precision) / LVL_CAP) + MAXP0;
		double mV = (MINVL - MINV0) * Math.log((Vanish * Math.E / LVL_CAP) + (LVL_CAP - Vanish) / LVL_CAP) + MINV0;
		double MV = (MAXVL - MAXV0) * Math.log((Vanish * Math.E / LVL_CAP) + (LVL_CAP - Vanish) / LVL_CAP) + MAXV0;
		//	 System.out.println("P: [" + mP + ", " + MP + "], V: [" + mV + ", " + MV + "]");
		rP = rP * (MP - mP) + mP;
		rV = rV * (MV - mV) + mV;
		//	 System.out.println("P: " + rP + ", V: " + rV);
		if (rP > rV) {
			return HIT;
		} else {
			return MISS;
		}
	}

	public boolean crits() {
		double l = (Luck / LVL_CAP) * Math.log((Luck * Math.E / LVL_CAP) + (LVL_CAP - Luck) / LVL_CAP);
		l += (Luck / LVL_CAP) * Math.log((Precision * Math.E / LVL_CAP) + (LVL_CAP - Precision) / LVL_CAP);
		return 2*Math.random() < l;
	}

	public int damages(Mob mob, boolean crit) {
		//double dmg = DAMAGE_LIMIT * Math.log((Strength * Math.E / LVL_CAP) + (LVL_CAP - Strength) / LVL_CAP) + Math.random() * Precision;
		double dmg = 1.6*(Strength * DAMAGE_LIMIT / (LVL_CAP * LVL_CAP)) + Math.random() * Precision;
		//System.out.println("Atk damage is " + dmg);
		//dmg -= (DAMAGE_LIMIT * Math.log((Guard * Math.E / LVL_CAP) + (LVL_CAP - Guard) / LVL_CAP) + Math.random() * Vanish);
		dmg -= (mob.Guard * DAMAGE_LIMIT / (LVL_CAP*LVL_CAP)) + Math.random() * mob.Vanish;
		//System.out.println("Def brought damage down to " + dmg);
		if (dmg < 0) {
			dmg = 0;
		}
		double lc = 1;
		if (crit) {
			lc = Math.log((Luck * Math.E / LVL_CAP) + (LVL_CAP - Luck) / LVL_CAP);
			lc = 1 + Math.max(Math.random() * (1.0 - lc), 0) + lc;
		}
		return (int)Math.round(lc * dmg);
	}

	public int spells(Mob mob) {
		//double dmg = DAMAGE_LIMIT * Math.log((Strength * Math.E / LVL_CAP) + (LVL_CAP - Strength) / LVL_CAP) + Math.random() * Precision;
		double dmg = 1.5*(Magic * DAMAGE_LIMIT / (LVL_CAP * LVL_CAP)) + Math.random() * Luck;
		//System.out.println("Atk damage is " + dmg);
		//dmg -= (DAMAGE_LIMIT * Math.log((Guard * Math.E / LVL_CAP) + (LVL_CAP - Guard) / LVL_CAP) + Math.random() * Vanish);
		dmg -= (mob.Resist * DAMAGE_LIMIT / (LVL_CAP*LVL_CAP)) + Math.random() * mob.Luck;
		//System.out.println("Def brought damage down to " + dmg);
		if (dmg < 0) {
			dmg = 0;
		}
		return (int)Math.round(dmg);
	}

	public int delayInMillis() {
		double val = Quickness + (double)Vanish / 10.0;
		//	 System.out.println("---Q + .1V = " + val);
		val = SPEED_LIMIT * Math.log((val * Math.E / LVL_CAP) + (LVL_CAP - val) / LVL_CAP);
		//	 System.out.println("which is " + val + " modded from " + SPEED_LIMIT);
		val *= ((SPDMOD * Math.random() * Luck / (LVL_CAP * LVL_CAP)) + 1);
		//	 System.out.println("With luck comes to " + val);
		//	 System.out.println("Max change was " + (1 + SPDMOD * Luck / (LVL_CAP * LVL_CAP)) + "x");
		numAtks = 1;
		while (val > MAXSPD) {
			//		 System.out.println(val + " is > " + MAXSPD + " reducing by " + SPDMOD);
			val -= SPDMOD;
			++numAtks;
		}
		//	 System.out.println("This is " + (MAXSPD + SPDMOD - val));
		return (int)Math.round( (MAXSPD + SPDMOD - val) * 10);
	}

	public String toString() {
		String s = "";
		s += "[R: " + Resist + ", L: " + Luck + ", M: " + Magic;
		s += ", Q: " + Quickness + ", S: " + Strength + ", P: ";
		s += Precision + ", G: " + Guard + ", V: " + Vanish + "]";
		return s;
	}

	public void addRandomPoints(int i) {
		maxHP += i;
		while (i > 0) {
			switch ((int)(8 * Math.random())) {
			case 0:
				++R;
				break;
			case 1: 
				++L;
				break;
			case 2:
				++M;
				break;
			case 3:
				++Q;
				break;
			case 4:
				++S;
				break;
			case 5:
				++P;
				break;
			case 6:
				++G;
				break;
			case 7:
				++V;
				break;
			}
			--i;
		}
		Resist = eval(R);
		Luck = eval(L);
		Magic = eval(M);
		Quickness = eval(Q);
		Strength = eval(S);
		Precision = eval(P);
		Guard = eval(G);
		Vanish = eval(V);

	}
}
