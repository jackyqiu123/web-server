package response;

import request.Request;

import java.io.*;

public class GetResponseService extends ResponseService {

    public GetResponseService(Request request) {
        super(request);
    }

    public void sendResponse(){
    //TODO get the file
    //TODO respond with response code and file-body by calling the ResponseService class
    
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()))){
            //TODO commented lines out because they did not compile
            if(isValidFile(this.file)){
                writer.write(this.okResponse());
                writer.write(this.getFileContents());
                writer.write(this.body.toString()); // server return the body content of the request
                writer.flush();
                writer.close();
            }
            else{
                //writer.write(this.notFoundResponse());
                writer.flush();
                writer.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        

    }
    
}
