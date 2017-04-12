package agentmap;

import java.io.File;
import java.util.Random;

public class Driver
{
	public static void main(String[] args)
	{
		Grid world = new Grid(new File(args[0]));
		Random rand = new Random();
		int random1 = (int) (world.grid.size() * rand.nextDouble());
		int random2 = (int) (world.grid.get(1).size() * rand.nextDouble());
		Player one = new Player(random1, random2, world);
		one.activate(world);
	}
}
