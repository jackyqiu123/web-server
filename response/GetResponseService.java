package response;

import logging.Logger;
import request.Request;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class GetResponseService extends ResponseService {

    public GetResponseService(Request request, Logger logger) {
        super(request, logger);
    }

    public void sendResponse(){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
            if(isValidFile(this.file)){
                if (request.getMimeType().contains("text")) {
                    List<String> body = getFileContentsText();
                    writer.write(this.okResponse());
                    for (String line : body) {
                        writer.write(line);
                    }
                } else {
                    byte[] body = getFileContentsBytes();
                    writer.write(this.okResponse());

                    //TODO fix image
                    DataOutputStream byteWriter = new DataOutputStream(socket.getOutputStream());
                    byteWriter.write(body);
                    byteWriter.flush();
                }

                //TODO do we need this??
                writer.write(this.body.toString()); // server return the body content of the request

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
