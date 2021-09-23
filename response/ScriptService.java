package response;

import request.Request;

import java.io.BufferedWriter;
import java.net.http.HttpClient;
import java.util.Map;

public class ScriptService {
    private Request request;
    private String scriptPath;
    private BufferedWriter bufferedWriter;

    public ScriptService(Request request, String scriptPath, BufferedWriter bufferedWriter) {
        this.request = request;
        this.scriptPath = scriptPath;
    }

    public void runScript() {
        Map<String, String> headers = request.getHeaders();

        ProcessBuilder processBuilder = new ProcessBuilder(scriptPath);
        Map<String, String> environment = processBuilder.environment();

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            environment.put(entry.getKey(), entry.getValue());
        }

        processBuilder.redirectOutput();
    }



}
