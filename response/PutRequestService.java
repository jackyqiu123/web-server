package response;

import request.HttpdConf;
import request.Request;

import java.io.*;
import java.net.Socket;
import java.util.Map;
public class PutRequestService {

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

    public PutRequestService(Request request) {
        this.request = request;
        this.uri = request.getUri();
        this.requestType = request.getRequestType().toString();
        this.headers = request.getHeaders();
        this.file = new File(this.uri);
        this.body = request.getBody();
        this.socket = request.getClient();
        this.request = request;

        if(this.isValidFile(request, this.file)){
            try {
                this.fileReader = new FileReader(this.uri);
            } catch (FileNotFoundException e) {
                //TODO appropriate error handling
            }
        }
    }

    private boolean isValidFile(Request request, File file) {
        //TODO impement - added to compile
        return false;
    }

    public void sendResponse(){
    //TODO create or update the file
    //TODO respond with response code by calling the ResponseService class
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
            if(!this.file.exists()){ // file does not exist and will create it
                if(this.file.createNewFile()){
                    //TODO commented out to compile
//                    writer.write(this.createdResponse());
                    writer.flush();
                    writer.close();
                } 
                else{ // unsucessful in createing new file
                    //TODO commented out to compile
//                    writer.write(badRequest());
                    writer.flush();
                    writer.close();
                }
            }
            else{ // file already exist
                //TODO commented out to compile
//                writer.write(okResponse());
                writer.flush();
                writer.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
