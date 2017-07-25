package Othello;

public class MinMaxState {

	public	int[]		move;
	public	Seed		mySeed;
	public	Board 		board;
	public	MinMaxState parent = null;
	public	int			heuristic = 0;
	public	boolean		isMax;

	/**
	 * None root node constructor
	 * @param mov: move from initial board
	 * @param b: the board
	 * @param p: the parent
	 */
	MinMaxState( int[] mov, Board b, MinMaxState p, int heuristicVal )
	{
		move = mov;
		board = b;
		parent = p;
		isMax = !p.isMax;
		mySeed = p.mySeed.flip();
		heuristic = heuristicVal;
	}

	/**
	 * root constructor
	 * @param heuristicVal 
	 * @param ismax 
	 * @param mov: move from initial board
	 * @param b: the board
	 * @param rootSeed: root seed
	 */
	MinMaxState( int[] mov, Board b, Seed rootSeed, int heuristicVal, boolean ismax )
	{
		move = mov;
		board = b;
		parent = null;
		isMax = ismax;
		mySeed = rootSeed;
		heuristic = heuristicVal;
	}
	int getH() {
		return heuristic;
	}

	public int[] getInitialMove() {
		MinMaxState prev = this;
		MinMaxState next = parent;
		while ( next != null ) {
			prev = next;
			next = next.parent;
		}
		return prev.move;
	}

}
