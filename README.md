### bio模式下-客户端和服务端的一次通信

### 1.服务端

1.监听tcp端口

2.while循环接收连接

3.对接收到的连接进行InputStream/OutputStream操作

```+
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
```

```+
 public void run() {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream  = null;
        try {
            bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            byte[] bytes = new byte[1024];
            int len ;
            //这里不能用while,因为只有当客户端连接关闭的时候，== -1 才会成立，否则会一直阻塞
            //请思考，当传输字节超过1024时，数据该如何处理
            if ((len = bufferedInputStream.read(bytes)) > -1) {
                String result = new String(bytes,0,len);
                log.info("本次接收到的结果={}",result);
            }
            log.info("回复信息给客户端");
            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            String outString = Thread.currentThread().getName()+"接收到了";
            bufferedOutputStream.write(outString.getBytes());
            bufferedOutputStream.flush();
            log.info("回复完成");
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
            }catch (IOException e){
                log.error("出现io异常={}",e);
            }
        }
    }
```

### 2.客户端

1.与服务端建立连接

2.发送消息给服务端

3.接收服务端返回的消息

```+ 
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


```

### 3.运行日志

1.服务端

```+ 
[INFO ] 2019-06-17 21:11:26 [main] com.kk.server.Server - 服务启动监听
[INFO ] 2019-06-17 21:11:26 [Thread-0] com.kk.server.HandleSocket - 来自客户端的信息=hello,world
[INFO ] 2019-06-17 21:11:26 [Thread-0] com.kk.server.HandleSocket - 回复信息给客户端=Thread-0已经进行处理
[INFO ] 2019-06-17 21:11:26 [Thread-0] com.kk.server.HandleSocket - 回复完毕
[INFO ] 2019-06-17 21:11:26 [Thread-0] com.kk.server.HandleSocket - 关闭流
```

2.客户端

```+ 
21:11:32.120 [main] INFO client.Client - 发送消息=hello,world
21:11:32.127 [main] INFO client.Client - 发送完毕
21:11:32.127 [main] INFO client.Client - 开始接收到消息
21:11:32.130 [main] INFO client.Client - 接收到的消息=Thread-0已经进行处理
```

