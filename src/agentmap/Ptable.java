package agentmap;

import java.util.ArrayList;

public class Ptable
{
	public ArrayList<ArrayList<Double>> prob = new ArrayList<ArrayList<Double>>();
	public ArrayList<Character> acts = new ArrayList<Character>();
	public ArrayList<Terrain> observ = new ArrayList<Terrain>();
	private ArrayList<ArrayList<Node>> grid;
	int width = 0, height = 0;
	
	public Ptable(ArrayList<ArrayList<Node>> grid,int width, int height)
	{
		this.width = width;
		this.height = height;
		this.grid = grid;
		for(int y = 0; y < height; y++)
		{
			prob.add(new ArrayList<Double>());
		}
	}
	private void equalInitializer(ArrayList<ArrayList<Node>> map)
	{
		int unblockedCells = 0;
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				if(!map.get(y).get(x).getType().equals(Terrain.Blocked))
				{
					unblockedCells++;
				}
			}
		}
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				if(!map.get(y).get(x).getType().equals(Terrain.Blocked))
				{
					prob.get(y).set(x, new Double((double)1/unblockedCells));
				}
				else
				{
					prob.get(y).set(x, new Double(0));
				}
			}
		}
	}
	public void addAction(Character action)
	{
		acts.add(action);
	}
	public void addObservvation(Terrain observation)
	{
		observ.add(observation);
	}
	/**
	 * filters a single cell given some observation
	 * @param x current cell's x value
	 * @param y current cell's y value
	 * @paran action the action taken to move into this cell
	 * @param observation the result observed when scanning this cell
	 */
	public Double filterStep(int x, int y, Terrain observation)
	{
		//ignore blocked cells
		if(!prob.get(y).get(x).equals(0d))
		{
			Double transProbability = 1d;//the transitional probability of going from one state to another
			if(grid.get(y).get(x).getType().equals(observation))
			{
				//calculate for correct movement as .9 and incorrect movement as .05
				transProbability = .9*.9+.1*.05;
			}
			else //calculate for correct movement as .05 and incorrect movement as .9
			{
				transProbability = .9*.05+.1*.9;
			}
			return transProbability*prob.get(y).get(x);
		}
		return 0d;
	}
	/**
	 * Assumes observation lists and action lists have been filled out.
	 * Takes an index and calculates the probable current location given the observations up to the point of the index
	 */
	public void filter(int index)
	{
		if(index>=acts.size()||index>=observ.size())
		{
			System.err.println("Filtering out of bounds");
			return;
		}
		//for as long as t is deneoted by the index assuming t>=0
		for(int time = 0; time < index; time++)
		{
			for(int i = 0; i < prob.size();i++)
			{
				ArrayList<Double> row = prob.get(i);
				for(int j = 0; j < row.size(); j++)
				{
					 prob.get(i).set(j, filterStep(j,i,observ.get(time)));
				}
			}
		}
	}
}
