package xin.qiangshuidiyu.netty.aio;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

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
