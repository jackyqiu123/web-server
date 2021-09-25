package response;

import request.Request;

import java.io.*;
import java.util.List;

public class GetResponseService extends ResponseService {

    public GetResponseService(Request request) {
        super(request);
    }

    public void sendResponse(){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()))){
            if(isValidFile(this.file)){
                List<String> body = getFileContents();

                writer.write(this.okResponse());

                for (String line : body) {
                    writer.write(line);
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
