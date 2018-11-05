package xin.qiangshuidiyu.netty.test;

import org.junit.Test;
import org.msgpack.MessagePack;
import org.msgpack.type.MapValue;
import org.msgpack.type.Value;
import xin.qiangshuidiyu.netty.common.Person;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestMessagePack {

    @Test
    public void test() throws IOException {
        MessagePack msgpack = new MessagePack();

        Map<String,String> map = new HashMap<>();
        map.put("key1","value1");
        map.put("key2","value1");
        map.put("key3","value1");
        map.put("key4","value1");
        map.put("key5","value1");

        byte[] write = msgpack.write(map);

        Value read = msgpack.read(write);
        System.out.println(read);

        MapValue mapValue = read.asMapValue();
        System.out.println(mapValue);


        Person person = new Person("张三",18,"15912345678","213012387@qq.com");
        byte[] personByte = msgpack.write(person);

        Value read1 = msgpack.read(personByte);

        System.out.println(read1);

        System.out.println(read1.getType());
    }
}
