package agentmap;

import java.util.ArrayList;


public class Node
{
	private char type;
	private ArrayList<Node> neighbors;
	
	public Node()
	{
		type = 0;
	}
	public Node(char type)
	{
		this.type = type;
	}
	public Node(char type, ArrayList<Node> neighbors)
	{
		this.type = type;
		this.neighbors = neighbors;
	}
	public char getType()
	{
		return type;
	}
	public void setType(char type)
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
