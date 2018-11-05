package com.mybatisplus.test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

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
public class CyclicBarrierTest {



    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        Thread t1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName()+":开始等待"+cyclicBarrier.getNumberWaiting());
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+":结束等待"+cyclicBarrier.getNumberWaiting());
        },"t1");
        Thread t2 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName()+":开始等待"+cyclicBarrier.getParties());
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+":结束等待"+cyclicBarrier.getParties());
        },"t2");

        t1.start();
        t2.start();
        Thread.sleep(1000);
        System.out.println("===================");
    }
}
