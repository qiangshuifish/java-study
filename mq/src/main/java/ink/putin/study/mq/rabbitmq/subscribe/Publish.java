package ink.putin.study.mq.rabbitmq.subscribe;

import com.rabbitmq.client.AMQP;
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
    private static final String EXCHANGE_NAME = "test_exchange_fanout";

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
        channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypes.FANOUT, false,false,null);

        String message = "订阅消息";
        channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes("utf-8"));
        System.out.println("Publish send ："+message);
        channel.close();
        connection.close();
    }
}
