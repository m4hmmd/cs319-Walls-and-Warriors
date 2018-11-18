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
    private static AudioData BGMD;
    private static ContinuousAudioDataStream loop;

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

    public static class wListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {
            playBackgroundMusic();
        }

        public void windowActivated(WindowEvent arg0) {
        }

        public void windowClosed(WindowEvent arg0) {
        }

        public void windowClosing(WindowEvent arg0) {
        }

        public void windowDeactivated(WindowEvent arg0) {
        }

        public void windowDeiconified(WindowEvent arg0) {
        }

        public void windowIconified(WindowEvent arg0) {
        }
    }

    public static class bListener implements ActionListener {

        private boolean playing = false;

        @Override
        public void actionPerformed(ActionEvent e) {
            playing = !(playing);
            if (playing) {
                stopBackgroundMusic();
            } else {
                playBackgroundMusic();
            }
        }
    }

    public static void gameWon() {
        try {
            gameWon = new AudioStream(new FileInputStream("src/sounds/gameWon.wav"));
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
        BGMP.start(gameWon);
    }

    public static void wallPlaced() {
        try {
            wallPlaced = new AudioStream(new FileInputStream("src/sounds/wallPlaced.wav"));
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
        BGMP.start(wallPlaced);
    }

    public static void playBackgroundMusic() {
        BGMP.start(loop);
    }

    public static void stopBackgroundMusic() {
        BGMP.stop(loop);
    }
}