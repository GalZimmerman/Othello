package Othello;

import java.util.List;

import sun.util.resources.OpenListResourceBundle;

public class AIPlayerMinimaxRBFS extends AIPlayerMinimax 
{
	OpenList theOpneList;

	public AIPlayerMinimaxRBFS(Board board, int maxSearchDepth,
			int maxNumberOfNodes, IOthelloHeuristic heuristic) {
		super(board, maxSearchDepth, maxNumberOfNodes, heuristic);
		// TODO Auto-generated constructor stub
	}
	
	/** Get next best move for computer. Return int[2] of {row, col} */
	@Override
	int[] move( int turnNo ) {
		startMovTime = System.currentTimeMillis();
		numberOfnodes = 0;
		theOpneList = new OpenList();
		int[] result = bfmax( myBoard, Integer.MIN_VALUE, Integer.MAX_VALUE); // parentBoard, alpha, beta
		theOpneList = null;		// deallocate memory
		return new int[] {result[1], result[2]};   // row, col
	}

	/**
	 * BFMAX (Node, Alpha, Beta)
			FOR each Child[i] of Node
				M[i] := Evaluation( Child[i] )
				IF M[i] > Beta return M[i]
			SORT Child[i] and M[i] in decreasing order
			IF only one child, M[2] := -infinity
			WHILE Alpha <= M[1] <= Beta
				M[l] := BFMIN( Child[l], max(Alpha,M[2]), Beta)
				insert Child[l] and M[1] in sorted order
	 * @return
	 */
	private int[] bfmax( Board node, int alpha, int beta )
	{
		List<int[]> nextMoves = generateMoves( mySeed, node );
		if  (nextMoves.isEmpty() ) return new int[] { Integer.MAX_VALUE,0, 0 }; // return indication for no children
		OpenList childs = new OpenList();
		for (int[] move : nextMoves) {
			Board childBoard = node.clone();
			childBoard.setMove( move[0], move[1], mySeed );
			int Mi = myHeuristic.evaluate(childBoard, mySeed, oppSeed);
			if ( Mi > beta ) return new int[] { Mi, move[0], move[1] };
			childs.add( new MinMaxState(move, childBoard, mySeed, Mi, true) );
			numberOfnodes += 1;
		}
		if ( childs.getSize() == 1 ) {
			// add faked node at the end
			childs.add( new MinMaxState(null, null, mySeed, Integer.MIN_VALUE, true));
		}
		
		MinMaxState child = childs.peekFirst();
		int M1 = child.heuristic;
			
		while (alpha <= M1 && M1 <= beta  && (numberOfnodes < MaxNumberOfNoedes) ) {
			child = childs.getFirst();
			M1 = bfmin( child.board, Integer.max(alpha,childs.peekFirst().heuristic), beta )[0];
			if ( M1 == Integer.MIN_VALUE ) return new int[] { child.heuristic, child.move[0], child.move[1] }; // if no children, return the node
			child.heuristic = M1;
			childs.add( child );
		}
		
		return new int[] { M1, child.move[0],  child.move[1] };
	}

	/**
	 * BFMIN (Node, Alpha, Beta)
		FOR each Child[i] of Node
			M[i] := Evaluation(Child[i])
			IF M[i] < Alpha return M[i]
		SORT Child[i] and M[i] in increasing order
		IF only one child, M[2] := infinity
		WHILE Alpha <= M[1] <= Beta
			M[1] := BFMAX( Child[l], Alpha, min(Beta,M[2]) )
			insert Child[l] and M[l] in sorted order
		return M[l] 
	 * @return
	 */
	private int[] bfmin( Board node, int alpha, int beta) {
		List<int[]> nextMoves = generateMoves( oppSeed, node );
		if (nextMoves.isEmpty() ) return new int[] { Integer.MIN_VALUE,0, 0 }; // return indication for no children
		OpenList childs = new OpenList();
		for (int[] move : nextMoves) {
			Board childBoard = node.clone();
			childBoard.setMove( move[0], move[1], oppSeed );
			int Mi = myHeuristic.evaluate(childBoard, mySeed, oppSeed );
			if ( Mi < alpha ) return new int[] { Mi, move[0], move[1] };
			childs.add( new MinMaxState(move, childBoard, oppSeed, Mi, false) );
			numberOfnodes += 1;
		}
		if ( childs.getSize() == 1 ) {
			// add faked node at the end
			childs.add( new MinMaxState(null, null, oppSeed, Integer.MAX_VALUE, false) );
		}
		
		MinMaxState child = childs.peekFirst();
		int M1 = child.heuristic;
			
		while (alpha <= M1 && M1 <= beta && (numberOfnodes < MaxNumberOfNoedes)) {
			child = childs.getFirst();		// extract first child
			M1 = bfmax( child.board, alpha, Integer.min(beta,childs.peekFirst().heuristic) )[0];
			if ( M1 == Integer.MAX_VALUE ) return new int[] { child.heuristic, child.move[0], child.move[1] }; // if no children, return the node
			child.heuristic = M1;
			childs.add( child );			// re-insert the child with new static value
		}
		return new int[] { M1, child.move[0],  child.move[1] };
	}

	@Override
	String getPlayerConfig() {
		return "RBFS ( Max Nodes: "+ MaxNumberOfNoedes + " )";
	}


}
