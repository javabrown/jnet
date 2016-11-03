package com.jbrown.jnet.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jbrown.jnet.commands.Responder;

import static java.lang.String.format;

public class BrownServer implements Runnable {
  private ServerSocketChannel _serverSocketChannel;
  private String _host;
  private int _port;
  private Responder _responder;

  // private ThreadPoolExecutor _threadExecutor;
  // private Map<String, WorkerThread> _workerThreadMap;
  private int _clientThreadIndex;

  private static boolean _runningFlag;

  public BrownServer(String host, int port, Responder responder)
      throws IOException {
    // _serverSocket = new ServerSocket(port, 50, InetAddress.getByName(host));
    _serverSocketChannel = ServerSocketChannel.open();

    _serverSocketChannel.configureBlocking(true);
    _serverSocketChannel.socket().bind(
        new InetSocketAddress(InetAddress.getByName(host), port));
    _host = host;
    _port = port;
    _runningFlag = false;
    _responder = responder;

    // _threadExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    // _workerThreadMap = new HashMap<String, WorkerThread>();
    _clientThreadIndex = 0;
  }

  @Override
  public void run() {
    _responder.initializeSocPool();
    _runningFlag = true;

    try {

      while (_runningFlag) {
        try {
          // Socket clientSocket = _serverSocket.accept();
          SocketChannel clientChannel = null;

          try {
            clientChannel = _serverSocketChannel.accept();
          } catch (IOException e) {
            if (!_runningFlag) {
              System.out.println("Server Stopped.");
              return;
            }
            throw new RuntimeException("Error accepting client connection", e);
          }

          Socket clientSocket = clientChannel.socket();

          System.out.println(clientSocket.getLocalSocketAddress().toString());

          String clientThreadId = format("t-%s", _clientThreadIndex++);

          // WorkerThread clientThread = new WorkerThread(clientThreadId,
          // clientSocket, _responder);

          WorkerThreadI clientThread = null;
          boolean isTelnetTerminal = false; // TODO - option to be added later
                                            // in UI

          if (isTelnetTerminal) {
            clientThread = new TerminalThread(clientThreadId, clientSocket,
                _responder);
          } else {
            clientThread = new WorkerThread(clientThreadId, clientSocket,
                _responder);
          }

          // _workerThreadMap.put(clientThreadId, clientThread);
          // _threadExecutor.execute(clientThread);
          _responder.getSocketPool().registerClient(clientThread);
        } catch (Exception ex) {
          ex.printStackTrace();
        } finally {

        }
      }

    } finally {
      _responder.getSocketPool().getThreadPoolExecutor().shutdownNow();
    }
  }

  public boolean isRunning() {
    return _runningFlag;
  }

  public String getHost() {
    return _host;
  }

  public int getPort() {
    return _port;
  }

  public boolean stop() {
    try {
      _responder.getSocketPool().shutdown();
      _runningFlag = false;
      _serverSocketChannel.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return _runningFlag;
  }
}