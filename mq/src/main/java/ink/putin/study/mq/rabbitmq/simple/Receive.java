package ink.putin.study.mq.rabbitmq.simple;

import com.rabbitmq.client.*;
import ink.putin.study.mq.rabbitmq.ConnectionUtil;
import ink.putin.study.mq.rabbitmq.MsgConsumer;

import java.io.IOException;

/**
 * @author 呛水滴鱼
 * @version 1.0
 * @since 1.0
 */
public class Receive {
    //队列名称
    private static final String QUEUE_NAME = "test_simple_queue";

    public static void main(String[] args) {
        try {
            //获取连接
            Connection connection = ConnectionUtil.getConnection();
            //从连接中获取一个通道
            Channel channel = connection.createChannel();
            //声明队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //定义消费者
            DefaultConsumer consumer = new MsgConsumer(channel,"一对一模式消费者");
            //监听队列
            channel.basicConsume(QUEUE_NAME, false, consumer);
        } catch (IOException | ShutdownSignalException | ConsumerCancelledException e) {
            e.printStackTrace();
        }
    }
}
