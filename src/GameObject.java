import java.awt.*;

public abstract class GameObject {

    int x, y;
    Color c;

    public GameObject(int x_, int y_, Color c) {
        x = x_;
        y = y_;
        this.c = c;
    }

    abstract void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth);

    Color getColor() {
        return c;
    }

    void setX(int x) {
        this.x = x;
    }

    int getX() {
        return x;
    }

    void setY(int y) {
        this.y = y;
    }

    int getY() {
        return y;
	}

	public abstract int getWholeMapIndex();
}
