package Othello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AIPlayerHuman extends AIPlayer 
{

	public AIPlayerHuman(Board board) 
	{
		myBoard = board;
	}

	@Override
	int[] move( int turnNo ) {
		// TODO Auto-generated method stub
		return getUserMove();
	}

	@Override
	public boolean isGameOver() {
		// TODO Auto-generated method stub
		return !( hasMoves( Seed.CROSS) || hasMoves( Seed.NOUGHT) );
	}

	@Override
	public int getNumberOfNodes() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	String getPlayerConfig() {
		return "Human User";
	}
	private int[] getUserMove()
	{
		while( true) { 
			System.out.println( "Please enter your move, 'row,col' where each is number between 0 to 7:" );
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String line = null;
			try {
				line = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int[] move = parseUserInput(line);
			if (    move != null && move.length==2 && 
					move[0]>=0 && move[0] <= Othello.ROWS &&
					move[1]>=0 && move[1] <= Othello.COLS &&
					myBoard.isAvailable(move[0], move[1], mySeed ))
				return move;
			System.out.println( "The enetred input has invalid format. Example for valid format: 0,0 for the top left corner" );
			
		}
	}

	private int[] parseUserInput(String line) {
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

}
