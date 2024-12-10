// RequestHandler.java
package com.fullstacknetwork.http.dto;

import com.fullstacknetwork.http.dto.request.HttpRequest;
import com.fullstacknetwork.http.dto.response.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class RequestHandler {
    public HttpResponse handle(HttpRequest request) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        headers.put("Connection", "close");

        String responseBody;

        // Handle GET request with query parameters (multiplication)
        if (request.getMethod().equals("GET") && !request.getQueryParams().isEmpty()) {
            try {
                int var1 = Integer.parseInt(request.getQueryParams().get("var1"));
                int var2 = Integer.parseInt(request.getQueryParams().get("var2"));
                int result = var1 * var2;
                responseBody = String.format("<html>GET request for calculation => %d x %d = %d</html>",
                        var1, var2, result);
            } catch (Exception e) {
                responseBody = "<html>Invalid parameters</html>";
            }
        }
        // Handle POST request (multiplication)
        else if (request.getMethod().equals("POST")) {
            String[] params = request.getBody().split("&");
            Map<String, String> postParams = new HashMap<>();
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    postParams.put(keyValue[0], keyValue[1]);
                }
            }

            try {
                int var1 = Integer.parseInt(postParams.get("var1"));
                int var2 = Integer.parseInt(postParams.get("var2"));
                int result = var1 * var2;
                responseBody = String.format("POST request for calculation => %d x %d = %d",
                        var1, var2, result);
            } catch (Exception e) {
                responseBody = "Invalid parameters";
            }
        }
        // Handle directory request
        else if (request.getMethod().equals("GET")) {
            responseBody = String.format("<html><p>HTTP Request GET for Path: %s</p></html>",
                    request.getPath());
        }
        // Handle unknown request
        else {
            responseBody = "<html>Invalid request</html>";
        }

        headers.put("Content-Length", String.valueOf(responseBody.length()));

        return new HttpResponse(200, "OK", headers, responseBody);
    }
}