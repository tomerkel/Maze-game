package algorithms.mazeGenerators;

import java.util.Random;

/**
 * this class will create a maze in a randomly way but ensure a solution
 */
public class SimpleMazeGenerator extends AMazeGenerator {
    /**
     * create a new maze with randomize algorithm that promise a path from start to goal
     * @param rowNum - the number of rows in the maze
     * @param colNum - the number of columns in the maze
     * @return new maze
     */
    @Override
    public Maze generate(int rowNum, int colNum) {
        // create empty maze
        EmptyMazeGenerator EMP = new EmptyMazeGenerator();
        Maze newMaze = EMP.generate(rowNum, colNum);
        newMaze.setIsEmpty(false);

        // run on all positions and randomly set walls to them
        for (int i = 1; i < rowNum-1; i++) {
            for (int j = 1; j < colNum - 1; j++) {
                int numDown = new Random().nextInt(2);
                int numRight = new Random().nextInt(2);

                //update the wall of curr position
                newMaze.positionsMaze[i][j].setWallRight(numRight);
                newMaze.positionsMaze[i][j].setWallDown(numDown);

                //update the neighbor position wall according to the current position
                newMaze.positionsMaze[i][j+1].setWallLeft(numRight);
                newMaze.positionsMaze[i+1][j].setWallUp(numDown);
            }
        }
        return newMaze;
    }
}

