package response;

import logging.Logger;
import request.Request;

public class ResponseHandler {

    public void sendResponse(Request request, Logger logger) {
        switch(request.getRequestType()){
            case GET:
                GetResponseService getHandler = new GetResponseService(request, logger);
                getHandler.sendResponse();
                break;
            case POST:
                PostResponseService postHandler = new PostResponseService(request, logger);
                postHandler.sendResponse();
                break;
            case HEAD:
                HeadResponseService headHandler = new HeadResponseService(request, logger);
                headHandler.sendResponse();
                break;
            case DELETE:
                DeleteResponseService deleteHandler = new DeleteResponseService(request, logger);
                deleteHandler.sendResponse();
                break; 
            case PUT:
                PutResponseService putHandler = new PutResponseService(request, logger);
                putHandler.sendResponse();
            default:
                throw new IllegalStateException("Unexpected value: " + request.getRequestType());
        }
    }
}
