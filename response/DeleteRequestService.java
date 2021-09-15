package response;

import request.Request;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class DeleteRequestService {
    // possible responses are 202(Accepted), 204(No Content), 200(OK) for Delete request
    private Request request;
    private String uri;
    private FileReader fileReader;
    private static int statusCode;
    private String statusReason;
    private String requestType;
    private Socket socket;
    private Map<String, String> headers;
    public DeleteRequestService(Request request) {
        this.uri = request.getUri();
        this.requestType = request.getRequestType().toString();
        this.headers = request.getHeaders();
    }

    public void sendResponse(){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            String mime = headers.get("Content-Type");
            String fileSize = headers.get("Content-Length");
            String httpVersion = request.getHttpVersion();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO delete the file
    //TODO respond with response code by calling the ResponseService class


}
