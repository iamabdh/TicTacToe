import java.util.Scanner;
/**
 * @author Abdullah Salim
 *  TicTacToe game class
 *  The game is CLI game 3x3, it accepts two player with Symbol chars, one player win. 
 */
public class TicTacToe {
	
    static char[][] board = new char[][]{{'1', '2', '3'}, {'4', '5', '6'}, {'7', '8', '9'}};
    
    /*
     * gameWinningPossibilites 2D array has wining combination such if one player has selected one of these 
     * such as 1, 2, 3 will win. 
     * playerWin function depends on it for evaluate winning player.
     */
    static int[][] gameWinningPossibilities = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    static char symbolPlayerOne = ' ';
    static char symbolPlayerTwo = ' ';

    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);
        do {
            System.out.println("Symbol for player one (such as X or O): ");
            symbolPlayerOne = inputScanner.next().charAt(0);
            System.out.println("Symbol for player two (such as X or O): ");
            symbolPlayerTwo = inputScanner.next().charAt(0);
            if (symbolPlayerOne == symbolPlayerTwo) {
                System.out.println("Invalid name for players, players cannot have the same name :(\n");
            }
        } while (symbolPlayerOne == symbolPlayerTwo);
        
        // boolean value for toggling current player
        boolean togglePlayer = true;
        // gameTryCount used to count the number of tries for game.
        int gameTryCount = 0;
        do {
            char currentPlayer;
            if (togglePlayer) {
               currentPlayer = symbolPlayerOne;
            } else {
                currentPlayer = symbolPlayerTwo;
            }
            displayBoard();
            System.out.print("Player symbol: "+ currentPlayer + "\n");
            System.out.println("Choose place number (such as 1 or 2 .. 9): ");
            int placeNo = inputScanner.nextInt();
            int[] rowColumn = getRowColumn(placeNo);
            int column = rowColumn[1];
            int row = rowColumn[0];
            if (checkValidInput(placeNo, row, column)) {
                board[row][column] = currentPlayer;
                togglePlayer = !togglePlayer;
                gameTryCount++;
                if (playerWin(currentPlayer)) {
                    System.out.println("Player "+ currentPlayer +" win :)");
                    break;
                }
            } else {
                System.out.println("Invalid input: you may type used place or place number not exists :)");
            }
        } while (!(gameTryCount >= 9));
    }

    /**
     * displayBoard used to display board of the game with choosed places
     */
    public static void displayBoard() {
        System.out.println();
        for (int row = 0; row < 3; row++){
            for (int column = 0; column < 3; column++) {
                if (column == 0 || column == 2) {
                    System.out.print(board[row][column]);
                } else {
                    System.out.print("  |  ");
                    System.out.print(board[row][column]);
                    System.out.print("  |  ");
                }
            }
            if (row == 0 || row == 1)  {
                System.out.println("\n--------------");
            }
        }
        System.out.println("\n\n");
    }
    /*
     * checkVaildInput used to check valid input from player, it return boolean value.
     * The function has two condition to validate first condition is the place between (1 .. 9),
     * second condition used for is the place has been used or not. 
     */
    public static boolean checkValidInput(int playerChoosedPlace, int row, int column) {
        if (playerChoosedPlace < 1 || playerChoosedPlace > 10) {
            return false;
        } else return board[row][column] != symbolPlayerOne && board[row][column] != symbolPlayerTwo;
    }
    
    /*
     * playerWin used to check if the player win, it returns boolean value.
     * it iterate in gameWinningPossibilites to lookup if there's winning player
     * @param currentPlayer char symbol
     */
    public static boolean playerWin(char currentPlayer) {
        for (int[] gameWiningPossibility : gameWinningPossibilities) {
            int winningPossibleStage = 0;
            for (int columnWinningCombination = 0; columnWinningCombination < 3; columnWinningCombination++) {
                int[] rowColumn = getRowColumn(gameWiningPossibility[columnWinningCombination]);
                int row = rowColumn[0];
                int column = rowColumn[1];
                if (board[row][column] == currentPlayer) {
                    winningPossibleStage++;
                } else {
                    continue;
                }
            }
            if (winningPossibleStage == 3) {
                return true;
            }
        }
        return false;
    }

    /**
     * getRowColumn used to perform specific calculation for getting row and column as index of board
     */
    public static int[] getRowColumn(int number) {
        return new int[] {(int)Math.ceil(number / 3.0) - 1,  (number - 1) % 3};
    }

}
