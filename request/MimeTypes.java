package request;

import java.util.*;
import java.io.*;

public class MimeTypes{
    private HashMap<String, String> mediaType;
    private File file;
    private BufferedReader bufferedReader;
    public MimeTypes(String mimeFile){
        this.file = new File(mimeFile);
        try {
            this.bufferedReader = new BufferedReader(new FileReader(this.file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.mediaType = new HashMap<String, String>();
    }
    public void execute(){
        try{
            String mime, ext;
            String line = this.bufferedReader.readLine();
            
            while(line != null){
                if(line.charAt(0) == '#' || line == " "){
                    line = this.bufferedReader.readLine();
                    continue;
                }
                StringTokenizer tokens = new StringTokenizer(line); 
                mime = tokens.nextToken();
                while(tokens.hasMoreTokens()){
                    ext = tokens.nextToken();
                    this.mediaType.put(ext, mime);
                }
                line = this.bufferedReader.readLine();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }


    }
    
    public String getMime(String ext){
        String mime = mediaType.get(ext);
        if(mime == null){
            return "text/text";
        }
        else{
            return mime;
        }
    }
}