package ink.putin.study.mq.rabbitmq.multi;

import com.rabbitmq.client.*;
import ink.putin.study.mq.rabbitmq.ConnectionUtil;
import ink.putin.study.mq.rabbitmq.MsgConsumer;

import java.io.IOException;

/**
 * @author 呛水滴鱼
 * @version 1.0
 * @since 1.0
 */
public class MultiReceive {

    private final static String QUEUE_NAME = "test_queue_work";

    private int prefetchCount;

    private String name;

    public MultiReceive(int prefetchCount, String name) {
        this.prefetchCount = prefetchCount;
        this.name = name;
    }

    public static void main(String[] args) throws IOException {
        MultiReceive receive1 = new MultiReceive(1,"多对多模式消费者1");
        MultiReceive receive2= new MultiReceive(2,"多对多模式消费者2");
        MultiReceive receive3= new MultiReceive(3,"多对多模式消费者3");

        receive1.handMsg();
        receive2.handMsg();
        receive3.handMsg();
    }

    public void handMsg() throws IOException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //能者多劳模式
        channel.basicQos(prefetchCount);
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        Consumer consumer = new MsgConsumer(channel,name);
        //监听队列，当b为true时，为自动提交（只要消息从队列中获取，无论消费者获取到消息后是否成功消息，都认为是消息已经成功消费），
        // 当b为false时，为手动提交（消费者从队列中获取消息后，服务器会将该消息标记为不可用状态，等待消费者的反馈，
        // 如果消费者一直没有反馈，那么该消息将一直处于不可用状态。
        //如果选用自动确认,在消费者拿走消息执行过程中出现宕机时,消息可能就会丢失！！）
        //使用channel.basicAck(envelope.getDeliveryTag(),false);进行消息确认
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }
}
