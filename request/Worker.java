package request;

import authentication.Authenticator;
import response.ResponseCode;
import response.ResponseService;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;

public class Worker {

    private Request request;

    private Socket client;
    private String documentRoot;

    private Map<String, String> aliases;
    private Map<String, String> scriptAliases;

    public Worker(Socket client,
                  String documentRoot,
                  Map<String, String> aliases,
                  Map<String, String> scriptAliases) {
        this.client = client;
        this.documentRoot = documentRoot;
        this.aliases = aliases;
        this.scriptAliases = scriptAliases;
    }

    public void parseRequest() {
        createRequestObject();

        ResponseCode responseCode = ResponseCode.CODE200;

        request.setUri(checkUri(request.getUri()));

        Authenticator authenticator = new Authenticator(request);
        responseCode = authenticator.checkAuthentication();

        //TODO check response code

        ResponseService response = new ResponseService();
        responseCode = response.sendResponse();

        //TODO check response code
    }

    private void createRequestObject() {
    }

    private String checkUri(String uri) {
        String[] uriParts = uri.split("/");
        String newUri = "";

        uriParts = Arrays.stream(uriParts).filter(item -> !item.equals("")).toArray(String[]::new);

        Boolean isAliased = false;

        for (String uriPart : uriParts) {
            //TODO slashes also after 'uriPart' ?
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
}