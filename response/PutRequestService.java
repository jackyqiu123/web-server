package response;

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
    private bytes[] body;

    public PutRequestService(Request request) {
        this.request = request;
        this.uri = request.getUri();
        this.requestType = request.getRequestType().toString();
        this.headers = request.getHeaders();
        this.file = new File(this.uri);
        this.body = request.getBody();

        if(this.isValidFile(request, this.file)){
            try {
                this.fileReader = new FileReader(this.uri);
            } catch (FileNotFoundException e) {
                //TODO appropriate error handling
            }
        }
    }
    public void sendResponse(){
    //TODO create or update the file
    //TODO respond with response code by calling the ResponseService class
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
            if(!this.file.exist()){ // file does not exist and will create it
                if(this.file.createNewFile()){
                    writer.write(this.createdResponse());
                    writer.flush();
                    writer.close();
                } 
                else{ // unsucessful in createing new file
                    writer.write(badRequest());
                    writer.flush();
                    writer.close();
                }
            }
            else{ // file already exist
                writer.write(okResponse());
                writer.flush();
                writer.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
