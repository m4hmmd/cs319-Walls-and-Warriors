import java.awt.*;
import java.awt.geom.AffineTransform;

public class Wall extends WallOrChain {

	public Wall(int x_Ind, int y_Ind, int[] xCoors, int[] yCoors, Color c, int index, int initialXShift,
			int initialYShift, int squareHeight, int squareWidth, int mapHeight, int mapWidth) {
		super(x_Ind, y_Ind, xCoors, yCoors, c, index, initialXShift, initialYShift, squareHeight, squareWidth,
				mapHeight, mapWidth);
	}

	void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth, int shiftY) {
		drawWallOption(g, initialXShift, initialYShift, squareHeight, squareWidth);
		setTheRectanglePoints(squareHeight, squareWidth, shiftY);
		g.setColor(getColor());

		if (visible) {

			Graphics2D g2d = (Graphics2D) g;
			for (Rectangle r : rects) {
				if (r.width < r.height) {
					AffineTransform backup = g2d.getTransform();
					AffineTransform trans = new AffineTransform();
					trans.rotate(Math.PI / 2, r.x + lineWidth / 2, r.y + lineWidth / 2);
					g2d.transform(trans);
					g2d.drawImage(wallLine, r.x, r.y, r.height, r.width, null);

					g2d.setTransform(backup); // restore previous transform
				} else
					g.drawImage(wallLine, r.x, r.y, r.width, r.height, null);
			}
			for (int i = 0; i < edgesX.length; i++) {
				int xEdgeCoor = xCoor + squareWidth * (edgesX[i]) - lineWidth / 2;
				int yEdgeCoor = yCoor + shiftY + squareHeight * (edgesY[i]) - lineWidth / 2;
				AffineTransform backup = g2d.getTransform();
				AffineTransform trans = new AffineTransform();
				int dir = getDirectionOfTheEdge(i);
				if (dir == 0) {
					trans.rotate(dir * Math.PI / 2, xEdgeCoor + lineWidth / 2, yEdgeCoor + lineWidth / 2);
				} else if (dir == 1) {
					trans.rotate(-dir * Math.PI / 2, xEdgeCoor + lineWidth / 2, yEdgeCoor + lineWidth / 2);
				} else if (dir == 2) {
					trans.rotate(-dir * Math.PI / 2, xEdgeCoor + lineWidth / 2, yEdgeCoor + lineWidth / 2);
				} else if (dir == 3) {
					trans.rotate(-dir * Math.PI / 2, xEdgeCoor + lineWidth / 2, yEdgeCoor + lineWidth / 2);
				}

				g2d.transform(trans);
				g.drawImage(wallEdge, xEdgeCoor, yEdgeCoor, lineWidth, lineWidth, null);

				g2d.setTransform(backup);
			}

			int healthWidth = squareWidth / 3;
			int healthHeight = squareWidth / 8;
			Rectangle nearestToCenter = getNearestRectToCenter();
			int CoorX = (int) nearestToCenter.getCenterX();
			int CoorY = (int) nearestToCenter.getCenterY();

			g.setColor(Color.GREEN);
			g.drawRect(CoorX - healthWidth / 2, CoorY - healthHeight / 2 - lineWidth, healthWidth, healthHeight);
			int r = (int) (510 * (1 - health * 1.0 / initialHealth));
			int gr = (int) (510 * (health * 1.0 / initialHealth));
			g.setColor(new Color(r >= 255 ? 255 : r, gr >= 255 ? 255 : gr, 0));
			g.fillRect(CoorX - healthWidth / 2, CoorY - healthHeight / 2 - lineWidth,
					(int) (healthWidth * (health * 1.0 / initialHealth)), healthHeight);
		}
		Graphics2D g2d = (Graphics2D) g;
		for (Rectangle r : rectsOnBar) {
			if (r.width < r.height) {
				AffineTransform backup = g2d.getTransform();
				AffineTransform trans = new AffineTransform();
				trans.rotate(Math.PI / 2, r.x + lineWidthOnBar / 2, r.y + lineWidthOnBar / 2);
				g2d.transform(trans);
				g2d.drawImage(wallLine, r.x, r.y, r.height, r.width, null);

				g2d.setTransform(backup); // restore previous transform
			} else
				g.drawImage(wallLine, r.x, r.y, r.width, r.height, null);
		}
		
		for (int i = 0; i < edgesX.length; i++) {
			int xEdgeCoor = (int) (wallContainer.getCenterX() - (centerX) * squareWidthOnBar)
					+ squareWidthOnBar * (edgesX[i]) - lineWidthOnBar / 2;
			int yEdgeCoor = (int) (wallContainer.getCenterY() - (centerY) * squareHeightOnBar)
					+ squareHeightOnBar * (edgesY[i]) - lineWidthOnBar / 2;
			AffineTransform backup = g2d.getTransform();
			AffineTransform trans = new AffineTransform();
			int dir = getDirectionOfTheEdge(i);
			if (dir == 0) {
				trans.rotate(dir * Math.PI / 2, xEdgeCoor + lineWidthOnBar / 2, yEdgeCoor + lineWidthOnBar / 2);
			} else if (dir == 1) {
				trans.rotate(-dir * Math.PI / 2, xEdgeCoor + lineWidthOnBar / 2, yEdgeCoor + lineWidthOnBar / 2);
			} else if (dir == 2) {
				trans.rotate(-dir * Math.PI / 2, xEdgeCoor + lineWidthOnBar / 2, yEdgeCoor + lineWidthOnBar / 2);
			} else if (dir == 3) {
				trans.rotate(-dir * Math.PI / 2, xEdgeCoor + lineWidthOnBar / 2, yEdgeCoor + lineWidthOnBar / 2);
			}

			g2d.transform(trans);
			g.drawImage(wallEdge, xEdgeCoor, yEdgeCoor, lineWidthOnBar, lineWidthOnBar, null);

			g2d.setTransform(backup);
		}

		// g2d.setColor(Color.gray.brighter().brighter().brighter());
		// g2d.fill(areaForSquare);

		if (collapsed) {
			g.setColor(Color.GRAY);
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(2));
			g2.drawLine(wallContainer.x, wallContainer.y, wallContainer.x + wallContainer.width,
					wallContainer.y + wallContainer.height);
			g2.drawLine(wallContainer.x + wallContainer.width, wallContainer.y, wallContainer.x,
					wallContainer.y + wallContainer.height);
			g2.setStroke(new BasicStroke(1));
		}
	}

	private int getDirectionOfTheEdge(int i) {
		int dir1 = getDirectionOfTheWall(i);
		int dir2 = getDirectionOfTheWall(i + 1);

		if (dir1 == Model.UP) {
			if (dir2 == Model.LEFT) {
				return 2;
			}
			if (dir2 == Model.RIGHT) {
				return 3;
			}
		}
		if (dir1 == Model.DOWN) {
			if (dir2 == Model.LEFT) {
				return 1;
			}
			if (dir2 == Model.RIGHT) {
				return 0;
			}
		}
		if (dir1 == Model.RIGHT) {
			if (dir2 == Model.UP) {
				return 1;
			}
			if (dir2 == Model.DOWN) {
				return 2;
			}
		}
		if (dir1 == Model.LEFT) {
			if (dir2 == Model.UP) {
				return 0;
			}
			if (dir2 == Model.DOWN) {
				return 3;
			}
		}
		return 0;
	}

	private int getDirectionOfTheWall(int i) {
		if (points.get(i).x < points.get(i + 1).x) { // Right
			return Model.RIGHT;
		} else if (points.get(i).y < points.get(i + 1).y) { // Down
			return Model.DOWN;
		} else if (points.get(i).x > points.get(i + 1).x) { // Left
			return Model.LEFT;
		} else if (points.get(i).y > points.get(i + 1).y) { // Up
			return Model.UP;
		}
		return 0;
	}
	
	private void drawWallOption(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
		if (collapsed) {
			g.setColor(Color.RED.brighter());
		} else if (!visible)
			g.setColor(Color.GRAY.brighter());
		else
			g.setColor(Color.GRAY.darker());
		g.fillRect(wallContainer.x, wallContainer.y, wallContainer.width, wallContainer.height);

		g.setColor(Color.GRAY);
		g.drawRect(wallContainer.x, wallContainer.y, wallContainer.width, wallContainer.height);

		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, squareHeight / 6));

		g.drawString("" + (index + 1), wallContainer.x + squareWidth / 14, wallContainer.y + squareHeight / 6);

	}

	public int getWholeMapIndex() {
		return Model.WALL;
	}
}
