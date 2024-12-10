package com.fullstacknetwork.http;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HttpApplication {
	public static void main(String[] args) {
		try {
			HttpServer server = new HttpServer(8080);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}