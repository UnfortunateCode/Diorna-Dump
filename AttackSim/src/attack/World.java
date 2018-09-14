package attack;

import java.io.*;
import java.util.LinkedList;

public class World extends Map{
	int w, h;
    Tile [][]map;
    
    int startX=-1, startY=-1;

	int[][] levelRanges = { { 1, 3}, { 2, 5}, { 3, 6}, { 5, 9},
			{ 7,11}, { 9,13}, {11,16}, {13,18},
			{16,21}, {18,23}, {21,27}, {24,30},
			{27,33}, {30,36}, {33,39}, {36,43},
			{39,46}, {43,50}, {46,53}, {50,57},
			{53,60}, {57,65}, {61,69}, {65,73},
			{69,77}, {73,81}, {77,85}, {81,89},
			{85,94}, {89,98}};
    
    private class Tile {
    	int type;
    	int dist;
    	int []sList;
    	int comp;
    }
    public World() {
    	specs = new LinkedList<Spec>();
    }
	public boolean loadFromFileLocation(String location) {
		String f;
		boolean err = false;
		int ind, temp;
		LinkedList<String> specCons = new LinkedList<String>();
		try {
			FileReader fr = new FileReader(location + "map.txt");
			BufferedReader br = new BufferedReader(fr);
			f = br.readLine();
			br.close();
			fr.close();
			ind = f.indexOf(",");
			w = Integer.parseInt(f.substring(0,ind));
			if (err) {System.out.println("W: " + w);}
			f = f.substring(ind+1);
			
			ind = f.indexOf(",");
			h = Integer.parseInt(f.substring(0,ind));
			if (err) {System.out.println("H: " + h);}
			f = f.substring(ind+1);
			
			map = new Tile[h][w];
			
			for (int i = 0; i < h; ++i) {
				if (err) {System.out.println(i + " - ");}
				for (int j = 0; j < w; ++j) {
					if (err) {System.out.print(j+":");}
					map[i][j] = new Tile();
					ind = f.indexOf(",");
					map[i][j].type = Integer.parseInt(f.substring(0,ind));
					if (err) {System.out.print("T: " + map[i][j].type);}
					f = f.substring(ind+1);
					if (map[i][j].type == 1) {
						if (startX < 0) {
							startX = j;
							startY = i;
						} else {
							if (Math.random() < 0.25) {
								startX = j;
								startY = i;
							}
						}
					}
					
					ind = f.indexOf(",");
					map[i][j].dist = Integer.parseInt(f.substring(0,ind));
					if (err) {System.out.print(" D: " + map[i][j].dist);}
					f = f.substring(ind+1);
					
					ind = f.indexOf(",");
					temp = Integer.parseInt(f.substring(0,ind));
					if (err) {System.out.print(" #" + temp + ": " );}
					map[i][j].sList = new int[temp];
					f = f.substring(ind+1);
					
					for (int k = 0; k < map[i][j].sList.length; ++k) {
						ind = f.indexOf(",");
						map[i][j].sList[k] = Integer.parseInt(f.substring(0,ind));
						if (err) {System.out.print(map[i][j].sList[k] + ",");}
						f = f.substring(ind+1);
					}
					if (err) {System.out.println();}
				}
			}
			for (int i = 0; i < h; ++i) {
				for (int j = 0; j < w; ++j) {
					ind = f.indexOf(",");
					map[i][j].comp = Integer.parseInt(f.substring(0,ind));
					f = f.substring(ind+1);
				}
			}
			
			fr = new FileReader(location + "specs.txt");
			br = new BufferedReader(fr);
			f = br.readLine();
			br.close();
			fr.close();
			
			Spec st;
			while ((ind = f.indexOf(",")) > 0) {
				f = f.substring(ind+1);
				st = new Spec(this);
				specs.add(st);
				ind = f.indexOf(",");
			    st.maxLevel = Integer.parseInt(f.substring(0,ind));
			    f = f.substring(ind+1);
			    
			    // get past connects
			    ind = f.indexOf(",");
			    temp = Integer.parseInt(f.substring(0,ind));
			    for (int i = 0; i < temp; ++i) {
			    	ind = f.indexOf(",",ind+1);
			    }
			    
			    // get past attacks
			    temp = ind;
			    ind = f.indexOf(",",ind+1);
			    temp = Integer.parseInt(f.substring(temp+1,ind));
			    for (int i = 0; i < temp; ++i) {
			    	ind = f.indexOf(",",ind+1);
			    }
			    
			    // store connects and attacks as string until later
			    specCons.add(f.substring(0,ind+1));
			    f = f.substring(ind+1);
			    
			}
			
			// All Specs are in specs, pointing at each other now
			// possible
			for (int i = 0; i < specCons.size(); ++i) {
				f = specCons.get(i);
				ind = f.indexOf(",");
				temp = Integer.parseInt(f.substring(0,ind));
				f = f.substring(ind+1);
				
				for (int j = 0; j < temp; ++j) {
					ind = f.indexOf(",");
					specs.get(i).connects.add(specs.get(Integer.parseInt(f.substring(0,ind))));
					f = f.substring(ind+1);
				}
				
				ind = f.indexOf(",");
				temp = Integer.parseInt(f.substring(0,ind));
				f = f.substring(ind+1);
				
				for (int j = 0; j < temp; ++j) {
					ind = f.indexOf(",");
					specs.get(i).attacks.add(specs.get(Integer.parseInt(f.substring(0,ind))));
					f = f.substring(ind+1);
				}
			}
			
			fr = new FileReader(location + "specLevels.txt");
			br = new BufferedReader(fr);
			f = br.readLine();
			br.close();
			fr.close();
			
			int r,l,m,q,s,p,g,v;
			int level;
			Mob mTemp;
			while ( (ind = f.indexOf(",")) > 0) {
				level = Integer.parseInt(f.substring(0,ind));
				f = f.substring(ind+1);
				ind = f.indexOf(",");
				temp = Integer.parseInt(f.substring(0,ind));
				f = f.substring(ind+1);
				for (int i = 0; i < temp; ++i) {
					ind = f.indexOf(",");
					r = Integer.parseInt(f.substring(0,ind));
					f = f.substring(ind+1);
					ind = f.indexOf(",");
					l = Integer.parseInt(f.substring(0,ind));
					f = f.substring(ind+1);
					ind = f.indexOf(",");
					m = Integer.parseInt(f.substring(0,ind));
					f = f.substring(ind+1);
					ind = f.indexOf(",");
					q = Integer.parseInt(f.substring(0,ind));
					f = f.substring(ind+1);
					ind = f.indexOf(",");
					s = Integer.parseInt(f.substring(0,ind));
					f = f.substring(ind+1);
					ind = f.indexOf(",");
					p = Integer.parseInt(f.substring(0,ind));
					f = f.substring(ind+1);
					ind = f.indexOf(",");
					g = Integer.parseInt(f.substring(0,ind));
					f = f.substring(ind+1);
					ind = f.indexOf(",");
					v = Integer.parseInt(f.substring(0,ind));
					f = f.substring(ind+1);
					mTemp = new Mob(r,l,m,q,s,p,g,v);
					mTemp.level = level;
					ind = f.indexOf(",");
					mTemp.maxHP = Integer.parseInt(f.substring(0,ind));
					f = f.substring(ind+1);
					specs.get(i).byLevel.add(mTemp);
					mTemp.species = specs.get(i);
				}
			}
			return true;
		} catch (Exception e) {
			System.err.println(e);
			System.err.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean loadFromFileLocation() {
		return loadFromFileLocation("");
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public String getMapName(int x, int y) {
		if (map[y][x].type == 1) {
			return "Town " + (char)('A' + (y%26)) + "" + (char)('a' + (x%26)) + "" + (y/26) + "" + (x/26);
		} else {
			switch(map[y][x].comp) {
			case 0:
				return "Field " + y + "" + x;
			case 1:
				return "Grassland " + y + "" + x;
			case 2:
				return "Forest " + y + "" + x;
			case 3:
				return y + "" + x + " Hills";
			case 4:
				return "Mountain " + y + "" + x;
			}
			
		}
		return "Map";
	}

	public boolean isTown(int x, int y) {
		return map[y][x].type == 1;
	}

	public int getComplexity(int x, int y) {
		return map[y][x].comp;
	}

	public double battleChance(int x, int y) {
		if (map[y][x].type == 1) {
			return 0;
		}
		return ((double)map[y][x].comp + (double)map[y][x].dist)/100.0 + 0.5;
	}

	public int maxX() {
		return w-1;
	}

	public int maxY() {
		return h-1;
	}

	public String mapDesc(int x, int y) {
		if (map[y][x].type == 1) {
			switch(map[y][x].comp) {
			case 0:
				return "very small Town";
			case 1:
				return "small Town";
			case 2:
				return "Town";
			case 3:
				return "large Town";
			case 4:
				return "very large Town";
			}
		} else {
			switch(map[y][x].comp) {
			case 0:
				return "flat field";
			case 1:
				return "tall grassland";
			case 2:
				return "forest";
			case 3:
				return "maze of hills";
			case 4:
				return "forested mountain";
			}
			
		}
		return "place";
	}

	public Mob encounter(int x, int y) {
		if (map[y][x].sList.length == 0) {
			return null;
		} 
		int level = Math.min(map[y][x].dist, levelRanges.length);
		level = (int)((levelRanges[level][1] - levelRanges[level][0] + 1) * Math.random() + levelRanges[level][0]);
		int sp = map[y][x].sList[(int)(map[y][x].sList.length*Math.random())];
		if (level > specs.get(sp).byLevel.size()) {
			level = specs.get(sp).byLevel.size();
		}
		return specs.get(sp).byLevel.get(level-1);
	}

}
