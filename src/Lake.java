import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Lake extends GameObject {
	private Image img;

	public Lake(int x_, int y_) {
		super(x_, y_);

		try
		{
			File file = new File("src/img/water.png");
			img = ImageIO.read(file);
		}
		catch ( Exception e )
		{
			System.out.println("Couldn't find file: " + e);
		}
	}

	@Override
	void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
		if (img != null)
			g.drawImage(img, initialXShift + squareWidth * x,
					initialYShift + squareHeight * y, squareWidth, squareHeight, null);
	}

	@Override
	public int getWholeMapIndex() {
		return Model.LAKE;
	}

}
