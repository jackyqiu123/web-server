package request;

import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class Request {
    private RequestType requestType;
    private String uri;
    private String httpVersion;

    private Socket client;

    private Map<String, String> headers;
    private byte[] body;

    public Request(Socket client) {
        this.client = client;

        headers = new HashMap<>();
    }


    public void parseAll()throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String inputLine;

        Boolean isFirstLine = true;

        while (!(inputLine = in.readLine()).equals("")) {
            if (isFirstLine) {
                parseRequestline(inputLine);
                isFirstLine = false;
            } else {
                parseHeaders(inputLine);
            }
        }
        in.close();



//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream(),
//                Charset.forName(StandardCharsets.UTF_8.name())));
//
//        String currentLine = bufferedReader.readLine();
//        parseRequestline(currentLine);
//        currentLine = bufferedReader.readLine();
//
//        while(!currentLine.equals("")){
//            parseHeaders(currentLine);
//            if(headers.get("Content-Length") != null || headers.get("Content-Length") != "0"){
//                parseBody(currentLine);
//            }
//            currentLine = bufferedReader.readLine();
//        }
    }

   private void parseRequestline(String line) throws IOException{
        if(line == null){
            throw new IOException("bad request");
        }
        String [] tokens = line.split(" ");
        if(tokens.length != 3){
            throw new IOException("bad request");
        }
        String inputRequestType = tokens[0];
        switch(inputRequestType){
            case "GET":
                requestType = RequestType.GET;
                break;
            case "POST":
                requestType = RequestType.POST;
                break;
            case "HEAD":
                requestType = RequestType.HEAD;
                break;
            case "PUT":
                requestType = RequestType.PUT;
                break;
            case "DELETE":
                requestType = RequestType.DELETE;
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
        InputStream inputStream = client.getInputStream();
        int contentSize = Integer.parseInt(headers.get("Content-Length"));
        this.body = new byte[contentSize];
        inputStream.read(this.body, 0, contentSize);
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
