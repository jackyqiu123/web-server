package resource;

import request.HttpdConf;
import request.Request;
import response.ResponseCode;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

public class ResourceChecker {

    private Map<String, String> aliases;
    private Map<String, String> scriptAliases;
    private String documentRoot;

    public ResourceChecker(Map<String, String> aliases,
                           Map<String, String> scriptAliases,
                           String documentRoot) {
        this.aliases = aliases;
        this.scriptAliases = scriptAliases;
        this.documentRoot = documentRoot;
    }

    public ResponseCode checkUri(Request request) {

        String uri = request.getUri();

        if (uri.equals("/")) {
            request.setUri(documentRoot + uri);
            return ResponseCode.CODE200;
        }

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
            return ResponseCode.CODE404;
        }

        if (file.isDirectory()) {
            //TODO append dirIndex -> from httpd.conf -> default is index.html
        }

        request.setUri(uri);

        return ResponseCode.CODE200;
    }
}
