import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Lancement de la musique de fond
        MusicPlayer.playBackgroundMusic("Media/audio/city-bgm-336601.wav");

        // Affichage du menu de sélection
        showVersionSelectionMenu();
    }

    /**
     * Affiche un menu pour permettre à l'utilisateur de choisir la version du jeu
     */
    private static void showVersionSelectionMenu() {
        String[] options = {"Interface Graphique", "Version Console"};

        int choice = JOptionPane.showOptionDialog(
            null,
            "Choisissez la version du jeu :",
            "Jeu de Dames",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        switch (choice) {
            case 0 -> launchGraphicalVersion();
            case 1 -> launchConsoleVersion();
            default -> System.exit(0); 
        }
    }

    /**
     * Lance la version graphique du jeu avec Swing
     */
    private static void launchGraphicalVersion() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Jeu de Dames");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setLocationRelativeTo(null); // Centre la fenêtre

            MainMenu.showMainMenu(frame); // Affiche le menu principal
            frame.setVisible(true);
        });
    }

    /**
     * Lance la version console du jeu
     */
    private static void launchConsoleVersion() {
        System.out.println(GameConstants.BOLD + GameConstants.GOLD + "=== Checkers Game ===" + GameConstants.RESET);
        CheckersGame game = new CheckersGame();
        game.start();
    }
}
