package com.mybatisplus.test;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

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
public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch count = new CountDownLatch(2);


        Thread t1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName()+":开始等待"+count.getCount());
                count.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+":结束等待"+count.getCount());
        },"t1");
        Thread t2 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName()+":开始等待"+count.getCount());
                count.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+":结束等待"+count.getCount());
        },"t2");

        t1.start();
        count.countDown();
        Thread.sleep(1000);
        t2.start();
        count.countDown();
        Thread.sleep(1000);
        System.out.println("=====================");
    }
}
