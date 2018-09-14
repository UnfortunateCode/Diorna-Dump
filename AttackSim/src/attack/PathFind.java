package attack;

import java.util.TreeSet;



public class PathFind {
 
	private class State implements Comparable<State> {
		String hist;
		int x, y;
		int g, h;
		public int compareTo(State s) {
			if ( (g+h)-(s.g+s.h) == 0) {
				if (x == s.x){
					return y - s.y;
				} else {
					return x - s.x;
				}
			}
			return (g+h)-(s.g+s.h);
		}
		
		
	}
	public String getPath(int [][]map, int sx, int sy, int gx, int gy) {
		int [][]states = new int[map.length][map[0].length];
		
		TreeSet<State> open = new TreeSet<State>();
		State st = new State();
		st.hist = "";
		st.x = sx;
		st.y = sy;
		st.g = 0;
		st.h = Math.abs(gx-st.x) + Math.abs(gy-st.y);
		open.add(st);
		states[st.y][st.x]= 1; 
		State stt;
		
		while (open.size() != 0) {
			st = open.first();
			open.remove(st);
			//System.out.println("Looking at " + st.x + ", " + st.y);
			if (st.x > 0 && states[st.y][st.x-1] == 0) {
				stt = new State();
				stt.hist = st.hist + "W";
				stt.x = st.x-1;
				stt.y = st.y;
				stt.g = st.g + 1 + map[stt.y][stt.x];
				stt.h = Math.abs(gx - stt.x)+ Math.abs(gy - stt.y);
				if (stt.h == 0) {
					return stt.hist;
				}
				open.add(stt);
				//System.out.println("Opened " + stt.x + ", " + stt.y + " g: " + stt.g + " h: " + stt.h);
				states[stt.y][stt.x] = 1; 
			}
			if (st.y > 0 && states[st.y-1][st.x] == 0) {
				stt = new State();
				stt.hist = st.hist + "N";
				stt.x = st.x;
				stt.y = st.y-1;
				stt.g = st.g + 1 + map[stt.y][stt.x];
				stt.h = Math.abs(gx - stt.x)+ Math.abs(gy - stt.y);
				if (stt.h == 0) {
					return stt.hist;
				}
				open.add(stt);
				//System.out.println("Opened " + stt.x + ", " + stt.y + " g: " + stt.g + " h: " + stt.h);
				states[stt.y][stt.x] = 1; 
			}
			if (st.y < states.length - 1 && states[st.y+1][st.x] == 0) {
				stt = new State();
				stt.hist = st.hist + "S";
				stt.x = st.x;
				stt.y = st.y+1;
				stt.g = st.g + 1 + map[stt.y][stt.x];
				stt.h = Math.abs(gx - stt.x)+ Math.abs(gy - stt.y);
				if (stt.h == 0) {
					return stt.hist;
				}
				open.add(stt);
				//System.out.println("Opened " + stt.x + ", " + stt.y + " g: " + stt.g + " h: " + stt.h);
				states[stt.y][stt.x] = 1; 
			}
			if (st.x < states[0].length - 1 && states[st.y][st.x+1] == 0) {
				stt = new State();
				stt.hist = st.hist + "E";
				stt.x = st.x+1;
				stt.y = st.y;
				stt.g = st.g + 1 + map[stt.y][stt.x];
				stt.h = Math.abs(gx - stt.x)+ Math.abs(gy - stt.y);
				if (stt.h == 0) {
					return stt.hist;
				}
				open.add(stt);
				//System.out.println("Opened " + stt.x + ", " + stt.y + " g: " + stt.g + " h: " + stt.h);
				states[stt.y][stt.x] = 1; 
			}
			states[st.y][st.x] = -1; 
		}		
		
		System.out.println("Failed, Visited states: ");
		for (int i = 0; i < states.length; ++i) {
			for (int j = 0; j < states[0].length; ++j) {
				if (states[i][j] < 0) {
					System.out.print(2);
				} else {
				    System.out.print(states[i][j]);
				}
			}
			System.out.println();
		}
		return "";
	}
}
