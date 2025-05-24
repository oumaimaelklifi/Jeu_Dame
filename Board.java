

import java.util.ArrayList;
import java.util.List;

public class Board {
    private char[][] grid;
    private List<Piece> redPieces;
    private List<Piece> bluePieces;
    private int movesSinceLastCapture;

    public List<Piece> getRedPieces() {
        return redPieces;
    }

    public List<Piece> getBluePieces() {
        return bluePieces;
    }

    public int getMovesSinceLastCapture() {
        return movesSinceLastCapture;
    }

    public Board() {
        grid = new char[GameConstants.BOARD_SIZE][GameConstants.BOARD_SIZE];
        redPieces = new ArrayList<>();
        bluePieces = new ArrayList<>();
        movesSinceLastCapture = 0;
        initializeBoard();
    }

    private void initializeBoard() {
        // Initialize empty board
        for (int i = 0; i < GameConstants.BOARD_SIZE; i++) {
            for (int j = 0; j < GameConstants.BOARD_SIZE; j++) {
                grid[i][j] = GameConstants.EMPTY;
            }
        }

        // Place red pieces (top)
        for (int i = 0; i < 3; i++) {
            for (int j = (i + 1) % 2; j < GameConstants.BOARD_SIZE; j += 2) {
                grid[i][j] = GameConstants.RED_PIECE;
                redPieces.add(new Piece(GameConstants.RED_PIECE, i, j));
            }
        }

        // Place blue pieces (bottom)
        for (int i = 5; i < GameConstants.BOARD_SIZE; i++) {
            for (int j = (i + 1) % 2; j < GameConstants.BOARD_SIZE; j += 2) {
                grid[i][j] = GameConstants.BLUE_PIECE;
                bluePieces.add(new Piece(GameConstants.BLUE_PIECE, i, j));
            }
        }
    }

    public void display() {
        System.out.println(GameConstants.GOLD + "  0 1 2 3 4 5 6 7" + GameConstants.RESET);
        for (int i = 0; i < GameConstants.BOARD_SIZE; i++) {
            System.out.print(GameConstants.GOLD + i + " " + GameConstants.RESET);
            for (int j = 0; j < GameConstants.BOARD_SIZE; j++) {
                if (grid[i][j] == GameConstants.RED_PIECE || grid[i][j] == GameConstants.RED_KING) {
                    System.out.print(GameConstants.RED + grid[i][j] + " " + GameConstants.RESET);
                } else if (grid[i][j] == GameConstants.BLUE_PIECE || grid[i][j] == GameConstants.BLUE_KING) {
                    System.out.print(GameConstants.BLUE + grid[i][j] + " " + GameConstants.RESET);
                } else {
                    System.out.print(grid[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    public boolean isJumpPossible(boolean isRedTurn) {
        for (Piece piece : isRedTurn ? redPieces : bluePieces) {
            int row = piece.getRow();
            int col = piece.getCol();
            char pieceType = grid[row][col];

            int[][] directions = getValidDirections(pieceType);
            for (int[] dir : directions) {
                int jumpRow = row + 2 * dir[0];
                int jumpCol = col + 2 * dir[1];
                int midRow = row + dir[0];
                int midCol = col + dir[1];

                if (isValidPosition(jumpRow, jumpCol) && grid[jumpRow][jumpCol] == GameConstants.EMPTY) {
                    if (isEnemy(grid[midRow][midCol], isRedTurn)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int[][] getValidDirections(char pieceType) {
        if (pieceType == GameConstants.RED_PIECE) {
            return new int[][] { { 1, -1 }, { 1, 1 } };
        } else if (pieceType == GameConstants.BLUE_PIECE) {
            return new int[][] { { -1, -1 }, { -1, 1 } };
        } else { // Kings
            return new int[][] { { 1, -1 }, { 1, 1 }, { -1, -1 }, { -1, 1 } };
        }
    }

    public boolean isEnemy(char piece, boolean isRedTurn) {
        if (isRedTurn) {
            return piece == GameConstants.BLUE_PIECE || piece == GameConstants.BLUE_KING;
        } else {
            return piece == GameConstants.RED_PIECE || piece == GameConstants.RED_KING;
        }
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < GameConstants.BOARD_SIZE &&
                col >= 0 && col < GameConstants.BOARD_SIZE;
    }

    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, boolean isRedTurn) {
        if (!isValidPosition(fromRow, fromCol) || !isValidPosition(toRow, toCol)) {
            return false;
        }

        char piece = grid[fromRow][fromCol];
        if ((isRedTurn && (piece != GameConstants.RED_PIECE && piece != GameConstants.RED_KING)) ||
                (!isRedTurn && (piece != GameConstants.BLUE_PIECE && piece != GameConstants.BLUE_KING))) {
            return false;
        }

        if (grid[toRow][toCol] != GameConstants.EMPTY) {
            return false;
        }

        int rowDiff = toRow - fromRow;
        int absRowDiff = Math.abs(rowDiff);
        int colDiff = toCol - fromCol;
        int absColDiff = Math.abs(colDiff);

        if (absRowDiff != absColDiff) {
            return false;
        }

        // Check piece movement direction for regular pieces
        if (piece == GameConstants.RED_PIECE && rowDiff <= 0) {
            return false;
        }
        if (piece == GameConstants.BLUE_PIECE && rowDiff >= 0) {
            return false;
        }

        // Check if jump is required
        boolean jumpAvailable = isJumpPossible(isRedTurn);

        // For kings, check all squares along the path
        if (piece == GameConstants.RED_KING || piece == GameConstants.BLUE_KING) {
            if (absRowDiff > 1) { // King is moving more than one square
                if (jumpAvailable && absRowDiff != 2) {
                    return false; // Must jump if available
                }

                // Check if path is clear and contains exactly one enemy piece for capture
                int rowStep = rowDiff > 0 ? 1 : -1;
                int colStep = colDiff > 0 ? 1 : -1;
                int enemyCount = 0;

                for (int i = 1; i < absRowDiff; i++) {
                    int currentRow = fromRow + i * rowStep;
                    int currentCol = fromCol + i * colStep;
                    char currentPiece = grid[currentRow][currentCol];

                    if (currentPiece != GameConstants.EMPTY) {
                        if (isEnemy(currentPiece, isRedTurn)) {
                            enemyCount++;
                            // Enemy piece must be immediately followed by empty square
                            if (i != absRowDiff - 1
                                    || grid[currentRow + rowStep][currentCol + colStep] != GameConstants.EMPTY) {
                                return false;
                            }
                        } else {
                            return false; // Friendly piece in the way
                        }
                    }
                }

                if (jumpAvailable) {
                    return enemyCount == 1; // Must capture exactly one piece if jump is available
                }
                return enemyCount == 0; // Can only move if no pieces in the way when no jumps available
            }
        }

        if (jumpAvailable) {
            if (absRowDiff != 2) {
                return false;
            }
            int midRow = (fromRow + toRow) / 2;
            int midCol = (fromCol + toCol) / 2;
            return isEnemy(grid[midRow][midCol], isRedTurn);
        } else {
            return absRowDiff == 1;
        }
    }

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        char piece = grid[fromRow][fromCol];
        grid[fromRow][fromCol] = GameConstants.EMPTY;
        grid[toRow][toCol] = piece;

        boolean isCapture = false;
        int rowDiff = toRow - fromRow;
        int absRowDiff = Math.abs(rowDiff);

        if (absRowDiff > 1) { // This is a jump or king slide capture
            if (piece == GameConstants.RED_KING || piece == GameConstants.BLUE_KING) {
                // For kings, remove all enemy pieces along the path
                int rowStep = rowDiff > 0 ? 1 : -1;
                int colStep = (toCol - fromCol) > 0 ? 1 : -1;

                for (int i = 1; i < absRowDiff; i++) {
                    int currentRow = fromRow + i * rowStep;
                    int currentCol = fromCol + i * colStep;
                    char currentPiece = grid[currentRow][currentCol];

                    if (currentPiece != GameConstants.EMPTY && isEnemy(currentPiece,
                            piece == GameConstants.RED_KING || piece == GameConstants.RED_PIECE)) {
                        grid[currentRow][currentCol] = GameConstants.EMPTY;
                        List<Piece> opponentPieces = (piece == GameConstants.RED_KING
                                || piece == GameConstants.RED_PIECE)
                                        ? bluePieces
                                        : redPieces;
                        opponentPieces.removeIf(p -> p.getRow() == currentRow && p.getCol() == currentCol);
                        isCapture = true;
                    }
                }
            } else {
                // Regular piece jump
                int midRow = (fromRow + toRow) / 2;
                int midCol = (fromCol + toCol) / 2;
                grid[midRow][midCol] = GameConstants.EMPTY;
                List<Piece> opponentPieces = (piece == GameConstants.RED_PIECE || piece == GameConstants.RED_KING)
                        ? bluePieces
                        : redPieces;
                opponentPieces.removeIf(p -> p.getRow() == midRow && p.getCol() == midCol);
                isCapture = true;
            }
        }

        if (isCapture) {
            movesSinceLastCapture = 0;
        } else {
            movesSinceLastCapture++;
        }

        // Update piece in the list
        List<Piece> pieces = (piece == GameConstants.RED_PIECE || piece == GameConstants.RED_KING)
                ? redPieces
                : bluePieces;
        pieces.stream()
                .filter(p -> p.getRow() == fromRow && p.getCol() == fromCol)
                .findFirst()
                .ifPresent(p -> p.move(toRow, toCol));

        // Promote to king if reached the end
        if ((piece == GameConstants.RED_PIECE && toRow == GameConstants.BOARD_SIZE - 1) ||
                (piece == GameConstants.BLUE_PIECE && toRow == 0)) {
            grid[toRow][toCol] = (piece == GameConstants.RED_PIECE)
                    ? GameConstants.RED_KING
                    : GameConstants.BLUE_KING;
            pieces.stream()
                    .filter(p -> p.getRow() == toRow && p.getCol() == toCol)
                    .findFirst()
                    .ifPresent(Piece::promoteToKing);
        }
    }

    public boolean canContinueJumping(int row, int col, boolean isRedTurn) {
        char pieceType = grid[row][col];
        int[][] directions = getValidDirections(pieceType);

        for (int[] dir : directions) {
            // Check single jumps first
            int jumpRow = row + 2 * dir[0];
            int jumpCol = col + 2 * dir[1];
            int midRow = row + dir[0];
            int midCol = col + dir[1];

            if (isValidPosition(jumpRow, jumpCol) &&
                    grid[jumpRow][jumpCol] == GameConstants.EMPTY &&
                    isEnemy(grid[midRow][midCol], isRedTurn)) {
                return true;
            }

            // For kings, check longer jumps
            if (pieceType == GameConstants.RED_KING || pieceType == GameConstants.BLUE_KING) {
                for (int distance = 3; distance < GameConstants.BOARD_SIZE; distance++) {
                    jumpRow = row + distance * dir[0];
                    jumpCol = col + distance * dir[1];
                    if (!isValidPosition(jumpRow, jumpCol) || grid[jumpRow][jumpCol] != GameConstants.EMPTY) {
                        break;
                    }

                    // Check if there's exactly one enemy piece along the path
                    boolean foundEnemy = false;
                    for (int i = 1; i < distance; i++) {
                        int currentRow = row + i * dir[0];
                        int currentCol = col + i * dir[1];
                        if (grid[currentRow][currentCol] != GameConstants.EMPTY) {
                            if (isEnemy(grid[currentRow][currentCol], isRedTurn) && !foundEnemy) {
                                foundEnemy = true;
                            } else {
                                foundEnemy = false; // More than one piece in path
                                break;
                            }
                        }
                    }

                    if (foundEnemy) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public char[][] getGrid() {
        return grid;
    }
}