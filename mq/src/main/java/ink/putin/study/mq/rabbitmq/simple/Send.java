package ink.putin.study.mq.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import ink.putin.study.mq.rabbitmq.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * @author 呛水滴鱼
 * @version 1.0
 * @since 1.0
 */
public class Send {
    private static Logger LOGGER = LoggerFactory.getLogger(ConnectionUtil.class);
    /**
     * 队列名称
     */
    private static final String QUEUE_NAME = "test_simple_queue";

    public static void main(String[] args) {
        try {
            //获取连接
            Connection connection = ConnectionUtil.getConnection();
            if(Objects.isNull(connection)){
                LOGGER.error("连接RabbitMQ失败");
                return;
            }
            //从连接中获取一个通道
            Channel channel = connection.createChannel();
            //声明队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "This is simple queue";
            //发送消息
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("utf-8"));
            System.out.println("[send]：" + message);
            channel.close();
            connection.close();


        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}