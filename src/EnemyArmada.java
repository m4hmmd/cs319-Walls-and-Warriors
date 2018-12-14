import java.awt.Color;
import java.awt.Graphics;

public class EnemyArmada extends Enemy {

	EnemyArmada(boolean movable, int x, int y) {
		super(movable, x, y);
	}

	@Override
	public void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
		g.setColor(new Color(0,0,175,200));
		g.fillRect(initialXShift + squareWidth * x, initialYShift + squareHeight * y, squareWidth, squareHeight);
		g.setColor(Color.RED);
		g.fillOval(initialXShift + squareWidth * getX() + squareWidth / 3,
				initialYShift + squareHeight * getY() + squareHeight / 3, squareWidth / 3, squareHeight / 3);
	}

	@Override
	public int getWholeMapIndex() {
		// TODO Auto-generated method stub
		return Model.ENEMY_ARMADA;
	}

}
