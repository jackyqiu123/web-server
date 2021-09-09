import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Worker {

    private Request request;

    private Socket client;
    private String documentRoot;

    private Map<String, String> aliases;
    private Map<String, String> scriptAliases;


    //private RequestType requestType;
    //private String uri;

    public Worker(Socket client,
                  String documentRoot,
                  Map<String, String> aliases,
                  Map<String, String> scriptAliases) {
        this.client = client;
        this.documentRoot = documentRoot;
        this.aliases = aliases;
        this.scriptAliases = scriptAliases;
    }

    public void parseRequest() {
        createRequestObject();

        request.setUri(checkUri(request.getUri()));

        checkAuthentication(request.getUri());

        sendResponse();
    }

    private void createRequestObject() {
    }

    private String checkUri(String uri) {
        String[] uriParts = uri.split("/");
        String newUri = "";

        uriParts = Arrays.stream(uriParts).filter(item -> !item.equals("")).toArray(String[]::new);

        Boolean isAliased = false;

        for (String uriPart : uriParts) {
            //TODO slashes also after 'uriPart' ?
            uriPart = "/" + uriPart + "/";
            if (aliases.containsKey(uriPart)) {
                uriPart = aliases.get(uriPart);
                isAliased = true;
                newUri += uriPart;
            } else if (scriptAliases.containsKey(uriPart)) {
                uriPart = aliases.get(uriPart);
                isAliased = true;
                newUri += uriPart;
            } else {
                newUri += uriPart;
            }
        }

        uri = newUri;

        // remove trailing '/'
        uri = uri.substring(0, uri.length() - 1);

        if (!isAliased) {
            uri = documentRoot + uri;
        }


        File file = new File(uri);

        if (!file.exists() && !file.isDirectory()) {
            //TODO respond with 404 not found ???
            return "";
        }

        if (file.isDirectory()) {
            //TODO append dirIndex
        }

        System.out.println("URI = " + uri);

        return uri;
    }

    private void checkAuthentication(String uri) {
        // get folder to check
        String folderUri = "";
        File file = new File(uri);

        if (file.isDirectory()) {
            folderUri = uri;
        } else {
            folderUri = file.getParent();
        }


        // check if htaccess file exists
        String htaccessFileUri = folderUri + ".htaccess";
        File htaccessFile = new File(htaccessFileUri);

        if (!htaccessFile.exists()) {
            //TODO send error response w/ code ?????
            return;
        }


        //read in authUserFile to password map
        String htpasswdUri = "";

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(htaccessFileUri))) {
            String firstLine = bufferedReader.readLine();
            htpasswdUri = firstLine.split(" ")[1];
        } catch (IOException e) {
            //TODO send error response w/ code 403 ???
            return;
        }


        Map<String, String> passwords = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(htpasswdUri))) {
            String line;
            while ((line = br.readLine()) != null) {
                String name = line.split(":")[0];
                String password = line.split(":")[1];

                // replace header in password (e.g. '{SHA}')
                password = password.replaceAll("\\{.*\\}", "");

                passwords.put(name, password);
            }
        } catch (IOException e) {
            //TODO send error response w/ code 403 ???
            return;
        }


        //check if auth header exists
        Boolean authExists = false;
        String authorizationHeader = "";

        // TODO get auth header from request.header -> if it does exist set authExists to true and set
        //  requestUser and requestPassword
        if (request.getHeaders().containsKey("Authorization")) {
            authorizationHeader = request.getHeaders().get("Authorization").toString();
            authExists = true;
        }

        if (!authExists) {
            //TODO response w/ status code 401
        }

        //validate password
        Boolean passwordIsCorrect = false;

        for (Map.Entry<String,String> entry : passwords.entrySet()) {
            String potentialPlainPassword = entry.getKey() + ":" + entry.getValue();
            String potentialHashedPassword = hashPlainPassword(potentialPlainPassword);

            // TODO: ask how passwords are hashed and how to check them
        }

    }

    private void sendResponse() {
        //TODO
    }




    private String hashPlainPassword(String potentialPlainPassword) {
        MessageDigest digest = null;
        byte[] hash = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(potentialPlainPassword.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (hash == null) {
            return "";
        }

        String result = new String(hash, StandardCharsets.UTF_8);
        return result;
    }
}