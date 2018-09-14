package attack;

import java.io.FileWriter;

public class Simulate {

	Mob [] mobs;
	int num;


	public Simulate (int i) {
		mobs = new Mob[i];
		num = i;
	}

	public Simulate (Simulate s) {
		mobs = new Mob[s.mobs.length];
		for (int i = 0; i < mobs.length; ++i) {
			mobs[i] = new Mob(s.mobs[i]);
		}
	}
	
	public Simulate (Mob[] m) {
		mobs = new Mob[m.length];
		for (int i = 0; i < m.length; ++i) {
			mobs[i] = new Mob(m[i]);
		}
	}
	
	public static boolean useMagic(Mob a, Mob b) {
		if (a.Magic > a.Strength) { 
			return ((a.Magic - a.Strength)+(a.Magic - b.Resist) - (a.Strength - b.Guard) > 0 );
		} else {
			return ((a.Strength - a.Magic) + (a.Strength - b.Guard) + (a.Magic - b.Resist) <= 0 );
		}
	}

	public static void battle(Mob a, Mob b, boolean disp) {
		Mob atk = a, def = b;
		double r;
		int turnCount = 100;
		boolean isCrit = false;
		int dmg;
		if(disp) {System.out.println("BATTLE");}
		a.delay = a.delayInMillis();
		b.delay = b.delayInMillis();
		r = Math.random();
		if (r < 0.05) {
			// first attack
			b.delay += b.delayInMillis();
			if (disp) {System.out.println("Sneak Attack");}
		} else if (r > 0.95) {
			// back attack
			a.delay += a.delayInMillis();
			if (disp) {System.out.println("Off Guard");}
		}

		turnCount = 100;
		while (a.HP > 0 && b.HP > 0 && --turnCount > 0) {
			if (turnCount < 90) {
				if (Math.random() < 0.05) {
					break;
				}
			}
			if (disp) {System.out.println("Turns: A-" + a.delay + ", B-" + b.delay);}
			if (a.delay <= b.delay) {
				if (disp) {
					System.out.print("A ");
				}
				atk = a;
				def = b;
			} else {
				if (disp) {
					System.out.print("B ");
				}
				atk = b;
				def = a;
			}
			def.delay -= atk.delay;
			atk.delay = atk.delayInMillis();
			if (disp) {

				switch (atk.numAtks){
				case 1: 
					System.out.println("Attacks");
					break;
				case 2:
					System.out.println("Double Attacks");
					break;
				case 3:
					System.out.println("Triple Attacks");
					break;
				case 4:
					System.out.println("Quad Attacks");
					break;
				default:
					System.out.println("Attacks " + atk.numAtks + " times");	
				}
			
			}
			while (def.HP > 0 && --atk.numAtks >= 0) {
				if (def.lucky()) {
					if (disp) {System.out.println("Lucky Miss");}
					continue;
				}
				if (Simulate.useMagic(atk, def)) {//atk.Magic - def.Resist > atk.Strength - def.Guard){
					if (disp) {System.out.println("Magic Cast");}
					dmg = atk.spells(def);	
				} else {
					switch(atk.hits(def)) {
					case Mob.DODGED:
						if (disp) {System.out.println("Dodged");}
						continue;
					case Mob.MISS:
						if (disp) {System.out.println("Missed");}
						continue;
					case Mob.HIT:
						if (disp) {System.out.println("Hit");}
						break;
					case Mob.SCORED:
						if (disp) {System.out.println("Scored");}
						break;
					}
					isCrit = a.crits();
					if (isCrit) {
						if (disp) {System.out.println("Critical Hit!!");}
					}
					dmg = atk.damages(def, isCrit);
				}
				def.HP -= dmg;
				if (def.HP < 0) {
					def.HP = 0;
				}
				if (disp) {System.out.println(dmg + " damage dealt");}
				if (disp) {System.out.println("HP: A-" + a.HP + ", B-" + b.HP);}
			}


		}
	}

	public static Mob[] scaleSeedsTo(int nVal) {
		double factor = (double)nVal / 100.0;
		Mob []ss = new Mob[seeds.length];
		for (int i = 0; i < seeds.length; ++i) {
			ss[i] = new Mob((int)(seeds[i].R * factor),
					(int)(seeds[i].L * factor),
					(int)(seeds[i].M * factor),
					(int)(seeds[i].Q * factor),
					(int)(seeds[i].S * factor),
					(int)(seeds[i].P * factor),
					(int)(seeds[i].G * factor),
					(int)(seeds[i].V * factor));
			ss[i].maxHP = nVal;
		}
		return ss;
	}
	
	public static Mob[] coEvolve(Simulate []sims, MapMaker m, boolean disp) {
		// Setup
		Simulate []s2 = new Simulate[sims.length];
		int i, j, k, gen;
		int []iWin = new int[sims.length];
		int []sWin = new int[sims.length];
		int numGens = 100;
		Mob[][] bestS = new Mob[sims.length][numGens];
		Mob[] best = new Mob[sims.length];
		int [][] wins = new int[sims.length][];
		for (i = 0; i < sims.length; ++i) {
			wins[i] = new int[sims[i].mobs.length];
		}
		Mob a, b, l, w;
		double r, r2;
		int p, p2;
		
		// Action
		for (gen = 0; gen < numGens; ++gen) {
			if (disp) {System.out.println("Generation: " + gen);}
			
			// Set all Mobs to max HP
			for (i = 0; i < sims.length; ++i) {
				for (j = 0; j < sims[i].mobs.length; ++j) {
					wins[i][j] = 0;
					sims[i].mobs[j].HP = sims[i].mobs[j].maxHP;
				}
			}
			
			// Perform battles
			for (Spec atk : m.specs) {
				for (Spec def : atk.attacks) {
					if (disp) {
						System.out.println(atk + " vs " + def);
					}
					for (j = 0; j < sims[atk.ID].mobs.length; ++j) {
						for (k = 0; k < sims[def.ID].mobs.length; ++k) {
							Simulate.battle(sims[atk.ID].mobs[j], sims[def.ID].mobs[k], false);
							if (sims[def.ID].mobs[k].HP == 0) {
								++wins[atk.ID][j];
							} else if (sims[atk.ID].mobs[j].HP != 0) {
								++wins[def.ID][k];
							}
							sims[atk.ID].mobs[j].HP = sims[atk.ID].mobs[j].maxHP;
							sims[def.ID].mobs[k].HP = sims[def.ID].mobs[k].maxHP;
							
						}
					}
				}
			} // end Perform battles
			
			// clear out iWin and sWin
			for (i = 0; i < sims.length; ++i) {
				iWin[i] = 0;
				sWin[i] = 0;
			}
			
			// determine where most wins are in each species
			for (i = 0; i < sims.length; ++i) {
				for (j = 0; j < sims[i].mobs.length; ++j) {
					if (wins[i][j] > wins[i][iWin[i]]) {
						iWin[i] = j;
					}
					sWin[i] += wins[i][j];
				}
			}
			
			// set best of Gen for each species
			for (i = 0; i < sims.length; ++i) {
				if (disp) {
					System.out.print(i + " best of Gen " + gen + " with ");
					System.out.print(wins[i][iWin[i]] + " is ");
					System.out.println(sims[i].mobs[iWin[i]]);
				}
				bestS[i][gen] = new Mob(sims[i].mobs[iWin[i]]);
				
				
			}
			
			// create the new Generation from old
			s2 = new Simulate[sims.length];
			for (i = 0; i < sims.length; ++i) {
				s2[i] = new Simulate(sims[i]);
				s2[i].mobs = new Mob[sims[i].mobs.length];
				
				for (j = 0; j < 0.5 * s2[i].mobs.length; ++j) {
					r = sWin[i] * Math.random();
					for (k = 0; k < wins[i].length; ++k) {
						r -= wins[i][k];
						if (r <= 0) {
							break;
						}
					}
					s2[i].mobs[j] = new Mob(sims[i].mobs[k]);
					if (Math.random() < 0.05) {
						s2[i].mobs[j].mutate();
					}
				}
				for (; j < s2[i].mobs.length; ++j) {
					p = 0;
					p2 = 0;
					r = sWin[i] * Math.random();
					r2 = sWin[i] * Math.random();
					for (k = 0; k < wins[i].length && (r > 0 || r2 > 0); ++k) {
						if (r > 0) {
							r -= wins[i][k];
							if (r <= 0) {
								p = k;
							}
						}
						if (r2 > 0) {
							r2 -= wins[i][k];
							if (r2 <= 0) {
								p2 = k;
							}
						}
					}
					s2[i].mobs[j] = new Mob(sims[i].mobs[p], sims[i].mobs[p2]);
					if (Math.random() < 0.05) {
						s2[i].mobs[j].mutate();
					}
				}
			}
			
			// set old gen to new
			for (i = 0; i < sims.length; ++i) {
				sims[i] = s2[i];
			}
		} // End of Generations loop
		
		for (i = 0; i < sims.length; ++i) {
			best[i] = bestS[i][numGens-1];
		}
		return best;
	}

	
	public static Mob [] seeds = {
			new Mob( 1, 1, 1, 1, 1, 1, 1, 1),
			new Mob(93, 1, 1, 1, 1, 1, 1, 1),
			new Mob( 1,93, 1, 1, 1, 1, 1, 1),
			new Mob( 1, 1,93, 1, 1, 1, 1, 1),
			new Mob( 1, 1, 1,93, 1, 1, 1, 1),
			new Mob( 1, 1, 1, 1,93, 1, 1, 1),
			new Mob( 1, 1, 1, 1, 1,93, 1, 1),
			new Mob( 1, 1, 1, 1, 1, 1,93, 1),
			new Mob( 1, 1, 1, 1, 1, 1, 1,93),
			new Mob(47,47, 1, 1, 1, 1, 1, 1),
			new Mob(47, 1,47, 1, 1, 1, 1, 1),
			new Mob(47, 1, 1,47, 1, 1, 1, 1),
			new Mob(47, 1, 1, 1,47, 1, 1, 1),
			new Mob(47, 1, 1, 1, 1,47, 1, 1),
			new Mob(47, 1, 1, 1, 1, 1,47, 1),
			new Mob(47, 1, 1, 1, 1, 1, 1,47),
			new Mob( 1,47,47, 1, 1, 1, 1, 1),
			new Mob( 1,47, 1,47, 1, 1, 1, 1),
			new Mob( 1,47, 1, 1,47, 1, 1, 1),
			new Mob( 1,47, 1, 1, 1,47, 1, 1),
			new Mob( 1,47, 1, 1, 1, 1,47, 1),
			new Mob( 1,47, 1, 1, 1, 1, 1,47),
			new Mob( 1, 1,47,47, 1, 1, 1, 1),
			new Mob( 1, 1,47, 1,47, 1, 1, 1),
			new Mob( 1, 1,47, 1, 1,47, 1, 1),
			new Mob( 1, 1,47, 1, 1, 1,47, 1),
			new Mob( 1, 1,47, 1, 1, 1, 1,47),
			new Mob( 1, 1, 1,47,47, 1, 1, 1),
			new Mob( 1, 1, 1,47, 1,47, 1, 1),
			new Mob( 1, 1, 1,47, 1, 1,47, 1),
			new Mob( 1, 1, 1,47, 1, 1, 1,47),
			new Mob( 1, 1, 1, 1,47,47, 1, 1),
			new Mob( 1, 1, 1, 1,47, 1,47, 1),
			new Mob( 1, 1, 1, 1,47, 1, 1,47),
			new Mob( 1, 1, 1, 1, 1,47,47, 1),
			new Mob( 1, 1, 1, 1, 1,47, 1,47),
			new Mob( 1, 1, 1, 1, 1, 1,47,47),
			new Mob(31,32,32, 1, 1, 1, 1, 1),
			new Mob(31,32, 1,32, 1, 1, 1, 1),
			new Mob(31,32, 1, 1,32, 1, 1, 1),
			new Mob(31,32, 1, 1, 1,32, 1, 1),
			new Mob(31,32, 1, 1, 1, 1,32, 1),
			new Mob(31,32, 1, 1, 1, 1, 1,32),
			new Mob(31, 1,32,32, 1, 1, 1, 1),
			new Mob(31, 1,32, 1,32, 1, 1, 1),
			new Mob(31, 1,32, 1, 1,32, 1, 1),
			new Mob(31, 1,32, 1, 1, 1,32, 1),
			new Mob(31, 1,32, 1, 1, 1, 1,32),
			new Mob(31, 1, 1,32,32, 1, 1, 1),
			new Mob(31, 1, 1,32, 1,32, 1, 1),
			new Mob(31, 1, 1,32, 1, 1,32, 1),
			new Mob(31, 1, 1,32, 1, 1, 1,32),
			new Mob(31, 1, 1, 1,32,32, 1, 1),
			new Mob(31, 1, 1, 1,32, 1,32, 1),
			new Mob(31, 1, 1, 1,32, 1, 1,32),
			new Mob(31, 1, 1, 1, 1,32,32, 1),
			new Mob(31, 1, 1, 1, 1,32, 1,32),
			new Mob(31, 1, 1, 1, 1, 1,32,32),
			new Mob( 1,31,32,32, 1, 1, 1, 1),
			new Mob( 1,31,32, 1,32, 1, 1, 1),
			new Mob( 1,31,32, 1, 1,32, 1, 1),
			new Mob( 1,31,32, 1, 1, 1,32, 1),
			new Mob( 1,31,32, 1, 1, 1, 1,32),
			new Mob( 1,31, 1,32,32, 1, 1, 1),
			new Mob( 1,31, 1,32, 1,32, 1, 1),
			new Mob( 1,31, 1,32, 1, 1,32, 1),
			new Mob( 1,31, 1,32, 1, 1, 1,32),
			new Mob( 1,31, 1, 1,32,32, 1, 1),
			new Mob( 1,31, 1, 1,32, 1,32, 1),
			new Mob( 1,31, 1, 1,32, 1, 1,32),
			new Mob( 1,31, 1, 1, 1,32,32, 1),
			new Mob( 1,31, 1, 1, 1,32, 1,32),
			new Mob( 1,31, 1, 1, 1, 1,32,32),
			new Mob( 1, 1,31,32,32, 1, 1, 1),
			new Mob( 1, 1,31,32, 1,32, 1, 1),
			new Mob( 1, 1,31,32, 1, 1,32, 1),
			new Mob( 1, 1,31,32, 1, 1, 1,32),
			new Mob( 1, 1,31, 1,32,32, 1, 1),
			new Mob( 1, 1,31, 1,32, 1,32, 1),
			new Mob( 1, 1,31, 1,32, 1, 1,32),
			new Mob( 1, 1,31, 1, 1,32,32, 1),
			new Mob( 1, 1,31, 1, 1,32, 1,32),
			new Mob( 1, 1,31, 1, 1, 1,32,32),
			new Mob( 1, 1, 1,31,32,32, 1, 1),
			new Mob( 1, 1, 1,31,32, 1,32, 1),
			new Mob( 1, 1, 1,31,32, 1, 1,32),
			new Mob( 1, 1, 1,31, 1,32,32, 1),
			new Mob( 1, 1, 1,31, 1,32, 1,32),
			new Mob( 1, 1, 1,31, 1, 1,32,32),
			new Mob( 1, 1, 1, 1,31,32,32, 1),
			new Mob( 1, 1, 1, 1,31,32, 1,32),
			new Mob( 1, 1, 1, 1,31, 1,32,32),
			new Mob( 1, 1, 1, 1, 1,31,32,32),
			new Mob(24,24,24,24, 1, 1, 1, 1),
			new Mob(24,24,24, 1,24, 1, 1, 1),
			new Mob(24,24,24, 1, 1,24, 1, 1),
			new Mob(24,24,24, 1, 1, 1,24, 1),
			new Mob(24,24,24, 1, 1, 1, 1,24),
			new Mob(24,24, 1,24,24, 1, 1, 1),
			new Mob(24,24, 1,24, 1,24, 1, 1),
			new Mob(24,24, 1,24, 1, 1,24, 1),
			new Mob(24,24, 1,24, 1, 1, 1,24),
			new Mob(24,24, 1, 1,24,24, 1, 1),
			new Mob(24,24, 1, 1,24, 1,24, 1),
			new Mob(24,24, 1, 1,24, 1, 1,24),
			new Mob(24,24, 1, 1, 1,24,24, 1),
			new Mob(24,24, 1, 1, 1,24, 1,24),
			new Mob(24,24, 1, 1, 1, 1,24,24),
			new Mob(24, 1,24,24,24, 1, 1, 1),
			new Mob(24, 1,24,24, 1,24, 1, 1),
			new Mob(24, 1,24,24, 1, 1,24, 1),
			new Mob(24, 1,24,24, 1, 1, 1,24),
			new Mob(24, 1,24, 1,24,24, 1, 1),
			new Mob(24, 1,24, 1,24, 1,24, 1),
			new Mob(24, 1,24, 1,24, 1, 1,24),
			new Mob(24, 1,24, 1, 1,24,24, 1),
			new Mob(24, 1,24, 1, 1,24, 1,24),
			new Mob(24, 1,24, 1, 1, 1,24,24),
			new Mob(24, 1, 1,24,24,24, 1, 1),
			new Mob(24, 1, 1,24,24, 1,24, 1),
			new Mob(24, 1, 1,24,24, 1, 1,24),
			new Mob(24, 1, 1,24, 1,24,24, 1),
			new Mob(24, 1, 1,24, 1,24, 1,24),
			new Mob(24, 1, 1,24, 1, 1,24,24),
			new Mob(24, 1, 1, 1,24,24,24, 1),
			new Mob(24, 1, 1, 1,24,24, 1,24),
			new Mob(24, 1, 1, 1,24, 1,24,24),
			new Mob(24, 1, 1, 1, 1,24,24,24),
			new Mob( 1,24,24,24,24, 1, 1, 1),
			new Mob( 1,24,24,24, 1,24, 1, 1),
			new Mob( 1,24,24,24, 1, 1,24, 1),
			new Mob( 1,24,24,24, 1, 1, 1,24),
			new Mob( 1,24,24, 1,24,24, 1, 1),
			new Mob( 1,24,24, 1,24, 1,24, 1),
			new Mob( 1,24,24, 1,24, 1, 1,24),
			new Mob( 1,24,24, 1, 1,24,24, 1),
			new Mob( 1,24,24, 1, 1,24, 1,24),
			new Mob( 1,24,24, 1, 1, 1,24,24),
			new Mob( 1,24, 1,24,24,24, 1, 1),
			new Mob( 1,24, 1,24,24, 1,24, 1),
			new Mob( 1,24, 1,24,24, 1, 1,24),
			new Mob( 1,24, 1,24, 1,24,24, 1),
			new Mob( 1,24, 1,24, 1,24, 1,24),
			new Mob( 1,24, 1,24, 1, 1,24,24),
			new Mob( 1,24, 1, 1,24,24,24, 1),
			new Mob( 1,24, 1, 1,24,24, 1,24),
			new Mob( 1,24, 1, 1,24, 1,24,24),
			new Mob( 1,24, 1, 1, 1,24,24,24),
			new Mob( 1, 1,24,24,24,24, 1, 1),
			new Mob( 1, 1,24,24,24, 1,24, 1),
			new Mob( 1, 1,24,24,24, 1, 1,24),
			new Mob( 1, 1,24,24, 1,24,24, 1),
			new Mob( 1, 1,24,24, 1,24, 1,24),
			new Mob( 1, 1,24,24, 1, 1,24,24),
			new Mob( 1, 1,24, 1,24,24,24, 1),
			new Mob( 1, 1,24, 1,24,24, 1,24),
			new Mob( 1, 1,24, 1,24, 1,24,24),
			new Mob( 1, 1,24, 1, 1,24,24,24),
			new Mob( 1, 1, 1,24,24,24,24, 1),
			new Mob( 1, 1, 1,24,24,24, 1,24),
			new Mob( 1, 1, 1,24,24, 1,24,24),
			new Mob( 1, 1, 1,24, 1,24,24,24),
			new Mob( 1, 1, 1, 1,24,24,24,24),
			new Mob(19,19,19,20,20, 1, 1, 1),
			new Mob(19,19,19,20, 1,20, 1, 1),
			new Mob(19,19,19,20, 1, 1,20, 1),
			new Mob(19,19,19,20, 1, 1, 1,20),
			new Mob(19,19,19, 1,20,20, 1, 1),
			new Mob(19,19,19, 1,20, 1,20, 1),
			new Mob(19,19,19, 1,20, 1, 1,20),
			new Mob(19,19,19, 1, 1,20,20, 1),
			new Mob(19,19,19, 1, 1,20, 1,20),
			new Mob(19,19,19, 1, 1, 1,20,20),
			new Mob(19,19, 1,19,20,20, 1, 1),
			new Mob(19,19, 1,19,20, 1,20, 1),
			new Mob(19,19, 1,19,20, 1, 1,20),
			new Mob(19,19, 1,19, 1,20,20, 1),
			new Mob(19,19, 1,19, 1,20, 1,20),
			new Mob(19,19, 1,19, 1, 1,20,20),
			new Mob(19,19, 1, 1,19,20,20, 1),
			new Mob(19,19, 1, 1,19,20, 1,20),
			new Mob(19,19, 1, 1,19, 1,20,20),
			new Mob(19,19, 1, 1, 1,19,20,20),
			new Mob(19, 1,19,19,20,20, 1, 1),
			new Mob(19, 1,19,19,20, 1,20, 1),
			new Mob(19, 1,19,19,20, 1, 1,20),
			new Mob(19, 1,19,19, 1,20,20, 1),
			new Mob(19, 1,19,19, 1,20, 1,20),
			new Mob(19, 1,19,19, 1, 1,20,20),
			new Mob(19, 1,19, 1,19,20,20, 1),
			new Mob(19, 1,19, 1,19,20, 1,20),
			new Mob(19, 1,19, 1,19, 1,20,20),
			new Mob(19, 1,19, 1, 1,19,20,20),
			new Mob(19, 1, 1,19,19,20,20, 1),
			new Mob(19, 1, 1,19,19,20, 1,20),
			new Mob(19, 1, 1,19,19, 1,20,20),
			new Mob(19, 1, 1,19, 1,19,20,20),
			new Mob(19, 1, 1, 1,19,19,20,20),
			new Mob( 1,19,19,19,20,20, 1, 1),
			new Mob( 1,19,19,19,20, 1,20, 1),
			new Mob( 1,19,19,19,20, 1, 1,20),
			new Mob( 1,19,19,19, 1,20,20, 1),
			new Mob( 1,19,19,19, 1,20, 1,20),
			new Mob( 1,19,19,19, 1, 1,20,20),
			new Mob( 1,19,19, 1,19,20,20, 1),
			new Mob( 1,19,19, 1,19,20, 1,20),
			new Mob( 1,19,19, 1,19, 1,20,20),
			new Mob( 1,19,19, 1, 1,19,20,20),
			new Mob( 1,19, 1,19,19,20,20, 1),
			new Mob( 1,19, 1,19,19,20, 1,20),
			new Mob( 1,19, 1,19,19, 1,20,20),
			new Mob( 1,19, 1,19, 1,19,20,20),
			new Mob( 1,19, 1, 1,19,19,20,20),
			new Mob( 1, 1,19,19,19,20,20, 1),
			new Mob( 1, 1,19,19,19,20, 1,20),
			new Mob( 1, 1,19,19,19, 1,20,20),
			new Mob( 1, 1,19,19, 1,19,20,20),
			new Mob( 1, 1,19, 1,19,19,20,20),
			new Mob( 1, 1, 1,19,19,19,20,20),
			new Mob(16,16,16,16,17,17, 1, 1),
			new Mob(16,16,16,16,17, 1,17, 1),
			new Mob(16,16,16,16,17, 1, 1,17),
			new Mob(16,16,16,16, 1,17,17, 1),
			new Mob(16,16,16,16, 1,17, 1,17),
			new Mob(16,16,16,16, 1, 1,17,17),
			new Mob(16,16,16, 1,16,17,17, 1),
			new Mob(16,16,16, 1,16,17, 1,17),
			new Mob(16,16,16, 1,16, 1,17,17),
			new Mob(16,16,16, 1, 1,16,17,17),
			new Mob(16,16, 1,16,16,17,17, 1),
			new Mob(16,16, 1,16,16,17, 1,17),
			new Mob(16,16, 1,16,16, 1,17,17),
			new Mob(16,16, 1,16, 1,16,17,17),
			new Mob(16,16, 1, 1,16,16,17,17),
			new Mob(16, 1,16,16,16,17,17, 1),
			new Mob(16, 1,16,16,16,17, 1,17),
			new Mob(16, 1,16,16,16, 1,17,17),
			new Mob(16, 1,16,16, 1,16,17,17),
			new Mob(16, 1,16, 1,16,16,17,17),
			new Mob(16, 1, 1,16,16,16,17,17),
			new Mob( 1,16,16,16,16,17,17, 1),
			new Mob( 1,16,16,16,16,17, 1,17),
			new Mob( 1,16,16,16,16, 1,17,17),
			new Mob( 1,16,16,16, 1,16,17,17),
			new Mob( 1,16,16, 1,16,16,17,17),
			new Mob( 1,16, 1,16,16,16,17,17),
			new Mob( 1, 1,16,16,16,16,17,17),
			new Mob(14,14,14,14,14,14,15, 1),
			new Mob(14,14,14,14,14,14, 1,15),
			new Mob(14,14,14,14,14, 1,14,15),
			new Mob(14,14,14,14, 1,14,14,15),
			new Mob(14,14,14, 1,14,14,14,15),
			new Mob(14,14, 1,14,14,14,14,15),
			new Mob(14, 1,14,14,14,14,14,15),
			new Mob( 1,14,14,14,14,14,14,15),
			new Mob(12,12,12,12,13,13,13,13)};


	public static int lvlUpPointsFor(int i) {
		return (i-1)/5 + (i-1)/10 + 5;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		MapMaker m = new MapMaker(70,20);
		System.out.println(m.numTowns + " town(s) on this map");
		while (m.numTowns > 10 || m.numTowns < 4) {
			m = new MapMaker(70, 20);
			System.out.println("This map has " + m.numTowns + " town(s)");
		}
		for (int i = 0; i < m.complexity.length; ++i) {
			for (int j = 0; j < m.complexity[0].length; ++j) {
				if (m.complexity[i][j] < 0) {
					System.out.print("#");
				} else {
					System.out.print(m.complexity[i][j] + "");
				}
			}
			System.out.println();
		}
		System.out.println();
		m.setPaths(false);
		System.out.println();
		m.display(true);
		//m.build();
		//System.out.println(m);
		//m.getAttackers(false);
		//System.out.print(1);

		System.out.println();
		m.findLvls(true);
		m.build();
		m.setSpecLevels(false);
		m.getAttackers(false);
		System.out.println(m);


		
		int mobPoints = 60;
		int maxLevel = 0;
		for (Spec s : m.specs){
			if (s.maxLevel > maxLevel) {
				maxLevel = s.maxLevel;
			}
		}
		Mob[] ss = Simulate.scaleSeedsTo(mobPoints);
		Simulate [] sims = new Simulate[m.specs.size()];
		
		m.writeTo();
		
		for (int i = 0; i < sims.length; ++i) {
			sims[i] = new Simulate(ss);
		}
		
		Mob[][] levels = new Mob[maxLevel][sims.length];
		levels[0] = coEvolve(sims, m, true);
		m.writeLevel(0, levels[0]);
		for (int i = 0; i < levels[0].length; ++i) {
			m.specs.get(i).byLevel.add(levels[0][i]);
			levels[0][i].level = 0;
			levels[0][i].species = m.specs.get(i);
		}
		
		for (int i = 1; i < maxLevel; ++i) {
			
			// create a new mob list for each species, based on
			// the best of the previous level, and added points
			// to level up.
			for (int j = 0; j < sims.length; ++j) {
				for (int k = 0; k < sims[j].mobs.length; ++k) {
					sims[j].mobs[k] = new Mob(levels[i-1][j]);
					sims[j].mobs[k].addRandomPoints(Simulate.lvlUpPointsFor(i)); 
				}
			}
			
			// find the best of this level
			levels[i] = coEvolve(sims, m, true);
			m.writeLevel(i, levels[i]);
			for (int j = 0; j < levels[i].length; ++j) {
				m.specs.get(j).byLevel.add(levels[i][j]);
				levels[i][j].level = i;
				levels[i][j].species = m.specs.get(j);
			}
		}
		
		for (int i = 0; i < levels[0].length; ++i) {
			System.out.println("Species " + i);
			for (int j = 0; j < levels.length; ++j) {
				System.out.println("Lvl " + j + ": " + levels[j][i]);
			}
		}
		
		/*Simulate [] s2 = new Simulate[m.specs.size()];
		int i, j, gen, k;
		int []iWin = new int[m.specs.size()];
		int []sWin = new int[m.specs.size()];
		int mobPoints = 60;
		int numGens = 100;
		Mob[] ss = Simulate.scaleSeedsTo(mobPoints);
		Mob[][] bestS = new Mob[m.specs.size()][numGens];
		int []bestI = new int[m.specs.size()];
		//*
		for (i = 0; i < m.specs.size(); ++i) {
			sims[i] = new Simulate(ss);
			bestI[i] = 0;
		}//
		
		int [][]wins = new int[m.specs.size()][ss.length];
		Mob a, b, l, w;
		double r, r2;
		int p, p2;
		for (gen = 0; gen < numGens; ++gen) {
			System.out.println("Generation: " + gen);
			for (i = 0; i < ss.length; ++i) {
				for (k = 0; k < m.specs.size(); ++k) {
					wins[k][i] = 0;
					sims[k].mobs[i].HP = mobPoints;
				}
			}
			for (i = 0; i < ss.length; ++i) {
				if (i % 100 == 0) {
					System.out.println(i + " league");
				}
				for (j = i+1; j < ss.length; ++j) {
					for (k = 0; k < atks.length; ++k) {
						// atks[k][0] vs atks[k][1]
						Simulate.battle(sims[atks[k][0]].mobs[i], sims[atks[k][1]].mobs[j], false);
						if (sims[atks[k][1]].mobs[j].HP == 0) {
							++wins[atks[k][0]][i];
						} else if (sims[atks[k][0]].mobs[i].HP != 0) {
							++wins[atks[k][1]][j];
						}
						sims[atks[k][0]].mobs[i].HP = mobPoints;
						sims[atks[k][1]].mobs[j].HP = mobPoints;
					}					
				}
			}

			for (k = 0; k < m.specs.size(); ++k) {
				iWin[k] = 0;
				sWin[k] = 0;
			}

			for (i = 0; i < ss.length; ++i) {
				for (k = 0; k < m.specs.size(); ++k) {
				if (wins[k][i] > wins[k][iWin[k]]) {
					iWin[k] = i;
				}
				sWin[k] += wins[k][i];
				}
			}
			for (k = 0; k < m.specs.size(); ++k) {
				System.out.println(k + " Best of Gen " + gen + " with " + wins[k][iWin[k]] + " is " + sims[k].mobs[iWin[k]]);
				bestS[k][bestI[k]++] = new Mob(sims[k].mobs[iWin[k]]);
				
				s2[k] = new Simulate(ss.length);
			}


			int i2;
			for (i2 = 0; i2 < .5*ss.length; ++i2) {
				for (k = 0; k < m.specs.size(); ++k) {
					r = sWin[k] * Math.random();
					for (i = 0; i < ss.length; ++i) {
						r -= wins[k][i];
						if (r < 0) {
							break;
						}
					}
					s2[k].mobs[i2] = new Mob(sims[k].mobs[i]);
					if (Math.random() < 0.05) {
						s2[k].mobs[i2].mutate();
					}
				}
			}
			for (; i2 < ss.length; ++i2) {
				for (k = 0; k < m.specs.size(); ++k) {
					p = 0;
					p2 = 0;
					r = sWin[k] * Math.random();
					r2 = sWin[k] * Math.random();
					for (i = 0; i < ss.length && (r > 0 || r2 > 0); ++i) {
						if (r > 0) {
							r -= wins[k][i];
							if (r < 0) {
								p = i;
							}
						}
						if (r2 > 0) {
							r2 -= wins[k][i];
							if (r2 < 0) {
								p2 = i;
							}
						}
					}
					s2[k].mobs[i2] = new Mob(sims[k].mobs[p], sims[k].mobs[p2]);
					if (Math.random() < 0.05) {
						s2[k].mobs[i2].mutate();
					}
				}
			}
			
			for (i = 0; i < s.num; ++i) {
				if (wins[i] > wins[iWin]) {
					iWin = i;
				}
				if (wins[i] > 500) {
					s2.mobs[i2++] = s.mobs[i];
				}
			}
			System.out.println("Best of Gen " + gen + " with " + wins[iWin] + " is " + s.mobs[iWin]);
			i3 = i2;
			for (; i2 < (int)(.75*s.num); ++i2) {
				s2.mobs[i2] = new Mob(s2.mobs[(int)(i3*Math.random())]);
				if (Math.random() < 0.05) {
					s2.mobs[i2].mutate();
				}
			}
			for (i3 = i2; i2 < s.num - 5; ++i2) {
				s2.mobs[i2] = new Mob(s2.mobs[(int)(i3*Math.random())],s2.mobs[(int)(i3*Math.random())]);
				if (Math.random() < 0.05) {
					s2.mobs[i2].mutate();
				}
			}
			for (; i2 < s.num; ++i2) {
				s2.mobs[i2] = new Mob(mobPoints);
			}
			for (k = 0; k < m.specs.size(); ++k) {
				sims[k] = s2[k];
			}
		}

		j = 0;
		for (i = 0; i < ss.length; ++i) {
			for (k = 0; k < m.specs.size(); ++k) {
			 System.out.println(k + " " + i + ": " + sims[k].mobs[i]);
			}
		}

		for (k = 0; k < m.specs.size(); ++k) {
			for (i = k+1; i < m.specs.size(); ++i) {
				a = sims[k].mobs[iWin[k]];
				b = sims[i].mobs[iWin[i]];

				a.HP = mobPoints;
				b.HP = mobPoints;
				System.out.println(k + " vs. " + i);
				System.out.println(a + " vs. " + b);
				Simulate.battle(a, b, true);
				System.out.println();
				a.HP = mobPoints;
				b.HP = mobPoints;
			}
		}*?

		/*for (j = 0; j < 10000; ++j) {
			if (j % 100 == 0) {
				System.out.println("Battle " + j);
			}
			a = s.mobs[(int)(10000 * Math.random())];
			while (!a.equals(b = s.mobs[(int)(10000 * Math.random())]));

			int aWins = 0, bWins = 0, ties=0;

			for (i = 0; i < 1000; ++i) {
				a.HP = 100;
				b.HP = 100;
				s.battle(a, b, false);
				if (a.HP == 0) {
					++bWins;
				} else if (b.HP == 0) {
					++aWins;
				} else {
					++ties;
				}
			}
			if (aWins > bWins) {
				w = a;
				l = b;
			} else if (bWins > aWins) {
				l = a;
				w = b;
			} else {
				continue;
			}
			r = Math.random();
			if (r < 0.5) {
				l = new Mob(s.mobs[(int)(10000*Math.random())]);
			} else if (r < 0.85) {
				l = new Mob(s.mobs[(int)(10000*Math.random())], 
						s.mobs[(int)(10000*Math.random())]);
			} else if (r < 0.95) {
				l = new Mob(w);
			} else {
				l = new Mob(mobPoints);
			}
			if (Math.random() < 0.05) {
				l.mutate();
			}
		}

		for (i = 0; i < s.mobs.length; ++i) {
			System.out.println("i: " + s.mobs[i]);
		}*/
	}

}
