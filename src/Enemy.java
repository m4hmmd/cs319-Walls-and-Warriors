
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Enemy extends Soldier {

	public Enemy(boolean movable, int x, int y) {
		super(movable, x, y, Color.RED);

		try
		{
			File file = new File("src/img/red.png");
			soldierImg = ImageIO.read(file);
		}
		catch ( Exception e )
		{
			System.out.println("Couldn't find file: " + e);
		}
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
