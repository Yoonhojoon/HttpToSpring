package com.fullstacknetwork.http;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HttpApplication {
	public static void main(String[] args) {
		try {
			HttpServer_1 server = new HttpServer_1(8080);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}