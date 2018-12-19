import sun.audio.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;

public class SoundManager {

	private static AudioPlayer BGMP;
	private static AudioStream BGM;
	private static AudioStream wallPlaced;
	private static AudioStream gameWon;
	private static AudioStream mouseOver;
	private static AudioData BGMD;
	private static ContinuousAudioDataStream loop;
	public static boolean playing = true;
	public static boolean sound = true;

	SoundManager() {
		try {
			BGMP = AudioPlayer.player;
			BGM = new AudioStream(new FileInputStream("src/sounds/BGM.wav"));
			BGMD = BGM.getData();
			loop = new ContinuousAudioDataStream(BGMD);
		} catch (IOException error) {
			System.out.println(error.getMessage());
		}
	}

	public static void gameWon() {
		if (sound) {
			try {
				gameWon = new AudioStream(new FileInputStream("src/sounds/gameWon.wav"));
			} catch (IOException error) {
				System.out.println(error.getMessage());
			}
			BGMP.start(gameWon);
		}
	}

	public static void wallPlaced() {
		if (sound) {
			try {
				wallPlaced = new AudioStream(new FileInputStream("src/sounds/wallPlaced.wav"));
			} catch (IOException error) {
				System.out.println(error.getMessage());
			}
			BGMP.start(wallPlaced);
		}
	}

	public static void playBackgroundMusic() {
		BGMP.start(loop);
	}

	public static void stopBackgroundMusic() {
		BGMP.stop(loop);
	}

	public static void mouseOver() {
		if (sound) {
			try {
				mouseOver = new AudioStream(new FileInputStream("src/sounds/mouseOver.wav"));
			} catch (IOException error) {
				System.out.println(error.getMessage());
			}
			BGMP.start(mouseOver);
		}
	}
	
	public static void switchPlay() {
		if (playing) {
			stopBackgroundMusic();
		} else {
			playBackgroundMusic();
		}
		playing = !(playing);
	}
	
	public static void start() {
		if (playing)
			playBackgroundMusic();
		else
			stopBackgroundMusic();
	}

	public static void switchSound() {
		sound = !(sound);
	}
}
