package com.fullstacknetwork.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class lec_06_prg_01_http_web_server {
    // 내부 클래스로 HttpRequest 정의
    static class HttpRequest {
        private String method;
        private String requestLine;
        private String path;
        private String version;

        public static HttpRequest parse(String requestString) {
            HttpRequest request = new HttpRequest();
            String[] lines = requestString.split("\r\n");
            String[] requestLine = lines[0].split(" ");

            request.method = requestLine[0];
            request.path = requestLine[1];
            request.version = requestLine[2];
            request.requestLine = lines[0];

            return request;
        }

        public String getMethod() { return method; }
        public String getRequestLine() { return requestLine; }
        public String getPath() { return path; }
        public String getVersion() { return version; }
    }

    // 내부 클래스로 HttpResponse 정의
    static class HttpResponse {
        private static final String NEW_LINE = "\r\n";
        private final StringBuilder responseBuilder = new StringBuilder();

        public HttpResponse() {
            responseBuilder.append("HTTP/1.1 200 OK").append(NEW_LINE);
            responseBuilder.append("Content-Type: text/html; charset=utf-8").append(NEW_LINE);
            responseBuilder.append(NEW_LINE);
            responseBuilder.append("<h1>Hello from Simple HTTP Server!</h1>");
        }

        public byte[] getBytes() {
            return responseBuilder.toString().getBytes();
        }
    }

    // 내부 클래스로 RequestHandler 정의
    static class RequestHandler {
        public HttpResponse handle(HttpRequest request) {
            return new HttpResponse();
        }
    }

    // 서버 관련 필드
    private final int port;
    private final Selector selector;
    private final ServerSocketChannel serverChannel;
    private boolean isRunning;
    private final RequestHandler requestHandler;

    public lec_06_prg_01_http_web_server(int port) throws IOException {
        this.port = port;
        this.selector = Selector.open();
        this.serverChannel = ServerSocketChannel.open();
        this.requestHandler = new RequestHandler();
        this.isRunning = true;
    }

    public void start() throws IOException {
        System.out.println("## HTTP server started at http://localhost:" + port);

        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(port));
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (isRunning) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    accept(key);
                } else if (key.isReadable()) {
                    read(key);
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        StringBuilder requestBuilder = new StringBuilder();

        try {
            int bytesRead;
            while ((bytesRead = clientChannel.read(buffer)) > 0) {
                buffer.flip();
                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes);
                requestBuilder.append(new String(bytes));
                buffer.clear();

                if (requestBuilder.toString().contains("\r\n\r\n")) {
                    break;
                }
            }

            if (bytesRead == -1) {
                clientChannel.close();
                return;
            }

            HttpRequest request = HttpRequest.parse(requestBuilder.toString());
            HttpResponse response = requestHandler.handle(request);

            ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes());
            while (responseBuffer.hasRemaining()) {
                clientChannel.write(responseBuffer);
            }

            System.out.println("::Client address   : " + clientChannel.socket().getInetAddress().getHostAddress());
            System.out.println("::Client port      : " + clientChannel.socket().getPort());
            System.out.println("::Request command  : " + request.getMethod());
            System.out.println("::Request line     : " + request.getRequestLine());
            System.out.println("::Request path     : " + request.getPath());
            System.out.println("::Request version  : " + request.getVersion());

        } finally {
            clientChannel.close();
        }
    }

    public void stop() throws IOException {
        isRunning = false;
        selector.close();
        serverChannel.close();
        System.out.println("HTTP server stopped.");
    }

    // 메인 메소드 추가
    public static void main(String[] args) {
        try {
            lec_06_prg_01_http_web_server server = new lec_06_prg_01_http_web_server(8080);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}