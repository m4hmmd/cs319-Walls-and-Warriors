import java.awt.*;

public class Soldier extends GameObject {

    Soldier(int x, int y, Color c) {
        super(x, y, c);
    }

    @Override
    void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
        g.setColor(getColor());
        g.fillOval(initialXShift + squareWidth * getX() + squareWidth / 3,
                initialYShift + squareHeight * getY() + squareHeight / 3,
                squareWidth / 3, squareHeight / 3);
    }
}
