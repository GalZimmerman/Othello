package Othello;
/* ----------------------------------------------------------------------------------------------------------------------
minimax(level, player, alpha, beta)  // player may be "computer" or "opponent"
if (gameover || level == 0)
   return score
children = all valid moves for this "player"
if (player is computer, i.e., max's turn)
   // Find max and store in alpha
   for each child
      score = minimax(level - 1, opponent, alpha, beta)
      if (score > alpha) alpha = score
      if (alpha >= beta) break;  // beta cut-off
   return alpha
else (player is opponent, i.e., min's turn)
   // Find min and store in beta
   for each child
      score = minimax(level - 1, computer, alpha, beta)
      if (score < beta) beta = score
      if (alpha >= beta) break;  // alpha cut-off
   return beta

// Initial call with alpha=-inf and beta=inf
minimax(2, computer, -inf, +inf)
--------------------------------------------------------------------------------------------------------------------- */

import java.util.ArrayList;
import java.util.List;

public class AIPlayerMinimaxAlphaBeta extends AIPlayer
{
	protected static int MAX_SEARCH_DEPTH = 7;
	protected long startMovTime;
	public int numberOfnodes;
	protected int MaxNumberOfNoedes = Integer.MAX_VALUE;
	protected IOthelloHeuristic myHeuristic;
	protected int myTurn=0;


	/** Constructor with the given game board 
	 * @param maxNumberOfNodes 
	 * @param heuristic  */
	public AIPlayerMinimaxAlphaBeta(Board board, int maxSearchDepth, int maxNumberOfNodes, IOthelloHeuristic heuristic ) {
		myBoard = board;
		MAX_SEARCH_DEPTH = maxSearchDepth;
		MaxNumberOfNoedes  = maxNumberOfNodes;
		myHeuristic = heuristic;
	}

	/** Get next best move for computer. Return int[2] of {row, col} */
	@Override
	int[] move( int turnNo ) {
		myTurn = turnNo;
		startMovTime = System.currentTimeMillis();
		numberOfnodes = 0;
		int[] result = minimax(MAX_SEARCH_DEPTH, mySeed, Integer.MIN_VALUE, Integer.MAX_VALUE); // depth, max-turn, alpha, beta
		return new int[] {result[1], result[2]};   // row, col
	}

	/** Minimax (recursive) at level of depth for maximizing or minimizing player
    with alpha-beta cut-off. Return int[3] of {score, row, col}  */
	protected int[] minimax(int depth, Seed player, int alpha, int beta) {
		// Generate possible next moves in a List of int[2] of {row, col}.
		List<int[]> nextMoves = generateMoves( player );

		// mySeed is maximizing; while oppSeed is minimizing
		int score;
		int bestRow = -1;
		int bestCol = -1;

		// Gameover or depth reached, evaluate score
		if ( nextMoves.isEmpty() || depth == 0 ||
			( System.currentTimeMillis() - startMovTime > 60000) || // Break the search in case time pass
			numberOfnodes >= MaxNumberOfNoedes )
		{
			score = myHeuristic.evaluate( myBoard, mySeed, oppSeed);
			return new int[] {score, bestRow, bestCol};
		} else {
			for (int[] move : nextMoves) {
				Board keepBoard = myBoard;
				myBoard = myBoard.clone();
				// Try this move for the current "player"
				myBoard.setMove( move[0], move[1], player );
				if (player == mySeed) {  // mySeed (computer) is maximizing player
					score = minimax(depth - 1, oppSeed, alpha, beta)[0];
					if (score > alpha) {
						alpha = score;
						bestRow = move[0];
						bestCol = move[1];
					}
				} else {  // oppSeed is minimizing player
					score = minimax(depth - 1, mySeed, alpha, beta)[0];
					if (score < beta) {
						beta = score;
						bestRow = move[0];
						bestCol = move[1];
					}
				}
				// Undo move
				myBoard = keepBoard;
				numberOfnodes += 1;
				
				// cut-off
	            if (alpha >= beta) break;

				// Break the search in case time pass
				if ( System.currentTimeMillis() - startMovTime > 60000)
					break;
			}
		}
		return new int[] {(player == mySeed) ? alpha : beta, bestRow, bestCol};
	}

	/** Find all valid next moves.
       Return List of moves in int[2] of {row, col} or empty list if gameover */
	protected List<int[]> generateMoves( Seed seed) {
		List<int[]> nextMoves = new ArrayList<int[]>(); // allocate List

		// Search for empty cells and add to the List
		for (int row = 0; row < ROWS; ++row) {
			for (int col = 0; col < COLS; ++col) {
				if ( myBoard.isAvailable(row, col, seed) ) {
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
		return numberOfnodes;
	}

	@Override
	String getPlayerConfig() {
		return "AlphaBeta ( "+ MAX_SEARCH_DEPTH + " )";
	}

}
