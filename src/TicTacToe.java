import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
/**
 * @author Abdullah Salim
 *  TicTacToe game class
 *  The game is CLI game 3x3, it accepts two player with Symbol chars, one player win. 
 */

import javax.sound.midi.VoiceStatus;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
public class TicTacToe {
	
    static char[][] board = new char[][]{{'1', '2', '3'}, {'4', '5', '6'}, {'7', '8', '9'}};
    static ArrayList<ArrayList<Character>> boardArrayList = new ArrayList<ArrayList<Character>>();
    public static HashMap<String, ArrayList<ArrayList<Character>>> TicTacToeGameData = null;
    public final static String DATA_FILE_TICTACTOE = "tic/data.json";
    public static Gson gson = new Gson();
    /*
     * gameWinningPossibilites 2D array has wining combination such if one player has selected one of these 
     * such as 1, 2, 3 will win. 
     * playerWin function depends on it for evaluate winning player.
     */
    
    static int[][] gameWinningPossibilities = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    static Character symbolPlayerOneSaved = ' ';
    static Character symbolPlayerTwoSaved = ' ';
    static Character currentPlayerSaved = ' ';
    static int gameCountInteger = 0;
    
    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);
        
        // load data from file
        TicTacToeGameData = gson.fromJson(LoadFileData(), new TypeToken<HashMap<String, ArrayList<ArrayList<Character>>>>(){}.getType());
        
        if (TicTacToeGameData != null) {
        	// data loaded 
        	boardArrayList = TicTacToeGameData.get("board");
        	symbolPlayerOneSaved = TicTacToeGameData.get("player").get(0).get(0);
        	symbolPlayerTwoSaved = TicTacToeGameData.get("player").get(0).get(1);
        	currentPlayerSaved = TicTacToeGameData.get("current").get(0).get(0);
        	gameCountInteger = (int)TicTacToeGameData.get("count").get(0).get(0) - 48;
        	System.out.println("Count game: "+ gameCountInteger);
//        	if (gameCharacter != null) {
//        		System.out.println(gameCharacter);
//        		gameCountInteger = Integer.parseInt(String.valueOf(gameCharacter));
//        	}
        	
        } else {
        	
        	// data not exist load these into file
        	do {
                System.out.println("Symbol for player one (such as X or O): ");
                symbolPlayerOneSaved = inputScanner.next().charAt(0);
                System.out.println("Symbol for player two (such as X or O): ");
                symbolPlayerTwoSaved = inputScanner.next().charAt(0);
                if (symbolPlayerOneSaved == symbolPlayerTwoSaved) {
                    System.out.println("Invalid name for players, players cannot have the same name :(\n");
                }
            } while (symbolPlayerOneSaved == symbolPlayerTwoSaved);
        	
        	// initialize new values to board
        	InitializeBoard();
        }
        
        
        
        // boolean value for toggling current player
        boolean togglePlayer = currentPlayerSaved == symbolPlayerOneSaved;
        // boolean isPlayerWin : flag used to exist loop
        boolean isPlayerWin = false;
        do {
            if (togglePlayer) {
               currentPlayerSaved = symbolPlayerOneSaved;
            } else {
                currentPlayerSaved = symbolPlayerTwoSaved;
            }
            
            // save current player
            SaveCurrentPlayer();
            displayBoard();
            
            System.out.print("Player symbol: "+ currentPlayerSaved + "\n");
            System.out.println("Choose place number (such as 1 or 2 .. 9): ");
            int placeNo = inputScanner.nextInt();
            int[] rowColumn = getRowColumn(placeNo);
            int column = rowColumn[1];
            int row = rowColumn[0];
            if (checkValidInput(placeNo, row, column)) {
                gameCountInteger++;
            	SaveMoveAndCount(row, column, gameCountInteger);
                togglePlayer = !togglePlayer;
                if (playerWin(currentPlayerSaved)) {
                    isPlayerWin = true;
                }
            } else {
                System.out.println("Invalid input: you may type used place or place number not exists :)");
            }
        } while (!(gameCountInteger >= 9) && !isPlayerWin);
        
        if (isPlayerWin) {
        	 System.out.println("Player "+ currentPlayerSaved +" win :)");
        	 ClearData();
        } else {
        	 System.out.println("Draw :(");
        	 ClearData();
        }
    }

    /**
     * displayBoard used to display board of the game with choosed places
     */
    public static void displayBoard() {
        System.out.println();
        for (int row = 0; row < 3; row++){
            for (int column = 0; column < 3; column++) {
                if (column == 0 || column == 2) {
                    System.out.print(boardArrayList.get(row).get(column));
                } else {
                    System.out.print("  |  ");
                    System.out.print(boardArrayList.get(row).get(column));
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
        } else return boardArrayList.get(row).get(column) != symbolPlayerOneSaved && boardArrayList.get(row).get(column) != symbolPlayerTwoSaved;
    }
    
    /*
     * playerWin used to check if the player win, it returns boolean value.
     * it iterate in gameWinningPossibilites to lookup if there's winning player
     * @param currentPlayer char symbol
     */
    public static boolean playerWin(char currentPlayer) {
    	int winningPossibleCount = 0;
        for (int winnigComintation = 0; winnigComintation < gameWinningPossibilities.length && winningPossibleCount != 3; winnigComintation ++) {
        	winningPossibleCount = 0;
            for (int columnWinningCombination = 0; columnWinningCombination < gameWinningPossibilities[winnigComintation].length; columnWinningCombination++) {
                int[] rowColumn = getRowColumn(gameWinningPossibilities[winnigComintation][columnWinningCombination]);
                int row = rowColumn[0];
                int column = rowColumn[1];
                if (boardArrayList.get(row).get(column)== currentPlayer) {
                    winningPossibleCount++;
                }
            }
        }
        return winningPossibleCount == 3;
    }

    /**
     * getRowColumn used to perform specific calculation for getting row and column as index of board
     * for example: if place number selected 3 it return [0, 2] as index for the board
     */
    public static int[] getRowColumn(int number) {
        return new int[] {(int)Math.ceil(number / 3.0) - 1,  (number - 1) % 3};
    }
    
    
    
	public static String LoadFileData() {
		File dataFile = new File(TicTacToe.DATA_FILE_TICTACTOE);
		String dataStudentJsonString = new String();

		if (dataFile.exists()) {
			System.out.println("File Found");
			Scanner filScanner;
			try {
				filScanner = new Scanner(dataFile);
				while (filScanner.hasNextLine()) {
					dataStudentJsonString += filScanner.nextLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("File not Found");
			try {
				System.out.println("creating new File: " + TicTacToe.DATA_FILE_TICTACTOE);
				FileWriter newFileWriter = new FileWriter(TicTacToe.DATA_FILE_TICTACTOE);
				newFileWriter.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		return dataStudentJsonString;
	}
	
	public static void SaveDataIntoFile() {
		String jsonString = gson.toJson(TicTacToeGameData);
		try {
			FileWriter newFileWriter = new FileWriter(TicTacToe.DATA_FILE_TICTACTOE);
			newFileWriter.write(jsonString);
			newFileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void InitializeBoard() {
		TicTacToeGameData = new HashMap<String, ArrayList<ArrayList<Character>>>(); 
		
		ArrayList<Character> firstRowArrayList = new ArrayList<Character>();
		ArrayList<Character> secondRowArrayList = new ArrayList<Character>();
		ArrayList<Character> thirdRowArrayList = new ArrayList<Character>();
		ArrayList<Character> playersSymboArrayList = new ArrayList<Character>();
		ArrayList<ArrayList<Character>> playerSymbolArrayListList = new ArrayList<ArrayList<Character>>(); 
		ArrayList<Character> currentPlayerNewArrayList = new ArrayList<Character>(); // assign to first player
		ArrayList<ArrayList<Character>> currentPlayerNewArrayListList = new ArrayList<ArrayList<Character>>(); // assign to first player		
		ArrayList<Character> gameCountArrayList = new ArrayList<Character>();
		ArrayList<ArrayList<Character>> gameCountArrayListList = new ArrayList<ArrayList<Character>>();
		
		
		
		
		firstRowArrayList.add('1');
		firstRowArrayList.add('2');
		firstRowArrayList.add('3');
		
		secondRowArrayList.add('4');
		secondRowArrayList.add('5');
		secondRowArrayList.add('6');
	
		thirdRowArrayList.add('7');
		thirdRowArrayList.add('8');
		thirdRowArrayList.add('9');
		
		boardArrayList.add(firstRowArrayList);
		boardArrayList.add(secondRowArrayList);
		boardArrayList.add(thirdRowArrayList);
		
		playersSymboArrayList.add(symbolPlayerOneSaved);
		playersSymboArrayList.add(symbolPlayerTwoSaved);
		playerSymbolArrayListList.add(playersSymboArrayList);
		
		
		currentPlayerSaved = symbolPlayerOneSaved;
		currentPlayerNewArrayList.add(currentPlayerSaved);
		currentPlayerNewArrayListList.add(currentPlayerNewArrayList);
		
		gameCountArrayList.add('0');
		gameCountArrayListList.add(gameCountArrayList);
		
		TicTacToeGameData.put("board", boardArrayList);
		TicTacToeGameData.put("player", playerSymbolArrayListList);
		TicTacToeGameData.put("current", currentPlayerNewArrayListList);
		TicTacToeGameData.put("count", gameCountArrayListList);
		SaveDataIntoFile();
	}
	
	// saveCurrentPlayer
	
	public static void SaveCurrentPlayer() {
		ArrayList<Character> currentPlayerNewArrayList = new ArrayList<Character>(); // assign to first player
		ArrayList<ArrayList<Character>> currentPlayerNewArrayListList = new ArrayList<ArrayList<Character>>(); // assign to first player
		currentPlayerNewArrayList.add(currentPlayerSaved);
		currentPlayerNewArrayListList.add(currentPlayerNewArrayList);
		TicTacToeGameData.put("current", currentPlayerNewArrayListList);
		SaveDataIntoFile();
	}
	
	public static void SaveMoveAndCount(int row, int column, int count) {
		boardArrayList.get(row).set(column, currentPlayerSaved);
		// update count value
		ArrayList<ArrayList<Character>> countGameCount = TicTacToeGameData.get("count");
		countGameCount.get(0).set(0, (char)count);
		TicTacToeGameData.put("board", boardArrayList);
		TicTacToeGameData.put("count", countGameCount);
		SaveDataIntoFile();
	}
	
	public static void ClearData() {
		File file = new File(DATA_FILE_TICTACTOE);
		file.delete();
	}

}
