package Model;

import Client.Client;
import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import Client.IClientStrategy;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;


public class MyModel extends Observable implements IModel {
    private Server GenerateMazeServer;
    private Server SolveProblemServer;
    private Maze maze;
    private int[][] mazeAsArray;
    private int CurrRowPlayer;
    private int CurrColPlayer;
    private Solution sol;


    public MyModel() {
        this.GenerateMazeServer = null;
        this.SolveProblemServer = null;
        this.maze = null;
        this.mazeAsArray = null;
        this.CurrRowPlayer = 0;
        this.CurrColPlayer = 0;
        this.sol = null;
        Server.setConfigurations("GenerateAlgorithm","MyMazeGenerator");
        Server.setConfigurations("SearchAlgorithm","Best First Search");
    }

    public void setSol(Solution sol) {
        this.sol = sol;
    }

    public void AddToObserverList (Observer o){
        this.addObserver(o);
    }

    public int[][] getMazeAsArray() {
        return mazeAsArray;
    }

    @Override
    public int getRowPlayer() {
        return CurrRowPlayer;
    }

    @Override
    public int getColPlayer() {
        return CurrColPlayer;
    }


    @Override
    public Maze getMaze() {
        return this.maze;
    }

    @Override
    public Solution getSolution() {
        return this.sol;
    }

    @Override
    public void SaveMaze(File MazeFile) {
        try {
            FileOutputStream fileOutForMaze = new FileOutputStream(MazeFile);
            ObjectOutputStream objectMazeOut = new ObjectOutputStream(fileOutForMaze);
            objectMazeOut.writeObject(maze);
            objectMazeOut.close();
            fileOutForMaze.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void LoadMaze(File file) {
        try{
            FileInputStream FileInForMaze = new FileInputStream(file.getAbsolutePath());
            ObjectInputStream MazeInputObj = new ObjectInputStream(FileInForMaze);
            Maze MazeFromFile = (Maze) MazeInputObj.readObject();
            this.maze = MazeFromFile;
            this.mazeAsArray = MazeFromFile.mazeToIntArray();
            this.sol = null;
            this.CurrRowPlayer = 2*maze.getStartPosition().getRowIndex();
            this.CurrColPlayer = 2*maze.getStartPosition().getColumnIndex();
            setChanged();
            notifyObservers("maze was loaded");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void startAndSetServer(){
        GenerateMazeServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        SolveProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());

        GenerateMazeServer.start();
        SolveProblemServer.start();
    }
    public void StopServers(){
        SolveProblemServer.stop();
        GenerateMazeServer.stop();

        setChanged();
        notifyObservers("Servers are close");
    }
    @Override
    public void  GenerateMaze(int row, int col) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[(2*row -1)*(2*col-1)+24 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        CurrRowPlayer = (2 * maze.getStartPosition().getRowIndex());
                        CurrColPlayer = (2 * maze.getStartPosition().getColumnIndex());
                        mazeAsArray = maze.mazeToIntArray();
                        setChanged();
                        notifyObservers("maze generated");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void SolveMaze() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {

                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        sol = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server

                        setChanged();
                        notifyObservers("maze solved");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void UpdateCharacterLocation(int direction) {
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

        boolean ifChange = false;
        switch(direction)
        {
            case 1: //NumPad_1
                if(CurrRowPlayer != 2*(maze.getRowNum()-1) && mazeAsArray[CurrRowPlayer+1][CurrColPlayer] == 0){
                    if(CurrColPlayer != 0 && mazeAsArray[CurrRowPlayer+1][CurrColPlayer-1] == 0){
                        ifChange = true;
                        CurrRowPlayer++;
                        CurrColPlayer--;
                    }
                }
                if(CurrColPlayer != 0 && mazeAsArray[CurrRowPlayer][CurrColPlayer-1] == 0 && !ifChange){
                    if(CurrRowPlayer != 2*(maze.getRowNum()-1) && mazeAsArray[CurrRowPlayer+1][CurrColPlayer-1] == 0){
                        ifChange = true;
                        CurrRowPlayer++;
                        CurrColPlayer--;
                    }
                }

                break;
            case 2: //Down or NumPad_2
                if(CurrRowPlayer != 2*(maze.getRowNum()-1) && mazeAsArray[CurrRowPlayer+1][CurrColPlayer] == 0){
                    ifChange = true;
                    CurrRowPlayer++;
                }

                break;
            case 3: //NumPad_3
                if(CurrRowPlayer != 2*(maze.getRowNum()-1) && mazeAsArray[CurrRowPlayer+1][CurrColPlayer] == 0) {
                    if (CurrColPlayer != 2 * (maze.getColNum()-1) && mazeAsArray[CurrRowPlayer + 1][CurrColPlayer + 1] == 0) {
                        ifChange = true;
                        CurrRowPlayer++;
                        CurrColPlayer++;
                    }
                }
                if(CurrColPlayer != 2*(maze.getColNum()-1) && mazeAsArray[CurrRowPlayer][CurrColPlayer+1] == 0 && !ifChange){
                    if(CurrRowPlayer != 2*(maze.getRowNum()-1) && mazeAsArray[CurrRowPlayer+1][CurrColPlayer+1] == 0){
                        ifChange = true;
                        CurrRowPlayer++;
                        CurrColPlayer++;
                    }
                }
                break;
            case 4: //Left or NumPad_4
                if(CurrColPlayer != 0 && mazeAsArray[CurrRowPlayer][CurrColPlayer-1] == 0){
                    ifChange = true;
                    CurrColPlayer--;
                }
                break;
            case 6: //Right or NumPad_6
                if(CurrColPlayer != 2*(maze.getColNum()-1) && mazeAsArray[CurrRowPlayer][CurrColPlayer+1] == 0){
                    ifChange = true;
                    CurrColPlayer++;
                }
                break;
            case 7: //NumPad_7
                if(CurrRowPlayer != 0 && mazeAsArray[CurrRowPlayer-1][CurrColPlayer] == 0){
                    if(CurrColPlayer != 0 && mazeAsArray[CurrRowPlayer-1][CurrColPlayer-1] == 0){
                        ifChange = true;
                        CurrRowPlayer--;
                        CurrColPlayer--;
                    }
                }
                if(CurrColPlayer != 0 && mazeAsArray[CurrRowPlayer][CurrColPlayer-1] == 0 && !ifChange){
                    if(CurrRowPlayer != 0 && mazeAsArray[CurrRowPlayer-1][CurrColPlayer-1] == 0){
                        ifChange = true;
                        CurrRowPlayer--;
                        CurrColPlayer--;
                    }
                }

                break;
            case 8: //Up or NumPad_8
                if(CurrRowPlayer != 0 && mazeAsArray[CurrRowPlayer-1][CurrColPlayer] == 0){
                    ifChange = true;
                    CurrRowPlayer--;
                }
                break;
            case 9: //NumPad_9
                if(CurrRowPlayer != 0 && mazeAsArray[CurrRowPlayer-1][CurrColPlayer] == 0){
                    if(CurrColPlayer != 2*(maze.getColNum()-1) && mazeAsArray[CurrRowPlayer-1][CurrColPlayer+1] == 0){
                        ifChange = true;
                        CurrRowPlayer--;
                        CurrColPlayer++;
                    }
                }
                if(CurrColPlayer != 2*(maze.getColNum()-1) && mazeAsArray[CurrRowPlayer][CurrColPlayer+1] == 0 && !ifChange){
                    if(CurrRowPlayer != 0 && mazeAsArray[CurrRowPlayer-1][CurrColPlayer+1] == 0){
                        ifChange = true;
                        CurrRowPlayer--;
                        CurrColPlayer++;
                    }
                }

                break;

        }
        if(ifChange){
            setChanged();
            notifyObservers("player moved");
        }

    }

}