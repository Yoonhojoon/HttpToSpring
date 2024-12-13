package com.fullstacknetwork.http.example;

import org.json.JSONObject;
import org.json.JSONArray;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class HttpExample_4 {
    public static void main(String[] args) {
        try {
            // JSON 파일 경로 설정
            String relativePath = "src/main/java/com/fullstacknetwork/http/example/lec-06-prg-04-json-example.json";

            // JSON 파일 읽기
            String jsonContent = new String(Files.readAllBytes(Paths.get(relativePath)));

            // JSON 문자열을 객체로 파싱
            JSONObject superHeroes = new JSONObject(jsonContent);

            // 파이썬 코드와 동일한 데이터 출력
            System.out.println(superHeroes.getString("homeTown")); // Metro City
            System.out.println(superHeroes.getBoolean("active")); // true
            System.out.println(superHeroes.getJSONArray("members")
                    .getJSONObject(1)
                    .getJSONArray("powers")
                    .getString(2)); // Superhuman reflexes

        } catch (IOException e) {
            System.err.println("파일을 찾을 수 없습니다: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("JSON 처리 중 오류 발생: " + e.getMessage());
        }
    }
}