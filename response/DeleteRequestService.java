package response;

import request.Request;

import java.io.FileReader;

public class DeleteRequestService {
    // possible responses are 202(Accepted), 204(No Content), 200(OK) for Delete request
    private String uri;
    private FileReader fileReader;
    private static int statusCode;
    private String statusReason;

    public DeleteRequestService(Request request) {
        this.uri = request.getUri();

    }
    public String sendResponse(){}

    //TODO delete the file
    //TODO respond with response code by calling the ResponseService class


}
