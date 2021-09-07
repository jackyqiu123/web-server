import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

public class Request {
    private RequestType requestType;
    private String uri;
    private String httpVersion;

    private InputStream inputStream;
    private Socket client;

    private Map headers;
    private byte[] body;

    public Request(RequestType requestType, String uri, String httpVersion, InputStream inputStream, Socket client, Map headers, byte[] body) {
        this.requestType = requestType;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.inputStream = inputStream;
        this.client = client;
        this.headers = headers;
        this.body = body;
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
