package xin.qiangshuidiyu.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 阻塞的io
 */
public class BioServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("服务启动端口：" + port);

        try {
            Socket socket = null;
            while (true) {
                socket = serverSocket.accept();
                new Thread(new BioServerHandler(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
            System.out.println("服务停止");
        }

    }
}
