package response;

import logging.Logger;
import request.Request;

import java.io.*;
import java.util.Map;

public class HeadResponseService extends ResponseService {

    public HeadResponseService(Request request, Logger logger, Map<String, String> scriptAliases) {
        super(request, logger, scriptAliases);
    }

    public void sendResponse(){ // basically the same response for Get but does not return file contents and body
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
            if (isValidFile(this.file)){
                writer.write(this.okResponse());
                writer.flush();
                writer.close();
            } else {
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
