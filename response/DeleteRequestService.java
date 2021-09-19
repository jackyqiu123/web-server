package response;

import request.Request;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class DeleteRequestService extends Response{
    // possible responses are 202(Accepted), 204(No Content), 200(OK) for Delete request
    private Request request;
    private String uri;
    private FileReader fileReader;
    private int statusCode;
    private String statusReason;
    private String requestType;
    private Socket socket;
    private Map<String, String> headers;
    private File file;

    public DeleteRequestService(Request request) {
        this.request = request;
        this.uri = request.getUri();
        this.requestType = request.getRequestType().toString();
        this.headers = request.getHeaders();
        this.file = new File(this.uri);

        if(this.isValidFile(request, this.file)){
            try {
                this.fileReader = new FileReader(this.uri);
            } catch (FileNotFoundException e) {
                //TODO appropriate error handling
            }
        }
    }

    public void sendResponse(){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            // String mime = headers.get("Content-Type");
            // String fileSize = headers.get("Content-Length");
            // String httpVersion = request.getHttpVersion();
            String fileSize = null; //TODO idk what this should be
            if((fileSize == null || fileSize == "0") && this.isValidFile(this.request, this.file)){
                // this.statusCode = 204;
                // this.statusReason = "NO CONTENT";
                // writer.write(httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
                // writer.write("Content-Length: " + fileSize + "\r\n");
                // writer.write("Content-Type: " + mime + "\r\n");

                //TODO commented out to compile:
                //writer.write(this.noContentResponse()); // note: writing out bytes, can be converted into a string
                this.file.delete();
                writer.flush();
                writer.close();
            }
            else{ // send 404 Not Found
                // this.statusCode = 404;
                // this.statusReason = "NOT FOUND";
                // writer.write(this.httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
                // writer.write("Content-Type: " + mime + "\r\n");

                //TODO commented out to compile:
                //writer.write(this.notFoundResponse());
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //TODO delete the file
    //TODO respond with response code by calling the ResponseService class


}
