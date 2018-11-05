package xin.qiangshuidiyu.netty.test;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.WeakHashMap;

public class TestJava {
    public static void main(String[] args) {
        String str = "Hello World";
        ByteBuffer allocate = ByteBuffer.allocate(str.getBytes().length);
        allocate.flip();
        int length = allocate.array().length;

        ByteBuffer wrap = ByteBuffer.wrap(str.getBytes());
//        wrap.put(str.getBytes());
        wrap.flip();
        int length1 = wrap.array().length;

        System.out.println(length);
        System.out.println(length1);
    }

    @Test
    public void test(){
        Integer integer1 = 0;
        Integer integer2 = 0;

        System.out.println(integer1 == integer2);

        Map<String,String> map = new WeakHashMap<>();
    }


    @Test
    public void testString(){
    }
}
