package response;

import request.HttpdConf;
import request.Request;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class GetRequestService extends Response{
    private Request request;
    private String uri;
    private FileReader fileReader;
    private int statusCode;
    private String statusReason;
    private String requestType;
    private Socket socket;
    private Map<String, String> headers;
    private File file;
    private HttpdConf httpdConf;
    private byte[] body;

    public GetRequestService(Request request) {
        this.request = request;
        this.uri = request.getUri();
        this.requestType = request.getRequestType().toString();
        this.headers = request.getHeaders();
        this.file = new File(this.uri);
        this.body = request.getBody();
        this.socket = request.getClient();

        if(this.isValidFile(file)){
            try {
                this.fileReader = new FileReader(this.uri);
            } catch (FileNotFoundException e) {
                //TODO appropriate error handling
            }
        }
    }

    public void sendResponse(){
    //TODO get the file
    //TODO respond with response code and file-body by calling the ResponseService class
    
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
            //TODO commented lines out because they did not compile
            if(isValidFile(file)){
                writer.write(this.okResponse());
                writer.write(this.getFileContents());
                writer.write(this.body.toString()); // server return the body content of the request
                writer.flush();
                writer.close();
            }
            else{
                //writer.write(this.notFoundResponse());
                writer.flush();
                writer.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        

    }
    
}
