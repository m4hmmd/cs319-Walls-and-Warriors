import org.omg.PortableInterceptor.INACTIVE;

import java.io.*;
import java.util.*;
public class FileSystem 
{	
	private int castle [] = new int [4];
	private int forestAndLake [] = new int [9];
	private boolean armada;
	private int number_of_soldiers[];
	private int size_of_soldiers;
	private FileDataStructure x [];
	int mapWidth;
	int mapLength;
	int size = 0;
	
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
	
	public void incrementSize()
	{
		size++;
	}
	
	public void resetSize()
	{
		size = 0;
	}
	
	public int getSizeOfSoldiers()
	{
		return size_of_soldiers;
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
	
	
	public int []  getForestAndLake()
	{
		return forestAndLake;
	}
	
	public boolean getArmada()
	{
		return armada;
	}
	
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
		
		number_of_soldiers = new int[Integer.parseInt(in.next())];
		size_of_soldiers = number_of_soldiers.length;
		
		x = new FileDataStructure[number_of_soldiers.length];
		for(int i = 0; i <number_of_soldiers.length ; i++)
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
		
		for(int i = 0; i < 9; i++)
		{
			forestAndLake[i] = Integer.parseInt(in.next());
		}
		
		armada = Boolean.parseBoolean(in.next());
		
		valid = in.next();
		int i = 0;
		while(!(valid.equals("levelEnd")))
		{
			x[i].setChainOrWall(Boolean.parseBoolean(valid));
			valid = in.next();
			while(true)
			{
				x[i].setShape(Integer.parseInt(valid));
				valid = in.next();
				
				if(valid.equals("levelEnd"))
					return;
				
				if(((valid.equals("true"))) || ((valid.equals("false"))))
					break;
			}
			i++;
		}
	}
}
