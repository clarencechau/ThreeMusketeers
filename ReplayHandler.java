package assignment1;

import java.util.List;
import java.util.Scanner;

public class ReplayHandler {
	private MoveIterator moveIterator;
	private Board board;
	private final Scanner scanner = new Scanner(System.in);
	private boolean active = true;

	public ReplayHandler(List<Move> moves, Board board) {
		this.moveIterator = new MoveIterator(moves);
		this.board = board;
	}

	public boolean isReplayOver() {
		// Returns if the user has exited action replay mode.
		return !this.active;
	}

	public void run() {
		System.out.print("\nACTION REPLAY Mode\n" + board);
		if (moveIterator.getMovesLength() == 0) {
			System.out.println("No moves to choose from. Exiting Action Replay Mode.");
			return;
		}
		while (!this.isReplayOver()) {
			// User input begins
			int moveNum = moveIterator.getMoveNumber();
			System.out.print("\nCurrently on Move " + moveNum);
			if (moveNum == 0) System.out.print(" (Start)");
			else if (!moveIterator.hasNext()) System.out.print(" (End)");
			System.out.println("""
                    \nU: Undo Move -------- R: Redo Move -------- Q: Quit Action Replay Mode""");
			System.out.print("Choose an option: ");
			while (!scanner.hasNext("[UuRrQq]")) {
				System.out.print("Invalid option. Enter U (Undo), R (Redo), or Q (Quit): ");
				scanner.next();
			}
			String choice = scanner.next().toUpperCase();
			if (choice.equals("U")) undo();
			else if (choice.equals("R")) redo();
			else if (choice.equals("Q")) this.active = false;
			// User input ends
		}
		System.out.println("\n\nExiting Action Replay Mode.\n");
	}

	public void undo() {
		// Receives a move from moveIterator to undo from the board.
		Move move = moveIterator.previous();
		if (move == null) {
			System.out.println("\nNo moves to undo.\n");
			return;
		}
		board.undoMove(move);
		System.out.println("\nUndo -> Move " + moveIterator.getMoveNumber() + "\n" + board);
	}

	public void redo() {
		// Receives a move from moveIterator to redo on the board.
		Move move = moveIterator.next();
		if (move == null) {
			System.out.println("\nNo moves to redo.\n");
			return;
		}
		board.redoMove(move);
		System.out.println("\nRedo -> Move " + moveIterator.getMoveNumber() + "\n" + board);
	}
}
