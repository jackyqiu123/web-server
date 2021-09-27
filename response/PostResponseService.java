package response;

import logging.Logger;
import request.Request;

import java.io.*;

public class PostResponseService extends ResponseService {

    public PostResponseService(Request request, Logger logger) {
        super(request, logger);
    }

    public void sendResponse(){

        //TODO do the scripting stuff

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
            if(!file.exists()){ // file does not exist and will create it
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

                if(!appendContentToFile(file, request.getBody())) {
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
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
