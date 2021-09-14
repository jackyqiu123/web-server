package request;

import java.util.*;
import java.io.*;

public class HttpdConf{
    // Private String serverRoot;
    // private String documentRoot;
    // private String logFile;
    private HashMap<String,String> scriptAliasMap;
    private HashMap<String,String> httpdMap;
    private HashMap<String,String> aliasMap;
    private File file;
    private BufferedReader bufferedReader;

    public HttpdConf(String filePath){
        this.file = new File(filePath);
        try {
            this.bufferedReader = new BufferedReader(new FileReader(this.file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.aliasMap = new HashMap<String,String>();
        this.scriptAliasMap = new HashMap<String,String>();
        this.httpdMap = new HashMap<String,String>();
    }

    public void execute(){
        try{
            String key,value,currToken;
            StringTokenizer tokens;
            String line = this.bufferedReader.readLine();
            
            while(line != null){
                tokens = new StringTokenizer(line);
                currToken = tokens.nextToken();
                if(currToken.equalsIgnoreCase("scriptalias")){
                    key = tokens.nextToken();
                    while(tokens.hasMoreTokens()){
                        value = tokens.nextToken().replace("\"", "");
                        this.scriptAliasMap.put(key, value);
                    }
                }
                else if(currToken.equalsIgnoreCase("alias")){
                    key = tokens.nextToken();
                    while(tokens.hasMoreTokens()){
                        value = tokens.nextToken().replace("\"", "");
                        this.aliasMap.put(key, value);
                    }
                }
                else{ 
                    key = currToken;
                    value = tokens.nextToken().replace("\"", "");
                    this.httpdMap.put(key, value);
                    
                    
                }
                line = this.bufferedReader.readLine();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public String gethttpd(String key, String confSetting){

        if(confSetting.equalsIgnoreCase("scriptalias")){
            return this.scriptAliasMap.get(key);
        }
        else if(confSetting.equalsIgnoreCase("alias")){
            return this.aliasMap.get(key);
        }
        else if(confSetting.equalsIgnoreCase("httpdconf")){
            return this.httpdMap.get(key);
        }
        else return null;
    }
    public String getExtension() throws IOException{
        String[] splitDocRoot = this.httpdMap.get("DocumentRoot").split("/");
        String filenName = splitDocRoot[splitDocRoot.length - 1];
        String[] splitFileName = fileName.split("\\.");
        String fileExt = splitFileName[splitFileName.length -1];
        if(fileExt == fileName){
            throw new IOException("Error: The file may be a directory instead of a file");
        }
        return fileExt;

    }

    public int getPort() { return Integer.valueOf(httpdMap.get("Listen")); }

    public HashMap<String,String> getScriptAliasMap(){
        return this.scriptAliasMap;
    }
    public HashMap<String,String> getAliasMap(){
        return this.aliasMap;
    }
    public HashMap<String,String> gethttpdMap(){
        return this.httpdMap;
    }

}