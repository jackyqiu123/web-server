package response;

import logging.Logger;
import request.Request;

import java.io.*;
import java.util.Map;

public class PutResponseService extends ResponseService {

    public PutResponseService(Request request, Logger logger, Map<String, String> scriptAliases) {
        super(request, logger, scriptAliases);
    }

    public void sendResponse(){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
            if(!file.exists()){ // file does not exist and will create it

                if (isScript(uri)) {
                    ScriptService scriptService = new ScriptService(request, uri);
                    scriptService.runScript(writer, body.toString());
                    return;
                }

                if(file.createNewFile()){

                    if(!writeContentToFile(file, request.getBody())) {
                        writer.write(internalServerError());
                        writer.flush();
                        writer.close();
                        return;
                    }

                    writer.write(createdResponse());
                    writer.flush();
                    writer.close();
                } 
                else{ // unsuccessful in creating new file
                    writer.write(badRequest());
                    writer.flush();
                    writer.close();
                }
            } else { // file already exist

                if(!writeContentToFile(file, request.getBody())) {
                    writer.write(internalServerError());
                    writer.flush();
                    writer.close();
                    return;
                }

                writer.write(okResponse());
                writer.flush();
                writer.close();
            }
        }
        catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
