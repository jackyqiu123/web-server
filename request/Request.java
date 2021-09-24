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
    private MimeTypes mimes;

    private Map<String, String> headers;
    private byte[] body = new byte[0];

    public Request(Socket client, Logger logger, MimeTypes mimes) {
        this.client = client;
        this.logger = logger;

        headers = new HashMap<>();
    }

    public ResponseCode parseAll() throws IOException {
        InputStream inputStream = client.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String inputLine;

        Boolean isFirstLine = true;
        Boolean hasBody = false;
        Boolean inBody = false;

        while ((inputLine = in.readLine()) != null) {

            System.out.println(inputLine);

            if (inBody) {
                if (inputLine.equals("")) {
                    break;
                }
                readInBody(inputLine);
                continue;
            }

            if (inputLine.equals("") && hasBody) {
                inBody = true;
                continue;
            } else if (inputLine.equals("")) {
                break;
            }

            if (isFirstLine) {
                logger.setRequestLine(inputLine);
                parseRequestLine(inputLine);
                isFirstLine = false;
            } else {
                parseHeaders(inputLine);
            }

            if (inputLine.contains("Content-Length") && headers.get("Content-Length") != "0") {
                hasBody = true;
            }
        }

        if (requestType == null || uri == null || httpVersion == null) {
            return ResponseCode.CODE500;
        } else {
            return ResponseCode.CODE200;
        }
    }

    private void parseRequestLine(String line) throws IOException {
        if (line == null) {
            throw new IOException("bad request");
        }
        String[] tokens = line.split(" ");
        if (tokens.length != 3) {
            throw new IOException("bad request");
        }
        String inputRequestType = tokens[0];
        switch (inputRequestType) {
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

    private void parseHeaders(String line) throws IOException {
        String[] tokens = line.split(":", 2);
        if (tokens.length != 2) {
            throw new IOException("header exception");
        } else {
            this.headers.put(tokens[0], tokens[1].trim());
        }
    }

    private void readInBody(String line) {
        byte[] inputLine = line.getBytes();

        byte[] newBytes = new byte[body.length + inputLine.length];
        System.arraycopy(body, 0, newBytes, 0, body.length);
        System.arraycopy(inputLine, 0, newBytes, body.length, inputLine.length);

        body = newBytes;
    }

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

    public byte[] getBody() {
        return body;
    }

    public String getMimeType() {
        Map<String, String> mimeTypes = mimes.getMimeTypes();
        String uriExtension = uri.split("\\.")[1];
        if (mimeTypes.containsKey(uriExtension)) {
            return mimeTypes.get(uriExtension);
        } else {
            return "text/text";
        }
    }
}
