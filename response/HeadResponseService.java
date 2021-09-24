package response;
import request.HttpdConf;
import request.MimeTypes;
import request.Request;

import java.io.*;
import java.net.Socket;
import java.util.Map;
public class HeadResponseService extends ResponseService {

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

    public HeadResponseService(Request request) {
        super(request);

        if(this.isValidFile(file)){
            try {
                this.fileReader = new FileReader(this.uri);
            } catch (FileNotFoundException e) {
                //TODO appropriate error handling
            }
        }
    }
    public void sendResponse(){ // basically the same response for Get but does not return file contents and body
    //TODO see if file exists
    //TODO respond with response code by calling the ResponseService class
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
            //TODO commented lines out because they did not compile
            if(isValidFile(file)){
                //TODO commented out to compile
//                writer.write(this.okResponse());
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
