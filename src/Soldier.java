import java.awt.*;

public abstract class Soldier extends GameObject {

	boolean movable;

	Soldier(boolean movable, int x, int y, Color c) {
		super(x, y, c);
		this.movable = movable;
	}

	@Override
	public abstract void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth);

	@Override
	public abstract int getWholeMapIndex();

}
