package com.fullstacknetwork.http;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class lec_06_prg_07_rest_server_v3 {
    public static void main(String[] args) {
        SpringApplication.run(lec_06_prg_07_rest_server_v3.class, args);
    }
}

class MembershipHandler {
    // 멤버십 관리를 위한 해시맵
    private Map<String, String> database = new HashMap<>();

    // POST 요청 처리
    public Map<String, String> create(String id, String value) {
        Map<String, String> response = new HashMap<>();
        if (database.containsKey(id)) {
            response.put(id, "None");
        } else {
            database.put(id, value);
            response.put(id, database.get(id));
        }
        return response;
    }

    // GET 요청 처리
    public Map<String, String> read(String id) {
        Map<String, String> response = new HashMap<>();
        response.put(id, database.getOrDefault(id, "None"));
        return response;
    }

    // PUT 요청 처리
    public Map<String, String> update(String id, String value) {
        Map<String, String> response = new HashMap<>();
        if (database.containsKey(id)) {
            database.put(id, value);
            response.put(id, database.get(id));
        } else {
            response.put(id, "None");
        }
        return response;
    }

    // DELETE 요청 처리
    public Map<String, String> delete(String id) {
        Map<String, String> response = new HashMap<>();
        if (database.containsKey(id)) {
            database.remove(id);
            response.put(id, "Removed");
        } else {
            response.put(id, "None");
        }
        return response;
    }
}

@RestController
@RequestMapping("/membership_api")
class MembershipManager {
    private final MembershipHandler myManager = new MembershipHandler();

    // Create 핸들러
    @PostMapping("/{memberId}")
    public ResponseEntity<Map<String, String>> post(
            @PathVariable String memberId,
            @RequestParam String value) {
        return ResponseEntity.ok(myManager.create(memberId, value));
    }

    // Read 핸들러
    @GetMapping("/{memberId}")
    public ResponseEntity<Map<String, String>> get(
            @PathVariable String memberId) {
        return ResponseEntity.ok(myManager.read(memberId));
    }

    // Update 핸들러
    @PutMapping("/{memberId}")
    public ResponseEntity<Map<String, String>> put(
            @PathVariable String memberId,
            @RequestParam String value) {
        return ResponseEntity.ok(myManager.update(memberId, value));
    }

    // Delete 핸들러
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable String memberId) {
        return ResponseEntity.ok(myManager.delete(memberId));
    }
}