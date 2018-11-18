import java.awt.*;
import java.util.ArrayList;

public class Model {

    private Wall[] walls;
    private Soldier[] soldiers;
    private Castle castle;
    private boolean[][] verLines;
    private boolean[][] horLines;
    private GameObject[][] map;

    int levelNo;
    boolean gameFinished = false;
    int squareHeight = 100, squareWidth = 100, lineWidth = squareWidth / 10, initialXShift = 200, initialYShift = 80, mapWidth, mapLength, barShift = 75;

    public Model(int mapWidth, int mapLength, Castle castle, Soldier[] soldiers, int levelNo) {
        this.mapLength = mapLength;
        this.mapWidth = mapWidth;
        this.castle = castle;
        this.soldiers = soldiers;
        this.levelNo = levelNo;

        verLines = new boolean[mapWidth + 1][mapLength];
        horLines = new boolean[mapWidth][mapLength + 1];
        map = new GameObject[mapWidth][mapLength];

        // unused lines
        verLines[0][0] = true;
        horLines[0][0] = true;

        verLines[verLines.length - 1][0] = true;
        horLines[horLines.length - 1][0] = true;

        verLines[0][verLines[0].length - 1] = true;
        horLines[0][horLines[0].length - 1] = true;

        verLines[verLines.length - 1][verLines[0].length - 1] = true;
        horLines[horLines.length - 1][horLines[0].length - 1] = true;

        // invalidate the line segment occupied by the castle
        int leftOne = (castle.x == castle.x1) ? castle.x : ((castle.x > castle.x1) ? castle.x1 : castle.x);
        int topOne = (castle.y == castle.y1) ? castle.y : ((castle.y > castle.y1) ? castle.y1 : castle.y);
        if (castle.x == castle.x1) {
            horLines[leftOne][topOne + 1] = true;
        } else {
            verLines[leftOne + 1][topOne] = true;
        }

    }

    void reset() {
        verLines = new boolean[mapWidth + 1][mapLength];
        horLines = new boolean[mapWidth][mapLength + 1];
        map = new GameObject[mapWidth][mapLength];

        gameFinished = false;

        // unused lines
        verLines[0][0] = true;
        horLines[0][0] = true;

        verLines[verLines.length - 1][0] = true;
        horLines[horLines.length - 1][0] = true;

        verLines[0][verLines[0].length - 1] = true;
        horLines[0][horLines[0].length - 1] = true;

        verLines[verLines.length - 1][verLines[0].length - 1] = true;
        horLines[horLines.length - 1][horLines[0].length - 1] = true;

        // invalidate the line segment occupied by the castle
        int leftOne = (castle.x == castle.x1) ? castle.x : ((castle.x > castle.x1) ? castle.x1 : castle.x);
        int topOne = (castle.y == castle.y1) ? castle.y : ((castle.y > castle.y1) ? castle.y1 : castle.y);
        if (castle.x == castle.x1) {
            horLines[leftOne][topOne + 1] = true;
        } else {
            verLines[leftOne + 1][topOne] = true;
        }

        for (Wall w : walls) {
            w.reset(initialXShift, initialYShift, squareHeight, squareWidth);
        }
    }

    void removeFromLines(Wall w) {
        for (int i = 0; i < w.points.size() - 1; i++) {
            int leftOne = (w.points.get(i).x == w.points.get(i + 1).x) ? w.points.get(i).x : ((w.points.get(i).x > w.points.get(i + 1).x) ? w.points.get(i + 1).x : w.points.get(i).x);
            int topOne = (w.points.get(i).y == w.points.get(i + 1).y) ? w.points.get(i).y : ((w.points.get(i).y > w.points.get(i + 1).y) ? w.points.get(i + 1).y : w.points.get(i).y);
            if (w.points.get(i).x == w.points.get(i + 1).x) {
                verLines[w.xInd + leftOne][w.yInd + topOne] = false;
            } else {
                horLines[w.xInd + leftOne][w.yInd + topOne] = false;
            }
        }
    }

    boolean onAvailablePlace(Wall w) {
        for (int i = 0; i < w.points.size() - 1; i++) {
            int leftOne = (w.points.get(i).x == w.points.get(i + 1).x) ? w.points.get(i).x : ((w.points.get(i).x > w.points.get(i + 1).x) ? w.points.get(i + 1).x : w.points.get(i).x);
            int topOne = (w.points.get(i).y == w.points.get(i + 1).y) ? w.points.get(i).y : ((w.points.get(i).y > w.points.get(i + 1).y) ? w.points.get(i + 1).y : w.points.get(i).y);

            if (w.points.get(i).x == w.points.get(i + 1).x && verLines[w.xInd + leftOne][w.yInd + topOne]) {
                return false;
            } else if (w.points.get(i).y == w.points.get(i + 1).y && horLines[w.xInd + leftOne][w.yInd + topOne]) {
                return false;
            }
        }
        return true;
    }

    void addToLines(Wall w) {
        for (int i = 0; i < w.points.size() - 1; i++) {
            int leftOne = (w.points.get(i).x == w.points.get(i + 1).x) ? w.points.get(i).x : ((w.points.get(i).x > w.points.get(i + 1).x) ? w.points.get(i + 1).x : w.points.get(i).x);
            int topOne = (w.points.get(i).y == w.points.get(i + 1).y) ? w.points.get(i).y : ((w.points.get(i).y > w.points.get(i + 1).y) ? w.points.get(i + 1).y : w.points.get(i).y);
            if (w.points.get(i).x == w.points.get(i + 1).x) {
                verLines[w.xInd + leftOne][w.yInd + topOne] = true;
            } else {
                horLines[w.xInd + leftOne][w.yInd + topOne] = true;
            }
        }
    }

    public boolean isGameFinished() {
        if (levelNo == 1) {
            if (walls[0].xInd == 1 && walls[0].yInd == 1
                    && walls[1].xInd == 3 && walls[1].yInd == 1
                    && walls[2].xInd == 1 && walls[2].yInd == 4
                    && walls[3].xInd == 0 && walls[3].yInd == 1) {
                gameFinished = true;
                return true;
            }
        } else if (levelNo == 2) {
            if (walls[0].xInd == 3 && walls[0].yInd == 2
                    && walls[1].xInd == 1 && walls[1].yInd == 4
                    && walls[2].xInd == 4 && walls[2].yInd == 2
                    && walls[3].xInd == 1 && walls[3].yInd == 0) {
                gameFinished = true;
                return true;
            }
        }
        return false;
    }

    boolean outOfScreen(Wall w) {
        ArrayList<Point> points = w.points;
        int leftMost = points.get(0).x;
        int rightMost = points.get(0).x;
        int bottomMost = points.get(0).y;
        int topMost = points.get(0).y;

        for (int i = 1; i < points.size(); i++) {
            if (leftMost > points.get(i).x) {
                leftMost = points.get(i).x;
            }
            if (rightMost < points.get(i).x) {
                rightMost = points.get(i).x;
            }
            if (bottomMost > points.get(i).y) {
                bottomMost = points.get(i).y;
            }
            if (topMost < points.get(i).y) {
                topMost = points.get(i).y;
            }
        }
        return (leftMost + w.xInd < 0) | (rightMost + w.xInd > mapWidth) | (bottomMost + w.yInd < 0) | (topMost + w.yInd > mapLength);
    }

    public void setWalls(Wall[] walls) {
        this.walls = walls;
    }

    public Wall[] getWalls() {
        return walls;
    }

    public Soldier[] getSoldiers() {
        return soldiers;
    }

    public Castle getCastle() {
        return castle;
    }

    public boolean[][] getVerLines() {
        return verLines;
    }

    public boolean[][] getHorLines() {
        return horLines;
    }

    public GameObject[][] getMap() {
        return map;
    }

    public int getSquareHeight() {
        return squareHeight;
    }

    public int getSquareWidth() {
        return squareWidth;
    }

    public int getInitialXShift() {
        return initialXShift;
    }

    public int getInitialYShift() {
        return initialYShift;
    }

    public void setInitialXShift(int xShift) {
        initialXShift = xShift;
    }

    public void setInitialYShift(int yShift) {
        initialYShift = yShift;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapLength() {
        return mapLength;
    }

    void setSizesOfObjects() {
        for (int i = 0; i < walls.length; i++) {
            Rectangle r = new Rectangle(initialXShift + squareWidth * i + (mapWidth - walls.length) * (squareWidth - 2) / 2, initialYShift + squareHeight * (mapLength + 1), squareWidth, squareHeight);
            walls[i].setWallContainer(r);
            walls[i].setRectangles();
        }
    }

    void rearrangeWalls() {
        for (int i = 0; i < walls.length; i++) {
            walls[i].setThePositionAgain(initialXShift, initialYShift, squareHeight, squareWidth);
        }
    }

    int getBarShift() {
        return barShift;
    }
}
