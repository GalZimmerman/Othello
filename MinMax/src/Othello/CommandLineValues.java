package Othello;



/**
 * This class handles the programs arguments.
 */
public class CommandLineValues {
    public String firstPlayer;
    public String secondPlayer;
    public int  level = 7;
    public int  maxNodes = Integer.MAX_VALUE;
    
	String cmdLine = 
	"command line: Othello <first-player> <second-player> [-l <level>] -n [<max-nodes>]\n" + 
	" Where:\n" + 
	"  first-player and second-player can be one of the following: User, MinMax, Alpha or AlphaP\n" + 
	"  level - max search depth\n" + 
	"  max-nodes - maximum number of nodes.\n";


    private boolean errorFree = false;

    public CommandLineValues( String[] args) {
       try {
    	   int numargs=2;
    	   firstPlayer = args[0];
    	   secondPlayer = args[1];
    	   if ( args[2].equals( "-l" ) ) {
    		   level = Integer.parseInt( args[3] );
    		   numargs += 2;
    	   }
    	   if ( args.length > numargs && args[numargs].equals( "-n" ) ) {
    		   maxNodes = Integer.parseInt( args[numargs+1] );
    		   numargs += 2;
    	   }
    	   
    	   if ( (args.length != numargs) ||
       			!isLegalPlayer (firstPlayer) || 
    			!isLegalPlayer (secondPlayer) ) {
    		   throw new IllegalArgumentException();
    	   }

            errorFree = true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println( cmdLine );
        }
    }

    private boolean isLegalPlayer(String player) {
    	boolean rc = player.equals( "User") || player.equals( "MinMax") || player.equals( "Alpha")  || player.equals( "AlphaP") || player.equals( "BFS") || player.equals( "RBFS")  ;
		return rc;
	}

	/**
     * Returns whether the parameters could be parsed without an
     * error.
     *
     * @return true if no error occurred.
     */
    public boolean isErrorFree() {
        return errorFree;
    }

}