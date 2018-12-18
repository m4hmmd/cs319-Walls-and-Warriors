import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Lake extends GameObject {
	private Image img;

	public Lake(int x_, int y_) {
		super(x_, y_);

		try
		{
			File file = new File("src/img/water-cartoon.png");
			img = ImageIO.read(file);
		}
		catch ( Exception e )
		{
			System.out.println("Couldn't find file: " + e);
		}
	}

	@Override
	void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
		float alpha = 0.7f;
		Graphics2D g2d = (Graphics2D) g;
		AlphaComposite acomp = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, alpha);
		g2d.setComposite(acomp);
		if (img != null)
			g2d.drawImage(img, initialXShift + squareWidth * x,
					initialYShift + squareHeight * y, squareWidth, squareHeight, null);
		alpha = 1f;
		acomp = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, alpha);
		g2d.setComposite(acomp);
	}

	@Override
	public int getWholeMapIndex() {
		return Model.LAKE;
	}

}
