package agentmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Ptable
{
	public ArrayList<ArrayList<Double>> prob = new ArrayList<ArrayList<Double>>(), buf = new ArrayList<ArrayList<Double>>();
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
		for(int y = 0; y < height; y++)
		{
			buf.add(new ArrayList<Double>());
		}
		equalInitializer(grid);
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
					prob.get(y).add(x, new Double((double)1/unblockedCells));
					buf.get(y).add(x, null);
				}
				else
				{
					prob.get(y).add(x, new Double(0));
					buf.get(y).add(x, null);
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
	public void normalize()
	{
		Double total = new Double(0d);
		for(ArrayList<Double> row : prob)
		{
			for(Double probability : row)
			{
				total += probability;
			}
		}
		for(ArrayList<Double> row : prob)
		{
			for(int i = 0; i < row.size(); i++)
			{
				row.set(i, row.get(i)/total);
			}
		}
	}
	public ArrayList<ArrayList<Double>> mult(ArrayList<ArrayList<Double>> matrixA, ArrayList<ArrayList<Double>>  matrixB)
	{
		ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();
		
		//provides space for arrays
		int row = matrixA.size(); 
		int col = matrixB.iterator().next().size();
		int msize = matrixB.size();
		//if matrixB's row is not equal to matrixA's columns
		if(matrixB.size()!=matrixA.iterator().next().size())
		{
			return null;
		}
		for(int i = 0; i < row; i++)
		{
			result.add(new ArrayList<Double>());
		}
		//multiplication is done here
		for(int i = 0; i < row; i++)
		{
			for(int j = 0; j < col;j++)
			{
				Double tmp = new Double(0);
				for(int k = 0; k < msize; k++)
				{
					tmp += matrixA.get(i).get(k)*matrixB.get(k).get(j);
				}
				result.get(i).set(j, tmp);
			}
		}
		
		return result;
	}
	/**
	 * filters a single cell given some observation
	 * @param x current cell's x value
	 * @param y current cell's y value
	 * @param action the action taken to move into this cell
	 * @param observation the result observed when scanning this cell
	 */
	private Double filterStep(int x, int y, char action, Terrain observation)
	{
		final Double pCorrectTraversal = new Double(0.9);
		final Double pIncorrectTraversal = new Double(0.1);
		final Double pCorrectSensor = new Double(0.9);
		final Double pIncorrectSensor = new Double(0.05);
		Double transProbability = 0d;//the transitional probability of going from one state to another
		//ignore blocked cells
		if(!grid.get(y).get(x).getType().equals(Terrain.Blocked))
		{
			//when taking the action to move here
			switch(action)
			{
			case 'u':
				//if we are not coming from a blocked cell add the probability of
				if(y == 0 || grid.get(y-1).get(x).getType().equals(Terrain.Blocked))
				{
					//traversal will always fail so the transitional probability will be the the same as doing nothing
					transProbability += prob.get(y).get(x);
				}
				else
				{
					transProbability += pIncorrectTraversal*prob.get(y).get(x);
				}
				if(y != grid.size()-1 && !grid.get(y+1).get(x).getType().equals(Terrain.Blocked))
				{
					transProbability += pCorrectTraversal*prob.get(y+1).get(x);
				}
				
				break;
			case 'd':
				if(y == grid.size()-1 || grid.get(y+1).get(x).getType().equals(Terrain.Blocked))
				{
					//traversal will always fail so the transitional probability will be the the same as doing nothing
					transProbability += prob.get(y).get(x);
				}
				else
				{
					transProbability += pIncorrectTraversal*prob.get(y).get(x);
				}
				if(y != 0 && !grid.get(y-1).get(x).getType().equals(Terrain.Blocked))
				{
					transProbability += pCorrectTraversal*prob.get(y-1).get(x);
				}
				break;
			case 'l':
				if(x == 0 || grid.get(y).get(x-1).getType().equals(Terrain.Blocked))
				{
					//traversal will always fail so the transitional probability will be the the same as doing nothing
					transProbability += prob.get(y).get(x);
				}
				else
				{
					transProbability += pIncorrectTraversal*prob.get(y).get(x);
				}
				if(x != grid.size()-1 && !grid.get(y).get(x+1).getType().equals(Terrain.Blocked))
				{
					transProbability += pCorrectTraversal*prob.get(y).get(x+1);
				}
				break;
			case 'r':
				if(x == grid.get(y).size()-1 || grid.get(y).get(x+1).getType().equals(Terrain.Blocked))
				{
					//traversal will always fail so the transitional probability will be the the same as doing nothing
					transProbability += prob.get(y).get(x);
				}
				else
				{
					transProbability += pIncorrectTraversal*prob.get(y).get(x);
				}
				if(x != 0 && !grid.get(y).get(x-1).getType().equals(Terrain.Blocked))
				{
					transProbability += pCorrectTraversal*prob.get(y).get(x-1);
				}
				break;
			default:
				System.err.println("Incorrect direction");
				break;
			}
			//if I were in C/C++ the following lines would be a memory leak
			if(grid.get(y).get(x).getType().equals(observation))
			{
				return new Double(transProbability*pCorrectSensor);
			}
			return new Double(transProbability*pIncorrectSensor);
		}
		return 0d;
	}
	/**
	 * Assumes observation lists and action lists have been filled out.
	 * Takes an index and calculates the probable current location given the observations up to the point of the index
	 */
	public void filter(int index)
	{
		if(index>acts.size()||index>observ.size())
		{
			System.err.println("Filtering out of bounds");
			return;
		}
		ArrayList<ArrayList<Double>> tmp = null; 
		//for as long as t is denoted by the index assuming t>=0
		for(int time = 0; time < index; time++)
		{
			for(int i = 0; i < prob.size();i++)
			{
				ArrayList<Double> row = prob.get(i);
				for(int j = 0; j < row.size(); j++)
				{
					Double THING = filterStep(j,i, acts.get(time), observ.get(time));
					buf.get(i).set(j, THING);
				}
			}
			System.out.println("Action: " + acts.get(time) +" Observation: "+ observ.get(time));
			//swap to avoid reallocating memory
			tmp = prob;
			prob = buf;
			buf = tmp; 
			normalize();
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
					System.out.print(" &\t");
				}
				else
				{
					System.out.println(" \\\\ \\hline");
				}
			}
		}
		System.out.println("\\end{tabular}");
		System.out.println("\\end{center}\n\n");
	}
}
