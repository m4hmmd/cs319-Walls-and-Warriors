import java.awt.CardLayout;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

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
	private FileSystem fileSystem = new FileSystem();
	GameView gv;
	
	//FileSytsme improvement
	private int castle [] = new int [4];
	private int forestAndLake [] = new int [9];
	private int mapWidth;
	private int mapLength;
	private int size_of_soldiers;
	private ArrayList <Integer> shape = new ArrayList <Integer>();
	

	public GameManager(GameView gv, int levelNo, CardLayout cardLayout, JPanel card)throws IOException {
		this.cardLayout = cardLayout;
		this.card = card;
		this.levelNo = levelNo;
		this.gv = gv;
		run();
	}

	public void run()throws IOException {
			
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
		
		forestAndLake = fileSystem.getForestAndLake();
		
		for (int i = forestAndLake[0]; i < forestAndLake[1]; i++) {
			model.addLake(i, forestAndLake[2]);
			model.addLake(i, forestAndLake[3]);
			model.addEnemyArmada(i, forestAndLake[4], false, null);
			//model.addAllyArmada(i, forestAndLake[6], fileSystem.getArmada());
		}

		for (int i = forestAndLake[7]; i < forestAndLake[8]; i++) {
			model.addForest(i, forestAndLake[8]);
		}
		
		
		for(int i = 0; i < 6; i++)
		{
			shape = fileSystem.getDataStructure().getShape();
			int listToArray [] = new int [shape.size()];
			for(int r = 0; r < shape.size(); r++)
				listToArray[r] = shape.get(r);
			model.addWallOrChain(fileSystem.getDataStructure().getChainOrWall(),listToArray);
			fileSystem.incrementSize();	
		}
		fileSystem.resetSize();
		
		myComponents = new MyComponents(gv, model, cardLayout, card, levelNo);

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

	public void startTimers() {
		model.startTimers();
		myComponents.startTimer();
	}

	public void stopTimers() {
		myComponents.stopTimer();
		model.stopTimers();
	}
}