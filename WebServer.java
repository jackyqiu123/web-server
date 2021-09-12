import request.HttpdConf;
import request.MimeTypes;

import java.net.*;
import java.io.*;
import java.lang.*;

public class WebServer{
  
  public static final int DEFAULT_PORT = 8080;
  public static void main(String[] args) throws IOException{
    final String HTTPD_CONF_FILE = "./config/httpd.conf";
    final String MIME_TYPES_FILE = "./config/mime.types";
    HttpdConf httpdConfig = new HttpdConf(HTTPD_CONF_FILE);
    MimeTypes mime = new MimeTypes(MIME_TYPES_FILE);

    httpdConfig.execute();
    mime.execute();

    int port = DEFAULT_PORT;

    if (httpdConfig.getPort() != 0) {
      port = httpdConfig.getPort();
    }

    ServerSocket serverSocket = new ServerSocket(port);
    System.out.println("Server is running on port: " + port);

    while(true){
      Socket clientSocket = serverSocket.accept();
    
      Runnable worker = new request.Worker(clientSocket, httpdConfig.gethttpdMap().get("DocumentRoot"),
              httpdConfig.getAliasMap(),httpdConfig.getScriptAliasMap());
      Thread thread = new Thread(worker);
      thread.start();
      

    }
    
  } // end of main
}
