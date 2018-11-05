# Java的I/O模型
java在JDK中提供了三种I/O模型，一类是最原始也是我们用得最多最熟悉的阻塞I/O模型BIO，使用InputStream和OutputStream进行读写操作。
![alt](https://www.qiangshuidiyu.xin/static/upload/20180626/upload_f5795d2db64b825e07198b53398fab8b.png)
第二种是jdk1.4之后的NIO,相比起BIO在性能上无疑是一个巨大的提升，NIO的应用场所主要是在网络上，因为相比起CPU，内存，硬盘的处理速度，网络差了太多太多，所以在开发网络应用时，使用BIO模型，处理器大多数的时候是在等待网络读写完毕，这无疑是对性能的极大浪费。NIO允许在网络读写期间，让处理器非阻塞的处理其他事情。
![alt](https://www.qiangshuidiyu.xin/static/upload/20180626/upload_10c71c2a31cbc34b1a946fca0bb55dc0.png)
这里初学者很容易把这个与多线程的读写混为一谈，首先NIO和BIO都是多线程并行操作的，但是系统创建线程的代价也是非常高的，这也是为什么使用线程的地方都会优先考虑使用线程池。当网络请求并发量大网络又不给力时，线程池的大多数线程都处于了等待网络io的阻塞状态，最后超出线程池的限制之后，服务任然会变得不可用。NIO的思想是，使用一部分线程专门的网络请求，连接成功后再用另外一部分线程监听网络读写，读写完毕之后再经行处理（一个线程可以监听N个），这样并发多的时候任然可以保证服务器接受请求正常。

AIO是在对NIO的一种改进，NIO在处理网络请求时是需要专门的线程去轮询监听网络读写是否完毕，AIO采用的是异步非阻塞的处理方法，如果熟悉nodejs的同学对于这个名词会非常的熟悉，AIO采用的处理方式和nodejs基本一致，是以回调函数的形式经行处理的。把网络读写完之后的操作以回调的形式传入，等网络读写完毕之后会自动执行这些代码。
![alt](https://www.qiangshuidiyu.xin/static/upload/20180626/upload_c48325f1a9a50565c21b30009d7d5a05.png)
## BIO 阻塞I/O模型
一个客服端对应一个服务端线程，代码编写简单，程序逻辑也简单。
循环调用serverSocket.accept()方法，一旦有新的连接建立就新建一个线程处理它。

```java
/**
 * 阻塞的io
 */
public class BioServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("服务启动端口：" + port);

        try {
            Socket socket = null;
            while (true) {
                socket = serverSocket.accept();
                new Thread(new BioServerHandler(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
            System.out.println("服务停止");
        }

    }
}
```
客服端请求处理器 BioServerHandler.java

```java
public class BioServerHandler implements Runnable {

    private Socket socket;

    public BioServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                in = new BufferedReader(new InputStreamReader(
                        this.socket.getInputStream()));
                out = new PrintWriter(this.socket.getOutputStream(), true);
                while (true) {
                    String body = in.readLine();
                    if (body == null)
                        break;
                    System.out.println("服务器收到客服端数据：" + body);
                    String res = "客服端你好！收到的数据为｛" + body + "｝";
                    out.println(res);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

```

## NIO 非阻塞I/O模型
相比BIO的处理逻辑,NIO处理起来要复杂得多。
首先需要新建一个Selector多路复用器，然后在Selector上注册服务端的`SelectionKey.OP_ACCEPT`事件，这其实和BIO中的`serverSocket.accept()`类似
一个是注册给Selector，然后去循环检查。一个是自己手动循环调用。

```java
/**
 * 非阻塞的io
 */
public class NioServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;

        Selector selector = Selector.open();
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //server绑定端口号
        socketChannel.socket().bind(new InetSocketAddress(port));
        //注册accept事件
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("服务器启动端口：" + port);

        NioServerHandler nioServerHandler = new NioServerHandler(selector);
        //处理请求
        while (true){
            nioServerHandler.requestHandler();
        }
    }
}
```

`requestHandler`方法中，第一行是设置处理的超时时间。然后把注册到selector上的channel循环遍历出来查看状态（其实在没有连接时，只会有一个），前边给ServerSocketChannel注册了` SelectionKey.OP_ACCEPT`事件，那么`key.isAcceptable()`的key对应的也一定是`ServerSocketChannel`,然后把接受的请求`SocketChannel`在挨个注册到`selector`上，然后的`key.isReadable()`就代表着网络I/O有数据了，在经行其他相应的操作。

NioServerHandler.java

```java
public class NioServerHandler {
    private Selector selector;

    public NioServerHandler(Selector selector) {
        this.selector = selector;
    }

    /**
     * 处理请求
     * @throws IOException
     */
    public void requestHandler() throws IOException {
        //1秒超时
        selector.select(1000);
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();

        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            handlerKey(selector, key);
        }
    }

    /**
     * 处理 selectionKeys
     * @param selector
     * @param key
     * @throws IOException
     */
    private static void handlerKey(Selector selector, SelectionKey key) throws IOException {


        if(key.isValid()){

            if(key.isAcceptable()){
                //接受事件 肯定是服务端的 ServerSocketChannel
                ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                //相当于接受一个Socket
                SocketChannel acceptChannel = channel.accept();
                // 设置非阻塞
                acceptChannel.configureBlocking(false);
                //注册到 selector 上
                acceptChannel.register(selector,SelectionKey.OP_READ);
            }

            if(key.isReadable()){
                //可读事件肯定是 Socket
                SocketChannel channel = (SocketChannel) key.channel();
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                // 读取请求数据到 byteBuffer
                int read = channel.read(byteBuffer);
                if(read > 0){
                    byteBuffer.flip();
                    String body = new String(byteBuffer.array(), "UTF-8");
                    System.out.println("服务器收到请求："+body);
                    //响应数据
                    byte[] bytes = ("客服端你好！收到的数据为｛" + body + "｝").getBytes("UTF-8");
                    ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
                    writeBuffer.put(bytes);
                    writeBuffer.flip();
                    channel.write(writeBuffer);

                    /*这种方式写不到客服端
                    ByteBuffer wrap = ByteBuffer.wrap(("客服端你好！收到的数据为｛" + body + "｝").getBytes("UTF-8"));
                    wrap.flip();
                    channel.write(wrap);*/

                }else {
                    key.channel();
                    channel.close();
                }
            }

        }
    }
}
```

## AIO 异步非阻塞I/O模型
 
 异步非阻塞的I/O就基本和node的编写方法非常相近了，也是基于回调来进行操作的！
 
 首先创建一个`AsynchronousServerSocketChannel`对象，绑定端口，然后在`accept`方法中传自己和请求的处理操作。这里为什么要传递自己呢，因为`AsynchronousServerSocketChannel.accept`是异步的，执行该方法后并不会阻塞，它就相当于BIO中`ServerSocket.accept`方法，前者非阻塞，后者阻塞。
 
```java
/**
 * 异步非阻塞的IO
 */
public class AioServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 8080;

        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));

        AioServer server = new AioServer();

        serverSocketChannel.accept(server, new AioServerRequestHandler(serverSocketChannel));
        System.out.println("服务器启动端口：" + port);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
```

请求处理器`AioServerRequestHandler`实现了一个接口`CompletionHandler<AsynchronousSocketChannel, AioServer>`，它有两个泛型，第一个参数是这个处理器**处理的数据类型是什么类**型，第二个是服务器本身，上边说了`AsynchronousServerSocketChannel.accept`是非阻塞的，在回调中再次调用`AsynchronousServerSocketChannel.accept`即代表可以继续接收请求。CompletionHandler接口有很多方法，JDK中接口方法可以有默认实现，所以可以选择性的重写一些方法来实现自己需要的功能。

```java
public class AioServerRequestHandler implements CompletionHandler<AsynchronousSocketChannel, AioServer> {

    private AsynchronousServerSocketChannel serverSocketChannel;

    public AioServerRequestHandler(AsynchronousServerSocketChannel serverSocketChannel) {
        this.serverSocketChannel = serverSocketChannel;
    }

    @Override
    public void completed(AsynchronousSocketChannel socketChannel, AioServer attachment) {
        //这个方法相当于是异步的 accept
        serverSocketChannel.accept(attachment,this);

        ByteBuffer allocate = ByteBuffer.allocate(1024);

        // 这里的read是异步回调方式，类似于node 中的 read(buffer,buffer,callback)的形式
        // 其他的read方法最终都会阻塞在这个方法里头，所以使用这种异步回调的方式
        socketChannel.read(allocate, allocate, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                try {
                    //读取数据
                    String body = new String(attachment.array(), "UTF-8");
                    System.out.println("服务器收到请求："+body);

                    //响应数据
                    byte[] bytes = ("客服端你好！收到的数据为｛" + body + "｝").getBytes("UTF-8");
                    ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
                    writeBuffer.put(bytes);
                    writeBuffer.flip();
                    socketChannel.write(writeBuffer);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                exc.printStackTrace();
                System.exit(1);
            }
        });

    }

    @Override
    public void failed(Throwable exc, AioServer attachment) {
        try {
            serverSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exc.printStackTrace();
        System.exit(1);
    }
}
```

## 客户端代码

```java
/**
 * 使用buff类缓冲流，直接使用流时会导致阻塞卡死
 */
public class SocketClient {
    public static void main(String[] args) {
        int port = 8080;
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket("127.0.0.1", port);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("你好服务器");
            System.out.println("已经向服务器发送数据");
            String resp = in.readLine();
            System.out.println("接受服务器数据 : " + resp);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## [源码地址](https://gitee.com/qsdy/netty-nio)

