
import java.awt.Color;
import java.awt.Graphics;

public class Enemy extends Soldier {

	public Enemy(boolean movable, int x, int y) {
		super(movable, x, y, Color.RED);
	}

	@Override
	public void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
		g.setColor(getColor());
		g.fillOval(initialXShift + squareWidth * getX() + squareWidth / 3,
				initialYShift + squareHeight * getY() + squareHeight / 3, squareWidth / 3, squareHeight / 3);
	}

	@Override
	public int getWholeMapIndex() {
		return Model.ENEMY;
	}
	
	@Override
	public boolean isArmada() {
		return false;
	}
}
