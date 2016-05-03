package com.jbrown.jnet.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import com.jbrown.jnet.commands.Responder;
import com.jbrown.jnet.response.ResponseI;
import com.jbrown.jnet.utils.KeysI;

public class WorkerThread implements Runnable {
  private String _clientThreadId;
  private Responder _responder;

  private Socket _csocket;
  //private ObjectOutputStream _writer = null;
  //private ObjectInputStream _reader = null;
  private SocketIO _socketIO;

  private InetAddress _clientAddress;

  private static boolean _isRunning;

  public WorkerThread(String clientThreadId, Socket csocket, Responder responder) {
     _clientThreadId = clientThreadId;
     _csocket = csocket;
     _responder = responder;
     _clientAddress = csocket.getInetAddress();
     _isRunning = false;
  }

  public String getThreadId(){
    return _clientThreadId;
  }

  public void stop(){
    _isRunning = false;
    this.closeActivity();
  }

  public void run() {
    _isRunning = true;
    SocketIO socketIO = null;

    try {
       //_csocket.setSoTimeout(3000000);
       //_writer = new ObjectOutputStream(_csocket.getOutputStream());
       //_reader = new ObjectInputStream(_csocket.getInputStream());
      _socketIO = new SocketIO(_csocket);

       boolean isQuitCommand = false;

       do {
         _socketIO.showPromot();
         String command = _socketIO.read().getCommand();
         isQuitCommand = command.equalsIgnoreCase(KeysI.QUIT);

         if(!isQuitCommand) {
           ResponseI result =
               _responder.respond(new Request(_csocket, command));

           _socketIO.writeOutput(String.format("\r%s\r", result.getResponse()));
         }

       } while (!isQuitCommand && _isRunning);
    }
    catch (Exception  e) {
       System.out.println(e);
    }
    finally{
        this.closeActivity();
    }
 }

  private void closeActivity(){
    try {
      _socketIO.close();
      _csocket.close();
      _csocket= null;
      System.out.printf("[Socket closed on client : %s]", _clientAddress);
    } catch (Exception e) {
      e.printStackTrace();
    }finally{
      _responder.getSocketPool().unregisterClient(_clientThreadId);
    }
  }

  private void sendResponse(SocketIO out, ResponseI response) throws Exception{
    out.writeOutput(String.format("\r%s\r", response.getResponse()));
    //out.writeObject(String.format("\r%s\r", "END"));
    //out.flush();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((_clientThreadId == null) ? 0 : _clientThreadId.hashCode());
    result = prime * result + ((_csocket == null) ? 0 : _csocket.hashCode());
    result = prime * result + ((_socketIO == null) ? 0 : _socketIO.hashCode());
    result = prime * result
        + ((_responder == null) ? 0 : _responder.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    WorkerThread other = (WorkerThread) obj;
    if (_clientThreadId == null) {
      if (other._clientThreadId != null)
        return false;
    } else if (!_clientThreadId.equals(other._clientThreadId))
      return false;
    if (_csocket == null) {
      if (other._csocket != null)
        return false;
    } else if (!_csocket.equals(other._csocket))
      return false;
    if (_socketIO == null) {
      if (other._socketIO != null)
        return false;
    } else if (!_socketIO.equals(other._socketIO))
      return false;
    if (_responder == null) {
      if (other._responder != null)
        return false;
    } else if (!_responder.equals(other._responder))
      return false;

    return true;
  }
}