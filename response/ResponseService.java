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

            case GET: 
                getHandler.sendResponse(request);
                break;
            case POST: 
                postHandler.sendResponse(request);
                break;
            case HEAD: 
                headHandler.sendResponse(request);
                break;
            case DELETE:
                deleteHandler.sendResponse(request);
                break; 
            case PUT: 
                putHandler.sendResponse(request);
            default:
                throw new IllegalStateException("Unexpected value: " + request.getRequestType());
        }

    }
}
