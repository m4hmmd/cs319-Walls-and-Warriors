import java.awt.Color;
import java.awt.Graphics;

public class Ally extends Soldier {

	public Ally(boolean movable, int x, int y) {
		super(movable, x, y, Color.BLUE);
	}

	@Override
	public void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
		g.setColor(getColor());
		g.fillOval(initialXShift + squareWidth * getX() + squareWidth / 3,
				initialYShift + squareHeight * getY() + squareHeight / 3, squareWidth / 3, squareHeight / 3);
	}

	@Override
	public int getWholeMapIndex() {
		// TODO Auto-generated method stub
		return Model.ALLY;
	}
}
