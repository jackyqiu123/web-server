package response;

import request.Request;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class GetResponseService extends ResponseService {

    public GetResponseService(Request request) {
        super(request);
    }

    public void sendResponse(){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()))){
            if(isValidFile(this.file)){

                String testMime = request.getMimeType();
                String testUri = request.getUri();

                if (request.getMimeType().contains("text")) {
                    List<String> body = getFileContentsText();
                    writer.write(this.okResponse());
                    for (String line : body) {
                        writer.write(line);
                    }
                } else {
                    byte[] body = getFileContentsBytes();
                    writer.write(this.okResponse());
                    //TODO
                    writer.write(Arrays.toString(body));
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
