package runes;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.LinkedList;

public class TicTacDisplay extends JPanel {

	public static LinkedList<TicTac> []tacs;
	public static int tacNum;
	
	int tacSize;
	TicTac tt = new TicTac(3);
	public TicTacDisplay() {
		tacSize = 8;
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				//System.out.println("Mouse Clicked!");
				++tacSize;
				if (tacSize > 20) {
					tacSize = 2;
				}
				repaint();
			}
		});
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(1200,50000);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString("Yo!",10,20);
		Dimension d = getPreferredSize();
		int sum = 0;
		for (int i = 0; i < tacs.length; ++i) {
			sum += tacs[i].size();
		}
		//tacSize = 8;
		int ticTacSize = tacSize * (tacNum - 1);
		int arrNum = 0;
		int p = 0;
		for (int i = 30; i <= d.height-(30+ticTacSize); i += (tacSize/2+ticTacSize)) {
		for (int j = 10; j <= d.width-(10 + ticTacSize); j += (tacSize/2+ticTacSize)) {
			if (sum <= 0) {
				break;
			}
			if (p >= tacs[arrNum].size()) {
				++arrNum;
				p = 0;
				j += tacSize/2+ticTacSize;
			}
			
			tacs[arrNum].get(p).paintSymbol(g, j, i, tacSize);
			++p;
			--sum;
		}
		if (sum <= 0) {
			break;
		}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		tacNum = 3;
		TicTac to = new TicTac(tacNum);
		int []sum = new int[(tacNum*tacNum)/2+1];
		tacs = new LinkedList[(tacNum*tacNum)/2+1];
		LinkedList<TicTac> masterList = new LinkedList<TicTac>();
		
		for (int i = 0; i < sum.length; ++i) {
			sum[i] = 0;
			tacs[i] = new LinkedList<TicTac>();
		}
		do {
			/*try {
			System.in.read();
			} catch (Exception e) {};
			System.out.println("Inserting: " + to);
			System.out.print("Into: ");
			for (int i = 0; i < tacs[to.cardinality()].size(); ++i) {
				System.out.print(tacs[to.cardinality()].get(i) + " ");
			}
			System.out.println();*/
			if (to.overCard()) {
				continue;
			}
			if (!tacs[to.cardinality()].contains(to)) {
				tacs[to.cardinality()].add(new TicTac(to));
				//System.out.print(" Inserted");
			}
			//System.out.println();
			++sum[to.cardinality()];
		} while (!to.increment());
		
		for (int i = 0; i < tacs.length; ++i) {
			masterList.addAll(tacs[i]);
		}
		
		for (int i = 0; i < sum.length; ++i) {
			System.out.println(i + ": " + tacs[i].size() + " of " + sum[i]);
		}
		
		int equivSum;
		for (int i = 0; i < TicTac.maxEquiv; ++i) {
			
			equivSum = 0;
			for (TicTac t : masterList) {
				if (t.numEquiv() == i) {
					++equivSum;
				}
			}
			if (equivSum > 0) {
			System.out.println(i + " equiv: " + equivSum);
			}
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}
	
	public static void createAndShowGUI() {
		JFrame jf = new JFrame("TicTac Symbols");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		TicTacDisplay ttd = new TicTacDisplay();
		JScrollPane sp = new JScrollPane(ttd);
		jf.add(sp);
		jf.pack();
		jf.setSize(1200,1000);
		jf.setVisible(true);
	}

}
