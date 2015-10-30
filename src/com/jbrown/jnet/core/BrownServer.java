package com.jbrown.jnet.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.jbrown.jnet.utils.KeysI;
import com.jbrown.jnet.commands.Responder;

import static java.lang.String.format;

public class BrownServer implements Runnable {
  private ServerSocket _socket;
  private int _port;
  private Socket _connectionSocket;
  private Responder _responder;
  
  private static boolean _runningFlag;
  
  public BrownServer(int port) throws IOException {
    _socket = new ServerSocket(port, 50, 
        InetAddress.getByName("192.168.8.130"));
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
            _connectionSocket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
            _connectionSocket.getOutputStream()));

        String command = "";

        while (!command.equalsIgnoreCase(KeysI.QUIT) && _runningFlag) {
          writer.write(format("\n\r%s> ", KeysI.PROMPT_K));
          writer.flush();
          command = reader.readLine().trim();
          String commandResult = _responder.respond(_connectionSocket, command);

          writer.write("\n\r"+commandResult + "\n\r");
          writer.flush();
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

  public boolean isRunning() {
    return _runningFlag;
  }

  public int port() {
    return _port;
  }

  public boolean stop() {
    _runningFlag = false;
    _socket = null;
    return _runningFlag;
  }
}
