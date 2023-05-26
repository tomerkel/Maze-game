package algorithms.search;

import java.util.*;

/**
 * an abstract class that all the searching algorithms need to extend
 */
public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {
    protected int visitedNodes;

    public abstract String getName();
    public ASearchingAlgorithm() {
        this.visitedNodes = 0;
    }
    protected abstract AState popOpenList();
    protected abstract void insertOpenList(AState s);
    @Override
    public abstract AState search(ISearchable iSearchable);

    /**
     * this method gets a searchable problem and return a solution to the problem
     * @param iSearchable - an object that contain a problem
     * @return a solution
     */
    @Override
    public Solution solve(ISearchable iSearchable){
        if(iSearchable == null){
            return null;
        }
        ArrayList<AState> path = new ArrayList<AState>();

        // run on the searchable problem and search the goal state
        AState tempState = search(iSearchable);

        // restore the path that the search algorithm create while trying search the goal
        while (tempState != null){
            path.add(tempState);
            tempState = tempState.getParent();
        }
        Solution solution = new Solution(path);
        return solution;
    }

    @Override
    public abstract int getNumberOfNodesEvaluated();
}
