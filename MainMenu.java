import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.ProcessHandle.Info;

public class MainMenu {

    public static void showMainMenu(JFrame frame) {
        frame.getContentPane().removeAll();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        // Titre du jeu
        JLabel titleLabel = new JLabel("Jeu de Dames", JLabel.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 70));
        titleLabel.setForeground(new Color(201, 161, 76));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // Icônes pour le son
        ImageIcon soundOnIcon = new ImageIcon("Media/images/sound_on.png");
        ImageIcon soundOffIcon = new ImageIcon("Media/images/sound_off.png");

        JButton soundButton = new JButton(MusicPlayer.isMuted() ? soundOffIcon : soundOnIcon);
        soundButton.setBorderPainted(false);
        soundButton.setContentAreaFilled(false);
        soundButton.setFocusPainted(false);
        soundButton.setOpaque(false);
        soundButton.setPreferredSize(new Dimension(60, 60));

        JLabel soundLabel = new JLabel("Son");
        soundLabel.setForeground(Color.WHITE);
        soundLabel.setFont(new Font("Arial", Font.BOLD, 16));
        soundLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Box soundBox = Box.createVerticalBox();
        soundBox.add(soundLabel);
        soundBox.add(Box.createVerticalStrut(5));
        soundBox.add(soundButton);
        soundBox.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));
        soundBox.setOpaque(false);

        topPanel.add(soundBox, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        // Image centrale
        ImageIcon logoIcon = new ImageIcon("Media/images/dames.png");
        JLabel logoLabel = new JLabel(logoIcon, JLabel.CENTER);
        panel.add(logoLabel, BorderLayout.CENTER);

        // Panneau des boutons en bas
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 30));
        buttonPanel.setBackground(Color.BLACK);

        // Boutons
        ImageIcon icon = new ImageIcon("Media/images/play2.png");

// Redimensionner l'image à 50x50 pixels par exemple
Image image = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
ImageIcon resizedIcon = new ImageIcon(image);

JButton playButton = new JButton(resizedIcon);
        JButton infoButton = new JButton(new ImageIcon("Media/images/info.png"));
        

        for (JButton btn : new JButton[]{playButton, infoButton}) {
            btn.setPreferredSize(new Dimension(70, 60));
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setOpaque(false);
        }

        buttonPanel.add(playButton);
        buttonPanel.add(infoButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        soundButton.addActionListener(e -> {
            MusicPlayer.toggleSound();
            soundButton.setIcon(MusicPlayer.isMuted() ? soundOffIcon : soundOnIcon);
        });

        // Action pour le bouton Play
       playButton.addActionListener(e -> {
    // 1. Fermer la fenêtre actuelle si nécessaire
    Window currentWindow = SwingUtilities.getWindowAncestor(playButton);
    if (currentWindow != null) {
        currentWindow.dispose();
    }
    
    // 2. Lancer le jeu
        SwingUtilities.invokeLater(() -> {
               CheckersGUI gui = new CheckersGUI();
                gui.setVisible(true);
        });
    });
        // Action pour le bouton Info
        infoButton.addActionListener(e -> {
          InfoScreen.showInfoScreen(frame);
        });

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }
}
