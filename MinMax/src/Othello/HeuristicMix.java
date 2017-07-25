package Othello;

public class HeuristicMix implements IOthelloHeuristic 
{
	private HeuristicMaximumDiscs hQuntity = new HeuristicMaximumDiscs();
	private HeuristicPos hPos = new HeuristicPos();

	@Override
	public int evaluate(Board b, Seed mySeed, Seed oppSeed) 
	{
		if( b.getCount( Seed.EMPTY ) < 40 ) {
			return hQuntity.evaluate(b, mySeed, oppSeed);
		}
		else {
			return hPos.evaluate(b, mySeed, oppSeed);
		}
	}

}
