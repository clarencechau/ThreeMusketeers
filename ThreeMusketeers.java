package assignment1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import commands.ConsoleListener;

public class ThreeMusketeers {

    private final Board board;
    private Agent musketeerAgent, guardAgent;
    private final Scanner scanner = new Scanner(System.in);
    private final List<Move> moves = new ArrayList<>();
    private ConsoleListener consoleListener;

    // All possible game modes
    public enum GameMode {
        Human("Human vs Human"),
        HumanRandom("Human vs Computer (Random)"),
        HumanGreedy("Human vs Computer (Greedy)");

        private final String gameMode;
        GameMode(final String gameMode) {
            this.gameMode = gameMode;
        }
    }

    /**
     * Default constructor to load Starter board
     */
    public ThreeMusketeers() {
        this.board = new Board();
        this.consoleListener = new ConsoleListener(this);
    }

    /**
     * Constructor to load custom board
     * @param boardFilePath filepath of custom board
     */
    public ThreeMusketeers(String boardFilePath) {
        this.board = new Board(boardFilePath);
        this.consoleListener = new ConsoleListener(this);
    }

    /**
     * Play game with human input mode selector
     */
    public void play(){
        System.out.println("Welcome! \n");
        final GameMode mode = getModeInput();
        System.out.println("Playing " + mode.gameMode);
        play(mode);
    }

    /**
     * Play game without human input mode selector
     * @param mode the GameMode to run
     */
    public void play(GameMode mode){
        selectMode(mode);
        runGame();
    }

    /**
     * Mode selector sets the correct agents based on the given GameMode
     * @param mode the selected GameMode
     */
    private void selectMode(GameMode mode) {
        switch (mode) {
            case Human -> {
                musketeerAgent = new HumanAgent(board, consoleListener);
                guardAgent = new HumanAgent(board, consoleListener);
            }
            case HumanRandom -> {
                String side = getSideInput();
                
                // The following statement may look weird, but it's what is known as a ternary statement.
                // Essentially, it sets musketeerAgent equal to a new HumanAgent if the value M is entered,
                // Otherwise, it sets musketeerAgent equal to a new RandomAgent
                musketeerAgent = side.equals("M") ? new HumanAgent(board, consoleListener) : new RandomAgent(board);
                
                guardAgent = side.equals("G") ? new HumanAgent(board, consoleListener) : new RandomAgent(board);
            }
            case HumanGreedy -> {
                String side = getSideInput();
                musketeerAgent = side.equals("M") ? new HumanAgent(board, consoleListener) : new GreedyAgent(board);
                guardAgent = side.equals("G") ? new HumanAgent(board, consoleListener) : new GreedyAgent(board);
            }
        }
    }

    /**
     * Runs the game, handling human input for move actions
     * Handles moves for different agents based on current turn 
     */
    private void runGame() {
        while(!board.isGameOver()) {
            System.out.println("\n" + board);

            final Agent currentAgent;
            if (board.getTurn() == Piece.Type.MUSKETEER)
                currentAgent = musketeerAgent;
            else
                currentAgent = guardAgent;

            if (currentAgent instanceof HumanAgent) { // Human move
            	
            	// we let the console listener handle human input for M/U/S/L and it will run the command in the game to complete the move. 
            	consoleListener.getInputOption(board.getTurn().getType());
            	
            	// left in comments until Story 1.2 is done.
//                switch (getInputOption()) {
//                    case "M":
//                        move(currentAgent);
//                        break;
//                    case "U":
//                        if (moves.size() == 0) {
//                            System.out.println("No moves to undo.");
//                            continue;
//                        }
//                        else if (moves.size() == 1 || isHumansPlaying()) {
//                            undoMove();
//                        }
//                        else {
//                            undoMove();
//                            undoMove();
//                        }
//                        break;
//                    case "S": // this case can be left out.
//                        board.saveBoard();
//                        break;
//                }
            }
            
            else { // Computer move
                System.out.printf("[%s] Calculating move...\n", currentAgent.getClass().getSimpleName());
                move(currentAgent);
            }
        }

        System.out.println(board);
        System.out.printf("\n%s won!%n", board.getWinner().getType());
        endGame();  // initiate end game sequence
    }

    /**
     * Gets a move from the given agent, adds a copy of the move using the copy constructor to the moves stack, and
     * does the move on the board.
     * @param agent Agent to get the move from.
     */
    protected void move(final Agent agent) {
        final Move move = agent.getMove();
        moves.add(new Move(move));
        board.move(move);
    }

    /**
     * Removes a move from the top of the moves stack and undoes the move on the board.
     */
    private void undoMove() {
        board.undoMove(moves.remove(moves.size() - 1));
        System.out.println("Undid the previous move.");
    }

    /**
     * Get human input for move action
     * @return the selected move action, 'M': move, 'U': undo, and 'S': save
     */
    private String getInputOption() {
        System.out.printf("[%s] Enter 'M' to move, 'U' to undo, and 'S' to save: ", board.getTurn().getType());
        while (!scanner.hasNext("[MUSmus]")) {
            System.out.print("Invalid option. Enter 'M', 'U', or 'S': ");
            scanner.next();
        }
        return scanner.next().toUpperCase();
    }

    /**
     * Returns whether both sides are human players
     * @return True if both sides are Human, False if one of the sides is a computer
     */
    private boolean isHumansPlaying() {
        return musketeerAgent instanceof HumanAgent && guardAgent instanceof HumanAgent;
    }

    /**
     * Get human input for side selection
     * @return the selected Human side for Human vs Computer games,  'M': Musketeer, G': Guard
     */
    private String getSideInput() {
    	return consoleListener.getSideInput();
//        System.out.print("Enter 'M' to be a Musketeer or 'G' to be a Guard: ");
//        while (!scanner.hasNext("[MGmg]")) {
//            System.out.println("Invalid option. Enter 'M' or 'G': ");
//            scanner.next();
//        }
//        return scanner.next().toUpperCase();
    }

    /**
     * Get human input for selecting the game mode
     * @return the chosen GameMode
     */
    private GameMode getModeInput() {
    	return consoleListener.getModeInput();
//        System.out.println("""
//                    0: Human vs Human
//                    1: Human vs Computer (Random)
//                    2: Human vs Computer (Greedy)""");
//        System.out.print("Choose a game mode to play i.e. enter a number: ");
//        while (!scanner.hasNextInt()) {
//            System.out.print("Invalid option. Enter 0, 1, or 2: ");
//            scanner.next();
//        }
//        final int mode = scanner.nextInt();
//        if (mode < 0 || mode > 2) {
//            System.out.println("Invalid option.");
//            return getModeInput();
//        }
//        return GameMode.values()[mode];
    }
    
    // IMPORTANT**:
    // these 4 functions align with the 4 types of commands the console will try and execute while the game is running. Check ThreeMusketeers.runGame() for the commented-out switch block for help on what these functions should try to accomplish.
    // Comments need to be done for these 4 functions as well.
    public void doMove() {
    	final Agent currentAgent;
    	if (board.getTurn() == Piece.Type.MUSKETEER) {
    		currentAgent = musketeerAgent;
    	} else {
    		currentAgent = guardAgent;
    	}
    	move(currentAgent);
    }
    
    public void doSave(String fileName) {
    	// saves the current game state to the file (TBD in S2)
    	System.out.println("Would have done doSave");
    }
    
    public void doLoad(String fileName) {  // to be done in S2
    	// loads the game
    	System.out.println("Would have done doLoad");
    }

    public void doUndoMove() {
    	// undoes the last move
    	if (moves.size() == 0) {
    		System.out.println("No moves to undo.");
    	}
    	else if (moves.size() == 1 || isHumansPlaying()) {
    		undoMove();
    	}
    	else {
    		undoMove();
    		undoMove();
    	}
    }

    private void endGame() {
    	// Plays when the game is over
    	System.out.println("\n\nGame Over! You can watch an interactive replay of this game if you want...");
    	System.out.print("Enter either 'Y' or 'N': ");
        while (!scanner.hasNext("[YNyn]")) {
            System.out.println("Invalid option. Enter either 'Y' or 'N': ");
            scanner.next();
        }
        String choice = scanner.next().toUpperCase();
        if (choice.equals("Y")) {  // choice to initiate action replay mode
        	ReplayHandler replay = new ReplayHandler(moves, board);
        	replay.run();
        }
    }

    public static void main(String[] args) {
        String boardFileName = "Boards/Starter.txt";
        Scanner scan = new Scanner(System.in);
        boolean restart = true;
        // Keep starting a game until user prompts to exit
        while (restart) {
	        ThreeMusketeers game = new ThreeMusketeers(boardFileName);
	        game.play();  // play the game

	        System.out.print("\n\n\nWould you like to play again? Enter 'Y' or 'N': ");
	        while (!scan.hasNext("[YNyn]")) {  // prompt to play again (restart)
	            System.out.print("Invalid option. Enter either 'Y' or 'N': ");
	            scan.next();
	        }
	        String choice = scan.next().toUpperCase();
	        if (choice.equals("N")) restart = false;  // user leaves; exit while loop
	        // System.out.println(booleanCondition ? valueIfTrue : valueIfFalse);
	        System.out.println(restart ? "Restarting...\n\n\n" : "Thanks for playing!");
        }
        scan.close();  // program ends
    }
}
