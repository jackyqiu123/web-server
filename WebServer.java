import java.net.*;
import java.io.*;

public class WebServer {
  public static final int DEFAULT_PORT = 8080;

  public static void main(String[] args) throws IOException{
    ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
    System.out.println("Server is running on port: " + DEFAULT_PORT);

    while(true){
      Socket clientSocket = serverSocket.accept();
      System.err.println("Client connected");
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    }
    
  } // end of main
}