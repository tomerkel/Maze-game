package algorithms.mazeGenerators;

/**
 * an abstract class that all the maze generators must extend
 */
public abstract class AMazeGenerator implements IMazeGenerator{
    /**
     * generate function - generate a new maze in given size
     * @param rowNum - the number of rows in the maze
     * @param colNum - the number of columns in the maze
     * @return - new Maze in given size
     */
    public abstract Maze generate(int rowNum, int colNum);
    @Override
    public long measureAlgorithmTimeMillis(int rowNum, int colNum) {
        /**
         * calculate the time in milliseconds of generate a new Maze
         */
        long StartTime = System.currentTimeMillis();
        generate(rowNum, colNum);
        long EndTime = System.currentTimeMillis();
        return EndTime-StartTime;
    }
}
