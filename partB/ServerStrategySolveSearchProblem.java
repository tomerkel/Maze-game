package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.BestFirstSearch;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;
import sun.awt.Mutex;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerStrategySolveSearchProblem implements ServerStrategy {

    private static ConcurrentHashMap SolutionMap;
    private AtomicInteger num;
    private Mutex mutex;

    public ServerStrategySolveSearchProblem() {
        SolutionMap = new ConcurrentHashMap<String,String>();
        this.num = new AtomicInteger(1);
        this.mutex = new Mutex();
    }
    @Override
    public void HandleClient(InputStream IStream, OutputStream OStream) {
        try {
            ObjectOutputStream OOS = new ObjectOutputStream(OStream);
            ObjectInputStream OINS = new ObjectInputStream(IStream);
            String tempDirectoryPath = System.getProperty("java.io.tmpdir");

            try {
                Maze toSolveMaze = (Maze) OINS.readObject();
                Solution solution = null;
                byte[] MazeAsByteArray = toSolveMaze.toByteArray();
                mutex.lock();
                if(SolutionMap.containsKey(Arrays.toString(MazeAsByteArray))){

                    FileInputStream SolutionInputFile = new FileInputStream(tempDirectoryPath+"\\"+SolutionMap.get(Arrays.toString(MazeAsByteArray)));
                    ObjectInputStream SolutionObject = new ObjectInputStream(SolutionInputFile);
                    solution = (Solution) SolutionObject.readObject();
                }
                else{
                    SearchableMaze searchableMaze = new SearchableMaze(toSolveMaze);
                    BestFirstSearch BestFS =new BestFirstSearch();
                    solution = BestFS.solve(searchableMaze);

                    SolutionMap.put(Arrays.toString(MazeAsByteArray),"Sol"+ num.toString());
                    File SolFile = new File(tempDirectoryPath+"\\"+"Sol"+ num.toString());
                    File MazeFile = new File(tempDirectoryPath+"\\"+"Maze"+ num.toString());
                    num.incrementAndGet();


                    FileOutputStream fileOutForMaze = new FileOutputStream(MazeFile);
                    ObjectOutputStream objectMazeOut = new ObjectOutputStream(fileOutForMaze);
                    objectMazeOut.write(MazeAsByteArray);
                    objectMazeOut.close();

                    FileOutputStream fileOutForSolution = new FileOutputStream(SolFile);
                    ObjectOutputStream objectSolutionOut = new ObjectOutputStream(fileOutForSolution);
                    objectSolutionOut.writeObject(solution);
                    objectSolutionOut.close();

                }
                mutex.unlock();
                OOS.writeObject(solution);
                OOS.flush();


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            OOS.close();
            OINS.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
