import java.util.*;
import java.io.*;

public class MimeTypes{
    private HashMap<String, String> mediaType;
    public MimeTypes(String mimeFile){
        this.mediaType = new HashMap<String, String>();
    }
    public void execute(String mimeFile){
        try{
            File file = new File(mimeFile);
            BufferedReader mimeReader = new BufferedReader(new FileReader(file));
            String mime, ext;
            String line = mimeReader.readLine();
            
            while(line != null){
                if(line.charAt(0) == "#" || line == ""){
                    continue;
                }
                StringTokenizer tokens = new StringTokenizer(line); 
                mime = tokens.nextToken();
                while(tokens.hasMoreTokens()){
                    ext = tokens.nextToken();
                    this.mediaType.put(ext, mime);
                }
                line = mimeReader.readLine();
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