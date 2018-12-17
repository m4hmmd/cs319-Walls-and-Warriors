import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;

public abstract class WallOrChain {

	private static final double MIN_ALPHA = 75;

	int lineWidthOnBar = 4, squareWidthOnBar = 20, squareHeightOnBar = 20;

	int index;
	int health;
	int initialHealth;
	Color c;
	Color originalColor;
	int xInd, yInd, xCoor, yCoor, turn;
	int[] initialXCoors;
	int[] initialYCoors;
	int[] edgesX, edgesY;
	double centerX, centerY;
	boolean visible = false;
	ArrayList<Point> points = new ArrayList<Point>();
	ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
	ArrayList<Rectangle> rectsOnBar = new ArrayList<Rectangle>();
	Rectangle nearestRectToCenter;
	Rectangle wallContainer = new Rectangle(0, 0, 0, 0);
	int lineWidth;
	int mapWidth;
	int mapHeight;
	boolean collapsed = false;
	Area area = null;
	Area areaForSquare = null;

	public WallOrChain(int x_Ind, int y_Ind, int[] xCoors, int[] yCoors, Color c, int index, int initialXShift,
			int initialYShift, int squareHeight, int squareWidth, int mapHeight, int mapWidth) {
		initialHealth = 2000;
		health = initialHealth;
		initialXCoors = new int[xCoors.length];
		for (int i = 0; i < xCoors.length; i++)
			initialXCoors[i] = xCoors[i];

		initialYCoors = new int[yCoors.length];
		for (int i = 0; i < yCoors.length; i++)
			initialYCoors[i] = yCoors[i];

		edgesX = new int[xCoors.length - 1 >= 0 ? xCoors.length - 2 : 0];
		edgesY = new int[xCoors.length - 1 >= 0 ? xCoors.length - 2 : 0];

		double totalX = xCoors[0] / 2.0;
		double totalY = yCoors[0] / 2.0;
		points.add(new Point(xCoors[0], yCoors[0]));
		lineWidth = squareWidth / 10;

		for (int i = 1; i < xCoors.length - 1; i++) {
			totalX += xCoors[i];
			totalY += yCoors[i];
			points.add(new Point(xCoors[i], yCoors[i]));
		}
		totalX += xCoors[xCoors.length - 1] / 2.0;
		totalY += yCoors[yCoors.length - 1] / 2.0;

		points.add(new Point(xCoors[xCoors.length - 1], yCoors[yCoors.length - 1]));

		setEdges();
		centerX = totalX / (xCoors.length - 1);
		centerY = totalY / (yCoors.length - 1);

		xInd = x_Ind;
		yInd = y_Ind;

		xCoor = initialXShift + xInd * squareWidth;
		yCoor = initialYShift + yInd * squareHeight;

		int nextXCoor = xCoor;
		int nextYCoor = yCoor;
		for (int i = 0; i < points.size() - 1; i++) {
			if (points.get(i).x < points.get(i + 1).x) {
				rects.add(new Rectangle(nextXCoor, nextYCoor - lineWidth / 2, squareWidth, lineWidth));
				nextXCoor = nextXCoor + squareWidth;
			} else if (points.get(i).y < points.get(i + 1).y) {
				rects.add(new Rectangle(nextXCoor - lineWidth / 2, nextYCoor, lineWidth, squareHeight));
				nextYCoor = nextYCoor + squareHeight;
			} else if (points.get(i).x > points.get(i + 1).x) {
				rects.add(new Rectangle(nextXCoor - squareWidth, nextYCoor - lineWidth / 2, squareWidth, lineWidth));
				nextXCoor = nextXCoor - squareWidth;
			} else if (points.get(i).y > points.get(i + 1).y) {
				rects.add(new Rectangle(nextXCoor - lineWidth / 2, nextYCoor - squareHeight, lineWidth, squareHeight));
				nextYCoor = nextYCoor - squareHeight;
			}
		}

		nextXCoor = (int) (wallContainer.getCenterX() - (centerX) * squareHeightOnBar);
		nextYCoor = (int) (wallContainer.getCenterY() - (centerY) * squareHeightOnBar);
		for (int i = 0; i < points.size() - 1; i++) {
			if (points.get(i).x < points.get(i + 1).x) {
				rectsOnBar.add(new Rectangle(nextXCoor, nextYCoor - lineWidthOnBar / 2,
						squareWidthOnBar, lineWidthOnBar));
				nextXCoor = nextXCoor + squareWidthOnBar;
			} else if (points.get(i).y < points.get(i + 1).y) {
				rectsOnBar.add(new Rectangle(nextXCoor - lineWidthOnBar / 2, nextYCoor,
						lineWidthOnBar, squareHeightOnBar));
				nextYCoor = nextYCoor + squareHeightOnBar;
			} else if (points.get(i).x > points.get(i + 1).x) {
				rectsOnBar.add(new Rectangle(nextXCoor - squareWidthOnBar,
						nextYCoor - lineWidthOnBar / 2, squareWidthOnBar, lineWidthOnBar));
				nextXCoor = nextXCoor - squareWidthOnBar;
			} else if (points.get(i).y > points.get(i + 1).y) {
				rectsOnBar.add(new Rectangle(nextXCoor - lineWidthOnBar / 2,
						nextYCoor - squareHeightOnBar, lineWidthOnBar, squareHeightOnBar));
				nextYCoor = nextYCoor - squareHeightOnBar;
			}
		}
		this.c = c;
		originalColor = c;
		this.index = index;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;

		nearestRectToCenter = getNearestRectangleToCenter(squareWidth, squareHeight);
	}

	public Rectangle getNearestRectToCenter() {
		return nearestRectToCenter;
	}

	private void setEdges() {
		int currentX = 0;
		int currentY = 0;
		int UP = 0;
		int DOWN = 1;
		int LEFT = 2;
		int RIGHT = 3;

		int[] directions = new int[points.size() - 1];

		for (int i = 0; i < points.size() - 1; i++) {
			if (points.get(i).x > points.get(i + 1).x) {
				directions[i] = LEFT;
			} else if (points.get(i).x < points.get(i + 1).x) {
				directions[i] = RIGHT;
			} else if (points.get(i).y > points.get(i + 1).y) {
				directions[i] = UP;
			} else if (points.get(i).y < points.get(i + 1).y) {
				directions[i] = DOWN;
			}
		}

		for (int i = 0; i < points.size() - 2; i++) {
			if (directions[i] == UP) {
				currentY--;
			} else if (directions[i] == DOWN) {
				currentY++;
			} else if (directions[i] == LEFT) {
				currentX--;
			} else if (directions[i] == RIGHT) {
				currentX++;
			}
			edgesX[i] = currentX;
			edgesY[i] = currentY;
		}
	}

	Color getColor() {
		return c;
	}

	abstract void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth, int shift);

	void turnRight() {
		for (Point p : points) {
			int px = p.x;
			int py = p.y;

			p.x = -py;
			p.y = px;
		}

		turn++;
		turn = turn % 4;

		double centX = centerX;
		double centY = centerY;
		centerX = -centY;
		centerY = centX;

		Point newInd = rotateAroundPoint(new Point(xInd, yInd), new Point((int) centX + xInd, (int) centY + yInd), 1);
		xInd = newInd.x;
		yInd = newInd.y;
		setEdges();
	}

	void turnLeft() {
		for (Point p : points) {
			int px = p.x;
			int py = p.y;

			p.x = py;
			p.y = -px;
		}

		turn--;
		turn = turn % 4;

		double centX = centerX;
		double centY = centerY;
		centerX = centY;
		centerY = -centX;

		Point newInd = rotateAroundPoint(new Point(xInd, yInd), new Point((int) centX + xInd, (int) centY + yInd), 0);
		xInd = newInd.x;
		yInd = newInd.y;
		setEdges();
	}

	public Point rotateAroundPoint(Point p, Point c, int turn) {
		int angle = -90;
		if (turn == 1)
			angle = 90;
		Point newxy = new Point(p.x, p.y);
		p.x -= c.x;
		p.y -= c.y;

		newxy.x = (int) (p.x * cos(angle) - p.y * sin(angle));
		newxy.y = (int) (p.x * sin(angle) + p.y * cos(angle));

		p.x = newxy.x + c.x;
		p.y = newxy.y + c.y;

		return p;
	}

	private double cos(int angle) {
		return Math.cos(Math.toRadians(angle));
	}

	private double sin(int angle) {
		return Math.sin(Math.toRadians(angle));
	}

	void setRectangles() {

		int nextXCoor = (int) (wallContainer.getCenterX() - (centerX) * squareWidthOnBar);
		int nextYCoor = (int) (wallContainer.getCenterY() - (centerY) * squareHeightOnBar);

		int line = (lineWidthOnBar) / 2;

		areaForSquare = new Area();

		if (points.get(0).x < points.get(1).x) { // RIGHT
			rectsOnBar.get(0).setBounds(nextXCoor - line, nextYCoor - line, squareWidthOnBar + 2*line,
					lineWidthOnBar);
			nextXCoor = nextXCoor + squareWidthOnBar;
		} else if (points.get(0).y < points.get(1).y) { // DOWN
			rectsOnBar.get(0).setBounds(nextXCoor - line, nextYCoor - line, lineWidthOnBar,
					squareHeightOnBar + 2*line);
			nextYCoor = nextYCoor + squareHeightOnBar;
		} else if (points.get(0).x > points.get(1).x) { // LEFT
			rectsOnBar.get(0).setBounds(nextXCoor - squareWidthOnBar - line, nextYCoor - line,
					squareWidthOnBar + 2*line, lineWidthOnBar);
			nextXCoor = nextXCoor - squareWidthOnBar;
		} else if (points.get(0).y > points.get(1).y) { // UP
			rectsOnBar.get(0).setBounds(nextXCoor - line, nextYCoor - squareHeightOnBar - line,
					lineWidthOnBar, squareHeightOnBar + 2*line);
			nextYCoor = nextYCoor - squareHeightOnBar;
		}
		areaForSquare.add(new Area(rectsOnBar.get(0)));

		for (int i = 1; i < points.size() - 1; i++) {
			if (points.get(i).x < points.get(i + 1).x) { // RIGHT
				rectsOnBar.get(i).setBounds(nextXCoor + (line), nextYCoor - (line), squareWidthOnBar,
						lineWidthOnBar);
				nextXCoor = nextXCoor + squareWidthOnBar;
			} else if (points.get(i).y < points.get(i + 1).y) { // DOWN
				rectsOnBar.get(i).setBounds(nextXCoor - (line), nextYCoor + (line), lineWidthOnBar,
						squareHeightOnBar);
				nextYCoor = nextYCoor + squareHeightOnBar;
			} else if (points.get(i).x > points.get(i + 1).x) { // LEFT
				rectsOnBar.get(i).setBounds(nextXCoor - squareWidthOnBar - line, nextYCoor - line,
						squareWidthOnBar, lineWidthOnBar);
				nextXCoor = nextXCoor - squareWidthOnBar;
			} else if (points.get(i).y > points.get(i + 1).y) { // UP
				rectsOnBar.get(i).setBounds(nextXCoor - line, nextYCoor - squareHeightOnBar - line,
						lineWidthOnBar, squareHeightOnBar);
				nextYCoor = nextYCoor - squareHeightOnBar;
			}
			areaForSquare.add(new Area(rectsOnBar.get(i)));
		}
	}
	void setThePositionAgain(int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
		if ((xCoor - initialXShift) % squareWidth < squareWidth / 2) {
			xCoor -= (xCoor - initialXShift) % squareWidth;
		} else {
			xCoor += squareWidth - ((xCoor - initialXShift) % squareWidth);
		}
		if ((yCoor - initialYShift) % squareHeight < squareHeight / 2) {
			yCoor -= (yCoor - initialYShift) % squareHeight;
		} else {
			yCoor += squareHeight - ((yCoor - initialYShift) % squareHeight);
		}

		xInd = (xCoor - initialXShift) / squareWidth;
		yInd = (yCoor - initialYShift) / squareHeight;
	}

	void setCoordinates(int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
		xInd = (xCoor - initialXShift) / squareWidth;
		yInd = (yCoor - initialYShift) / squareHeight;
	}

	void setThePositionAgainByIndex(int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
		xCoor = xInd * squareWidth + initialXShift;
		yCoor = yInd * squareHeight + initialYShift;

		if ((xCoor - initialXShift) % squareWidth < squareWidth / 2) {
			xCoor -= (xCoor - initialXShift) % squareWidth;
		} else {
			xCoor += squareWidth - ((xCoor - initialXShift) % squareWidth);
		}
		if ((yCoor - initialYShift) % squareHeight < squareHeight / 2) {
			yCoor -= (yCoor - initialYShift) % squareHeight;
		} else {
			yCoor += squareHeight - ((yCoor - initialYShift) % squareHeight);
		}

	}

	void setTheRectanglePoints(int squareHeight, int squareWidth, int shiftY) {
		int nextXCoor = xCoor;
		int nextYCoor = yCoor + shiftY;

		int line = (lineWidth) / 2;

		area = new Area();
		for (int i = 0; i < points.size() - 2; i++) {
			if (points.get(i).x < points.get(i + 1).x) { // RIGHT
				rects.get(i).setBounds(nextXCoor + (line), nextYCoor - (line), squareWidth, lineWidth);
				nextXCoor = nextXCoor + squareWidth;
			} else if (points.get(i).y < points.get(i + 1).y) { // DOWN
				rects.get(i).setBounds(nextXCoor - (line), nextYCoor + (line), lineWidth, squareHeight);
				nextYCoor = nextYCoor + squareHeight;
			} else if (points.get(i).x > points.get(i + 1).x) { // LEFT
				rects.get(i).setBounds(nextXCoor - squareWidth - line, nextYCoor - line, squareWidth, lineWidth);
				nextXCoor = nextXCoor - squareWidth;
			} else if (points.get(i).y > points.get(i + 1).y) { // UP
				rects.get(i).setBounds(nextXCoor - line, nextYCoor - squareHeight - line, lineWidth, squareHeight);
				nextYCoor = nextYCoor - squareHeight;
			}
			area.add(new Area(rects.get(i)));
		}

		if (points.get(points.size() - 2).x < points.get(points.size() - 1).x) {
			rects.get(points.size() - 2).setBounds(nextXCoor + lineWidth / 2, nextYCoor - lineWidth / 2,
					squareWidth - lineWidth, lineWidth);
			nextXCoor = nextXCoor + squareWidth;
		} else if (points.get(points.size() - 2).y < points.get(points.size() - 1).y) {
			rects.get(points.size() - 2).setBounds(nextXCoor - lineWidth / 2, nextYCoor + lineWidth / 2, lineWidth,
					squareHeight - lineWidth);
			nextYCoor = nextYCoor + squareHeight;
		} else if (points.get(points.size() - 2).x > points.get(points.size() - 1).x) {
			rects.get(points.size() - 2).setBounds(nextXCoor - squareWidth + lineWidth / 2, nextYCoor - lineWidth / 2,
					squareWidth - lineWidth, lineWidth);
			nextXCoor = nextXCoor - squareWidth;
		} else if (points.get(points.size() - 2).y > points.get(points.size() - 1).y) {
			rects.get(points.size() - 2).setBounds(nextXCoor - lineWidth / 2, nextYCoor - squareHeight + lineWidth / 2,
					lineWidth, squareHeight - lineWidth);
			nextYCoor = nextYCoor - squareHeight;
		}
		area.add(new Area(rects.get(rects.size() - 1)));
	}

	void remove() {
		visible = false;
		setColorToOriginal();
		updateColor();
		setIndexes(-10, -10);
	}

	void appear() {
		visible = true;
	}

	boolean contains(int x, int y) {
		for (int i = 0; i < rects.size(); i++) {
			if (rects.get(i).contains(x, y)) {
				return true;
			}
		}
		return false;
	}

	boolean containsLine(Soldier s, int dir) {
		Point p1 = null;
		Point p2 = null;

		if (dir == Model.UP) {
			p1 = new Point(s.getX(), s.getY());
			p2 = new Point(s.getX() + 1, s.getY());
		} else if (dir == Model.DOWN) {
			p1 = new Point(s.getX(), s.getY() + 1);
			p2 = new Point(s.getX() + 1, s.getY() + 1);
		} else if (dir == Model.LEFT) {
			p1 = new Point(s.getX(), s.getY());
			p2 = new Point(s.getX(), s.getY() + 1);
		} else if (dir == Model.RIGHT) {
			p1 = new Point(s.getX() + 1, s.getY());
			p2 = new Point(s.getX() + 1, s.getY() + 1);
		}

		for (int i = 0; i < points.size() - 1; i++) {
			Point thisLine1 = new Point(this.xInd + points.get(i).x, this.yInd + points.get(i).y);
			Point thisLine2 = new Point(this.xInd + points.get(i + 1).x, this.yInd + points.get(i + 1).y);
			if (p1.equals(thisLine1) && p2.equals(thisLine2) || p2.equals(thisLine1) && p1.equals(thisLine2)) {
				return true;
			}
		}
		return false;
	}

	void setWallContainer(Rectangle r) {
		wallContainer = r;
	}

	void reset(int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
		collapsed = false;
		health = initialHealth;
		lineWidth = squareWidth / 10;

		setTurn(0);
		remove();

		setRectangles();
		setTheRectanglePoints(squareHeight, squareWidth, 0);
	}

	public int getLineWidthOnBar() {
		return lineWidthOnBar;
	}

	public void setLineWidthOnBar(int lineWidthOnBar) {
		if (lineWidthOnBar > 0)
			this.lineWidthOnBar = lineWidthOnBar;
		else
			this.lineWidthOnBar = 1;
	}

	public int getSquareWidthOnBar() {
		return squareWidthOnBar;
	}

	public void setSquareWidthOnBar(int squareWidthOnBar) {
		this.squareWidthOnBar = squareWidthOnBar;
	}

	public int getSquareHeightOnBar() {
		return squareHeightOnBar;
	}

	public void setSquareHeightOnBar(int squareHeightOnBar) {
		this.squareHeightOnBar = squareHeightOnBar;
	}

	public abstract int getWholeMapIndex();

	public void setColor(Color color) {
		c = color;
		updateColor();
	}

	public void setColorToOriginal() {
		c = originalColor;
		updateColor();
	}

	public void goLeft() {
		if (xInd > -2)
			xInd--;
	}

	public void goRight() {
		if (xInd < mapWidth + 1)
			xInd++;
	}

	public void goUp() {
		if (yInd > -1)
			yInd--;
	}

	public void goDown() {
		if (yInd < mapHeight + 1)
			yInd++;
	}

	public void setTurn(int turnStart) {
		for (; turn != turnStart;)
			turnLeft();
	}

	public void setIndexes(int x, int y) {
		xInd = x;
		yInd = y;
	}

	public int damaged(int damageAmount) {
		health -= damageAmount;
		return health;
	}

	public void updateColor() {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int alpha = (int) (MIN_ALPHA + (255 - MIN_ALPHA) * (health * 1.0 / initialHealth));
		if (alpha > 0)
			c = new Color(r, g, b, alpha);
	}

	private Rectangle getNearestRectangleToCenter(int squareWidth, int squareHeight) {
		double min = calculateDistanceBetweenPoints(xCoor + centerX * squareWidth, yCoor + centerY * squareHeight,
				rects.get(0).getCenterX(), rects.get(0).getCenterY());
		Rectangle nearest = rects.get(0);
		for (int i = 1; i < rects.size(); i++) {
			double length = calculateDistanceBetweenPoints(xCoor + centerX * squareWidth,
					yCoor + centerY * squareHeight, rects.get(i).getCenterX(), rects.get(i).getCenterY());
			if (min > length) {
				min = length;
				nearest = rects.get(i);
			}
		}
		return nearest;
	}

	public double calculateDistanceBetweenPoints(double x1, double y1, double x2, double y2) {
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

}
