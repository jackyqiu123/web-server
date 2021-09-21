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
    //private String mime;
    private String fileSize;
    private String httpVersion;
    

    public Boolean isValidFile(Request request, File file){
        if((this.httpdConf.getAliasMap().get("/ab/").isEmpty() || this.httpdConf.getAliasMap().get("/ab/") == null)&& 
        this.httpdConf.getAliasMap().get("/ab/").isEmpty() || this.httpdConf.getAliasMap().get("/~traciely/") == null){
            return false;
        }
        if(file.exists() && !file.isDirectory()){
            return true;
        }
        return true;
    }
    public byte[] noContentResponse(){
        StringBuilder response = new StringBuilder();
        String mime = this.headers.get("Content-Type");
        String fileSize = this.headers.get("Content-Length");
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 204;
        this.statusReason = "NO CONTENT";

        response.append(httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Content-Length: " + this.fileSize + "\r\n");
        response.append("Content-Type: " + mime + "\r\n");
        byte[] responseBytes = response.toString().getBytes();
        return responseBytes;
    }
    public byte[] notFoundResponse(){
        StringBuilder response = new StringBuilder();
        String mime = this.headers.get("Content-Type");
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 404;
        this.statusReason = "NOT FOUND";
        response.append(this.httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Content-Type: " + mime + "\r\n");

        byte[] responseBytes = response.toString().getBytes();
        return responseBytes;
    }

    public byte[] createdResponse(){
        StringBuilder response = new StringBuilder();
        String mime = this.headers.get("Content-Type");
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 201;
        this.statusReason = "CREATED";
        response.append(this.httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Content-Type: " + mime + "\r\n");
        response.append("Content-Location: " + this.uri);
        //TODO changed this.url to this.uri ???
        response.append("Content-Location: " + this.uri);
        //TODO add return to compile
        byte[] responseBytes = response.toString().getBytes();
        return responseBytes;
    }

    public byte[] okResponse(){
        StringBuilder response = new StringBuilder();
        String mime = this.headers.get("Content-Type");
        String fileSize = this.headers.get("Content-Length");
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 200;
        this.statusReason = "OK";

        response.append(httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Content-Length: " + this.fileSize + "\r\n");
        response.append("Content-Type: " + this.mime + "\r\n");
        byte[] responseBytes = response.toString().getBytes();
        return responseBytes;
    }
    public byte[] badRequest(){
        StringBuilder response = new StringBuilder();
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 400;
        this.statusReason = "BAD REQUEST";
        response.append(httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        byte[] responseBytes = response.toString().getBytes();
        return responseBytes;

    }
    public byte[] unauthorizedResponse(){
        StringBuilder response = new StringBuilder();
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 401;
        this.statusReason = "UNAUTHORIZED";
        Date date = new Date();
        response.append(httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Date: " + date );
        response.append("WWW-Authenticate: Basic \r\n"); // property can vary
        byte[] responseBytes = response.toString().getBytes();
        return responseBytes;
    }
    public byte[] forbiddenResponse(){
        StringBuilder response = new StringBuilder();
        String httpVersion = this.request.getHttpVersion();
        Date date = new Date();
        this.statusCode = 403;
        this.statusReason = "FORBIDDEN";
        response.append(httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Date: " + date );
        //TODO add return to compile
        byte[] responseBytes = response.toString().getBytes();
        return responseBytes;
    }
    public byte[] getFileContents() throws IOException {
        byte[] contents = Files.readAllBytes(this.file.toPath());
        return contents;
    }
}
