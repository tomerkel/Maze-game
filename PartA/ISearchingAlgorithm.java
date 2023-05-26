package algorithms.search;

/**
 * this interface will ensure that all searching algorithms use these methods
 */
public interface ISearchingAlgorithm {
    Solution solve(ISearchable iSearchable);
    AState search(ISearchable iSearchable);
    int getNumberOfNodesEvaluated();
    String getName();
}
