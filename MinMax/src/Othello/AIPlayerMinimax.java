package Othello;
/* ----------------------------------------------------------------------------------------------------------------------
minimax(level, player)  // player may be "computer" or "opponent"
if (gameover || level == 0)
   return score
children = all legal moves for this player
if (player is computer, i.e., maximizing player)
   // find max
   bestScore = -inf
   for each child
      score = minimax(level - 1, opponent)
      if (score > bestScore) bestScore = score
   return bestScore
else (player is opponent, i.e., minimizing player)
   // find min
   bestScore = +inf
   for each child
      score = minimax(level - 1, computer)
      if (score < bestScore) bestScore = score
   return bestScore

// Initial Call
minimax(2, computer)
--------------------------------------------------------------------------------------------------------------------- */


import java.util.*;


/** AIPlayer using Minimax algorithm */
public class AIPlayerMinimax extends AIPlayer {
	protected static  int MaxNumberOfNoedes = Integer.MAX_VALUE;
	protected static int MAX_SEARCH_DEPTH = 7;
	protected long startMovTime;
	public int numberOfnodes;
	public IOthelloHeuristic myHeuristic;
	

	/** Constructor with the given game board 
	 * @param heuristic */
	public AIPlayerMinimax(Board board, int maxSearchDepth, int maxNumberOfNodes, IOthelloHeuristic heuristic) {
		myBoard = board;
		MAX_SEARCH_DEPTH = maxSearchDepth;
		MaxNumberOfNoedes = maxNumberOfNodes;
		myHeuristic = heuristic;
	}

	/** Get next best move for computer. Return int[2] of {row, col} */
	@Override
	int[] move( int turnNo ) {
		startMovTime = System.currentTimeMillis();
		numberOfnodes = 0;
		int[] result = minimax(MAX_SEARCH_DEPTH, mySeed); // depth, max turn
		return new int[] {result[1], result[2]};   // row, col
	}

	/** Recursive minimax at level of depth for either maximizing or minimizing player.
       Return int[3] of {score, row, col}  */
	private int[] minimax(int depth, Seed player) {
		// Generate possible next moves in a List of int[2] of {row, col}.
		List<int[]> nextMoves = generateMoves( player );

		// mySeed is maximizing; while oppSeed is minimizing
		int bestScore = (player == mySeed) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		int currentScore;
		int bestRow = -1;
		int bestCol = -1;

		// Gameover or depth reached, evaluate score
		if ( nextMoves.isEmpty() || depth == 0 ||
			( System.currentTimeMillis() - startMovTime > 60000) || // Break the search in case time pass
			numberOfnodes >= MaxNumberOfNoedes )
		{
			bestScore = myHeuristic.evaluate( myBoard, mySeed, oppSeed);
		} else {
			for (int[] move : nextMoves) {
				Board keepBoard = myBoard;
				myBoard = myBoard.clone();
				// Try this move for the current "player"
				myBoard.setMove( move[0], move[1], player );
				if (player == mySeed) {  // mySeed (computer) is maximizing player
					currentScore = minimax(depth - 1, oppSeed)[0];
					if (currentScore > bestScore) {
						bestScore = currentScore;
						bestRow = move[0];
						bestCol = move[1];
					}
				} else {  // oppSeed is minimizing player
					currentScore = minimax(depth - 1, mySeed)[0];
					if (currentScore < bestScore) {
						bestScore = currentScore;
						bestRow = move[0];
						bestCol = move[1];
					}
				}
				// Undo move
				myBoard = keepBoard;
				numberOfnodes += 1;
				
			}
		}
		return new int[] {bestScore, bestRow, bestCol};
	}


	/** Find all valid next moves.
    Return List of moves in int[2] of {row, col} or empty list if gameover */
	private List<int[]> generateMoves( Seed seed ) {
		
		List<int[]> nextMoves = new ArrayList<int[]>(); // allocate List
		return generateMoves( seed, myBoard );
	}
	
	/** Find all valid next moves.
    Return List of moves in int[2] of {row, col} or empty list if gameover */
	public List<int[]> generateMoves( Seed seed, Board b ) {
		List<int[]> nextMoves = new ArrayList<int[]>(); // allocate List

		// Search for empty cells and add to the List
		for (int row = 0; row < ROWS; ++row) {
			for (int col = 0; col < COLS; ++col) {
				if ( b.isAvailable(row, col, seed) ) {
					nextMoves.add(new int[] {row, col});
				}
			}
		}
		return nextMoves;
	}


	public boolean isGameOver() {
		if ( generateMoves( Seed.CROSS).isEmpty() && generateMoves( Seed.NOUGHT).isEmpty() ) 
			return true;
		return false;
	}

	@Override
	public int getNumberOfNodes() {
		// TODO Auto-generated method stub
		return numberOfnodes;
	}

	@Override
	String getPlayerConfig() {
		return "MiniMax ( "+ MAX_SEARCH_DEPTH + " )";
	}
}