import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public abstract class Soldier extends GameObject {
	int xShift = 0;
	int yShift = 0;

	protected Image soldierImg;

	int power = 2;
	boolean movable;
	ArrayList<Integer> route;
	Point initialPoint;
	int width;
	int height;
	int nextDir;
	Timer t;
	int nextPos;

	Soldier(boolean movable, int x, int y, ArrayList<Integer> route) {
		super(x, y);
		this.movable = movable;
		this.route = route;
		nextDir = 1;
		nextPos = 0;
		initialPoint = new Point(x, y);
		t = new Timer(100, null);
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
		width = 5*squareWidth/8;
		height = 5*squareHeight/8;
		if (soldierImg != null) {
			g.drawImage(soldierImg, initialXShift + squareWidth * getX() + squareWidth / 16*3 + (movable ? xShift : 0),
					initialYShift + squareHeight * getY() + squareHeight  / 16*3 + (movable ? yShift : 0), width,
					height, null);
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
	
	public void setXShift(int xShift) {
		this.xShift = xShift;
	}

	void setYShift(int yShift) {
		this.yShift = yShift;
	}
}
