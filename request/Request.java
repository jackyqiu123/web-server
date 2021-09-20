package request;

import logging.Logger;
import response.ResponseCode;

import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class Request {
    private RequestType requestType;
    private String uri;
    private String httpVersion;

    private Socket client;
    private Logger logger;

    private Map<String, String> headers;
    //private byte[] body;
    private String body = "";

    public Request(Socket client, Logger logger) {
        this.client = client;
        this.logger = logger;

        headers = new HashMap<>();
    }


    public ResponseCode parseAll() throws IOException{
        InputStream inputStream = client.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String inputLine;

        Boolean isFirstLine = true;
        Boolean hasBody = false;
        Boolean inBody = false;

        while ((inputLine = in.readLine()) != null) {

            System.out.println(inputLine);

            if (inBody) {
                //parseBody(inputStream);
                if (inputLine.equals("")) {
                    break;
                }
                readInBody(inputLine);
                continue;
            }

            if (inputLine.equals("") && hasBody && !inBody) {
                inBody  = true;
                continue;
            } else if (inputLine.equals("")) {
                break;
            }

//            if(headers.get("Content-Length") != null && headers.get("Content-Length") != "0"){
//                parseBody(inputStream);
//            }

            if (isFirstLine) {
                logger.setRequestLine(inputLine);
                parseRequestline(inputLine);
                isFirstLine = false;
            } else {
                parseHeaders(inputLine);
            }

            if(inputLine.contains("Content-Length") && headers.get("Content-Length") != "0"){
                hasBody = true;
            }
        }
        in.close();


        if (requestType == null || uri == null || httpVersion == null) {
            return ResponseCode.CODE500;
        } else {
            return ResponseCode.CODE200;
        }



        //TODO TODO TODO -^


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

    private void readInBody(String line) {
        body += line;
    }


//    private void parseBody(InputStream inputStream)throws IOException{
//        int contentSize = Integer.parseInt(headers.get("Content-Length"));
//        this.body = new byte[contentSize];
//        inputStream.read(this.body, 0, contentSize);
//    }

    public RequestType getRequestType() {
        return requestType;
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

    public Socket getClient() {
        return client;
    }

    public Map getHeaders() {
        return headers;
    }

//    public byte[] getBody() {
//        return body;
//    }

    public String getBody() {
        return body;
    }
}
