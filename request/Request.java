package request;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;
import java.io.*;

public class Request {
    private RequestType requestType;
    private String uri;
    private String httpVersion;

    private InputStream inputStream;
    private Socket client;

    private Map<String, String> headers;
    private byte[] body;

//    public Request(RequestType requestType, String uri, String httpVersion, InputStream inputStream, Socket client, Map headers, byte[] body) {
//        this.requestType = requestType;
//        this.uri = uri;
//        this.httpVersion = httpVersion;
//        this.inputStream = inputStream;
//        this.client = client;
//        this.headers = headers;
//        this.body = body;
//    }

    public Request(Socket client) {
        this.client = client;
    }


    public void parseAll()throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String currLine = bufferedReader.readLine();
        while(!currLine.isEmpty()){
            parseHeaders(currLine);
            if(headers.get("Content-Length") != null || headers.get("Content-Length") != "0"){
                parseBody(currLine);
            }
            currLine = bufferedReader.readLine();
        }
    }
   private void parseRequestline(String line) throws IOException{
        if(line == null){
            throw new IOException("bad request");
        }
        String [] tokens = line.split(" ");
        if(tokens.length != 3){
            throw new IOException("bad request");
        }
        String requestType = tokens[0];
        switch(requestType){
            case "GET":
                break;
            case "POST":
                break;
            case "HEAD":
                break;
            case "PUT":
                break;
            case "DELETE":
                break;
            default:
                throw new IOException("bad request");
        }
        this.uri = tokens[1];
        this.httpVersion = tokens[2];
    }
    private void parseHeaders(String line) throws IOException{
        String [] tokens = line.split(":", 2);
        if(tokens.length != 2){
            throw new IOException("header exception");
        }
        else{
            this.headers.put(tokens[0], tokens[1].trim());
        }
    }
    private void parseBody(String line)throws IOException{
        int contentSize = Integer.parseInt(headers.get("Content-Length"));
        this.body = new byte[contentSize];
        this.inputStream.read(this.body, 0, contentSize);
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public Map getHeaders() {
        return headers;
    }

    public void setHeaders(Map headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
