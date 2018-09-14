package octelem;

import java.awt.Point;
import java.util.LinkedList;

public class OctElem {

	public static void main(String[] args) {
		
		System.err.println("Hello World");
				
		int [][] connections = new int[16][4];
		for (int i = 0; i < connections.length; ++i) {
			connections[i] = new int[4];
			connections[i][0] = (i+15)%16;
			connections[i][1] = (i+1)%16;
			connections[i][2] = (i*9+11)%16;
			connections[i][3] = (i*9+13)%16;
			
			switch (i % 4) {
			case 0:
				connections[i][0] = (connections[i][0]+12)%16;
				connections[i][2] = (connections[i][2]+12)%16;
				break;
			case 3:
				connections[i][1] = (connections[i][1]+4)%16;
				connections[i][3] = (connections[i][3]+4)%16;
				break;
			}
		}
		
		for (int i = 0; i < connections.length; ++i) {
			System.out.println(i+": "+connections[i][0]+", "+connections[i][1]+", "+connections[i][2]+", "+connections[i][3]);
			
		}
		
		LinkedList<Integer[]> perms = new LinkedList<Integer[]>();
		Integer []perm = new Integer[16];
		perm[0] = 0;
		int []rem = {0,-1,-1,-1,-1};
		LinkedList<Integer> firstArr, nextArr;
		
		for (int i = 0; i < connections[0].length; ++i) {
			perm[1] = connections[0][i];
			rem[1] = perm[1];
			firstArr = MINUS(get(connections[perm[1]]),rem);
			
			for (Integer j : firstArr) {
				perm[2] = j;
				rem[2] = j;
				
				perm[13] = MINUS(AND(get(connections[0]),get(connections[perm[2]])),rem).getFirst();
				rem[3] = perm[13];
				
				nextArr = MINUS(get(connections[perm[2]]),rem);
				for (Integer k : nextArr) {
					perm[3] = k;
					rem[4] = k;
					
					perm[6] = MINUS(AND(get(connections[perm[1]]),get(connections[perm[3]])),rem).getFirst();
					perm[15] = MINUS(get(connections[perm[2]]),rem).getFirst();
					//lPrintln(get(connections[perm[1]]));
					//lPrintln(get(connections[perm[15]]));
					//lPrintln(AND(get(connections[perm[1]]),get(connections[perm[15]])));
					//lPrintln(get(rem));
					perm[4] = MINUS(AND(get(connections[perm[1]]),get(connections[perm[15]])),rem).getFirst();
					
					perm[5] = (perm[13]+8)%16;
					perm[7] = (perm[15]+8)%16;
					perm[8] = (perm[0]+8)%16;
					perm[9] = (perm[1]+8)%16;
					perm[10] = (perm[2]+8)%16;
					perm[11] = (perm[3]+8)%16;
					perm[12] = (perm[4]+8)%16;
					perm[14] = (perm[6]+8)%16;
					
					perms.add(perm.clone());
					
					rem[4] = -1;
				}
				
				rem[2] = -1;
				rem[3] = -1;
			}
			
			rem[1] = -1;
		}
		
		// Display Perms
		for (Integer[] i : perms) {
			for (int j = 0; j < i.length; ++j) {
				System.out.print(i[j] + "  ");
			}
			System.out.println();
		}
		
		
		Integer[][] temp = new Integer[4][16];
		for (int i = 0; i < temp.length; ++i) {
			temp[i] = new Integer[16];
		}
		int [][]order = {{0,1,13,6,2,3,15,12,8,9,5,14,10,11,7,4},
				{0,1,6,15,2,3,12,5,8,9,14,7,10,11,4,13},
				{0,4,13,1,2,6,15,3,8,12,5,9,10,14,7,11},
				{0,13,6,1,2,15,12,3,8,5,14,9,10,7,4,11}};
		
		
		LinkedList<Integer[]> ords = new LinkedList<Integer[]>();
		for (Integer[] i : perms) {
			for (int j = 0; j < 16; ++j) {
				for (int k = 0; k < 4; ++k) {
					temp[k][j] = i[order[k][j]];
				}
			}
			for (int k = 0; k < 4; ++k) {
				ords.add(temp[k]);
			}
			
			
		}
		// Display Ords
		for (Integer[] i : ords) {
			for (int j = 0; j < i.length; ++j) {
				System.out.print(i[j] + "  ");
			}
			System.out.println();
		}
		
		int val = 9999;
		Integer[] best = ords.getFirst();

		Integer[] from = ords.getFirst();
		int sum = 0;
		int []com = {15, 7, 13, 6, 12, 5, 4, 14};
		LinkedList<Integer[]> myperms = new LinkedList<Integer[]>();
		Integer[] per = {0,1,2,3,4,5,6,7};
		myperms.add(per);
		
		
		
		Point []points = { new Point(2,0), new Point(3,0), new Point(4,0),
				new Point(6,2), new Point(6,3), new Point(6,4), new Point(6,5), new Point(6,6),
				new Point(4,8), new Point(3,8), new Point(2,8),
				new Point(0,6), new Point(0,5), new Point(0,4), new Point(0,3), new Point(0,2)};
		
		
		Integer[] mytemp = new Integer[16];
		Point []dist = new Point[16];
		for (Integer[] i : ords) {
			for (int j = 0; j < 8; ++j) {
				for (int k = 0; k < 16; ++k) {
					mytemp[k] = i[(k+j)%16];
					dist[mytemp[k]] = points[k];
				}
				sum = 0;
				for (int k = 0; k < 4; ++k) {
					sum += Math.abs(dist[k].x-dist[com[k]].x) + Math.abs(dist[k].y-dist[com[k]].y);
				}
				for (int k = 4; k < 8; ++k) {
					sum += Math.abs(dist[k+4].x-dist[com[k]].x) + Math.abs(dist[k+4].y-dist[com[k]].y);
				}
				
				if (sum < val) {
					val = sum;
					best = mytemp.clone();
					from = i.clone();
				}
			}
		}
		
		System.out.println("Sum: " + val);
		for (int i = 0; i < best.length; ++i) {
			System.out.print(best[i] + " ");
		}
		System.out.println();
		
		System.out.println("From: ");
		for (int i = 0; i < from.length; ++i) {
			System.out.print(from[i] + " ");
		}
		System.out.println();
	}
	
	public static LinkedList<Integer> get(int[] arr) {
		LinkedList<Integer> ll = new LinkedList<Integer>();
		for (int i = 0; i < arr.length; ++i) {
			ll.add(arr[i]);
		}
		return ll;
	}
	
	public static LinkedList<Integer> AND(LinkedList<Integer> list1, LinkedList<Integer> list2) {
		LinkedList<Integer> ll = new LinkedList<Integer>();
		for (Integer num : list1) {
			if (list2.contains(num)) {
				ll.add(num);
			}
		}
		return ll;
	}
	
	public static LinkedList<Integer> MINUS(LinkedList<Integer> mainlist, int []remlist) {
		LinkedList<Integer> ll = (LinkedList<Integer>)mainlist.clone();
		for (int i = 0; i < remlist.length; ++i) {
			ll.remove((Integer)remlist[i]);
			
		}
		
		return ll;
	}
	
	public static void lPrintln(LinkedList<Integer> l) {
		for (Integer i : l) {
			System.out.print(i + " ");
		}
		System.out.println();
	}

}
