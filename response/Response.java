package response;

import request.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.nio.file.Files;

public abstract class Response {  
    private Request request;
    private HttpdConf httpdConf;
    private String uri;
    private FileReader fileReader;
    private int statusCode;
    private String statusReason;
    private String requestType;
    private Socket socket;
    private Map<String, String> headers;
    private File file;

    private String fileSize;
    private String httpVersion;
    

    public Boolean isValidFile(File file){
        return file.exists() && !file.isDirectory();
    }

    public String noContentResponse(){
        StringBuilder response = new StringBuilder();
        // String mime = this.headers.get("Content-Type");
        //TODO commented out to compile
//        String mime = this.getUriFileExtension();
        Date date = new Date();
        String fileSize = this.headers.get("Content-Length");
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 204;
        this.statusReason = "NO CONTENT";

        response.append(httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Date: " + date );
        response.append("Content-Length: " + this.fileSize + "\r\n");
        //TODO commented out to compile
//        response.append("Content-Type: " + mime + "\r\n");
        response.append("Content-Location: " + this.uri);
        String responseBytes = response.toString();
        return responseBytes;
    }
    public String notFoundResponse(){
        StringBuilder response = new StringBuilder();
        // String mime = this.headers.get("Content-Type");
        //TODO commented out to compile
//        String mime = this.getUriFileExtension();
        Date date = new Date();
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 404;
        this.statusReason = "NOT FOUND";
        response.append(this.httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Date: " + date );
        //TODO commented out to compile
//        response.append("Content-Type: " + mime + "\r\n");

        String responseBytes = response.toString();
        return responseBytes;
    }

    public String createdResponse(){
        StringBuilder response = new StringBuilder();
        // String mime = this.headers.get("Content-Type");
        //TODO commented out to compile
//        String mime = this.getUriFileExtension();
        Date date = new Date();
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 201;
        this.statusReason = "CREATED";
        response.append(this.httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Date: " + date );
        //TODO commented out to compile
//        response.append("Content-Type: " + mime + "\r\n");
        response.append("Content-Location: " + this.uri);
        //TODO changed this.url to this.uri ???
        response.append("Content-Location: " + this.uri);
        //TODO add return to compile
        String responseString = response.toString();
        return responseString;
    }

    public String okResponse(){
        StringBuilder response = new StringBuilder();
        Date date = new Date();
        // String mime = this.headers.get("Content-Type");
        //TODO commented out to compile
//        String mime = this.getUriFileExtension();
        String fileSize = this.headers.get("Content-Length");
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 200;
        this.statusReason = "OK";

        response.append(httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Date: " + date );
        response.append("Content-Length: " + this.fileSize + "\r\n");
        //TODO commented out to compile
//        response.append("Content-Type: " + this.mime + "\r\n");
        response.append("Content-Location: " + this.uri);
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
        response.append("Date: " + date );
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
        response.append("Date: " + date );
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
        response.append("Date: " + date );
        //TODO add return to compile
        String responseString = response.toString();
        return responseString;
    }
    public String getFileContents() throws IOException {
        byte[] contents = Files.readAllBytes(this.file.toPath());
        String contentString = contents.toString();
        return contentString;
    }
}
