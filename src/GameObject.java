import java.awt.*;

public abstract class GameObject {

    int x, y;

    public GameObject(int x_, int y_) {
        x = x_;
        y = y_;
    }

    abstract void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth);

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
