package ink.putin.study.mq.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author
 * @version 1.0
 * @since 1.0
 */
public class TestRabbitMQ {


    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = null;
        //定义一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务端地址（域名地址/ip）
        factory.setHost("rabbit.putin.ink");
        //设置服务器端口号
        factory.setPort(5672);
        //设置虚拟主机(相当于数据库中的库)
        factory.setVirtualHost("/");
        //设置用户名
        factory.setUsername("rabbitmq");
        //设置密码
        factory.setPassword("mima1234");
        connection = factory.newConnection();

        Map<String, Object> serverProperties = connection.getServerProperties();
        System.out.println(serverProperties);
        System.out.println(connection);
    }
}
