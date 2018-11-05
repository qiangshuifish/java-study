package xin.qiangshuidiyu.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;
import xin.qiangshuidiyu.netty.common.Person;

import java.util.List;

/**
 * 自定义解码器
 */
public class MessagePackDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        MessagePack messagePack = new MessagePack();

        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(byteBuf.readerIndex(),bytes,0,bytes.length);

        Person person = messagePack.read(bytes, Person.class);
        list.add(person);
    }
}
