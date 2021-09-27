package response;

import logging.Logger;
import request.Request;

import java.util.Map;

public class ResponseHandler {

    public void sendResponse(Request request, Logger logger, Map<String, String> scriptAliases) {
        switch(request.getRequestType()){
            case GET:
                GetResponseService getHandler = new GetResponseService(request, logger, scriptAliases);
                getHandler.sendResponse();
                break;
            case POST:
                PostResponseService postHandler = new PostResponseService(request, logger, scriptAliases);
                postHandler.sendResponse();
                break;
            case HEAD:
                HeadResponseService headHandler = new HeadResponseService(request, logger, scriptAliases);
                headHandler.sendResponse();
                break;
            case DELETE:
                DeleteResponseService deleteHandler = new DeleteResponseService(request, logger, scriptAliases);
                deleteHandler.sendResponse();
                break; 
            case PUT:
                PutResponseService putHandler = new PutResponseService(request, logger, scriptAliases);
                putHandler.sendResponse();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + request.getRequestType());
        }
    }
}
