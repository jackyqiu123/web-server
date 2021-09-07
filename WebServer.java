import java.net.*;
import java.io.*;
import java.lang.*;

public class WebServer {
  public static final int DEFAULT_PORT = 8080;
  
  public static void main(String[] args) throws IOException{
    ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
    final String HTTPD_CONF_FILE = "./config/httpd.conf";
    final String MIME_TYPES_FILE = "./config/mime.types";
    HttpdConf httpdConfig = new HttpdConf(HTTPD_CONF_FILE);
    MimeTypes mime = new MimeTypes(MIME_TYPES_FILE);
    System.out.println("Server is running on port: " + DEFAULT_PORT);

    while(true){
      Socket clientSocket = serverSocket.accept();
      // Thread worker = new Worker(clientSocket, , httpdConfig.getAliasMap(),httpdConfig.getScriptAliasMap());
      // worker.start();
      System.err.println("Client connected");
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    }
    
  } // end of main
}
