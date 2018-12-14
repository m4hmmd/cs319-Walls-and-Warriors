import java.awt.Color;
import java.awt.Graphics;

public class Chain extends WallOrChain {

	public Chain(int x_Ind, int y_Ind, int[] xCoors, int[] yCoors, Color c, int index, int initialXShift,
			int initialYShift, int squareHeight, int squareWidth, int mapHeight, int mapWidth) {
		super(x_Ind, y_Ind, xCoors, yCoors, c, index, initialXShift, initialYShift, squareHeight,
				squareWidth, mapHeight, mapWidth);
	}

	void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth, int shiftY) {
		setTheRectanglePoints(squareHeight, squareWidth, shiftY);
		g.setColor(getColor());
		if (visible) {
			for (int i = 0; i < points.size() - 1; i++) {
				g.drawRect((int) rects.get(i).getX(), (int) rects.get(i).getY(), (int) rects.get(i).getWidth(),
						(int) rects.get(i).getHeight());
			}
		} else {
			for (int i = 0; i < points.size() - 1; i++) {
				g.drawRect((int) rectsOnGreenSquare.get(i).getX(), (int) rectsOnGreenSquare.get(i).getY(),
						(int) rectsOnGreenSquare.get(i).getWidth(), (int) rectsOnGreenSquare.get(i).getHeight());
			}
		}
	}

}
