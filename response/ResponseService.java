package response;

import request.Request;
import request.RequestType;

public class ResponseService {

    public void sendResponse(Request request) {
        // TODO: send responses
        DeleteRequestService deleteHandler = new  DeleteRequestService (request);
        GetRequestService getHandler = new GetRequestService(request);
        HeadRequestService headHandler = new HeadRequestService(request);
        PostRequestService postHandler = new PostRequestService(request);
        PutRequestService putHandler = new PutRequestService(request);
        switch(request.getRequestType()){

            case GET: 
                getHandler.sendResponse();
                break;
            case POST: 
                postHandler.sendResponse();
                break;
            case HEAD: 
                headHandler.sendResponse();
                break;
            case DELETE:
                deleteHandler.sendResponse();
                break; 
            case PUT: 
                putHandler.sendResponse();
            default:
                throw new IllegalStateException("Unexpected value: " + request.getRequestType());
        }

    }
}
