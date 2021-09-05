import java.io.File;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Worker {

    private Socket client;
    private List<String> request;
    private String documentRoot;

    private Map<String, String> aliases;
    private Map<String, String> scriptAliases;


    private RequestType requestType;
    private String uri;

    public Worker(Socket client,
                  List<String> request,
                  Map<String, String> aliases,
                  Map<String, String> scriptAliases) {
        this.client = client;
        this.request = request;
        this.aliases = aliases;
        this.scriptAliases = scriptAliases;
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

        uri = checkUri();

        checkAuthentication();

        sendResponse();
    }

    private String checkUri() {
        String[] uriParts = uri.split("/");
        String newUri = "";

        uriParts = Arrays.stream(uriParts).filter(item -> !item.equals("")).toArray(String[]::new);

        Boolean isAliased = false;

        for (String uriPart : uriParts) {
            //TODO slashes also after 'uriPart'
            uriPart = "/" + uriPart + "/";
            if (aliases.containsKey(uriPart)) {
                uriPart = aliases.get(uriPart);
                isAliased = true;
                newUri += uriPart;
            } else if (scriptAliases.containsKey(uriPart)) {
                uriPart = aliases.get(uriPart);
                isAliased = true;
                newUri += uriPart;
            } else {
                newUri += uriPart;
            }
        }

        uri = newUri;

        // remove trailing '/'
        uri = uri.substring(0, uri.length() - 1);

        if (!isAliased) {
            uri = documentRoot + uri;
        }


        File file = new File(uri);

        if (!file.exists() && !file.isDirectory()) {
            //TODO respond with 404 not found ???
            return "";
        }

        if (file.isDirectory()) {
            //TODO append dirIndex
        }

        System.out.println("URI = " + uri);

        return uri;
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