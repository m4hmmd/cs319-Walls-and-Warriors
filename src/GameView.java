import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GameView implements ActionListener {

    CardLayout cardLayout;
    JPanel card = new JPanel();
    JButton play = new JButton("Play");
    JButton settings = new JButton("Settings");
    JButton howTo = new JButton("How to Play");
    JButton credits = new JButton("Credits");
    JButton quit = new JButton("Quit");
    JButton home1 = new JButton("Home");
    JButton home2 = new JButton("Home");

    SoundManager sm = new SoundManager();

    static JButton[] levelButtons = {new JButton("Level 1"), new JButton("Level 2"), new JButton("Level 3"), new JButton("Level 4"), new JButton("Level 5")};

    JButton backButton = new JButton("Back");
    JButton backButtonHowTo = new JButton("Back");
    JButton backButtonCredits = new JButton("Back");
    JButton nextButton = new JButton("Next");

    static int lastCompletedLevel = 0;
    int currentLevelIndex = 0;
    GameManager managers[] = new GameManager[5];

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == play) {
            cardLayout.show(card, "Level Menu");
        } else if (e.getSource() == home1 || e.getSource() == home2) {
            cardLayout.show(card, "Game Menu");
            managers[currentLevelIndex].reset();
        } else if (e.getSource() == howTo) {
            cardLayout.show(card, "How To Play");
        } else if (e.getSource() == credits) {
            cardLayout.show(card, "Credits");
        } else if (e.getSource() == backButtonHowTo) {
            cardLayout.show(card, "Game Menu");
        } else if (e.getSource() == backButtonCredits) {
            cardLayout.show(card, "Game Menu");
        } else if (e.getSource() == backButton) {
            cardLayout.show(card, "Game Menu");
        } else if (e.getSource() == nextButton) {
            cardLayout.show(card, "Level Menu 2");
        } else if (e.getSource() == levelButtons[0]) {
            cardLayout.show(card, "Level 1");
        } else if (e.getSource() == levelButtons[1]) {
            cardLayout.show(card, "Level 2");
        } else if (e.getSource() == quit) {
            System.exit(0);
        }
    }

    public void run() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        MyJFrame frm = new MyJFrame();
        // frm.add(new JLabel(new ImageIcon("/Users/ahmetmalal/Desktop/ObjeCode/src/com/images/gameImage.png")));

        int width = screenSize.width / 2; //(int)screenSize.getWidth();
        int height = screenSize.height * 3 / 4;//(int)screenSize.getHeight();
        int startPoint = 0;
        int buttonHeight = height / 14;
        int buttonWidth = width / 5;
        int buttonsLeftMargin = width / 15;
        int levelButtonsLeftMargin = width / 15;

        JLabel gameMenuImage = new JLabel(new ImageIcon(scaleImage(width, height, "src/img/img1.jpeg")));
        gameMenuImage.setBounds(0, 0, width, height);
        gameMenuImage.setVisible(true);

        JLabel LevelMenuImage = new JLabel(new ImageIcon(scaleImage(width, height, "src/img/img1.jpeg")));
        LevelMenuImage.setBounds(0, 0, width, height);
        LevelMenuImage.setVisible(true);
        JPanel contentPane = (JPanel) frm.getContentPane();
        card.setLayout(cardLayout = new CardLayout());
        
        managers[0] = new GameManager(1, cardLayout, card);
        managers[1] = new GameManager(2, cardLayout, card);

        JPanel gamePanel = new JPanel();

        gamePanel.setLayout(null);
        gamePanel.add(play);
        gamePanel.add(settings);
        gamePanel.add(howTo);
        gamePanel.add(credits);
        gamePanel.add(howTo);
        gamePanel.add(credits);
        gamePanel.add(quit);

        play.setBounds(buttonsLeftMargin, height / 7, buttonWidth, buttonHeight);
        settings.setBounds(buttonsLeftMargin, 2 * height / 7, buttonWidth, buttonHeight);
        howTo.setBounds(buttonsLeftMargin, 3 * height / 7, buttonWidth, buttonHeight);
        credits.setBounds(buttonsLeftMargin, 4 * height / 7, buttonWidth, buttonHeight);
        quit.setBounds(buttonsLeftMargin, 5 * height / 7, buttonWidth, buttonHeight);

        home1.setBounds(levelButtonsLeftMargin / 3, height / 40, buttonWidth / 2, buttonWidth / 4);
        home2.setBounds(levelButtonsLeftMargin / 3, height / 40, buttonWidth / 2, buttonWidth / 4);

        gamePanel.add(gameMenuImage);

        play.addActionListener(this);
        quit.addActionListener(this);
        backButton.addActionListener(this);
        nextButton.addActionListener(this);
        howTo.addActionListener(this);
        credits.addActionListener(this);
        backButtonHowTo.addActionListener(this);
        backButtonCredits.addActionListener(this);
        home1.addActionListener(this);
        home2.addActionListener(this);


        JPanel LevelPanel = new JPanel(null);

        for (int i = 0; i < levelButtons.length; i++) {
            LevelPanel.add(levelButtons[i]);
            levelButtons[i].setBounds(levelButtonsLeftMargin, (i + 1) * height / 7, buttonWidth, buttonHeight);
            levelButtons[i].addActionListener(this);
            if (i > lastCompletedLevel) {
                levelButtons[i].setEnabled(false);
            }
        }

        JPanel howToPanel = new JPanel(null);
        JLabel howToBackground = new JLabel(new ImageIcon(scaleImage(width, height, "src/img/img1.jpeg")));
        howToBackground.setBounds(0, 0, width, height);
        howToBackground.setVisible(true);
        JLabel howToPlay = new JLabel("<html><center>Walls & Warriors is a board game played with warrior figures and walls placed under specific rules. The goal of this game place the four walls on the game board so that all the blue knights are inside the enclosure and all the red knights are on the outside to defend the castle to be left inside the walls. There is always only one correct solution.</center><br><center>Gameplay: </center><center>Given a board with red and blue soldiers and, on higher leverls, lakes and special soldiers, the player needs to select one of the provided wall shapes, rotate it as needed and put onto the board, so as to complete the castle with all blue knights inside and red ones outside.<center></html>");
        howToPlay.setFont(new Font("Times", 1, 30));
        howToPlay.setForeground(Color.WHITE);
        howToPlay.setBounds(width / 6, height / 10, 2 * width / 3, 4 * height / 5);
        howToPlay.setVisible(true);
        howToPanel.add(backButtonHowTo);
        howToPanel.add(howToPlay);
        howToPanel.add(howToBackground);

        JPanel creditsPanel = new JPanel(null);
        JLabel creditsBackground = new JLabel(new ImageIcon(scaleImage(width, height, "src/img/img1.jpeg")));
        creditsBackground.setBounds(0, 0, width, height);
        creditsBackground.setVisible(true);
        JLabel credits = new JLabel("<html><center>Creators:</center><br><br><center>Ahmet Malal<center><br><br><center>Huseyn Allahyarov<center><br><br><center>Ibrahim Mammadov<center><br><br><center>Mahammad Shirinov<center><br><br><center>Samet Demir<center></html>");
        credits.setFont(new Font("Times", 1, 30));
        credits.setForeground(Color.WHITE);
        credits.setBounds(width / 3, height / 10, width / 3, 4 * height / 5);
        credits.setVisible(true);
        creditsPanel.add(backButtonCredits);
        creditsPanel.add(credits);
        creditsPanel.add(creditsBackground);

        LevelPanel.add(backButton);
        LevelPanel.add(nextButton);
        LevelPanel.add(LevelMenuImage);

        backButton.setBounds(width / 20, 6 * height / 7, width / 13, buttonHeight / 3 * 2);
        backButtonHowTo.setBounds(width / 20, 6 * height / 7, width / 13, buttonHeight / 3 * 2);
        backButtonCredits.setBounds(width / 20, 6 * height / 7, width / 13, buttonHeight / 3 * 2);
        nextButton.setBounds((int) ((19 / 20.0 - 1 / 13.0) * width), 6 * height / 7, width / 13, buttonHeight / 3 * 2);

        managers[0].getPanel().add(home1);
        managers[1].getPanel().add(home2);

        card.add("Game Menu", gamePanel);
        card.add("Level Menu", LevelPanel);
        card.add("How To Play", howToPanel);
        card.add("Credits", creditsPanel);
        card.add("Level 1", managers[0].getPanel());
        card.add("Level 2", managers[1].getPanel());
        cardLayout.show(card, "Game Menu");

        contentPane.add(card);
        frm.addWindowListener(new SoundManager.wListener());
        frm.setVisible(true);
        frm.setResizable(true);
        frm.setSize(width, height);
        frm.setLocation(startPoint, startPoint);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class MyJFrame extends JFrame {

        @Override
        public void paintComponents(Graphics g) {
            super.paintComponents(g); //To change body of generated methods, choose Tools | Templates.
        }
    }

    public static void main(String[] args) {
        GameView gameView = new GameView();
        gameView.run();

    }

    public BufferedImage scaleImage(int WIDTH, int HEIGHT, String filename) {
        BufferedImage bi = null;
        try {
            ImageIcon ii = new ImageIcon(filename);//path to image
            bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D) bi.createGraphics();
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
            g2d.drawImage(ii.getImage(), 0, 0, WIDTH, HEIGHT, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bi;
    }
}
