import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public abstract class Soldier extends GameObject {
	protected Image soldierImg;

	int power = 2;
	boolean movable;
	Point initialPoint;
	Timer t ;

	Soldier(boolean movable, int x, int y) {
		super(x, y);
		this.movable = movable;
		initialPoint = new Point(x, y);
		t = new Timer(3000, null);
	}

	public void addActionListener(ActionListener aL) {
		t.addActionListener(aL);
	}

	public void start() {
		t.start();
	}

	public void stop() {
		t.stop();
	}
	
	@Override
	public void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
		if (soldierImg != null) {
			g.drawImage(soldierImg, initialXShift + squareWidth * getX() + squareWidth / 8,
					initialYShift + squareHeight * getY() + squareHeight / 8,
					3 * squareWidth / 4, 3 * squareHeight / 4, null);
		}
	}

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
