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
	public Double filterStep(int x, int y, char action, Terrain observation)
	{
		final Double pCorrectTraversal = new Double(0.9);
		final Double pIncorrectTraversal = new Double(0.1);
		final Double pCorrectSensor = new Double(0.9);
		final Double pIncorrectSensor = new Double(0.05);
		//ignore blocked cells
		if(!prob.get(y).get(x).equals(0d))
		{
			Double transProbability = 1d;//the transitional probability of going from one state to another
			//when taking the action to move here
			switch(action)
			{
			case 'u':
				if(y!=0)
				{
					transProbability *= pCorrectTraversal*pCorrectSensor + pIncorrectTraversal*pIncorrectSensor;
				}
				else
				{
					transProbability *= pCorrectSensor;
				}
				break;
			case 'd':
				if(y != prob.size())
				{
					transProbability *= pCorrectTraversal*pCorrectSensor + pIncorrectTraversal*pIncorrectSensor;
				}
				else
				{
					transProbability *= pCorrectSensor;
				}
				break;
			case 'l':
				if(x != 0)
				{
					transProbability *= pCorrectTraversal*pCorrectSensor + pIncorrectTraversal*pIncorrectSensor;
				}
				else
				{
					transProbability *= pCorrectSensor;
				}
				break;
			case 'r':
				if(x != prob.get(y).size())
				{
					transProbability *= pCorrectTraversal*pCorrectSensor + pIncorrectTraversal*pIncorrectSensor;
				}
				else
				{
					transProbability *= pCorrectSensor;
				}
				break;
			default:
				System.err.println("Incorrect direction");
				System.exit(1);
				break;
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
					 prob.get(i).set(j, filterStep(j,i, acts.get(time), observ.get(time)));
				}
			}
		}
	}
}
