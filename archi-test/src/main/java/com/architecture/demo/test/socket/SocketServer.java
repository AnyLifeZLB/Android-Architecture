package com.architecture.demo.test.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 模拟推送，保活验证调试
 *
 */
public class SocketServer {

    /**
     * 基于TCP协议的Socket通信，实现用户登陆 服务器端
     * @param args
     */
    public static void main(String[] args) {
        try {
            // 1.创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
            ServerSocket serverSocket = new ServerSocket(8886);
            Socket socket = null;
            // 记录客户端的数量
            int count = 0;
            System.out.println("***服务器启动***");
            // 循环监听客户端的连接
            while (true) {
                socket = serverSocket.accept();
                // 创建一个新的线程
                ServerThread serverThread = new ServerThread(socket);
                // 启动线程
                serverThread.start();
                count++;
                System.out.println("客户端数量： " + count);
                InetAddress address = socket.getInetAddress();
                System.out.println("当前客户端的IP： " + address.getHostAddress()+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}