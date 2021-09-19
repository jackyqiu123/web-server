package logging;

public class Logger {
    private String logFileLocation;

    public Logger(String logFileLocation) {
        this.logFileLocation = logFileLocation;
    }

    public Boolean log() {
        //TODO implement
        return false;
    }

}



// LogFormat "%h %l %u %t \"%r\" %>s %b" common
//      127.0.0.1 (%h) -> IP address from host
//      - (%l) -> hyphen indicates that request is not available
//      frank (%u) -> userid of the person requesting the document as determined by HTTP authentication
//      [10/Oct/2000:13:55:36 -0700] (%t) -> time request was received
//      "GET /apache_pb.gif HTTP/1.0" (\"%r\") -> request line from the client is given in double quotes
//      200 (%>s) -> status code that the server sends back to the client
//      2326 (%b) -> size of the object returned to the client
// 127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /apache_pb.gif HTTP/1.0" 200 2326