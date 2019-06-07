package ink.putin.study.mq.rabbitmq.multi;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import ink.putin.study.mq.rabbitmq.ConnectionUtil;

/**
 * @author 呛水滴鱼
 * @version 1.0
 * @since 1.0
 */
public class MultiProducer {
    private final static String QUEUE_NAME = "test_queue_work";

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        for (int i = 0; i < 5000; i++) {
            String message = "" + i;
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("推送第 "+message+" 条消息");
//            Thread.sleep(Math.round(Math.random()*10));
        }
        channel.close();
        connection.close();
    }
}
