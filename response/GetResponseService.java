package response;

import logging.Logger;
import request.Request;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GetResponseService extends ResponseService {

    public GetResponseService(Request request, Logger logger, Map<String, String> scriptAliases) {
        super(request, logger, scriptAliases);
    }

    public void sendResponse(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        ZonedDateTime ifModifiedSince = null;

        if (request.getHeaders().containsKey("If-Modified-Since")) {
            String ifModifiedSinceString = request.getHeaders().get("If-Modified-Since").toString();
            try {
                ifModifiedSince = ZonedDateTime.now().parse(ifModifiedSinceString, formatter);
            } catch (DateTimeParseException e) {
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                    writer.write(badRequest());
                    writer.flush();
                    writer.close();
                    return;
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

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
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                    writer.write(notModifiedResponse());
                    writer.flush();
                    writer.close();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (isScript(uri)) {
            //TODO send bytes?
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                ScriptService scriptService = new ScriptService(request, uri);
                scriptService.runScript(writer, body.toString());
                return;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(isValidFile(this.file)){
            if (request.getMimeType().contains("text")) {
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                    List<String> body = getFileContentsText();
                    writer.write(this.okResponse());
                    for (String line : body) {
                        writer.write(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try (DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {
                    byte[] body = getFileContentsBytes();
                    dataOutputStream.write(this.okResponse().getBytes(StandardCharsets.UTF_8));
                    dataOutputStream.write(body);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //TODO DELETE THIS?
            //writer.write(this.body.toString()); // server return the body content of the request

        } else{
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                writer.write(notFoundResponse());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
