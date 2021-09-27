package response;

import logging.Logger;
import request.Request;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class GetResponseService extends ResponseService {

    public GetResponseService(Request request, Logger logger, Map<String, String> scriptAliases) {
        super(request, logger, scriptAliases);
    }

    public void sendResponse(){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
            String ifModifiedSince = "";
            if (request.getHeaders().containsKey("If-Modified-Since")) {
                ifModifiedSince = request.getHeaders().get("If-Modified-Since").toString();

                //TODO convert ifModifiedSince to Date
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy hh:mm:ss z");
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(ifModifiedSince, formatter);
            }

            if (isScript(uri)) {
                //TODO execute script
                Boolean isScript = true;
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
            }
            else{
                writer.write(this.notFoundResponse());
                writer.flush();
                writer.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
