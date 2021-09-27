package response;

import logging.Logger;
import request.Request;

import java.io.*;

public class DeleteResponseService extends ResponseService {

    public DeleteResponseService(Request request, Logger logger) {
        super(request, logger);
    }

    public void sendResponse(){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.getSocket().getOutputStream()))) {
            int fileSize = body.toString().length();

            if(isValidFile(file)){
                if(fileSize == 0) {
                    writer.write(noContentResponse()); // note: writing out bytes, can be converted into a string
                    getFile().delete();
                    writer.flush();
                    writer.close();
                } else {
                    writer.write(okResponse()); // note: writing out bytes, can be converted into a string
                    getFile().delete();
                    writer.flush();
                    writer.close();
                }
            } else { // send 404 Not Found
                writer.write(this.notFoundResponse());
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
