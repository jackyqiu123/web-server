package request;

import authentication.Authenticator;
import logging.Logger;
import resource.ResourceChecker;
import response.ResponseCode;
import response.ResponseHandler;
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
    private MimeTypes mimes;

    private Logger logger;

    public Worker(Socket client,
                  String documentRoot,
                  Map<String, String> aliases,
                  Map<String, String> scriptAliases,
                  String logFileLocation,
                  MimeTypes mimes) {
        this.client = client;
        this.documentRoot = documentRoot;
        this.aliases = aliases;
        this.scriptAliases = scriptAliases;
        this.logger = new Logger(logFileLocation);
        this.mimes = mimes;
    }

    public void run() {
        ResponseCode responseCode = ResponseCode.CODE200;

        logger.setDate();

        request = new Request(client, logger, mimes);

        try {
            responseCode = request.parseAll();
        } catch (IOException e) {
            handleError(responseCode);
            return;
        }

        if (responseCode != ResponseCode.CODE200) {
            handleError(responseCode);
            return;
        }

        ResourceChecker resourceChecker = new ResourceChecker(aliases, scriptAliases, documentRoot);
        responseCode = resourceChecker.checkUri(request);

        if (responseCode != ResponseCode.CODE200) {
            handleError(responseCode);
            return;
        }

        Authenticator authenticator = new Authenticator(request, logger);
        responseCode = authenticator.checkAuthentication();

        if (responseCode != ResponseCode.CODE200) {
            handleError(responseCode);
            return;
        }

        ResponseHandler responseHandler = new ResponseHandler();
        responseHandler.sendResponse(request, logger);

        logger.logStatus();
    }

    private void handleError(ResponseCode responseCode) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))){
            ResponseService responseService = new ResponseService(request, logger);

            switch (responseCode) {
                case CODE400:
                    writer.write(responseService.badRequest());
                    break;
                case CODE401:
                    writer.write(responseService.unauthorizedResponse());
                    break;
                case CODE403:
                    writer.write(responseService.forbiddenResponse());
                    break;
                case CODE404:
                    writer.write(responseService.notFoundResponse());
                    break;
                case CODE500:
                    writer.write(responseService.internalServerError());
                    break;
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}