package Othello;

public class Cell
{
	public Seed content;
	
	Cell()
	{
		content = Seed.EMPTY;
	}
	public Cell( Seed val )
	{
		content = val;
	}
	@Override
	public String toString()
	{
		if (content==Seed.CROSS)  return "X";
		if (content==Seed.NOUGHT) return "O";
		return " ";
	}

}
