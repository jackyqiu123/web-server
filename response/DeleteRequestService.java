package response;

import request.Request;

import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class DeleteRequestService {
    // possible responses are 202(Accepted), 204(No Content), 200(OK) for Delete request
    private String uri;
    private FileReader fileReader;
    private static int statusCode;
    private String statusReason;
    private String requestType;
    private Socket socket;
    private HashMap<String, String> headers;
    public DeleteRequestService(Request request) {
        this.uri = request.getUri();
        this.requestType = request.getRequestType.toString();
        this.headers = request.getHeaders();
    }
    public void sendResponse(){
        BufferedWriter writer = new BufferedWrite(new OutputStreamWriter(socket.getOutputStream()));
        String mime = headers.get("Content-Type");
        String fileSize = headers.get("Content-Length");
        String httpVersion = request.getHttpVersion();
    }

    //TODO delete the file
    //TODO respond with response code by calling the ResponseService class


}
