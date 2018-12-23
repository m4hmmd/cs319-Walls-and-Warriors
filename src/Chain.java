import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Chain extends WallOrChain {

	public Chain(int x_Ind, int y_Ind, int[] xCoors, int[] yCoors, Color c, int index, int initialXShift,
			int initialYShift, int squareHeight, int squareWidth, int mapHeight, int mapWidth) {
		super(x_Ind, y_Ind, xCoors, yCoors, c, index, initialXShift, initialYShift, squareHeight, squareWidth,
				mapHeight, mapWidth);
	}

	void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth, int shiftY) {
		drawWallOption(g, initialXShift, initialYShift, squareHeight, squareWidth);
		setTheRectanglePoints(squareHeight, squareWidth, shiftY);
		g.setColor(getColor());

		BufferedImage imgLine = new BufferedImage(chainLine.getWidth(null), chainLine.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = imgLine.createGraphics();
		bGr.drawImage(chainLine, 0, 0, null);
		bGr.dispose();

		// get width and height of the image
		int width = imgLine.getWidth();
		int height = imgLine.getHeight();
		//convert to sepia
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				int p = imgLine.getRGB(x,y);

				int a = (p>>24)&0xff;
				int R = (p>>16)&0xff;
				int G = (p>>8)&0xff;
				int B = p&0xff;

				//calculate newRed, newGreen, newBlue
				int newRed = (int)((getColor().getRed()/300.0)*R);
				int newGreen = (int)((getColor().getGreen()/300.0)*G);
				int newBlue = (int)((getColor().getBlue()/300.0)*B);

				//check condition
				if (newRed > 255)
					R = 255;
				else
					R = newRed;

				if (newGreen > 255)
					G = 255;
				else
					G = newGreen;

				if (newBlue > 255)
					B = 255;
				else
					B = newBlue;

				//set new RGB value
				p = (a<<24) | (R<<16) | (G<<8) | B;

				imgLine.setRGB(x, y, p);
			}
		}

		BufferedImage imgEdge = new BufferedImage(chainEdge.getWidth(null), chainEdge.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr2 = imgEdge.createGraphics();
		bGr2.drawImage(chainEdge, 0, 0, null);
		bGr2.dispose();

		// get width and height of the image
		width = imgEdge.getWidth();
		height = imgEdge.getHeight();
		//convert to sepia
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				int p = imgEdge.getRGB(x,y);

				int a = (p>>24)&0xff;
				int R = (p>>16)&0xff;
				int G = (p>>8)&0xff;
				int B = p&0xff;

				//calculate newRed, newGreen, newBlue
				int newRed = (int)((getColor().getRed()/300.0)*R);
				int newGreen = (int)((getColor().getGreen()/300.0)*G);
				int newBlue = (int)((getColor().getBlue()/300.0)*B);

				//check condition
				if (newRed > 255)
					R = 255;
				else
					R = newRed;

				if (newGreen > 255)
					G = 255;
				else
					G = newGreen;

				if (newBlue > 255)
					B = 255;
				else
					B = newBlue;

				//set new RGB value
				p = (a<<24) | (R<<16) | (G<<8) | B;

				imgEdge.setRGB(x, y, p);
			}
		}

		if (visible) {

			Graphics2D g2d = (Graphics2D) g;
			for (Rectangle r : rects) {
				if (r.width < r.height) {
					AffineTransform backup = g2d.getTransform();
					AffineTransform trans = new AffineTransform();
					trans.rotate(Math.PI / 2, r.x + lineWidth / 2, r.y + lineWidth / 2);
					g2d.transform(trans);
					g2d.drawImage(imgLine, r.x, r.y, r.height, r.width, null);

					g2d.setTransform(backup); // restore previous transform
				} else
					g.drawImage(imgLine, r.x, r.y, r.width, r.height, null);
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
				g.drawImage(imgEdge, xEdgeCoor, yEdgeCoor, lineWidth, lineWidth, null);

				g2d.setTransform(backup);
			}
		}
		Graphics2D g2d = (Graphics2D) g;
		for (Rectangle r : rectsOnBar) {
			if (r.width < r.height) {
				AffineTransform backup = g2d.getTransform();
				AffineTransform trans = new AffineTransform();
				trans.rotate(Math.PI / 2, r.x + lineWidthOnBar / 2, r.y + lineWidthOnBar / 2);
				g2d.transform(trans);
				g2d.drawImage(imgLine, r.x, r.y, r.height, r.width, null);

				g2d.setTransform(backup); // restore previous transform
			} else
				g.drawImage(imgLine, r.x, r.y, r.width, r.height, null);
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
			g.drawImage(imgEdge, xEdgeCoor, yEdgeCoor, lineWidthOnBar, lineWidthOnBar, null);

			g2d.setTransform(backup);
		}

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
	
	@Override
	public int getWholeMapIndex() {
		return Model.CHAIN;
	}

	private int getDirectionOfTheEdge(int i) {
		int dir1 = getDirectionOfTheChain(i);
		int dir2 = getDirectionOfTheChain(i + 1);

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

	private int getDirectionOfTheChain(int i) {
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
}
