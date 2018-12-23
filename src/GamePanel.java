import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements MouseMotionListener, MouseListener {
	String path;
	String name;
	Image image;

	ArrayList<GameButton> buttons = new ArrayList<GameButton>();
	ArrayList<JLabel> labels = new ArrayList<JLabel>();
	GameButton backButton = null;
	boolean one = false;
	int buttonHeight;
	int buttonWidth;
	int buttonDefaultSize = 20;
	int buttonScaledSize = 25;


	public GamePanel(String name, String path) throws IOException {
		setLayout(null);
		this.path = path;
		this.name = name;
		image = ImageIO.read(new File(path));
		setVisible(true);
		repaint();
	}

	public String getName() {
		return name;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		setSizesOfButtons(g);
		setSizesOfLabels(g);
	}

	private void setSizesOfLabels(Graphics g) {
		int labelHeight = 2 * getHeight() / (3 * labels.size() + 1);
		int labelH = labelHeight / 2;
		for (int i = 0; i < labels.size(); i++) {
			if (!one)
				labels.get(i).setBounds(0, 2 * labelH * i + 2 * labelH, getWidth(), labelHeight);
			else {
				labels.get(i).setBounds(0, 0, getWidth(), getHeight());
				labels.get(i).setFont(new Font("Arial", Font.PLAIN, getWidth() / 50));
			}
		}
	}

	public void setSizesOfButtons(Graphics g) {

		buttonHeight = 2 * getHeight() / (3 * buttons.size() + 4);
		buttonWidth = getWidth() / 2;
		int buttonH = buttonHeight / 2;
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setBounds(buttonWidth / 2, 3 * buttonH * i + 2 * buttonH, buttonWidth, buttonHeight);
		}
		if (backButton != null) {
			backButton.setBounds(0, (int) (getHeight() / 10 * 0.5), getWidth() / 5, getHeight() / 10);
		}
	}

	public void addButton(GameButton btn) {
		buttons.add(btn);
		setSizesOfButtons(getGraphics());
		add(btn);
	}

	public void addLabel(JLabel label) {
		labels.add(label);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Arial", Font.PLAIN, 40));
		label.setOpaque(false);
		setSizesOfLabels(getGraphics());
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		label.addMouseMotionListener(this);
		label.addMouseListener(this);
		add(label);
	}

	public void addOnlyOneLabel(JLabel label) {
		one = true;
		labels.add(label);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Arial", Font.PLAIN, 25));
		label.setOpaque(false);
		setSizesOfLabels(getGraphics());
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		add(label);
	}

	public void addBackButton(GameButton back) {
		if (backButton == null) {
			backButton = back;
			backButton.setBounds(0, (int) (getHeight() / 10 * 0.5), getWidth() / 5, getHeight() / 10);
			add(back);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (e.getSource() instanceof GameButton)
			if (((GameButton) (e.getSource())).isEnabled())
				((GameButton) (e.getSource())).setFont(new Font("Arial", Font.PLAIN, ((GameButton) e.getSource()).getSizeScaledize()));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if ((e.getSource() instanceof GameButton) && ((GameButton) (e.getSource())).isEnabled())
			SoundManager.mouseOver();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() instanceof GameButton)
			((GameButton) (e.getSource())).setFont(new Font("Arial", Font.PLAIN, ((GameButton) e.getSource()).getDefaultSize()));
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
