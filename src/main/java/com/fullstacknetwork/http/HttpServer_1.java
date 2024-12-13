package com.fullstacknetwork.http;

import com.fullstacknetwork.http.dto.RequestHandler;
import com.fullstacknetwork.http.dto.request.HttpRequest;
import com.fullstacknetwork.http.dto.response.HttpResponse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class HttpServer_1 {
    private final int port;
    private final Selector selector;
    private final ServerSocketChannel serverChannel;
    private boolean isRunning;
    private final RequestHandler requestHandler;

    public HttpServer_1(int port) throws IOException {
        this.port = port;
        this.selector = Selector.open();
        this.serverChannel = ServerSocketChannel.open();
        this.requestHandler = new RequestHandler();
        this.isRunning = true;
    }

    public void start() throws IOException {
        System.out.println("## HTTP server started at http://localhost:" + port);

        // Configure server channel
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(port));
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        // Event loop
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

                // Check if we've reached the end of the HTTP request
                if (requestBuilder.toString().contains("\r\n\r\n")) {
                    break;
                }
            }

            if (bytesRead == -1) {
                clientChannel.close();
                return;
            }

            // Parse and handle the request
            HttpRequest request = HttpRequest.parse(requestBuilder.toString());
            HttpResponse response = requestHandler.handle(request);

            // Send response
            ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes());
            while (responseBuffer.hasRemaining()) {
                clientChannel.write(responseBuffer);
            }

            // Print request details
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
}
