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
	public ArrayList<ArrayList<Node>> grid;
	
	/**
	 * Constructor for creating a grid from a file
	 * @param file
	 */
	public Grid(File file)
	{
		//creates all unblocked cells
		this.grid = new ArrayList<ArrayList<Node>>();
		Scanner scan;
		try
		{
			scan = new Scanner(file);
			String tmp;
			Node tmpNode;
			for (int row=0; scan.hasNext(); row++)
			{
				this.grid.add(new ArrayList<Node>());
				tmp = scan.nextLine();
				for (int col=0; col < tmp.length(); col++)
				{
					tmpNode = new Node();
					switch (tmp.charAt(col))
					{
					case 'N':
						tmpNode.setType(Terrain.Normal);
						break;
					case 'H':
						tmpNode.setType(Terrain.Highway);
						break;
					case 'T':
						tmpNode.setType(Terrain.Hard);
						break;
					case 'B':
						tmpNode.setType(Terrain.Blocked);
						break;
					default:
						System.out.println("Invalid Terrain type");
						System.exit(0);
						break;
					}
					
					tmpNode.setX(col);
					tmpNode.setY(row);
					this.grid.get(row).add(tmpNode);
				}
			}
			scan.close();
			//set neighbors
			for (int row=0; row<this.grid.size(); row++)
			{		
				for (int col=0; col<this.grid.get(row).size(); col++)
				{
					setNeighbors(row, col);
				}
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor for making a random grid of height*width size
	 * @param height
	 * @param width
	 */
	public Grid(int height, int width)
	{
		//creates all unblocked cells
		this.grid = new ArrayList<ArrayList<Node>>();
		for (int row=0; row<height; row++)
		{
			this.grid.add(new ArrayList<Node>());		
			for (int col=0; col<width; col++)
			{
				this.grid.get(row).add(new Node(Terrain.Normal));
			}
		}
		//set neighbors
		for (int row=0; row<height; row++)
		{		
			for (int col=0; col<width; col++)
			{
				setNeighbors(row, col);
			}
		}	
		generateTerrain(height, width);
	}
	
	/**
	 * generateTerrain - generates 50% normal terrain, 20% highway, 20% hard, 10% blocked cells and fills ArrayList
	 * @param height
	 * @param width
	 */
	public void generateTerrain(int height, int width){
		Random rand = new Random();
		double curr = rand.nextDouble();
		int totalCells = height*width;
		int nCells = (int) (totalCells * 0.5);
		int hwCells = (int) (totalCells * 0.2);
		int hdCells = (int) (totalCells * 0.2);
		int bCells = (int) (totalCells * 0.1);
		
		for(int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				if(curr >= 0 && curr < 0.5){
					if(nCells > 0){
						grid.get(i).get(j).setType(Terrain.Normal);
						nCells--;
					}else{
						j--;	
					}
				} else if(curr >= 0.5 && curr < 0.7){
					if(hwCells > 0){
						grid.get(i).get(j).setType(Terrain.Highway);
						hwCells--;
					}else{
						j--;	
					}	
				} else if(curr >= 0.7 && curr < 0.9){
					if(hdCells > 0){
						grid.get(i).get(j).setType(Terrain.Hard);
						hdCells--;
					}else{
						j--;	
					}
				} else{
					if(bCells > 0){
						grid.get(i).get(j).setType(Terrain.Blocked);
						bCells--;
					}else{
						j--;	
					}
				}
				curr = rand.nextDouble();
			}
		}	
	}
	
	/**
	 * default constructor
	 */
	public Grid(){
		
	}
	/**
	 * Determines if x/y coordinate location is in the grid
	 * @param grid
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isInBounds(int x, int y)
	{
		return (y < grid.size()&& x < grid.get(y).size())? true : false;
	}
	
	/**
	 * Sets the references of the grid nodes to point to neighbors
	 * @param row
	 * @param col
	 */
	private void setNeighbors(int row, int col)
	{
		//left cells
		if (col != 0)
		{
			if(row!=0)
			{
				grid.get(row).get(col).getNeighbors().add(grid.get(row-1).get(col-1));
			}
			grid.get(row).get(col).getNeighbors().add(grid.get(row).get(col-1));
			if(row!=grid.size()-1)
			{
				grid.get(row).get(col).getNeighbors().add(grid.get(row+1).get(col-1));
			}
		}
		//right cells
		if (col !=grid.get(row).size()-1)
		{
			if(row!=0)
			{
				grid.get(row).get(col).getNeighbors().add(grid.get(row-1).get(col+1));
			}
			grid.get(row).get(col).getNeighbors().add(grid.get(row).get(col+1));
			if(row!=grid.size()-1)
			{
				grid.get(row).get(col).getNeighbors().add(grid.get(row+1).get(col+1));
			}
		}
		//top
		if(row!=0)
		{
			grid.get(row).get(col).getNeighbors().add(grid.get(row-1).get(col));
		}
		//bottom
		if(row!=grid.size()-1)
		{
			grid.get(row).get(col).getNeighbors().add(grid.get(row+1).get(col));;
		}
	}
}