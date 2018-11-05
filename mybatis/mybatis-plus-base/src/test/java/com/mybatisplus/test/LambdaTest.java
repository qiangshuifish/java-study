package com.mybatisplus.test;

import org.apache.tomcat.util.http.fileupload.util.Streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p></p>
 *
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author wenpengyuan
 * @version 1.0
 * @since 1.0
 */
public class LambdaTest {
    public static void main(String[] args) {
        String ss = "Hello";

        String[] aa = ss.split("");

        String[] bb = {"H", "e", "l", "l", "o"};


        String[] strings = {"Hello", "World"};

        //Arrays.stream接收一个数组返回一个流
        List<Stream<String>> streamList = Arrays.asList(strings).stream().
                map(str -> str.split("")).
                map(str -> Arrays.stream(str)).
                collect(Collectors.toList());
        //分步写(map)

        Stream<String[]> stream = Arrays.asList(strings).stream().
                map(str -> str.split(""));

        Stream<Stream<String>> streamStream = stream.map(strings1 -> Arrays.stream(strings1));
        List<Stream<String>> streamList1 = streamStream.collect(Collectors.toList());


        List<String> stringList = Arrays.asList(strings).stream().
                map(str -> str.split("")).
                flatMap(str -> Arrays.stream(str))
                .collect(Collectors.toList());


        //分步写(流只能消费一次)(flatMap)
        Stream<String[]> stream1 = Arrays.asList(strings).stream().
                map(str -> str.split(""));

        Stream<String> stringStream = stream1.flatMap(strings1 -> Arrays.stream(strings1));

        List<String> stringList1 = stringStream.collect(Collectors.toList());

    }
}
