package authentication;

import request.Request;
import response.ResponseCode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Authenticator {

    //TODO copy over authentication implementation

    public ResponseCode checkAuthentication(Request request) {
        // get folder to check
        String folderUri = "";
        File file = new File(request.getUri());

        if (file.isDirectory()) {
            folderUri = request.getUri();
        } else {
            folderUri = file.getParent();
        }


        // check if htaccess file exists
        String htaccessFileUri = folderUri + ".htaccess";
        File htaccessFile = new File(htaccessFileUri);

        if (!htaccessFile.exists()) {
            //TODO send error response w/ code ?????
            return null;
        }


        //read in authUserFile to password map
        String htpasswdUri = "";

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(htaccessFileUri))) {
            String firstLine = bufferedReader.readLine();
            htpasswdUri = firstLine.split(" ")[1];
        } catch (IOException e) {
            //TODO send error response w/ code 403 ???
            return ResponseCode.CODE403;
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
            return ResponseCode.CODE403;
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


        //success
        return ResponseCode.CODE200;
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
