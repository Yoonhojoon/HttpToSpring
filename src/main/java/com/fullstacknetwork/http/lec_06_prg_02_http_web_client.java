package com.fullstacknetwork.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

public class lec_06_prg_02_http_web_client {
    public static void main(String[] args) {
        System.out.println("## HTTP client started.");

        try {
            // First GET request
            System.out.println("## GET request for http://localhost:8080/temp/");
            String getResponse1 = sendGetRequest("http://localhost:8080/temp/");
            System.out.println("## GET response [start]");
            System.out.println(getResponse1);
            System.out.println("## GET response [end]");

            // Second GET request with query parameters
            System.out.println("## GET request for http://localhost:8080/?var1=9&var2=9");
            String getResponse2 = sendGetRequest("http://localhost:8080/?var1=9&var2=9");
            System.out.println("## GET response [start]");
            System.out.println(getResponse2);
            System.out.println("## GET response [end]");

            // POST request
            System.out.println("## POST request for http://localhost:8080/ with var1 is 9 and var2 is 9");
            Map<String, String> postData = Map.of("var1", "9", "var2", "9");
            String postResponse = sendPostRequest("http://localhost:8080", postData);
            System.out.println("## POST response [start]");
            System.out.println(postResponse);
            System.out.println("## POST response [end]");

        } catch (IOException e) {
            System.err.println("Error during HTTP requests: " + e.getMessage());
        }

        System.out.println("## HTTP client completed.");
    }

    private static String sendGetRequest(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        return getResponse(conn);
    }

    private static String sendPostRequest(String urlStr, Map<String, String> params) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String postData = getParamsString(params);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(postData.getBytes(StandardCharsets.UTF_8));
        }

        return getResponse(conn);
    }

    private static String getParamsString(Map<String, String> params) throws IOException {
        StringJoiner sj = new StringJoiner("&");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                    + URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return sj.toString();
    }

    private static String getResponse(HttpURLConnection conn) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
                response.append('\n');
            }
        } finally {
            conn.disconnect();
        }
        return response.toString();
    }
}