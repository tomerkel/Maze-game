package algorithms.mazeGenerators;

import java.util.Random;

/**
 * this class will create an empty maze
 */
public class EmptyMazeGenerator extends AMazeGenerator {
    /**
     * this function will generate an empty maze
     * @param rowNum - the number of rows in the maze
     * @param colNum - the number of columns in the maze
     * @return a new empty Maze
     */
    @Override
    public Maze generate(int rowNum, int colNum) {
        Maze newMaze = new Maze(rowNum, colNum);

        // choose a random number to the row of start/goal position
        int start = new Random().nextInt(rowNum);
        int end = new Random().nextInt(rowNum);

        // create new start/goal position
        Position startPos = newMaze.positionsMaze[start][0];
        Position endPos = newMaze.positionsMaze[end][colNum-1];

        // set the selected start/goal positions into the maze
        newMaze.setStartPosition(startPos);
        newMaze.setGoalPosition(endPos);
        return newMaze;
    }
}
