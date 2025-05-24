
import java.util.List;
import java.util.Scanner;

public class CheckersGame {
    public Board board;
    public Player redPlayer;
    public Player bluePlayer;
    public boolean isRedTurn;
    public Scanner scanner;
    public int lastToRow;
    public int lastToCol;

    public CheckersGame() {
        board = new Board();
        scanner = new Scanner(System.in);
        initializePlayers();
        isRedTurn = true;
    }

    public void initializePlayers() {
        System.out.println(GameConstants.GOLD + "Welcome to Checkers!" + GameConstants.RESET);
        System.out.print("Enter Red player name: ");
        String redName = scanner.nextLine();
        System.out.print("Enter Blue player name: ");
        String blueName = scanner.nextLine();

        redPlayer = new Player(redName, true);
        bluePlayer = new Player(blueName, false);
    }

    public void start() {
        while (!isGameOver()) {
            board.display();
            Player currentPlayer = isRedTurn ? redPlayer : bluePlayer;

            System.out.println(GameConstants.GOLD + "\n" + currentPlayer.getName() +
                    "'s turn (" + currentPlayer.getColorName() + ")" + GameConstants.RESET);

            boolean moveCompleted = false;
            while (!moveCompleted && !isGameOver()) {
                System.out.print("Enter your move (fromRow fromCol toRow toCol): ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("quit")) {
                    System.out.println(GameConstants.GOLD + "Game ended." + GameConstants.RESET);
                    return;
                }

                try {
                    String[] parts = input.split(" ");
                    int fromRow = Integer.parseInt(parts[0]);
                    int fromCol = Integer.parseInt(parts[1]);
                    int toRow = Integer.parseInt(parts[2]);
                    int toCol = Integer.parseInt(parts[3]);

                    if (board.isValidMove(fromRow, fromCol, toRow, toCol, isRedTurn)) {
                        board.movePiece(fromRow, fromCol, toRow, toCol);
                        lastToRow = toRow;
                        lastToCol = toCol;

                        if (Math.abs(toRow - fromRow) == 2 && board.canContinueJumping(toRow, toCol, isRedTurn)) {
                            System.out.println(GameConstants.GOLD + "You must continue jumping!" + GameConstants.RESET);
                            board.display();
                            System.out.print("Enter next jump for piece at " + toRow + " " + toCol + ": ");
                        } else {
                            moveCompleted = true;
                            isRedTurn = !isRedTurn;
                        }
                    } else {
                        System.out.println(GameConstants.RED + "Invalid move! Try again." + GameConstants.RESET);
                    }
                } catch (Exception e) {
                    System.out.println(
                            GameConstants.RED + "Invalid input format! Use: row col toRow toCol" + GameConstants.RESET);
                }
            }
        }

        announceWinner();
    }

    public boolean isGameOver() {
        // Check if either player has no pieces
        if (board.getRedPieces().isEmpty())
            return true;
        if (board.getBluePieces().isEmpty())
            return true;

        // Check 40-move rule
        if (board.getMovesSinceLastCapture() >= 40)
            return true;

        // Check if current player can move
        return !canPlayerMove(isRedTurn);
    }

    public boolean canPlayerMove(boolean isRed) {
        List<Piece> pieces = isRed ? board.getRedPieces() : board.getBluePieces();
        for (Piece piece : pieces) {
            if (hasValidMoves(piece, isRed)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasValidMoves(Piece piece, boolean isRed) {
        int row = piece.getRow();
        int col = piece.getCol();
        char pieceType = piece.getType();

        int[][] directions = board.getValidDirections(pieceType);
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (board.isValidPosition(newRow, newCol) &&
                    board.getGrid()[newRow][newCol] == GameConstants.EMPTY) {
                if (!board.isJumpPossible(isRed)) {
                    return true;
                }
            }

            int jumpRow = row + 2 * dir[0];
            int jumpCol = col + 2 * dir[1];
            if (board.isValidPosition(jumpRow, jumpCol) &&
                    board.getGrid()[jumpRow][jumpCol] == GameConstants.EMPTY) {
                int midRow = row + dir[0];
                int midCol = col + dir[1];
                if (board.isEnemy(board.getGrid()[midRow][midCol], isRed)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void announceWinner() {
        if (board.getRedPieces().isEmpty()) {
            System.out.println(GameConstants.BLUE + bluePlayer.getName() + " (Blue) wins!" + GameConstants.RESET);
        } else if (board.getBluePieces().isEmpty()) {
            System.out.println(GameConstants.RED + redPlayer.getName() + " (Red) wins!" + GameConstants.RESET);
        } else if (board.getMovesSinceLastCapture() >= 40) {
            System.out.println(
                    GameConstants.GOLD + "Game ended in a draw (40 moves without capture)" + GameConstants.RESET);
        } else if (!canPlayerMove(true)) {
            System.out.println(
                    GameConstants.BLUE + bluePlayer.getName() + " (Blue) wins by stalemate!" + GameConstants.RESET);
        } else {
            System.out.println(
                    GameConstants.RED + redPlayer.getName() + " (Red) wins by stalemate!" + GameConstants.RESET);
        }
    }
}