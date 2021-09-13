package response;

public class ResponseService {

    private ResponseCode responseCode;
    private byte[] body;
    //maybe some header?

    public ResponseService(ResponseCode responseCode, byte[] body) {
        this.responseCode = responseCode;
        this.body = body;
    }



    public void sendResponse() {

        // TODO: send responses

    }
}
