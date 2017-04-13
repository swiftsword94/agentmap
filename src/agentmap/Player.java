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
	public Ptable ptable;
	/**
	 * Constructor for making a Player object - points to x/y coordinates and holds tile type
	 * @param x
	 * @param y
	 * @param t
	 */
	public Player(int x, int y, Grid w){
		System.out.println("Initial position: (" + x + ", " + y + ")");
		xC = x;
		yC = y;
		type = w.grid.get(yC).get(xC).getType();
		ptable = new Ptable(w.grid, w.grid.get(y).size(), w.grid.size());
		ptable.acts = new ArrayList<Character>();
		ptable.observ = new ArrayList<Terrain>();
	}
	
	/**
	 * Default constructor - random 
	 */
	public Player(){
		xC = 0;
		yC = 0;
		type = Terrain.Normal;
		ptable.acts = new ArrayList<Character>();
		ptable.observ = new ArrayList<Terrain>();
	}
	
	public void activateA(Grid w){
		ptable.acts.add('r');
		ptable.acts.add('r');
		ptable.acts.add('d');
		ptable.acts.add('d');
		
		ptable.observ.add(Terrain.Normal);
		ptable.observ.add(Terrain.Normal);
		ptable.observ.add(Terrain.Highway);
		ptable.observ.add(Terrain.Highway);
		
	}
	/**
	 * activate - generate a list of acts and observations
	 */
	public void activate(Grid w){
		
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
				ptable.acts.add('u');
			} else if (curr > 0.25 && curr <= 0.5){
				ptable.acts.add('d');
			} else if (curr > 0.5 && curr <= 0.75){
				ptable.acts.add('l');
			} else{
				ptable.acts.add('r');
			}
			curr = rand.nextDouble();
		}
		
		for(int i = 0; i < 100; i++){
			if(ptable.acts.get(i).equals('d')){
				if(yC+1<ptable.height && !w.grid.get(yC+1).get(xC).getType().equals(Terrain.Blocked))
				{
					yC++;
				}
			} else if(ptable.acts.get(i).equals('u')){
				if(yC-1>=0 && !w.grid.get(yC-1).get(xC).getType().equals(Terrain.Blocked))
				{
					yC--;
				}
			} else if(ptable.acts.get(i).equals('l')){
				if(xC-1>=0 && !w.grid.get(yC).get(xC-1).getType().equals(Terrain.Blocked))
				{
					xC--;
				}
			} else if(ptable.acts.get(i).equals('r')){
				if(xC+1<ptable.width && !w.grid.get(yC).get(xC+1).getType().equals(Terrain.Blocked))
				{
					xC++;
				}
			}
			type = w.grid.get(yC).get(xC).getType();
			//System.out.println(ptable.acts.get(i));
			//System.out.println(type);
			ptable.observ.add(type);
		}
		
	}
}