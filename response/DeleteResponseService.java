package response;

import logging.Logger;
import request.Request;

import java.io.*;

public class DeleteResponseService extends ResponseService {
    // possible responses are 202(Accepted), 204(No Content), 200(OK) for Delete request

    public DeleteResponseService(Request request, Logger logger) {
        super(request, logger);
    }

    public void sendResponse(){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.getSocket().getOutputStream()))) {
            int fileSize = body.toString().length();
            String httpVersion = request.getHttpVersion();

            if(isValidFile(file)){
                if(fileSize == 0) {
                    writer.write(this.noContentResponse()); // note: writing out bytes, can be converted into a string
                    getFile().delete();
                    writer.flush();
                    writer.close();
                } else {
                    writer.write(this.okResponse()); // note: writing out bytes, can be converted into a string
                    getFile().delete();
                    writer.flush();
                    writer.close();
                }


//                 this.statusCode = 200;
//                 this.statusReason = "NO CONTENT";
//                 writer.write(httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
//                 writer.write("Content-Length: " + fileSize + "\r\n");
//                 writer.write("Content-Type: " + request.getMimeType() + "\r\n");
//
//                writer.write(this.noContentResponse()); // note: writing out bytes, can be converted into a string
//                getFile().delete();
//                writer.flush();
//                writer.close();



            } else { // send 404 Not Found
                 this.statusCode = 404;
                 this.statusReason = "NOT FOUND";
                 writer.write(this.httpVersion + " " + this.statusCode + " " + this.statusReason + "\r\n");
                 writer.write("Content-Type: " + request.getMimeType() + "\r\n");

                writer.write(this.notFoundResponse());
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
