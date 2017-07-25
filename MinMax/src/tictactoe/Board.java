package tictactoe;

public class Board {
	public Cell [][] cells = new Cell [GameMain.ROWS] [GameMain.COLS]; 
	
	Board()
	{
		for (int i = 0; i < GameMain.ROWS; i++) {
			for (int j = 0; j < GameMain.COLS; j++) {
				cells[i][j] = new Cell( Seed.EMPTY );
			}
		}
	}
	@Override
	public String toString()
	{
		StringBuffer str = new StringBuffer(100); 
		for (int i = 0; i < GameMain.ROWS; i++) {
			for (int j = 0; j < GameMain.COLS; j++) {
				str.append(cells[i][j].toString());
				if ( j !=  GameMain.COLS-1) str.append( " | ");			
			}
			str.append( "\n");		
			if ( i ==  GameMain.ROWS-1)	
				str.append( "\n");
			else 
				str.append( "--+---+--\n");
		}
		return str.toString();
	}
	public void setMove(int[] move, Seed s) 
	{
		cells[move[0]][move[1]].content = s;
	}
	
}
