package com.fullstacknetwork.http.example;

import org.json.JSONObject;
import org.json.JSONArray;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class HttpExample_3 {
    public static void main(String[] args) {
        try {
            // JSON 파일 읽기
            String jsonContent = new String(Files.readAllBytes(
                    Paths.get("lec-06-prg-03-json-example.json")));

            // JSON 파싱
            JSONObject superHeroes = new JSONObject(jsonContent);

            // 데이터 출력
            System.out.println(superHeroes.getString("homeTown"));
            System.out.println(superHeroes.getBoolean("active"));

            // members 배열의 두 번째 요소(인덱스 1)에서 powers 배열의 세 번째 요소(인덱스 2) 가져오기
            JSONArray members = superHeroes.getJSONArray("members");
            JSONObject secondMember = members.getJSONObject(1);
            JSONArray powers = secondMember.getJSONArray("powers");
            System.out.println(powers.getString(2));

        } catch (IOException e) {
            System.err.println("No file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("JSON 처리 중 오류 발생: " + e.getMessage());
        }
    }
}