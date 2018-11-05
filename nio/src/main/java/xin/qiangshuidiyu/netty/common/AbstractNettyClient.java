package xin.qiangshuidiyu.netty.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

abstract public class AbstractNettyClient {

    public void startClient(String host,int ports){
        NioEventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        AbstractNettyClient client = this;
        try {
            ChannelFuture channelFuture = bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            client.initChannel(socketChannel);
                        }
                    }).connect(host, ports).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            group.shutdownGracefully();
        }
    }

    abstract protected void initChannel(SocketChannel socketChannel) throws Exception;
}
