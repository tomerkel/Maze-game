package algorithms.search;

import java.util.*;

/**
 * this class extend ASearchingAlgorithm class
 * this class will search the goal state by BFS algorithm
 */
public class BreadthFirstSearch extends ASearchingAlgorithm {
    protected Queue<AState> openList;

    public BreadthFirstSearch() {
        this.openList = new LinkedList<AState>();
    }

    /**
     * this method searching the goal state from the start state with BFS algorithm
     * @param iSearchable - the problem that we want to search in
     * @return the goal state
     */
    @Override
    public AState search(ISearchable iSearchable) {
        if(iSearchable == null){
            return null;
        }
        AState startState = iSearchable.getStartState();

        // create a set data structure that will save the visited states and insert the start state into it
        HashSet<AState> visited = new HashSet<AState>();
        insertOpenList(startState);

        while (!openList.isEmpty()){
            AState currState = popOpenList();
            visited.add(currState);

            // array of all the possible moves from current state
            ArrayList<AState> neighborsState = iSearchable.getAllPossibleStates(currState);

            // an int that represent us if we found the goal state
            int ifWeFoundTheGoal = -1;

            for (AState nState : neighborsState) {
                // if we didnt visit this neighbor state and the neighbor not in the queue
                if(!(visited.contains(nState)) && !(openList.contains(nState))){

                    // set the parent of neighbor state to be current state
                    nState.setParent(currState);

                    if (nState == iSearchable.getGoalState()){
                        ifWeFoundTheGoal = 0;
                        visited.add(nState);
                        break;
                    }
                    // add the neighbor state to the queue
                    insertOpenList(nState);
                }
            }
            if (ifWeFoundTheGoal == 0){
                break;
            }
        }
        visitedNodes = visited.size();
        return iSearchable.getGoalState();
    }
    public String getName(){
        return "Breadth First Search";
    }

    @Override
    public int getNumberOfNodesEvaluated() {
        return visitedNodes;
    }

    @Override
    protected AState popOpenList() {
        return openList.poll();
    }

    @Override
    protected void insertOpenList(AState s) {
        openList.add(s);
    }
}
