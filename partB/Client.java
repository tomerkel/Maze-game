package Client;

import javax.imageio.IIOException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private IClientStrategy clientStrategy;
    private InetAddress ServerIP;
    private int port;

    public Client(InetAddress serverIP, int port,IClientStrategy clientStrategy) {
        this.clientStrategy = clientStrategy;
        this.ServerIP = serverIP;
        this.port = port;
    }
    //public void clientStrategy(InputStream inFromServer, OutputStream outToServer){}

    public void communicateWithServer(){
        Run();
    }
    public void Run(){
        try{
            Socket clientSocket = new Socket(ServerIP, port);
            OutputStream COS = clientSocket.getOutputStream();
            InputStream CIS = clientSocket.getInputStream();

            clientStrategy.clientStrategy(CIS,COS);
            CIS.close();
            COS.close();
            clientSocket.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
