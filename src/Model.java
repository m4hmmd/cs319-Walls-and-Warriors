import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;
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
	public static final int WALL = 6;
	public static final int CHAIN = 7;
	public static final int VALID_FOR_WALL = 8;
	public static final int VALID_FOR_CHAIN = 9;
	public static final int ALLY_ARMADA = 10;
	public static final int ENEMY_ARMADA = 11;
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
		soldiers = new ArrayList<>();
		movables = new ArrayList<>();

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

	void reset() { ///////
		map = new int[2 * mapWidth + 1][2 * mapLength + 1];
		soldiers.clear();
		gameFinished = false;
		noOfEnemies = 0;

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

		resetMovableSoldiers();
		stopTimers();
		movables.clear();

		// invalidate the line segment occupied by the castle
		int leftOne = (castle.x == castle.x1) ? castle.x : ((castle.x > castle.x1) ? castle.x1 : castle.x);
		int topOne = (castle.y == castle.y1) ? castle.y : ((castle.y > castle.y1) ? castle.y1 : castle.y);
		if (castle.x == castle.x1) {
			map[leftOne * 2 + 1][topOne * 2 + 2] = CASTLE;
		} else {
			map[leftOne * 2 + 2][topOne * 2 + 1] = CASTLE;
		}

		for (WallOrChain w : walls) {
			w.reset(initialXShift, initialYShift, squareHeight, squareWidth, lineWidth);
		}
		resetWholeMapWithRespectToMap();
	}

	private void resetMovableSoldiers() {
		for (Soldier s : movables) {
			if (s.isArmada() && !s.isInInitialPosition()) {
				gameObjects[s.getX()][s.getY()] = new Lake(s.getX(), s.getY());
				s.sendToInitialPosition();
				gameObjects[s.getX()][s.getY()] = s;
			} else {
				gameObjects[s.getX()][s.getY()] = null;
				s.sendToInitialPosition();
				gameObjects[s.getX()][s.getY()] = s;
			}
			s.nextDir = 1;
			s.nextPos = 0;
		}
	}

	public void addSoldier(boolean ally, boolean movable, int i, int j, ArrayList<Integer> route) {
		Soldier s = null;
		if (ally)
			s = new Ally(movable, i, j, route);
		else {
			s = new Enemy(movable, i, j, route);
			noOfEnemies++;
		}
		if (movable) {
			movables.add(s);
			s.addActionListener(new MovableTimerActionListener(s, route));
		}
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
				} else if (gameObjects[i][j] instanceof AllyArmada) {
					Soldier s = (Soldier) (gameObjects[i][j]);
					gameObjects[i][j] = null;
					addAllyArmada(i, j, s.movable, s.route);
				} else if (gameObjects[i][j] instanceof EnemyArmada) {
					Soldier s = (Soldier) (gameObjects[i][j]);
					gameObjects[i][j] = null;
					addEnemyArmada(i, j, s.movable, s.route);
				} else if (gameObjects[i][j] instanceof Soldier) {
					Soldier s = (Soldier) (gameObjects[i][j]);
					gameObjects[i][j] = null;
					addSoldier(s instanceof Ally, s.movable, i, j, s.route);
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
			if (map[(w.xInd + w.edgesX[i]) * 2][(w.yInd + w.edgesY[i]) * 2] == WALL
					|| map[(w.xInd + w.edgesX[i]) * 2][(w.yInd + w.edgesY[i]) * 2] == CHAIN) {
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
		for (int i = 0; i < w.edgesX.length; i++) {
			if (map[(indexX + w.edgesX[i]) * 2][(indexY + w.edgesY[i]) * 2] == WALL
					|| map[(w.xInd + w.edgesX[i]) * 2][(w.yInd + w.edgesY[i]) * 2] == CHAIN) {
				return false;
			}
		}
		return true;
	}

	void addToLines(WallOrChain w) {
		int index = w.getWholeMapIndex();
		for (int i = 0; i < w.points.size() - 1; i++) {
			int leftOne = (w.points.get(i).x == w.points.get(i + 1).x) ? w.points.get(i).x
					: ((w.points.get(i).x > w.points.get(i + 1).x) ? w.points.get(i + 1).x : w.points.get(i).x);
			int topOne = (w.points.get(i).y == w.points.get(i + 1).y) ? w.points.get(i).y
					: ((w.points.get(i).y > w.points.get(i + 1).y) ? w.points.get(i + 1).y : w.points.get(i).y);
			if (w.points.get(i).x == w.points.get(i + 1).x) {
				map[(w.xInd + leftOne) * 2][(w.yInd + topOne) * 2 + 1] = index;
			} else {
				map[(w.xInd + leftOne) * 2 + 1][(w.yInd + topOne) * 2] = index;
			}
		}

		for (int i = 0; i < w.edgesX.length; i++) {
			map[(w.xInd + w.edgesX[i]) * 2][(w.yInd + w.edgesY[i]) * 2] = index;
		}
	}

	public boolean isGameFinished() {

		ArrayList<Point> visited = new ArrayList<Point>();
		int numberOfEnemies = 0;

		for (int i = 1; i < map.length; i = i + 2) {
			if (map[i][0] != WALL && map[i][0] != CHAIN)
				numberOfEnemies += numberOfEnemiesOutside(i, 1, visited);
		}

		for (int i = 1; i < map.length; i = i + 2) {
			if (map[i][map[0].length - 1] != WALL && map[i][map[0].length - 1] != CHAIN)
				numberOfEnemies += numberOfEnemiesOutside(i, map[0].length - 2, visited);
		}

		for (int j = 1; j < map[0].length; j = j + 2) {
			if (map[0][j] != WALL && map[0][j] != CHAIN)
				numberOfEnemies += numberOfEnemiesOutside(1, j, visited);
		}

		for (int j = 1; j < map[0].length; j = j + 2) {
			if (map[map.length - 1][j] != WALL && map[map.length - 1][j] != CHAIN)
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
		if (map[i][j] == ALLY || map[i][j] == ALLY_ARMADA || map[i][j] == CASTLE)
			return -80;

		visited.add(new Point(i, j));
		int cUp = 0;
		int cDown = 0;
		int cRight = 0;
		int cLeft = 0;

		if (map[i][j + 1] != WALL && map[i][j + 1] != CHAIN)
			cUp = numberOfEnemiesOutside(i, j + 2, visited);
		if (map[i][j - 1] != WALL && map[i][j - 1] != CHAIN)
			cDown = numberOfEnemiesOutside(i, j - 2, visited);
		if (map[i + 1][j] != WALL && map[i + 1][j] != CHAIN)
			cRight = numberOfEnemiesOutside(i + 2, j, visited);
		if (map[i - 1][j] != WALL && map[i - 1][j] != CHAIN)
			cLeft = numberOfEnemiesOutside(i - 2, j, visited);

		int thisCell = map[i][j] == ENEMY || map[i][j] == ENEMY_ARMADA ? 1 : 0;

		if (cUp == -80 || cDown == -80 || cRight == -80 || cLeft == -80)
			return -80;
		return thisCell + cUp + cDown + cRight + cLeft;
	}

	public void printWholeMap() {

		int[][] array = transpose(map);

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
			if (p == UP) {
				currentY--;
			} else if (p == DOWN) {
				currentY++;
			} else if (p == LEFT) {
				currentX--;
			} else if (p == RIGHT) {
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
			left = map[2 * i - 1][2 * j + 1] == LAKE || map[2 * i - 1][2 * j + 1] == ENEMY_ARMADA
					|| map[2 * i - 1][2 * j + 1] == ALLY_ARMADA;
		}
		if (i < mapWidth - 1) {
			right = map[2 * i + 3][2 * j + 1] == LAKE || map[2 * i + 3][2 * j + 1] == ENEMY_ARMADA
					|| map[2 * i + 3][2 * j + 1] == ALLY_ARMADA;
		}

		if (j != 0) {
			up = map[2 * i + 1][2 * j - 1] == LAKE || map[2 * i + 1][2 * j - 1] == ENEMY_ARMADA
					|| map[2 * i + 1][2 * j - 1] == ALLY_ARMADA;
		}
		if (j < mapLength - 1) {
			down = map[2 * i + 1][2 * j + 3] == LAKE || map[2 * i + 1][2 * j + 3] == ENEMY_ARMADA
					|| map[2 * i + 1][2 * j + 3] == ALLY_ARMADA;
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

	public void addAllyArmada(int i, int j, boolean movable, ArrayList<Integer> route) {
		if (gameObjects[i][j] != null)
			return;

		gameObjects[i][j] = new AllyArmada(movable, i, j, route);
		map[2 * i + 1][2 * j + 1] = ALLY_ARMADA;

		if (movable) {
			movables.add((Soldier) gameObjects[i][j]);
			((Soldier) gameObjects[i][j])
					.addActionListener(new MovableTimerActionListener((Soldier) gameObjects[i][j], route));
		}
		soldiers.add((Soldier) gameObjects[i][j]);

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

	public void addEnemyArmada(int i, int j, boolean movable, ArrayList<Integer> route) {
		if (gameObjects[i][j] != null)
			return;
		gameObjects[i][j] = new EnemyArmada(movable, i, j, route);
		noOfEnemies++;
		map[2 * i + 1][2 * j + 1] = ENEMY_ARMADA;

		if (movable) {
			movables.add((Soldier) gameObjects[i][j]);
			((Soldier) gameObjects[i][j])
					.addActionListener(new MovableTimerActionListener((Soldier) gameObjects[i][j], route));
		}
		soldiers.add((Soldier) gameObjects[i][j]);

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

		boolean up = false;
		boolean down = false;
		boolean right = false;
		boolean left = false;

		if (map[2 * i + 1][2 * j] != EDGE_OF_LAKE)
			map[2 * i + 1][2 * j] = VALID_FOR_WALL;
		if (map[2 * i + 1][2 * j + 2] != EDGE_OF_LAKE)
			map[2 * i + 1][2 * j + 2] = VALID_FOR_WALL;
		if (map[2 * i][2 * j + 1] != EDGE_OF_LAKE)
			map[2 * i][2 * j + 1] = VALID_FOR_WALL;
		if (map[2 * i + 2][2 * j + 1] != EDGE_OF_LAKE)
			map[2 * i + 2][2 * j + 1] = VALID_FOR_WALL;

		if (i != 0) {
			left = map[2 * i - 1][2 * j + 1] == FOREST;
		}
		if (i < mapWidth - 1) {
			right = map[2 * i + 3][2 * j + 1] == FOREST;
		}

		if (j != 0) {
			up = map[2 * i + 1][2 * j - 1] == FOREST;
		}
		if (j < mapLength - 1) {
			down = map[2 * i + 1][2 * j + 3] == FOREST;
		}

		if (up) {
			map[2 * i + 1][2 * j] = FOREST;
		}

		if (down) {
			map[2 * i + 1][2 * j + 2] = FOREST;
		}

		if (left) {
			map[2 * i][2 * j + 1] = FOREST;
		}

		if (right) {
			map[2 * i + 2][2 * j + 1] = FOREST;
		}
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
			lineWidth = (squareWidth + squareHeight) / 10;
		else
			lineWidth = 1;

		if (lineWidth % 2 != 0)
			lineWidth++;

		for (WallOrChain w : walls) {
			w.setLineWidthOnBar((lineWidth / 2) % 2 == 0 ? lineWidth / 2 : lineWidth / 2 - 1);
			w.setSquareHeightOnBar(squareHeight / 4);
			w.setSquareWidthOnBar(squareWidth / 4);
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

	public void stopTimers() {
		for (Soldier m : movables) {
			if (m.t.isRunning())
				m.stop();
		}
	}

	public void startTimers() {
		for (Soldier m : movables) {
			if (isAvailable(m, m.nextDir == 1 ? m.route.get(m.nextPos)
					: (1 - m.route.get(m.nextPos)) + 4 * (m.route.get(m.nextPos) / 2)))
				m.start();
		}
	}

	public void rearrangeTimers() {
		for (Soldier m : movables) {
			if (thereIsWallNearBy(m) != -1) {
				if (m.t.isRunning())
					m.stop();
				continue;
			}
			if (isAvailable(m, m.nextDir == 1 ? m.route.get(m.nextPos)
					: (1 - m.route.get(m.nextPos)) + 4 * (m.route.get(m.nextPos) / 2))) {
				if (!m.t.isRunning())
					m.start();
			} else {
				if (m.t.isRunning())
					m.stop();
			}
		}
	}

	public void startTimer(Soldier s) {
		s.start();
	}

	public void stopTimer(Soldier s) {
		s.stop();
	}

	private boolean canMove(Soldier s) {
		boolean up = isAvailable(s, Model.UP);
		boolean down = isAvailable(s, Model.DOWN);
		boolean left = isAvailable(s, Model.LEFT);
		boolean right = isAvailable(s, Model.RIGHT);

		return up || down || left || right;
	}

	private void move(Soldier s, int dir) {
		if (s instanceof AllyArmada || s instanceof EnemyArmada) {
			gameObjects[s.getX()][s.getY()] = new Lake(s.getX(), s.getY());
			map[2 * s.getX() + 1][2 * s.getY() + 1] = LAKE;
		} else {
			gameObjects[s.getX()][s.getY()] = null;
			map[2 * s.getX() + 1][2 * s.getY() + 1] = EMPTY;
		}

		if (dir == UP)
			s.y--;
		else if (dir == DOWN)
			s.y++;
		else if (dir == LEFT)
			s.x--;
		else if (dir == RIGHT)
			s.x++;
		gameObjects[s.getX()][s.getY()] = s;
		map[2 * s.getX() + 1][2 * s.getY() + 1] = s.getWholeMapIndex();
	}

	private boolean isAvailable(Soldier s, int dir) {
		int nextX = s.getX();
		int nextY = s.getY();
		int mapX = 0;
		int mapY = 0;
		if (dir == LEFT) {
			mapX = 2 * nextX;
			mapY = 2 * nextY + 1;
			nextX--;
		}
		if (dir == RIGHT) {
			mapX = 2 * nextX + 2;
			mapY = 2 * nextY + 1;
			nextX++;
		}
		if (dir == UP) {
			mapX = 2 * nextX + 1;
			mapY = 2 * nextY;
			nextY--;
		}
		if (dir == DOWN) {
			mapX = 2 * nextX + 1;
			mapY = 2 * nextY + 2;
			nextY++;
		}

		if (!outOfScreen(nextX, nextY) && map[mapX][mapY] != WALL && map[mapX][mapY] != CHAIN) {
			if (s instanceof AllyArmada || s instanceof EnemyArmada)
				return gameObjects[nextX][nextY] instanceof Lake;
			else
				return gameObjects[nextX][nextY] == null;
		}
		return false;
	}

	private boolean outOfScreen(int x, int y) {
		if (x == 0 && y == 0)
			return true;
		if (x == mapWidth - 1 && y == 0)
			return true;
		if (x == 0 && y == mapLength - 1)
			return true;
		if (x == mapWidth - 1 && y == mapLength - 1)
			return true;
		return !(x >= 0 && x < mapWidth && y >= 0 && y < mapLength);
	}

	private boolean outOfScreenMap(int x, int y) {
		return !(x >= 0 && x < map.length && y >= 0 && y < map[0].length);
	}

	class MovableTimerActionListener implements ActionListener {
		Soldier s;
		ArrayList<Integer> route;
		int pos = 0, dir = 1;
		int xShift = 0;
		int yShift = 0;
		boolean move = false;
		boolean first = true;
		int prev = 0;
		int i = 0;

		public MovableTimerActionListener(Soldier s, ArrayList<Integer> route) {
			this.s = s;
			this.route = route;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			// if dir == 1 => route[pos], if dir == 0 => (1-route[pos]) + 4 * (route[pos]/2)
			int w = s.width;
			int h = s.height;
			int period = 70;

			if (move) {
				int direction = dir == 1 ? route.get(pos) : (1 - route.get(pos)) + 4 * (route.get(pos) / 2);
				move = false;

				if (direction == Model.UP) {
					yShift += period;
				}
				if (direction == Model.DOWN) {
					yShift -= period;
				}
				if (direction == Model.LEFT) {
					xShift += period;
				}
				if (direction == Model.RIGHT) {
					xShift -= period;
				}
				if (dir == 1) {
					if (s.nextPos == route.size() - 1) {
						s.nextDir = 1 - s.nextDir;
					} else {
						s.nextPos = s.nextPos + 1;
					}
				} else {
					if (pos == 0) {
						s.nextDir = 1 - s.nextDir;
					} else {
						s.nextPos = s.nextPos - 1;
					}
				}
				move(s, dir == 1 ? route.get(pos) : (1 - route.get(pos)) + 4 * (route.get(pos) / 2));
			}

			i++;
			i = i % period;

			if (i == 0) {
				dir = s.nextDir;
				pos = s.nextPos;
			}

			int direction = dir == 1 ? route.get(pos) : (1 - route.get(pos)) + 4 * (route.get(pos) / 2);
			if (direction == Model.UP) {
				xShift = 0;
				yShift--;
			} else if (direction == Model.DOWN) {
				xShift = 0;
				yShift++;
			} else if (direction == Model.LEFT) {
				xShift--;
				yShift = 0;
			} else if (direction == Model.RIGHT) {
				xShift++;
				yShift = 0;
			}

			s.setXShift((int) (squareWidth * xShift / ((double) period)));
			s.setYShift((int) (squareHeight * yShift / ((double) period)));

			if (direction == Model.UP) {
				if (s.yShift < -(squareHeight / 2 + h / 2)) {
					if (prev != 1) {
						move = true;
						map[s.getX() * 2 + 1][s.getY() * 2] = s.isArmada() ? VALID_FOR_CHAIN : VALID_FOR_WALL;
					}
					first = prev != 1;
					prev = 1;
				} else if (s.yShift < -(squareHeight / 2 - h / 2)) {
					map[s.getX() * 2 + 1][s.getY() * 2] = s.getWholeMapIndex();
					first = prev != 2;
					prev = 2;
				} else {
					first = prev != 3;
					prev = 3;
				}
			} else if (direction == Model.DOWN) {
				if (s.yShift > (squareHeight / 2 + h / 2)) {
					if (prev != 4) {
						move = true;
						map[s.getX() * 2 + 1][s.getY() * 2 + 2] = s.isArmada() ? VALID_FOR_CHAIN : VALID_FOR_WALL;
					}
					first = prev != 4;
					prev = 4;
				} else if (s.yShift > (squareHeight / 2 - h / 2)) {
					first = prev != 5;
					map[s.getX() * 2 + 1][s.getY() * 2 + 2] = s.getWholeMapIndex();
					prev = 5;
				} else {
					first = prev != 6;
					prev = 6;
				}
			} else if (direction == Model.LEFT) {
				if (s.xShift < -(squareWidth / 2 + w / 2)) {
					if (prev != 7) {
						move = true;
						map[s.getX() * 2][s.getY() * 2 + 1] = s.isArmada() ? VALID_FOR_CHAIN : VALID_FOR_WALL;
					}
					first = prev != 7;
					prev = 7;
				} else if (s.xShift < -(squareWidth / 2 - w / 2)) {
					first = prev != 8;
					map[s.getX() * 2][s.getY() * 2 + 1] = s.getWholeMapIndex();
					prev = 8;
				} else {
					first = prev != 9;
					prev = 9;
				}
			} else if (direction == Model.RIGHT) {
				if (s.xShift > (squareWidth / 2 + w / 2)) {
					if (prev != 10) {
						move = true;
						map[s.getX() * 2 + 2][s.getY() * 2 + 1] = s.isArmada() ? VALID_FOR_CHAIN : VALID_FOR_WALL;
					}
					first = prev != 10;
					prev = 10;
				} else if (s.xShift > (squareWidth / 2 - w / 2)) {
					map[s.getX() * 2 + 2][s.getY() * 2 + 1] = s.getWholeMapIndex();
					first = prev != 11;
					prev = 11;
				} else {
					first = prev != 12;
					prev = 12;
				}
			}
		}
	}

	public int update() {
		int result = updateHealthes();
		rearrangeTimers();
		return result;
	}

	private int updateHealthes() {
		for (Soldier s : soldiers) {
			if (s instanceof Enemy) {
				int dir = thereIsWallNearBy(s);
				if (dir != -1) {
					WallOrChain w = wallNearBy(s, dir);
					int health = w.damaged(s.power);

					if (health <= 0) {
						w.collapsed = true;
						removeFromLines(w);
						w.remove();
						return -1;
					}
					if (Math.abs(4 * health - w.initialHealth ) < 5 && !w.messageShown) {
						w.messageShow();
						return 1;
					} else {
						w.updateColor();
					}

				}
			}
		}
		return 0;
	}

	private WallOrChain wallNearBy(Soldier s, int dir) {
		for (WallOrChain w : walls) {
			if (isNextTo(w, s, dir))
				return w;
		}
		return null;
	}

	private boolean isNextTo(WallOrChain w, Soldier s, int dir) {
		if (w.visible) {
			if (w.containsLine(s, dir))
				return true;
		}
		return false;
	}

	private int thereIsWallNearBy(Soldier s) {
		int mapX = s.getX() * 2 + 1;
		int mapY = s.getY() * 2 + 1;

		if (!outOfScreenMap(mapX + 1, mapY) && map[mapX + 1][mapY] == WALL)
			return Model.RIGHT;
		if (!outOfScreenMap(mapX - 1, mapY) && map[mapX - 1][mapY] == WALL)
			return Model.LEFT;
		if (!outOfScreenMap(mapX, mapY + 1) && map[mapX][mapY + 1] == WALL)
			return Model.DOWN;
		if (!outOfScreenMap(mapX, mapY - 1) && map[mapX][mapY - 1] == WALL)
			return Model.UP;
		return -1;
	}

	public void pause() {
		stopTimers();
	}

	public void resume() {
		startTimers();
	}
}
