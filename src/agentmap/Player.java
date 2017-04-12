package agentmap;
import java.util.Random;
import java.util.ArrayList;

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
	public Terrain type; // tracks tile type
	
	/**
	 * Constructor for making a Player object - points to x/y coordinates and holds tile type
	 * @param x
	 * @param y
	 * @param t
	 */
	public Player(int x, int y, Grid w){
		xC = x;
		yC = y;
		type = w.grid.get(xC).get(yC).getType();
	}
	
	/**
	 * Default constructor - random 
	 */
	public Player(){
		xC = 0;
		yC = 0;
		type = Terrain.Normal;
	}
	
	/**
	 * activate - generate a list of acts and observations
	 */
	public void activate(Grid w){
		ArrayList<Character> acts = new ArrayList<Character>();
		ArrayList<Terrain> observations = new ArrayList<Terrain>();
		// For question 1
		/*acts.add('r');
		acts.add('r');
		acts.add('d');
		acts.add('d');
		
		observations.add(Terrain.Normal);
		observations.add(Terrain.Normal);
		observations.add(Terrain.Hard);
		observations.add(Terrain.Hard);*/
		
		// 100 random moves
		Random rand = new Random();
		double curr = rand.nextDouble();
		for(int i = 0; i < 100; i++){
			if(curr <= 0.25){
				acts.add('u');
			} else if (curr > 0.25 && curr <= 0.5){
				acts.add('d');
			} else if (curr > 0.5 && curr <= 0.75){
				acts.add('l');
			} else{
				acts.add('r');
			}
			curr = rand.nextDouble();
		}
		
		for(int i = 0; i < 100; i++){
			if(acts.get(i).equals('u')){
				yC ++;
			} else if(acts.get(i).equals('d')){
				yC --;
			} else if(acts.get(i).equals('l')){
				xC --;
			} else if(acts.get(i).equals('r')){
				xC ++;
			}
			type = w.grid.get(xC).get(yC).getType();
			observations.add(type);
		}
		
	}
}