package Othello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Othello {

	public static final int ROWS = 8;
	public static final int COLS = 8;
	private static long[] totalTime = {0,0};
	private static int[] totalSearcedhNodes = {0,0};
	private static int turnNo ;

	public static void main(String[] args) 
	{
		//		 * Abstract superclass for all AI players with different strategies.
		//		 * To construct an AI player:
		//		 * 1. Construct an instance (of its subclass) with the game Board
		//		 * 2. Call setSeed() to set the computer's seed
		//		 * 3. Call move() which returns the next move in an int[2] array of {row, col}.
		
		
		// command line: Othello <first-player> <second-player> [-l <level>] -n [<max-nodes>]
		// Where:
		//  first-player and second-player can be one of the following: User, MinMax or Alpha 
		//  level - max search depth
		//  max-nodes - maximum number of nodes.

		CommandLineValues cmdline = new CommandLineValues( args );
		if (!cmdline.isErrorFree() ) 
			return;
		
		Seed p1Seed =  Seed.CROSS;
		Seed p2Seed =  Seed.NOUGHT;
		Board board = new Board();
		board.cells[3][3] = new Cell(Seed.CROSS);
		board.cells[3][4] = new Cell(Seed.NOUGHT);
		board.cells[4][4] = new Cell(Seed.CROSS);
		board.cells[4][3] = new Cell(Seed.NOUGHT);
		int maxSearchDepth = Integer.MAX_VALUE;
		int maxNumberOfNodes = Integer.MAX_VALUE;
		//		AIPlayer secondPlayer = new AIPlayerMinimax( board );
		//		AIPlayer secondPlayer = new AIPlayerHuman( board );
		
		AIPlayer firstPlayer  = createPlayer( board, cmdline.firstPlayer, cmdline.level, cmdline.maxNodes);
		AIPlayer secondPlayer  = createPlayer( board, cmdline.secondPlayer, cmdline.level, cmdline.maxNodes);
//		AIPlayer secondPlayer = new AIPlayerMinimax( board, maxSearchDepth, maxNumberOfNodes );
//		AIPlayer firstPlayer  = new AIPlayerMinimaxAlphaBeta( board, maxSearchDepth, maxNumberOfNodes );


		secondPlayer.setSeed( p2Seed  );
		firstPlayer.setSeed( p1Seed  );

		System.out.println( "Start Game." );
		System.out.println( "First Player: " + firstPlayer.getPlayerConfig() + " - " + (p1Seed == Seed.CROSS ? "X" : "O") );
		System.out.println( "Second Player: " + secondPlayer.getPlayerConfig() + " - " +  (p2Seed == Seed.CROSS ? "X" : "O") );

		boolean gameOver = false;
		//		boolean fisrtRun = true;
		while( !gameOver ) {
			gameOver = playOneTurn(p1Seed, board, firstPlayer);

			if ( !gameOver )
				gameOver = playOneTurn(p2Seed, board, secondPlayer);

		}

		System.out.println( "First: " + firstPlayer.getPlayerConfig() + " Total Time " +  totalTime[seedI(p1Seed)] + " Total Searched Nodes " +  totalSearcedhNodes[seedI(p1Seed)]);
		System.out.println( "Second: " + secondPlayer.getPlayerConfig() + " Total Time " +  totalTime[seedI(p2Seed)] + " Total Searched Nodes " +  totalSearcedhNodes[seedI(p2Seed)]);
		System.out.println( firstPlayer.getPlayerConfig() + " has " + board.getCount( p1Seed ) + " seeds");
		System.out.println( secondPlayer.getPlayerConfig() + " has " + board.getCount( p2Seed ) + " seeds");

	}

	private static AIPlayer createPlayer(Board board, String playerName, int maxSearchDepth, int maxNumberOfNodes)
	{
		AIPlayer player = null;
		if ( playerName.equals( "User") ) {
			player = new AIPlayerHuman( board );
		}
		else if ( playerName.equals( "MinMax") ) {
			player = new AIPlayerMinimax( board, maxSearchDepth, maxNumberOfNodes, new HeuristicMaximumDiscs() );
		}
		else if ( playerName.equals( "Alpha") ) {
			player = new AIPlayerMinimaxAlphaBeta( board, maxSearchDepth, maxNumberOfNodes, new HeuristicMaximumDiscs() );
		}
		else if ( playerName.equals( "AlphaP") ) {
			player = new AIPlayerMinimaxAlphaBetaPlus( board, maxSearchDepth, maxNumberOfNodes, new HeuristicMaximumDiscs() );
		}
		else if ( playerName.equals( "BFS") ) {
			player = new AIPlayerMinimaxBFS( board, maxSearchDepth, maxNumberOfNodes, new HeuristicMaximumDiscs() );
		}
		else if ( playerName.equals( "RBFS") ) {
			player = new AIPlayerMinimaxRBFS( board, maxSearchDepth, maxNumberOfNodes, new HeuristicPos() );
		}
		return player;
	}

	/**
	 * Play one turn
	 * @param Seed: player seed
	 * @param board: the board
	 * @param firstPlayer: the player algorithm
	 * @return: true if game is over
	 */
	private static boolean playOneTurn(Seed seed, Board board, AIPlayer player)
	{
		turnNo += 1;
		if ( player.hasMoves(seed) ) {
			long startTime = System.currentTimeMillis();
			int[] move = player.move( turnNo );
			long turnTime = System.currentTimeMillis() - startTime;
			board.setMove( move, seed );
			totalTime [seedI(seed)] += turnTime;
			totalSearcedhNodes [seedI(seed)] += player.getNumberOfNodes();
			System.out.println( turnNo + ". " + player.getPlayerConfig() + " Move: ( " + move[0] + ", " + move[1] + 
					" ), Number of tested nodes is: " + player.getNumberOfNodes() + 
					",TurnTime: " + turnTime + 
					"\n================================\n" + board );
		}
		else {
			System.out.println( player.getPlayerConfig() + " has no move. It is your turn");
		}
		if ( player.isGameOver() ) {
			System.out.println( "Game Over");
			return true;
		}
		return false;
	}

	static private int seedI( Seed seed ) {
		return seed==Seed.CROSS ? 0 : 1;
	}
}
