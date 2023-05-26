package algorithms.mazeGenerators;

import java.util.*;

/**
 * this generator will use Kruskal's algorithm to create a maze with at least 1 solution
 */
public class MyMazeGenerator extends AMazeGenerator {
    /**
     * data structure DisJointSet that help us use Kruskal's algorithm efficiently
     */
    private class DisJointSet {
        private Map<Position, Position> Parent = new HashMap<>();
        private Map<Position, Integer> Rank = new HashMap<>();

        /**
         * this function will build the maps data structures of the DisJointSet class
         * @param PosArr - 2D array of positions
         */
        private void MakeDisJointSet(Position[][] PosArr){
            for (int i = 0; i < PosArr.length; i++) {
                for (int j = 0; j < PosArr[i].length; j++) {
                    Parent.put(PosArr[i][j],PosArr[i][j]);   // initialize the parent map that each position will be is own parent
                    Rank.put(PosArr[i][j],0);    // initialize the rank map that each position rank will be 0
                }
            }
        }

        /**
         * this function will find the root of the given position
         * the root represent the farthest position that there is a path to the position
         * @param pos - position
         * @return - the root of the given position
         */
        private Position find(Position pos){
            //if position is not the root
            if(!((Parent.get(pos).getRowIndex() == pos.getRowIndex())&&(Parent.get(pos).getColumnIndex() == pos.getColumnIndex()))){
                Parent.put(pos,find(Parent.get(pos)));   // this will get the tree depth shorter
            }
            return Parent.get(pos);
        }

        /**
         * This function will merge groups if needed
         * the merge will be between two roots
         * @param pos1 - position 1
         * @param pos2 - position 2
         */
        private void union(Position pos1,Position pos2){
            // find the root of each Position
            Position pos1root = find(pos1);
            Position pos2root = find(pos2);

            // if the two Positions are in the same group
            if (pos1root.getRowIndex() == pos2root.getRowIndex() && pos1root.getColumnIndex() == pos2root.getColumnIndex()){
                return;
            }

            //if the depth of the first position is bigger
            if (Rank.get(pos1root) > Rank.get(pos2root)){
                Parent.put(pos2root,pos1root);
            }

            //if the depth of the second position is bigger
            else if(Rank.get(pos2root) > Rank.get(pos1root)){
                Parent.put(pos1root,pos2root);
            }

            // the depth is equal
            else{
                Parent.put(pos2root,pos1root);
                Rank.put(pos1root,Rank.get(pos1root)+1);
            }
        }
    }

    /**
     * generate new maze with kruskal algorithm
     * @param rowNum - the number of rows in the maze
     * @param colNum - the number of columns in the maze
     * @return - new maze
     */
    @Override
    public Maze generate(int rowNum, int colNum) {
        // create an empty maze and turn all maze to walls
        EmptyMazeGenerator EMP = new EmptyMazeGenerator();
        Maze newMaze = EMP.generate(rowNum, colNum);
        newMaze.setIsEmpty(false);
        newMaze.turnAllMazeToWalls();

        // create the disJointSet data structure
        DisJointSet DJSet = new DisJointSet();
        DJSet.MakeDisJointSet(newMaze.positionsMaze);

        int numOfGroups = rowNum*colNum; // the max number of groups (the number of group in the start)
        while(numOfGroups > 1){

            //choose random position
            int row = new Random().nextInt(rowNum);
            int col = new Random().nextInt(colNum);
            Position currPos = newMaze.positionsMaze[row][col];

            // get all the neighbors positions of this current position
            ArrayList<Position> neighborPosArr = neighborPosArrayCreate(currPos,rowNum,colNum);

            while (neighborPosArr.size() > 0){
                // choose randomly a position from the neighbors array
                int index = new Random().nextInt(neighborPosArr.size());
                Position tempPos = neighborPosArr.get(index);
                tempPos = newMaze.positionsMaze[tempPos.getRowIndex()][tempPos.getColumnIndex()];

                // find the roots of current and temp positions
                Position parentcurr = DJSet.find(currPos);
                Position parenttemp = DJSet.find(tempPos);

                // if the two positions are in the same group they will have the same root
                if(parentcurr.getRowIndex() == parenttemp.getRowIndex() && parentcurr.getColumnIndex() == parenttemp.getColumnIndex()){
                    neighborPosArr.remove(index);
                }
                //if the two positions belong to two different groups then merge the groups and break the wall between them
                else{
                    breakTheWalls(currPos,tempPos);
                    DJSet.union(currPos,tempPos);
                    numOfGroups--;
                    break;
                }
            }
        }

        return newMaze;
    }

    /**
     * this function break the wall between 2 positions to open path (turn 1 to 0)
     * @param posCurr - the position that we "stand" on
     * @param posTemp - the position that we want to reach
     */
    private void breakTheWalls(Position posCurr, Position posTemp){
        if(posCurr.getRowIndex() == posTemp.getRowIndex()){
            //we move Left
            if(posCurr.getColumnIndex() > posTemp.getColumnIndex()){
                posCurr.setWallLeft(0);
                posTemp.setWallRight(0);

            }
            // we move Right
            else{
                posCurr.setWallRight(0);
                posTemp.setWallLeft(0);
            }
        }
        else{
            if(posCurr.getRowIndex() > posTemp.getRowIndex()){
                //we move Up
                posCurr.setWallUp(0);
                posTemp.setWallDown(0);
            }
            // we move Down
            else{
                posCurr.setWallDown(0);
                posTemp.setWallUp(0);
            }
        }
    }

    /**
     * create an array of all the available neighbors of the current position
     * @param currPos - current position
     * @param rowNum - the limit of maze row number
     * @param colNum - the limit of maze column number
     * @return an array of positions
     */
    private ArrayList<Position> neighborPosArrayCreate(Position currPos,int rowNum,int colNum){
        ArrayList<Position> neighborPos = new ArrayList<>();

        // add neighbors of currPos to the array if they not out of range
        if(currPos.getRowIndex()-1 >= 0){
            neighborPos.add(new Position((currPos.getRowIndex()-1),(currPos.getColumnIndex())));
        }
        if(currPos.getRowIndex()+1 < rowNum){
            neighborPos.add(new Position((currPos.getRowIndex()+1),(currPos.getColumnIndex())));
        }
        if(currPos.getColumnIndex()-1 >= 0 ){
            neighborPos.add(new Position((currPos.getRowIndex()),(currPos.getColumnIndex()-1)));
        }
        if(currPos.getColumnIndex()+1 < colNum ){
            neighborPos.add(new Position((currPos.getRowIndex()),(currPos.getColumnIndex()+1)));
        }
        return neighborPos;
    }


}


