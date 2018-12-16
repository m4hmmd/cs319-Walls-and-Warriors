import java.awt.*;

public class Castle extends GameObject {

	int x1, y1;

	Castle(int x, int y, int x1, int y1, Color c) {
		super(x, y);
		this.x1 = x1;
		this.y1 = y1;
	}

	int getX1() {
		return x1;
	}

	int getY1() {
		return y1;
	}

	void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
		int width = squareWidth / 5;
		int height = squareHeight / 5;
		g.setColor(new Color(0, 255, 0));
		int leftOne = (getX() == getX1()) ? getX() : ((getX() > getX1()) ? getX1() : getX());
		int topOne = (getY() == getY1()) ? getY() : ((getY() > getY1()) ? getY1() : getY());

		g.fillOval(initialXShift + squareWidth * getX() + squareWidth / 4,
				initialYShift + squareHeight * getY() + squareHeight / 4, squareWidth / 2, squareHeight / 2);
		g.fillOval(initialXShift + squareWidth * getX1() + squareWidth / 3,
				initialYShift + squareHeight * getY1() + squareHeight / 3, squareWidth / 3, squareHeight / 3);

		g.fillRect(initialXShift + squareWidth * leftOne + squareWidth / 2 - ((getY() == getY1()) ? 0 : width / 2),
				initialYShift + squareHeight * topOne + squareHeight / 2 - ((getX() == getX1()) ? 0 : height / 2),
				(getX() == getX1()) ? width : (squareWidth), (getY() == getY1()) ? height : squareHeight);
	}

	@Override
	public int getWholeMapIndex() {
		// TODO Auto-generated method stub
		return Model.CASTLE;
	}
}
