package response;

import logging.Logger;
import request.Request;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GetResponseService extends ResponseService {

    public GetResponseService(Request request, Logger logger, Map<String, String> scriptAliases) {
        super(request, logger, scriptAliases);
    }

    public void sendResponse(){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            ZonedDateTime ifModifiedSince = null;
            if (request.getHeaders().containsKey("If-Modified-Since")) {
                String ifModifiedSinceString = request.getHeaders().get("If-Modified-Since").toString();
                ifModifiedSince = ZonedDateTime.now().parse(ifModifiedSinceString, formatter);
            }

            if (isValidFile(file) && ifModifiedSince != null) {
                LocalDateTime fileTime = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault()
                );

                Instant fileTimeInstant = fileTime.toInstant(ZoneOffset.UTC);

                ZoneId zoneId = ZoneId.of( "America/Los_Angeles" );
                ZonedDateTime fileLastModified = ZonedDateTime.ofInstant( fileTimeInstant , zoneId );


                long difference = ifModifiedSince.compareTo(fileLastModified);

                if(difference > 0) {
                    writer.write(notModifiedResponse());
                    writer.flush();
                    writer.close();
                    return;
                }
            }

            if (isScript(uri)) {
                ScriptService scriptService = new ScriptService(request, uri);
                scriptService.runScript(writer, body.toString());
                return;
            }

            if(isValidFile(this.file)){
                //TODO compare ifModifiedSince to file last modified date
                //TODO if true -> return 304 response


                if (request.getMimeType().contains("text")) {
                    List<String> body = getFileContentsText();
                    writer.write(this.okResponse());
                    for (String line : body) {
                        writer.write(line);
                    }
                } else {
                    String body = getFileContentsBytes();
                    writer.write(this.okResponse());

                    //TODO fix image
                    writer.write(body);
                    writer.flush();
                }

                //TODO do we need this??
                //writer.write(this.body.toString()); // server return the body content of the request

                writer.flush();
                writer.close();
            } else{
                writer.write(notFoundResponse());
                writer.flush();
                writer.close();
            }
        } catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
