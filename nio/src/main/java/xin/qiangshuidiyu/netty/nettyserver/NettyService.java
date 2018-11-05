package xin.qiangshuidiyu.netty.nettyserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyService {
    public static void main(String[] args) throws InterruptedException {
        // 两个工作线程组，一个用于网络连接，一个网络读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 辅助类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    // 类型为NioServerSocketChannel，ServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());
            //绑定端口
            ChannelFuture future = serverBootstrap.bind(8080).sync();
            //阻塞 到这里
            future.channel().closeFuture().sync();

        }finally {
            //异常退出
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
