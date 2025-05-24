import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {

    private static Clip clip;
    private static boolean isMuted = false;

    public static void playBackgroundMusic(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
            
            if (!isMuted) {
                clip.start();
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Erreur lors du chargement du son: " + e.getMessage());
        }
    }

    public static void toggleSound() {
        isMuted = !isMuted;

        if (clip != null) {
            if (isMuted) {
                clip.stop();
            } else {
                clip.start();
            }
        }

        System.out.println("Son activé ? " + !isMuted);
    }

    public static boolean isMuted() {
        return isMuted;
    }
}
