package response;

import request.Request;

import java.io.*;
import java.net.Socket;
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
    private bytes[] body;

    public PostRequestService(Request request) {
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
        mime.execute();
        httpdConf.execute();
    }
    public void sendResponse(){
    //TODO create the file
    //TODO respond with response code by calling the ResponseService class
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
            if(isValidFile(this.request, this.file)){
                String byteString = new String(this.body, standardCharsets.UTF_8);
                if(byteString == ""){
                    writer.write(this.okResponse());
                    writer.flush();
                    writer.close();
                }
                else{
                    writer.write(this.createdResponse());
                    writer.flush();
                    writer.close();
                }
            
            }
            else{
                writer.write(this.forbiddenResponse());
                writer.flush();
                writer.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
