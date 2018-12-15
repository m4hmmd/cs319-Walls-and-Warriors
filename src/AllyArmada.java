import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.io.File;

public class AllyArmada extends Ally {

	public AllyArmada(boolean movable,int x, int y) {
		super(movable, x, y);

		try
		{
			File file = new File("src/img/blue-ship.png");
			soldierImg = ImageIO.read(file);
		}
		catch ( Exception e )
		{
			System.out.println("Couldn't find file: " + e);
		}
	}

	@Override
	public void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
		g.setColor(new Color(0,0,175,200));
		g.fillRect(initialXShift + squareWidth * x, initialYShift + squareHeight * y, squareWidth, squareHeight);
		super.draw(g, initialXShift, initialYShift, squareHeight, squareWidth);
	}

	@Override
	public int getWholeMapIndex() {
		return Model.ALLY_ARMADA;
	}

	@Override
	public boolean isArmada() {
		return true;
	}
}
