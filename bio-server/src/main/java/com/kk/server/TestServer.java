package com.kk.server;

import java.io.IOException;

public class TestServer {

    public static void main(String[] args) throws IOException {
        Server server = new Server(8081);
        server.listen();
    }
}
