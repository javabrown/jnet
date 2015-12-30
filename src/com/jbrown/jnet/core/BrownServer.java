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

  //private ThreadPoolExecutor _threadExecutor;
  //private Map<String, WorkerThread> _workerThreadMap;
   private int _clientThreadIndex;

  private static boolean _runningFlag;

  public BrownServer(String host, int port, Responder responder) throws IOException {
    //_serverSocket = new ServerSocket(port, 50, InetAddress.getByName(host));
    _serverSocketChannel = ServerSocketChannel.open();

    _serverSocketChannel.configureBlocking(true);
    _serverSocketChannel.socket().bind(
        new InetSocketAddress(InetAddress.getByName(host), port));
    _host = host;
    _port = port;
    _runningFlag = false;
    _responder = responder;

    //_threadExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    //_workerThreadMap = new HashMap<String, WorkerThread>();
    _clientThreadIndex = 0;
  }

  @Override
  public void run() {
    _responder.initializeSocPool();
    _runningFlag = true;

    try {

      while (_runningFlag) {
        try {
          //Socket clientSocket = _serverSocket.accept();
          SocketChannel clientChannel = _serverSocketChannel.accept();
          Socket clientSocket = clientChannel.socket();

          System.out.println(clientSocket.getLocalSocketAddress().toString());

          String clientThreadId = format("t-%s", _clientThreadIndex++);
          WorkerThread clientThread = new WorkerThread(clientThreadId,
              clientSocket, _responder);

          //_workerThreadMap.put(clientThreadId, clientThread);
          //_threadExecutor.execute(clientThread);
          _responder.getSocketPool().registerClient(clientThread);
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
        finally {

        }
      }

    }
    finally {
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

//class WorkerThread implements Runnable {
//  private String _clientThreadId;
//  private Responder _responder;
//
//  private Socket _csocket;
//  private PrintStream _writer = null;
//  private BufferedReader _reader = null;
//
//  private static boolean _isRunning;
//
//  public WorkerThread(String clientThreadId, Socket csocket, Responder responder) {
//     _clientThreadId = clientThreadId;
//     _csocket = csocket;
//     _responder = responder;
//     _isRunning = false;
//  }
//
//  public String getThreadId(){
//    return _clientThreadId;
//  }
//
//  public void stop(){
//    _isRunning = false;
//    this.closeActivity();
//  }
//
//  public void run() {
//    _isRunning = true;
//
//
//    try {
//       _writer = new PrintStream(_csocket.getOutputStream());
//       _reader = new BufferedReader(
//           new InputStreamReader( _csocket.getInputStream(), KeysI.UTF_8));
//
//       String command = "";
//
//       while (!command.equalsIgnoreCase(KeysI.QUIT) || _isRunning) {
//         _writer.printf("\n\r%s> ", KeysI.PROMPT_K);
//         _writer.flush();
//         command = _reader.readLine().trim();
//
//         String commandResult =
//             _responder.respond(new Request(_csocket, command));
//
//         this.sendResponse(_writer, commandResult);
//       }
//    }
//    catch (IOException e) {
//       System.out.println(e);
//    }
//    finally{
//        this.closeActivity();
//    }
// }
//
//  private void closeActivity(){
//    try {
//      _writer.close();
//      _reader.close();
//      _csocket.close();
//      _csocket= null;
//      System.out.printf("[Socket closed on client : %s]",
//          _csocket.getInetAddress());
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  private void sendResponse(PrintStream out, String commandResult) throws IOException{
//    out.printf("\n\r%s\n\r", commandResult);
//    out.flush();
//  }
//}