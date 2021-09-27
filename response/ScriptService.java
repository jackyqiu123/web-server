package response;

import request.Request;

import java.io.*;
import java.util.Map;

public class ScriptService {
    private Request request;
    private String scriptPath;

    public ScriptService(Request request, String scriptPath) {
        this.request = request;
        this.scriptPath = scriptPath;
    }

    public void runScript(BufferedWriter bufferedResponseWriter, String body) throws IOException, InterruptedException {
        Map<String, String> headers = request.getHeaders();

        ProcessBuilder processBuilder = new ProcessBuilder(scriptPath.substring(0, scriptPath.length() - 1));
        Map<String, String> environment = processBuilder.environment();

        //set environment variables
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            environment.put(entry.getKey(), entry.getValue());
        }

        //start process
        Process process = processBuilder.start();

        //write body (if present) to the stdin of the process
        if (body != null) {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

            bufferedWriter.write(body);
        }

        //wait for process to finish
        process.waitFor();

        //write output of the script to the bufferedResponseWriter
        bufferedResponseWriter.write(String.valueOf(process.getOutputStream()));
        bufferedResponseWriter.flush();
    }
}
