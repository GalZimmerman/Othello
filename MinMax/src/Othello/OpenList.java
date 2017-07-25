package Othello;

import java.util.Comparator;
import java.util.PriorityQueue;

public class OpenList
{
	
	private PriorityQueue<MinMaxState> theOpenList;
	
    //Comparator anonymous class implementation
    public static Comparator<MinMaxState> stateComparator = new Comparator<MinMaxState>(){
         
        @Override
        public int compare(MinMaxState node1, MinMaxState node2) {
        	if ( node1.isMax ) {
        		if ( node1.getH() == Integer.MIN_VALUE ) {
        			return 1;
        		}
        		else if ( node2.getH() == Integer.MIN_VALUE ) {
        			return -1;
        		}
        		return (int) ( node2.getH() - node1.getH() ); // node2 - node1 because we want descending (biggest first).      		
        	}
        	else {
        		if ( node1.getH() == Integer.MAX_VALUE ) {
        			return 1;
        		}
        		else if ( node2.getH() == Integer.MAX_VALUE ) {
        			return -1;
        		}
       		return (int) ( node1.getH() - node2.getH() ); // node1 - node2 because we want ascending order (smallest first).
        	}
        }
    };
    
	public OpenList()
	{
		theOpenList = new PriorityQueue<MinMaxState>(11, stateComparator);
	}
	
	public MinMaxState getFirst()
	{
		return theOpenList.poll();
	}
	
	public void add( MinMaxState node )
	{
		theOpenList.add( node );
	}

	public MinMaxState peekFirst() {
		return theOpenList.peek();
	}

	public int getSize() {
		return theOpenList.size();
	}

}
