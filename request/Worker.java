package request;

import authentication.Authenticator;
import resource.ResourceChecker;
import response.ResponseCode;
import response.ResponseService;

import java.io.*;
import java.net.Socket;
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
        request = new Request(client);
        try {
            request.parseAll();
        } catch (IOException e) {
            //TODO respond with error message
        }

        ResponseCode responseCode = ResponseCode.CODE200;

        ResourceChecker resourceChecker = new ResourceChecker(aliases, scriptAliases, documentRoot);
        responseCode = resourceChecker.checkUri(request);

        if (responseCode != ResponseCode.CODE200) {
            //TODO send response w/ error code
        }

        Authenticator authenticator = new Authenticator(request);
        responseCode = authenticator.checkAuthentication();

        if (responseCode != ResponseCode.CODE200) {
            //TODO send response w/ error code
        }

        // TODO call appropriate RequestService
    }
}