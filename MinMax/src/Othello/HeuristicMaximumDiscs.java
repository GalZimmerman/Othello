package Othello;

public class HeuristicMaximumDiscs implements IOthelloHeuristic {

	/** The heuristic evaluation function for the current board
    @Return number of my seeds(PC) minus the the opponent seeds 
  */

	@Override
	public int evaluate( Board b, Seed mySeed, Seed oppSeed ) {
		int myseeds = 0;
		int opseeds = 0;
		// Go over all board cells and count my seeds as well as opponent seeds
		for (int i = 0; i < Othello.ROWS; i++) {
			for (int j = 0; j < Othello.COLS; j++) {
				if ( b.getCell(i,j) == mySeed ) {
					myseeds += 1;
				} else if (  b.getCell(i,j) == oppSeed ) {
					opseeds += 1;
				}
			}
		}
		return myseeds-opseeds;
	}

}
