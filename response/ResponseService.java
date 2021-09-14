package response;

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
            case "GET": // getHandler executes method
            case "POST": // post Handler executes method
            case "HEAD": // headHandler executes method
            case "DELETE": // deleteHandler executes method
            case "PUT": // putHandler executes method
        }  

    }
}
