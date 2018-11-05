package xin.qiangshuidiyu.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BioServerHandler implements Runnable {

    private Socket socket;

    public BioServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                in = new BufferedReader(new InputStreamReader(
                        this.socket.getInputStream()));
                out = new PrintWriter(this.socket.getOutputStream(), true);
                while (true) {
                    String body = in.readLine();
                    if (body == null)
                        break;
                    System.out.println("服务器收到客服端数据：" + body);
                    String res = "客服端你好！收到的数据为｛" + body + "｝";
                    out.println(res);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
