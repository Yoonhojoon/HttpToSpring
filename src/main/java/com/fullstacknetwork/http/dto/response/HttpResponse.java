// HttpResponse.java
package com.fullstacknetwork.http.dto.response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final int statusCode;
    private final String statusMessage;
    private final Map<String, String> headers;
    private final String body;

    public HttpResponse(int statusCode, String statusMessage, Map<String, String> headers, String body) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.headers = headers;
        this.body = body;
    }

    public byte[] getBytes() {
        StringBuilder response = new StringBuilder();

        // Status Line
        response.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusMessage).append("\r\n");

        // Headers
        headers.forEach((key, value) -> response.append(key).append(": ").append(value).append("\r\n"));

        // Empty line separating headers from body
        response.append("\r\n");

        // Body
        if (body != null) {
            response.append(body);
        }

        return response.toString().getBytes();
    }
}