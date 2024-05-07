package com.architecture.demo.test.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * 服务器线程处理类
 *
 * https://www.kancloud.cn/nov_93/java_socket/135526
 */
public class ServerThread extends Thread {
   // 和本线程相关的Socket
   Socket socket = null;
   public ServerThread(Socket socket) {
      this.socket = socket;
   }

   // 线程执行的操作，响应客户端的请求
   public void run() {
      InputStream is = null;
      InputStreamReader isr = null;
      BufferedReader br = null;
      OutputStream os = null;
      PrintWriter pw = null;
      try {
         System.out.println("服务器启动新连接");


         while (true){
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String info = br.readLine();
            if (info!=null&&info.length()!=0) {
               // 循环读取客户端的信息
               System.out.println("收到客户端信息：" + info);
            }
//            socket.shutdownInput();// 关闭输入流

            // 获取输出流，响应客户端的请求
            os = socket.getOutputStream();
            pw = new PrintWriter(os);
//            pw.write("这是一条来自服务端的消息");
            pw.println("这是一条来自服务端的消息 2");
            pw.flush();// 调用flush()方法将缓冲输出

            sleep(1000);
         }


      } catch (IOException e) {
         System.out.println("IOException：" + e.toString());

      } catch (InterruptedException e) {
         System.out.println("InterruptedException：" + e.toString());
         throw new RuntimeException(e);
      } finally {
         // 关闭资源
         try {
            if (pw != null) {
               pw.close();
            }
            if (os != null) {
               os.close();
            }
            if (br != null) {
               br.close();
            }
            if (isr != null) {
               isr.close();
            }
            if (is != null) {
               is.close();
            }
            if (socket != null) {
               socket.close();
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
}