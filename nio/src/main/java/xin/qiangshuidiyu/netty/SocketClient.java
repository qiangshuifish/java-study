package xin.qiangshuidiyu.netty;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 使用buff类缓冲流，直接使用流时会导致阻塞卡死
 */
public class SocketClient {
    public static void main(String[] args) {
        int port = 8080;
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket("127.0.0.1", port);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("你好服务器");
            System.out.println("已经向服务器发送数据");
            String resp = in.readLine();
            System.out.println("接受服务器数据 : " + resp);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
