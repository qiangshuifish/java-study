package ink.putin.study.mq.rabbitmq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 呛水滴鱼
 * @version 1.0
 * @since 1.0
 */
public class JavaTest {
    public static void main(String[] args) {
        String str = "===== 消费者 user.list.1 ：消费第 1 条 ==== TOPIC 路由消息 user.list.1 \n" +
                "===== 消费者 user.# ：消费第 1 条 ==== TOPIC 路由消息 user.* \n" +
                "===== 消费者 user.list ：消费第 1 条 ==== TOPIC 路由消息 user.list \n" +
                "===== 消费者 user.list.1.10 ：消费第 1 条 ==== TOPIC 路由消息 user.list.1.10 \n" +
                "===== 消费者 user.* ：消费第 1 条 ==== TOPIC 路由消息 user.* \n" +
                "===== 消费者 user.# ：消费第 2 条 ==== TOPIC 路由消息 user.# \n" +
                "===== 消费者 user.* ：消费第 2 条 ==== TOPIC 路由消息 user.# \n" +
                "===== 消费者 user.* ：消费第 3 条 ==== TOPIC 路由消息 user.list \n" +
                "===== 消费者 user.# ：消费第 3 条 ==== TOPIC 路由消息 user.list \n" +
                "===== 消费者 user.* ：消费第 4 条 ==== TOPIC 路由消息 user.list.1 \n" +
                "===== 消费者 user.# ：消费第 4 条 ==== TOPIC 路由消息 user.list.1 \n" +
                "===== 消费者 user.* ：消费第 5 条 ==== TOPIC 路由消息 user.list.1.10 \n" +
                "===== 消费者 user.# ：消费第 5 条 ==== TOPIC 路由消息 user.list.1.10 \n" +
                "===== 消费者 user.* ：消费第 6 条 ==== TOPIC 路由消息 user.list.1.10 \n" +
                "===== 消费者 user.# ：消费第 6 条 ==== TOPIC 路由消息 user.list.1.10 ";
        List<String> list = new ArrayList<>(20);
        for (String msg : str.split("\n")) {
            list.add(msg);
        }

        Collections.sort(list);
        for (String s : list) {
            System.out.println(s);
        }

    }
}
