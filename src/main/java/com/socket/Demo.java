package com.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xuhandong on 17-9-26.
 */
public class Demo {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        while(true) {
            System.out.println("=======");
            Socket accept = serverSocket.accept();
            InputStream inputStream = accept.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String s = br.readLine();
            System.out.println(s);
            OutputStream outputStream = accept.getOutputStream();
            PrintWriter pw = new PrintWriter(outputStream);
            pw.write("abccc");
            pw.flush();
            pw.close();
            br.close();
            inputStream.close();
            accept.close();
            accept.close();
        }
    }
}
