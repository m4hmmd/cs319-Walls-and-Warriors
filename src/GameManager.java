import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author samet
 */
public class GameManager {

    private MyComponents myComponents;
    private Model model;
    private CardLayout cardLayout;
    private JPanel card;
    private int levelNo;

    public GameManager(int levelNo, CardLayout cardLayout, JPanel card) {
        this.cardLayout = cardLayout;
        this.card = card;
        this.levelNo = levelNo;
        run();
    }

    public void run() {

        if (levelNo == 1) {
            Soldier[] soldiers = new Soldier[6];
            Wall[] walls = new Wall[4];
            Rectangle[] wallContainers = new Rectangle[4];

            // Allies
            soldiers[0] = new Soldier(1, 0, Color.blue);
            soldiers[1] = new Soldier(3, 1, Color.blue);
            soldiers[2] = new Soldier(0, 2, Color.blue);

            // Enemies
            soldiers[3] = new Soldier(0, 1, Color.red);
            soldiers[4] = new Soldier(3, 2, Color.red);
            soldiers[5] = new Soldier(2, 0, Color.red);

            Castle castle = new Castle(2, 2, 1, 2, Color.green);

            int mapWidth = 5;
            int mapLength = 4;
            model = new Model(mapWidth, mapLength, castle, soldiers, levelNo);
            // 5, 4

            for (int i = 0; i < walls.length; i++) {
                wallContainers[i] = new Rectangle(model.initialXShift + model.squareWidth * i + (mapWidth - walls.length) * (model.squareWidth - 2) / 2, model.initialYShift + model.squareHeight * (mapLength + 1), model.squareWidth, model.squareHeight);
            }

            walls[0] = new Wall(0, 0, new int[]{0, 0, 1, 1, 2, 2}, new int[]{0, -1, -1, 0, 0, 1}, new Color(158, 90, 6).brighter(), 0, wallContainers[0], model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(), model.getSquareWidth());
            walls[1] = new Wall(0, 0, new int[]{0, 0, 1, 1}, new int[]{0, -1, -1, 0}, new Color(158, 90, 6).brighter().brighter(), 1, wallContainers[1], model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(), model.getSquareWidth());
            walls[2] = new Wall(0, 0, new int[]{0, 0, 1, 1, 2}, new int[]{0, 1, 1, 2, 2}, new Color(158, 90, 6).darker(), 2, wallContainers[2], model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(), model.getSquareWidth());
            walls[3] = new Wall(0, 0, new int[]{0, -1, -1, 0, 0, -1, -1}, new int[]{0, 0, -1, -1, -2, -2, -3}, new Color(158, 90, 6), 3, wallContainers[3], model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(), model.getSquareWidth());

            model.setWalls(walls);

            myComponents = new MyComponents(model, cardLayout, card, levelNo);
        } else if (levelNo == 2) {

            Soldier[] soldiers = new Soldier[7];
            Wall[] walls = new Wall[4];
            Rectangle[] wallContainers = new Rectangle[4];

            // Allies
            soldiers[0] = new Soldier(2, 0, Color.blue);
            soldiers[1] = new Soldier(2, 3, Color.blue);
            soldiers[2] = new Soldier(3, 2, Color.blue);

            // Enemies
            soldiers[3] = new Soldier(1, 2, Color.red);
            soldiers[4] = new Soldier(1, 3, Color.red);
            soldiers[5] = new Soldier(3, 0, Color.red);
            soldiers[6] = new Soldier(4, 2, Color.red);

            Castle castle = new Castle(1, 1, 2, 1, Color.green);

            int mapWidth = 5;
            int mapLength = 4;
            model = new Model(mapWidth, mapLength, castle, soldiers, levelNo);
            // 5, 4

            for (int i = 0; i < walls.length; i++) {
                wallContainers[i] = new Rectangle(model.initialXShift + model.squareWidth * i + (mapWidth - walls.length) * (model.squareWidth - 2) / 2, model.initialYShift + model.squareHeight * (mapLength + 1), model.squareWidth, model.squareHeight);
            }

            walls[0] = new Wall(0, 0, new int[]{0, 0, 1, 1, 2, 2}, new int[]{0, -1, -1, 0, 0, 1}, new Color(158, 90, 6).brighter(), 0, wallContainers[0], model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(), model.getSquareWidth());
            walls[1] = new Wall(0, 0, new int[]{0, 0, 1, 1}, new int[]{0, -1, -1, 0}, new Color(158, 90, 6).brighter().brighter(), 1, wallContainers[1], model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(), model.getSquareWidth());
            walls[2] = new Wall(0, 0, new int[]{0, 0, 1, 1, 2}, new int[]{0, 1, 1, 2, 2}, new Color(158, 90, 6).darker(), 2, wallContainers[2], model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(), model.getSquareWidth());
            walls[3] = new Wall(0, 0, new int[]{0, -1, -1, 0, 0, -1, -1}, new int[]{0, 0, -1, -1, -2, -2, -3}, new Color(158, 90, 6), 3, wallContainers[3], model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(), model.getSquareWidth());

            model.setWalls(walls);

            myComponents = new MyComponents(model, cardLayout, card, levelNo);

        }

    }

    public JComponent getPanel() {
        return myComponents;
    }

    public int getInitialXShift() {
        return model.getInitialXShift();
    }

    public int getMapWidth() {
        return model.getMapWidth();
    }

    public int getSquareWidth() {
        return model.getSquareWidth();
    }

    public int getInitialYShift() {
        return model.getInitialYShift();
    }

    public int getSquareHeight() {
        return model.getSquareHeight();
    }

    public int getMapLength() {
        return model.getMapLength();
    }

    void reset() {
        model.reset();
    }
}

//   void occupiedLine(int xInd, int yInd, int x1, int y1, int x2, int y2) {
//
//      int leftOne = (x1==x2) ? x1 : ((x1>x2) ? x2 : x1);
//      int topOne = (y1==y2) ? y1 : ((y1>y2) ? y2 : y1);
//      if(x1==x2)
//         verLines[xInd + leftOne][yInd + topOne] = true;
//      else
//         horLines[xInd + leftOne][yInd + topOne] = true;
//   }

