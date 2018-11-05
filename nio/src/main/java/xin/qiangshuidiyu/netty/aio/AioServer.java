package xin.qiangshuidiyu.netty.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

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
