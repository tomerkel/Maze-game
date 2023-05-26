package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int Port;
    private int TimeOut;
    private ServerStrategy serverStrategy;
    private volatile boolean Stop;
    private ExecutorService pool = null;


    public Server(int port, int timeOut, ServerStrategy serverStrategy) {
        Port = port;
        TimeOut = timeOut;
        this.serverStrategy = serverStrategy;
        Stop = false;
        pool = Executors.newFixedThreadPool(5);


    }
    private void helpClient(Socket clientSocket){

        try {
            OutputStream toClient = clientSocket.getOutputStream();
            InputStream fromClient = clientSocket.getInputStream();


            this.serverStrategy.HandleClient(fromClient,toClient);


            fromClient.close();
            toClient.close();
            clientSocket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void Run(){
        try{
            ServerSocket serverSocket = new ServerSocket(Port);
            serverSocket.setSoTimeout(TimeOut);

            try{
                while(!Stop){
                    Socket clientSocket = serverSocket.accept();

                    Thread t = new Thread( () ->{
                        helpClient(clientSocket);

                    });
                    pool.execute(t);

                }


            }catch (IOException e) {
                System.out.println("Waiting for connections");
            }

            pool.shutdown();
            serverSocket.close();

        }catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void start(){
        Thread tr = new Thread(this::Run);
        tr.start();
    }

    public synchronized void stop() throws InterruptedException {
        Thread.sleep(1000);
        Stop = true;
    }
}
