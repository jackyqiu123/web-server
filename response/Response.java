package response;

import request.*;
import java.io.*;
import java.net.Socket;
import java.util.Map;

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
    private String mime;
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
    public byte[] NoContentResponse(){
        StringBuilder response = new StringBuilder();
        String mime = this.headers.get("Content-Type");
        String fileSize = this.headers.get("Content-Length");
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 204;
        this.statusReason = "NO CONTENT";

        response.append(httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Content-Length: " + this.fileSize + "\r\n");
        response.append("Content-Type: " + this.mime + "\r\n");
        byte[] responseBytes = response.toString().getBytes();
        return responseBytes;
    }
    public byte[] NotFoundResponse(){
        StringBuilder response = new StringBuilder();
        String httpVersion = this.request.getHttpVersion();
        this.statusCode = 404;
        this.statusReason = "NOT FOUND";
        response.append(this.httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
        response.append("Content-Type: " + this.mime + "\r\n");

        byte[] responseBytes = response.toString().getBytes();
        return responseBytes;
    }
}
