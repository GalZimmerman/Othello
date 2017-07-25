package Othello;

import java.security.InvalidParameterException;

public enum Seed {
	EMPTY ( 0 ),
	CROSS ( 1 ), 
	NOUGHT( 2 );
	
	private final int m_val;
	Seed( int val )
	{
		m_val = val;
	}
	public Seed theOtherSeed()
	{
		if (m_val == 1) 
			return NOUGHT;
		else if (m_val==2) {
			return CROSS;
		}
		throw new InvalidParameterException( "theOtherSeed for EMPTYseed is invalid");
	}
	public Seed flip()
	{
		return m_val == 1  ? NOUGHT : CROSS;
	}

	@Override
	public String toString()
	{
		if (m_val==2)  return "X";
		if (m_val==2) return "O";
		return " ";
	}

	
		
}
