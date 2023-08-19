import javax.swing.*;

public class GameController {

    private TicTacToe gameInstance;

    public GameController() {
        startNewGame();
    }

    public void startNewGame() {
        if (gameInstance != null) {
            gameInstance.dispose(); // Close the previous game window
        }
        gameInstance = new TicTacToe(this); // Pass the controller to the game instance
    }

    public void gameEnded() {
        int option = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            startNewGame();
        } else {
            System.exit(0); // Exit the application if the user doesn't want to play again
        }
    }

    public static void main(String[] args) {
        new GameController();
    }
}
