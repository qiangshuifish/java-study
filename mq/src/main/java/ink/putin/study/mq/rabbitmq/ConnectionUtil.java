package ink.putin.study.mq.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 呛水滴鱼
 * @version 1.0
 * @since 1.0
 */
public class ConnectionUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(ConnectionUtil.class);

    public static Connection getConnection() {
        try {
            Connection connection = null;
            //定义一个连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            //设置服务端地址（域名地址/ip）
//            factory.setHost("rabbit.putin.ink");
            factory.setHost("172.27.6.3");
            //设置服务器端口号
            factory.setPort(5672);
            //设置虚拟主机(相当于数据库中的库)
            factory.setVirtualHost("/");
            //设置用户名
            factory.setUsername("rabbitmq");
            //设置密码
            factory.setPassword("mima1234");
            connection = factory.newConnection();
            return connection;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }
}