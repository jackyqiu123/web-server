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
        GetRequestService getHandler = new GetRequestService(request.getUri());
        HeadRequestService headHandler = new HeadRequestService(request.getUri());
        PostRequestService postHandler = new PostRequestService(request.getUri());
        PutRequestService putHandler = new PutRequestService(request.getUri());
        switch(request.getRequestType()){
            case GET: // getHandler executes method
            case POST: // post Handler executes method
            case HEAD: // headHandler executes method
            case DELETE: // deleteHandler executes method
            case PUT: // putHandler executes method
            default:
                throw new IllegalStateException("Unexpected value: " + request.getRequestType());
        }

    }
}
