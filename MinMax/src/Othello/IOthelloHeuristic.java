package Othello;

public interface IOthelloHeuristic 
{
	/** The heuristic evaluation function for the current board
    @Return number of my seeds(PC) minus the the opponent seeds 
  */
	int evaluate(Board b, Seed mySeed, Seed oppSeed);

}
