package response;

import request.HttpdConf;
import request.MimeTypes;
import request.Request;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
public class PostRequestService extends Response{

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
    private MimeTypes mime;
    private byte[] body;

    public PostRequestService(Request request) {
        this.request = request;
        this.uri = request.getUri();
        this.requestType = request.getRequestType().toString();
        this.headers = request.getHeaders();
        this.file = new File(this.uri);
        this.body = request.getBody();
        this.socket = request.getClient();

        if(this.isValidFile(this.file)){
            try {
                this.fileReader = new FileReader(this.uri);
            } catch (FileNotFoundException e) {
                //TODO appropriate error handling
            }
        }
    }
    public void sendResponse(){
    //TODO create the file
    //TODO respond with response code by calling the ResponseService class
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
            if(isValidFile(this.file)){
                String byteString = new String(this.body, StandardCharsets.UTF_8);
                if(byteString == ""){
                    //TODO commented out to compile
//                    writer.write(this.okResponse());
                    writer.flush();
                    writer.close();
                }
                else{
                    //TODO commented out to compile
//                    writer.write(this.createdResponse());
                    writer.flush();
                    writer.close();
                }
            
            }
            else{
                //TODO commented out to compile
//                writer.write(this.forbiddenResponse());
                writer.flush();
                writer.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
