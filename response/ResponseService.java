package response;

import request.Request;
import request.RequestType;

public class ResponseService {

    private ResponseCode responseCode;
    private byte[] body;
    //maybe some header?

    public ResponseService(ResponseCode responseCode, byte[] body) {
        this.responseCode = responseCode;
        this.body = body;
    }



    public void sendResponse(Request request) {
        // TODO: send responses
        DeleteRequestService deleteHandler = new  DeleteRequestService (request);
        GetRequestService getHandler = new GetRequestService(request);
        HeadRequestService headHandler = new HeadRequestService(request);
        PostRequestService postHandler = new PostRequestService(request);
        PutRequestService putHandler = new PutRequestService(request);
        switch(request.getRequestType()){
            case GET: getHandler.sendResponse();
            case POST: postHandler.sendResponse();
            case HEAD: headHandler.sendResponse();
            case DELETE: deleteHandler.sendResponse();
            case PUT: putHandler.sendResponse();
            default:
                throw new IllegalStateException("Unexpected value: " + request.getRequestType());
        }

    }
}
