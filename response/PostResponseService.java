package response;

import logging.Logger;
import request.Request;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PostResponseService extends ResponseService {

    public PostResponseService(Request request, Logger logger) {
        super(request, logger);
    }

    public void sendResponse(){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
            if(isValidFile(file)){
                String byteString = new String(this.body, StandardCharsets.UTF_8);
                if(byteString == ""){
                    writer.write(this.okResponse());
                    writer.flush();
                    writer.close();
                }
                else{
                    writer.write(this.createdResponse());
                    writer.flush();
                    writer.close();
                }
            } else {

                writer.write(this.forbiddenResponse());
                writer.flush();
                writer.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
