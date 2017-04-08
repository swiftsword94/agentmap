package agentmap;

import java.io.File;

public class Driver
{
	public static void main(String[] args)
	{
		Grid world = new Grid();
		world.createGrid(new File(args[0]));
	}
}
