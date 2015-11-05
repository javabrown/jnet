package com.jbrown.jnet.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jbrown.jnet.utils.KeysI;
import com.jbrown.jnet.commands.Responder;

import static java.lang.String.format;

public class BrownServer implements Runnable {
  private ServerSocket _serverSocket;
  private String _host;
  private int _port;
  private Responder _responder;
  
  private ThreadPoolExecutor _threadExecutor;
  private Map<String, ClientThread> _clientThreadMap; 
  private int _clientThreadIndex;
  
  private static boolean _runningFlag;

  public BrownServer(String host, int port, Responder responder) throws IOException {
    _serverSocket = new ServerSocket(port, 50, InetAddress.getByName(host));
    _host = host;
    _port = port;
    _runningFlag = false;
    _responder = responder;
    
    _threadExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    _clientThreadMap = new HashMap<String, ClientThread>();
    _clientThreadIndex = 0;
  }

  @Override
  public void run() {
    _runningFlag = true;
    try {

      while (_runningFlag) {
        try {
          Socket clientSocket = _serverSocket.accept();
          System.out.println(clientSocket.getLocalSocketAddress().toString());
          
          String clientThreadId = format("client-thread-%s", _clientThreadIndex++);
          ClientThread clientThread = new ClientThread(clientThreadId,
              clientSocket, _responder);
          
          _clientThreadMap.put(clientThreadId, clientThread);
          _threadExecutor.execute(clientThread);
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

  public boolean isRunning() {
    return _runningFlag;
  }

  public int port() {
    return _port;
  }

  public boolean stop() {
    try {
      _runningFlag = false;
      
      for(ClientThread client : _clientThreadMap.values()){
        client.stop();
        client = null;
        System.out.printf("Client thread %s stopped.", 
            client.getThreadId());
      }
      
      _serverSocket.close();
      _threadExecutor.shutdown();
      
      JOptionPane.showMessageDialog(new JFrame(), "Server Stopped");
    } catch (IOException e) {
      e.printStackTrace();
    }

    return _runningFlag;
  }
}

class ClientThread implements Runnable {
  private String _clientThreadId;
  private Socket _csocket;
  private Responder _responder;
  
  private static boolean _isRunning; 
  
  public ClientThread(String clientThreadId, Socket csocket, Responder responder) {
     _clientThreadId = clientThreadId;
     _csocket = csocket;
     _responder = responder;
     _isRunning = false;
  }

  public String getThreadId(){
    return _clientThreadId;
  }
  
  public void stop(){
    _isRunning = false;
  }
  
  public void run() {
    _isRunning = true;
    PrintStream pstream = null;
    BufferedReader reader = null;

    try {
       pstream = new PrintStream(_csocket.getOutputStream());
       reader = new BufferedReader(
           new InputStreamReader( _csocket.getInputStream(), KeysI.UTF_8));

       String command = "";

       while (!command.equalsIgnoreCase(KeysI.QUIT) || _isRunning) {
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