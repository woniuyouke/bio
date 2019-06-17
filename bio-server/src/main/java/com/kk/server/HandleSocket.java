package com.kk.server;


import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Slf4j
public class HandleSocket implements Runnable {

    private Socket socket;

    public HandleSocket(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream  = null;
        try {
            bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            byte[] bytes = new byte[1024];
            int len ;
            if ((len = bufferedInputStream.read(bytes)) > -1) {
                String result = new String(bytes,0,len);
                log.info("来自客户端的信息={}",result);
            }
            String outString = Thread.currentThread().getName()+"已经进行处理";
            log.info("回复信息给客户端={}",outString);
            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            bufferedOutputStream.write(outString.getBytes());
            bufferedOutputStream.flush();
            log.info("回复完毕");
        } catch (IOException e) {
            log.error("出现io异常={}",e);
        } finally {
            log.info("关闭流");
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
                if(socket != null){
                    socket.close();
                }
            }catch (IOException e){
                log.error("出现io异常={}",e);
            }
        }
    }
}
