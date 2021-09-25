package response;

import request.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.nio.file.Files;

public abstract class ResponseService {
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

    public ResponseService(Request request) {
        this.request = request;
        this.uri = request.getUri();
        this.requestType = request.getRequestType().toString();
        this.headers = request.getHeaders();
        this.file = new File(request.getUri());
        this.body = request.getBody();
        this.socket = request.getClient();

        if(isValidFile(file)){
            try {
                this.fileReader = new FileReader(this.uri);
            } catch (FileNotFoundException e) {
                //TODO appropriate error handling
            }
        }
    }

    public Boolean isValidFile(File file){
        if (file == null) {
            return false;
        }
        return file.exists() && !file.isDirectory();
    }

    public String noContentResponse(){
        StringBuilder response = new StringBuilder();
        Date date = new Date();
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 204;
        this.statusReason = "NO CONTENT";

        response.append(httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Date: " + date + "\r\n");
        response.append("Content-Length: " + contentLength + "\r\n");
        response.append("Content-Type: " + request.getMimeType() + "\r\n");
        response.append("Content-Location: " + this.uri + "\r\n");
        String responseBytes = response.toString();
        return responseBytes;
    }

    public String notFoundResponse(){
        StringBuilder response = new StringBuilder();
        Date date = new Date();
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 404;
        this.statusReason = "NOT FOUND";
        response.append(this.httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Date: " + date + "\r\n");
        response.append("Content-Type: " + request.getMimeType() + "\r\n");

        String responseBytes = response.toString();
        return responseBytes;
    }

    public String createdResponse(){
        StringBuilder response = new StringBuilder();
        Date date = new Date();
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 201;
        this.statusReason = "CREATED";
        response.append(this.httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Date: " + date + "\r\n");
        response.append("Content-Type: " + request.getMimeType() + "\r\n");
        response.append("Content-Location: " + this.uri + "\r\n");
        String responseString = response.toString();
        return responseString;
    }

    public String okResponse(){
        StringBuilder response = new StringBuilder();
        Date date = new Date();
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 200;
        this.statusReason = "OK";

        response.append(httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Date: " + date + "\r\n");
        response.append("Content-Length: " + contentLength + "\r\n");
        response.append("Content-Type: " + request.getMimeType() + "\r\n");
        response.append("Content-Location: " + this.uri + "\r\n");
        response.append("\r\n");
        String responseString = response.toString();
        return responseString;
    }

    public String badRequest(){
        StringBuilder response = new StringBuilder();
        String httpVersion = this.request.getHttpVersion();
        Date date = new Date();
        this.statusCode = 400;
        this.statusReason = "BAD REQUEST";
        response.append(httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Date: " + date + "\r\n");
        String responseString = response.toString();
        return responseString;
    }

    public String unauthorizedResponse(){
        StringBuilder response = new StringBuilder();
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 401;
        this.statusReason = "UNAUTHORIZED";
        Date date = new Date();
        response.append(httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Date: " + date + "\r\n");
        response.append("WWW-Authenticate: Basic \r\n"); // property can vary
        String responseString = response.toString();
        return responseString;
    }

    public String forbiddenResponse(){
        StringBuilder response = new StringBuilder();
        String httpVersion = this.request.getHttpVersion();
        Date date = new Date();
        this.statusCode = 403;
        this.statusReason = "FORBIDDEN";
        response.append(httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Date: " + date + "\r\n");
        String responseString = response.toString();
        return responseString;
    }

    public List<String> getFileContentsText() throws IOException {
        List<String> content = Files.readAllLines(file.toPath());

        int contentLengthCounter = 0;
        for (String line : content) {
            contentLengthCounter += line.length();
        }
        contentLength = contentLengthCounter;

        return content;
    }

    public byte[] getFileContentsBytes() throws IOException {
        byte[] content = Files.readAllBytes(file.toPath());

        contentLength = content.length;

        return content;
    }

    Request getRequest() {
        return request;
    }

    String getUri() {
        return uri;
    }

    int getStatusCode() {
        return statusCode;
    }

    String getStatusReason() {
        return statusReason;
    }

    String getRequestType() {
        return requestType;
    }

    Socket getSocket() {
        return socket;
    }

    Map<String, String> getHeaders() {
        return headers;
    }

    File getFile() {
        return file;
    }

    String getFileSize() {
        return fileSize;
    }

    String getHttpVersion() {
        return httpVersion;
    }

    byte[] getBody() {
        return body;
    }
}
