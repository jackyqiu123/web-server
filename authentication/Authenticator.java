package authentication;

import request.Request;
import response.ResponseCode;

import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

//TODO cherry on top: store htpasswd password for later access on first parse

public class Authenticator {

    Request request;

    private Map<String, String> passwords;

    public Authenticator(Request request) {
        this.request = request;
    }

    public ResponseCode checkAuthentication() {
        ResponseCode responseCode;
        responseCode = readInPasswords();

        if (responseCode == null) return ResponseCode.CODE200;
        if (responseCode != ResponseCode.CODE200) return responseCode;

        responseCode = checkPassword();

        return responseCode;
    }

    private ResponseCode readInPasswords() {
        // get folder to check
        String folderUri = "";
        File file = new File(request.getUri());

        if (file.isDirectory()) {
            folderUri = request.getUri();
        } else {
            folderUri = file.getParent();
        }

        // check if access file exists
        String accessFileEnding = ".htaccess";

        if (request.getHeaders().containsKey("AccessFile")) {
            accessFileEnding = request.getHeaders().get("AccessFile").toString();
        }

        String htaccessFileUri = folderUri + accessFileEnding;
        File htaccessFile = new File(htaccessFileUri);

        if (!htaccessFile.exists()) {
            return null;
        }

        //read in authUserFile to password map
        passwords = new HashMap<>();

        String htpasswdUri = "";

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(htaccessFileUri))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("AuthUserFile")) {
                    htpasswdUri = line.split(" ")[1].replaceAll("^\"|\"$", "");
                    break;
                }
            }

            if (htpasswdUri.isEmpty()) {
                throw new RuntimeException();
            }
        } catch (IOException | RuntimeException e) {
            return ResponseCode.CODE500;
        }

        //read in .htpasswd to password map
        passwords = new HashMap<>();

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
            return ResponseCode.CODE500;
        }

        return ResponseCode.CODE200;
    }

    private ResponseCode checkPassword() {
        //check if auth header exists
        Boolean authExists = false;
        String authorizationHeader = "";

        if (request.getHeaders().containsKey("Authorization")) {
            authorizationHeader = request.getHeaders().get("Authorization").toString();
            authorizationHeader = authorizationHeader.split(" ")[1];
            authExists = true;
        }

        if (!authExists) {
            return ResponseCode.CODE401;
        }

        String credentials = new String(
                Base64.getDecoder().decode( authorizationHeader ),
                Charset.forName( "UTF-8" )
        );

        String[] tokens = credentials.split( ":" );

        Boolean passwordCorrect = verifyPassword(tokens[0], tokens[1]);

        if (!passwordCorrect) {
            return ResponseCode.CODE403;
        }

        return ResponseCode.CODE200;
    }

    private boolean verifyPassword( String username, String password ) {
        // encrypt the password, and compare it to the password stored
        // in the password file (keyed by username)
        if (!passwords.containsKey(username)) {
            return false;
        }

        String realPassword = passwords.get(username);
        String requestPassword = encryptClearPassword(password);

        return realPassword.equals(requestPassword);
    }

    private String encryptClearPassword( String password ) {
        // Encrypt the cleartext password (that was decoded from the Base64 String
        // provided by the client) using the SHA-1 encryption algorithm
        try {
            MessageDigest mDigest = MessageDigest.getInstance( "SHA-1" );
            byte[] result = mDigest.digest( password.getBytes() );

            return Base64.getEncoder().encodeToString( result );
        } catch( Exception e ) {
            return "";
        }
    }
}
