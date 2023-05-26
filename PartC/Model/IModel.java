package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.io.File;
import java.util.Observer;

public interface IModel {
    public void GenerateMaze(int row, int col);
    public Maze getMaze();
    public void UpdateCharacterLocation(int direction);
    public int getRowPlayer();
    public int getColPlayer();
    public void SolveMaze();
    public Solution getSolution();
    public int[][] getMazeAsArray();
    public void AddToObserverList(Observer o);

    public void SaveMaze(File MazeFile);

    void LoadMaze(File file);
    public void startAndSetServer();
    public void StopServers();

    public void setSol(Solution sol);
}
