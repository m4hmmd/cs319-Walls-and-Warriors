import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;

@SuppressWarnings("serial")
public class LevelView extends JComponent {

	private Image mapTile;

	public Model model;
	private CardLayout cardLayout;
	private JPanel card;
	int levelNo;
	WallOrChain selectedMouse;
	WallOrChain selectedKey;

	JButton backButton;
	JButton pauseButton;
	JButton hintButton;
	private int pWidth = 0;
	private int pHeight = 0;
	GameView gv;
	Timer t;
	Timer timer = new Timer(500, null);

	protected static int wallLeftRotation = KeyEvent.VK_A;
	protected static int wallRightRotation = KeyEvent.VK_D;
	protected static int wallDrop = KeyEvent.VK_Q;
	protected static int wallPlace = KeyEvent.VK_ENTER;
	protected static int wallPrevLocation = KeyEvent.VK_Z;

	public LevelView(GameView gv, Model model, CardLayout cardLayout, JPanel card, int levelNo) {
		try {
			File file = new File("src/img/grass.png");
			mapTile = ImageIO.read(file);
		} catch (Exception e) {
			System.out.println("Couldn't find file: " + e);
		}
		this.model = model;
		this.cardLayout = cardLayout;
		this.card = card;
		this.levelNo = levelNo;
		this.gv = gv;
		t = new Timer(100, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int update = model.update();
				if (update == 0)
					repaint();
				else if (update == 1) {
					if (selectedMouse != null)
						selectedMouse.remove();
					Object[] options = { "Close" };
					pause();
					int n = JOptionPane.showOptionDialog(null, "Wall or Chain is under the attacking of enemies. ",
							"Hurry Up!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
							options[0]);
					resume();
				} else if (update == -1) {
					pause();
					repaint();
					Object[] options = { "Return Level Menu", "Restart" };

					int n = JOptionPane.showOptionDialog(null, "A Wall was collapsed", "Game Over",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

					if (n == JOptionPane.YES_OPTION) {
						cardLayout.show(card, "Level Menu");
						cardLayout.show(card, "Game Level Menu");
						model.reset();
					} else if (n == JOptionPane.NO_OPTION) {
						restart();
						requestFocusInWindow();
					}
				}
			}
		});

		backButton = new GameButton("Back", "Level Menu", 30, 40, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(card, "Level Menu");
				returnLevelMenu();
			}
		});

		hintButton = new GameButton("Hint", "Level " + levelNo, 30, 40, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pause();
				repaint();

				model.reset();
				// random 1
				int randomWallIndex = (levelNo -1) % model.getWalls().length ;

				FileSystem fs = new FileSystem();
				int hintXCoor = fs.getHintShape(randomWallIndex, levelNo)[0];
				int hintYCoor = fs.getHintShape(randomWallIndex, levelNo)[1];
				int hintTurn = fs.getHintShape(randomWallIndex, levelNo)[2];
				model.getWalls()[randomWallIndex].appear();
				model.getWalls()[randomWallIndex].setTurn(hintTurn);
				model.getWalls()[randomWallIndex].setIndexes(hintXCoor, hintYCoor);
				model.getWalls()[randomWallIndex].setThePositionAgainByIndex(model.initialXShift, model.initialYShift,
						model.squareHeight, model.squareWidth);
				model.addToLines(model.getWalls()[randomWallIndex]);
				model.getWalls()[randomWallIndex].setRectangles();
				model.getWalls()[randomWallIndex].setTheRectanglePoints(model.squareHeight, model.squareWidth,
						model.initialYShift);
				resume();
				hintButton.setEnabled(false);
				repaint();

			}
		});

		pauseButton = new GameButton("Pause", "Level " + levelNo, 30, 40, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pause();
				repaint();
				cardLayout.show(card, "Pause");
			}
		});
		add(hintButton);
		add(backButton);
		add(pauseButton);
		Listeners listener = new Listeners();
		requestFocusInWindow();
		addKeyListener(listener);
		addMouseListener(listener);
		addMouseMotionListener(listener);
		setMinimumSize(new Dimension(300, model.getBarShift() + 50));
		setFocusable(true);
	}

	public void restart() {
		model.reset();
		t.restart();
		hintButton.setEnabled(true);
		selectedKey = null;
		selectedMouse = null;
	}

	public void returnLevelMenu() {
		model.reset();
		stopTimer();
		hintButton.setEnabled(true);
		timer.stop();
		selectedKey = null;
		selectedMouse = null;
	}

	public void startTimer() {
		t.start();
	}

	public void stopTimer() {
		t.stop();
	}

	public void pause() {
		stopTimer();
		model.pause();
	}

	protected void resume() {
		startTimer();
		model.resume();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		try {
			g.drawImage(ImageIO.read(new File("src/img/img0.jpeg")), 0, 0, getWidth(), getHeight(), null);
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
		if (selectedKey != null)
			setColor(selectedKey);
		if (selectedMouse != null)
			setColor(selectedMouse);
		drawWalls(g);

		backButton.setBounds(0, (int) (getHeight() / 10 * 0.5), getWidth() / 5, getHeight() / 10);
		pauseButton.setBounds((int) (getWidth() / 10 * 7.5), (int) (getHeight() / 10 * 0.5), getWidth() / 5,
				getHeight() / 10);
		hintButton.setBounds((int) (getWidth() / 10 * 7.5), (int) 8*(getHeight() / 10 ), getWidth() / 5, getHeight() / 10);
		pWidth = getWidth();
		pHeight = getHeight();
	}

	private void drawGameObjects(Graphics g) {
		for (GameObject[] gm : model.getMap()) {
			for (GameObject gmo : gm) {
				if (gmo != null && !model.movables.contains(gmo))
					gmo.draw(g, model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(),
							model.getSquareWidth());
			}
		}

		for (Soldier soldier : model.movables) {
			soldier.draw(g, model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(),
					model.getSquareWidth());
		}
	}

	void drawGrid(Graphics g) {
		g.setColor(new Color(102, 59, 22));
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
					float alpha = 0.7f;
					Graphics2D g2d = (Graphics2D) g;
					AlphaComposite acomp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
					g2d.setComposite(acomp);
					if (mapTile != null)
						g2d.drawImage(mapTile, model.initialXShift + model.squareWidth * i,
								model.initialYShift + model.squareHeight * j, model.squareWidth + 1,
								model.squareHeight + 1, null);

					alpha = 1f;
					acomp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
					g2d.setComposite(acomp);
				}
			}
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
		int turnStartKey;
		int turnStartMouse;
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
					else if (SwingUtilities.isRightMouseButton(e))
						model.getWalls()[i].turnRight();
					model.getWalls()[i].setRectangles();
					emptyPlace = false;
				}
				i++;
			}
			repaint();
			if (SwingUtilities.isRightMouseButton(e)) {
				return;
			}
			if (SwingUtilities.isLeftMouseButton(e)) {
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
							turnStartKey = selectedKey.turn;
						} else
							selectedKey = null;
						emptyPlace = false;
						break;
					}
				}
			}
			if (emptyPlace) {
				placeTheWall(selectedKey);
				selectedKey = null;
			}

			repaint();
		}

		public void mousePressed(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				if (selectedMouse != null) {
					selectedMouse.turnLeft();
					selectedMouse.xCoor = (int) (wallStartX_MOUSE + e.getX() - clickedX
							- selectedMouse.centerX * model.getSquareWidth());
					selectedMouse.yCoor = (int) (wallStartY_MOUSE + e.getY() - clickedY
							- selectedMouse.centerY * model.getSquareHeight());

					setColor(selectedMouse);
				}
				repaint();
				return;
			} else {
				clickedX = e.getX();
				clickedY = e.getY();
				boolean isWall = false;
				for (WallOrChain w : model.getWalls()) {
					if (!w.collapsed) {
						if (w != selectedKey && w.visible && w.contains(e.getX(), e.getY())) {
							selectedMouse = w;
							model.removeFromLines(selectedMouse);
							wallStartX_MOUSE = (int) (e.getX());
							wallStartY_MOUSE = (int) (e.getY());
							wallStartXInd_MOUSE = selectedMouse.xInd;
							wallStartYInd_MOUSE = selectedMouse.yInd;
							turnStartMouse = selectedMouse.turn;
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
							turnStartMouse = selectedMouse.turn;
							isWall = true;
							break;
						}
					}
				}

				if (!isWall) {
					for (int i = 0; i < model.getWalls().length; i++) {

						if (!model.getWalls()[i].collapsed) {
							if (model.getWalls()[i].wallContainer.contains(clickedX, clickedY)
									&& !model.getWalls()[i].visible) {
								selectedMouse = model.getWalls()[i];
								indexOfGreenSquare = i;
								wallStartX_MOUSE = (int) model.getWalls()[i].wallContainer.getCenterX();
								wallStartY_MOUSE = (int) model.getWalls()[i].wallContainer.getCenterY();
								wasInvisible_MOUSE = true;
							}
						}
					}
				}
			}
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				return;
			}
			if (selectedMouse != null) {
				if (!selectedMouse.visible)
					selectedMouse.appear();
				selectedMouse.xCoor = (int) (wallStartX_MOUSE + e.getX() - clickedX
						- selectedMouse.centerX * model.getSquareWidth());
				selectedMouse.yCoor = (int) (wallStartY_MOUSE + e.getY() - clickedY
						- selectedMouse.centerY * model.getSquareHeight());
				setColor(selectedMouse);
			}
			repaint();
		}

		public void mouseReleased(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				return;
			}
			if (selectedMouse != null) {
				selectedMouse.setColorToOriginal();
				selectedMouse.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(),
						model.getSquareHeight(), model.getSquareWidth());

				if (model.outOfScreen(selectedMouse)) {
					selectedMouse.remove();
					selectedMouse.setRectangles();
					selectedMouse = null;
				} else {
					if (!model.onAvailablePlace(selectedMouse)) {
						if (wasInvisible_MOUSE) {
							selectedMouse.remove();
							selectedMouse.setRectangles();
							selectedMouse = null;
						} else {
							selectedMouse.setTurn(turnStartMouse);
							if (model.isAvailablePlaceFor(selectedMouse, wallStartXInd_MOUSE, wallStartYInd_MOUSE)) {
								selectedMouse.xInd = wallStartXInd_MOUSE;
								selectedMouse.yInd = wallStartYInd_MOUSE;
								selectedMouse.setThePositionAgainByIndex(model.getInitialXShift(),
										model.getInitialYShift(), model.getSquareHeight(), model.getSquareWidth());
								model.addToLines(selectedMouse);
								selectedMouse = null;
							} else {
								for (int i = 0; i < timer.getActionListeners().length; i++) {
									timer.removeActionListener(timer.getActionListeners()[i]);
								}

								timer.addActionListener(new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent e) {
										selectedMouse.remove();
										selectedMouse = null;
										timer.stop();
									}
								});

								selectedMouse.setColor(Color.RED);
								selectedMouse.setIndexes(wallStartXInd_MOUSE, wallStartYInd_MOUSE);
								selectedMouse.setTurn(turnStartMouse);
								selectedMouse.setThePositionAgainByIndex(model.initialXShift, model.initialYShift,
										model.squareHeight, model.squareWidth);
								timer.start();

							}
						}

					} else {
						selectedMouse.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(),
								model.getSquareHeight(), model.getSquareWidth());
						model.addToLines(selectedMouse);
						SoundManager.wallPlaced();

						if (model.isGameFinished()) {
							selectedMouse = null;
							selectedKey = null;
							gameFinished();
						}
						selectedMouse = null;
					}
				}
			}
			wasInvisible_MOUSE = false;
			if (!timer.isRunning())
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
							wall.setTurn(turnStartKey);
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
			stopTimer();
			model.stopTimers();
			SoundManager.gameWon();

			if (GameView.lastCompletedLevel < levelNo)
				GameView.lastCompletedLevel = levelNo;
			if (gv.managers.length == GameView.lastCompletedLevel) {

				Object[] options = { "Return Level Menu" };
				int n = JOptionPane.showOptionDialog(null, "You have finished the game!", "Congratulations!",
						JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (n == JOptionPane.OK_OPTION) {
					cardLayout.show(card, "Level Menu");
					model.reset();
				}
				selectedKey = null;
				selectedMouse = null;
			} else {

				Object[] options = { "Return Level Menu", "Next Level" };
				int n = JOptionPane.showOptionDialog(null, "You have completed this level!", "Congratulations!",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

				gv.levelButtons[levelNo].setEnabled(true);
				gv.levelButtons[levelNo].setIcon(null);
				if (n == JOptionPane.YES_OPTION) {
					cardLayout.show(card, "Level Menu");
					model.reset();
				} else if (n == JOptionPane.NO_OPTION) {
					cardLayout.show(card, "Level " + (levelNo + 1));
					model.reset();
					gv.managers[levelNo].startTimers();
					gv.showDescription(levelNo + 1);
				}
				selectedKey = null;
				selectedMouse = null;
			}

			FileSystem fs = new FileSystem();
			fs.writeSavedLevelNo(GameView.lastCompletedLevel);
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

				else if (key == wallPlace) {
					if (selectedKey != null) {
						placeSelectedKey();
					}
					wasInvisible_KEY = false;
					repaint();
				}

				else if (key == wallRightRotation) {
					selectedKey.turnRight();
					selectedKey.setThePositionAgainByIndex(model.initialXShift, model.initialYShift, model.squareHeight,
							model.squareWidth);
					selectedKey.setRectangles();
				}

				else if (key == wallLeftRotation) {
					selectedKey.turnLeft();
					selectedKey.setThePositionAgainByIndex(model.initialXShift, model.initialYShift, model.squareHeight,
							model.squareWidth);
					selectedKey.setRectangles();
				}

				else if (key == wallPrevLocation) {
					int turn = selectedKey.turn;
					Color prevColor = selectedKey.c;
					if (!wasInvisible_KEY) {
						selectedKey.setTurn(turnStartKey);
					}
					if (wasInvisible_KEY) {
						selectedKey.remove();
						selectedKey.setRectangles();
						selectedKey = null;
					} else if (model.isAvailablePlaceFor(selectedKey, wallStartXInd_KEY, wallStartYInd_KEY)) {
						selectedKey.xCoor = wallStartX_KEY;
						selectedKey.yCoor = wallStartY_KEY;
						selectedKey.setColorToOriginal();
						selectedKey.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(),
								model.getSquareHeight(), model.getSquareWidth());
						model.addToLines(selectedKey);
						selectedKey = null;
					} else {
						Point startPoint = new Point(selectedKey.xInd, selectedKey.yInd);

						for (int i = 0; i < timer.getActionListeners().length; i++) {
							timer.removeActionListener(timer.getActionListeners()[i]);
						}

						timer.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								selectedKey.setIndexes(startPoint.x, startPoint.y);
								selectedKey.setTurn(turn);
								selectedKey.setThePositionAgainByIndex(model.initialXShift, model.initialYShift,
										model.squareHeight, model.squareWidth);
								selectedKey.setColor(prevColor);
								timer.stop();
							}
						});

						selectedKey.setColor(Color.RED);
						selectedKey.setIndexes(wallStartXInd_KEY, wallStartYInd_KEY);
						selectedKey.setTurn(turnStartKey);
						selectedKey.setThePositionAgainByIndex(model.initialXShift, model.initialYShift,
								model.squareHeight, model.squareWidth);
						timer.start();
					}
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

			else if (key == KeyEvent.VK_7) {
				numberPressed(7);
			}

			else if (key == KeyEvent.VK_8) {
				numberPressed(8);
			}

			else if (key == KeyEvent.VK_9) {
				numberPressed(9);
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

			else if (key == KeyEvent.VK_NUMPAD7) {
				numberPressed(7);
			}

			else if (key == KeyEvent.VK_NUMPAD8) {
				numberPressed(8);
			}

			else if (key == KeyEvent.VK_NUMPAD9) {
				numberPressed(9);
			}

			else if (key == KeyEvent.VK_ESCAPE) {
				pause();
				repaint();
				cardLayout.show(card, "Pause");
			}

			else if (key == wallDrop) {
				if (selectedKey != null) {
					selectedKey.remove();
					selectedKey = null;
				}
			}
			repaint();
		}

		private void numberPressed(int i) {
			if (i > model.getWalls().length || i <= 0)
				return;
			boolean bool = selectedKey != model.getWalls()[i - 1];

			if (selectedKey != null) {
				placeSelectedKey();
			}
			if (selectedKey == null) {
				if (i > 0 && i <= model.getWalls().length && model.getWalls()[i - 1] != null
						&& model.getWalls()[i - 1] != selectedMouse && !model.getWalls()[i - 1].collapsed) {
					if (bool) {
						if (model.getWalls()[i - 1].visible) {
							selectedKey = model.getWalls()[i - 1];
							model.removeFromLines(selectedKey);
							wallStartX_KEY = selectedKey.xCoor;
							wallStartY_KEY = selectedKey.yCoor;
							wallStartXInd_KEY = selectedKey.xInd;
							wallStartYInd_KEY = selectedKey.yInd;
							turnStartKey = selectedKey.turn;
							wasInvisible_KEY = false;
						} else {
							model.getWalls()[i - 1].appear();
							int indexX = (int) ((model.getWalls()[i - 1].wallContainer.getCenterX()
									- model.initialXShift) / model.squareWidth);
							int indexY = (int) ((model.getWalls()[i - 1].wallContainer.getCenterY()
									- model.initialYShift) / model.squareHeight);
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
			Color prevColor = selectedKey.c;
			selectedKey.setColorToOriginal();
			selectedKey.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(), model.getSquareHeight(),
					model.getSquareWidth());

			if (model.outOfScreen(selectedKey)) {
				selectedKey.remove();
				selectedKey.setRectangles();
				selectedKey = null;
			} else {
				if (!model.onAvailablePlace(selectedKey)) {
					if (wasInvisible_KEY) {
						selectedKey.remove();
						selectedKey.setRectangles();
						selectedKey = null;
					} else {
						int turn = selectedKey.turn;
						selectedKey.setTurn(turnStartKey);
						if (model.isAvailablePlaceFor(selectedKey, wallStartXInd_KEY, wallStartYInd_KEY)) {
							selectedKey.xCoor = wallStartX_KEY;
							selectedKey.yCoor = wallStartY_KEY;
							selectedKey.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(),
									model.getSquareHeight(), model.getSquareWidth());
							model.addToLines(selectedKey);
							selectedKey = null;
						} else {
							Point startPoint = new Point(selectedKey.xInd, selectedKey.yInd);

							for (int i = 0; i < timer.getActionListeners().length; i++) {
								timer.removeActionListener(timer.getActionListeners()[i]);
							}

							timer.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									selectedKey.setIndexes(startPoint.x, startPoint.y);
									selectedKey.setTurn(turn);
									selectedKey.setThePositionAgainByIndex(model.initialXShift, model.initialYShift,
											model.squareHeight, model.squareWidth);
									selectedKey.setColor(prevColor);
									timer.stop();
								}
							});

							selectedKey.setColor(Color.RED);
							selectedKey.setIndexes(wallStartXInd_KEY, wallStartYInd_KEY);
							selectedKey.setTurn(turnStartKey);
							selectedKey.setThePositionAgainByIndex(model.initialXShift, model.initialYShift,
									model.squareHeight, model.squareWidth);
							timer.start();
						}
					}

				} else {
					selectedKey.setThePositionAgain(model.getInitialXShift(), model.getInitialYShift(),
							model.getSquareHeight(), model.getSquareWidth());
					model.addToLines(selectedKey);
					SoundManager.wallPlaced();

					if (model.isGameFinished()) {
						gameFinished();
					}
					selectedKey = null;
				}
			}
			wasInvisible_KEY = false;
		}
	}

	private void setColor(WallOrChain w) {
		if (w.visible) {
			w.setThePositionAgain(model.initialXShift, model.initialYShift, model.squareHeight, model.squareWidth);
			if (model.outOfScreen(w)) {
				w.setColor(Color.BLACK);
			} else if (!model.onAvailablePlace(w)) {
				w.setColor(Color.RED);
			} else {
				w.setColorToOriginal();
			}
		} else
			w.setColorToOriginal();
	}
}
