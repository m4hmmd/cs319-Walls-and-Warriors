import java.awt.Color;
import java.awt.Graphics;

public class Forest extends GameObject {

	public Forest(int x_, int y_) {
		super(x_, y_, new Color(0,255,0,200).darker());
		// TODO Auto-generated constructor stub
	}

	@Override
	void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
		g.setColor(c);
		g.fillRect(initialXShift + squareWidth * x, initialYShift + squareHeight * y, squareWidth, squareHeight);
	}

	@Override
	public int getWholeMapIndex() {
		// TODO Auto-generated method stub
		return Model.FOREST;
	}
}
