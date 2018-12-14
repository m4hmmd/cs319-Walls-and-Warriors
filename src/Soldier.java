import java.awt.*;

public abstract class Soldier extends GameObject {

	boolean movable;
	Point initialPoint;

	Soldier(boolean movable, int x, int y, Color c) {
		super(x, y, c);
		this.movable = movable;
		initialPoint = new Point(x, y);
	}

	@Override
	public abstract void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth);

	@Override
	public abstract int getWholeMapIndex();

	public void sendToInitialPosition() {
		x = initialPoint.x;
		y = initialPoint.y;
	}

	public abstract boolean isArmada();

	public boolean isInInitialPosition() {
		return initialPoint.x == x && initialPoint.y == y;
	}
}
