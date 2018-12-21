import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class EnemyArmada extends Enemy {
	private Image lakeImg;

	EnemyArmada(boolean movable, int x, int y, ArrayList<Integer> route) {
		super(movable, x, y, route);

		try
		{
			File file = new File("src/img/red-ship.png");
			soldierImg = ImageIO.read(file);
			file = new File("src/img/water-cartoon.png");
			lakeImg = ImageIO.read(file);
		}
		catch ( Exception e )
		{
			System.out.println("Couldn't find file: " + e);
		}
	}

	@Override
	public void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
		Graphics2D g2d = (Graphics2D) g;
		AlphaComposite acomp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
		g2d.setComposite(acomp);
		if (lakeImg != null)
			g.drawImage(lakeImg, initialXShift + squareWidth * x,
					initialYShift + squareHeight * y, squareWidth, squareHeight, null);
		acomp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
		g2d.setComposite(acomp);
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
