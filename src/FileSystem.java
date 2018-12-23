import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class FileSystem 
{	
	private int castle [] = new int [4];
	private int forest [];
	private int lake [];
	private int numberOfSoldiers;
	private int numberOfArmada;
	private FileDataStructure x [];
	private FileDataStructure armadaX [];
	int mapWidth;
	int mapLength;
	int size = 0;
	int sizeX = 0;
	int numberOfWalls;
	private ArrayList<Integer>[] walls;
	
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

	public int getNumberOfWalls() { return numberOfWalls; }

	public ArrayList<Integer> []  getWalls()
	{
		return walls;
	}
	
	public void createLevel(int level)throws IOException
	{
		
		String lvl = "level" + level;

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

		numberOfWalls = Integer.parseInt(in.next());

		valid = in.next();
		
		int i = 0;
		walls = new ArrayList[numberOfWalls];
		while(!(valid.equals("levelEnd")))
		{
			walls[i] = new ArrayList<>();
			boolean isWall = Boolean.parseBoolean(valid);
			walls[i].add(isWall ? 1 : 0);

			valid = in.next();
			while(true)
			{
				walls[i].add(Integer.parseInt(valid));
				valid = in.next();
				
				if(valid.equals("levelEnd"))
					return;
				
				if(((valid.equals("true"))) || ((valid.equals("false"))))
					break;
			}
			i++;
		}
	}

	public void decryptMapFile() {
		decryptFile("Map.encrypted", "Map.txt");
	}

	public void decryptFile(String filePath, String temp)
	{
		String key = "This is a secret";
		File encryptedFile = new File(filePath);
		File decryptedFile = new File(temp);

		try {
			Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			FileInputStream inputStream = new FileInputStream(encryptedFile);
			byte[] inputBytes = new byte[(int) encryptedFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			FileOutputStream outputStream = new FileOutputStream(decryptedFile);
			outputStream.write(outputBytes);

			inputStream.close();
			outputStream.close();

		}
		catch (NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException e)
		{
			e.printStackTrace();
		}
	}

	public void encryptFile(String filePath, String encrypted)
	{
		String key = "This is a secret";
		File inputFile = new File(filePath);
		File encryptedFile = new File(encrypted);

		try {
			Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			FileOutputStream outputStream = new FileOutputStream(encryptedFile);
			outputStream.write(outputBytes);

			inputStream.close();
			outputStream.close();

		}
		catch (NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException e)
		{
			e.printStackTrace();
		}
	}

	public void deleteMapFile()
	{
		deleteFile("Map.txt");
	}

	public void deleteFile(String fileName)
	{
		File file = new File(fileName);
		file.delete();
	}


	public int[] getHintShape(int wallNumber, int levelNo) {
		decryptFile("solutions.encrypted", "hint.txt");

		int[] xYturn = new int[3];
		FileReader f = null;
		try {
			f = new FileReader("hint.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner scan = new Scanner(f);
		scan.useDelimiter(", *");
		int x, y, t;
		String level, wall;
		scan.nextLine();
		while (scan.hasNext()) {
			if (scan.hasNext()) {
				level = scan.next();
				if (level.equalsIgnoreCase("Level " + levelNo)) {
					while (scan.hasNext()) {
						wall = scan.next();
						int temp = wallNumber + 1;
						if (level.equalsIgnoreCase("Level " + levelNo) && wall.equalsIgnoreCase("Wall " + temp)) {
							xYturn[0] = Integer.parseInt(scan.next());
							xYturn[1] = Integer.parseInt(scan.next());
							xYturn[2] = Integer.parseInt(scan.next());
							scan.close();
							deleteFile("hint.txt");
							return xYturn;
						} else {
							scan.nextLine();
							level = scan.next();
						}

					}
				} else
					scan.nextLine();
			} else {
				break;
			}
			// scan.nextLine();
		}
		scan.close();

		deleteFile("hint.txt");

		return null;
	}

	public int getSavedLevelNo() {
		int last = 0;
		decryptFile("savedLevelNo.encrypted", "no.txt");
		try {
			// code loads the game
			File f = new File("no.txt");
			Scanner s = new Scanner(f);
			last = (Integer) s.nextInt();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		deleteFile("no.txt");
		return last;
	}

	public void writeSavedLevelNo(int levelNo) {
		decryptFile("savedLevelNo.encrypted", "level.txt");
		try {

			Writer wr = new FileWriter("level.txt");
			wr.write(levelNo + ""); // write string
			wr.flush();
			wr.close();
		} catch (Exception ea) {
			System.out.println("exception");
		}
		encryptFile("level.txt", "savedLevelNo.encrypted");
		deleteFile("level.txt");
	}
}
