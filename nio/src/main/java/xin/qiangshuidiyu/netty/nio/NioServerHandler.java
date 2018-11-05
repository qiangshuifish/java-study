package xin.qiangshuidiyu.netty.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

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
