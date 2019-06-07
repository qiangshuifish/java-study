package ink.putin.study.mq.rabbitmq.topics;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import ink.putin.study.mq.rabbitmq.ConnectionUtil;
import org.springframework.amqp.core.ExchangeTypes;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 呛水滴鱼
 * @version 1.0
 * @since 1.0
 */
public class Publish {

    /**
     * 交换机名称
     */
    private static final String EXCHANGE_NAME = "test_exchange_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();
        /*
            声明exchange交换机
            参数1：交换机名称
            参数2：交换机类型
            参数3：交换机持久性，如果为true则服务器重启时不会丢失
            参数4：交换机在不被使用时是否删除
            参数5：交换机的其他属性
         */
        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypes.TOPIC, false, false, null);


        String routingKey1 = "user.*";
        String routingKey2 = "user.#";
        String routingKey3 = "user.list";
        String routingKey4 = "user.list.1";
        String routingKey5 = "user.list.1.10";
        String routingKey6 = "user";

        String message1 = "TOPIC 路由消息 " + routingKey1;
        String message2 = "TOPIC 路由消息 " + routingKey2;
        String message3 = "TOPIC 路由消息 " + routingKey3;
        String message4 = "TOPIC 路由消息 " + routingKey4;
        String message5 = "TOPIC 路由消息 " + routingKey5;
        String message6 = "TOPIC 路由消息 " + routingKey6;

        channel.basicPublish(EXCHANGE_NAME, routingKey1, null, message1.getBytes("utf-8"));
        channel.basicPublish(EXCHANGE_NAME, routingKey2, null, message2.getBytes("utf-8"));
        channel.basicPublish(EXCHANGE_NAME, routingKey3, null, message3.getBytes("utf-8"));
        channel.basicPublish(EXCHANGE_NAME, routingKey4, null, message4.getBytes("utf-8"));
        channel.basicPublish(EXCHANGE_NAME, routingKey5, null, message5.getBytes("utf-8"));
        channel.basicPublish(EXCHANGE_NAME, routingKey6, null, message5.getBytes("utf-8"));

        System.out.println("Publish send ：" + message1);
        System.out.println("Publish send ：" + message2);
        System.out.println("Publish send ：" + message3);
        System.out.println("Publish send ：" + message4);
        System.out.println("Publish send ：" + message5);
        System.out.println("Publish send ：" + message6);
        channel.close();
        connection.close();
    }
}
