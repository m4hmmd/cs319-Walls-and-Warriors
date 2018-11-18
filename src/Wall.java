import java.awt.*;
import java.util.ArrayList;

public class Wall {

    int lineWidthOnGreenSquare = 4, squareWidthOnGreenSquare = 20, squareHeightOnGreenSquare = 20;
    int index;
    Color c;
    int xInd, yInd, xCoor, yCoor, turn;
    int[] initialXCoors;
    int[] initialYCoors;
    double centerX, centerY;
    boolean visible = false;
    ArrayList<Point> points = new ArrayList<Point>();
    ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
    ArrayList<Rectangle> rectsOnGreenSquare = new ArrayList<Rectangle>();
    Rectangle wallContainer;
    int lineWidth;

    Wall(int x_Ind, int y_Ind, int[] xCoors, int[] yCoors, Color c, int index, Rectangle rectangle, int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
        
        initialXCoors = new int[xCoors.length];
        for(int i = 0; i < xCoors.length; i++)
            initialXCoors[i] = xCoors[i];
        
        initialYCoors = new int[yCoors.length];
        for(int i = 0; i < yCoors.length; i++)
            initialYCoors[i] = yCoors[i];
        
        this.wallContainer = rectangle;
        double totalX = 0;
        double totalY = 0;
        lineWidth = squareWidth / 10;

        for (int i = 0; i < xCoors.length; i++) {
            totalX += xCoors[i];
            totalY += yCoors[i];
            points.add(new Point(xCoors[i], yCoors[i]));
        }

        centerX = totalX / xCoors.length;
        centerY = totalY / yCoors.length;

        xInd = x_Ind;
        yInd = y_Ind;

        xCoor = initialXShift + xInd * squareWidth;
        yCoor = initialYShift + yInd * squareHeight;

        int nextXCoor = xCoor;
        int nextYCoor = yCoor;
        for (int i = 0; i < points.size() - 1; i++) {
            if (points.get(i).x < points.get(i + 1).x) {
                rects.add(new Rectangle(nextXCoor, nextYCoor - lineWidth / 2, squareWidth, lineWidth));
                nextXCoor = nextXCoor + squareWidth;
            } else if (points.get(i).y < points.get(i + 1).y) {
                rects.add(new Rectangle(nextXCoor - lineWidth / 2, nextYCoor, lineWidth, squareHeight));
                nextYCoor = nextYCoor + squareHeight;
            } else if (points.get(i).x > points.get(i + 1).x) {
                rects.add(new Rectangle(nextXCoor - squareWidth, nextYCoor - lineWidth / 2, squareWidth, lineWidth));
                nextXCoor = nextXCoor - squareWidth;
            } else if (points.get(i).y > points.get(i + 1).y) {
                rects.add(new Rectangle(nextXCoor - lineWidth / 2, nextYCoor - squareHeight, lineWidth, squareHeight));
                nextYCoor = nextYCoor - squareHeight;
            }
        }

        nextXCoor = (int) (rectangle.getCenterX() - (centerX) * squareHeightOnGreenSquare);
        nextYCoor = (int) (rectangle.getCenterY() - (centerY) * squareHeightOnGreenSquare);
        for (int i = 0; i < points.size() - 1; i++) {
            if (points.get(i).x < points.get(i + 1).x) {
                rectsOnGreenSquare.add(new Rectangle(nextXCoor, nextYCoor - lineWidthOnGreenSquare / 2, squareWidthOnGreenSquare, lineWidthOnGreenSquare));
                nextXCoor = nextXCoor + squareWidthOnGreenSquare;
            } else if (points.get(i).y < points.get(i + 1).y) {
                rectsOnGreenSquare.add(new Rectangle(nextXCoor - lineWidthOnGreenSquare / 2, nextYCoor, lineWidthOnGreenSquare, squareHeightOnGreenSquare));
                nextYCoor = nextYCoor + squareHeightOnGreenSquare;
            } else if (points.get(i).x > points.get(i + 1).x) {
                rectsOnGreenSquare.add(new Rectangle(nextXCoor - squareWidthOnGreenSquare, nextYCoor - lineWidthOnGreenSquare / 2, squareWidthOnGreenSquare, lineWidthOnGreenSquare));
                nextXCoor = nextXCoor - squareWidthOnGreenSquare;
            } else if (points.get(i).y > points.get(i + 1).y) {
                rectsOnGreenSquare.add(new Rectangle(nextXCoor - lineWidthOnGreenSquare / 2, nextYCoor - squareHeightOnGreenSquare, lineWidthOnGreenSquare, squareHeightOnGreenSquare));
                nextYCoor = nextYCoor - squareHeightOnGreenSquare;
            }
        }
        this.c = c;
        this.index = index;
    }

    Color getColor() {
        return c;
    }

    void draw(Graphics g, int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
        setTheRectanglePoints(squareHeight, squareWidth);
        g.setColor(getColor());
        if (visible) {
            for (int i = 0; i < points.size() - 1; i++) {
                g.fillRect((int) rects.get(i).getX(), (int) rects.get(i).getY(), (int) rects.get(i).getWidth(), (int) rects.get(i).getHeight());
            }
        } else {
            for (int i = 0; i < points.size() - 1; i++) {
                g.fillRect((int) rectsOnGreenSquare.get(i).getX(), (int) rectsOnGreenSquare.get(i).getY(), (int) rectsOnGreenSquare.get(i).getWidth(), (int) rectsOnGreenSquare.get(i).getHeight());
            }
        }
    }

    void turnLeft() {
        for (Point p : points) {
            int px = p.x;
            int py = p.y;

            p.x = -py;
            p.y = px;
        }

        turn++;
        turn = turn % 4;

        double centX = centerX;
        double centY = centerY;
        centerX = -centY;
        centerY = centX;
    }

    void setRectangles() {

        int nextXCoor = (int) (wallContainer.getCenterX() - (centerX) * squareHeightOnGreenSquare);
        int nextYCoor = (int) (wallContainer.getCenterY() - (centerY) * squareHeightOnGreenSquare);
        for (int i = 0; i < points.size() - 1; i++) {
            if (points.get(i).x < points.get(i + 1).x) {
                rectsOnGreenSquare.set(i, new Rectangle(nextXCoor, nextYCoor - lineWidthOnGreenSquare / 2, squareWidthOnGreenSquare, lineWidthOnGreenSquare));
                nextXCoor = nextXCoor + squareWidthOnGreenSquare;
            } else if (points.get(i).y < points.get(i + 1).y) {
                rectsOnGreenSquare.set(i, new Rectangle(nextXCoor - lineWidthOnGreenSquare / 2, nextYCoor, lineWidthOnGreenSquare, squareHeightOnGreenSquare));
                nextYCoor = nextYCoor + squareHeightOnGreenSquare;
            } else if (points.get(i).x > points.get(i + 1).x) {
                rectsOnGreenSquare.set(i, new Rectangle(nextXCoor - squareWidthOnGreenSquare, nextYCoor - lineWidthOnGreenSquare / 2, squareWidthOnGreenSquare, lineWidthOnGreenSquare));
                nextXCoor = nextXCoor - squareWidthOnGreenSquare;
            } else if (points.get(i).y > points.get(i + 1).y) {
                rectsOnGreenSquare.set(i, new Rectangle(nextXCoor - lineWidthOnGreenSquare / 2, nextYCoor - squareHeightOnGreenSquare, lineWidthOnGreenSquare, squareHeightOnGreenSquare));
                nextYCoor = nextYCoor - squareHeightOnGreenSquare;
            }
        }
    }

    void setThePositionAgain(int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
        if ((xCoor - initialXShift) % squareWidth < squareWidth / 2) {
            xCoor -= (xCoor - initialXShift) % squareWidth;
        } else {
            xCoor += squareWidth - ((xCoor - initialXShift) % squareWidth);
        }
        if ((yCoor - initialYShift) % squareHeight < squareHeight / 2) {
            yCoor -= (yCoor - initialYShift) % squareHeight;
        } else {
            yCoor += squareHeight - ((yCoor - initialYShift) % squareHeight);
        }

        xInd = (xCoor - initialXShift) / squareWidth;
        yInd = (yCoor - initialYShift) / squareHeight;
    }

    void setTheRectanglePoints(int squareHeight, int squareWidth) {
        int nextXCoor = xCoor;
        int nextYCoor = yCoor;
        for (int i = 0; i < points.size() - 1; i++) {
            if (points.get(i).x < points.get(i + 1).x) {
                rects.set(i, new Rectangle(nextXCoor, nextYCoor - lineWidth / 2, squareWidth, lineWidth));
                nextXCoor = nextXCoor + squareWidth;
            } else if (points.get(i).y < points.get(i + 1).y) {
                rects.set(i, new Rectangle(nextXCoor - lineWidth / 2, nextYCoor, lineWidth, squareHeight));
                nextYCoor = nextYCoor + squareHeight;
            } else if (points.get(i).x > points.get(i + 1).x) {
                rects.set(i, new Rectangle(nextXCoor - squareWidth, nextYCoor - lineWidth / 2, squareWidth, lineWidth));
                nextXCoor = nextXCoor - squareWidth;
            } else if (points.get(i).y > points.get(i + 1).y) {
                rects.set(i, new Rectangle(nextXCoor - lineWidth / 2, nextYCoor - squareHeight, lineWidth, squareHeight));
                nextYCoor = nextYCoor - squareHeight;
            }
        }
    }

    void remove() {
        visible = false;
    }

    void appear() {
        visible = true;
    }

    boolean contains(int x, int y) {
        for (int i = 0; i < rects.size(); i++) {
            if (rects.get(i).contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    void setWallContainer(Rectangle r) {
        wallContainer = r;
    }

    void reset(int initialXShift, int initialYShift, int squareHeight, int squareWidth) {
        double totalX = 0;
        double totalY = 0;
        lineWidth = squareWidth / 10;
        points.clear();
        for (int i = 0; i < initialXCoors.length; i++) {
            totalX += initialXCoors[i];
            totalY += initialYCoors[i];
            points.add(new Point(initialXCoors[i], initialYCoors[i]));
        }
        
        centerX = totalX / initialXCoors.length;
        centerY = totalY / initialYCoors.length;
        
        xInd = 0;
        yInd = 0;
        xCoor = initialXShift + xInd * squareWidth;
        yCoor = initialYShift + yInd * squareHeight;
        
        turn = 0;
        visible = false;
        
        setRectangles();
        setTheRectanglePoints(squareHeight, squareWidth);
        xInd = -1;
        yInd = -1;
    }

}
