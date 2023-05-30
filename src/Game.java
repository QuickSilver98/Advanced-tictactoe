import java.util.Scanner;

public class Game {

    private static final char PLAYER_SYMBOL = 'X';
    private static final char COMPUTER_SYMBOL = 'O';
    private static final char EMPTY_SYMBOL = ' ';
    private final int boardSize;
    private boolean playerTurn;
    private final char[][] board;

    public Game(int boardSize) {
        this.boardSize = boardSize;
        board = new char[boardSize][boardSize];
        playerTurn = true;
        initializeBoard();
    }

    private static class Move{
        int row;
        int col;
        int score;

        public Move(int row,int col,int score){
            this.row = row;
            this.col = col;
            this.score = score;
        }
    }

    private void initializeBoard() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                board[row][col] = EMPTY_SYMBOL;
            }
        }
    }

    private void printBoard() {
        System.out.println();
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                System.out.print(board[row][col]);
                if (col < boardSize - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
            if (row < boardSize - 1) {
                System.out.println("-".repeat(boardSize * 4 - 1));
            }
        }
        System.out.println();
    }

    private boolean isValidMove(int row, int col) {
        if (row < 0 || row >= boardSize || col < 0 || col >= boardSize) {
            System.out.println("Invalid move! Row and column must be within the board range.");
            return false;
        }
        if (board[row][col] != EMPTY_SYMBOL) {
            System.out.println("Invalid move! The cell is already occupied.");
            return false;
        }
        return true;
    }

    private void playerMove() {
        Scanner scanner = new Scanner(System.in);
        int row, col;

        do {
            System.out.print("Enter row (1-" + boardSize + "): ");
            row = scanner.nextInt() - 1;
            System.out.print("Enter column (1-" + boardSize + "): ");
            col = scanner.nextInt() - 1;
        } while (!isValidMove(row, col));

        board[row][col] = PLAYER_SYMBOL;
    }


    private void computerMove() {
        Move bestMove = minimax(board, 0, true);
        board[bestMove.row][bestMove.col] = COMPUTER_SYMBOL;
    }


    private boolean checkWin(char symbol) {
        // Check rows and columns
        for (int i = 0; i < boardSize; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) {
                return true;
            }
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) {
                return true;
            }
        }

        // Check diagonals
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) {
            return true;
        }
        return board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol;
    }

    private boolean isBoardFull(){
        for (char[] chars : board) {
            for (char aChar : chars) {
                if (aChar == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private Move minimax(char[][] currentBoard, int depth, boolean maximizingPlayer) {
        if (checkWin(PLAYER_SYMBOL)) {
            return new Move(-1, -1, -10);
        } else if (checkWin(COMPUTER_SYMBOL)) {
            return new Move(-1, -1, 10);
        } else if (isBoardFull()) {
            return new Move(-1, -1, 0);
        }

        Move bestMove = new Move(-1, -1, maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE);

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (currentBoard[row][col] == EMPTY_SYMBOL) {
                    currentBoard[row][col] = maximizingPlayer ? COMPUTER_SYMBOL : PLAYER_SYMBOL;
                    Move move = minimax(currentBoard, depth + 1, !maximizingPlayer);
                    currentBoard[row][col] = EMPTY_SYMBOL;

                    if (maximizingPlayer) {
                        if (move.score == 10) { // Computer wins
                            bestMove.row = row;
                            bestMove.col = col;
                            bestMove.score = move.score;
                            return bestMove; // Return immediately if a winning move is found
                        } else if (move.score > bestMove.score) {
                            bestMove.row = row;
                            bestMove.col = col;
                            bestMove.score = move.score;
                        }
                    } else {
                        if (move.score == -10) { // Player wins
                            bestMove.row = row;
                            bestMove.col = col;
                            bestMove.score = move.score;
                            return bestMove; // Return immediately if a winning move is found
                        } else if (move.score < bestMove.score) {
                            bestMove.row = row;
                            bestMove.col = col;
                            bestMove.score = move.score;
                        }
                    }
                }
            }
        }

        return bestMove;
    }

    public void play() {
        System.out.println("Welcome to Tic-Tac-Toe!");

        while (true) {
            printBoard();
            if (playerTurn) {
                playerMove();
                if (checkWin(PLAYER_SYMBOL)) {
                    printBoard();
                    System.out.println("Congratulations! You won!");
                    break;
                }
            } else {
                computerMove();
                if (checkWin(COMPUTER_SYMBOL)) {
                    printBoard();
                    System.out.println("Computer wins! You lose!");
                    break;
                }
            }

            if (isBoardFull()) {
                printBoard();
                System.out.println("It's a tie!");
                break;
            }

            playerTurn = !playerTurn;
        }
    }
}
