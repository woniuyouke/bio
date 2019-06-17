package client;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class Client {

    public void start() throws IOException {
        Socket socket = new Socket("127.0.0.1", 8081);
        String msg = "hello,world";
        //1.拿到输出流
        //2.对输出流进行处理
        log.info("发送消息={}",msg);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        byte[] bytes = msg.getBytes();
        //3.输出msg
        bufferedOutputStream.write(bytes);
        bufferedOutputStream.flush();
        log.info("发送完毕");
        log.info("开始接收到消息");
        //4.对输入流进行处理
        BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        byte[] inBytes = new byte[1024];
        int len;
        //5.读取输入流
        if ((len = bufferedInputStream.read(inBytes)) != -1) {
            String result = new String(inBytes, 0, len);
            log.info("接收到的消息={}",result);
        }
        //6.关闭输入输出流
        bufferedOutputStream.close();
        bufferedInputStream.close();
        socket.close();
    }


}
