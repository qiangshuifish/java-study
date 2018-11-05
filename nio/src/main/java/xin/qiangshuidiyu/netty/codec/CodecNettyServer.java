package xin.qiangshuidiyu.netty.codec;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import xin.qiangshuidiyu.netty.common.AbstractNettyServer;
import xin.qiangshuidiyu.netty.common.Person;

/**
 * 自定义编码解码服务端
 */
public class CodecNettyServer extends AbstractNettyServer {

    public static void main(String[] args) throws Exception {
        CodecNettyServer server = new CodecNettyServer();

        server.startServer(8080);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new StringEncoder())
                .addLast(new MessagePackDecoder())
                .addLast(new ChannelHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        Person person = (Person) msg;
                        System.out.println("服务器收到："+person);
                        String response = "客服端你好";
                        ctx.writeAndFlush(response);
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        cause.printStackTrace();
                        socketChannel.close();
                    }
                });
    }
}
