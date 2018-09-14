package runes;

import java.awt.Color;
import java.awt.Graphics;
import java.util.BitSet;

public class TicTac {
	public static int maxEquiv = 1;

	  private enum Transform { EQUAL, VERTICAL, HORIZONTAL, ONE_EIGHTY, MAIN_DIAGONAL, NINETY, TWO_SEVENTY, SUB_DIAGONAL }
	  private enum DisplaySquare { FULL, BOTTOM_RIGHT, BOTTOM_LEFT, HORIZONTAL, TOP_RIGHT, VERTICAL, CROSS, TOP_LEFT }

	  private int width;
	  private int height;
	  private int numEquiv;
	  
	  private BitSet nodes;
	  private DisplaySquare [][]display;
	  private boolean updateDisplay;

	  // Create a new Square TicTac
	  public TicTac(int x) {
		  numEquiv = 1;
	    width = height = x;
	    nodes = new BitSet(x*x);
	    //System.out.println("Created size " + x + "x" + x + " = " + nodes.size());
	    display = new DisplaySquare[x-1][x-1];
	    updateDisplay = true;
	    resetDisplay();
	  }

	  // Create a new Rectangle TicTac
	  public TicTac(int x, int y) {
		  numEquiv = 1;
	    width = x;
	    height = y;
	    nodes = new BitSet(x*y);
	    display = new DisplaySquare[y-1][x-1];
	    updateDisplay = true;
	    resetDisplay();
	  }
	  
	  public TicTac(TicTac t) {
		  width = t.width;
		  height = t.height;
		  nodes = new BitSet(width * height);
		  display = new DisplaySquare[height-1][width-1];
		  updateDisplay = t.updateDisplay;
		  for (int i = 0; i < height*width; ++i) {
			  nodes.set(i,t.nodes.get(i));
		  }
		  resetDisplay();
	  }
	  
	  public TicTac invert() {
		  TicTac t = new TicTac(this);
		  t.nodes.flip(0, width*height);
		  t.resetDisplay();
		  return t;
	  }

	  public String toString() {
		  String s = "[ ";
		  for (int i = 0; i < height; ++i) {
			  for (int j = 0; j < width; ++j) {
				  if (nodes.get(i*width+j)) {
					  s += "1";
				  } else {
					  s += "0";
			  	  }
			  }
			  s += " ";
		  }
		  return s + "]";
	  }
	  public boolean equals(Object o) {
		  try {
			  return equals((TicTac)o);
		  } catch (Exception e) {
			  
		  }
		  //System.out.println("EPICFAIL");
		  return false;
	  }
	  // Determine equality with another TicTac
	  public boolean equals(TicTac t) {
		  //System.out.println();
		  //System.out.println("Comparing " + this + " with " + t);
	    if (width != t.width || height != t.height || cardinality() != t.cardinality()) {
	    	//System.out.println("Doesn't Match");
	      return false;
	    }

	    BitSet bs = new BitSet(8);
	    bs.set(0,bs.size(),false);
	    bs.set(0,8,true);

	    for (int i = 0; i < height; ++i) {
	      for (int j = 0; j < width; ++j) {
	        if (bs.get(Transform.EQUAL.ordinal()) && nodes.get(i*width+j) != t.nodes.get(width*(i)+(j))) {
	          bs.flip(Transform.EQUAL.ordinal());
	          //System.out.println("Not Equal at " + i + "," + j + " & " + i + "," + j);
	        }
	        if (bs.get(Transform.VERTICAL.ordinal()) && nodes.get(i*width+j) != t.nodes.get(width*(i)+(width - j - 1))) {
	          bs.flip(Transform.VERTICAL.ordinal());
	          //System.out.println("Not Vertical at " + i + "," + j + " & " + i + "," + (width - j - 1));
	        }
	        if (bs.get(Transform.HORIZONTAL.ordinal()) && nodes.get(i*width+j) != t.nodes.get(width*(height - i - 1)+(j))) {
	          bs.flip(Transform.HORIZONTAL.ordinal());
	          //System.out.println("Not Horizontal at " + i + "," + j + " & " + (height - i - 1) + "," + j);
	        }
	        if (bs.get(Transform.ONE_EIGHTY.ordinal()) && nodes.get(i*width+j) != t.nodes.get(width*(height - i - 1)+(width - j - 1))) {
	          bs.flip(Transform.ONE_EIGHTY.ordinal());
	          //System.out.println("Not One Eighty at " + i + "," + j + " & " + (height - i - 1) + "," + (width - j - 1));
	        }

	        if (width == height) {
	          if (bs.get(Transform.MAIN_DIAGONAL.ordinal()) && nodes.get(i*width+j) != t.nodes.get(width*(j)+(i))) {
	            bs.flip(Transform.MAIN_DIAGONAL.ordinal());
		         // System.out.println("Not Main Diagonal at " + i + "," + j + " & " + j + "," + i);
	          }
	          if (bs.get(Transform.NINETY.ordinal()) && nodes.get(i*width+j) != t.nodes.get(width*(j)+(width - i - 1))) {
	            bs.flip(Transform.NINETY.ordinal());
		        //  System.out.println("Not Ninety at " + i + "," + j + " & " + j + "," + (width - i - 1));
	          }
	          if (bs.get(Transform.TWO_SEVENTY.ordinal()) && nodes.get(i*width+j) != t.nodes.get(width*(height - j - 1)+(i))) {
	            bs.flip(Transform.TWO_SEVENTY.ordinal());
		        //  System.out.println("Not Two Seventy at " + i + "," + j + " & " + (height - j - 1) + "," + i);
	          }
	          if (bs.get(Transform.SUB_DIAGONAL.ordinal()) && nodes.get(i*width+j) != t.nodes.get(width*(height - j - 1)+(width - i - 1))) {
	            bs.flip(Transform.SUB_DIAGONAL.ordinal());
		        //  System.out.println("Not Sub Diagonal at " + i + "," + j + " & " + (height - j - 1) + "," + (width - i - 1));
	          }
	        }
	      }
	    }

	    if (bs.cardinality() != 0) {
	    	if ((++numEquiv) > maxEquiv) {
	    		maxEquiv = numEquiv;
	    	}
	    	if ((++t.numEquiv) > maxEquiv) {
	    		maxEquiv = t.numEquiv;
	    	}
	    	
	    	//System.out.println("bs.card != 0");
	    	return true;
	    }
	    //System.out.println("bs.card == 0");
	    t = t.invert();
	    if (cardinality() != t.cardinality()) {
	    	//System.out.println("Invers card no match");
	    	return false;
	    }
	    bs.set(0,bs.size(),false);
	    bs.set(0,8,true);
	    

	    for (int i = 0; i < height; ++i) {
	      for (int j = 0; j < width; ++j) {
	        if (bs.get(Transform.EQUAL.ordinal()) && nodes.get(i*width+j) != t.nodes.get(width*(i)+(j))) {
	          bs.flip(Transform.EQUAL.ordinal());
	        }
	        if (bs.get(Transform.VERTICAL.ordinal()) && nodes.get(i*width+j) != t.nodes.get(width*(i)+(width - j - 1))) {
	          bs.flip(Transform.VERTICAL.ordinal());
	        }
	        if (bs.get(Transform.HORIZONTAL.ordinal()) && nodes.get(i*width+j) != t.nodes.get(width*(height - i - 1)+(j))) {
	          bs.flip(Transform.HORIZONTAL.ordinal());
	        }
	        if (bs.get(Transform.ONE_EIGHTY.ordinal()) && nodes.get(i*width+j) != t.nodes.get(width*(height - i - 1)+(width - j - 1))) {
	          bs.flip(Transform.ONE_EIGHTY.ordinal());
	        }

	        if (width == height) {
	          if (bs.get(Transform.MAIN_DIAGONAL.ordinal()) && nodes.get(i*width+j) != t.nodes.get(width*(j)+(i))) {
	            bs.flip(Transform.MAIN_DIAGONAL.ordinal());
	          }
	          if (bs.get(Transform.NINETY.ordinal()) && nodes.get(i*width+j) != t.nodes.get(width*(j)+(width - i - 1))) {
	            bs.flip(Transform.NINETY.ordinal());
	          }
	          if (bs.get(Transform.TWO_SEVENTY.ordinal()) && nodes.get(i*width+j) != t.nodes.get(width*(height - j - 1)+(i))) {
	            bs.flip(Transform.TWO_SEVENTY.ordinal());
	          }
	          if (bs.get(Transform.SUB_DIAGONAL.ordinal()) && nodes.get(i*width+j) != t.nodes.get(width*(height - j - 1)+(width - i - 1))) {
	            bs.flip(Transform.SUB_DIAGONAL.ordinal());
	          }
	        }
	      }
	    }
	    
	    //System.out.println("invert bs.card != 0: " + (bs.cardinality() != 0));
	    if (bs.cardinality() != 0) {
	    	if ((++numEquiv) > maxEquiv) {
	    		maxEquiv = numEquiv;
	    	}
	    	if ((++t.numEquiv) > maxEquiv) {
	    		maxEquiv = t.numEquiv;
	    	}
	    	return true;
	    }
	    return false;
	  }

	  public int numEquiv() {
		  return numEquiv;
	  }
	  // Increments and returns whether looped
	  public boolean increment() {
	    for (int i = width * height - 1; i >= 0; --i) {
	      if (!nodes.get(i)) {
	        nodes.flip(i,width * height);
	        resetDisplay();
	        //System.out.println(nodes  + " " + (width*height));
	        return false;
	      }
	    }
	    nodes.flip(0,width*height);
	    return true;
	  }

	  public void set(int i) {
	    nodes.set(i);
	    resetDisplay();
	  }

	  public void unset(int i) {
	    nodes.set(i, false);
	    resetDisplay();
	  }

	  public void set(int fromIndex, int toIndex) {
	    nodes.set(fromIndex, toIndex);
	    resetDisplay();
	  }
	 
	  public void unset(int fromIndex, int toIndex) {
	    nodes.set(fromIndex, toIndex, false);
	    resetDisplay();
	  }
	  
	  public boolean overCard() {
		  return nodes.cardinality() > width * height / 2;
	  }
	  public int cardinality() {
		  if (nodes.cardinality() > width * height / 2) {
			  return (width * height) - nodes.cardinality();
		  }
		  return nodes.cardinality();
	  }

	  public void toggleDisplayUpdate() {
	    updateDisplay = !updateDisplay;
	  }

	  public void resetDisplay() {
	    if (updateDisplay) {
	      BitSet corners;

	      for (int i = 0; i < height - 1; ++i) {
	        for (int j = 0; j < width - 1; ++j) {
	          corners = new BitSet(4);
	          
	          corners.set(0,nodes.get(i*width+j));
	          corners.set(1,nodes.get(i*width+j+1));
	          corners.set(2,nodes.get(i*width+width+j));
	          corners.set(3,nodes.get(i*width+width+j+1));
	         
	          if (corners.get(0)) {
	            corners.flip(0,4);
	          }

	          if (!corners.get(1) && !corners.get(2) && !corners.get(3)) {
	            display[i][j] = DisplaySquare.FULL;
	            continue;
	          }
	          if (!corners.get(1) && !corners.get(2) && corners.get(3)) {
	            display[i][j] = DisplaySquare.BOTTOM_RIGHT;
	            continue;
	          }
	          if (!corners.get(1) && corners.get(2) && !corners.get(3)) {
	            display[i][j] = DisplaySquare.BOTTOM_LEFT;
	            continue;
	          }
	          if (!corners.get(1) && corners.get(2) && corners.get(3)) {
	            display[i][j] = DisplaySquare.HORIZONTAL;
	            continue;
	          }
	          if (corners.get(1) && !corners.get(2) && !corners.get(3)) {
	            display[i][j] = DisplaySquare.TOP_RIGHT;
	            continue;
	          }
	          if (corners.get(1) && !corners.get(2) && corners.get(3)) {
	            display[i][j] = DisplaySquare.VERTICAL;
	            continue;
	          }
	          if (corners.get(1) && corners.get(2) && !corners.get(3)) {
	            display[i][j] = DisplaySquare.CROSS;
	            continue;
	          }
	          if (corners.get(1) && corners.get(2) && corners.get(3)) {
	            display[i][j] = DisplaySquare.TOP_LEFT;
	            continue;
	          }


	        }   
	      }
	    }
	  }

	  public void paintSymbol(Graphics g, int x, int y, int size) {
		  g.setColor(Color.BLACK);
		  /*g.fillRect(40,10,50,50);*/
		  int []xPoints = new int[3];
		  int []yPoints = new int[3];
		  int []xCross = new int[4];
		  int []yCross = new int[4];
		  for (int i = 0; i < display.length; ++i) {
			  for (int j = 0; j < display[i].length; ++j) {
				  //System.out.print("[" + j + "," + i + "] < [" + display[i].length + "," + display.length + "]");
				  //System.out.println(" " + display[i][j]);
				  switch(display[i][j]) {
				  case FULL:
					  g.fillRect(x+j*size, y+i*size, size, size);
					  break;
				  case BOTTOM_RIGHT:
					  xPoints[0] = x+j*size;
					  xPoints[1] = x+(j+1)*size;
					  xPoints[2] = x+j*size;
					  
					  yPoints[0] = y+i*size;
					  yPoints[1] = y+i*size;
					  yPoints[2] = y+(i+1)*size;
					  g.fillPolygon(xPoints,yPoints,3);
					  g.fillOval(x+(j+1)*size, y+(i+1)*size-1, 1, 2);
					  break;
				  case BOTTOM_LEFT:

					  xPoints[0] = x+j*size;
					  xPoints[1] = x+(j+1)*size;
					  xPoints[2] = x+(j+1)*size;
					  
					  yPoints[0] = y+i*size;
					  yPoints[1] = y+i*size;
					  yPoints[2] = y+(i+1)*size;
					  g.fillPolygon(xPoints,yPoints,3);
					  g.fillOval(x+j*size, y+(i+1)*size-1, 1,2);
					  break;
				  case HORIZONTAL:
					  g.drawLine(x+j*size, y+i*size, x+(j+1)*size, y+i*size);
					  g.drawLine(x+j*size, y+(i+1)*size, x+(j+1)*size, y+(i+1)*size);
					  /*
					  g.fillRect(x+j*size, y+i*size-(size/6), size, size/3);
					  g.fillRect(x+j*size, y+(i+1)*size-(size/6), size, size/3);
					  */
					  break;
				  case TOP_RIGHT:
					  xPoints[0] = x+j*size;
					  xPoints[1] = x+(j+1)*size;
					  xPoints[2] = x+j*size;
					  
					  yPoints[0] = y+i*size;
					  yPoints[1] = y+(i+1)*size;
					  yPoints[2] = y+(i+1)*size;
					  g.fillPolygon(xPoints,yPoints,3);
					  g.fillOval(x+(j+1)*size, y+i*size-1, 1,2);
					  break;
				  case VERTICAL:
					  g.drawLine(x+j*size, y+i*size, x+j*size, y+(i+1)*size);
					  g.drawLine(x+(j+1)*size, y+i*size, x+(j+1)*size, y+(i+1)*size);
					  /*
					  g.fillRect(x+j*size-(size/6), y+i*size, size/3, size);
					  g.fillRect(x+(j+1)*size-(size/6), y+i*size, size/3, size);
					  */
					  break;
					  
				  case CROSS:
					  g.drawLine(x+j*size, y+i*size, x+(j+1)*size, y + (i+1)*size);
					  g.drawLine(x+(j+1)*size, y+i*size, x+j*size, y+(i+1)*size);
					  /*
					  xCross[0] = x + j*size - size/6;
					  xCross[1] = x + j*size + size/6;
					  xCross[2] = x + (j+1)*size + size/6;
					  xCross[3] = x + (j+1)*size - size/6;
					  
					  yCross[0] = y + i*size + size/6;
					  yCross[1] = y + i*size - size/6;
					  yCross[2] = y + (i+1)*size - size/6;
					  yCross[3] = y + (i+1)*size + size/6;
					  g.fillPolygon(xCross,yCross,4);
					  
					  xCross[0] = x + j*size - size/6;
					  xCross[1] = x + j*size + size/6;
					  xCross[2] = x + (j+1)*size + size/6;
					  xCross[3] = x + (j+1)*size - size/6;
					  
					  yCross[0] = y + (i+1)*size - size/6;
					  yCross[1] = y + (i+1)*size + size/6;
					  yCross[2] = y + i*size + size/6;
					  yCross[3] = y + i*size - size/6;
					  g.fillPolygon(xCross,yCross,4);
					  */
					  break;
				  case TOP_LEFT:
					  xPoints[0] = x+(j+1)*size;
					  xPoints[1] = x+(j+1)*size;
					  xPoints[2] = x+j*size;
					  
					  yPoints[0] = y+i*size;
					  yPoints[1] = y+(i+1)*size;
					  yPoints[2] = y+(i+1)*size;
					  g.fillPolygon(xPoints,yPoints,3);
					  g.fillOval(x+j*size, y+i*size-1, 1, 2);
					  break;
				  }
			  }
		  }
	  }
	}
