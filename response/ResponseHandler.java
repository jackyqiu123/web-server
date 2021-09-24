package response;

import request.Request;

public class ResponseHandler {

    public void sendResponse(Request request) {
        switch(request.getRequestType()){
            case GET:
                GetResponseService getHandler = new GetResponseService(request);
                getHandler.sendResponse();
                break;
            case POST:
                PostResponseService postHandler = new PostResponseService(request);
                postHandler.sendResponse();
                break;
            case HEAD:
                HeadResponseService headHandler = new HeadResponseService(request);
                headHandler.sendResponse();
                break;
            case DELETE:
                DeleteResponseService deleteHandler = new DeleteResponseService(request);
                deleteHandler.sendResponse();
                break; 
            case PUT:
                PutResponseService putHandler = new PutResponseService(request);
                putHandler.sendResponse();
            default:
                throw new IllegalStateException("Unexpected value: " + request.getRequestType());
        }
    }
}
