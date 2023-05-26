package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;

/**
 * this class turn a maze problem into a searchable problem that can be solved with searching algorithms
 */
public class SearchableMaze implements ISearchable {
    private Maze maze;
    private MazeState[][] MazeStateArray;
    private MazeState StartState;
    private MazeState GoalState;

    public SearchableMaze(Maze maze) {
        this.maze = maze;
        this.MazeStateArray = new MazeState[maze.getRowNum()][maze.getColNum()];

        // turn the 2D position array into 2D maze state array
        for(int i = 0 ; i < maze.getRowNum() ; i++){
            for(int j = 0 ; j < maze.getColNum(); j++){
                this.MazeStateArray[i][j] = new MazeState(maze.positionsMaze[i][j]);

                // initialize the cost for the maximum
                this.MazeStateArray[i][j].setCost(maze.getRowNum()*maze.getColNum()*15);
            }
        }
        this.StartState = this.MazeStateArray[maze.getStartPosition().getRowIndex()][maze.getStartPosition().getColumnIndex()];
        this.StartState.setCost(0);
        this.GoalState = this.MazeStateArray[maze.getGoalPosition().getRowIndex()][maze.getGoalPosition().getColumnIndex()];
    }

    @Override
    public AState getStartState() {
        return StartState;
    }

    @Override
    public AState getGoalState() {
        return GoalState;
    }

    /**
     * this function will find all the neighbors of the current position in the left/right
     * if there is a path from the left/right neighbor to up or down this function will add them to the array
     * so the current state can do a diagonal step
     * @param currPos - the current position
     * @param posCost - the current position cost
     * @param RightOrLeft - a string that will tell us which direction we need to go
     * @return ArrayList<AState> array of neighbor
     */
    private ArrayList<AState> getRightOrLeftPossibleStates(Position currPos,double posCost,String RightOrLeft){
        ArrayList<AState> returnList = new ArrayList<AState>();
        int numRightOrLeft;

        // tell us if we need to go left or right
        if(RightOrLeft == "Left"){
            numRightOrLeft = -1;
        }
        else{
            numRightOrLeft = 1;
        }
        int row = currPos.getRowIndex();
        int col = currPos.getColumnIndex();
        MazeState RightOrLeftState = MazeStateArray[row][col+numRightOrLeft];

        // if the new cost lower then the previous, change it
        if(RightOrLeftState.getCost() > posCost+10){
            RightOrLeftState.setCost(posCost+10);
        }
        returnList.add(RightOrLeftState);

        //we now want to check the diagonals step

        if(RightOrLeftState.getStatePos().getWallUp() == 0){
            MazeState diagonalRightOrLeftUp = MazeStateArray[row -1][col+numRightOrLeft];

            // if the new cost lower then the previous, change it
            if(diagonalRightOrLeftUp.getCost() > posCost+15){
                diagonalRightOrLeftUp.setCost(posCost+15);
            }

            returnList.add(diagonalRightOrLeftUp);
        }
        if(RightOrLeftState.getStatePos().getWallDown() == 0){
            MazeState diagonalRightOrLeftDown = MazeStateArray[row + 1][col+numRightOrLeft];

            // if the new cost lower then the previous, change it
            if(diagonalRightOrLeftDown.getCost() > posCost+15){
                diagonalRightOrLeftDown.setCost(posCost+15);
            }

            returnList.add(diagonalRightOrLeftDown);
        }
        return returnList;
    }
    /**
     * this function will find all the neighbors of the current position in the up/down
     * if there is a path from the up/down neighbor to left or right this function will add them to the array
     * so the current state can do a diagonal step
     * @param currPos - the current position
     * @param posCost - the current position cost
     * @param RightOrLeft - a string that will tell us which direction we need to go
     * @return ArrayList<AState> array of neighbor
     */
    private ArrayList<AState> getUpOrDownPossibleStates(Position currPos,double posCost,String upOrDown){
        ArrayList<AState> returnList = new ArrayList<AState>();
        int numUpOrDown;

        // tell us if we need to go up or down
        if(upOrDown == "Up"){
            numUpOrDown = -1;
        }
        else{
            numUpOrDown = 1;
        }
        int row = currPos.getRowIndex();
        int col = currPos.getColumnIndex();
        MazeState UpOrDownState = MazeStateArray[row + numUpOrDown][col];

        // if the new cost lower then the previous, change it
        if(UpOrDownState.getCost() > posCost+10){
            UpOrDownState.setCost(posCost+10);
        }

        returnList.add(UpOrDownState);

        //we now want to check the diagonals step

        if(UpOrDownState.getStatePos().getWallRight() == 0){
            MazeState diagonalUpOrDownRight = MazeStateArray[row + numUpOrDown][col+1];

            // if the new cost lower then the previous, change it
            if(diagonalUpOrDownRight.getCost() > posCost+15){
                diagonalUpOrDownRight.setCost(posCost+15);
            }

            returnList.add(diagonalUpOrDownRight);
        }
        if(UpOrDownState.getStatePos().getWallLeft() == 0){
            MazeState diagonalUpOrDownLeft = MazeStateArray[row + numUpOrDown][col-1];

            // if the new cost lower then the previous, change it
            if(diagonalUpOrDownLeft.getCost() > posCost+15 ){
                diagonalUpOrDownLeft.setCost(posCost+15);
            }
            returnList.add(diagonalUpOrDownLeft);
        }
        return returnList;
    }

    /**
     * this function will get all the neighbors that exist a path between them and the current position
     * including diagonals
     * @param currState - current position
     * @return ArrayList<AState> array of neighbors
     */
    @Override
    public ArrayList<AState> getAllPossibleStates(AState currState) {
        ArrayList<AState> returnList = new ArrayList<AState>();
        MazeState currMazeState = (MazeState)currState;

        // if there isn't a wall up
        if(currMazeState.getStatePos().getWallUp() == 0) {
            returnList.addAll(getUpOrDownPossibleStates(currMazeState.getStatePos(),currMazeState.getCost(),"Up"));
        }
        // if there isn't a wall down
        if(currMazeState.getStatePos().getWallDown() == 0){
            returnList.addAll(getUpOrDownPossibleStates(currMazeState.getStatePos(),currMazeState.getCost(),"Down"));
        }
        // if there isn't a wall left
        if(currMazeState.getStatePos().getWallLeft() == 0){
            returnList.addAll(getRightOrLeftPossibleStates(currMazeState.getStatePos(),currMazeState.getCost(),"Left"));
        }
        // if there isn't a wall right
        if(currMazeState.getStatePos().getWallRight() == 0){
            returnList.addAll(getRightOrLeftPossibleStates(currMazeState.getStatePos(),currMazeState.getCost(),"Right"));
        }
        return returnList;
    }
}
