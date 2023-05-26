package algorithms.mazeGenerators;

/**
 * this class represent nXm maze that consists of positions
 * to every maze we will save his start position and goal position
 * we will save the maze as 2D array of positions
 */
public class Maze {
    public Position[][] positionsMaze;
    private Position StartPosition;
    private Position GoalPosition;
    private int rowNum;
    private int colNum;
    private boolean isEmpty;

    /**
     * Maze constructor
     * @param rowNum - number of rows in the Maze
     * @param colNum - number of columns in the Maze
     */
    public Maze(int rowNum, int colNum) {
        this.positionsMaze = new Position[rowNum][colNum]; //create new 2D array of positions
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.isEmpty = true;
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                this.positionsMaze[i][j] = new Position(i,j); // create a new position to every index
                setPositionLimits(this.positionsMaze[i][j]); //set limits to every position
            }
        }
        this.StartPosition = null;
        this.GoalPosition = null;
    }

    /**
     * if the given position on the frame, set the possibility to wall in different directions
     * @param pos - a position
     */
    private void setPositionLimits(Position pos){

        if(pos.getRowIndex() == 0){  // if the position is in the first row
            pos.setWallUp(-1);
        }
        if(pos.getColumnIndex() == 0){ // if the position is in the first column
            pos.setWallLeft(-1);
        }
        if(pos.getRowIndex() == rowNum-1){  // if the position is in the last row
            pos.setWallDown(-1);
        }
        if(pos.getColumnIndex() == colNum-1){  // if the position is in the last column
            pos.setWallRight(-1);
        }
    }

    /**
     * this function turn all the maze walls to 1
     */
    public void turnAllMazeToWalls(){
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j <colNum; j++) {
                if(this.positionsMaze[i][j].getWallUp() != -1){
                    this.positionsMaze[i][j].setWallUp(1);
                }
                if(this.positionsMaze[i][j].getWallDown() != -1){
                    this.positionsMaze[i][j].setWallDown(1);
                }
                if(this.positionsMaze[i][j].getWallRight() != -1){
                    this.positionsMaze[i][j].setWallRight(1);
                }
                if(this.positionsMaze[i][j].getWallLeft() != -1){
                    this.positionsMaze[i][j].setWallLeft(1);
                }
            }
        }
    }

    /**
     * mark the start position with 'S' and the goal position with 'E'.
     * mark every wall with '1' and open path with '0'.
     */

    public void print(){
        for (int i=0;i<rowNum; i++){
            String rowString ="";
            for (int j=0; j<colNum;j++) {
                // every position will represent as '0', 'S' or 'E'
                if ((StartPosition.getRowIndex() == i) && (StartPosition.getColumnIndex() == j)) {
                    System.out.print("S");
                }
                else if (GoalPosition.getRowIndex() == i && (GoalPosition.getColumnIndex() == j)) {
                    System.out.print("E");
                }
                else{
                    System.out.print(0);
                }
                if (positionsMaze[i][j].getWallRight() != -1){ //if not on the right frame
                    System.out.print(positionsMaze[i][j].getWallRight()); // print to every position 0/1 if there isn't/is wall to his right
                }
                if (positionsMaze[i][j].getWallDown() != -1) { //if not on the bottom frame
                    rowString += positionsMaze[i][j].getWallDown(); // add to rowString 0/1 if there isn't/is wall beneath it
                    if (isEmpty == true && j != colNum-1){ // if empty maze and in range add '0' to rowString
                        rowString += 0;
                    }
                    else if(isEmpty == false && j != colNum-1){ // if not empty maze and in range add '1' to rowString
                        rowString += 1;
                    }
                }
            }
            System.out.print("\n");
            if(i != rowNum-1){ //print rowString
                System.out.print(rowString+"\n");
            }

        }
    }

    public void setIsEmpty(boolean empty) {
        isEmpty = empty;
    }


    public Position getStartPosition() {
        return StartPosition;
    }

    public Position getGoalPosition() {
        return GoalPosition;
    }

    public void setStartPosition(Position startPosition) {
        StartPosition = startPosition;
    }

    public void setGoalPosition(Position goalPosition) {
        GoalPosition = goalPosition;
    }

    public int getRowNum() {
        return rowNum;
    }

    public int getColNum() {
        return colNum;
    }
}


