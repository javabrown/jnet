package com.jbrown.jnet.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.jbrown.jnet.utils.KeysI;
import com.jbrown.jnet.commands.Responder;

import static java.lang.String.format;

public class BrownServer implements Runnable {
  private ServerSocket _serverSocket;
  private String _host;
  private int _port;
  private Responder _responder;
  private ThreadPoolExecutor _threadExecutor;


  private static boolean _runningFlag;

  public BrownServer(String host, int port, Responder responder) throws IOException {
    _serverSocket = new ServerSocket(port, 50, InetAddress.getByName(host));
    _host = host;
    _port = port;
    _runningFlag = false;
    _responder = responder;
    _threadExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
  }

  @Override
  public void run() {
    _runningFlag = true;
    try {

      while (_runningFlag) {
        try {
          Socket clientSocket = _serverSocket.accept();
          System.out.println(clientSocket.getLocalSocketAddress().toString());
          _threadExecutor.execute(new ServerThread(clientSocket, _responder));
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
        finally {

        }
      }

    }
    finally {
      _threadExecutor.shutdownNow();
    }
  }

//  private void sendResponse(BufferedWriter out, String commandResult) throws IOException{
////    out.write("HTTP/1.0 200 OK\r\n");
////    out.write(format("Date: %s\r\n", new Date().toString()));
////    out.write("Server: J/0.8.4\r\n");
////    out.write("Content-Type: text/html\r\n");
////    out.write("Content-Length: 59\r\n");
////    out.write("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
////    out.write(format("Last-modified: %s\r\n", new Date().toString()));
//    out.write(format("\n\r%s\n\r", commandResult));
//    out.flush();
//  }

  public boolean isRunning() {
    return _runningFlag;
  }

  public int port() {
    return _port;
  }

  public boolean stop() {
    try {
      _runningFlag = false;

      _serverSocket.close();
      _threadExecutor.shutdown();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return _runningFlag;
  }
}

class ServerThread implements Runnable {
  private Socket _csocket;
  private Responder _responder;

  public ServerThread(Socket csocket, Responder responder) {
     _csocket = csocket;
     _responder = responder;
  }

  public void run() {
    PrintStream pstream = null;
    BufferedReader reader = null;

    try {
       pstream = new PrintStream(_csocket.getOutputStream());
       reader = new BufferedReader(
           new InputStreamReader( _csocket.getInputStream(), KeysI.UTF_8));

       String command = "";

       while (!command.equalsIgnoreCase(KeysI.QUIT)) {
         pstream.printf("\n\r%s> ", KeysI.PROMPT_K);
         pstream.flush();
         command = reader.readLine().trim();

         String commandResult =
             _responder.respond(new Request(_csocket, command));

         this.sendResponse(pstream, commandResult);
       }
    }
    catch (IOException e) {
       System.out.println(e);
    }
    finally{
      try {
        pstream.close();
        reader.close();
        _csocket.close();

        System.out.printf("[Socket closed on client : %s]",
            _csocket.getInetAddress());
      } catch (Exception e) {
        e.printStackTrace();
      }

      //System.out.printf("[Client Socket closed for client : %s]",
      //      _csocket.getLocalSocketAddress().toString());
    }
 }

  private void sendResponse(PrintStream out, String commandResult) throws IOException{
    out.printf("\n\r%s\n\r", commandResult);
    out.flush();
  }
}