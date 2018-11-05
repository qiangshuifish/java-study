package xin.qiangshuidiyu.netty.frame;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * LineBasedFrameDecoder 处理拆包，粘包 客服端
 */
public class NettyFrameClient {

    public static void main(String[] args) {

        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new LineBasedFrameDecoder(1024))
                                .addLast(new StringDecoder())
                                .addLast(new ChannelHandlerAdapter(){
                                    int count = 0;
                                    @Override
                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {

                                        StringBuilder sb = new StringBuilder();

                                        // 这里组装了一个有101个换行符的字符串，在服务端LineBasedFrameDecoder时
                                        // 会被当做101个包处理，也就是会触发 101次 channelRead 方法
                                        sb.append("客服端将向服务器发送100条数据\n");
                                        for (int i = 0; i < 100; i++) {
                                             sb.append("客户端想服务发送的数据，第").append(count++).append("条\n");
                                        }
                                        ByteBuf byteBuf = Unpooled.copiedBuffer(sb.toString().getBytes());
                                        ctx.writeAndFlush(byteBuf);
                                    }

                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        String response = (String) msg;
                                        System.out.println("已经收到服务器返回消息");
                                        System.out.println(response);

                                    }

                                    @Override
                                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                        // 释放资源
                                        cause.printStackTrace();
                                        ctx.close();
                                    }
                                });
                    }
                });

        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.connect("localhost", 8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            eventLoopGroup.shutdownGracefully();
        }
    }
}
