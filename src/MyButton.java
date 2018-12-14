import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class MyButton extends JButton {
	private String nextPanelName;

	public MyButton(String name, String nextPanel, ActionListener aL) {
		super(name);
		
		this.nextPanelName = nextPanel;
		addActionListener(aL);
	}

	public String getNextPanelName() {
		return nextPanelName;
	}
}
