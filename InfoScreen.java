import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class InfoScreen {
    
    private static final Color BACKGROUND_COLOR = new Color(25, 25, 35);
    private static final Color ACCENT_COLOR = new Color(220, 180, 80);
    private static final Color TEXT_COLOR = new Color(240, 240, 240);
    private static final Color BUTTON_COLOR = new Color(180, 120, 50);
    private static final Font TITLE_FONT = new Font("Montserrat", Font.BOLD, 42);
    private static final Font TEXT_FONT = new Font("Calibri", Font.PLAIN, 16);
    
    public static void showInfoScreen(JFrame frame) {
        frame.getContentPane().removeAll();
        
        // Création du panneau principal avec un layout personnalisé
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Arrière-plan dégradé
                GradientPaint gradient = new GradientPaint(
                    0, 0, BACKGROUND_COLOR,
                    0, getHeight(), new Color(15, 15, 25));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Motif de damier subtil en arrière-plan
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f));
                int size = 40;
                g2d.setColor(Color.WHITE);
                for (int i = 0; i < getWidth(); i += size) {
                    for (int j = 0; j < getHeight(); j += size) {
                        if ((i / size + j / size) % 2 == 0) {
                            g2d.fillRect(i, j, size, size);
                        }
                    }
                }
                
                // Cercles décoratifs
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
                g2d.setColor(ACCENT_COLOR);
                g2d.fill(new Ellipse2D.Double(-100, -100, 300, 300));
                g2d.fill(new Ellipse2D.Double(getWidth() - 200, getHeight() - 200, 300, 300));
                
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        
        // Panneau de titre avec effet visuel
        JPanel titlePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Ligne décorative sous le titre
                g2d.setColor(ACCENT_COLOR);
                int y = getHeight() - 10;
                g2d.setStroke(new BasicStroke(3f));
                g2d.drawLine(getWidth() / 4, y, getWidth() * 3 / 4, y);
                g2d.dispose();
            }
        };
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 40, 0));
        
        // Titre avec effet d'ombre
        JLabel titleLabel = new JLabel("JEU DE DAMES", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(ACCENT_COLOR);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        // Sous-titre
        JLabel subtitleLabel = new JLabel("Informations & Règles du jeu", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Calibri", Font.ITALIC, 18));
        subtitleLabel.setForeground(TEXT_COLOR);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Panneau central avec logo et informations
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Chargement et redimensionnement de l'image
        ImageIcon originalIcon = new ImageIcon("Media/images/dames.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        
        JLabel logoLabel = new JLabel(scaledIcon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Cercle lumineux derrière le logo
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
                g2d.setColor(ACCENT_COLOR);
                int size = Math.max(getWidth(), getHeight()) + 30;
                g2d.fillOval(getWidth()/2 - size/2, getHeight()/2 - size/2, size, size);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        // Panneau d'information avec style personnalisé
        JPanel infoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fond semi-transparent
                g2d.setColor(new Color(40, 40, 50, 220));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Bordure subtile
                g2d.setColor(ACCENT_COLOR);
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 18, 18);
                
                g2d.dispose();
            }
        };
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        
        // En-tête des informations
        JLabel infoHeader = new JLabel("Version 1.0");
        infoHeader.setFont(new Font("Calibri", Font.BOLD, 18));
        infoHeader.setForeground(ACCENT_COLOR);
        infoHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Texte des informations avec style amélioré
        String infoContent = 
            "Développé par Oumaima ElKhlifi & Hafssa Hamdach\n" +
            "Ce jeu vous permet de jouer aux dames contre l'ordinateur ou contre un" +
            "autre joueur en mode local." +
            "• OBJECTIF: Capturer tous les pions adverses ou bloquer leur mouvement" +
            "• DÉPLACEMENT: Les pions se déplacent en diagonale vers l'avant" +
            "• PRISE: En sautant par-dessus un pion adverse sur une case vide" +
            "• DAME: Un pion qui atteint la dernière rangée devient une dame et" +
            "  peut se déplacer en diagonale dans n'importe quelle direction" +
            "Bonne chance et amusez-vous bien!";
            
        JTextArea infoText = new JTextArea(infoContent);
        infoText.setFont(TEXT_FONT);
        infoText.setForeground(TEXT_COLOR);
        infoText.setEditable(false);
        infoText.setOpaque(false);
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);
        
        // Ajout des composants au panneau d'information
        infoPanel.add(infoHeader);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(infoText);
        
        // Bouton personnalisé pour retourner au menu
        JButton backButton = new JButton("Retour au Menu Principal") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dégradé pour le bouton
                GradientPaint gradient = new GradientPaint(
                    0, 0, BUTTON_COLOR,
                    0, getHeight(), BUTTON_COLOR.darker());
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Effet de brillance
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight()/2, 15, 15);
                
                g2d.dispose();
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(ACCENT_COLOR);
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                g2d.dispose();
            }
        };
        backButton.setFont(new Font("Calibri", Font.BOLD, 16));
        backButton.setForeground(TEXT_COLOR);
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> MainMenu.showMainMenu(frame));
        
        // Effet de survol pour le bouton
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setForeground(Color.WHITE);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setForeground(TEXT_COLOR);
            }
        });
        
        // Organisation du panneau central
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        centerPanel.add(logoLabel, gbc);
        
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        centerPanel.add(infoPanel, gbc);
        
        // Panneau pour le bouton avec de l'espace autour
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        backButton.setPreferredSize(new Dimension(250, 45));
        buttonPanel.add(backButton);
        
        // Assemblage final
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel);
        frame.revalidate();
        frame.repaint();
    }
}