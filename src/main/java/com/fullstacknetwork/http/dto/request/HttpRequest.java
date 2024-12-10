// HttpRequest.java
package com.fullstacknetwork.http.dto.request;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String path;
    private final String version;
    private final String requestLine;
    private final Map<String, String> headers;
    private final Map<String, String> queryParams;
    private final String body;

    public HttpRequest(String method, String path, String version, String requestLine,
                       Map<String, String> headers, Map<String, String> queryParams, String body) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.requestLine = requestLine;
        this.headers = headers;
        this.queryParams = queryParams;
        this.body = body;
    }

    public static HttpRequest parse(String rawRequest) {
        String[] requestParts = rawRequest.split("\r\n\r\n", 2);
        String[] headerParts = requestParts[0].split("\r\n");
        String[] requestLine = headerParts[0].split(" ");

        String method = requestLine[0];
        String fullPath = requestLine[1];
        String version = requestLine[2];

        // Parse query parameters
        Map<String, String> queryParams = new HashMap<>();
        String path = fullPath;
        if (fullPath.contains("?")) {
            String[] pathParts = fullPath.split("\\?", 2);
            path = pathParts[0];
            String[] params = pathParts[1].split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }

        // Parse headers
        Map<String, String> headers = new HashMap<>();
        for (int i = 1; i < headerParts.length; i++) {
            String[] headerPair = headerParts[i].split(": ", 2);
            if (headerPair.length == 2) {
                headers.put(headerPair[0], headerPair[1]);
            }
        }

        // Get body if exists
        String body = requestParts.length > 1 ? requestParts[1] : "";

        return new HttpRequest(method, path, version, headerParts[0], headers, queryParams, body);
    }

    // Getters
    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getVersion() { return version; }
    public String getRequestLine() { return requestLine; }
    public Map<String, String> getHeaders() { return headers; }
    public Map<String, String> getQueryParams() { return queryParams; }
    public String getBody() { return body; }
}