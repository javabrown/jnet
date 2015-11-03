package com.jbrown.jnet.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import com.jbrown.jnet.utils.KeysI;
import com.jbrown.jnet.commands.Responder;

import static java.lang.String.format;

public class BrownServer implements Runnable {
  private ServerSocket _socket;
  private String _host;
  private int _port;
  private Socket _connectionSocket;
  private Responder _responder;
  
  private static boolean _runningFlag;
  
  public BrownServer(String host, int port) throws IOException {
    _socket = new ServerSocket(port, 50, InetAddress.getByName(host));
    _host = host;
    _port = port;
    _runningFlag = false;
    _responder = new Responder();
  }

  @Override
  public void run() {
    _runningFlag = true;

    while (_runningFlag) {
      try {
        _connectionSocket = _socket.accept();
        System.out.println(_connectionSocket.getLocalSocketAddress().toString());

        BufferedReader reader = new BufferedReader(new InputStreamReader(
            _connectionSocket.getInputStream(), KeysI.UTF_8));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
            _connectionSocket.getOutputStream(), KeysI.UTF_8));

        String command = "";

        while (!command.equalsIgnoreCase(KeysI.QUIT) && _runningFlag) {
          out.write(format("\n\r%s> ", KeysI.PROMPT_K));
          out.flush();
          command = reader.readLine().trim();
          
          //out.write(format("=====\n\r%s> ====\n\r", command));
          //out.flush();
          
          String commandResult = _responder.respond(_connectionSocket, command);

          sendResponse(out, commandResult);
        }

      } catch (Exception ex) {
        ex.printStackTrace();
      } finally {
        try {
          _connectionSocket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }

        System.out.printf("[Socket closed on port : %s]", _port);
      }
    }
  }

  private void sendResponse(BufferedWriter out, String commandResult) throws IOException{
//    out.write("HTTP/1.0 200 OK\r\n");
//    out.write(format("Date: %s\r\n", new Date().toString()));
//    out.write("Server: J/0.8.4\r\n");
//    out.write("Content-Type: text/html\r\n");
//    out.write("Content-Length: 59\r\n");
//    out.write("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
//    out.write(format("Last-modified: %s\r\n", new Date().toString()));
    out.write(format("\n\r%s\n\r", commandResult));
    out.flush();
  }
  
  public boolean isRunning() {
    return _runningFlag;
  }

  public int port() {
    return _port;
  }

  public boolean stop() {
    try {
      _socket.close();
      _connectionSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    _runningFlag = false;
    _socket = null;
    return _runningFlag;
  }
}
