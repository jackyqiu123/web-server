package response;

import request.Request;

public class ResponseHandler {

    public void sendResponse(Request request) {
        // TODO: send responses
        DeleteResponseService deleteHandler = new DeleteResponseService(request);
        GetResponseService getHandler = new GetResponseService(request);
        HeadResponseService headHandler = new HeadResponseService(request);
        PostResponseService postHandler = new PostResponseService(request);
        PutResponseService putHandler = new PutResponseService(request);
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
