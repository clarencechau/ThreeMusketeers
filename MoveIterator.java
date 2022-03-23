package assignment1;

import java.util.Iterator;
import java.util.List;

public class MoveIterator implements Iterator<Move>{
	private Move[] moves;
	private int index;
	 
	public MoveIterator(List<Move> moves) {
		this.moves = new Move[moves.size()];
        this.moves = moves.toArray(this.moves);
		// set index after the final move
		this.index = this.moves.length;
		// remember not to refer directly to moves[this.moves.length]
		// as that index is out of bounds!
	}
 
	public boolean hasNext() {
		return index < this.getMovesLength();
	}
	
	public boolean hasPrevious() {
		return index > 0;
	}

	public int getMovesLength() {
		return this.moves.length;
	}
 
	public Move next() {
		// checks the current index, then moves forward
		if (!this.hasNext()) return null;
		Move current = this.current();
		index++;
		return new Move(current.toCell, current.fromCell);
	}

	public Move previous() {
		// moves back and immediately checks the resultant index
		// this is because starting point is outside range (when the game ends)
		if (!this.hasPrevious()) return null;
		index--;  // could use moves[index--] but this crashes upon the first move
		return new Move(this.current());
	}

	public Move current() {
		return moves[index];
	}

	public int getMoveNumber() {
		// index is 0 at the start of the game, and the length of moves at the end
		return index;
	}
}
