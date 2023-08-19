import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;

public class TicTacToe {

    private JFrame frame;
    private JButton[][] buttons;
    private char[][] board;
    private char currentPlayer;
    private boolean gameEnded;
    private GameController controller;

    public TicTacToe(GameController controller) {
        this.controller = controller;

        frame = new JFrame("Tic Tac Toe");
        buttons = new JButton[3][3];
        board = new char[3][3];
        currentPlayer = 'X';
        gameEnded = false;

        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                frame.add(buttons[i][j]);
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (gameEnded) {
                            resetGame();
                            return;
                        }

                        JButton clickedButton = (JButton) e.getSource();
                        clickedButton.setText(String.valueOf(currentPlayer));
                        clickedButton.setEnabled(false);
                        updateBoard();

                        if (hasContestantWon(currentPlayer)) {
                            gameEnded = true;
                            JOptionPane.showMessageDialog(frame, currentPlayer + " wins!");
                            resetGame();
                            return;
                        } else if (isBoardFull()) {
                            gameEnded = true;
                            JOptionPane.showMessageDialog(frame, "It's a tie!");
                            resetGame();
                            return;
                        }

                        // Switch to the other player
                        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';

                        // Bot's turn
                        if (currentPlayer == 'O') {
                            botMove();
                            updateBoard();
                            if (hasContestantWon(currentPlayer)) {
                                gameEnded = true;
                                JOptionPane.showMessageDialog(frame, currentPlayer + " wins!");
                                resetGame();
                            } else if (isBoardFull()) {
                                gameEnded = true;
                                JOptionPane.showMessageDialog(frame, "It's a tie!");
                                resetGame();
                            }
                            currentPlayer = 'X';
                        }
                    }
                });
            }
        }

        frame.setVisible(true);
    }

    private void botMove() {
        int bestScore = Integer.MIN_VALUE;
        Point bestMove = new Point(-1, -1);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    board[i][j] = 'O';
                    int currentScore = minimax(board, 'X');
                    board[i][j] = '\0';

                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestMove.x = i;
                        bestMove.y = j;
                    }
                }
            }
        }

        buttons[bestMove.x][bestMove.y].setText("O");
        buttons[bestMove.x][bestMove.y].setEnabled(false);
    }

    private int minimax(char[][] board, char currentPlayer) {
        char opponent = (currentPlayer == 'O') ? 'X' : 'O';

        if (hasContestantWon('O')) {
            return 10;
        }
        if (hasContestantWon('X')) {
            return -10;
        }
        if (isBoardFull()) {
            return 0;
        }

        int score = (currentPlayer == 'O') ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    board[i][j] = currentPlayer;
                    if (currentPlayer == 'O') {
                        score = Math.max(score, minimax(board, 'X'));
                    } else {
                        score = Math.min(score, minimax(board, 'O'));
                    }
                    board[i][j] = '\0';
                }
            }
        }

        return score;
    }

    private void updateBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!buttons[i][j].getText().isEmpty()) {
                    board[i][j] = buttons[i][j].getText().charAt(0);
                } else {
                    board[i][j] = '\0';
                }
            }
        }
    }

    private boolean hasContestantWon(char symbol) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) {
                return true;
            }
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) {
                return true;
            }
        }
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) {
            return true;
        }
        if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetGame() {
        controller.gameEnded();
    }

    public void dispose() {
        frame.dispose();
    }
}
