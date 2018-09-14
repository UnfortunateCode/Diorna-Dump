package attack;

import java.util.Scanner;

public class Play {
	private static final int NORTH = 0;
	private static final int EAST = 1;
	private static final int SOUTH = 2;
	private static final int WEST = 3;

	World w;
	
	public boolean loadWorld() {
		w = new World();
		return w.loadFromFileLocation();
	}
	
	public boolean loadWorld(String location) {
		w = new World();
		return w.loadFromFileLocation(location);
	}
	
	public void play() {
		if (w == null) {
			loadWorld();
		}
		Player p = new Player();
		
		System.out.println("Initialize your stats");

		char [] stats = {'R','L','M','Q','S','P','G','V'};
		p.maxHP = 60;
		p.level = 1;
		p.xp = 0;
		int dmg = 60, point;
		char c;
		p.HP = p.maxHP;
		while (dmg > 0) {
			System.out.println("You can put up to " + dmg + " points in your stats:");
			
			System.out.println("(R)esist " + (p.req(p.Resist+1)-p.R) + " needed for Level " + (p.Resist+1));
			System.out.println("(L)uck " + (p.req(p.Luck+1)-p.L) + " needed for Level " + (p.Luck+1));
			System.out.println("(M)agic " + (p.req(p.Magic+1)-p.M) + " needed for Level " + (p.Magic+1));
			System.out.println("(Q)uickness " + (p.req(p.Quickness+1)-p.Q) + " needed for Level " + (p.Quickness+1));
			System.out.println("(S)trength " + (p.req(p.Strength+1)-p.S) + " needed for Level " + (p.Strength+1));
			System.out.println("(P)recision " + (p.req(p.Precision+1)-p.P) + " needed for Level " + (p.Precision+1));
			System.out.println("(G)uard " + (p.req(p.Guard+1)-p.G) + " needed for Level " + (p.Guard+1));
			System.out.println("(V)anish " + (p.req(p.Vanish+1)-p.V) + " needed for Level " + (p.Vanish+1));
			
			c = getOpt(stats);
			System.out.println("And how many points?");
			point = getInt();
			if (point > dmg) {
				point = dmg;
			}
			switch (c) {
			case 'R':
				p.R+=point;
				break;
			case 'L': 
				p.L+=point;
				break;
			case 'M':
				p.M+=point;
				break;
			case 'Q':
				p.Q+=point;
				break;
			case 'S':
				p.S+=point;
				break;
			case 'P':
				p.P+=point;
				break;
			case 'G':
				p.G+=point;
				break;
			case 'V':
				p.V+=point;
				break;
			}

			p.Resist = p.eval(p.R);
			p.Luck = p.eval(p.L);
			p.Magic = p.eval(p.M);
			p.Quickness = p.eval(p.Q);
			p.Strength = p.eval(p.S);
			p.Precision = p.eval(p.P);
			p.Guard = p.eval(p.G);
			p.Vanish = p.eval(p.V);
			dmg -= point;
		}
		
		
		int x, y, comp, e, i;
		int []dist = new int[4];
		char [] opts = {0,0,0,0,0,0};
		String exit;
		boolean isTown;
		x = w.getStartX();
		y = w.getStartY();
		String name = w.getMapName(x, y);
		isTown = w.isTown(x,y);
		comp = w.getComplexity(x,y);
		double bper = w.battleChance(x,y);
		
		dist[NORTH] = comp+1;
		dist[EAST] = comp+1;
		dist[SOUTH] = comp+1;
		dist[WEST] = comp+1;
		
		while (true) {
			exit = null;
			e = 0;
			if (dist[NORTH] == 0 && y > 0) {
				if (dist[EAST] == 0 && x < w.maxX()) {
					exit = "North East";
					e = 2;
				} else if (dist[WEST] == 0 && x > 0) {
					exit = "North West";
					e = 2;
				} else {
					exit = "North";
					e = 1;
				}
			} else if (dist[SOUTH] == 0 && y < w.maxY()) {
				if (dist[EAST] == 0 && x < w.maxX()) {
					exit = "South East";
					e = 2;
				} else if (dist[WEST] == 0 && x > 0) {
					exit = "South West";
					e = 2;
				} else {
					exit = "South";
					e = 1;
				}
			} else if (dist[EAST] == 0 && x < w.maxX()) {
				exit = "East";
				e = 1;
			} else if (dist[WEST] == 0 && x > 0) {
				exit = "West";
				e = 1;
			}
			if (exit == null) {
				System.out.println("You are in " + name);
			} else {
				System.out.println("You are at the " + exit + " exit of " + name);
			}
			
			if (dist[NORTH] == 0 && y > 0) {
				System.out.println("To the North you see a " + w.mapDesc(x,y-1) + ".");
			} else if (dist[NORTH] == 0 && y == 0) {
				System.out.println("To the North you see a wall.");
			} else if (dist[NORTH] == 1 && y > 0) {
				System.out.println("To the North you can see the North Exit");
			} else if (dist[NORTH] == 1 && y == 0) {
				System.out.println("To the North you can see the North Wall");
			} else {
				System.out.println("To the North the area continues.");
			}
			
			if (dist[EAST] == 0 && x < w.maxX()) {
				System.out.println("To the East you see a " + w.mapDesc(x+1,y) + ".");
			} else if (dist[EAST] == 0 && x == w.maxX()) {
				System.out.println("To the East you see a wall.");
			} else if (dist[EAST] == 1 && x < w.maxX()) {
				System.out.println("To the East you can see the East Exit");
			} else if (dist[EAST] == 1 && x == w.maxX()) {
				System.out.println("To the East you can see the East Wall");
			} else {
				System.out.println("To the East the area continues.");
			}
			
			if (dist[SOUTH] == 0 && y < w.maxY()) {
				System.out.println("To the South you see a " + w.mapDesc(x,y+1) + ".");
			} else if (dist[SOUTH] == 0 && y == w.maxY()) {
				System.out.println("To the South you see a wall.");
			} else if (dist[SOUTH] == 1 && y < w.maxY()) {
				System.out.println("To the South you can see the South Exit");
			} else if (dist[SOUTH] == 1 && y == w.maxY()) {
				System.out.println("To the South you can see the South Wall");
			} else {
				System.out.println("To the South the area continues.");
			}
			
			if (dist[WEST] == 0 && x > 0) {
				System.out.println("To the West you see a " + w.mapDesc(x-1,y) + ".");
			} else if (dist[WEST] == 0 && x == 0) {
				System.out.println("To the West you see a wall.");
			} else if (dist[WEST] == 1 && x > 0) {
				System.out.println("To the West you can see the West Exit");
			} else if (dist[WEST] == 1 && x == 0) {
				System.out.println("To the West you can see the West Wall");
			} else {
				System.out.println("To the West the area continues.");
			}
			
			for (i = 0; i < opts.length; ++i) {
				opts[i] = 0;
			}
			i = 0;
			if (isTown) {
				System.out.print("You can (R)est, ");
				opts[i++] = 'R';
			}
			if (exit != null) {
				System.out.print("e(X)it, ");
				opts[i++] = 'X';
			}
			if (i != 0) {
				System.out.println("or (M)ove");
				opts[i++] = 'M';
				c = getOpt(opts);
				
				if (c == 'R') {
					p.HP = p.maxHP;
					System.out.println("You regain HP");
					continue;
				} else if (c == 'X') {
					
					// Determine exit direction
					if (e == 1) {
						c = exit.charAt(0);
					} else if (e == 2) {
						for (i = 0; i < opts.length; ++i) {
							opts[i] = 0;
						}
						System.out.println("Which direction do you want to exit?");
						System.out.print("You can exit ");
						if (dist[NORTH]==0) {
							System.out.print("(N)orth, or ");
							opts[0] = 'N';
						} else {
							System.out.print("(S)outh, or ");
							opts[0] = 'S';
						}
						if (dist[EAST]==0) {
							System.out.println("(E)ast");
							opts[1] = 'E';
						} else {
							System.out.println("(W)est");
							opts[1] = 'W';
						}
						c = getOpt(opts);
					} // Exit direction determined
					
					// Move in direction
					switch (c) {
					case 'N':
						--y;
						break;
					case 'E':
						++x;
						break;
					case 'S':
						++y;
						break;
					case 'W':
						--x;
						break;
					}
					name = w.getMapName(x, y);
					isTown = w.isTown(x,y);
					comp = w.getComplexity(x,y);
					bper = w.battleChance(x,y);
					
					dist[NORTH] = comp+1;
					dist[EAST] = comp+1;
					dist[SOUTH] = comp+1;
					dist[WEST] = comp+1;
					switch (c) {
					case 'N':
						dist[SOUTH] = 0;
						break;
					case 'E':
						dist[WEST] = 0;
						break;
					case 'S':
						dist[NORTH] = 0;
						break;
					case 'W':
						dist[EAST] = 0;
						break;
					}
					
					// map set up, looping to it
					continue;
					
				} // Move selected
			} // only option is to move
			
			// (only?) option is to move about
			System.out.println("Which direction do you want to move?");
			for (i = 0; i < opts.length; ++i) {
				opts[i] = 0;
			}
			System.out.print("You can go ");
			i = 0;
			if (dist[NORTH]!=0) {
				System.out.print("(N)orth, ");
				opts[i++] = 'N';
			}
			if (dist[EAST]!=0) {
				if (dist[SOUTH] == 0 && dist[WEST] == 0) {
					System.out.println("or (E)ast");
				} else {
					System.out.print("(E)ast, ");
				}
				opts[i++] = 'E';
			}
			if (dist[SOUTH]!=0) {
				if (dist[WEST] == 0) {
					System.out.println("or (S)outh");
				} else {
					System.out.print("(S)outh, ");
				}
				opts[i++] = 'S';
			}
			if (dist[WEST]!=0) {
				System.out.println("or (W)est");
				opts[i++] = 'W';
			}
			c = getOpt(opts);
			switch (c) {
			case 'N':
				dist[NORTH]--;
				dist[SOUTH] = Math.min(dist[SOUTH]+1, comp+1);
				break;
			case 'E':
				dist[EAST]--;
				dist[WEST] = Math.min(dist[WEST]+1, comp+1);
				break;
			case 'S':
				dist[SOUTH]--;
				dist[NORTH] = Math.min(dist[NORTH]+1, comp+1);
				break;
			case 'W':
				dist[WEST]--;
				dist[EAST] = Math.min(dist[EAST]+1, comp+1);
				break;
			}
			
			// before moving, check if a battle occurred
			if (Math.random() < bper) {
				battle(p, w.encounter(x,y));
				if (p.HP <= 0) {
					System.out.println("You died.  Returning to Starting Town.");
					p.HP = p.maxHP;
					x = w.getStartX();
					y = w.getStartY();
					name = w.getMapName(x, y);
					isTown = w.isTown(x,y);
					comp = w.getComplexity(x,y);
					bper = w.battleChance(x,y);
					
					dist[NORTH] = comp+1;
					dist[EAST] = comp+1;
					dist[SOUTH] = comp+1;
					dist[WEST] = comp+1; 
				}
			}
		}
	}
	private void battle(Player p, Mob b) {
		System.out.println("Battle!");
		double r;
		boolean isCrit = false;
		int turnCount = 0, dmg, point;
		char c;
		char [] opts = {'A','C','R'};
		char [] stats = {'R','L','M','Q','S','P','G','V'};
		
		b.HP = b.maxHP;
		
		p.delay = p.delayInMillis();
		b.delay = b.delayInMillis();
		r = Math.random();
		if (r < 0.05) {
			// first attack
			b.delay += b.delayInMillis();
			System.out.println("You Snuck Up on a Species " + b.species.ID);
		} else if (r > 0.95) {
			// back attack
			p.delay += p.delayInMillis();
			System.out.println("A Species " + b.species.ID + " has caught you Off Guard");
		} else {
			System.out.println("You are in battle with a Species " + b.species.ID);
		}
		
		while (p.HP > 0 && b.HP > 0) {
			if (++turnCount > 20 + b.level) {
				if (Math.random() < 0.05) {
					System.out.println("The Species " + b.species.ID + " has fled.");
					break;
				}
			}
			
			if (p.delay <= b.delay) {
				// Player turn
				b.delay -= p.delay;
				p.delay = p.delayInMillis();
				
				System.out.println("You can (A)ttack, (C)ast Magic, or (R)un Away");
				c = getOpt(opts);
				switch(c) {
				case 'A':
					// Player attacks
					dmg = 0;
					while (b.HP > 0 && --p.numAtks >= 0) {
						System.out.println("You attack.");
						
						if (b.lucky()) {
							System.out.println("The Species " + b.species.ID + " gets lucky and is missed.");
							continue;
						}
						
						switch (p.hits(b)) {
						case Mob.DODGED:
							System.out.println("The Species " + b.species.ID + " pulls a maneuver and dodges.");
							continue;
						case Mob.MISS:
							System.out.println("You completely miss the Species " + b.species.ID + ".");
							continue;
						case Mob.HIT:
							System.out.print("You hit the Species " + b.species.ID);
							break;
						case Mob.SCORED:
							System.out.print("You Scored on the Species " + b.species.ID);
							break;
						}
						isCrit = p.crits();
						if (isCrit) {
							System.out.println(" and it was Critical!");
						} else {
							System.out.println(".");
						}
						dmg += p.damages(b, isCrit);
					}
					System.out.println("The Species " + b.species.ID + " takes " + dmg + " points of damage.");
					b.HP -= dmg;
					if (b.HP < 0) {
						b.HP = 0;
					}
					
					break;
				case 'C':
					// Player casts
					dmg = 0;
					while (b.HP > 0 && --p.numAtks >= 0) {
						System.out.println("You cast magic.");
						if (b.lucky()) {
							System.out.println("The Species " + b.species.ID + " gets lucky and is missed.");
							continue;
						}
						dmg += p.spells(b);
					}
					System.out.println("The Species " + b.species.ID + " takes " + dmg + " points of damage.");
						
					b.HP -= dmg;
					if (b.HP < 0) {
						b.HP = 0;
						
					}
					break;
				case 'R':
					// Player runs away
					if (Math.random() < 0.5) {
						System.out.println("You successfully escape!");
						return;
					} else {
						System.out.println("You were unable to escape.");
						continue;
					}
				}
			} else {
				// Enemy turn

				p.delay -= b.delay;
				b.delay = b.delayInMillis();
				dmg = 0;
				while (p.HP > 0 && --b.numAtks >= 0) {
					if (b.useMagic(p)) {
						System.out.println("The Species " + b.species.ID + " casts magic.");
						if (p.lucky()) {
							System.out.println("You get lucky and are missed!");
							continue;
						}
						
						dmg += b.spells(p);
					} else {
						System.out.println("The Species " + b.species.ID + " attacks.");
						if (p.lucky()) {
							System.out.println("You get lucky and are missed!");
						}
						switch (b.hits(p)) {
						case Mob.DODGED:
							System.out.println("You skillfully dodge the attack!");
							continue;
						case Mob.MISS:
							System.out.println("The Species " + b.species.ID + " completely misses you!");
							continue;
						case Mob.HIT:
							System.out.print("The Species " + b.species.ID + " hits");
							break;
						case Mob.SCORED:
							System.out.print("The Species " + b.species.ID + " scores a hit");
							break;
						}
						isCrit = b.crits();
						if (isCrit) {
							System.out.println(" and it was Critical.");
						} else {
							System.out.println(".");
						}
						dmg += b.damages(p, isCrit);
					}
				}
				System.out.println("You take " + dmg + " points of damage.");
				
				p.HP -= dmg;
				if (p.HP < 0) {
					p.HP = 0;
				}
				System.out.println("You are reduced to " + p.HP + " HP");
			}
		}
		
		if (b.HP <= 0) {
			System.out.println("You defeated the Species " + b.species.ID + "!");
			dmg = Math.max((int)((((double)(b.level - p.level)*(double)b.maxHP/(9.0+(double)p.level)) + b.maxHP) * 0.1),0); 
			System.out.println("You gain " + dmg + "xp!");
			p.xp += dmg;
			while(p.xp > p.maxHP) {
				System.out.println("You gain a Level!");
				p.xp -= p.maxHP;
				p.level++;
				dmg = (p.level - 1) / 5 + (p.level - 1) / 10 + 5;
				p.maxHP += dmg;
				p.HP = p.maxHP;
				while (dmg > 0) {
					System.out.println("You can put up to " + dmg + " points in your stats:");
					
					System.out.println("(R)esist " + (p.req(p.Resist+1)-p.R) + " needed for Level " + (p.Resist+1));
					System.out.println("(L)uck " + (p.req(p.Luck+1)-p.L) + " needed for Level " + (p.Luck+1));
					System.out.println("(M)agic " + (p.req(p.Magic+1)-p.M) + " needed for Level " + (p.Magic+1));
					System.out.println("(Q)uickness " + (p.req(p.Quickness+1)-p.Q) + " needed for Level " + (p.Quickness+1));
					System.out.println("(S)trength " + (p.req(p.Strength+1)-p.S) + " needed for Level " + (p.Strength+1));
					System.out.println("(P)recision " + (p.req(p.Precision+1)-p.P) + " needed for Level " + (p.Precision+1));
					System.out.println("(G)uard " + (p.req(p.Guard+1)-p.G) + " needed for Level " + (p.Guard+1));
					System.out.println("(V)anish " + (p.req(p.Vanish+1)-p.V) + " needed for Level " + (p.Vanish+1));
					
					c = getOpt(stats);
					System.out.println("And how many points?");
					point = getInt();
					if (point > dmg) {
						point = dmg;
					}
					switch (c) {
					case 'R':
						p.R+=point;
						break;
					case 'L': 
						p.L+=point;
						break;
					case 'M':
						p.M+=point;
						break;
					case 'Q':
						p.Q+=point;
						break;
					case 'S':
						p.S+=point;
						break;
					case 'P':
						p.P+=point;
						break;
					case 'G':
						p.G+=point;
						break;
					case 'V':
						p.V+=point;
						break;
					}
					dmg -= point;
				}
			}
		}
	}

	private int getInt() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print(">");
			try {
				return sc.nextInt();
			} catch (Exception e) {
				sc = new Scanner(System.in);
			}
		}
	}

	private char getOpt(char[] opts) {
		Scanner sc = new Scanner(System.in);
		String in;
		char c;
		int i;
		while (true) {
			System.out.print(">");
			try {
				in = sc.next("[A-Za-z].*");
				c = Character.toUpperCase(in.charAt(0));
				for (i = 0; i < opts.length; ++i) {
					if (c == opts[i]) {
						return c;
					}
				}
				sc = new Scanner(System.in);
				continue;
			} catch (Exception e) {
				sc = new Scanner(System.in);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Play p = new Play();
		if (p.loadWorld()) {
			p.play();
		}
		System.out.println("Done.");

	}

}
