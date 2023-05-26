package algorithms.search;

import java.util.ArrayList;

/**
 * this interface will take a problem and translate it to a searchable problem
 */
public interface ISearchable {
    AState getStartState();
    AState getGoalState();
    ArrayList<AState> getAllPossibleStates(AState currState);
}
