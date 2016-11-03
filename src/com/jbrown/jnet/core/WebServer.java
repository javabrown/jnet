package com.jbrown.jnet.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.jbrown.jnet.utils.Utils;

public class WebServer {

  
  public static void main(String[] args) throws Exception {

    int port = 1989;
    ServerSocket serverSocket = new ServerSocket(port);
    System.err.println("Server launched on port : " + port);

    while (true) {

      Socket clientSocket = serverSocket.accept();
      System.err.println("New connected client");

      BufferedReader in = new BufferedReader(new InputStreamReader(
          clientSocket.getInputStream()));
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
          clientSocket.getOutputStream()));

      String s;

      while ((s = in.readLine()) != null) {
        System.out.println(s);
        if (s.isEmpty()) {
          break;
        }
      }
      
      
    
      out.write("HTTP/1.0 200 OK\r\n");
      out.write("Date: Fri, 31 Dec 1999 23:59:59 GMT\r\n");
      out.write("Server: Apache/0.8.4\r\n");
      out.write("Content-Type: application/json\r\n");
      out.write("Content-Length: 59\r\n");
      out.write("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
      out.write("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
      out.write("\r\n");

      out.write(Utils.toJson("Hello World"));

      System.err.println("Connecting with the customer completed");
      out.close();
      in.close();
      clientSocket.close();
    }

  }
}
