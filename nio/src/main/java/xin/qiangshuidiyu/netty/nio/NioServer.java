package xin.qiangshuidiyu.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

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
