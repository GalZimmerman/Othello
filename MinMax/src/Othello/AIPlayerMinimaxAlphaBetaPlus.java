package Othello;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class AIPlayerMinimaxAlphaBetaPlus extends AIPlayerMinimaxAlphaBeta {

	public AIPlayerMinimaxAlphaBetaPlus(Board board, int maxSearchDepth,
			int maxNumberOfNodes, IOthelloHeuristic heuristic) {
		super(board, maxSearchDepth, maxNumberOfNodes, heuristic);
		// TODO Auto-generated constructor stub
	}
	public class MoveWithPriority
	{
		int[] move;
		int priority;
		private int moveId;
		
		public MoveWithPriority( int[] mov, int prio, int movIndex)
		{
			move = mov;
			priority = prio;
			moveId = movIndex;
		}
	}
	
	
	/** Minimax (recursive) at level of depth for maximizing or minimizing player
    with alpha-beta cut-off. Return int[3] of {score, row, col}  */
	@Override
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
			PriorityQueue<MoveWithPriority> priQ = sortMoves( nextMoves, player == mySeed );

//			for (MoveWithPriority i = priQ.poll(); !priQ.isEmpty(); i = priQ.poll()) {
			while ( !priQ.isEmpty() ) {
				MoveWithPriority extendedMove = priQ.poll();
				int[] move = extendedMove.move;
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

	private PriorityQueue<MoveWithPriority> sortMoves(List<int[]> nextMoves, boolean maxTurn) {
		PriorityQueue<MoveWithPriority> priQ = new PriorityQueue<>(10, 
			new Comparator<MoveWithPriority>() {
				public int compare(MoveWithPriority mov1, MoveWithPriority mov2) {
//					return Integer.valueOf(mov1.moveId).compareTo(mov2.moveId);
					return mov2.priority != mov1.priority ? 
						Integer.valueOf(mov2.priority).compareTo(mov1.priority) :
						Integer.valueOf(mov1.moveId).compareTo(mov2.moveId);
				}
			}
		);
		
		// Sort move based on game stage
		if ( myTurn > 48 )
			quantityHeuristic(nextMoves, maxTurn, priQ);
		else 
			locationHeuristic(nextMoves, priQ);
		
		return priQ;
	}

	private void quantityHeuristic(List<int[]> nextMoves, boolean maxTurn,
			PriorityQueue<MoveWithPriority> priQ) {
		Seed me = maxTurn ? mySeed : oppSeed;
		Seed oponent = maxTurn ? oppSeed : mySeed;
		int movI = 0;
		for (int[] move : nextMoves) {
			Board newBoard = myBoard.clone();
			newBoard.setMove(move, me);
			priQ.add( new MoveWithPriority( move, myHeuristic.evaluate( newBoard, me, oponent), movI++ ) );
		}
	}

	private void locationHeuristic(List<int[]> nextMoves,
			PriorityQueue<MoveWithPriority> priQ) {
		int movI = 0;
		for (int[] move : nextMoves) {
			priQ.add( new MoveWithPriority( move, HeuristicPos.evalMove(move[0],  move[1]), movI++ ) );
		}
	}
	
	@Override
	String getPlayerConfig() {
		return "AlphaBeta plus ordering ( "+ MAX_SEARCH_DEPTH + " )";
	}

}
