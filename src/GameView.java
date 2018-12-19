import javax.imageio.ImageIO;
import javax.swing.*;

import javax.swing.table.*;
import javax.swing.text.JTextComponent;

//import com.sun.org.glassfish.gmbal.GmbalMBeanNOPImpl;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
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
	MyButton[] levelButtons = { new MyButton("Level 1", "Level 1", btnSizeL, btnSizeScaledL, this),
			new MyButton("Level 2", "Level 2", btnSizeL, btnSizeScaledL, this),
			new MyButton("Level 3", "Level 3", btnSizeL, btnSizeScaledL, this),
			new MyButton("Level 4", "Level 4", btnSizeL, btnSizeScaledL, this),
			new MyButton("Level 5", "Level 5", btnSizeL, btnSizeScaledL, this) };
	MyButton music = new MyButton("Music: " + (SoundManager.playing ? "ON" : "OFF"), "Settings", btnSizeM,
			btnSizeScaledM, this);
	MyButton sound = new MyButton("Sound: " + (SoundManager.sound ? "ON" : "OFF"), "Settings", btnSizeM,
			btnSizeScaledM, this);
	MyButton setRotationAnticlockwise = new MyButton(
			"Left Rotation: \t" + KeyEvent.getKeyText(MyComponents.wallLeftRotation), "Settings", btnSizeM,
			btnSizeScaledM, this);
	MyButton setRotationClockwise = new MyButton(
			"Right Rotation: \t" + KeyEvent.getKeyText(MyComponents.wallRightRotation), "Settings", btnSizeM,
			btnSizeScaledM, this);
	MyButton setDrop = new MyButton("Wall Drop: \t" + KeyEvent.getKeyText(MyComponents.wallDrop), "Settings", btnSizeM,
			btnSizeScaledM, this);
	MyButton setPlace = new MyButton("Wall Place: \t" + KeyEvent.getKeyText(MyComponents.wallPlace), "Settings",
			btnSizeM, btnSizeScaledM, this);
	MyButton setPrevLocation = new MyButton(
			"Wall Previous Location: \t" + KeyEvent.getKeyText(MyComponents.wallPrevLocation), "Settings", btnSizeM,
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

		setMinimumSize(new Dimension(800, 600));
		managers[0] = new GameManager(this, 1, cardLayout, card);
		managers[1] = new GameManager(this, 2, cardLayout, card);

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
		setRotationAnticlockwise.setText("Left Rotation: \t" + KeyEvent.getKeyText(MyComponents.wallLeftRotation));
		setRotationClockwise.setText("Right Rotation: \t" + KeyEvent.getKeyText(MyComponents.wallRightRotation));
		setDrop.setText("Wall Drop: \t" + KeyEvent.getKeyText(MyComponents.wallDrop));
		setPlace.setText("Wall Place: \t" + KeyEvent.getKeyText(MyComponents.wallPlace));
		setPrevLocation.setText("Wall Previous Location: \t" + KeyEvent.getKeyText(MyComponents.wallPrevLocation));
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
	}

	private void createCreditsPanel() throws IOException {

		MyPanel creditsPanel = new MyPanel("Credits", "src/img/img1.jpeg");
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

		MyButton back = new MyButton("Back", "Game Menu", btnSizeS, btnSizeScaledS, this);
		creditsPanel.addBackButton(back);

		card.add("Credits", creditsPanel);
	}

	private void createHowToPanel() throws IOException {
		MyPanel howToPanel = new MyPanel("How To Play", "src/img/img1.jpeg");

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

		MyButton back = new MyButton("Back", "Game Menu", btnSizeS, btnSizeScaledS, this);

		// back.setPreferredSize(new Dimension(20, 20));

		howToPanel.addBackButton(back);

		card.add("How To Play", howToPanel);

	}

	private void createLevelMenu() {
		try {

			MyButton back = new MyButton("Back", "Game Menu", btnSizeS, btnSizeScaledS, this);
			MyPanel levelMenu = new MyPanel("Game Menu", "src/img/img1.jpeg");

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
			MyButton play = new MyButton("New Game", "Level Menu", btnSizeL, btnSizeScaledL, this);
			play.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					for (int i = 1; i < levelButtons.length; i++) {
						levelButtons[i].setEnabled(false);
						try {
							Image img = ImageIO.read(new File("src/img/locked.png"));
							Image newimg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // scale it the smooth way
							levelButtons[i].setIcon(new ImageIcon(newimg));
							Writer wr = new FileWriter("savedLevelNo.txt");
							lastCompletedLevel = 0;
			                		wr.write(GameView.lastCompletedLevel+""); // write string
			                		wr.flush();
			                		wr.close();
						} catch (Exception ex) {
							System.out.println(ex);
						}
					}
				}
			});
			MyButton loadGame = new MyButton("Load Game", "Level Menu", btnSizeL, btnSizeScaledL, this);
			loadGame.addActionListener(new ActionListener() {

 				@Override
				public void actionPerformed(ActionEvent e) {
					int last = 0;
					try {
						// code loads the game
						File f = new File("savedLevelNo.txt");
						Scanner s = new Scanner(f);
						last = (Integer) s.nextInt();

					} catch (  IOException e1) {
						e1.printStackTrace();
					}

					for(int i=0;i< last + 1;i++){
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
			
			MyButton settings = new MyButton("Settings", "Settings", btnSizeL, btnSizeScaledL, this);
			MyButton howToPlay = new MyButton("How To Play", "How To Play", btnSizeL, btnSizeScaledL, this);
			MyButton credits = new MyButton("Credits", "Credits", btnSizeL, btnSizeScaledL, this);
			MyButton quit = new MyButton("Quit", "Quit", btnSizeL, btnSizeScaledL, this);

			MyPanel gameMenu = new MyPanel("Game Menu", "src/img/img1.jpeg");

			gameMenu.addButton(play);
			gameMenu.addButton(loadGame);
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
		out.writeObject(MyComponents.wallDrop);
		out.writeObject(MyComponents.wallLeftRotation);
		out.writeObject(MyComponents.wallPlace);
		out.writeObject(MyComponents.wallPrevLocation);
		out.writeObject(MyComponents.wallRightRotation);
		out.close();
	}

	public void loadGame() throws IOException {
		// code loads the game
		FileInputStream fileIn = new FileInputStream("game.txt"); // this codes take the inforations from txt file
		ObjectInputStream in = new ObjectInputStream(fileIn);
		try {
			SoundManager.playing = ((Boolean) in.readObject());
			SoundManager.sound = ((Boolean) in.readObject());
			MyComponents.wallDrop = ((Integer) in.readObject());
			MyComponents.wallLeftRotation = ((Integer) in.readObject());
			MyComponents.wallPlace = ((Integer) in.readObject());
			MyComponents.wallPrevLocation = ((Integer) in.readObject());
			MyComponents.wallRightRotation = ((Integer) in.readObject());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		in.close();
		fileIn.close();
	}

	private void createSettingsMenu() throws IOException {
		MyPanel settingsMenu = new MyPanel("Settings", "src/img/img1.jpeg");

		MyButton back = new MyButton("Back", "Game Menu", btnSizeS, btnSizeScaledS, this);

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
		MyPanel pauseMenu = new MyPanel("Pause", "src/img/img1.jpeg");

		MyButton restart = new MyButton("Restart", "Level " + currentLevelIndex, btnSizeM, btnSizeScaledM, this);
		MyButton resume = new MyButton("Resume", "Level " + currentLevelIndex, btnSizeM, btnSizeScaledM, this);
		MyButton returnHome = new MyButton("Return Home", "Game Menu", btnSizeM, btnSizeScaledM, this);

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
		returnHome.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(card, "Game Menu");
				managers[currentLevelIndex - 1].reset();
			}
		});

		pauseMenu.addButton(resume);
		pauseMenu.addButton(restart);
		pauseMenu.addButton(returnHome);

		card.add("Pause", pauseMenu);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (((MyButton) e.getSource()).getNextPanelName().equals("Quit")) {
			System.exit(0);
		}
		cardLayout.show(card, ((MyButton) e.getSource()).getNextPanelName());
		for (int i = 0; i < managers.length; i++) {
			if (((MyButton) e.getSource()).getNextPanelName().equals("Level " + (i + 1))) {
				managers[i].getPanel().requestFocusInWindow();
				managers[i].startTimers();
				currentLevelIndex = i + 1;
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
				MyComponents.wallLeftRotation = newKey;
				setRotationAnticlockwise
						.setText("Left Rotation: \t" + KeyEvent.getKeyText(MyComponents.wallLeftRotation));
			} else if (clicked == 2) {
				MyComponents.wallRightRotation = newKey;
				setRotationClockwise
						.setText("Right Rotation: \t" + KeyEvent.getKeyText(MyComponents.wallRightRotation));
			} else if (clicked == 3) {
				MyComponents.wallDrop = newKey;
				setDrop.setText("Wall Drop: \t" + KeyEvent.getKeyText(MyComponents.wallDrop));
			} else if (clicked == 4) {
				MyComponents.wallPlace = newKey;
				setPlace.setText("Wall Place: \t" + KeyEvent.getKeyText(MyComponents.wallPlace));
			} else if (clicked == 5) {
				MyComponents.wallPrevLocation = newKey;
				setPrevLocation
						.setText("Wall Previous Location: \t" + KeyEvent.getKeyText(MyComponents.wallPrevLocation));
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

			if (pressed == MyComponents.wallLeftRotation || pressed == MyComponents.wallRightRotation
					|| pressed == MyComponents.wallDrop || pressed == MyComponents.wallPlace
					|| pressed == MyComponents.wallPrevLocation || pressed == KeyEvent.VK_1 || pressed == KeyEvent.VK_2
					|| pressed == KeyEvent.VK_3 || pressed == KeyEvent.VK_4 || pressed == KeyEvent.VK_5
					|| pressed == KeyEvent.VK_6 || pressed == KeyEvent.VK_NUMPAD1 || pressed == KeyEvent.VK_NUMPAD2
					|| pressed == KeyEvent.VK_NUMPAD3 || pressed == KeyEvent.VK_NUMPAD4
					|| pressed == KeyEvent.VK_NUMPAD5 || pressed == KeyEvent.VK_NUMPAD6 || pressed == KeyEvent.VK_UP
					|| pressed == KeyEvent.VK_DOWN || pressed == KeyEvent.VK_LEFT || pressed == KeyEvent.VK_RIGHT
					|| pressed == KeyEvent.VK_SPACE) {

				JOptionPane.showMessageDialog(null, "Entered key is in use!", "Used Key", JOptionPane.ERROR_MESSAGE);

				return true;
			} else
				return false;
		}
	}
}
