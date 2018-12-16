import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class EnemyArmada extends Enemy {
	private Image lakeImg;

	EnemyArmada(boolean movable, int x, int y) {
		super(movable, x, y);

		try
		{
			File file = new File("src/img/red-ship.png");
			soldierImg = ImageIO.read(file);
			file = new File("src/img/water.png");
			lakeImg = ImageIO.read(file);
		}
		catch ( Exception e )
		{
			System.out.println("Couldn't find file: " + e);
		}
	}

	@Override
	public void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
//		g.setColor(new Color(0,0,175,200));
//		g.fillRect(initialXShift + squareWidth * x, initialYShift + squareHeight * y, squareWidth, squareHeight);
		if (lakeImg != null)
			g.drawImage(lakeImg, initialXShift + squareWidth * x,
					initialYShift + squareHeight * y, squareWidth, squareHeight, null);
		super.draw(g, initialXShift, initialYShift, squareHeight, squareWidth);
	}

	@Override
	public int getWholeMapIndex() {
		return Model.ENEMY_ARMADA;
	}

	@Override
	public boolean isArmada() {
		return true;
	}
}
