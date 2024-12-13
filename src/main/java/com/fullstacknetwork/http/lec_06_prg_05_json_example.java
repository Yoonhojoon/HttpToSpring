package com.fullstacknetwork.http;


import org.json.JSONObject;
import org.json.JSONArray;

public class lec_06_prg_05_json_example {
    public static void main(String[] args) {
        // JSON 객체 생성
        JSONObject superHeroes = new JSONObject();
        superHeroes.put("squadName", "Super hero squad");
        superHeroes.put("homeTown", "Metro City");
        superHeroes.put("formed", 2016);
        superHeroes.put("secretBase", "Super tower");
        superHeroes.put("active", true);

        // members 배열 생성
        JSONArray members = new JSONArray();

        // 첫 번째 멤버
        JSONObject member1 = new JSONObject();
        member1.put("name", "Molecule Man");
        member1.put("age", 29);
        member1.put("secretIdentity", "Dan Jukes");

        JSONArray powers1 = new JSONArray();
        powers1.put("Radiation resistance");
        powers1.put("Turning tiny");
        powers1.put("Radiation blast");
        member1.put("powers", powers1);

        // 두 번째 멤버
        JSONObject member2 = new JSONObject();
        member2.put("name", "Madame Uppercut");
        member2.put("age", 39);
        member2.put("secretIdentity", "Jane Wilson");

        JSONArray powers2 = new JSONArray();
        powers2.put("Million tonne punch");
        powers2.put("Damage resistance");
        powers2.put("Superhuman reflexes");
        member2.put("powers", powers2);

        // 세 번째 멤버
        JSONObject member3 = new JSONObject();
        member3.put("name", "Eternal Flame");
        member3.put("age", 1000000);
        member3.put("secretIdentity", "Unknown");

        JSONArray powers3 = new JSONArray();
        powers3.put("Immortality");
        powers3.put("Heat Immunity");
        powers3.put("Inferno");
        powers3.put("Teleportation");
        powers3.put("Interdimensional travel");
        member3.put("powers", powers3);

        // members 배열에 멤버들 추가
        members.put(member1);
        members.put(member2);
        members.put(member3);
        superHeroes.put("members", members);

        // JSON 출력 (들여쓰기 4칸 적용)
        System.out.println(superHeroes.toString(4));
    }
}