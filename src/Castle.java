import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;

public class Castle extends GameObject {
	private Image img;

	int x1, y1;

	Castle(int x, int y, int x1, int y1, Color c) {
		super(x, y);

		try {
			File file = new File("src/img/castle.png");
			img = ImageIO.read(file);
		} catch (Exception e) {
			System.out.println("Couldn't find file: " + e);
		}

		this.x1 = x1;
		this.y1 = y1;
	}

	int getX1() {
		return x1;
	}

	int getY1() {
		return y1;
	}

	void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
		int leftOne = (getX() == getX1()) ? getX() : ((getX() > getX1()) ? getX1() : getX());
		int topOne = (getY() == getY1()) ? getY() : ((getY() > getY1()) ? getY1() : getY());

		Graphics2D g2d = (Graphics2D) g;
		double scale = 0.8;
		double shift = (1 - scale) / 2;

		if (img != null) {
			if (getY() == getY1()) {
				g.drawImage(img, initialXShift + squareWidth * leftOne + (int) (2*shift * squareWidth),
						initialYShift + squareHeight * topOne + (int) (shift * squareHeight),
						(int) (2 * squareWidth * scale), (int) (squareHeight * scale), null);
			} else {
				AffineTransform backup = g2d.getTransform();
				AffineTransform trans = new AffineTransform();
				trans.rotate(Math.PI / 2, initialXShift + squareWidth * leftOne + squareWidth / 2,
						initialYShift + squareHeight * topOne + squareHeight / 2);
				g2d.transform(trans);
				g2d.drawImage(img, initialXShift + squareWidth * leftOne + (int) (2*shift * squareWidth), initialYShift + squareHeight * topOne + (int) (shift * squareHeight),
						(int) (2 * squareWidth * scale), (int) (squareHeight * scale), null);

				g2d.setTransform(backup); // restore previous transform
			}
		}
	}

	@Override
	public int getWholeMapIndex() {
		return Model.CASTLE;
	}
}
