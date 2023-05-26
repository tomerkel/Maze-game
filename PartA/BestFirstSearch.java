package algorithms.search;


import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * this class is a searching algorithm that return the lightest path from start to goal
 * this class inherit BFS class and have the same searching algorithm with different data structure
 */
public class BestFirstSearch extends BreadthFirstSearch{
    /**
     * this class is a comparator for the priority queue that we use in Best First Search algorithm
     * it will compare between 2 AStates by their cost
     */
    private class AStateComparator implements Comparator<AState>{
        @Override
        public int compare(AState s1, AState s2) {
            double s1Cost = s1.getCost();
            double s2Cost = s2.getCost();
            if(s1Cost == s2Cost){
                return 0;
            }
            else if(s1Cost > s2Cost){
                return 1;
            }
            else{
                return -1;
            }

        }
    }
    public BestFirstSearch() {
        this.openList = new PriorityQueue<AState>( new AStateComparator());
    }

    public String getName(){
        return "Best First Search";
    }

}
