

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class CheckersGUI extends JFrame {
    private Board board;
    private JPanel boardPanel;
    private JPanel infoPanel;
    private JLabel statusLabel;
    private JLabel blackScoreLabel;
    private JLabel whiteScoreLabel;
    private JLabel timerLabel;
    private JButton resetButton;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean isBlackTurn = true;
    private boolean mustContinueJumping = false;
    private Timer gameTimer;
    private int blackTime = 0;
    private int whiteTime = 0;

    public CheckersGUI() {
        board = new Board();
        initializeGUI();
        startTimer();
        updateBoard();
    }

    private void initializeGUI() {
        setTitle("Checkers Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize components first
        boardPanel = new JPanel(new GridLayout(8, 8));
        boardPanel.setPreferredSize(new Dimension(500, 500));

        // Info panel
        infoPanel = new JPanel(new BorderLayout());
        infoPanel.setPreferredSize(new Dimension(200, 500));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.setBackground(Color.BLACK);

        // Score panel
        JPanel scorePanel = new JPanel(new GridLayout(4, 1));
        scorePanel.setBackground(Color.BLACK);

        blackScoreLabel = new JLabel("Black: 12 pieces", JLabel.CENTER);
        blackScoreLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        blackScoreLabel.setForeground(Color.WHITE);
        blackScoreLabel.setBackground(Color.BLACK);

        whiteScoreLabel = new JLabel("White: 12 pieces", JLabel.CENTER);
        whiteScoreLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        whiteScoreLabel.setForeground(Color.WHITE);
        whiteScoreLabel.setBackground(Color.BLACK);

        timerLabel = new JLabel("Time - Black: 0s, White: 0s", JLabel.CENTER);
        timerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setBackground(Color.BLACK);

        resetButton = new JButton("Reset Game");
        resetButton.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        resetButton.setBackground(Color.DARK_GRAY);
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(e -> resetGame());

        scorePanel.add(blackScoreLabel);
        scorePanel.add(whiteScoreLabel);
        scorePanel.add(timerLabel);
        scorePanel.add(resetButton);

        // Status label
        statusLabel = new JLabel("Black's turn", JLabel.CENTER);
        statusLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(Color.DARK_GRAY);
        statusLabel.setForeground(Color.WHITE);

        infoPanel.add(scorePanel, BorderLayout.NORTH);
        infoPanel.add(statusLabel, BorderLayout.SOUTH);

        add(boardPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
    }

    private void startTimer() {
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isBlackTurn) {
                    blackTime++;
                } else {
                    whiteTime++;
                }
                updateTimerLabel();
            }
        }, 1000, 1000);
    }

    private void updateTimerLabel() {
        SwingUtilities.invokeLater(() -> {
            timerLabel.setText(String.format("Time - Black: %ds, White: %ds", blackTime, whiteTime));
        });
    }

    private void resetGame() {
        gameTimer.cancel();
        board = new Board();
        isBlackTurn = true;
        mustContinueJumping = false;
        selectedRow = -1;
        selectedCol = -1;
        blackTime = 0;
        whiteTime = 0;
        startTimer();
        updateStatus();
        updateBoard();
    }

    private void updateBoard() {
        boardPanel.removeAll();

        // Update scores
        blackScoreLabel.setText("Black: " + board.getRedPieces().size() + " pieces");
        whiteScoreLabel.setText("White: " + board.getBluePieces().size() + " pieces");

        for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
            for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(60, 60));

                // Set board colors
                if ((row + col) % 2 == 0) {
                    button.setBackground(new Color(210, 180, 140));
                } else {
                    button.setBackground(new Color(139, 69, 19));
                }

                // Highlight possible moves if piece is selected
                if (selectedRow != -1 && selectedCol != -1) {
                    if (board.isValidMove(selectedRow, selectedCol, row, col, isBlackTurn)) {
                        button.setBackground(new Color(144, 238, 144)); // Light green for possible moves
                    }
                }

                char piece = board.getGrid()[row][col];
                if (piece == GameConstants.RED_PIECE || piece == GameConstants.RED_KING) {
                    button.setIcon(createPieceIcon(Color.BLACK, piece == GameConstants.RED_KING));
                } else if (piece == GameConstants.BLUE_PIECE || piece == GameConstants.BLUE_KING) {
                    button.setIcon(createPieceIcon(Color.WHITE, piece == GameConstants.BLUE_KING));
                }

                // Highlight selected piece
                if (row == selectedRow && col == selectedCol) {
                    button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                }

                final int currentRow = row;
                final int currentCol = col;
                button.addActionListener(e -> handleSquareClick(currentRow, currentCol));

                boardPanel.add(button);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private ImageIcon createPieceIcon(Color color, boolean isKing) {
        int size = 50;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.fillOval(5, 5, size - 10, size - 10);

        if (isKing) {
            g2d.setColor(Color.YELLOW);
            g2d.fillPolygon(new int[] { size / 2 - 10, size / 2, size / 2 + 10 },
                    new int[] { size / 2 - 5, size / 2 + 10, size / 2 - 5 }, 3);
        }

        g2d.dispose();
        return new ImageIcon(image);
    }

    private void handleSquareClick(int row, int col) {
        if (mustContinueJumping && (row != selectedRow || col != selectedCol)) {
            if (board.isValidMove(selectedRow, selectedCol, row, col, isBlackTurn) &&
                    Math.abs(row - selectedRow) == 2) {
                board.movePiece(selectedRow, selectedCol, row, col);

                if (board.canContinueJumping(row, col, isBlackTurn)) {
                    selectedRow = row;
                    selectedCol = col;
                    statusLabel.setText((isBlackTurn ? "Black" : "White") + " must continue jumping!");
                } else {
                    mustContinueJumping = false;
                    isBlackTurn = !isBlackTurn;
                    selectedRow = -1;
                    selectedCol = -1;
                    updateStatus();
                }
                updateBoard();
                checkGameOver();
            }
            return;
        }

        if (selectedRow == -1) {
            char piece = board.getGrid()[row][col];
            boolean isBlackPiece = (piece == GameConstants.RED_PIECE || piece == GameConstants.RED_KING);
            boolean isWhitePiece = (piece == GameConstants.BLUE_PIECE || piece == GameConstants.BLUE_KING);

            if ((isBlackTurn && isBlackPiece) || (!isBlackTurn && isWhitePiece)) {
                selectedRow = row;
                selectedCol = col;
                updateBoard();
            }
        } else {
            if (board.isValidMove(selectedRow, selectedCol, row, col, isBlackTurn)) {
                board.movePiece(selectedRow, selectedCol, row, col);

                if (Math.abs(row - selectedRow) == 2 && board.canContinueJumping(row, col, isBlackTurn)) {
                    mustContinueJumping = true;
                    selectedRow = row;
                    selectedCol = col;
                    statusLabel.setText((isBlackTurn ? "Black" : "White") + " must continue jumping!");
                } else {
                    mustContinueJumping = false;
                    isBlackTurn = !isBlackTurn;
                    selectedRow = -1;
                    selectedCol = -1;
                    updateStatus();
                }
                updateBoard();
                checkGameOver();
            } else {
                selectedRow = -1;
                selectedCol = -1;
                updateBoard();
            }
        }
    }

    private void checkGameOver() {
        boolean blackCanMove = canPlayerMove(true);
        boolean whiteCanMove = canPlayerMove(false);

        if (board.getRedPieces().isEmpty() || !blackCanMove) {
            endGame("White wins! Black loses!");
        } else if (board.getBluePieces().isEmpty() || !whiteCanMove) {
            endGame("Black wins! White loses!");
        } else if (board.getMovesSinceLastCapture() >= 40) {
            endGame("Game ended in a draw (40 moves without capture)");
        }
    }

    private boolean canPlayerMove(boolean isBlack) {
        List<Piece> pieces = isBlack ? board.getRedPieces() : board.getBluePieces();
        for (Piece piece : pieces) {
            int row = piece.getRow();
            int col = piece.getCol();
            char pieceType = board.getGrid()[row][col];

            int[][] directions = board.getValidDirections(pieceType);
            for (int[] dir : directions) {
                // Check normal moves
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                if (board.isValidPosition(newRow, newCol) &&
                        board.getGrid()[newRow][newCol] == GameConstants.EMPTY) {
                    if (!board.isJumpPossible(isBlack)) {
                        return true;
                    }
                }

                // Check jumps
                int jumpRow = row + 2 * dir[0];
                int jumpCol = col + 2 * dir[1];
                if (board.isValidPosition(jumpRow, jumpCol) &&
                        board.getGrid()[jumpRow][jumpCol] == GameConstants.EMPTY) {
                    int midRow = row + dir[0];
                    int midCol = col + dir[1];
                    if (board.isEnemy(board.getGrid()[midRow][midCol], isBlack)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void endGame(String message) {
        gameTimer.cancel();
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        resetGame();
    }

    private void updateStatus() {
        statusLabel.setText(isBlackTurn ? "Black's turn" : "White's turn");
    }
}