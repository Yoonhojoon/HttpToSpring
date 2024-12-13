package com.fullstacknetwork.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class lec_06_prg_08_rest_client_v3 {
    private static final String BASE_URL = "http://127.0.0.1:8080/membership_api/";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) {
        try {
            // #1 미등록 회원 조회: 에러 케이스
            HttpResponse<String> response = sendRequest("GET", "0001", null);
            printResponse("#1", response);

            // #2 신규 회원 등록: 정상 케이스
            response = sendRequest("POST", "0001", "value=apple");
            printResponse("#2", response);

            // #3 등록된 회원 조회: 정상 케이스
            response = sendRequest("GET", "0001", null);
            printResponse("#3", response);

            // #4 이미 등록된 회원 재등록: 에러 케이스
            response = sendRequest("POST", "0001", "value=xpple");
            printResponse("#4", response);

            // #5 미등록 회원 정보 수정: 에러 케이스
            response = sendRequest("PUT", "0002", "value=xrange");
            printResponse("#5", response);

            // #6 등록된 회원 정보 수정: 정상 케이스
            sendRequest("POST", "0002", "value=xrange"); // 먼저 회원 등록
            response = sendRequest("PUT", "0002", "value=orange");
            printResponse("#6", response);

            // #7 등록된 회원 삭제: 정상 케이스
            response = sendRequest("DELETE", "0001", null);
            printResponse("#7", response);

            // #8 미등록 회원 삭제: 에러 케이스
            response = sendRequest("DELETE", "0001", null);
            printResponse("#8", response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HttpResponse<String> sendRequest(String method, String memberId, String data) throws Exception {
        String url = BASE_URL + memberId;

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(data != null ? url + "?" + data : url));

        switch (method) {
            case "GET":
                requestBuilder.GET();
                break;
            case "POST":
                requestBuilder.POST(HttpRequest.BodyPublishers.noBody());
                break;
            case "PUT":
                requestBuilder.PUT(HttpRequest.BodyPublishers.noBody());
                break;
            case "DELETE":
                requestBuilder.DELETE();
                break;
        }

        HttpRequest request = requestBuilder.build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static void printResponse(String testNum, HttpResponse<String> response) {
        try {
            JSONObject json = new JSONObject(response.body());
            String memberId = json.keys().next();
            String result = json.getString(memberId);

            System.out.printf("%s Code: %d >> JSON: %s >> JSON Result: %s%n",
                    testNum,
                    response.statusCode(),
                    response.body(),
                    result);
        } catch (Exception e) {
            System.err.println("Error parsing response: " + e.getMessage());
        }
    }
}