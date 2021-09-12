package request;

import authentication.Authenticator;
import resource.ResourceChecker;
import response.ResponseCode;
import response.ResponseService;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;

public class Worker implements Runnable {

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

    public void run() {
        createRequestObject();

        ResponseCode responseCode = ResponseCode.CODE200;


        ResourceChecker resourceChecker = new ResourceChecker(aliases, scriptAliases, documentRoot);
        request.setUri(resourceChecker.checkUri(request.getUri()));

        Authenticator authenticator = new Authenticator(request);
        responseCode = authenticator.checkAuthentication();

        //TODO check response code

        ResponseService response = new ResponseService();
        responseCode = response.sendResponse();

        //TODO check response code
    }

    private void createRequestObject() {
    }
}