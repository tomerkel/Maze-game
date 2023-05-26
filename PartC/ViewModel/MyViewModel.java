package ViewModel;

import Model.IModel;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private Maze maze;
    private int [][] mazeAsArray;
    private Solution sol;
    private int rowPlayer;
    private int colPlayer;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.AddToObserverList(this);
        this.maze = null;
        this.mazeAsArray = null;
        this.sol = null;
        this.colPlayer = 0;
        this.rowPlayer = 0;
    }

    public void setSol(Solution sol) {
        this.sol = sol;
        this.model.setSol(null);
    }

    public Maze getMaze() {
        return maze;
    }

    public int[][] getMazeAsArray() {
        return mazeAsArray;
    }

    public Solution getSolution() {
        return sol;
    }

    public int getRowPlayer() {
        return rowPlayer;
    }

    public int getColPlayer() {
        return colPlayer;
    }

    public void GenerateMaze(String row, String col){
        if(row.matches("[0-9]+") && col.matches("[0-9]+")){
            if(Integer.valueOf(row) > 500 || Integer.valueOf(col) > 500 || Integer.valueOf(row) < 2 || Integer.valueOf(col) < 2){
                setChanged();
                notifyObservers("input is not valid");
            }
            else{
                this.model.GenerateMaze(Integer.valueOf(row),Integer.valueOf(col));
            }
        }
        else{
            setChanged();
            notifyObservers("input is not valid");
        }
    }

    public void solveMaze(){
        this.model.SolveMaze();
    }


    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof IModel){
            String changeString = (String)arg;

            switch(changeString){
                case "maze generated": case "maze was loaded":
                    this.maze = model.getMaze();
                    this.mazeAsArray = model.getMazeAsArray();
                    this.rowPlayer = model.getRowPlayer();
                    this.colPlayer = model.getColPlayer();
                    break;
                case "maze solved":
                    this.sol = model.getSolution();
                    break;
                case "player moved":
                    this.rowPlayer = model.getRowPlayer();
                    this.colPlayer = model.getColPlayer();
                    if(this.rowPlayer == 2*model.getMaze().getGoalPosition().getRowIndex() && this.colPlayer ==2*model.getMaze().getGoalPosition().getColumnIndex() ){
                        changeString = "you made it to the goal!";
                    }
                    break;
                case "Servers are close":
                    break;
            }
            setChanged();
            notifyObservers(changeString);

        }
    }
    public void movePlayer(KeyEvent keyEvent)
    {
        /*
            direction = 1 -> NumPad_1
            direction = 2 -> Down or NumPad_2
            direction = 3 -> NumPad_3
            direction = 4 -> Left or NumPad_4
            direction = 6 -> Right or NumPad_6
            direction = 7 -> NumPad_7
            direction = 8 -> Up or NumPad_8
            direction = 9 -> NumPad_9
         */
        int direction = -1;

        switch (keyEvent.getCode()){
            case NUMPAD1: // NumPad_1
                direction = 1;
                break;
            case DOWN: case NUMPAD2:// Down or NumPad_2
                direction = 2;
                break;
            case NUMPAD3: // NumPad_3
                direction = 3;
                break;
            case LEFT: case NUMPAD4: // Left or NumPad_4
                direction = 4;
                break;
            case RIGHT: case NUMPAD6: // Right or NumPad_6
                direction = 6;
                break;
            case NUMPAD7: // NumPad_7
                direction = 7;
                break;
            case UP: case NUMPAD8:// Up or NumPad_8
                direction = 8;
                break;
            case NUMPAD9: // NumPad_9
                direction = 9;
                break;
        }
        if(direction != -1){
            model.UpdateCharacterLocation(direction);
        }

    }

    public void SaveMaze(File file) {
        if(file != null){
            model.SaveMaze(file);
        }
    }

    public void LoadMaze(File file) {
        if(file != null){
            model.LoadMaze(file);
        }
    }

    public void CloseServers() {
        model.StopServers();
    }
}
