package Othello;
/*
 * Risk Regions  form: http://mnemstudio.org/game-reversi-example-2.htm
Beyond Minimax, this implementation of reversi also includes risk regions defined on the board:




Figure 2. Arbitrary regions representing very general movement risks. Typically, Regions 3 and 5 are very valuable strategic areas. I've found that Region 4 is a real weakness in example 1, often making it easy for me to take the corners.


Regions 3 and 5 are coveted real estate on the Reversi gameboard, because it's harder for opponents to threaten your pieces. Especially so for Region 5. These two regions also make it easy to take large numbers of opponent pieces at once. So the AI in this example is encouraged to place pieces in these regions whenever possible.

In contrast, Regions 2 and 4 are risky territory because opponents gain access to Regions 3 and 5 after taking pieces place here. So, the AI is discouraged from occupying these two regions.

To calculate the best move, the number of possible opponent pieces taken is compared to the bias of the risk regions involved.
 */
public class HeuristicPos implements IOthelloHeuristic 
{
	static public int evalMove( int x, int y )
	{
		int ret = 1;
		// Bias is imposed here to simulate more strategic behavior.  Occupying corners and
		// edges of the game board often lead to strategic advantages in the game.
		if( x == 0 &&                y == 0 || 
				x == 0 &&                y == Othello.ROWS - 1 || 
				x == Othello.ROWS - 1 && y == 0 || 
				x == Othello.ROWS - 1 && y == Othello.ROWS - 1 - 1) {
			// Highest bias toward corners.
			ret = 10;
		}
		else if (
				x == 1 && y == 0 ||
				x == 0 && y == 1 ||
				x == 1 && y == 1 ||
				x == Othello.ROWS - 2 && y == 0 ||
				x == Othello.ROWS - 1 && y == 1 ||
				x == Othello.ROWS - 2 && y == 1 ||
				x == 0 && y == Othello.ROWS - 2 ||
				x == 1 && y == Othello.ROWS - 1 ||
				x == 1 && y == Othello.ROWS - 2 ||
				x == Othello.ROWS - 1 && y == Othello.ROWS - 2 ||
				x == Othello.ROWS - 2 && y == Othello.ROWS - 1 ||
				x == Othello.ROWS - 2 && y == Othello.ROWS - 2) {
			ret = -5;
		}
		else if(x == 0 || 
				x ==  Othello.ROWS - 1 || 
				y == 0 || 
				y ==  Othello.ROWS - 1) {
			// Lower bias toward edges.
			ret = 5;
		}
		return ret;
	}

	@Override
	public int evaluate(Board b, Seed mySeed, Seed oppSeed) {
		int sum = 0;
		// Go over all board cells and count my seeds as well as opponent seeds
		for (int i = 0; i < Othello.ROWS; i++) {
			for (int j = 0; j < Othello.COLS; j++) {
				if ( b.getCell(i,j) == mySeed ) {
					sum += evalMove(i,j);
				} else if (  b.getCell(i,j) == oppSeed ) {
					sum -= evalMove(i,j);;
				}
			}
		}
		return sum;
	}
}
