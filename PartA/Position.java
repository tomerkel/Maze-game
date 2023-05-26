package algorithms.mazeGenerators;


/**
 * this class represent a place in the maze
 * to each place we will save his rowIndex and colIndex
 * this class will hold information of all the possibles walls around it:
 *      * the fields will represent if there is a wall to every direction
 *      * wall = 1
 *      * open path = 0
 *      * out of range = -1
 */
public class Position {
    private int RowIndex;
    private int ColumnIndex;
    private int wallUp;
    private int wallDown;
    private int wallRight;
    private int wallLeft;


    public Position(int rowIndex, int columnIndex) {
        RowIndex = rowIndex;
        ColumnIndex = columnIndex;
    }

    public int getRowIndex() {
        return RowIndex;
    }

    public int getColumnIndex() {
        return ColumnIndex;
    }

    @Override
    public String toString() {
        return "{"+RowIndex+","+ColumnIndex+"}";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) {
            return false;
        }
        Position pos = (Position) obj;
        if ((pos.getRowIndex() == RowIndex) && (pos.getColumnIndex() == ColumnIndex)){
            return true;
        }
        return false;
    }


    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    public int getWallUp() {
        return wallUp;
    }

    public void setWallUp(int wallUp) {
        this.wallUp = wallUp;
    }

    public int getWallDown() {
        return wallDown;
    }

    public void setWallDown(int wallDown) {
        this.wallDown = wallDown;
    }

    public int getWallRight() {
        return wallRight;
    }

    public void setWallRight(int wallRight) {
        this.wallRight = wallRight;
    }

    public int getWallLeft() {
        return wallLeft;
    }

    public void setWallLeft(int wallLeft) {
        this.wallLeft = wallLeft;
    }
}
