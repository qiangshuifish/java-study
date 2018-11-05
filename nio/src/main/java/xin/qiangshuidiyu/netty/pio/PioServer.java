package xin.qiangshuidiyu.netty.pio;

import xin.qiangshuidiyu.netty.bio.BioServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * BIO + 线程池
 */
public class PioServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("服务启动端口：" + port);

        try {
            Socket socket = null;
            while (true) {
                socket = serverSocket.accept();
                ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 200,
                        5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(500));
                executor.submit(new BioServerHandler(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
            System.out.println("服务停止");
        }

    }
}
