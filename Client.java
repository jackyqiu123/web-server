import java.net.Socket;
import java.util.List;

public class Client {

    private Socket client;
    private List<String> request;

    private RequestType requestType;
    private String uri;

    public Client(Socket client, List<String> request) {
        this.client = client;
        this.request = request;
    }

    public void parseRequest() {
        try {
            String rawRequestType = request.get(0).split(" ")[0];

            switch(rawRequestType) {
                case "GET": requestType = RequestType.GET;
                case "HEAD": requestType = RequestType.HEAD;
                case "POST": requestType = RequestType.POST;
                case "PUT": requestType = RequestType.PUT;
                case "DELETE": requestType = RequestType.DELETE;
                default: throw new RuntimeException();
            }
        } catch (RuntimeException e) {
            //TODO response w/ status code 400
        }

        try {
            String identifier = request.get(0).split(" ")[1];
            uri = identifier;
        } catch (RuntimeException e) {
            //TODO response w/ status code 400
        }

        checkUri();

        checkAuthentication();

        sendResponse();
    }

    private void checkUri() {
        //TODO
        // if: uri aliased OR script aliased: modify uri
        // else: resolve path (DOC_ROOT + URI)
        // if: not isFile:append dirIndex
    }

    private void checkAuthentication() {
        //TODO
        // if: htaccess exists: if not auth-header exists: 401 error
        // if: not valid pwd: 403 error
    }

    private void sendResponse() {
        //TODO
    }
}