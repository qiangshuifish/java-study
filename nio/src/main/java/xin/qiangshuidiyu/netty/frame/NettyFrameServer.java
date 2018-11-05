package xin.qiangshuidiyu.netty.frame;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 *  LineBasedFrameDecoder 处理拆包，粘包  服务端
 */
public class NettyFrameServer {

    public static void main(String[] args) {
        int port = 8080;

        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(boosGroup,workerGroup)
                .childOption(ChannelOption.SO_BACKLOG,1024*10)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>(){

                    @Override
                    protected void initChannel(SocketChannel channel) {
                        // 获取管道，可以添加各种各样 ChannelHandlerAdapter,执行按先后顺序
                        channel.pipeline()
                                // 按'\n' 结尾为一个数据包的编码器
                                .addLast(new LineBasedFrameDecoder(1024))
                                // 字符串解码器
                                .addLast(new StringDecoder())
                                // 接受请求数据的处理器
                                .addLast(new ChannelHandlerAdapter(){
                                    int count = 0;
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        //前面配了StringDecoder，到这里可以直接使用String
                                        String request = (String) msg;
                                        System.out.println("服务器收到请求：\n"+request);

                                        // 这里组装了一个有101个换行符的字符串，在客户端LineBasedFrameDecoder时
                                        // 会被当做101个包处理，也就是会触发 101次 channelRead 方法
                                        StringBuilder stringBuilder = new StringBuilder();
                                        String response = "服务器已经收到响应了,将回复100行数据\n";
                                        stringBuilder.append(response);
                                        stringBuilder.append("服务器返回的第一行数据").append(count++).append("\n");
                                        //返回数据
                                        ByteBuf resp = Unpooled.copiedBuffer(stringBuilder.toString().getBytes());
                                        ctx.writeAndFlush(resp);
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

        try {
            // 绑定端口
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            // 关闭异步阻塞
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
