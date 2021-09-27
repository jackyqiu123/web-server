package response;

import logging.Logger;
import request.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.nio.file.Files;

public class ResponseService {
    protected final Logger logger;
    protected final Request request;
    protected final String uri;
    protected FileReader fileReader;
    protected int statusCode;
    protected String statusReason;
    protected final String requestType;
    protected final Socket socket;
    protected final Map<String, String> headers;
    protected final File file;
    protected String fileSize;
    protected String httpVersion;
    protected final byte[] body;
    protected int contentLength = 0;
    protected Map<String, String> scriptAliases;

    public ResponseService(Request request, Logger logger, Map<String, String> scriptAliases) {
        this.request = request;
        this.uri = request.getUri();
        this.requestType = request.getRequestType().toString();
        this.headers = request.getHeaders();
        this.file = new File(request.getUri());
        this.body = request.getBody();
        this.socket = request.getClient();
        this.logger = logger;
        this.scriptAliases = scriptAliases;

        if(isValidFile(file)){
            try {
                this.fileReader = new FileReader(this.uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean isValidFile(File file){
        if (file == null) {
            return false;
        }
        return file.exists() && !file.isDirectory();
    }

    public String okResponse(){
        StringBuilder response = new StringBuilder();
        Date date = new Date();
        String httpVersion = request.getHttpVersion();
        statusCode = 200;
        statusReason = "OK";

        logger.setStatusCode(String.valueOf(statusCode));

        response.append(httpVersion + " " + statusCode + " " + statusReason + "\r\n");
        response.append("Date: " + date + "\r\n");
        response.append("Content-Length: " + contentLength + "\r\n");
        response.append("Content-Type: " + request.getMimeType() + "\r\n");
        response.append("Content-Location: " + this.uri + "\r\n");
        response.append("\r\n");
        String responseString = response.toString();
        return responseString;
    }

    public String createdResponse(){
        StringBuilder response = new StringBuilder();
        Date date = new Date();
        String httpVersion = request.getHttpVersion();
        statusCode = 201;
        statusReason = "CREATED";

        logger.setStatusCode(String.valueOf(statusCode));

        response.append(httpVersion + " " + statusCode + " " + statusReason + "\r\n");
        response.append("Date: " + date + "\r\n");
        response.append("Content-Type: " + request.getMimeType() + "\r\n");
        response.append("Content-Location: " + this.uri + "\r\n");
        String responseString = response.toString();
        return responseString;
    }

    public String noContentResponse(){
        StringBuilder response = new StringBuilder();
        Date date = new Date();
        String httpVersion = this.request.getHttpVersion();
        statusCode = 204;
        statusReason = "NO CONTENT";

        logger.setStatusCode(String.valueOf(statusCode));

        response.append(httpVersion + " " + statusCode + " " + statusReason + "\r\n");
        response.append("Date: " + date + "\r\n");
        response.append("Content-Length: " + contentLength + "\r\n");
        response.append("Content-Type: " + request.getMimeType() + "\r\n");
        response.append("Content-Location: " + uri + "\r\n");
        String responseBytes = response.toString();
        return responseBytes;
    }

    public String badRequest(){
        StringBuilder response = new StringBuilder();
        String httpVersion = request.getHttpVersion();
        Date date = new Date();
        statusCode = 400;
        statusReason = "BAD REQUEST";

        logger.setStatusCode(String.valueOf(statusCode));

        response.append(httpVersion + " " + statusCode + " " + statusReason + "\r\n");
        response.append("Date: " + date + "\r\n");
        String responseString = response.toString();
        return responseString;
    }

    public String unauthorizedResponse(){
        StringBuilder response = new StringBuilder();
        String httpVersion = request.getHttpVersion();
        statusCode = 401;
        statusReason = "UNAUTHORIZED";

        logger.setStatusCode(String.valueOf(statusCode));

        Date date = new Date();
        response.append(httpVersion + " " + statusCode + " " + statusReason + "\r\n");
        response.append("Date: " + date + "\r\n");
        response.append("WWW-Authenticate: Basic \r\n"); // property can vary
        String responseString = response.toString();
        return responseString;
    }

    public String forbiddenResponse(){
        StringBuilder response = new StringBuilder();
        String httpVersion = request.getHttpVersion();
        Date date = new Date();
        statusCode = 403;
        statusReason = "FORBIDDEN";

        logger.setStatusCode(String.valueOf(statusCode));

        response.append(httpVersion + " " + statusCode + " " + statusReason + "\r\n");
        response.append("Date: " + date + "\r\n");
        String responseString = response.toString();
        return responseString;
    }

    public String notFoundResponse(){
        StringBuilder response = new StringBuilder();
        Date date = new Date();
        String httpVersion = request.getHttpVersion();
        statusCode = 404;
        statusReason = "NOT FOUND";

        logger.setStatusCode(String.valueOf(statusCode));

        response.append(httpVersion + " " + statusCode + " " + statusReason + "\r\n");
        response.append("Date: " + date + "\r\n");
        response.append("Content-Type: " + request.getMimeType() + "\r\n");

        String responseBytes = response.toString();
        return responseBytes;
    }

    public String internalServerError(){
        StringBuilder response = new StringBuilder();
        String httpVersion = request.getHttpVersion();
        Date date = new Date();
        statusCode = 500;
        statusReason = "INTERNAL SERVER ERROR";

        logger.setStatusCode(String.valueOf(statusCode));

        response.append(httpVersion + " " + statusCode + " " + statusReason + "\r\n");
        response.append("Date: " + date + "\r\n");
        String responseString = response.toString();
        return responseString;
    }

    public List<String> getFileContentsText() throws IOException {
        List<String> content = Files.readAllLines(file.toPath(), StandardCharsets.US_ASCII);

        int contentLengthCounter = 0;
        for (String line : content) {
            contentLengthCounter += line.length();
        }
        contentLength = contentLengthCounter;
        logger.setSizeOfObjectReturned(String.valueOf(contentLength));

        return content;
    }

    public String getFileContentsBytes() throws IOException {
        byte[] content = Files.readAllBytes(file.toPath());

        contentLength = content.length;
        logger.setSizeOfObjectReturned(String.valueOf(contentLength));

        return content.toString();
    }

    public Boolean writeContentToFile(File file, byte[] content) {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean appendContentToFile(File file, byte[] content) {
        try (FileOutputStream outputStream = new FileOutputStream(file, true)) {
            outputStream.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean isScript(String uri) {
        for (String scriptAlias : scriptAliases.values()) {
            if (uri.contains(scriptAlias)) {
                return true;
            }
        }
        return false;
    }
}
