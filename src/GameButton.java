import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class GameButton extends JButton {
	private String nextPanelName;
	private int size, sizeScaled;

	public GameButton(String name, String nextPanel, int size, int sizeScaled, ActionListener aL) {
		super(name);
		this.size = size;
		this.sizeScaled = sizeScaled;

		setForeground(Color.WHITE);
		setFont(new Font("Arial", Font.PLAIN, size));
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

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
		});
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {

			}

			@Override
			public void mouseMoved(MouseEvent e) {
				if (e.getSource() instanceof GameButton)
					if (((GameButton) (e.getSource())).isEnabled())
						((GameButton) (e.getSource())).setFont(new Font("Arial", Font.PLAIN, ((GameButton) e.getSource()).getSizeScaledize()));
			}
		});

		this.nextPanelName = nextPanel;
		addActionListener(aL);
	}

	public int getDefaultSize() {
		return size;
	}

	public int getSizeScaledize() {
		return sizeScaled;
	}

	public String getNextPanelName() {
		return nextPanelName;
	}
}
