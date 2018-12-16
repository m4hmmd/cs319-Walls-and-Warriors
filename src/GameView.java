import javax.imageio.ImageIO;
import javax.swing.*;

import javax.swing.table.*;
import javax.swing.text.JTextComponent;

//import com.sun.org.glassfish.gmbal.GmbalMBeanNOPImpl;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameView extends JFrame implements ActionListener {

	SoundManager sm = new SoundManager();
	private static int newKey;
	private static int clicked = 0;

	static int lastCompletedLevel = 0;
	int currentLevelIndex = 0;

	int btnSizeL = 40, btnSizeScaledL = 50, btnSizeS = 25, btnSizeScaledS = 30;

	CardLayout cardLayout;
	JPanel card = new JPanel();
	GameManager managers[] = new GameManager[5];
	MyButton[] levelButtons = { new MyButton("Level 1", "Level 1", btnSizeL, btnSizeScaledL, this),
			new MyButton("Level 2", "Level 2", btnSizeL, btnSizeScaledL, this),
			new MyButton("Level 3", "Level 3", btnSizeL, btnSizeScaledL, this),
			new MyButton("Level 4", "Level 4", btnSizeL, btnSizeScaledL, this),
			new MyButton("Level 5", "Level 5", btnSizeL, btnSizeScaledL, this) };

	public GameView() throws IOException {
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
		addWindowListener(new SoundManager.wListener());
		
		cardLayout.show(card, "Game Menu");
		add(card);
	}

	private void createPanels() throws IOException {
		createGameMenu();
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
			MyButton play = new MyButton("Play", "Level Menu", btnSizeL, btnSizeScaledL, this);
			MyButton settings = new MyButton("Settings", "Settings", btnSizeL, btnSizeScaledL, this);
			MyButton howToPlay = new MyButton("How To Play", "How To Play", btnSizeL, btnSizeScaledL, this);
			MyButton credits = new MyButton("Credits", "Credits", btnSizeL, btnSizeScaledL, this);
			MyButton quit = new MyButton("Quit", "Quit", btnSizeL, btnSizeScaledL, this);

			MyPanel gameMenu = new MyPanel("Game Menu", "src/img/img1.jpeg");

			gameMenu.addButton(play);
			gameMenu.addButton(settings);
			gameMenu.addButton(howToPlay);
			gameMenu.addButton(credits);
			gameMenu.addButton(quit);

			card.add("Game Menu", gameMenu);
		} catch (Exception e) {

		}
	}
	
	private void createSettingsMenu() throws IOException {		
		MyPanel settingsMenu = new MyPanel("Settings", "src/img/img1.jpeg");
		
		JLabel soundSettings = new JLabel("<html><h1>Sound Settings</h1></html>");
		JLabel keyboardSettings = new JLabel("<html><h1>Keyboard Customizations</h1></html>");
		
		MyButton switchMute = new MyButton("Mute", "Settings", btnSizeL, btnSizeScaledL, this);
		
		MyButton setRotationAnticlockwise = new MyButton("Wall Rotation Anticlockwise", "Settings", btnSizeL, btnSizeScaledL, this);
		MyButton setRotationClockwise = new MyButton("Wall Rotation Clockwise", "Settings", btnSizeL, btnSizeScaledL, this);
		MyButton setDrop = new MyButton("Wall Drop", "Settings", btnSizeL, btnSizeScaledL, this);
		MyButton setPlace = new MyButton("Wall Place", "Settings", btnSizeL, btnSizeScaledL, this);;
		MyButton setPrevLocation = new MyButton("Wall Previous Location", "Settings", btnSizeL, btnSizeScaledL, this);
		MyButton back = new MyButton("Back", "Game Menu", btnSizeS, btnSizeScaledS, this);
		
		setRotationAnticlockwise.addMouseListener(new CustomizeRotationAnticlockwiseButton());
		setRotationClockwise.addMouseListener(new CustomizeRotationClockwiseButton());
		setDrop.addMouseListener(new CustomizeDropButton());
		setPlace.addMouseListener(new CustomizePlaceButton());
		setPrevLocation.addMouseListener(new CustomizePrevLocationButton());
		
		setRotationAnticlockwise.addKeyListener(new customizeKeys());
		setRotationClockwise.addKeyListener(new customizeKeys());
		setDrop.addKeyListener(new customizeKeys());
		setPlace.addKeyListener(new customizeKeys());
		setPrevLocation.addKeyListener(new customizeKeys());
		
		switchMute.addActionListener(new SoundManager.bListener());
		
		settingsMenu.addButton(switchMute);
		settingsMenu.addButton(setRotationAnticlockwise);
		settingsMenu.addButton(setRotationClockwise);
		settingsMenu.addButton(setDrop);
		settingsMenu.addButton(setPlace);
		settingsMenu.addButton(setPrevLocation);
		
		settingsMenu.addBackButton(back);
		
		card.add("Settings", settingsMenu);
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
			}
		}
	}

	public static class CustomizeRotationAnticlockwiseButton implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			clicked = 1;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public static class CustomizeRotationClockwiseButton implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			clicked = 2;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}		
	}
	
	public static class CustomizeDropButton implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			clicked = 3;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}		
	}
	
	public static class CustomizePlaceButton implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			clicked = 4;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}		
	}
	
	public static class CustomizePrevLocationButton implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			clicked = 5;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}		
	}
	
	public void run() throws IOException {

		GameView f = new GameView();
		
		f.setVisible(true);
		f.setBounds(100, 100, 800, 500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args)throws IOException {
		try {
			GameView gameView = new GameView();
			gameView.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static class customizeKeys implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			if (checkKey(e))
				return;
			
			newKey = e.getKeyCode();
			if (clicked == 1)
				MyComponents.wallRotateAnticlockwise = newKey;
			else if (clicked == 2) 
				MyComponents.wallRotateClockwise = newKey;
			else if (clicked == 3)
				MyComponents.wallDrop = newKey;
			else if (clicked == 4)
				MyComponents.wallPlace = newKey;
			else if (clicked == 5)
				MyComponents.wallPrevLocation = newKey;
			clicked = 0;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		public boolean checkKey(KeyEvent e) {
			int pressed = e.getKeyCode();
			if (pressed == MyComponents.wallRotateAnticlockwise ||
				pressed == MyComponents.wallRotateClockwise ||	
				pressed == MyComponents.wallDrop ||
				pressed == MyComponents.wallPlace ||
				pressed == MyComponents.wallPrevLocation ||
				pressed == KeyEvent.VK_1 ||
				pressed == KeyEvent.VK_2 ||	
				pressed == KeyEvent.VK_3 ||
				pressed == KeyEvent.VK_4 ||
				pressed == KeyEvent.VK_5||
				pressed == KeyEvent.VK_6 || 
				pressed == KeyEvent.VK_NUMPAD1 ||
				pressed == KeyEvent.VK_NUMPAD2 ||	
				pressed == KeyEvent.VK_NUMPAD3 ||
				pressed == KeyEvent.VK_NUMPAD4 ||
				pressed == KeyEvent.VK_NUMPAD5 ||
				pressed == KeyEvent.VK_NUMPAD6 || 
				pressed == KeyEvent.VK_UP ||
				pressed == KeyEvent.VK_DOWN ||
				pressed == KeyEvent.VK_LEFT ||
				pressed == KeyEvent.VK_RIGHT) {
				
				JOptionPane.showMessageDialog(null, "Entered key is in use!", "Used Key", JOptionPane.ERROR_MESSAGE);
				
				return true;
			}
			else 
				return false;
		}
	}
}
