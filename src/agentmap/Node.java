package agentmap;

import java.util.ArrayList;


public class Node
{
	private int x, y;
	private Terrain type;
	private ArrayList<Node> neighbors = new ArrayList<Node>();
	
	public Node()
	{
		type = Terrain.Normal;
	}
	public Node(Terrain type)
	{
		this.type = type;
	}
	public Node(Terrain type, ArrayList<Node> neighbors)
	{
		this.type = type;
		this.neighbors = neighbors;
	}
	public int getX()
	{
		return x;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public int getY()
	{
		return y;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	public Terrain getType()
	{
		return type;
	}
	public void setType(Terrain type)
	{
		this.type = type;
	}
	public ArrayList<Node> getNeighbors()
	{
		return neighbors;
	}
	public void setNeighbors(ArrayList<Node> neighbors)
	{
		this.neighbors = neighbors;
	}
	public boolean addNeighbor(Node neighbor)
	{
		return neighbors.add(neighbor);
	}
}
