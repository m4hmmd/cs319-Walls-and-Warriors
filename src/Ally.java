import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Ally extends Soldier {
//	private Image soldierImg;

	public Ally(boolean movable, int x, int y, ArrayList<Integer> route) {
		super(movable, x, y, route);

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
