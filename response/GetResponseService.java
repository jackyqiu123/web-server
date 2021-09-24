package response;

import request.HttpdConf;
import request.Request;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class GetResponseService extends ResponseService {
//    private Request request;
//    private String uri;
//    private FileReader fileReader;
//    private int statusCode;
//    private String statusReason;
//    private String requestType;
//    private Socket socket;
//    private Map<String, String> headers;
//    private File file;
//    private HttpdConf httpdConf;
//    private byte[] body;

    public GetResponseService(Request request) {
        super(request);
    }

    public void sendResponse(){
    //TODO get the file
    //TODO respond with response code and file-body by calling the ResponseService class
    
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.getSocket().getOutputStream()))){
            //TODO commented lines out because they did not compile
            if(isValidFile(this.getFile())){
                writer.write(this.okResponse());
                writer.write(this.getFileContents());
                writer.write(this.getBody().toString()); // server return the body content of the request
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
