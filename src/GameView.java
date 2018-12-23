import javax.imageio.ImageIO;
import javax.swing.*;

//import com.sun.org.glassfish.gmbal.GmbalMBeanNOPImpl;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class GameView extends JFrame implements ActionListener {

	SoundManager sm = new SoundManager();
	private static int newKey;
	private static int clicked = 0;
	private boolean paused = false;

	static int lastCompletedLevel = 0;
	int currentLevelIndex = 0;

	int btnSizeL = 40, btnSizeScaledL = 45, btnSizeM = 30, btnSizeScaledM = 35, btnSizeS = 25, btnSizeScaledS = 30;

	CardLayout cardLayout;
	JPanel card = new JPanel();
	GameManager managers[] = new GameManager[5];
	GameButton[] levelButtons = { new GameButton("Level 1", "Level 1", btnSizeL, btnSizeScaledL, this),
			new GameButton("Level 2", "Level 2", btnSizeL, btnSizeScaledL, this),
			new GameButton("Level 3", "Level 3", btnSizeL, btnSizeScaledL, this),
			new GameButton("Level 4", "Level 4", btnSizeL, btnSizeScaledL, this),
			new GameButton("Level 5", "Level 5", btnSizeL, btnSizeScaledL, this) };
	GameButton music = new GameButton("Music: " + (SoundManager.playing ? "ON" : "OFF"), "Settings", btnSizeM,
			btnSizeScaledM, this);
	GameButton sound = new GameButton("Sound: " + (SoundManager.sound ? "ON" : "OFF"), "Settings", btnSizeM, btnSizeScaledM,
			this);
	GameButton setRotationAnticlockwise = new GameButton(
			"Left Rotation: \t" + KeyEvent.getKeyText(LevelView.wallLeftRotation), "Settings", btnSizeM,
			btnSizeScaledM, this);
	GameButton setRotationClockwise = new GameButton(
			"Right Rotation: \t" + KeyEvent.getKeyText(LevelView.wallRightRotation), "Settings", btnSizeM,
			btnSizeScaledM, this);
	GameButton setDrop = new GameButton("Wall Drop: \t" + KeyEvent.getKeyText(LevelView.wallDrop), "Settings", btnSizeM,
			btnSizeScaledM, this);
	GameButton setPlace = new GameButton("Wall Place: \t" + KeyEvent.getKeyText(LevelView.wallPlace), "Settings",
			btnSizeM, btnSizeScaledM, this);
	GameButton setPrevLocation = new GameButton(
			"Wall Previous Location: \t" + KeyEvent.getKeyText(LevelView.wallPrevLocation), "Settings", btnSizeM,
			btnSizeScaledM, this);

	boolean listenKey;

	public GameView() throws IOException {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				try {
					saveGame();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));
		card.setLayout(cardLayout = new CardLayout());

		setMinimumSize(new Dimension(1024, 576));
		for (int i = 0; i < managers.length; i++) {
			managers[i] = new GameManager(this, i + 1, cardLayout, card);
		}

		createPanels();

		for (int i = 1; i < levelButtons.length; i++) {
			levelButtons[i].setEnabled(false);
			try {
				Image img = ImageIO.read(new File("src/img/locked.png"));
				Image newimg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // scale it the smooth way
				levelButtons[i].setIcon(new ImageIcon(newimg));
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}

		cardLayout.show(card, "Game Menu");
		add(card);
		loadGame();
		music.setText("Music: " + (SoundManager.playing ? "ON" : "OFF"));
		sound.setText("Sound: " + (SoundManager.sound ? "ON" : "OFF"));
		setRotationAnticlockwise.setText("Left Rotation: \t" + KeyEvent.getKeyText(LevelView.wallLeftRotation));
		setRotationClockwise.setText("Right Rotation: \t" + KeyEvent.getKeyText(LevelView.wallRightRotation));
		setDrop.setText("Wall Drop: \t" + KeyEvent.getKeyText(LevelView.wallDrop));
		setPlace.setText("Wall Place: \t" + KeyEvent.getKeyText(LevelView.wallPlace));
		setPrevLocation.setText("Wall Previous Location: \t" + KeyEvent.getKeyText(LevelView.wallPrevLocation));
		SoundManager.start();
	}

	private void createPanels() throws IOException {
		createGameMenu();
		createPauseMenu();
		createSettingsMenu();
		createLevelMenu();
		createHowToPanel();
		createCreditsPanel();
		createLevelPanels();
	}

	private void createLevelPanels() {
		card.add("Level 1", managers[0].getPanel());
		card.add("Level 2", managers[1].getPanel());
		card.add("Level 3", managers[2].getPanel());
		card.add("Level 4", managers[3].getPanel());
		card.add("Level 5", managers[4].getPanel());
	}

	private void createCreditsPanel() throws IOException {

		GamePanel creditsPanel = new GamePanel("Credits", "src/img/img1.jpeg");
		JLabel credits = new JLabel(
				"Creators:\nAhmet Malal\nHuseyn Allahyarov\nIbrahim Mammadov\n Mahammad Shirinov\n Samet Demir");
		credits.setFont(new Font("Times", 1, 30));
		credits.setForeground(Color.WHITE);
		credits.setBounds(0, 0, getWidth(), getHeight());
		credits.setVisible(true);
		creditsPanel.addLabel(new JLabel("Contributors:"));
		creditsPanel.addLabel(new JLabel(""));
		creditsPanel.addLabel(new JLabel("Ahmet Malal"));
		creditsPanel.addLabel(new JLabel("Huseyn Allahyarov"));
		creditsPanel.addLabel(new JLabel("Ibrahim Mammadov"));
		creditsPanel.addLabel(new JLabel("Mahammad Shirinov"));
		creditsPanel.addLabel(new JLabel("Samet Demir"));

		GameButton back = new GameButton("Back", "Game Menu", btnSizeS, btnSizeScaledS, this);
		creditsPanel.addBackButton(back);

		card.add("Credits", creditsPanel);
	}

	private void createHowToPanel() throws IOException {
		GamePanel howToPanel = new GamePanel("How To Play", "src/img/img1.jpeg");

		JLabel howToPlay = new JLabel(
				"<html><center><h1>HOW TO PLAY</h1></center><br>" + "<u><h2>Description of the Game</h2></u><br>"
						+ "Walls & Warriors is a board game played with warrior figures <br>"
						+ "and walls placed under specific rules. The goal of this game place the <br>"
						+ "four walls on the game board so that all the blue knights are inside the <br>"
						+ "enclosure and all the red knights are on the outside to defend the castle <br>"
						+ "to be left inside the walls.<br><br>" + "<u><h2>Gameplay</h2></u><br>"
						+ "Given a board with red and blue soldiers and, on higher levels, lakes and <br>"
						+ "special soldiers, the player needs to select one of the provided wall shapes, <br>"
						+ "rotate it as needed and put onto the board, so as to complete the castle <br>"
						+ "with all blue knights inside and red ones outside.</html>");
		howToPlay.setFont(new Font("Times", 1, 30));
		howToPlay.setForeground(Color.WHITE);
		howToPlay.setBounds(0, 0, getWidth(), getHeight());
		howToPlay.setVisible(true);
		howToPanel.addOnlyOneLabel(howToPlay);

		GameButton back = new GameButton("Back", "Game Menu", btnSizeS, btnSizeScaledS, this);

		// back.setPreferredSize(new Dimension(20, 20));

		howToPanel.addBackButton(back);

		card.add("How To Play", howToPanel);

	}

	private void createLevelMenu() {
		try {

			GameButton back = new GameButton("Back", "Game Menu", btnSizeS, btnSizeScaledS, this);
			GamePanel levelMenu = new GamePanel("Game Menu", "src/img/img1.jpeg");

			for (int i = 0; i < levelButtons.length; i++) {
				levelMenu.addButton(levelButtons[i]);
			}

			levelMenu.addBackButton(back);
			card.add("Level Menu", levelMenu);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createGameMenu() {
		try {
			GameButton play = new GameButton("Play", "Level Menu", btnSizeL, btnSizeScaledL, this);
			play.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					FileSystem savedFs = new FileSystem();
					int last = savedFs.getSavedLevelNo();

					for (int i = 0; i < (last + 1 < levelButtons.length ? last + 1 : last); i++) {
						levelButtons[i].setIcon(null);
						levelButtons[i].setEnabled(true);
					}

					for (int i = last + 1; i < levelButtons.length; i++) {
						levelButtons[i].setEnabled(false);
						try {
							Image img = ImageIO.read(new File("src/img/locked.png"));
							Image newimg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // scale it the smooth way
							levelButtons[i].setIcon(new ImageIcon(newimg));
						} catch (Exception ex) {
							System.out.println(ex);
						}
					}
				}
			});

			GameButton settings = new GameButton("Settings", "Settings", btnSizeL, btnSizeScaledL, this);
			GameButton howToPlay = new GameButton("How To Play", "How To Play", btnSizeL, btnSizeScaledL, this);
			GameButton credits = new GameButton("Credits", "Credits", btnSizeL, btnSizeScaledL, this);
			GameButton quit = new GameButton("Quit", "Quit", btnSizeL, btnSizeScaledL, this);

			GamePanel gameMenu = new GamePanel("Game Menu", "src/img/img1.jpeg");

			// gameMenu.addButton(play);
			gameMenu.addButton(play);
			gameMenu.addButton(settings);
			gameMenu.addButton(howToPlay);
			gameMenu.addButton(credits);
			gameMenu.addButton(quit);

			card.add("Game Menu", gameMenu);
		} catch (Exception e) {

		}
	}

	public void saveGame() throws IOException {
		// code saves the game
		FileOutputStream fileOut = new FileOutputStream("game.txt");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(SoundManager.playing);
		out.writeObject(SoundManager.sound);
		out.writeObject(LevelView.wallDrop);
		out.writeObject(LevelView.wallLeftRotation);
		out.writeObject(LevelView.wallPlace);
		out.writeObject(LevelView.wallPrevLocation);
		out.writeObject(LevelView.wallRightRotation);
		out.close();
	}

	public void loadGame() {
		try {
			FileInputStream fileIn = new FileInputStream("game.txt"); // this codes take the inforations from txt file
			ObjectInputStream in = new ObjectInputStream(fileIn);
			SoundManager.playing = ((Boolean) in.readObject());
			SoundManager.sound = ((Boolean) in.readObject());
			LevelView.wallDrop = ((Integer) in.readObject());
			LevelView.wallLeftRotation = ((Integer) in.readObject());
			LevelView.wallPlace = ((Integer) in.readObject());
			LevelView.wallPrevLocation = ((Integer) in.readObject());
			LevelView.wallRightRotation = ((Integer) in.readObject());
			in.close();
			fileIn.close();
		} catch (ClassNotFoundException e1) {
			return;
		} catch (IOException e1) {
			return;
		}
	}

	private void createSettingsMenu() throws IOException {
		GamePanel settingsMenu = new GamePanel("Settings", "src/img/img1.jpeg");

		GameButton back = new GameButton("Back", "Game Menu", btnSizeS, btnSizeScaledS, this);

		setRotationAnticlockwise.addActionListener(new CustomizeRotationAnticlockwiseButton());
		setRotationClockwise.addActionListener(new CustomizeRotationClockwiseButton());
		setDrop.addActionListener(new CustomizeDropButton());
		setPlace.addActionListener(new CustomizePlaceButton());
		setPrevLocation.addActionListener(new CustomizePrevLocationButton());

		setRotationAnticlockwise.addKeyListener(new customizeKeys());
		setRotationClockwise.addKeyListener(new customizeKeys());
		setDrop.addKeyListener(new customizeKeys());
		setPlace.addKeyListener(new customizeKeys());
		setPrevLocation.addKeyListener(new customizeKeys());

		music.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SoundManager.switchPlay();
				music.setText("Music: " + (SoundManager.playing ? "ON" : "OFF"));
			}
		});

		sound.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SoundManager.switchSound();
				sound.setText("Sound: " + (SoundManager.sound ? "ON" : "OFF"));
			}
		});

		settingsMenu.addButton(music);
		settingsMenu.addButton(sound);
		settingsMenu.addButton(setRotationAnticlockwise);
		settingsMenu.addButton(setRotationClockwise);
		settingsMenu.addButton(setDrop);
		settingsMenu.addButton(setPlace);
		settingsMenu.addButton(setPrevLocation);

		settingsMenu.addBackButton(back);

		card.add("Settings", settingsMenu);
	}

	private void createPauseMenu() throws IOException {
		GamePanel pauseMenu = new GamePanel("Pause", "src/img/img1.jpeg");

		GameButton restart = new GameButton("Restart", "Level " + currentLevelIndex, btnSizeM, btnSizeScaledM, this);
		GameButton resume = new GameButton("Resume", "Level " + currentLevelIndex, btnSizeM, btnSizeScaledM, this);
		GameButton returnLevelMenu = new GameButton("Return Level Menu", "Level Menu", btnSizeM, btnSizeScaledM, this);

		restart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				managers[currentLevelIndex - 1].reset();
				managers[currentLevelIndex - 1].startTimers();
				cardLayout.show(card, "Level " + currentLevelIndex);
				managers[currentLevelIndex - 1].getPanel().requestFocusInWindow();
			}
		});
		resume.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				managers[currentLevelIndex - 1].startTimers();
				cardLayout.show(card, "Level " + currentLevelIndex);
				managers[currentLevelIndex - 1].getPanel().requestFocusInWindow();
			}
		});
		returnLevelMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(card, "Game Menu");
				managers[currentLevelIndex - 1].reset();
			}
		});

		pauseMenu.addButton(resume);
		pauseMenu.addButton(restart);
		pauseMenu.addButton(returnLevelMenu);

		card.add("Pause", pauseMenu);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (((GameButton) e.getSource()).getNextPanelName().equals("Quit")) {
			System.exit(0);
		}
		cardLayout.show(card, ((GameButton) e.getSource()).getNextPanelName());

		for (int i = 0; i < managers.length; i++) {
			if (((GameButton) e.getSource()).getNextPanelName().equals("Level " + (i + 1))) {
				managers[i].getPanel().requestFocusInWindow();
				managers[i].startTimers();
				currentLevelIndex = i + 1;

				// description of Levels
				showDescription(currentLevelIndex);

			}
		}
	}

	public class CustomizeRotationAnticlockwiseButton implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			clicked = 1;
			listenKey = true;
		}
	}

	public class CustomizeRotationClockwiseButton implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			clicked = 2;
			listenKey = true;
		}
	}

	public class CustomizeDropButton implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			clicked = 3;
			listenKey = true;
		}
	}

	public class CustomizePlaceButton implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			clicked = 4;
			listenKey = true;
		}
	}

	public class CustomizePrevLocationButton implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			clicked = 5;
			listenKey = true;
		}
	}

	public void run() throws IOException {
		setVisible(true);
		setBounds(100, 100, 800, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) throws IOException {
		try {
			GameView gameView = new GameView();
			gameView.run();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public class customizeKeys implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			if (!listenKey || checkKey(e))
				return;

			newKey = e.getKeyCode();
			if (clicked == 1) {
				LevelView.wallLeftRotation = newKey;
				setRotationAnticlockwise
						.setText("Left Rotation: \t" + KeyEvent.getKeyText(LevelView.wallLeftRotation));
			} else if (clicked == 2) {
				LevelView.wallRightRotation = newKey;
				setRotationClockwise
						.setText("Right Rotation: \t" + KeyEvent.getKeyText(LevelView.wallRightRotation));
			} else if (clicked == 3) {
				LevelView.wallDrop = newKey;
				setDrop.setText("Wall Drop: \t" + KeyEvent.getKeyText(LevelView.wallDrop));
			} else if (clicked == 4) {
				LevelView.wallPlace = newKey;
				setPlace.setText("Wall Place: \t" + KeyEvent.getKeyText(LevelView.wallPlace));
			} else if (clicked == 5) {
				LevelView.wallPrevLocation = newKey;
				setPrevLocation
						.setText("Wall Previous Location: \t" + KeyEvent.getKeyText(LevelView.wallPrevLocation));
			}
			listenKey = false;
			clicked = 0;
		}

		@Override
		public void keyReleased(KeyEvent e) {

		}

		@Override
		public void keyTyped(KeyEvent e) {

		}

		public boolean checkKey(KeyEvent e) {
			int pressed = e.getKeyCode();

			if (pressed == LevelView.wallLeftRotation || pressed == LevelView.wallRightRotation
					|| pressed == LevelView.wallDrop || pressed == LevelView.wallPlace
					|| pressed == LevelView.wallPrevLocation || pressed == KeyEvent.VK_1 || pressed == KeyEvent.VK_2
					|| pressed == KeyEvent.VK_3 || pressed == KeyEvent.VK_4 || pressed == KeyEvent.VK_5
					|| pressed == KeyEvent.VK_6 || pressed == KeyEvent.VK_7 || pressed == KeyEvent.VK_8
					|| pressed == KeyEvent.VK_9 || pressed == KeyEvent.VK_NUMPAD1 || pressed == KeyEvent.VK_NUMPAD2
					|| pressed == KeyEvent.VK_NUMPAD3 || pressed == KeyEvent.VK_NUMPAD4
					|| pressed == KeyEvent.VK_NUMPAD5 || pressed == KeyEvent.VK_NUMPAD6
					|| pressed == KeyEvent.VK_NUMPAD7 || pressed == KeyEvent.VK_NUMPAD8
					|| pressed == KeyEvent.VK_NUMPAD9 || pressed == KeyEvent.VK_UP || pressed == KeyEvent.VK_DOWN
					|| pressed == KeyEvent.VK_LEFT || pressed == KeyEvent.VK_RIGHT || pressed == KeyEvent.VK_ESCAPE) {

				JOptionPane.showMessageDialog(null, "Entered key is in use!", "Used Key", JOptionPane.ERROR_MESSAGE);

				return true;
			} else
				return false;
		}
	}

	public void showDescription(int levelNo) {
		if (levelNo == 1) {
			repaint();
			Object[] options = { "Close" };
			int n = JOptionPane.showOptionDialog(null, "Surround your knights and castle with the walls", "Level 1",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		}
		if (levelNo == 2) {
			repaint();
			Object[] options = { "Close" };
			int n1 = JOptionPane.showOptionDialog(null, "- Be careful! Some enemies are moving\n"
					+ "- Use chains to protect your ships in the lake!\n"
					+ "- You cannot place a wall or a chain on the edge of the lake\n"
					+ "- Walls also cannot cut through forests!", "Level 2",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		}
		if (levelNo == 3) {
			repaint();
		}
		if (levelNo == 4) {
			repaint();
		}
		if (levelNo == 5) {
			repaint();
		}

		currentLevelIndex = levelNo;
	}
}
