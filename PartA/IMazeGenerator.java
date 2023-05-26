package algorithms.mazeGenerators;

/**
 * an interface that every maze generator must implement
 */
public interface IMazeGenerator {
    public Maze generate(int rowNum,int colNum);
    public long measureAlgorithmTimeMillis(int rowNum,int colNum);
}
