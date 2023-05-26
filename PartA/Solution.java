package algorithms.search;

import java.util.ArrayList;

/**
 * this class will represent the solution for the searchable problem
 */
public class Solution {
    private ArrayList<AState> SolutionPath;
    public ArrayList<AState> getSolutionPath(){
        return SolutionPath;
    }

    /**
     * reverse the path of the solution and save it in variable
     * @param solutionPath - list of AStates goal to start
     */
    public Solution(ArrayList<AState> solutionPath) {
        ArrayList<AState> fromTheStartList = new ArrayList<AState>();
        if(solutionPath.size() > 1){
            for(int i = solutionPath.size()-1; i >= 0; i--){
                fromTheStartList.add(solutionPath.get(i));
            }
        }
        this.SolutionPath = fromTheStartList;

    }
}
