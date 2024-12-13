package com.fullstacknetwork.http;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HttpApplication {
	public static void main(String[] args) {
		try {
			lec_06_prg_01_http_web_server server = new lec_06_prg_01_http_web_server(8080);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}