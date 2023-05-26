package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.io.*;

public class ServerStrategyGenerateMaze implements ServerStrategy{
    @Override
    public void HandleClient(InputStream IStream, OutputStream OStream) {
        try {
            ObjectOutputStream OOS = new ObjectOutputStream(OStream);
            ObjectInputStream OINS = new ObjectInputStream(IStream);
            ByteArrayOutputStream BAOS = new ByteArrayOutputStream();

            try {
                int[] size = (int[]) OINS.readObject();
                int rowNum = size[0];
                int colNum = size[1];
                MyMazeGenerator MazeGenerator = new MyMazeGenerator();
                Maze newMaze = MazeGenerator.generate(rowNum,colNum);
                OutputStream out = new MyCompressorOutputStream(BAOS);
                out.write(newMaze.toByteArray());
                byte[] MazeByteArray = BAOS.toByteArray();
                OOS.writeObject(MazeByteArray);
                out.flush();
                out.close();


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            OOS.close();
            OINS.close();
            BAOS.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
