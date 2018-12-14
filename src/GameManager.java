import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.*;
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
	GameView gv;

	public GameManager(GameView gv, int levelNo, CardLayout cardLayout, JPanel card) {
		this.cardLayout = cardLayout;
		this.card = card;
		this.levelNo = levelNo;
		this.gv = gv;
		run();
	}

	public void run() {

		if (levelNo == 1) {

			Castle castle = new Castle(1, 2, 2, 2, Color.green);
			// Castle castle = new Castle(1, 2, 1, 3, Color.green);
			int mapWidth = 8;
			int mapLength = 6;
			model = new Model(mapWidth, mapLength, castle, levelNo);

			// Allies
			model.addSoldier(true, false, 1, 0);
			model.addSoldier(true, false, 3, 1);
			model.addSoldier(true, false, 0, 2);
			
			// Enemies
			model.addSoldier(false, false, 0, 1);
			model.addSoldier(false, true, 3, 2);
			model.addSoldier(false, false, 2, 0);

			// 5, 4

			for (int i = 5; i < 8; i++) {
				model.addLake(i, 1);
				model.addLake(i, 2);
				model.addEnemyArmada(i, 3, i < 6);
				//model.addAllyArmada(i, 4, false);
			}

			for (int i = 1; i < 5; i++) {
				model.addForest(i, 5);
			}

			model.addWallOrChain(true, Model.UP, Model.RIGHT, Model.DOWN, Model.RIGHT, Model.DOWN);
			model.addWallOrChain(true, Model.UP, Model.RIGHT, Model.DOWN);
			model.addWallOrChain(true, Model.DOWN, Model.RIGHT, Model.DOWN, Model.RIGHT);
			model.addWallOrChain(true, Model.LEFT, Model.UP, Model.RIGHT, Model.UP, Model.LEFT, Model.UP);
			model.addWallOrChain(false, Model.DOWN, Model.RIGHT, Model.DOWN, Model.RIGHT);
			model.addWallOrChain(false, Model.LEFT);

			myComponents = new MyComponents(gv, model, cardLayout, card, levelNo);
		} else if (levelNo == 2) {

			Castle castle = new Castle(1, 1, 2, 1, Color.green);

			int mapWidth = 5;
			int mapLength = 4;
			model = new Model(mapWidth, mapLength, castle, levelNo);

			// Allies
			model.addSoldier(true, false, 2, 0);
			model.addSoldier(true, false, 2, 3);
			model.addSoldier(true, false, 3, 2);

			// Enemies
			model.addSoldier(false, false, 1, 2);
			model.addSoldier(false, false, 1, 3);
			model.addSoldier(false, false, 3, 0);
			model.addSoldier(false, false, 4, 2);
			// 5, 4

			model.addWallOrChain(true, Model.UP, Model.RIGHT, Model.DOWN, Model.RIGHT, Model.DOWN);
			model.addWallOrChain(true, Model.UP, Model.RIGHT, Model.DOWN);
			model.addWallOrChain(true, Model.DOWN, Model.RIGHT, Model.DOWN, Model.RIGHT);
			model.addWallOrChain(true, Model.LEFT, Model.UP, Model.RIGHT, Model.UP, Model.LEFT, Model.UP);
			model.addWallOrChain(false, Model.DOWN, Model.RIGHT, Model.DOWN, Model.RIGHT);
			model.addWallOrChain(false, Model.LEFT, Model.UP, Model.RIGHT, Model.UP, Model.LEFT, Model.UP);

			myComponents = new MyComponents(gv, model, cardLayout, card, levelNo);

		}

	}

	public MyComponents getPanel() {
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

// void occupiedLine(int xInd, int yInd, int x1, int y1, int x2, int y2) {
//
// int leftOne = (x1==x2) ? x1 : ((x1>x2) ? x2 : x1);
// int topOne = (y1==y2) ? y1 : ((y1>y2) ? y2 : y1);
// if(x1==x2)
// verLines[xInd + leftOne][yInd + topOne] = true;
// else
// horLines[xInd + leftOne][yInd + topOne] = true;
// }
