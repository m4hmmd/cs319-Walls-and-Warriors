import java.util.ArrayList;

public class FileDataStructure 
{
	private int num [] = new int [2];
	private boolean inst [] = new boolean [2];
	private ArrayList <Integer> route;
	

	public void setSoldier(boolean a, boolean b, int x, int y, ArrayList<Integer> route)
	{
		inst[0] = a;
		inst[1] = b;
		num[0] = x;
		num[1] = y;
		this.route = route;
	}
	
	public int [] getNum()
	{
		return num;
	}
	
	public boolean [] getInst()
	{
		return inst;
	}

	public ArrayList <Integer> getRoute()
	{
		return route;
	}
}
