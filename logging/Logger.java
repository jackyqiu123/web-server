package logging;

import java.io.BufferedWriter;
import java.io.File;
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
        result += "\r\n";

        File file = new File(logFileLocation);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("ERROR - while trying to create log file");
                e.printStackTrace();
            }
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(logFileLocation, true))) {
            writer.write(result);
        } catch (IOException e) {
            System.out.println("ERROR - while trying to write log into log file");
            e.printStackTrace();
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
