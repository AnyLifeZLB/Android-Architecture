package com.architecture.demo.test.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Android Socket 收取信息，弹框到通知栏
 * 保活验证调试，模拟网络数据传送和进程保活验证
 *
 * 参考：
 * 1 https://www.cnblogs.com/wisdo/p/5860001.html
 * 2 https://www.kancloud.cn/nov_93/java_socket/135525
 *
 */
class AndroidSocketClient {


    /**
     * 客户端
     *
     * @param args
     */
    public static void main(String[] args) {

        try {
            //1、创建客户端Socket，指定服务器地址和端口
            Socket socket = new Socket("localhost",8886);
            //2、获取输出流，向服务端发送信息
            OutputStream os = socket.getOutputStream();  //字节输出流
            PrintWriter pw = new PrintWriter(os);        //将输出流包装为打印流
            pw.write("用户名：admin； 密码：123");
            pw.flush();
            socket.shutdownOutput();                     //关闭输出流
            //3.获取输入流，并读取服务器端的响应信息
            InputStream is= socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String info = null;
            while ((info = br.readLine()) != null) {
                // 循环读取客户端的信息
                System.out.println("我是客户端，服务端说：" + info);
            }
            //4.关闭资源
            br.close();
            is.close();
            pw.close();
            os.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
