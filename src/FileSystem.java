import java.io.*;
import java.util.*;
public class FileSystem 
{	
	private int castle [] = new int [4];
	private int forest [];
	private int lake [];
	private boolean armada;
	private int numberOfSoldiers;
	private int numberOfArmada;
	private FileDataStructure x [];
	private FileDataStructure armadaX [];
	int mapWidth;
	int mapLength;
	int size = 0;
	int sizeX = 0;
	int numberOfWalls;
	
	public void setState(int level)throws IOException
	{
		PrintWriter write = new PrintWriter("GameState.txt");
		write.println(level);
		write.close();
	}
	
	public int getState()throws IOException
	{
		Scanner scan = new Scanner(System.in);
		
		File read = new File("GameState.txt");
		Scanner in = new Scanner(read);
		
		String level = in.next();
		return Integer.parseInt(level);
	}
	
	public FileDataStructure getDataStructure()
	{
		return x[size];
	}

	public FileDataStructure getDataStructureX()
	{
		return armadaX[sizeX];
	}
	
	public void incrementSize()
	{
		size++;
	}
	
	public void resetSize()
	{
		size = 0;
	}

	public void incrementSizeX()
	{
		sizeX++;
	}

	public void resetSizeX()
	{
		sizeX = 0;
	}
	
	public int getNumberOfSoldiers()
	{
		return numberOfSoldiers;
	}

	public int getNumberOfArmada()
	{
		return numberOfArmada;
	}
	
	public int [] getCastle()
	{
		return castle;
	}
	
	public int  getMapWidth()
	{
		return mapWidth;
	}
	
	public int  getMapLength()
	{
		return mapLength;
	}

	public int []  getForest()
	{
		return forest;
	}

	public int []  getLake()
	{
		return lake;
	}

	public boolean getArmada()
	{
		return armada;
	}

	public int getNumberOfWalls() { return numberOfWalls + 1; }
	
	public void createLevel(int level)throws IOException
	{
		
		String lvl = "level" + level;
		
		Scanner scan = new Scanner(System.in);
		
		File read = new File("Map.txt");
		Scanner in = new Scanner(read);
		
		String valid = in.next();
		while(true)
		{
			if(valid.equals(lvl))
					break;
			if(valid.equals("end"))
				return;
			valid = in.next();
		}
		
		
		for(int i = 0; i < 4; i++)
		{
			castle[i] = Integer.parseInt(in.next());
		}
		
		mapWidth = Integer.parseInt(in.next());
		mapLength = Integer.parseInt(in.next());
		
		numberOfSoldiers = Integer.parseInt(in.next());
		
		x = new FileDataStructure[numberOfSoldiers];
		for(int i = 0; i < numberOfSoldiers; i++)
		{
			x[i] = new FileDataStructure();
			boolean ally = Boolean.parseBoolean(in.next());
			boolean movable = Boolean.parseBoolean(in.next());
			if (!movable) {
				x[i].setSoldier(ally, false, Integer.parseInt(in.next()), Integer.parseInt(in.next()), null);
			} else {
				int xx = Integer.parseInt(in.next());
				int y = Integer.parseInt(in.next());
				int routeLength = Integer.parseInt(in.next());
				ArrayList<Integer> route = new ArrayList<>();
				for (int j = 0; j < routeLength; j++) {
					route.add(Integer.parseInt(in.next()));
				}
				x[i].setSoldier(ally, true, xx, y, route);
			}
		}

		numberOfArmada = Integer.parseInt(in.next());

		armadaX = new FileDataStructure[numberOfArmada];
		for(int i = 0; i < numberOfArmada; i++)
		{
			armadaX[i] = new FileDataStructure();
			boolean ally = Boolean.parseBoolean(in.next());
			boolean movable = Boolean.parseBoolean(in.next());
			if (!movable) {
				armadaX[i].setSoldier(ally, false, Integer.parseInt(in.next()), Integer.parseInt(in.next()), null);
			} else {
				int xx = Integer.parseInt(in.next());
				int y = Integer.parseInt(in.next());
				int routeLength = Integer.parseInt(in.next());
				ArrayList<Integer> route = new ArrayList<>();
				for (int j = 0; j < routeLength; j++) {
					route.add(Integer.parseInt(in.next()));
				}
				armadaX[i].setSoldier(ally, true, xx, y, route);
			}
		}

		int numberOfLake = Integer.parseInt(in.next());
		lake = new int[2 * numberOfLake];
		for(int i = 0; i < 2 * numberOfLake; i++)
		{
			lake[i] = Integer.parseInt(in.next());
		}

		int numberOfForest = Integer.parseInt(in.next());
		forest = new int[2 * numberOfForest];
		for(int i = 0; i < 2 * numberOfForest; i++)
		{
			forest[i] = Integer.parseInt(in.next());
		}
		System.out.println("read all forests");

		valid = in.next();
		numberOfWalls = 0;
		while(!(valid.equals("levelEnd")))
		{
			x[numberOfWalls].setChainOrWall(Boolean.parseBoolean(valid));
			valid = in.next();
			while(true)
			{
				x[numberOfWalls].setShape(Integer.parseInt(valid));
				valid = in.next();
				
				if(valid.equals("levelEnd"))
					return;
				
				if(((valid.equals("true"))) || ((valid.equals("false"))))
					break;
			}
			numberOfWalls++;
		}
	}
}
