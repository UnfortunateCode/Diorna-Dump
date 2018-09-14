package attack;

import java.io.FileWriter;
import java.util.LinkedList;

public class MapMaker extends Map {
	private final int MAXLEVEL = 12;
	private final static int MAP = 0;
	private final static int TOWN = 1;
	private final static int PATH = 2;
	private int level = 0;
	Tile [][]map;
	int [][]complexity;
	int [][]distances;
	//LinkedList<Spec> specs;
	LinkedList<Town> towns;
	TriArr<String> paths;
	int numTowns;
	int[][] levelRanges = { { 1, 3}, { 2, 5}, { 3, 6}, { 5, 9},
			{ 7,11}, { 9,13}, {11,16}, {13,18},
			{16,21}, {18,23}, {21,27}, {24,30},
			{27,33}, {30,36}, {33,39}, {36,43},
			{39,46}, {43,50}, {46,53}, {50,57},
			{53,60}, {57,65}, {61,69}, {65,73},
			{69,77}, {73,81}, {77,85}, {81,89},
			{85,94}, {89,98}};

	public void writeTo() {
		try {
			FileWriter fw;
			fw = new FileWriter("test.txt");
			fw.append(1 + "," + 2 + ",");
			fw.close();
			
			fw = new FileWriter("map.txt");
			String f = "";
			f+=(map[0].length + "," + map.length + ",");
			for (int i = 0; i < map.length; ++i) {
				for (int j = 0; j < map[0].length; ++j) {
					f+=(map[i][j].type + "," + map[i][j].lvlRange + "," + (map[i][j].sList.length));
					for (int k = 0; k < map[i][j].sList.length; ++k) {
						f+=("," + map[i][j].sList[k].ID);
					}
					f+=(",");
				}
			}
			for (int i = 0; i < complexity.length; ++i) {
				f+=(complexity[i][0] + ",");
				for (int j = 1; j < complexity[0].length; ++j) {
					f+=(complexity[i][j] + ",");
				}
			}
			for (int i = 0; i < distances.length; ++i) {
				f+=(distances[i][0] + ",");
				for (int j = 1; j < distances[0].length; ++j) {
					f+=(distances[i][j] + ",");
				}
			}
			fw.append(f);
			fw.close();
			
			
			fw = new FileWriter("towns.txt");
			for (Town t : towns) {
				fw.append(t.x + "," + t.y + ",");
			}
			fw.close();
			
			fw = new FileWriter("specs.txt");
			for (Spec s : specs) {
				fw.append(s.ID + "," + s.maxLevel + "," + s.connects.size());
				for (Spec sc : s.connects){
					fw.append("," + sc.ID);
				}
				fw.append("," + s.attacks.size());
				for (Spec sc : s.attacks) {
					fw.append("," + sc.ID);
				}
				fw.append(",");
			}
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
			
		}
	}
	
	public void writeLevel(int level, Mob[] bests) {
		//R = L = M = Q = S = P = G = V;
		try {
			FileWriter fw = new FileWriter("specLevels.txt",true);
			fw.append(level + "," + bests.length + ",");
			for (int i = 0; i < bests.length; ++i) {
				fw.append(bests[i].R + "," + bests[i].L + ",");
				fw.append(bests[i].M + "," + bests[i].Q + ",");
				fw.append(bests[i].S + "," + bests[i].P + ",");
				fw.append(bests[i].G + "," + bests[i].V + ",");
				fw.append(bests[i].maxHP + ",");
			}
			fw.close();
		} catch (Exception e) {
			
		}
	}
	
	private class Town {
		int x, y;
		public Town(int a, int b) {
			x = a;
			y = b;
		}
	}
	private class Tile {
		int height;
		Spec []sList;
		int type;
		int lvlRange;

		public Tile() {
			height = (int)(4 * Math.random()) + 2;
			sList = new Spec[height];
			type = MAP;
			lvlRange = 0;
		}

		public Tile (int i) {
			height = Math.min(i,MAXLEVEL);
			sList = new Spec[height];
			type = MAP;
			lvlRange = 0;
		}

		public Spec getTop() {
			if (height > level) {
				return sList[level];
			}
			return null;
		}
		public boolean add(Spec s) {
			if (height > level && sList[level] == null) {
				sList[level] = s;
				s.atLevel(level);
				return true;
			}
			return false;
		}

		public boolean filled() {
			return (height <= level || sList[level] != null);
		}
		public String toString() {
			String str = "[ ";
			for (int i = 0; i < sList.length; ++i) {
				if (sList[i] != null) {

					str += sList[i] + " "; 
				}
			}
			str += "]";
			return str;
		}

	}


	private boolean isMapFilled() {
		for (int i = 0; i < map.length; ++i) {
			for (int j = 0; j < map[0].length; ++j) {
				if (!map[i][j].filled()) {
					return false;
				}
			}
		}
		return true;
	}
	public MapMaker(int w, int h) {
		map = new Tile[h][w];
		numTowns = 0;
		specs = new LinkedList<Spec>();
		towns = new LinkedList<Town>();
		complexity = createMap(w,h);
		for (int i = 0; i < map.length; ++i) {
			for (int j = 0; j < map[0].length; ++j) {
				map[i][j] = new Tile(2+complexity[i][j]);
			}
		}
		for (Town t : towns) {
			map[t.y][t.x]= new Tile(0);
			map[t.y][t.x].type = TOWN;
			++numTowns;
		}
	}

	public void setSpecLevels(boolean disp) {
		for (int i = 0; i < map.length; ++i) {
			for (int j = 0; j < map[0].length; ++j) {
				for (int k = 0; k < map[i][j].sList.length; ++k) {
					if (levelRanges[distances[i][j]][1] > map[i][j].sList[k].maxLevel) {
						map[i][j].sList[k].maxLevel = levelRanges[distances[i][j]][1];
					}
				}
			}
		}
	}
	public void setPaths(boolean disp) {
		String s;
		if (disp) {System.out.println("Setting Paths");}
		PathFind pf = new PathFind();
		paths = new TriArr<String>(towns.size());
		for (int i = 0; i < towns.size(); ++i) {
			for (int j = i+1; j < towns.size(); ++j) {
				s = pf.getPath(complexity, towns.get(i).x, towns.get(i).y, towns.get(j).x, towns.get(j).y);
				paths.set(i, j, s);
				if (disp) {
					System.out.print("From " + towns.get(i).x + ", " + towns.get(i).y);
					System.out.println(" To " + towns.get(j).x + ", " + towns.get(j).y);
					System.out.println(s);
					System.out.println(paths.get(i,j));
				}
			}
		}
		if (disp) {
			System.out.println("Finished, displaying:");
		}
		int x, y;
		String [][]temp = new String[complexity.length][complexity[0].length];
		for (int i = 0; i < temp.length; ++i) {
			for (int j = 0; j < temp[0].length; ++j) {
				temp[i][j] = "" + complexity[i][j];
			}
		}
		for (int i = 0; i < towns.size(); ++i) {
			for (int j = i+1; j < towns.size(); ++j) {
				s = paths.get(i,j);
				x = towns.get(i).x;
				y = towns.get(i).y;
				for (int k = 0; k < s.length(); ++k) {
					switch(s.charAt(k)) {
					case 'N':
						--y;
						break;
					case 'E':
						++x;
						break;
					case 'W':
						--x;
						break;
					case 'S':
						++y;
						break;
					}
					temp[y][x] = "o";
					map[y][x].type = PATH;
				}
			}
		}
		for (Town t : towns) {
			temp[t.y][t.x]= "#"; 
			map[t.y][t.x].type = TOWN; 
		} 
		if (disp) {
			for (int i = 0; i < temp.length; ++i) {
				for (int j = 0; j < temp[0].length; ++j) {
					System.out.print(temp[i][j]);
				}
				System.out.println();
			}
		}
	}

	private int[][] createMap(int w, int h) {
		double [][] pArr = {{0.0,0.0,0.1,0.1,0.2,0.0,0.0,0.2,0.4,0.0,0.0},
				{0.0,0.0,0.3,0.3,0.2,0.5,0.0,0.2,0.2,0.4,0.0},
				{0.0,0.0,0.6,0.3,0.2,0.2,0.3,0.2,0.1,0.2,0.2}};

		int []points = {8,5,3,2,1};
		int i = 0;
		int i1,i2, min, max, per;
		int [][]map = new int[h][w];
		double r;
		boolean flag;
		for (int a = 0; a < w+h-1; ++a) {
			for (int b = 0; b <= a; ++b) {
				if ((a-b) >= w) {
					continue;
				}
				if (b >= h) {
					break;
				}

				if (b == 0 && (a-b) == 0) {
					i = 0;
				} else if (b == 0) {
					i = map[b][a-b-1];
				} else if ((a-b) == 0) {
					i = map[b-1][a-b];
				} else {
					i1 = map[b][a-b-1];
					i2 = map[b-1][a-b];
					if (i1==i2) {
						i = i1;
					} else {
						min = Math.min(i1, i2);
						max = Math.max(i1, i2);
						per = (max - min)*points[max]+points[min];
						if (per * Math.random() < points[min]) {
							i = min;
						} else {
							i = max;
						}
					}
				}
				r = Math.random();

				if (r < pArr[0][i]) { 
					i -= 2; 
				} else if ((r -= pArr[0][i]) < pArr[1][i+1] ) {
					i -= 1;
				} else if ((r -= pArr[1][i+1]) < pArr[2][i+2]) {

				} else if ((r -= pArr[2][i+2]) < pArr[1][i+2]) {
					i += 1;
				} else {
					i += 2;
				}
				//System.err.println("[" + b + "][" + (a-b) + "]: " + i);
				map[b][a-b] = i;
			}
		}
		for (int y = 1; y < h-1; ++y) {
			for (int x = 1; x < w-1; ++x) {
				if (    map[y-1][x] == 0 &&
						map[y][x-1] == 0 &&
						map[y+1][x] == 0 &&
						map[y][x+1] == 0) {
					flag = false;
					for (Town t : towns) {
						if (Math.abs(t.x-x) + Math.abs(t.y-y) < 3) {
							flag = true;
							break;
						}
					}
					if (!flag) {
						towns.add(new Town(x,y));
					}
				}
			}
		}
		return map;
	}

	public void findLvls(boolean disp) {
		distances = new int[map.length][map[0].length];
		int total, dist;
		for (int i = 0; i < map.length; ++i) {
			for (int j = 0; j < map[0].length; ++j) {
				total = Integer.MAX_VALUE;
				for (Town t : towns) {
					dist = Math.abs(t.x-j) + Math.abs(t.y-i);
					if (dist < total) {
						total = dist;
					}
				}
				if (map[i][j].type == PATH) {
					total /= 2;
				}
				total = Math.min(total, 13);
				distances[i][j] = total;
				map[i][j].lvlRange = Math.max(map[i][j].lvlRange, total);
				if (disp) {
					if (total < 10) {
						System.out.print(" " + total);
					} else {
						System.out.print(total);
					}}
			}
			if (disp) {System.out.println();}
		}
	}
	public void build() {
		int t, u, v;
		int []w;
		Spec sp;
		LinkedList<Spec> nears;
		for (; level < MAXLEVEL; ++level) {
			while (!isMapFilled()) {
				t = (int)(map.length * map[0].length * Math.random());
				while (!isMapFilled()) {
					if (!map[t/map[0].length][t%map[0].length].filled()) {
						nears = new LinkedList<Spec>();
						if ((t/map[0].length) > 0) {
							if (map[(t/map[0].length)-1][t%map[0].length].getTop() != null) {
								nears.add(map[(t/map[0].length)-1][t%map[0].length].getTop());
							}
						}
						if ((t/map[0].length)+1 < map.length ) {
							if (map[(t/map[0].length)+1][t%map[0].length].getTop() != null) {
								nears.add(map[(t/map[0].length)+1][t%map[0].length].getTop());
							}
						}
						if ((t%map[0].length) > 0) {
							if (map[(t/map[0].length)][(t%map[0].length)-1].getTop() != null) {
								nears.add(map[(t/map[0].length)][(t%map[0].length)-1].getTop());
							}
						}
						if ((t%map[0].length)+1 < map[0].length) {
							if (map[(t/map[0].length)][(t%map[0].length)+1].getTop() != null) {
								nears.add(map[(t/map[0].length)][(t%map[0].length)+1].getTop());
							}
						}
						if(nears.size() == 0) {
							for (u = 0; u < level; ++u) {
								if (map[(t/map[0].length)][(t%map[0].length)].sList[u] != null) {
									if (map[(t/map[0].length)][(t%map[0].length)].sList[u].topLevel != level) {
										nears.add(map[(t/map[0].length)][(t%map[0].length)].sList[u]);
									}
								}
							}
							if (nears.size() == 0) {
								nears.add(new Spec(this));
							} else {

								if (Math.random() < 0.25) {
									nears = new LinkedList<Spec>();
									nears.add(new Spec(this));
								}
							}
						}
						//*
						v = -1;
						w = new int[nears.size()];
						u = Integer.MAX_VALUE;
						for (int i = 0; i < nears.size(); ++i) {
							w[i] = nears.get(i).num;
						}
						for (Spec s : map[t/map[0].length][t%map[0].length].sList) {
							if (nears.contains(s)) {
								w[nears.indexOf(s)]+=0;
							}
						}
						for (int i= 0; i < w.length; ++i) {
							if (w[i] < u) {
								u = w[i];
								v = i;
							}
						}
						if (v >= 0) {
							sp = nears.get(v);
							map[t/map[0].length][t%map[0].length].add(sp);
							++sp.num;
							break;
						}/*/
						if (nears.size() > 0) {
							sp = nears.get((int)(nears.size()*Math.random()));
							map[t/map.length][t%map.length].add(sp);
							++sp.num;
							break;
						}//*/
					}
					t = (t+1) % (map.length * map[0].length);
				}
			}
		}
	}

	public int[][] getAttackers(boolean disp) {
		double r;
		int numAttackers = 0;
		for (int i = 0; i < map.length; ++i) {
			for (int j = 0; j < map[0].length; ++j) {
				for (int k = 0; k < map[i][j].sList.length; ++k) {
					for (int l = k; l < map[i][j].sList.length; ++l) {
						if (map[i][j].sList[k] != map[i][j].sList[l] &&
								!map[i][j].sList[k].connects.contains(map[i][j].sList[l])) {
							map[i][j].sList[k].connects.add(map[i][j].sList[l]);
							map[i][j].sList[l].connects.add(map[i][j].sList[k]);

							r = Math.random();
							if (r < 0.4) {
								map[i][j].sList[k].attacks.add(map[i][j].sList[l]);
								++numAttackers;
							} else if (r < 0.8) {
								map[i][j].sList[l].attacks.add(map[i][j].sList[k]);
								++numAttackers;
							} else if (r < 0.9) {
								map[i][j].sList[l].attacks.add(map[i][j].sList[k]);
								map[i][j].sList[k].attacks.add(map[i][j].sList[l]);
								numAttackers += 2;
							}
						}
					}
				}
			}
		}

		int [][]attacks = new int[numAttackers][2];
		int ind = 0;
		for (int i = 0; i < specs.size(); ++i) {
			for (int j = 0; j < specs.get(i).attacks.size(); ++j) {
				attacks[ind][0] = i;
				attacks[ind][1] = specs.indexOf(specs.get(i).attacks.get(j));
				if(disp){System.out.println(i + " atks " + attacks[ind][1]);}				
				++ind;

			}
		}
		return attacks;
	}

	public String toString() {
		String str = "";
		for (int i = 0; i < map.length; ++i) {
			for (int j = 0; j < map[0].length; ++j) {
				str += map[i][j] + "\t";
			}
			str += "\n";
		}
		return str;
	}
	public void display(boolean withPaths) {
		if (withPaths) {
			for (int i = 0; i < map.length; ++i) {
				for (int j = 0; j < map[0].length; ++j) {
					if (map[i][j].type == PATH) {
						System.out.print("o");
					} else if (map[i][j].type == TOWN) {
						System.out.print("#");
					} else {
						System.out.print(complexity[i][j]);
					}
				}
				System.out.println();
			}
		} else {
			System.out.println(this);
		}
	}
	public static void main(String []args) {
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
		m.findLvls(false);
		m.build();
		m.setSpecLevels(false);
		m.getAttackers(false);


	}
}
