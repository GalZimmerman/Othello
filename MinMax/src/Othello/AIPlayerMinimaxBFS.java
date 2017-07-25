package Othello;

import java.util.List;

public class AIPlayerMinimaxBFS extends AIPlayerMinimax 
{
	OpenList theOpneList;

	public AIPlayerMinimaxBFS(Board board, int maxSearchDepth,
			int maxNumberOfNodes, HeuristicMaximumDiscs heuristic) {
		super(board, maxSearchDepth, maxNumberOfNodes, heuristic);
		// TODO Auto-generated constructor stub
	}
	
	/** Get next best move for computer. Return int[2] of {row, col} */
	@Override
	int[] move( int turnNo ) {
		startMovTime = System.currentTimeMillis();
		numberOfnodes = 0;
		theOpneList = new OpenList();
		int[] result = expand( ); // depth, max turn
		theOpneList = null;
		return new int[] {result[1], result[2]};   // row, col
	}
	
	private int[] expand() {
		MinMaxState parent = null;
		// Generate possible next moves in a List of int[2] of {row, col}.

		while ( (numberOfnodes < MaxNumberOfNoedes) && 
			  ( System.currentTimeMillis() - startMovTime < 60000) )  { // Break the search in case time pass)
			
			Board prntBoard = parent != null ? parent.board : myBoard;
			Seed player = mySeed;
			if ( parent != null ) {
				player = parent.mySeed.flip();
			}
			List<int[]> nextMoves = generateMoves( player, prntBoard );

			// Add all child to OpenList
			for (int[] move : nextMoves) {
				Board childBoard = prntBoard.clone();
				MinMaxState child = null;
				if ( parent == null ) { // i.e. root nodes
					// Try this move for the current "player"
					childBoard.setMove( move[0], move[1], mySeed );
					child = new MinMaxState(  move, childBoard, mySeed, myHeuristic.evaluate(childBoard, mySeed, oppSeed), true );
				}
				else { // none root node
					// Try this move for the current "player"
					childBoard.setMove( move[0], move[1], player );
					child = new MinMaxState( move, childBoard, parent, myHeuristic.evaluate(childBoard, mySeed, oppSeed) );
				}
				theOpneList.add( child );
				numberOfnodes += 1;
			}
			parent = theOpneList.getFirst();
		}
		MinMaxState bestNode = theOpneList.getFirst();
		if ( bestNode == null ) bestNode = parent;
		int [] move = bestNode.getInitialMove();
		return new int[] {0, move[0], move[1]};
	}

	@Override
	String getPlayerConfig() {
		return "BFS ( Max Nodes: "+ MaxNumberOfNoedes + " )";
	}

}
