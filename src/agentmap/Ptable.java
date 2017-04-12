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
					//probability of getting a correct observation
					if(observation.equals(grid.get(y).get(x).getType())||grid.get(y-1).get(x).getType().equals(Terrain.Blocked))
					{
						transProbability *= pCorrectTraversal*pCorrectSensor + pIncorrectTraversal*pIncorrectSensor;
					}
					else
					{
						transProbability *= pCorrectTraversal*pIncorrectSensor + pIncorrectTraversal*pCorrectSensor;
					}
				}
				else
				{
					if(observation.equals(grid.get(y).get(x).getType()))
					{
						transProbability *= pCorrectSensor;
					}
					else
					{
						transProbability *= pIncorrectSensor;
					}
				}
				break;
			case 'd':
				if(y != prob.size()-1)
				{
					if(observation.equals(grid.get(y).get(x).getType())||grid.get(y+1).get(x).getType().equals(Terrain.Blocked))
					{
						transProbability *= pCorrectTraversal*pCorrectSensor + pIncorrectTraversal*pIncorrectSensor;
					}
					else
					{
						transProbability *= pCorrectTraversal*pIncorrectSensor + pIncorrectTraversal*pCorrectSensor;
					}
				}
				else
				{
					if(observation.equals(grid.get(y).get(x).getType()))
					{
						transProbability *= pCorrectSensor;
					}
					else
					{
						transProbability *= pIncorrectSensor;
					}
				}
				break;
			case 'l':
				if(x != 0)
				{
					if(observation.equals(grid.get(y).get(x).getType())||grid.get(y).get(x-1).getType().equals(Terrain.Blocked))
					{
						transProbability *= pCorrectTraversal*pCorrectSensor + pIncorrectTraversal*pIncorrectSensor;
					}
					else
					{
						transProbability *= pCorrectTraversal*pIncorrectSensor + pIncorrectTraversal*pCorrectSensor;
					}
				}
				else
				{
					if(observation.equals(grid.get(y).get(x).getType()))
					{
						transProbability *= pCorrectSensor;
					}
					else
					{
						transProbability *= pIncorrectSensor;
					}
				}
				break;
			case 'r':
				if(x != prob.get(y).size()-1)
				{
					if(observation.equals(grid.get(y).get(x).getType())||grid.get(y).get(x+1).getType().equals(Terrain.Blocked))
					{
						transProbability *= pCorrectTraversal*pCorrectSensor + pIncorrectTraversal*pIncorrectSensor;
					}
					else
					{
						transProbability *= pCorrectTraversal*pIncorrectSensor + pIncorrectTraversal*pCorrectSensor;
					}
				}
				else
				{
					if(observation.equals(grid.get(y).get(x).getType()))
					{
						transProbability *= pCorrectSensor;
					}
					else
					{
						transProbability *= pIncorrectSensor;
					}
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
		//for as long as t is denoted by the index assuming t>=0
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
			print();
		}
	}
	public void print()
	{
		System.out.println("\\begin{center}");
		System.out.println("\\begin{tabular}{|c|c|c|}");
		System.out.println("\\hline");
		for(ArrayList<Double> row : prob)
		{
			for(int i = 0; i < row.size(); i++)
			{
				System.out.print(row.get(i));
				if(i!=row.size()-1)
				{
					System.out.print(" &");
				}
				else
				{
					System.out.print("\\\\ \\hline");
				}
			}
		}
		System.out.println("\\end{tabular}");
		System.out.println("\\end{center}");
	}
}
