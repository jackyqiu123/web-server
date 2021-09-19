package logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;


public class Logger {
    private String logFileLocation;

    private String hostIpAddress = "-";
    private String rfc1413Id = "-";
    private String userId = "-";
    private String date = "-";
    private String requestLine = "-";
    private String statusCode = "-";
    private String sizeOfObjectReturned = "-";


    public Logger(String logFileLocation) {
        this.logFileLocation = logFileLocation;
    }

    public void logStatus() {
        String result = "";
        result += hostIpAddress;
        result += " " + rfc1413Id;
        result += " " + userId;
        result += " " + date;
        result += " " + requestLine;
        result += " " + statusCode;
        result += " " + sizeOfObjectReturned;

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(logFileLocation))) {
            writer.write(result);
        } catch (IOException e) {
            System.out.println("ERROR - while trying to write log into log file");
        }

        System.out.println(result);
    }

    public void setHostIpAddress(String hostIpAddress) {
        this.hostIpAddress = hostIpAddress;
    }

    public void setRfc1413Id(String rfc1413Id) {
        this.rfc1413Id = rfc1413Id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDate() {
        //[day/month/year:hour:minute:second zone]
        String timeZone = TimeZone.getDefault().toString();

        DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss");
        Date date = new Date();

        LocalDateTime now = LocalDateTime.now();
        ZoneId zone = ZoneId.of("America/Los_Angeles");
        ZoneOffset zoneOffSet = zone.getRules().getOffset(now);

        this.date = dateFormat.format(date) + " " + zoneOffSet.toString().replace(":", "");
    }

    public void setRequestLine(String requestLine) {
        this.requestLine = requestLine;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setSizeOfObjectReturned(String sizeOfObjectReturned) {
        this.sizeOfObjectReturned = sizeOfObjectReturned;
    }
}



// LogFormat "%h %l %u %t \"%r\" %>s %b" common
//      127.0.0.1 (%h) -> IP address from host
//      - (%l) -> (hyphen indicates that request is not available) RFC 1413 identity of the client determined by identd on the clients machine
//      ✅frank (%u) -> userid of the person requesting the document as determined by HTTP authentication
//      ✅[10/Oct/2000:13:55:36 -0700] (%t) -> time request was received
//      ✅"GET /apache_pb.gif HTTP/1.0" (\"%r\") -> request line from the client is given in double quotes
//      200 (%>s) -> status code that the server sends back to the client
//      2326 (%b) -> size of the object returned to the client

// 127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /apache_pb.gif HTTP/1.0" 200 2326