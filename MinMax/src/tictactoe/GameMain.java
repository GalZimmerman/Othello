package tictactoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GameMain {

	public static final int ROWS = 3;
	public static final int COLS = 3;

	public static void main(String[] args) 
	{
//		 * Abstract superclass for all AI players with different strategies.
//		 * To construct an AI player:
//		 * 1. Construct an instance (of its subclass) with the game Board
//		 * 2. Call setSeed() to set the computer's seed
//		 * 3. Call move() which returns the next move in an int[2] array of {row, col}.

		Seed mySeed =  Seed.CROSS;
		Seed opSeed =  Seed.NOUGHT;
		Board board = new Board();
		AIPlayerMinimax aiPlayer = new AIPlayerMinimax( board );
		aiPlayer.setSeed( mySeed );
		boolean fisrtRun = true;
		while( true ) {
			// Computer move
			if ( fisrtRun ) {
				fisrtRun = false;
			}
			else {
				int[] move = aiPlayer.move();
				board.setMove( move, mySeed );
			}
			System.out.println( "=========\n\n" + board );
			if (aiPlayer.isGameOver() ) {
				System.out.println( "Game Over. AIPlayer won.");
				return;
			}
			
			// User move
			int[] omove = null;
			boolean legalMove = false;
			do {
				omove = getUserMove();
				if ( isEmpty( omove[0], omove[1], board ) ) legalMove = true;
			} while ( !legalMove );
			board.setMove( omove, opSeed );
			System.out.println( "=========\n\n" + board );
			if (aiPlayer.isGameOver() ) {
				if (aiPlayer.isGameOver() ) {
					System.out.println( "Game Over. User won.");
					return;
				}
			}
		}
	}

	private static int[] getUserMove()
	{
		while( true) { 
			System.out.println( "Please enter your move, 'row,col' where each is number between 0 to 2:" );
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int[] move = parseUserInput(line);
			if ( move != null && move.length==2 && 
					move[0]>=0 && move[0] <=2 &&
					move[1]>=0 && move[1] <=2 )
				return parseUserInput(line);
			System.out.println( "The enetred input has invalid format. Example for valid format: 1,1 for the center cell" );
			
		}
	}

	private static int[] parseUserInput(String line) {
		if (line == null)
			return null;
		
		String[] tokens = line.split(",");
		if (tokens.length != 2 )
			return null;
		
		int [] omove = new int[2];
		for (int i = 0; i < omove.length; i++) {
			omove[i] = Integer.parseUnsignedInt(tokens[i]);
		}
		return omove;
	}
	private static boolean isEmpty( int row, int col, Board board)
	{
		return board.cells[row][col].content == Seed.EMPTY;
	}

}
