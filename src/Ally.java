import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Ally extends Soldier {
//	private Image soldierImg;

	public Ally(boolean movable, int x, int y) {
		super(movable, x, y, Color.BLUE);

		try
		{
			File file = new File("src/img/blue.png");
			soldierImg = ImageIO.read(file);
		}
		catch ( Exception e )
		{
			System.out.println("Couldn't find file: " + e);
		}
	}

	@Override
	public int getWholeMapIndex() {
		return Model.ALLY;
	}

	@Override
	public boolean isArmada() {
		return false;
	}
}
