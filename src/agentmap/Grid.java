package agentmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Grid 
{
	private int width = 0;
	private int height = 0;
	private ArrayList<ArrayList<Node>> grid;

	public static <T> boolean isInBounds(ArrayList<ArrayList<T>> grid, int x, int y)
	{
		return (y < grid.size()&& x < grid.get(y).size())? true : false;
	}
	
	public void addHardCellBlock(ArrayList<ArrayList<Node>> grid, int xCoord, int yCoord, int size)
	{
		
		Random rand = new Random();
		if(size%2==0)
		{
			size++;
		}
		
		for(int x = xCoord; x < grid.get(yCoord).size()&& (x < xCoord+(size/2+1)); x++)//right
		{
			for(int y = yCoord; y >= 0 && (y > yCoord-(size/2+1)); y--)//righttop+middle
			{
				grid.get(y).get(x).setType((rand.nextBoolean())?'1':'2'); 
			}
			for(int y = yCoord+1; y < grid.size() && (y < yCoord+(size/2+1)); y++)//rightbottom
			{
				grid.get(y).get(x).setType((rand.nextBoolean())?'1':'2'); 
			}
		}
		for(int x = xCoord-1; x >= 0 && (x > xCoord-(size/2+1)); x--)//left
		{
			for(int y = yCoord; y >= 0 && (y > yCoord-(size/2+1)); y--)//lefttop+middle
			{
				grid.get(y).get(x).setType((rand.nextBoolean())?'1':'2'); 
			}
			for(int y = yCoord+1; y < grid.size() && (y < yCoord+(size/2+1)); y++)//leftbottom
			{
				grid.get(y).get(x).setType((rand.nextBoolean())?'1':'2'); 
			}
		}
	}
	/*
	public boolean setNeighbor(Node start, Node end)
	{
		if(start.addNeighbor(end))
		{
			return true;
		}
		else
		{
			return false;	
		}
	}
	*/
	public ArrayList<Node> addHighway(ArrayList<ArrayList<Node>> graph, int xCoord, int yCoord, int length)
	{
		int x = xCoord, y = yCoord;
		char direction;
		double dirchoice;
		boolean isEnd = false;
		Random random = new Random();
		ArrayList<Node> highway = new ArrayList<Node>();
		//pick direction from start
		if(x == 0)
		{
			if(y == 0)
			{
				direction = (random.nextBoolean())?'r':'d';
			}
			else if(y == graph.size()-1)
			{
				direction = (random.nextBoolean())?'r':'u';
			}
			else
			{
				direction = 'r';
			}
		}
		else if(x == graph.get(y).size()-1)
		{
			if(y == 0)
			{
				direction = (random.nextBoolean())?'l':'d';
			}
			else if(y == graph.size()-1)
			{
				direction = (random.nextBoolean())?'l':'u';
			}
			else
			{
				direction = 'l';
			}
		}
		else
		{
			if(y == 0)
			{
				direction = 'd';
			}
			else if(y == graph.size()-1)
			{
				direction = 'u';
			}
			else
			{
				return null;
			}
		}
		//continue forward
		while(!isEnd)
		{
			switch(direction)//draws lines given a direction
			{
			case 'u':
				for(int i = 0; i < length; i++, y--)
				{
					if(y <= 0)//&& highway.get(highway.size()-1).equals(graph.get(1).get(x)))
					{
						isEnd = true;
						break;
					}
					if(graph.get(y).get(x).getType() != 'a' && graph.get(y).get(x).getType() != 'b' && !highway.contains(graph.get(y).get(x)))
					{
						highway.add(graph.get(y).get(x));
					}
					else
					{
						return null;
					}
				}
				break;
			case 'd':
				for(int i = 0; i < length; i++, y++)
				{
					if(y >= graph.size()-1)//&& highway.get(highway.size()-1).equals(graph.get(graph.size()-2).get(x)))
					{
						isEnd = true;
						break;
					}
					if(graph.get(y).get(x).getType() != 'a' && graph.get(y).get(x).getType() != 'b' && !highway.contains(graph.get(y).get(x)))
					{
						highway.add(graph.get(y).get(x));
					}
					else
					{
						return null;
					}
				}
				break;
			case 'l':
				for(int i = 0; i < length; i++, x--)
				{
					if(x <= 0)//&& highway.get(highway.size()-1).equals(graph.get(y).get(1)))
					{
						isEnd = true;
						break;
					}
					if(graph.get(y).get(x).getType() != 'a' && graph.get(y).get(x).getType() != 'b' && !highway.contains(graph.get(y).get(x)))
					{
						highway.add(graph.get(y).get(x));
					}
					else
					{
						return null;
					}
				}
				break;
			case 'r':
				for(int i = 0; i < length; i++, x++)
				{
					if(x >= graph.get(y).size()-1)//&& highway.get(highway.size()-1).equals(graph.get(y).get(graph.get(y).size()-2)))
					{
						isEnd = true;
						break;
					}
					if(graph.get(y).get(x).getType() != 'a' && graph.get(y).get(x).getType() != 'b' && !highway.contains(graph.get(y).get(x)))
					{
						highway.add(graph.get(y).get(x));
					}
					else
					{
						return null;
					}
				}
				break;
			default:
				return null;
			}
			//chooses which direction to go
			dirchoice = random.nextDouble();  
			if(dirchoice < .6)
			{
				//go the same direction
			}
			else if(dirchoice < .8)//go counterclockwise
			{
				switch(direction)
				{
				case 'u':
					direction = 'l';
					break;
				case 'd':
					direction = 'r';
					break;
				case 'l':
					direction = 'd';
					break;
				case 'r':
					direction = 'u';
					break;
				default:
					return null;
				}
			}
			else//go clockwise
			{
				switch(direction)
				{
				case 'u':
					direction = 'r';
					break;
				case 'd':
					direction = 'l';
					break;
				case 'l':
					direction = 'u';
					break;
				case 'r':
					direction = 'd';
					break;
				default:return null;
				}
			}
		}
		//checking highway validity
		if(isEnd && highway.size()<100)
		{
			return null;
		}
		return highway;
	}
	/**
	 * Randomly fills a board with highways 
	 * @param graph An ArrayList of ArayList of Nodes represented in the form of a Cartesian plane.
	 * @param nHighways The number of highways to fill the board with.
	 */
	public void fillHighways(ArrayList<ArrayList<Node>> graph, int nHighways)
	{
		ArrayList<Node> highway = new ArrayList<Node>();
		int x = 0, y = 0;
		Random rand = new Random();
		/*
		 * I need to pick a number that resides on the outside boundaries of the walls randomly
		 * I could simply get all of the edge nodes and choose randomly
		 * I could try to pick a side and then pick a number  
		 */
		//pick a side
		double side;
		
		for(int i = 0; i < nHighways;)
		{
			side = rand.nextDouble();  
			if(side < .25)//top
			{
				y = 0;
				x = (int)Math.round(rand.nextDouble()*(graph.get(y).size()-1));
			}
			else if(side < .50)//bottom
			{
				y = graph.size()-1;
				x = (int)Math.round(rand.nextDouble()*(graph.get(y).size()-1));
			}
			else if(side < .75)//left
			{
				y = (int)Math.round(rand.nextDouble()*(graph.size()-1));
				x = 0;
			}
			else//right
			{
				y = (int)Math.round(rand.nextDouble()*(graph.size()-1));
				x = graph.get(y).size()-1;
			}
			
			highway = addHighway(graph, x, y, 20);
			if(highway != null)
			{
				i++;
				for(Node e : highway)
				{
					e.setType((e.getType()  =='1' || e.getType()  =='a' )? 'a' : 'b');
				}
			}
		}
	}
	public void addBlockedCell(ArrayList<ArrayList<Node>> graph, int x, int y)
	{
		graph.get(y).get(x).setType('0');
	}
	public void fillBlockedCells(ArrayList<ArrayList<Node>> graph, int cellnum)
	{
		Random rand = new Random();
		int x = 0, y = 0;
		for(int i = 0;i<cellnum;)
		{
			y = (int) Math.round(rand.nextDouble()*(graph.size()-1));
			x = (int) Math.round(rand.nextDouble()*(graph.get(y).size()-1));
			addBlockedCell(graph, x, y);
			i++;
		}
	}
	//create grid
	public ArrayList<ArrayList<Node>> createGrid(int height, int width)
	{
		//creates all unblocked cells
		this.grid = new ArrayList<ArrayList<Node>>();
		for (int row=0; row<height; row++)
		{
			this.grid.add(new ArrayList<Node>());		
			for (int col=0; col<width; col++)
			{
				this.grid.get(row).add(new Node('1'));
			}
		}
		//set neighbors
		for (int row=0; row<height; row++)
		{		
			for (int col=0; col<width; col++)
			{
				grid.get(row).get(col).addNeighbor(grid.get(row-1).get(col+1));
				grid.get(row).get(col).addNeighbor(grid.get(row-1).get(col));
				grid.get(row).get(col).addNeighbor(grid.get(row-1).get(col-1));
				grid.get(row).get(col).addNeighbor(grid.get(row).get(col+1));
				grid.get(row).get(col).addNeighbor(grid.get(row).get(col-1));
				grid.get(row).get(col).addNeighbor(grid.get(row+1).get(col+1));
				grid.get(row).get(col).addNeighbor(grid.get(row+1).get(col));
				grid.get(row).get(col).addNeighbor(grid.get(row+1).get(col-1));
			}
		}
		
		for(int i = 0;i < 1;i++)//fills the map with hard cells
		{
			addHardCellBlock(this.grid, (int)Math.round(Math.random()*(width-1)),(int)Math.round(Math.random()*(height-1)), 1);
		}
		//DO HIGHWAYS
		fillHighways(this.grid, 4);
		//DO Blocked Cells
		fillBlockedCells(this.grid, (int)Math.round(this.grid.size()*this.grid.get(0).size()*.2));
		return this.grid;
	}
	public ArrayList<ArrayList<Node>> createGrid(File file)
	{
		//creates all unblocked cells
		this.grid = new ArrayList<ArrayList<Node>>();
		Scanner scan;
		try
		{
			scan = new Scanner(file);
			String tmp;
			for (int row=0; scan.hasNext(); row++)
			{
				this.grid.add(new ArrayList<Node>());
				tmp = scan.nextLine();
				for (int col=0; col < tmp.length(); col++)
				{
					this.grid.get(row).add(new Node(tmp.charAt(col)));
				}
			}
			scan.close();
			//set neighbors
				for (int row=0; row<this.grid.size(); row++)
				{		
					for (int col=0; col<this.grid.get(row).size(); col++)
					{
						grid.get(row).get(col).addNeighbor(grid.get(row-1).get(col+1));
						grid.get(row).get(col).addNeighbor(grid.get(row-1).get(col));
						grid.get(row).get(col).addNeighbor(grid.get(row-1).get(col-1));
						grid.get(row).get(col).addNeighbor(grid.get(row).get(col+1));
						grid.get(row).get(col).addNeighbor(grid.get(row).get(col-1));
						grid.get(row).get(col).addNeighbor(grid.get(row+1).get(col+1));
						grid.get(row).get(col).addNeighbor(grid.get(row+1).get(col));
						grid.get(row).get(col).addNeighbor(grid.get(row+1).get(col-1));
					}
				}
				return this.grid;
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				return null;
			}
		}
	}