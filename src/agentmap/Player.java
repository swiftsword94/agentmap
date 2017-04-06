package agentmap;

/**
 * This class needs to point to a tile on a grid
 * Hold x/y coordinates, and sense the tile type
 * 
 * @author Alex Smirnov
 *
 */
public class Player {
	public int xC; // tracks x coordinate
	public int yC; // tracks y coordinate
	public char type; // tracks tile type
	
	/**
	 * Constructor for making a Player object - points to x/y coordinates and holds tile type
	 * @param x
	 * @param y
	 * @param t
	 */
	public Player(int x, int y, char t){
		xC = x;
		yC = y;
		type = t;
	}
	
	/**
	 * Default constructor
	 */
	public Player(){
		xC = 0;
		yC = 0;
		type = '0';
	}
}