package xin.qiangshuidiyu.netty.codec;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import xin.qiangshuidiyu.netty.common.AbstractNettyClient;
import xin.qiangshuidiyu.netty.common.Person;

/**
 * 自定义编码解码客服端
 */
public class CodecNettyClient extends AbstractNettyClient {

    public static void main(String[] args) {
        CodecNettyClient client = new CodecNettyClient();

        client.startClient("localhost",8080);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new StringDecoder())
                .addLast(new MessagePackEncoder())
                .addLast(new ChannelHandlerAdapter(){
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        //配置了解码器 所以这里不需要自己解析成byte数组
                        ctx.writeAndFlush(new Person("张三",18,"15912345678","213012387@qq.com"));
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        //在解码器中已经解析成字符串了
                        String response = (String) msg;
                        System.out.println("客服端收到："+response);
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        cause.printStackTrace();
                        socketChannel.close();
                    }
                });
    }
}
