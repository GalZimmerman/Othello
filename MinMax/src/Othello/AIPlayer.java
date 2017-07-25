package Othello;

/**
 * Abstract superclass for all AI players with different strategies.
 * To construct an AI player:
 * 1. Construct an instance (of its subclass) with the game Board
 * 2. Call setSeed() to set the computer's seed
 * 3. Call move() which returns the next move in an int[2] array of {row, col}.
 *
 * The implementation subclasses need to override abstract method move().
 * They shall not modify Cell[][], i.e., no side effect expected.
 * Assume that next move is available, i.e., not game-over yet.
 */
public abstract class AIPlayer {
	protected int ROWS = Othello.ROWS;  // number of rows
	protected int COLS = Othello.COLS;  // number of columns

	//   protected Cell[][] cells; // the board's ROWS-by-COLS array of Cells
	protected Seed mySeed;    // computer's seed
	protected Seed oppSeed;   // opponent's seed
	protected Board  myBoard;


	//   /** Constructor with reference to game board */
	//   public AIPlayer(Board board) {
	////      cells = board.cells;
	//   }

	/** Set/change the seed used by computer and opponent */
	public void setSeed(Seed seed) {
		this.mySeed = seed;
		oppSeed = (mySeed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
	}
	
	/**
	 * Return true if play can make a move
	 * @param seed: the play seed
	 * @return true if has at least one move
	 */
//	abstract public boolean hasMoves(Seed abSeed);
	public boolean hasMoves( Seed seed ) {

		// Search for empty cells and add to the List
		for (int row = 0; row < ROWS; ++row) {
			for (int col = 0; col < COLS; ++col) {
				if ( myBoard.isAvailable(row, col, seed) ) {
					return true;
				}
			}
		}
		return false;
	}


	/** Abstract method to get next move. Return int[2] of {row, col} 
	 * @param turnNo */
	abstract int[] move(int turnNo);  // to be implemented by subclasses


	abstract public boolean isGameOver();

	abstract public int getNumberOfNodes();

	abstract String getPlayerConfig();
}
