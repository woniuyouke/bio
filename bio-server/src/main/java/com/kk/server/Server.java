package com.kk.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class Server {

    private int port;


    public Server(int port) {
        this.port = port;
    }

    public void listen() throws IOException {
        ServerSocket serverSocket = null;
        try {
            log.info("服务启动监听");
            serverSocket = new ServerSocket(port);
            //循环接收到客户端的连接
            while (true) {
                Socket socket = serverSocket.accept();
                //得到连接后，开启一个线程处理连接
                handleSocket(socket);
            }
        }finally {
            if(serverSocket != null){
                serverSocket.close();
            }
        }
    }
    private void handleSocket(Socket socket) {
        HandleSocket socketHandle = new HandleSocket(socket);
        new Thread(socketHandle).start();
    }
}
