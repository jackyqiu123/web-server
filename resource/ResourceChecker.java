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
            request.setUri(documentRoot);
            return ResponseCode.CODE200;
        }

        String[] uriParts = uri.split("/");

        uriParts = filterOutEmptyStrings(uriParts);

        uri = aliaseUri(uriParts);

        File file = new File(uri);

        if (!file.exists() && !file.isDirectory()) {
            return ResponseCode.CODE404;
        }

        if (file.isDirectory()) {
            uri = uri + getDirectoryIndexHeader(request);
        }

        request.setUri(uri);

        return ResponseCode.CODE200;
    }


    private String[] filterOutEmptyStrings(String[] array) {
        return Arrays.stream(array).filter(item -> !item.equals("")).toArray(String[]::new);
    }

    private String aliaseUri(String[] uriParts) {
        String newUri = "";
        Boolean isAliased = false;

        for (String uriPart : uriParts) {
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

        if (!isAliased) {
            newUri = documentRoot.substring(0, documentRoot.length() - 1) + newUri;
        }

        return newUri;
    }

    private String getDirectoryIndexHeader(Request request) {
        if (request.getHeaders().containsKey("DirectoryIndex")) {
            return request.getHeaders().get("DirectoryIndex").toString();
        } else {
            return "index.html";
        }
    }
}
