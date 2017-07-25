package Othello;

public class Board {
	protected int ROWS = Othello.ROWS;  // number of rows
	protected int COLS = Othello.COLS;  // number of columns

	public Cell [][] cells = new Cell [Othello.ROWS] [Othello.COLS]; 

	Board()
	{
		for (int i = 0; i < Othello.ROWS; i++) {
			for (int j = 0; j < Othello.COLS; j++) {
				cells[i][j] = new Cell( Seed.EMPTY );
			}
		}
	}
	Board ( Board other)
	{
		for (int i = 0; i < Othello.ROWS; i++) {
			for (int j = 0; j < Othello.COLS; j++) {
				cells[i][j] = new Cell( other.cells[i][j].content );
			}
		}

	}
	@Override
	public String toString()
	{
		StringBuffer str = new StringBuffer(100); 
		str.append( "   0   1   2   3   4   5   6   7 \n");
		str.append( " +---+---+---+---+---+---+---+---+\n");
		for (int i = 0; i < Othello.ROWS; i++) {
			str.append(i).append(": ");
			for (int j = 0; j < Othello.COLS; j++) {
				str.append(cells[i][j].toString());
				if ( j !=  Othello.COLS-1) str.append( " | ");			
			}
			str.append( " |\n");		
			//			if ( i ==  Othello.ROWS-1)	
			//				str.append( "\n");
			//			else 
			str.append( " +---+---+---+---+---+---+---+---+\n");
		}
		return str.toString();
	}
	public void setMove(int row, int col, Seed seed)
	{
		// Sets in all direction
		checkAndFlipOneDirection(row, col, -1, -1, Math.min(row,col),               seed);
		checkAndFlipOneDirection(row, col, -1,  0, row,                             seed);
		checkAndFlipOneDirection(row, col, -1 , 1, Math.min(row,COLS-col-1),        seed);
		checkAndFlipOneDirection(row, col,  0, -1, col,                             seed);
		checkAndFlipOneDirection(row, col,  0,  1, COLS-col-1,                      seed);
		checkAndFlipOneDirection(row, col,  1 ,-1, Math.min(ROWS-row-1,col),        seed);
		checkAndFlipOneDirection(row, col,  1,  0, ROWS-row-1,                      seed);
		checkAndFlipOneDirection(row, col,  1,  1, Math.min(ROWS-row-1,COLS-col-1), seed);
		cells[row][col].content = seed;
	}

	public void setMove(int[] move, Seed s)
	{
		setMove(move[0], move[1], s);
		//		cells[move[0]][move[1]].content = s;
	}

	private void checkAndFlipOneDirection(int row, int col, int rDir, int cDir, int stepsToBorder, Seed seed) {
		int revNum = getNumberOfSeedsToReverse( row, col, rDir, cDir,stepsToBorder, seed, seed.theOtherSeed() );
		if ( revNum >0 ) {
			reverse( row, col, rDir, cDir, revNum, seed );	// reverse (set to seed) appropriate cells
		}
	}
	/**
	 * Check if can set a specified cell (row,col) to seed. 
	 * The condition is that all cells in specified direction contains opseed (at least 1) and immediately followed by cell with seed.
	 * @param row: the specified cell row
	 * @param col: the specified cell column
	 * @param rDir: row direction. possible values: -1 -up, 0 - no change, 1-down (increasing)
	 * @param cDir: column direction. possible values: -1 -left, 0 - no change, 1-right (increasing)
	 * @param steps: maximum number of step in the specified direction until getting to board border
	 * @param seed: the seed we want to put in cell
	 * @param opSeed: the other seed
	 * @return	number of opseeds that can be reveres in specified direction.
	 */
	public int getNumberOfSeedsToReverse(int row, int col, int rDir, int cDir, int steps, Seed seed, Seed opSeed)
	{
		int revers = 0;
		if ( steps < 2 ) {
			return 0;
		}
		boolean findNoneOpSeed = false;
		int nrow = -1;
		int ncol = -1;

		// search for cell(s) with opponent seed in the specified direction. 
		// Exit the loop when find none opSeed or get to the border of the board.
		for (int i = 1; i <= steps && !findNoneOpSeed; i++) {
			nrow = row + i*rDir;
			ncol = col + i*cDir;
			if( cells[nrow][ncol].content == opSeed ) {
				revers += 1;
			}
			else {
				findNoneOpSeed = true;
			}
		}
		if ( findNoneOpSeed && revers>0 && cells[nrow][ncol].content == seed)
			return revers;
		return 0;		
	}

	/**
	 * Reverse (set to seed) a cells in one line from a specified cell.
	 * @param row: the row of the specified cell.
	 * @param col: the column of the specified cell.
	 * @param rDir: the direction of the rows
	 * @param cDir: the direction of the columns
	 * @param revNum: number of cells to set
	 * @param seed: the seed value to set
	 */
	private void reverse(int row, int col, int rDir, int cDir, int revNum, Seed seed)
	{
		for (int i = 1; i <= revNum; i++) {
			cells[row + i*rDir][col + i*cDir].content = seed;
		}
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @param seed 
	 * @return
	 */
	public boolean isAvailable(int row, int col, Seed seed) {
		boolean isAv = true;

		// If cell not empty - return flase.
		if ( !isCellEmpty(row, col) )
			return false;
		
		// Check in all directions whether or not cell is optional move
		isAv =              checkDirection( row, col, -1,-1, Math.min(row,col),               seed, seed.theOtherSeed() );
		if ( !isAv ) isAv = checkDirection( row, col, -1, 0, row,                             seed, seed.theOtherSeed() );
		if ( !isAv ) isAv = checkDirection( row, col, -1, 1, Math.min(row,COLS-col-1),        seed, seed.theOtherSeed() );
		if ( !isAv ) isAv = checkDirection( row, col,  0,-1, col,                             seed, seed.theOtherSeed() );
		if ( !isAv ) isAv = checkDirection( row, col,  0, 1, COLS-col-1,                      seed, seed.theOtherSeed() );
		if ( !isAv ) isAv = checkDirection( row, col,  1,-1, Math.min(ROWS-row-1,col),        seed, seed.theOtherSeed() );
		if ( !isAv ) isAv = checkDirection( row, col,  1, 0, ROWS-row-1,                      seed, seed.theOtherSeed() );
		if ( !isAv ) isAv = checkDirection( row, col,  1, 1, Math.min(ROWS-row-1,COLS-col-1), seed, seed.theOtherSeed() );
		return isAv;
	}

	private boolean checkDirection(int row, int col, int rDir, int cDir, int steps, Seed seed, Seed opSeed)
	{
		return  getNumberOfSeedsToReverse(row, col, rDir, cDir, steps, seed, opSeed) != 0;
	}



	public Board clone()
	{
		return new Board (this);
	}
	public boolean isCellEmpty(int row, int col) {
		 return cells[row][col].content == Seed.EMPTY;
		
	}
	public Seed getCell(int row, int col) {
		return cells[row][col].content;
	}
	public int getCount(Seed seed) {
		int count = 0;
		for (int i = 0; i < Othello.ROWS; i++) {
			for (int j = 0; j < Othello.COLS; j++) {
				if ( cells[i][j].content == seed  ) count+=1;
			}
		}
		return count;
	}

}
