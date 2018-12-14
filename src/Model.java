import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Timer;

public class Model {
	public static final int CORNER = -3;
	public static final int EDGE_OF_LAKE = -2;
	public static final int EDGE_POINTS = -1;
	public static final int EMPTY = 0;
	public static final int CASTLE = 1;
	public static final int ALLY = 2;
	public static final int ENEMY = 3;
	public static final int FOREST = 4;
	public static final int LAKE = 5;
	public static final int WALL_OR_CHAIN = 6;
	public static final int VALID_FOR_WALL = 7;
	public static final int VALID_FOR_CHAIN = 8;
	public static final int ALLY_ARMADA = 9;
	public static final int ENEMY_ARMADA = 10;
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;

	private WallOrChain[] walls;
	private ArrayList<Soldier> soldiers;
	public ArrayList<Soldier> movables;
	private Castle castle;
	public GameObject[][] gameObjects;
	int noOfEnemies;

	int[][] map;

	int levelNo;
	boolean gameFinished = false;
	int squareHeight = 70, squareWidth = 80, lineWidth = squareWidth / 10, initialXShift = 200, initialYShift = 80,
			mapWidth, mapLength, barShift = 75;

	public Model(int mapWidth, int mapLength, Castle castle, int levelNo) {
		this.mapLength = mapLength;
		this.mapWidth = mapWidth;
		this.castle = castle;
		this.levelNo = levelNo;
		soldiers = new ArrayList<Soldier>();
		movables = new ArrayList<Soldier>();

		map = new int[2 * mapWidth + 1][2 * mapLength + 1];

		gameObjects = new GameObject[mapWidth][mapLength];

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (i % 2 == 0 && j % 2 == 1 || i % 2 == 1 && j % 2 == 0)
					map[i][j] = VALID_FOR_WALL;
				else if (i % 2 == 0 && j % 2 == 0) {
					map[i][j] = EDGE_POINTS;
				}
			}
		}

		// unused lines
		map[0][1] = CORNER;
		map[1][0] = CORNER;

		map[map.length - 2][0] = CORNER;
		map[map.length - 1][1] = CORNER;

		map[0][map[0].length - 2] = CORNER;
		map[1][map[0].length - 1] = CORNER;

		map[map.length - 1][map[0].length - 2] = CORNER;
		map[map.length - 2][map[0].length - 1] = CORNER;

		// Adding them to the Map
		gameObjects[castle.getX()][castle.getY()] = castle;
		gameObjects[castle.getX1()][castle.getY1()] = castle;

		map[castle.getX() * 2 + 1][castle.getY() * 2 + 1] = CASTLE;
		map[castle.getX1() * 2 + 1][castle.getY1() * 2 + 1] = CASTLE;

		// invalidate the line segment occupied by the castle
		int leftOne = (castle.x == castle.x1) ? castle.x : ((castle.x > castle.x1) ? castle.x1 : castle.x);
		int topOne = (castle.y == castle.y1) ? castle.y : ((castle.y > castle.y1) ? castle.y1 : castle.y);
		if (castle.x == castle.x1) {
			map[leftOne * 2 + 1][topOne * 2 + 2] = CASTLE;
		} else {
			map[leftOne * 2 + 2][topOne * 2 + 1] = CASTLE;
		}

		walls = new WallOrChain[0];
	}

	void reset() {
		map = new int[2 * mapWidth + 1][2 * mapLength + 1];

		gameFinished = false;

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (i % 2 == 0 && j % 2 == 1 || i % 2 == 1 && j % 2 == 0)
					map[i][j] = VALID_FOR_WALL;
				else if (i % 2 == 0 && j % 2 == 0) {
					map[i][j] = EDGE_POINTS;
				}
			}
		}

		// unused lines
		map[0][1] = CORNER;
		map[1][0] = CORNER;

		map[map.length - 2][0] = CORNER;
		map[map.length - 1][1] = CORNER;

		map[0][map[0].length - 2] = CORNER;
		map[1][map[0].length - 1] = CORNER;

		map[map.length - 1][map[0].length - 2] = CORNER;
		map[map.length - 2][map[0].length - 1] = CORNER;

		map[castle.getX() * 2 + 1][castle.getY() * 2 + 1] = CASTLE;
		map[castle.getX1() * 2 + 1][castle.getY1() * 2 + 1] = CASTLE;

		for (Soldier s : soldiers) {
			if (s.getColor() == Color.RED)
				map[s.getX() * 2 + 1][s.getY() * 2 + 1] = ENEMY;
			if (s.getColor() == Color.BLUE)
				map[s.getX() * 2 + 1][s.getY() * 2 + 1] = ALLY;
		}

		// invalidate the line segment occupied by the castle
		int leftOne = (castle.x == castle.x1) ? castle.x : ((castle.x > castle.x1) ? castle.x1 : castle.x);
		int topOne = (castle.y == castle.y1) ? castle.y : ((castle.y > castle.y1) ? castle.y1 : castle.y);
		if (castle.x == castle.x1) {
			map[leftOne * 2 + 1][topOne * 2 + 2] = CASTLE;
		} else {
			map[leftOne * 2 + 2][topOne * 2 + 1] = CASTLE;
		}

		for (WallOrChain w : walls) {
			w.reset(initialXShift, initialYShift, squareHeight, squareWidth);
		}
		resetWholeMapWithRespectToMap();
	}

	public void addSoldier(boolean ally, boolean movable, int i, int j) {
		Soldier s = null;
		if (ally)
			s = new Ally(movable, i, j);
		else {
			s = new Enemy(movable, i, j);
			noOfEnemies++;
		}
		if (movable)
			movables.add(s);
		soldiers.add(s);
		gameObjects[i][j] = s;
		map[s.getX() * 2 + 1][s.getY() * 2 + 1] = s.getWholeMapIndex();
	}

	private void resetWholeMapWithRespectToMap() {
		for (int i = 0; i < gameObjects.length; i++) {
			for (int j = 0; j < gameObjects[0].length; j++) {
				if (gameObjects[i][j] instanceof Lake) {
					gameObjects[i][j] = null;
					addLake(i, j);
				} else if (gameObjects[i][j] instanceof Forest) {
					gameObjects[i][j] = null;
					addForest(i, j);
				}
			}
		}
	}

	void removeFromLines(WallOrChain w) {
		for (int i = 0; i < w.points.size() - 1; i++) {
			int leftOne = (w.points.get(i).x == w.points.get(i + 1).x) ? w.points.get(i).x
					: ((w.points.get(i).x > w.points.get(i + 1).x) ? w.points.get(i + 1).x : w.points.get(i).x);
			int topOne = (w.points.get(i).y == w.points.get(i + 1).y) ? w.points.get(i).y
					: ((w.points.get(i).y > w.points.get(i + 1).y) ? w.points.get(i + 1).y : w.points.get(i).y);
			if (w.points.get(i).x == w.points.get(i + 1).x) {
				if (w instanceof Wall) {
					map[(w.xInd + leftOne) * 2][(w.yInd + topOne) * 2 + 1] = VALID_FOR_WALL;
				} else {
					map[(w.xInd + leftOne) * 2][(w.yInd + topOne) * 2 + 1] = VALID_FOR_CHAIN;
				}
			} else {
				if (w instanceof Wall) {
					map[(w.xInd + leftOne) * 2 + 1][(w.yInd + topOne) * 2] = VALID_FOR_WALL;
				} else {
					map[(w.xInd + leftOne) * 2 + 1][(w.yInd + topOne) * 2] = VALID_FOR_CHAIN;
				}
			}
		}

		for (int i = 0; i < w.edgesX.length; i++) {
			map[(w.xInd + w.edgesX[i]) * 2][(w.yInd + w.edgesY[i]) * 2] = EMPTY;
		}
	}

	boolean onAvailablePlace(WallOrChain w) {
		if (outOfScreen(w))
			return false;
		for (int i = 0; i < w.points.size() - 1; i++) {
			int leftOne = (w.points.get(i).x == w.points.get(i + 1).x) ? w.points.get(i).x
					: ((w.points.get(i).x > w.points.get(i + 1).x) ? w.points.get(i + 1).x : w.points.get(i).x);
			int topOne = (w.points.get(i).y == w.points.get(i + 1).y) ? w.points.get(i).y
					: ((w.points.get(i).y > w.points.get(i + 1).y) ? w.points.get(i + 1).y : w.points.get(i).y);
			int valid = 0;

			if (w instanceof Wall)
				valid = VALID_FOR_WALL;
			else
				valid = VALID_FOR_CHAIN;
			if (w.points.get(i).x == w.points.get(i + 1).x
					&& (map[(w.xInd + leftOne) * 2][(w.yInd + topOne) * 2 + 1] != valid)) {
				return false;
			} else if (w.points.get(i).y == w.points.get(i + 1).y
					&& (map[(w.xInd + leftOne) * 2 + 1][(w.yInd + topOne) * 2] != valid)) {
				return false;
			}
		}
		for (int i = 0; i < w.edgesX.length; i++) {
			if (map[(w.xInd + w.edgesX[i]) * 2][(w.yInd + w.edgesY[i]) * 2] == WALL_OR_CHAIN) {
				return false;
			}
		}
		return true;
	}

	boolean isAvailablePlaceFor(WallOrChain w, int indexX, int indexY) {
		if (indexX < 0 || indexY < 0)
			return false;

		for (int i = 0; i < w.points.size() - 1; i++) {
			int leftOne = (w.points.get(i).x == w.points.get(i + 1).x) ? w.points.get(i).x
					: ((w.points.get(i).x > w.points.get(i + 1).x) ? w.points.get(i + 1).x : w.points.get(i).x);
			int topOne = (w.points.get(i).y == w.points.get(i + 1).y) ? w.points.get(i).y
					: ((w.points.get(i).y > w.points.get(i + 1).y) ? w.points.get(i + 1).y : w.points.get(i).y);
			int valid = 0;

			if (w instanceof Wall)
				valid = VALID_FOR_WALL;
			else
				valid = VALID_FOR_CHAIN;
			if (w.points.get(i).x == w.points.get(i + 1).x
					&& (map[(indexX + leftOne) * 2][(indexY + topOne) * 2 + 1] != valid)) {
				return false;
			} else if (w.points.get(i).y == w.points.get(i + 1).y
					&& (map[(indexX + leftOne) * 2 + 1][(indexY + topOne) * 2] != valid)) {
				return false;
			}
		}
		return true;
	}

	void addToLines(WallOrChain w) {
		for (int i = 0; i < w.points.size() - 1; i++) {
			int leftOne = (w.points.get(i).x == w.points.get(i + 1).x) ? w.points.get(i).x
					: ((w.points.get(i).x > w.points.get(i + 1).x) ? w.points.get(i + 1).x : w.points.get(i).x);
			int topOne = (w.points.get(i).y == w.points.get(i + 1).y) ? w.points.get(i).y
					: ((w.points.get(i).y > w.points.get(i + 1).y) ? w.points.get(i + 1).y : w.points.get(i).y);
			if (w.points.get(i).x == w.points.get(i + 1).x) {
				map[(w.xInd + leftOne) * 2][(w.yInd + topOne) * 2 + 1] = WALL_OR_CHAIN;
			} else {
				map[(w.xInd + leftOne) * 2 + 1][(w.yInd + topOne) * 2] = WALL_OR_CHAIN;
			}
		}

		for (int i = 0; i < w.edgesX.length; i++) {
			map[(w.xInd + w.edgesX[i]) * 2][(w.yInd + w.edgesY[i]) * 2] = WALL_OR_CHAIN;
		}
	}

	public boolean isGameFinished() {

		ArrayList<Point> visited = new ArrayList<Point>();
		int numberOfEnemies = 0;

		for (int i = 1; i < map.length; i = i + 2) {
			if (map[i][0] != WALL_OR_CHAIN)
				numberOfEnemies += numberOfEnemiesOutside(i, 1, visited);
		}

		for (int i = 1; i < map.length; i = i + 2) {
			if (map[i][map[0].length - 1] != WALL_OR_CHAIN)
				numberOfEnemies += numberOfEnemiesOutside(i, map[0].length - 2, visited);
		}

		for (int j = 1; j < map[0].length; j = j + 2) {
			if (map[0][j] != WALL_OR_CHAIN)
				numberOfEnemies += numberOfEnemiesOutside(1, j, visited);
		}

		for (int j = 1; j < map[0].length; j = j + 2) {
			if (map[map.length - 1][j] != WALL_OR_CHAIN)
				numberOfEnemies += numberOfEnemiesOutside(map.length - 2, j, visited);
		}

		return numberOfEnemies == noOfEnemies;
	}

	public int numberOfEnemiesOutside(int i, int j, ArrayList<Point> visited) {
		if (visited.contains(new Point(i, j)))
			return 0;
		if (j < 0)
			return 0;
		if (i < 0)
			return 0;
		if (j > map[0].length - 1)
			return 0;
		if (i > map.length - 1)
			return 0;
		if (map[i][j] == ALLY || map[i][j] == ALLY_ARMADA)
			return -80;

		visited.add(new Point(i, j));
		int cUp = 0;
		int cDown = 0;
		int cRight = 0;
		int cLeft = 0;

		if ((map[i][j + 1] != WALL_OR_CHAIN && map[i][j + 1] != CASTLE))
			cUp = numberOfEnemiesOutside(i, j + 2, visited);
		if ((map[i][j - 1] != WALL_OR_CHAIN && map[i][j - 1] != CASTLE))
			cDown = numberOfEnemiesOutside(i, j - 2, visited);
		if ((map[i + 1][j] != WALL_OR_CHAIN && map[i + 1][j] != CASTLE))
			cRight = numberOfEnemiesOutside(i + 2, j, visited);
		if ((map[i - 1][j] != WALL_OR_CHAIN && map[i - 1][j] != CASTLE))
			cLeft = numberOfEnemiesOutside(i - 2, j, visited);

		int thisCell = map[i][j] == ENEMY || map[i][j] == ENEMY_ARMADA ? 1 : 0;

		if (cUp == -80 || cDown == -80 || cRight == -80 || cLeft == -80)
			return -80;
		return thisCell + cUp + cDown + cRight + cLeft;
	}

	public void printWholeMap() {

		int[][] array = Model.transpose(map);

		System.out.println("---------------------------------------------------");

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				System.out.printf("%4d", array[i][j]);
			}
			System.out.println();
		}
		System.out.println("---------------------------------------------------");
	}

	public static int[][] transpose(int arr[][]) {
		int m = arr.length;
		int n = arr[0].length;
		int ret[][] = new int[n][m];

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				ret[j][i] = arr[i][j];
			}
		}

		return ret;
	}

	boolean outOfScreen(WallOrChain w) {
		ArrayList<Point> points = w.points;
		for (int i = 0; i < points.size() - 1; i++) {
			int leftMost = points.get(i).x;
			int rightMost = points.get(i).x;
			int topMost = points.get(i).y;
			int bottomMost = points.get(i).y;

			if (leftMost > points.get(i + 1).x) {
				leftMost = points.get(i + 1).x;
			}
			if (rightMost < points.get(i + 1).x) {
				rightMost = points.get(i + 1).x;
			}
			if (topMost > points.get(i + 1).y) {
				topMost = points.get(i + 1).y;
			}
			if (bottomMost < points.get(i + 1).y) {
				bottomMost = points.get(i + 1).y;
			}
			if (leftMost + w.xInd == 0 && topMost + w.yInd == 0)
				return true;
			if (leftMost + w.xInd == 0 && bottomMost + w.yInd == mapLength)
				return true;
			if (rightMost + w.xInd == mapWidth && topMost + w.yInd == 0)
				return true;
			if (rightMost + w.xInd == mapWidth && bottomMost + w.yInd == mapLength)
				return true;

			if ((leftMost + w.xInd < 0) | (rightMost + w.xInd > mapWidth) | (topMost + w.yInd < 0)
					| (bottomMost + w.yInd > mapLength))
				return true;
		}
		return false;
	}

	public void setWalls(WallOrChain[] walls2) {
		this.walls = walls2;
	}

	public void addWallOrChain(boolean wall, int... points) {
		int[] yCoors = new int[points.length + 1];
		int[] xCoors = new int[points.length + 1];

		setXandYCoors(xCoors, yCoors, points);

		WallOrChain w = null;

		if (wall)
			w = new Wall(-1, -1, xCoors, yCoors, getColor(walls.length), walls.length, initialXShift, initialYShift,
					squareWidth, squareWidth, mapLength, mapWidth);
		else
			w = new Chain(-1, -1, xCoors, yCoors, getColor(walls.length), walls.length, initialXShift, initialYShift,
					squareWidth, squareWidth, mapLength, mapWidth);

		addWallOrChain(w);
		updateWallContainers();

	}

	private void addWallOrChain(WallOrChain w) {
		WallOrChain[] wallsTemp = new WallOrChain[walls.length + 1];
		int i = 0;
		for (WallOrChain wOrC : walls) {
			wallsTemp[i++] = wOrC;
		}
		wallsTemp[walls.length] = w;
		walls = wallsTemp;
	}

	private void setXandYCoors(int[] xCoors, int[] yCoors, int[] points) {
		int currentX = 0;
		int currentY = 0;
		int i = 0;

		for (int p : points) {
			xCoors[i] = currentX;
			yCoors[i] = currentY;
			if (p == Model.UP) {
				currentY--;
			} else if (p == Model.DOWN) {
				currentY++;
			} else if (p == Model.LEFT) {
				currentX--;
			} else if (p == Model.RIGHT) {
				currentX++;
			}
			i++;
		}
		xCoors[i] = currentX;
		yCoors[i] = currentY;
	}

	private void updateWallContainers() {
		for (int i = 0; i < walls.length; i++) {
			walls[i].setWallContainer(
					new Rectangle(initialXShift + squareWidth * i + (mapWidth - walls.length) * (squareWidth - 2) / 2,
							initialYShift + squareHeight * (mapLength + 1), squareWidth, squareHeight));
			walls[i].setRectangles();
		}

	}

	private Color getColor(int index) {
		Color c = new Color(225, 128, 8).darker().darker();

		for (; index > 0; index--) {
			c = c.brighter();
		}
		return c;
	}

	public WallOrChain[] getWalls() {
		return walls;
	}

	public ArrayList<Soldier> getSoldiers() {
		return soldiers;
	}

	public Castle getCastle() {
		return castle;
	}

	public GameObject[][] getMap() {
		return gameObjects;
	}

	public int getSquareHeight() {
		return squareHeight;
	}

	public int getSquareWidth() {
		return squareWidth;
	}

	public int getInitialXShift() {
		return initialXShift;
	}

	public int getInitialYShift() {
		return initialYShift;
	}

	public void setInitialXShift(int xShift) {
		initialXShift = xShift;
	}

	public void setInitialYShift(int yShift) {
		initialYShift = yShift;
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public int getMapLength() {
		return mapLength;
	}

	void setSizesOfObjects() {
		for (int i = 0; i < walls.length; i++) {
			Rectangle r = new Rectangle(
					initialXShift + squareWidth * i + (mapWidth - walls.length) * (squareWidth - 2) / 2,
					initialYShift + squareHeight * (mapLength + 1), squareWidth, squareHeight);
			walls[i].setWallContainer(r);
			walls[i].setRectangles();
		}
	}

	void rearrangeWalls() {
		for (int i = 0; i < walls.length; i++) {
			walls[i].setThePositionAgainByIndex(initialXShift, initialYShift, squareHeight, squareWidth);
		}
	}

	int getBarShift() {
		return barShift;
	}

	public void addGameObject(int i, int j, GameObject gmo) {
		gameObjects[i][j] = gmo;
	}

	public void addLake(int i, int j) {
		if (gameObjects[i][j] != null)
			return;
		gameObjects[i][j] = new Lake(i, j);
		map[2 * i + 1][2 * j + 1] = LAKE;

		boolean up = false;
		boolean down = false;
		boolean right = false;
		boolean left = false;

		map[2 * i + 1][2 * j] = EDGE_OF_LAKE;
		map[2 * i + 1][2 * j + 2] = EDGE_OF_LAKE;
		map[2 * i][2 * j + 1] = EDGE_OF_LAKE;
		map[2 * i + 2][2 * j + 1] = EDGE_OF_LAKE;

		if (i != 0) {
			left = map[2 * i - 1][2 * j + 1] == LAKE;
		}
		if (i < mapWidth - 1) {
			right = map[2 * i + 3][2 * j + 1] == LAKE;
		}

		if (j != 0) {
			up = map[2 * i + 1][2 * j - 1] == LAKE;
		}
		if (j < mapLength - 1) {
			down = map[2 * i + 1][2 * j + 3] == LAKE;
		}

		if (up) {
			map[2 * i + 1][2 * j] = VALID_FOR_CHAIN;
		}

		if (down) {
			map[2 * i + 1][2 * j + 2] = VALID_FOR_CHAIN;
		}

		if (left) {
			map[2 * i][2 * j + 1] = VALID_FOR_CHAIN;
		}

		if (right) {
			map[2 * i + 2][2 * j + 1] = VALID_FOR_CHAIN;
		}
	}

	public void addAllyArmada(int i, int j, boolean movable) {
		if (gameObjects[i][j] != null)
			return;

		gameObjects[i][j] = new AllyArmada(movable, i, j);
		map[2 * i + 1][2 * j + 1] = ALLY_ARMADA;

		if (movable)
			movables.add((Soldier) gameObjects[i][j]);

		boolean up = false;
		boolean down = false;
		boolean right = false;
		boolean left = false;

		map[2 * i + 1][2 * j] = EDGE_OF_LAKE;
		map[2 * i + 1][2 * j + 2] = EDGE_OF_LAKE;
		map[2 * i][2 * j + 1] = EDGE_OF_LAKE;
		map[2 * i + 2][2 * j + 1] = EDGE_OF_LAKE;

		if (i != 0) {
			left = map[2 * i - 1][2 * j + 1] == LAKE || map[2 * i - 1][2 * j + 1] == ALLY_ARMADA
					|| map[2 * i - 1][2 * j + 1] == ENEMY_ARMADA;
		}
		if (i < mapWidth - 1) {
			right = map[2 * i + 3][2 * j + 1] == LAKE || map[2 * i + 3][2 * j + 1] == ALLY_ARMADA
					|| map[2 * i + 3][2 * j + 1] == ENEMY_ARMADA;
		}

		if (j != 0) {
			up = map[2 * i + 1][2 * j - 1] == LAKE || map[2 * i + 1][2 * j - 1] == ALLY_ARMADA
					|| map[2 * i + 1][2 * j - 1] == ENEMY_ARMADA;
		}
		if (j < mapLength - 1) {
			down = map[2 * i + 1][2 * j + 3] == LAKE || map[2 * i + 1][2 * j + 3] == ALLY_ARMADA
					|| map[2 * i + 1][2 * j + 3] == ENEMY_ARMADA;
		}

		if (up) {
			map[2 * i + 1][2 * j] = VALID_FOR_CHAIN;
		}

		if (down) {
			map[2 * i + 1][2 * j + 2] = VALID_FOR_CHAIN;
		}

		if (left) {
			map[2 * i][2 * j + 1] = VALID_FOR_CHAIN;
		}

		if (right) {
			map[2 * i + 2][2 * j + 1] = VALID_FOR_CHAIN;
		}
	}

	public void addEnemyArmada(int i, int j, boolean movable) {
		if (gameObjects[i][j] != null)
			return;
		gameObjects[i][j] = new EnemyArmada(movable, i, j);
		noOfEnemies++;
		map[2 * i + 1][2 * j + 1] = ENEMY_ARMADA;

		if (movable)
			movables.add((Soldier) gameObjects[i][j]);

		boolean up = false;
		boolean down = false;
		boolean right = false;
		boolean left = false;

		map[2 * i + 1][2 * j] = EDGE_OF_LAKE;
		map[2 * i + 1][2 * j + 2] = EDGE_OF_LAKE;
		map[2 * i][2 * j + 1] = EDGE_OF_LAKE;
		map[2 * i + 2][2 * j + 1] = EDGE_OF_LAKE;

		if (i != 0) {
			left = map[2 * i - 1][2 * j + 1] == LAKE || map[2 * i - 1][2 * j + 1] == ALLY_ARMADA
					|| map[2 * i - 1][2 * j + 1] == ENEMY_ARMADA;
		}
		if (i < mapWidth - 1) {
			right = map[2 * i + 3][2 * j + 1] == LAKE || map[2 * i + 3][2 * j + 1] == ALLY_ARMADA
					|| map[2 * i + 3][2 * j + 1] == ENEMY_ARMADA;
		}

		if (j != 0) {
			up = map[2 * i + 1][2 * j - 1] == LAKE || map[2 * i + 1][2 * j - 1] == ALLY_ARMADA
					|| map[2 * i + 1][2 * j - 1] == ENEMY_ARMADA;
		}
		if (j < mapLength - 1) {
			down = map[2 * i + 1][2 * j + 3] == LAKE || map[2 * i + 1][2 * j + 3] == ALLY_ARMADA
					|| map[2 * i + 1][2 * j + 3] == ENEMY_ARMADA;
		}

		if (up) {
			map[2 * i + 1][2 * j] = VALID_FOR_CHAIN;
		}

		if (down) {
			map[2 * i + 1][2 * j + 2] = VALID_FOR_CHAIN;
		}

		if (left) {
			map[2 * i][2 * j + 1] = VALID_FOR_CHAIN;
		}

		if (right) {
			map[2 * i + 2][2 * j + 1] = VALID_FOR_CHAIN;
		}
	}

	public void addForest(int i, int j) {
		if (gameObjects[i][j] != null)
			return;
		gameObjects[i][j] = new Forest(i, j);
		map[2 * i + 1][2 * j + 1] = FOREST;

		map[2 * i + 1][2 * j] = FOREST;
		map[2 * i + 1][2 * j + 2] = FOREST;
		map[2 * i][2 * j + 1] = FOREST;
		map[2 * i + 2][2 * j + 1] = FOREST;

	}

	public void setValues(int width, int height) {
		squareWidth = width / (mapWidth + 4);
		squareHeight = (height - barShift) / (mapLength + 3);

		if (squareWidth < squareHeight)
			squareHeight = squareWidth;
		else {
			squareWidth = squareHeight;
		}
		if (squareWidth / 10 != 0)
			lineWidth = (squareWidth + squareHeight) / 20;
		else
			lineWidth = 1;

		for (WallOrChain w : walls) {
			w.setLineWidthOnGreenSquare(lineWidth / 2);
			w.setSquareHeightOnGreenSquare(squareHeight / 4);
			w.setSquareWidthOnGreenSquare(squareWidth / 4);
			w.lineWidth = lineWidth;
		}

	}

	public void centerTheGame(int width, int height) {
		int gameWidth = getSquareWidth() * (getMapWidth());
		int gameHeight = (getSquareHeight()) * (getMapLength() + 2);

		setInitialXShift((width - gameWidth) / 2);

		if ((height - gameHeight) / 2 > getBarShift()) {
			setInitialYShift((height - gameHeight) / 2);
		} else {
			setInitialYShift(getBarShift());
		}

		setSizesOfObjects();
		rearrangeWalls();
	}
}
