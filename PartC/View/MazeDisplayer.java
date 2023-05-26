package View;

import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * this class represent the canvas that the maze is drawn on it.
 */
public class MazeDisplayer extends Canvas {

    private int [][] maze;
    private int CurrRowPlayer = 0;
    private int CurrColPlayer = 0;
    private int GoalRow = 0;
    private int GoalCol = 0;
    private ArrayList<String> pathOfSol;
    // images
    StringProperty imageNameWall = new SimpleStringProperty();
    StringProperty imageNamePlayer = new SimpleStringProperty();
    StringProperty imageNameGoal = new SimpleStringProperty();
    StringProperty imageNameSol = new SimpleStringProperty();

    //########## Gets ##########//
    public String getImageNameSol() {
        return imageNameSol.get();
    }
    public String getImageNameGoal() {
        return imageNameGoal.get();
    }
    public int[][] getMaze() {
        return maze;
    }
    public int getGoalRow() {
        return GoalRow;
    }
    public int getGoalCol() {
        return GoalCol;
    }
    public String getImageNameWall() {
        return imageNameWall.get();
    }
    public String getImageNamePlayer() {
        return imageNamePlayer.get();
    }
    public int getCurrRowPlayer() {
        return CurrRowPlayer;
    }
    public int getCurrColPlayer() {
        return CurrColPlayer;
    }
    public ArrayList<String> getPathOfSol() {
        return pathOfSol;
    }

    //########## Sets ##########//
    public void setGoalRow(int goalRow) {
        GoalRow = goalRow;
    }
    public void setGoalCol(int goalCol) {
        GoalCol = goalCol;
    }
    public void setPathOfSol(ArrayList<String> pathOfSol) {
        this.pathOfSol = pathOfSol;
    }
    public void setImageNameSol(String ImageNameSol) {
        this.imageNameSol.set(ImageNameSol);
    }
    public void setImageNameGoal(String ImageNameGoal) {
        this.imageNameGoal.set(ImageNameGoal);
    }
    public void setMaze(int[][] NewMaze) {
        this.maze = NewMaze;
    }
    public void setImageNameWall(String imageFileNameWall) {
        this.imageNameWall.set(imageFileNameWall);
    }
    public void setImageNamePlayer(String imageNamePlayer) {
        this.imageNamePlayer.set(imageNamePlayer);
    }
    public void setCurrRowPlayer(int currRowPlayer) {
        CurrRowPlayer = currRowPlayer;
    }
    public void setCurrColPlayer(int currColPlayer) {
        CurrColPlayer = currColPlayer;
    }

    //########## Sets for the draw of maze ##########//
    public void setPlayerPosition(int NewRowPlayerPos, int NewColPlayerPos){
    double HeightOfCanvas = getHeight();
    double WidthOfCanvas = getWidth();
    int row = maze.length;
    int col = maze[0].length;
    double HeightOfCell = HeightOfCanvas/row;
    double WidthOfCell = WidthOfCanvas/col;

    double oldHeightPosPlayer = getCurrRowPlayer() * HeightOfCell;
    double oldWidthPosPlayer = getCurrColPlayer() * WidthOfCell;

    double newHeightPosPlayer = NewRowPlayerPos * HeightOfCell;
    double newWidthPosPlayer = NewColPlayerPos * WidthOfCell;

    clearDrawPlayer(HeightOfCell,WidthOfCell,oldHeightPosPlayer,oldWidthPosPlayer);
    setCurrRowPlayer(NewRowPlayerPos);
    setCurrColPlayer(NewColPlayerPos);
    drawPlayer(HeightOfCell,WidthOfCell,newHeightPosPlayer,newWidthPosPlayer);

}
    public void setGoalPosition(int GoalPosRow, int GoalPosCol){
        double HeightOfCanvas = getHeight();
        double WidthOfCanvas = getWidth();
        int row = maze.length;
        int col = maze[0].length;
        double HeightOfCell = HeightOfCanvas/row;
        double WidthOfCell = WidthOfCanvas/col;
        GraphicsContext graphicsContext = getGraphicsContext2D();
        Image ImageOfGoal = null;
        try{
            ImageOfGoal = new Image(new FileInputStream(getImageNameGoal()));
        }catch (FileNotFoundException e) {
            System.out.println("There is no goal file....");
        }
        double posOfWidth,posOfHeight;
        this.GoalRow = GoalPosRow;
        this.GoalCol = GoalPosCol;
        posOfHeight = ( this.GoalRow) * HeightOfCell;
        posOfWidth = (this.GoalCol) * WidthOfCell;

        if(ImageOfGoal == null){
            graphicsContext.fillRect(posOfWidth,posOfHeight,WidthOfCell,HeightOfCell);
        }
        else{
            graphicsContext.drawImage(ImageOfGoal,posOfWidth,posOfHeight,WidthOfCell,HeightOfCell);
        }
    }
    public void setMazeSolutionPath(Solution sol) {
        pathOfSol = new ArrayList<String>();
        ArrayList<AState> solPathAState = sol.getSolutionPath();
        int prevRow = -1;
        int prevCol = -1;
        for(int i = 0 ; i < solPathAState.size() ; i++){
            MazeState ms = (MazeState)solPathAState.get(i);
            int row = 2*ms.getStatePos().getRowIndex();
            int col = 2*ms.getStatePos().getColumnIndex();
            if(i != solPathAState.size()-1){
                pathOfSol.add( Arrays.toString(new int[]{row,col}));
            }
            if(i != 0){
                if(prevRow > row && prevCol > col){ //diagonal left-up
                    if(maze[prevRow-1][prevCol] == 0 && maze[prevRow-2][prevCol-1] == 0){
                        pathOfSol.add( Arrays.toString(new int[]{prevRow-1,prevCol}));
                        pathOfSol.add( Arrays.toString(new int[]{prevRow-2,prevCol}));
                        pathOfSol.add( Arrays.toString(new int[]{prevRow-2,prevCol -1}));
                    }
                    else{
                        pathOfSol.add( Arrays.toString(new int[]{prevRow,prevCol-1}));
                        pathOfSol.add( Arrays.toString(new int[]{prevRow,prevCol-2}));
                        pathOfSol.add( Arrays.toString(new int[]{prevRow-1,prevCol -2}));
                    }

                }
                else if(prevRow < row && prevCol > col){ //diagonal left-down
                    if(maze[prevRow+1][prevCol] == 0 && maze[prevRow+2][prevCol-1] == 0){
                        pathOfSol.add( Arrays.toString(new int[]{prevRow+1,prevCol}));
                        pathOfSol.add( Arrays.toString(new int[]{prevRow+2,prevCol}));
                        pathOfSol.add( Arrays.toString(new int[]{prevRow+2,prevCol -1}));
                    }
                    else{
                        pathOfSol.add( Arrays.toString(new int[]{prevRow,prevCol-1}));
                        pathOfSol.add( Arrays.toString(new int[]{prevRow,prevCol-2}));
                        pathOfSol.add( Arrays.toString(new int[]{prevRow+1,prevCol -2}));
                    }
                }
                else if(prevRow > row && prevCol < col){ //diagonal right-up
                    if(maze[prevRow-1][prevCol] == 0 && maze[prevRow-2][prevCol+1] == 0){
                        pathOfSol.add( Arrays.toString(new int[]{prevRow-1,prevCol}));
                        pathOfSol.add( Arrays.toString(new int[]{prevRow-2,prevCol}));
                        pathOfSol.add( Arrays.toString(new int[]{prevRow-2,prevCol+1}));
                    }
                    else{
                        pathOfSol.add( Arrays.toString(new int[]{prevRow,prevCol+1}));
                        pathOfSol.add( Arrays.toString(new int[]{prevRow,prevCol+2}));
                        pathOfSol.add( Arrays.toString(new int[]{prevRow-1,prevCol +2}));
                    }
                }
                else if(prevRow < row && prevCol  < col){ //diagonal right-down
                    if(maze[prevRow+1][prevCol] == 0 && maze[prevRow+2][prevCol+1] == 0){
                        pathOfSol.add( Arrays.toString(new int[]{prevRow+1,prevCol}));
                        pathOfSol.add( Arrays.toString(new int[]{prevRow+2,prevCol}));
                        pathOfSol.add( Arrays.toString(new int[]{prevRow+2,prevCol+1}));
                    }
                    else{
                        pathOfSol.add( Arrays.toString(new int[]{prevRow,prevCol+1}));
                        pathOfSol.add( Arrays.toString(new int[]{prevRow,prevCol+2}));
                        pathOfSol.add( Arrays.toString(new int[]{prevRow+1,prevCol+2}));
                    }
                }
                else if(prevRow > row && prevCol == col){
                    pathOfSol.add(Arrays.toString(new int[]{prevRow-1,prevCol}));
                }
                else if(prevRow < row && prevCol == col){
                    pathOfSol.add(Arrays.toString(new int[]{prevRow+1,prevCol}));
                }
                else if(prevCol > col && prevRow == row){
                    pathOfSol.add(Arrays.toString(new int[]{prevRow,prevCol-1}));
                }
                else if(prevCol < col && prevRow == row){
                    pathOfSol.add(Arrays.toString(new int[]{prevRow,prevCol+1}));
                }
            }
            prevRow = row;
            prevCol = col;
        }

    }

    //########## Canvas function ##########//
    public void CanvasToBasic(){
        double HeightOfCanvas = this.getHeight();
        double WidthOfCanvas = getWidth();
        GraphicsContext graphicsContext = getGraphicsContext2D();
        graphicsContext.clearRect(0,0,WidthOfCanvas,HeightOfCanvas);
        graphicsContext.setFill(Color.GRAY);
    }
    public void ClearAllCanvas(){
        CanvasToBasic();
        setCurrColPlayer(0);
        setCurrRowPlayer(0);
        setGoalRow(0);
        setGoalCol(0);
        setMaze(null);
        setPathOfSol(null);
    }

    //########## Player function ##########//
    public void clearDrawPlayer(double HeightOfCell, double WidthOfCell, double HeightPosPlayer, double WidthPosPlayer){
        GraphicsContext graphicsContext = getGraphicsContext2D();
        if(getCurrRowPlayer() == this.GoalRow && getCurrColPlayer() == this.GoalCol){
            setGoalPosition(this.GoalRow,this.GoalCol);
        }
        else{
            if(pathOfSol != null && pathOfSol.contains(Arrays.toString(new int[]{getCurrRowPlayer(),getCurrColPlayer()}))){
                Image imageNameSol = null;
                try{
                    imageNameSol = new Image(new FileInputStream(getImageNameSol()));
                }catch (FileNotFoundException e) {
                    System.out.println("There is no sol file....");
                }
                graphicsContext.drawImage(imageNameSol,WidthPosPlayer,HeightPosPlayer,WidthOfCell,HeightOfCell);
            }
            else{
                graphicsContext.clearRect(WidthPosPlayer,HeightPosPlayer,WidthOfCell,HeightOfCell);
            }
        }

    }

    //########## Draw ##########//
    public void drawPlayer(double HeightOfCell, double WidthOfCell, double HeightPosPlayer, double WidthPosPlayer){
        GraphicsContext graphicsContext = getGraphicsContext2D();
        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no Image player....");
        }
        graphicsContext.drawImage(playerImage,WidthPosPlayer,HeightPosPlayer,WidthOfCell,HeightOfCell);

    }
    public void drawMazeSolution(){
        double HeightOfCanvas = getHeight();
        double WidthOfCanvas = getWidth();
        int row = maze.length;
        int col = maze[0].length;
        double HeightOfCell = HeightOfCanvas/row;
        double WidthOfCell = WidthOfCanvas/col;
        GraphicsContext graphicsContext = getGraphicsContext2D();
        Image imageNameSol = null;
        try{
            imageNameSol = new Image(new FileInputStream(getImageNameSol()));
        }catch (FileNotFoundException e) {
            System.out.println("There is no goal file....");
        }
        double posOfWidth,posOfHeight;
        for(int i = 0 ; i < pathOfSol.size() ; i++){
            String SolPosAsString = pathOfSol.get(i);
            int[] solAsIntArray = Arrays.stream(SolPosAsString.substring(1, SolPosAsString.length()-1).split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
             int rowOfSol = solAsIntArray[0];
             int colOfSol = solAsIntArray[1];
             posOfHeight = rowOfSol * HeightOfCell;
             posOfWidth = colOfSol * WidthOfCell;
             if(!(rowOfSol == CurrRowPlayer && colOfSol == CurrColPlayer)){
                 if(imageNameSol == null){
                     graphicsContext.fillRect(posOfWidth,posOfHeight,WidthOfCell,HeightOfCell);
                 }
                 else{
                     graphicsContext.drawImage(imageNameSol,posOfWidth,posOfHeight,WidthOfCell,HeightOfCell);
                 }
             }
        }
    }
    public void drawMaze() {
        if (maze != null) {
            double HeightOfCanvas = this.getHeight();
            double WidthOfCanvas = getWidth();
            int row = maze.length;
            int col = maze[0].length;
            double HeightOfCell = HeightOfCanvas/row;
            double WidthOfCell = WidthOfCanvas/col;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0,0,WidthOfCanvas,HeightOfCanvas);
            graphicsContext.setFill(Color.GRAY);


            //Draw Maze
            double posOfWidth,posOfHeight;
            Image ImageOfWall = null;

            try {
                ImageOfWall = new Image(new FileInputStream(getImageNameWall()));
            }catch (FileNotFoundException e) {
                System.out.println("There is no file....");
            }

            for(int i = 0 ; i < row ; i++){
                for(int j = 0 ; j < col ; j++){
                    if(maze[i][j] == 1) // Wall
                    {
                        posOfHeight = i * HeightOfCell;
                        posOfWidth = j * WidthOfCell;
                        if(ImageOfWall == null){
                            graphicsContext.fillRect(posOfWidth,posOfHeight,WidthOfCell,HeightOfCell);
                        }
                        else{
                            graphicsContext.drawImage(ImageOfWall,posOfWidth,posOfHeight,WidthOfCell,HeightOfCell);
                        }
                    }
                }
            }

        }
    }

}
