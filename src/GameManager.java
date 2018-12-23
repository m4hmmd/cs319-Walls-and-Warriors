import java.awt.CardLayout;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 *
 * @author samet
 */
public class GameManager {

	private LevelView levelView;
	private Model model;
	private CardLayout cardLayout;
	private JPanel card;
	private int levelNo;
	private FileSystem fileSystem = new FileSystem();
	GameView gv;
	
	//FileSystem improvement
	private int castle [] = new int [4];
	private int forest [];
	private int lake [];
	private int mapWidth;
	private int mapLength;
	private int size_of_soldiers;

	public GameManager(GameView gv, int levelNo, CardLayout cardLayout, JPanel card)throws IOException {
		this.cardLayout = cardLayout;
		this.card = card;
		this.levelNo = levelNo;
		this.gv = gv;
		run();
	}

	public void run()throws IOException {
		fileSystem.decryptMapFile();
			
		fileSystem.createLevel(levelNo);
		castle = fileSystem.getCastle();
		Castle castle = new Castle(this.castle[0], this.castle[1], this.castle[2], this.castle[3], Color.green);
		mapWidth = fileSystem.getMapWidth();
		mapLength = fileSystem.getMapLength();

		model = new Model(mapWidth, mapLength, castle, levelNo);

		size_of_soldiers = fileSystem.getNumberOfSoldiers();

		for(int i = 0; i < size_of_soldiers; i++)
		{
			model.addSoldier(fileSystem.getDataStructure().getInst()[0], fileSystem.getDataStructure().getInst()[1], fileSystem.getDataStructure().getNum()[0], fileSystem.getDataStructure().getNum()[1], fileSystem.getDataStructure().getRoute());
			fileSystem.incrementSize();
		}
		fileSystem.resetSize();

		for(int i = 0; i < fileSystem.getNumberOfArmada(); i++)
		{
			if (fileSystem.getDataStructureX().getInst()[0])
				model.addAllyArmada(fileSystem.getDataStructureX().getNum()[0], fileSystem.getDataStructureX().getNum()[1], fileSystem.getDataStructureX().getInst()[1], fileSystem.getDataStructureX().getRoute());
			else
				model.addEnemyArmada(fileSystem.getDataStructureX().getNum()[0], fileSystem.getDataStructureX().getNum()[1], fileSystem.getDataStructureX().getInst()[1], fileSystem.getDataStructureX().getRoute());
			fileSystem.incrementSizeX();
		}
		fileSystem.resetSizeX();

		lake = fileSystem.getLake();
		for (int i = 0; i < lake.length; i+=2) {
			int a = lake[i];
			int b = lake[i+1];
			model.addLake(a, b);
		}

		forest = fileSystem.getForest();
		for (int i = 0; i < forest.length; i+=2) {
			int a = forest[i];
			int b = forest[i+1];
			model.addForest(a, b);
		}

		ArrayList<Integer>[] walls = fileSystem.getWalls();
		for(int i = 0; i < fileSystem.getNumberOfWalls(); i++)
		{
			int listToArray [] = new int [walls[i].size()-1];
			for(int r = 1; r < walls[i].size(); r++)
				listToArray[r-1] = walls[i].get(r);

			boolean isWall = walls[i].get(0) == 1 ? true : false;
			model.addWallOrChain(isWall,listToArray);
			fileSystem.incrementSize();
		}
		fileSystem.resetSize();
		fileSystem.deleteMapFile();

		levelView = new LevelView(gv, model, cardLayout, card, levelNo);
	}

	public LevelView getPanel() {
		return levelView;
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

	public void startTimers() {
		model.startTimers();
		levelView.startTimer();
	}

	public void stopTimers() {
		levelView.stopTimer();
		model.stopTimers();
	}
}
