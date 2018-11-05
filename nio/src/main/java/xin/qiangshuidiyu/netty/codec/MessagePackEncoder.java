package xin.qiangshuidiyu.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;
import xin.qiangshuidiyu.netty.common.Person;

/**
 * 自定义编码器
 */
public class MessagePackEncoder extends MessageToByteEncoder<Person> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Person msg, ByteBuf out) throws Exception {
        MessagePack messagePack = new MessagePack();

        byte[] write = messagePack.write(msg);
        out.writeBytes(write);
    }
}
