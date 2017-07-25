package tictactoe;

public enum Seed {
	EMPTY ( 0 ),
	CROSS ( 1 ), 
	NOUGHT( 2 );
	
	private final int m_val;
	Seed( int val )
	{
		m_val = val;
	}
		
}
