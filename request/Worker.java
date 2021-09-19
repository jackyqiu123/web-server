package request;

import authentication.Authenticator;
import logging.Logger;
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

    private Logger logger;

    public Worker(Socket client,
                  String documentRoot,
                  Map<String, String> aliases,
                  Map<String, String> scriptAliases,
                  String logFileLocation) {
        this.client = client;
        this.documentRoot = documentRoot;
        this.aliases = aliases;
        this.scriptAliases = scriptAliases;
        this.logger = new Logger(logFileLocation);
    }

    public void run() {
        ResponseCode responseCode = ResponseCode.CODE200;

        logger.setDate();

        request = new Request(client);

        try {
            responseCode = request.parseAll();
        } catch (IOException e) {
            //TODO respond with error message
            handleError(responseCode);
            System.out.println("ERROR - IO Exception");
            return;
        }

        if (responseCode != ResponseCode.CODE200) {
            //TODO send response w/ error code
            handleError(responseCode);
            System.out.println("ERROR - " + responseCode);
            return;
        }

        ResourceChecker resourceChecker = new ResourceChecker(aliases, scriptAliases, documentRoot);
        responseCode = resourceChecker.checkUri(request);

        if (responseCode != ResponseCode.CODE200) {
            //TODO send response w/ error code
            handleError(responseCode);
            System.out.println("ERROR - " + responseCode);
            return;
        }

        Authenticator authenticator = new Authenticator(request);
        responseCode = authenticator.checkAuthentication();

        if (responseCode != ResponseCode.CODE200) {
            //TODO send response w/ error code
            handleError(responseCode);
            System.out.println("ERROR - " + responseCode);
            return;
        }

        // TODO call appropriate RequestService
    }

    private void handleError(ResponseCode responseCode) {
        //TODO: implement
    }

}