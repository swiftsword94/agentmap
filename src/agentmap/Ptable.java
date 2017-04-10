package agentmap;

import java.util.ArrayList;

public class Ptable
{
	public ArrayList<ArrayList<Double>> prob = new ArrayList<ArrayList<Double>>();
	public ArrayList<Character> acts = new ArrayList<Character>();
	public ArrayList<Terrain> observ = new ArrayList<Terrain>();
	int width = 0, height = 0;
	
	public Ptable(ArrayList<ArrayList<Node>> mapToCopy,int width, int height)
	{
		this.width = width;
		this.height = height;
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
	public void filter(Character act, Terrain observation)
	{
		
	}
}
