
import java.util.ArrayList;

public class FileDataStructure 
{
	private int num [] = new int [2];
	private boolean inst [] = new boolean [2];
	private boolean chainOrWall;
	private ArrayList <Integer> shape = new ArrayList <Integer>();
	

	public void setSoldier(boolean a,boolean b, int x, int y)
	{
		inst[0] = a;
		inst[1] = b;
		num[0] = x;
		num[1] = y;
	}
	
	public void setChainOrWall(boolean a)
	{
		chainOrWall = a;
	}
	
	public void setShape(int x)
	{
		shape.add(x);
	}
	
	public int [] getNum()
	{
		return num;
	}
	
	public boolean [] getInst()
	{
		return inst;
	}
	
	public boolean getChainOrWall()
	{
		return chainOrWall;
	}
	
	public ArrayList <Integer> getShape()
	{
		return shape;
	}
}
