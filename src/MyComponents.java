import javax.imageio.ImageIO;
import javax.swing.*;

import com.sun.xml.internal.ws.org.objectweb.asm.ClassAdapter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class MyComponents extends JComponent {

	private Model model;
	private CardLayout cardLayout;
	private JPanel card;
	int levelNo;
	WallOrChain selectedMouse;
	WallOrChain selectedKey;

	JButton backButton;
	private int pWidth = 0;
	private int pHeight = 0;
	GameView gv;
	TimerActionListener timer;

	public MyComponents(GameView gv, Model model, CardLayout cardLayout, JPanel card, int levelNo) {
		this.model = model;
		this.cardLayout = cardLayout;
		this.card = card;
		this.levelNo = levelNo;
		this.gv = gv;
		timer = new TimerActionListener();
//		backButton = new JButton("Home");
		backButton = new MyButton("Home", "Game Menu", 30, 40,new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(card, "Game Menu");
				model.reset();
				selectedKey = null;
				selectedMouse = null;
				timer.stop();
			}
		});
		// back = new MyButton("Back", "Game Menu", btnSizeS, btnSizeScaledS,this);

//		backButton.setForeground(Color.WHITE);
//		backButton.setFont(new Font("Arial", Font.PLAIN, 30));
//		backButton.setOpaque(false);
//		backButton.setContentAreaFilled(false);
//		backButton.setBorderPainted(false);
//		// backButton.setHorizontalAlignment(SwingConstants.LEFT);
//
		backButton.addMouseListener(new MouseAdapter() {
			public void mouseExited(MouseEvent e) {
				((JButton) (e.getSource())).setFont(new Font("Arial", Font.PLAIN, 30));
			}
		});
		backButton.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				((JButton) (e.getSource())).setFont(new Font("Arial", Font.PLAIN, 40));
			}

		});
//		backButton.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				cardLayout.show(card, "Game Menu");
//				model.reset();
//				selectedKey = null;
//				selectedMouse = null;
//				timer.stop();
//			}
//		});

		add(backButton);
		Listeners listener = new Listeners();
		requestFocusInWindow();
		addKeyListener(listener);
		addMouseListener(listener);
		addMouseMotionListener(listener);
		setMinimumSize(new Dimension(300, model.getBarShift() + 50));
		setFocusable(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		try {
			g.drawImage(ImageIO.read(new File("src/img/img1.jpeg")), 0, 0, getWidth(), getHeight(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(Color.gray);

		if (pWidth != getWidth() || pHeight != getHeight()) {
			model.setValues(getWidth(), getHeight());
			model.centerTheGame(getWidth(), getHeight());
			model.rearrangeWalls();
		}

		int fontSize = 20;
		g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
		g.setColor(Color.WHITE.darker());
		g.drawString("LEVEL " + levelNo, getWidth() / 2 - 45, 50);

		drawGrid(g);
		drawGameObjects(g);
		drawSoldiers(g);
		model.getCastle().draw(g, model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(),
				model.getSquareWidth());
		drawWallOptions(g);
		drawWalls(g);

		backButton.setBounds(0, (int) (getHeight() / 10 * 0.5), getWidth() / 5, getHeight() / 10);
		pWidth = getWidth();
		pHeight = getHeight();
	}

	private void drawGameObjects(Graphics g) {
		for (GameObject[] gm : model.getMap()) {
			for (GameObject gmo : gm) {
				if (gmo != null)
					gmo.draw(g, model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(),
							model.getSquareWidth());
			}
		}
	}

	void drawGrid(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(model.initialXShift + model.squareWidth - model.lineWidth / 2,
				model.initialYShift - model.lineWidth / 2, model.squareWidth * (model.mapWidth - 2) + model.lineWidth,
				model.squareHeight * model.mapLength + model.lineWidth);
		g.fillRect(model.initialXShift - model.lineWidth / 2,
				model.initialYShift + model.squareHeight - model.lineWidth / 2,
				model.squareWidth * model.mapWidth + model.lineWidth,
				model.squareHeight * (model.mapLength - 2) + model.lineWidth);

		for (int i = 0; i < model.mapWidth; i++) {
			for (int j = 0; j < model.mapLength; j++) {
				if (i == 0 && j == 0) {
				} else if (i == model.mapWidth - 1 && j == 0) {
				} else if (i == 0 && j == model.mapLength - 1) {
				} else if (i == model.mapWidth - 1 && j == model.mapLength - 1) {
				} else {
					g.setColor(Color.gray.brighter());
					g.fillRect(model.initialXShift + model.squareWidth * i + model.lineWidth / 2,
							model.initialYShift + model.squareHeight * j + model.lineWidth / 2,
							model.squareWidth - model.lineWidth + 1, model.squareHeight - model.lineWidth + 1);
				}
			}
		}
	}

	void drawWallOptions(Graphics g) {
		g.setColor(Color.GRAY.brighter());
		for (int i = 0; i < model.getWalls().length; i++) {
			Rectangle r = model.getWalls()[i].wallContainer;
			g.fillRect(r.x, r.y, r.width, r.height);
		}
		g.setColor(Color.GRAY);
		for (int i = 0; i < model.getWalls().length; i++) {
			Rectangle r = model.getWalls()[i].wallContainer;
			g.drawRect(r.x, r.y, r.width, r.height);
		}
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, model.squareHeight / 6));
		for (int i = 0; i < model.getWalls().length; i++) {
			Rectangle r = model.getWalls()[i].wallContainer;
			g.drawString("" + (i + 1), r.x + model.squareWidth / 14, r.y + model.squareHeight / 6);
		}
	}

	void drawSoldiers(Graphics g) {
		for (int i = 0; i < model.getSoldiers().size(); i++) {
			model.getSoldiers().get(i).draw(g, model.getInitialXShift(), model.getInitialYShift(),
					model.getSquareHeight(), model.getSquareWidth());
		}
	}

	void drawWalls(Graphics g) {
		for (int i = 0; i < model.getWalls().length; i++) {
			if (selectedMouse != model.getWalls()[i] && selectedKey != model.getWalls()[i])
				model.getWalls()[i].draw(g, model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(),
						model.getSquareWidth(), 0);
		}
		if (selectedMouse != null) {
			selectedMouse.draw(g, model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(),
					model.getSquareWidth(), 0);
		}
		if (selectedKey != null) {
			selectedKey.draw(g, model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(),
					model.getSquareWidth(), -model.getSquareHeight() / 6);
		}
	}

	class Listeners implements MouseListener, MouseMotionListener, KeyListener {

		int indexOfGreenSquare;
		int wallStartX_KEY;
		int wallStartY_KEY;
		int wallStartXInd_KEY;
		int wallStartYInd_KEY;
		int wallStartX_MOUSE;
		int wallStartY_MOUSE;
		int wallStartXInd_MOUSE;
		int wallStartYInd_MOUSE;
		int turnStart;
		int clickedX;
		int clickedY;
		boolean wasInvisible_KEY;
		boolean wasInvisible_MOUSE;

		public void mouseClicked(MouseEvent e) {
			int i = 0;
			boolean emptyPlace = true;

			for (WallOrChain wall : model.getWalls()) {
				Rectangle r = wall.wallContainer;
				if (r.contains(e.getX(), e.getY()) && !model.getWalls()[i].visible) {
					if (SwingUtilities.isLeftMouseButton(e))
						model.getWalls()[i].turnLeft();
					else
						model.getWalls()[i].turnRight();
					model.getWalls()[i].setRectangles();
					emptyPlace = false;
				}
				i++;
			}

			for (WallOrChain w : model.getWalls()) {
				if (w.visible && w.contains(e.getX(), e.getY())) {
					if (selectedKey != null) {
						placeTheWall(selectedKey);
					}
					if (selectedKey != w) {
						selectedKey = w;
						model.removeFromLines(selectedKey);
						wallStartX_KEY = selectedKey.xCoor;
						wallStartY_KEY = selectedKey.yCoor;
						wallStartXInd_KEY = selectedKey.xInd;
						wallStartYInd_KEY = selectedKey.yInd;
						turnStart = selectedKey.turn;
					} else
						selectedKey = null;
					emptyPlace = false;
					break;
				}
			}

			if (emptyPlace) {
				placeTheWall(selectedKey);
				selectedKey = null;
			}
			repaint();
		}

		public void mousePressed(MouseEvent e) {
			clickedX = e.getX();
			clickedY = e.getY();
			boolean isWall = false;
			for (WallOrChain w : model.getWalls()) {
				if (w != selectedKey && w.visible && w.contains(e.getX(), e.getY())) {
					selectedMouse = w;
					model.removeFromLines(selectedMouse);
					wallStartX_MOUSE = selectedMouse.xCoor;
					wallStartY_MOUSE = selectedMouse.yCoor;
					wallStartXInd_MOUSE = selectedMouse.xInd;
					wallStartYInd_MOUSE = selectedMouse.yInd;
					isWall = true;
					break;
				}

				else if (w == selectedKey && w.visible && w.contains(e.getX(), e.getY())) {
					selectedMouse = w;
					selectedKey = null;
					wallStartX_MOUSE = selectedMouse.xCoor;
					wallStartY_MOUSE = selectedMouse.yCoor;
					wallStartXInd_MOUSE = wallStartXInd_KEY;
					wallStartYInd_MOUSE = wallStartYInd_KEY;
					isWall = true;
					break;
				}
			}

			if (!isWall) {
				for (int i = 0; i < model.getWalls().length; i++) {

					if (model.getWalls()[i].wallContainer.contains(clickedX, clickedY)
							&& !model.getWalls()[i].visible) {
						model.getWalls()[i].appear();
						selectedMouse = model.getWalls()[i];
						indexOfGreenSquare = i;
						wallStartX_MOUSE = (int) model.getWalls()[i].wallContainer.getCenterX();
						wallStartY_MOUSE = (int) model.getWalls()[i].wallContainer.getCenterY();
						wasInvisible_MOUSE = true;
					}
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (selectedMouse != null) {
				if (!wasInvisible_MOUSE) {
					selectedMouse.xCoor = (int) (wallStartX_MOUSE + e.getX() - clickedX);
					selectedMouse.yCoor = (int) (wallStartY_MOUSE + e.getY() - clickedY);
				} else {
					selectedMouse.xCoor = (int) (wallStartX_MOUSE + e.getX() - clickedX
							- selectedMouse.centerX * model.getSquareWidth());
					selectedMouse.yCoor = (int) (wallStartY_MOUSE + e.getY() - clickedY
							- selectedMouse.centerY * model.getSquareHeight());
				}
				setColor(selectedMouse);
			}
			repaint();
		}

		private void setColor(WallOrChain w) {
			if (model.outOfScreen(w)) {
				w.setColor(Color.BLACK);
				w.setCoordinates(model.initialXShift, model.initialYShift, model.squareHeight, model.squareWidth);
			} else if (!model.onAvailablePlace(w)) {
				w.setThePositionAgain(model.initialXShift, model.initialYShift, model.squareHeight, model.squareWidth);
				w.setColor(Color.RED);
			} else {
				w.setThePositionAgain(model.initialXShift, model.initialYShift, model.squareHeight, model.squareWidth);
				w.setColorToOriginal();
			}

		}

		public void mouseReleased(MouseEvent e) {
			if (selectedMouse != null) {
				selectedMouse.setColorToOriginal();
				selectedMouse.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(),
						model.getSquareHeight(), model.getSquareWidth());

				if (model.outOfScreen(selectedMouse)) {
					selectedMouse.remove();
					selectedMouse.setRectangles();
				} else {
					if (!model.onAvailablePlace(selectedMouse)) {
						if (wasInvisible_MOUSE) {
							selectedMouse.remove();
							selectedMouse.setRectangles();

						} else {
							if (model.isAvailablePlaceFor(selectedMouse, wallStartXInd_MOUSE, wallStartYInd_MOUSE)) {
								selectedMouse.xCoor = wallStartX_MOUSE;
								selectedMouse.yCoor = wallStartY_MOUSE;
								selectedMouse.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(),
										model.getSquareHeight(), model.getSquareWidth());
								model.addToLines(selectedMouse);
							} else {
								selectedMouse.setIndexes(-1, -1);
								selectedMouse.remove();
							}
						}

					} else {
						selectedMouse.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(),
								model.getSquareHeight(), model.getSquareWidth());
						model.addToLines(selectedMouse);
						SoundManager.wallPlaced();

						if (model.isGameFinished()) {
							gameFinished();
						}
					}
				}
			}
			wasInvisible_MOUSE = false;
			selectedMouse = null;
			repaint();
		}

		private void placeTheWall(WallOrChain wall) {
			if (wall != null) {
				wall.setColorToOriginal();
				wall.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(),
						model.getSquareWidth());

				if (model.outOfScreen(wall)) {
					wall.remove();
					wall.setRectangles();
				} else {
					if (!model.onAvailablePlace(wall)) {
						if (wasInvisible_KEY) {
							wall.remove();
							wall.setRectangles();
						} else {
							wall.xCoor = wallStartX_KEY;
							wall.yCoor = wallStartY_KEY;
							wall.setTurn(turnStart);
							wall.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(),
									model.getSquareHeight(), model.getSquareWidth());
							model.addToLines(wall);

						}

					} else {
						wall.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(),
								model.getSquareHeight(), model.getSquareWidth());
						model.addToLines(wall);
						SoundManager.wallPlaced();

						if (model.isGameFinished()) {
							gameFinished();
						}
					}
				}
			}
			wall = null;
			repaint();
		}

		private void gameFinished() {
			selectedKey = null;
			repaint();
			model.gameFinished = true;
			SoundManager.gameWon();

			Object[] options = { "Return Home", "Next Level" };

			int n = JOptionPane.showOptionDialog(null, "You have completed this level!", "Congratulations!!",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

			if (GameView.lastCompletedLevel < levelNo)
				GameView.lastCompletedLevel = levelNo;

			gv.levelButtons[levelNo].setEnabled(true);
			gv.levelButtons[levelNo].setIcon(null);
			if (n == JOptionPane.YES_OPTION) {
				cardLayout.show(card, "Game Menu");
				model.reset();
			} else if (n == JOptionPane.NO_OPTION) {
				cardLayout.show(card, "Level " + (levelNo + 1));
				model.reset();
			}
			selectedKey = null;
			selectedMouse = null;
			timer.stop();
		}

		public void mouseExited(MouseEvent e) {

		}

		public void mouseMoved(MouseEvent e) {

		}

		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			if (selectedKey != null) {
				if (key == KeyEvent.VK_LEFT) {
					selectedKey.goLeft();
					selectedKey.setThePositionAgainByIndex(model.initialXShift, model.initialYShift, model.squareHeight,
							model.squareWidth);
				}

				else if (key == KeyEvent.VK_RIGHT) {
					selectedKey.goRight();
					selectedKey.setThePositionAgainByIndex(model.initialXShift, model.initialYShift, model.squareHeight,
							model.squareWidth);
				}

				else if (key == KeyEvent.VK_UP) {
					selectedKey.goUp();
					selectedKey.setThePositionAgainByIndex(model.initialXShift, model.initialYShift, model.squareHeight,
							model.squareWidth);
				}

				else if (key == KeyEvent.VK_DOWN) {
					selectedKey.goDown();
					selectedKey.setThePositionAgainByIndex(model.initialXShift, model.initialYShift, model.squareHeight,
							model.squareWidth);
				}

				else if (key == KeyEvent.VK_ENTER) {
					if (selectedKey != null) {
						placeSelectedKey();
					}
					wasInvisible_KEY = false;
					selectedKey = null;
					repaint();
				}

				else if (key == KeyEvent.VK_D) {
					selectedKey.turnRight();
					selectedKey.setThePositionAgainByIndex(model.initialXShift, model.initialYShift, model.squareHeight,
							model.squareWidth);
					selectedKey.setRectangles();
				}

				else if (key == KeyEvent.VK_A) {
					selectedKey.turnLeft();
					selectedKey.setThePositionAgainByIndex(model.initialXShift, model.initialYShift, model.squareHeight,
							model.squareWidth);
					selectedKey.setRectangles();
				}

				else if (key == KeyEvent.VK_ESCAPE) {
					if (!wasInvisible_KEY) {
						selectedKey.setTurn(turnStart);
					}
					if (wasInvisible_KEY) {
						selectedKey.remove();
						selectedKey.setRectangles();
					} else if (model.isAvailablePlaceFor(selectedKey, wallStartXInd_KEY, wallStartYInd_KEY)) {
						selectedKey.xCoor = wallStartX_KEY;
						selectedKey.yCoor = wallStartY_KEY;
						selectedKey.setColorToOriginal();
						selectedKey.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(),
								model.getSquareHeight(), model.getSquareWidth());
						model.addToLines(selectedKey);
					} else {
						selectedKey.remove();
						selectedKey.setRectangles();
					}
					selectedKey = null;
				}

				if (selectedKey != null)
					setColor(selectedKey);
			}

			if (key == KeyEvent.VK_1) {
				numberPressed(1);
			}

			else if (key == KeyEvent.VK_2) {
				numberPressed(2);
			}

			else if (key == KeyEvent.VK_3) {
				numberPressed(3);
			}

			else if (key == KeyEvent.VK_4) {
				numberPressed(4);
			}

			else if (key == KeyEvent.VK_5) {
				numberPressed(5);
			}

			else if (key == KeyEvent.VK_6) {
				numberPressed(6);
			}

			else if (key == KeyEvent.VK_NUMPAD1) {
				numberPressed(1);
			}

			else if (key == KeyEvent.VK_NUMPAD2) {
				numberPressed(2);
			}

			else if (key == KeyEvent.VK_NUMPAD3) {
				numberPressed(3);
			}

			else if (key == KeyEvent.VK_NUMPAD4) {
				numberPressed(4);
			}

			else if (key == KeyEvent.VK_NUMPAD5) {
				numberPressed(5);
			}

			else if (key == KeyEvent.VK_NUMPAD6) {
				numberPressed(6);
			}

			else if (key == KeyEvent.VK_Q) {
				if (selectedKey != null) {
					selectedKey.remove();
					selectedKey = null;
				}
			}
			repaint();
		}

		private void numberPressed(int i) {

			if (selectedKey != null) {
				placeSelectedKey();
			}
			if (i > 0 && i <= model.getWalls().length && model.getWalls()[i - 1] != null
					&& model.getWalls()[i - 1] != selectedMouse) {
				if (selectedKey != model.getWalls()[i - 1]) {
					if (model.getWalls()[i - 1].visible) {
						selectedKey = model.getWalls()[i - 1];
						model.removeFromLines(selectedKey);
						wallStartX_KEY = selectedKey.xCoor;
						wallStartY_KEY = selectedKey.yCoor;
						wallStartXInd_KEY = selectedKey.xInd;
						wallStartYInd_KEY = selectedKey.yInd;
						turnStart = selectedKey.turn;
						wasInvisible_KEY = false;
					} else {
						model.getWalls()[i - 1].appear();
						int indexX = (int) ((model.getWalls()[i - 1].wallContainer.getCenterX() - model.initialXShift)
								/ model.squareWidth);
						int indexY = (int) ((model.getWalls()[i - 1].wallContainer.getCenterY() - model.initialYShift)
								/ model.squareHeight);
						model.getWalls()[i - 1].setIndexes(indexX, indexY);
						model.getWalls()[i - 1].setThePositionAgainByIndex(model.initialXShift, model.initialYShift,
								model.squareHeight, model.squareWidth);
						selectedKey = model.getWalls()[i - 1];
						wallStartXInd_KEY = selectedKey.xInd;
						wallStartYInd_KEY = selectedKey.yInd;
						wasInvisible_KEY = true;
						setColor(selectedKey);
					}
				} else {
					selectedKey = null;
				}
				repaint();
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}

		public void placeSelectedKey() {
			selectedKey.setColorToOriginal();
			selectedKey.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(),
					model.getSquareWidth());

			if (model.outOfScreen(selectedKey)) {
				selectedKey.remove();
				selectedKey.setRectangles();
			} else {
				if (!model.onAvailablePlace(selectedKey)) {
					if (wasInvisible_KEY) {
						selectedKey.remove();
						selectedKey.setRectangles();
					} else {
						selectedKey.setTurn(turnStart);
						if (model.isAvailablePlaceFor(selectedKey, wallStartXInd_KEY, wallStartYInd_KEY)) {
							selectedKey.xCoor = wallStartX_KEY;
							selectedKey.yCoor = wallStartY_KEY;
							selectedKey.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(),
									model.getSquareHeight(), model.getSquareWidth());
							model.addToLines(selectedKey);
						} else {
							selectedKey.remove();
							selectedKey.setRectangles();
						}
						/*
						 * selectedKey.xCoor = wallStartX_KEY; selectedKey.yCoor = wallStartY_KEY;
						 * selectedKey.setTurn(turnStart);
						 * selectedKey.setThePositionAgain(model.getInitialXShift(),
						 * model.getInitialYShift(), model.getSquareHeight(), model.getSquareWidth());
						 * model.addToLines(selectedKey);
						 */

					}

				} else {
					selectedKey.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(),
							model.getSquareHeight(), model.getSquareWidth());
					model.addToLines(selectedKey);
					SoundManager.wallPlaced();

					if (model.isGameFinished()) {
						gameFinished();
					}
				}
			}
			wasInvisible_KEY = false;
		}
	}

	public class TimerActionListener implements ActionListener {
		Timer t = new Timer(3000, this);

		@Override
		public void actionPerformed(ActionEvent e) {
			for (Soldier s : model.movables) {

				ArrayList<Integer> dirs = new ArrayList<Integer>();

				if (isAvailable(s, Model.UP)) {
					dirs.add(Model.UP);
				}
				if (isAvailable(s, Model.DOWN)) {
					dirs.add(Model.DOWN);
				}
				if (isAvailable(s, Model.LEFT)) {
					dirs.add(Model.LEFT);
				}
				if (isAvailable(s, Model.RIGHT)) {
					dirs.add(Model.RIGHT);
				}

				if (!dirs.isEmpty()) {
					int dir = (int) (Math.random() * dirs.size());

					move(s, dirs.get(dir));
					repaint();
				}
			}
		}

		private void move(Soldier s, int dir) {
			if (s instanceof AllyArmada || s instanceof EnemyArmada) {
				model.gameObjects[s.getX()][s.getY()] = new Lake(s.getX(), s.getY());
				model.map[2*s.getX()+1][2*s.getY()+1] = Model.LAKE;
			} else {
				model.gameObjects[s.getX()][s.getY()] = null;
				model.map[2*s.getX()+1][2*s.getY()+1] = Model.EMPTY;
			}

			if (dir == Model.UP)
				s.y--;
			if (dir == Model.DOWN)
				s.y++;
			if (dir == Model.LEFT)
				s.x--;
			if (dir == Model.RIGHT)
				s.x++;
			model.gameObjects[s.getX()][s.getY()] = s;
			model.map[2*s.getX()+1][2*s.getY()+1] = s.getWholeMapIndex();
		}

		private boolean isAvailable(Soldier s, int dir) {
			int nextX = s.getX();
			int nextY = s.getY();
			int mapX = 0;
			int mapY = 0;
			if (dir == Model.LEFT) {
				mapX = 2 * nextX;
				mapY = 2 * nextY + 1;
				nextX--;
			}
			if (dir == Model.RIGHT) {
				mapX = 2 * nextX + 2;
				mapY = 2 * nextY + 1;
				nextX++;
			}
			if (dir == Model.UP) {
				mapX = 2 * nextX + 1;
				mapY = 2 * nextY;
				nextY--;
			}
			if (dir == Model.DOWN) {
				mapX = 2 * nextX + 1;
				mapY = 2 * nextY + 2;
				nextY++;
			}

			if (!outOfScreen(nextX, nextY) && model.map[mapX][mapY] != Model.WALL_OR_CHAIN) {
				if (s instanceof AllyArmada || s instanceof EnemyArmada)
					return model.gameObjects[nextX][nextY] instanceof Lake;
				else
					return model.gameObjects[nextX][nextY] == null;
			}
			return false;
		}

		private boolean outOfScreen(int x, int y) {
			if (x == 0 && y == 0)
				return true;
			if (x == model.mapWidth - 1 && y == 0)
				return true;
			if (x == 0 && y == model.mapLength - 1)
				return true;
			if (x == model.mapWidth - 1 && y == model.mapLength - 1)
				return true;
			return !(x >= 0 && x < model.mapWidth && y >= 0 && y < model.mapLength);
		}

		public void start() {
			t.start();
		}

		public void stop() {
			t.stop();
		}
	}
}
