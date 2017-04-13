package agentmap;

import java.io.File;
import java.util.Random;

public class Driver
{
	public static void main(String[] args)
	{
		Grid world = new Grid(new File(args[0]));
		Random rand = new Random();
		int random1, random2;
		do
		{
			random1 = (int) Math.round((world.grid.size()-1) * rand.nextDouble());
			random2 = (int) Math.round((world.grid.get(0).size()-1) * rand.nextDouble());
		}
		while(world.grid.get(random1).get(random2).getType().equals(Terrain.Blocked));
		Player one = new Player(random2, random1, world);
		one.activate(world);
		one.ptable.filter(100);
	}
}
