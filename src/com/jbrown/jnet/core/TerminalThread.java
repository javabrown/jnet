package com.jbrown.jnet.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import com.jbrown.jnet.commands.Responder;
import com.jbrown.jnet.response.ResponseI;
import com.jbrown.jnet.utils.KeysI;

public class TerminalThread implements WorkerThreadI {
  private String _clientThreadId;
  private Responder _responder;

  private Socket _csocket;
  private PrintStream _writer = null;
  private BufferedReader _reader = null;
  private InetAddress _clientAddress;

  private static boolean _isRunning;

  public TerminalThread(String clientThreadId, Socket csocket, Responder responder) {
     _clientThreadId = clientThreadId;
     _csocket = csocket;
     _responder = responder;
     _clientAddress = csocket.getInetAddress();
     _isRunning = false;
  }

  @Override
  public String getThreadId(){
    return _clientThreadId;
  }

  public void stop(){
    _isRunning = false;
    this.closeActivity();
  }

  public void run() {
    _isRunning = true;

    try {
       _writer = new PrintStream(_csocket.getOutputStream(), true);
       _reader = new BufferedReader(
           new InputStreamReader( _csocket.getInputStream(), KeysI.UTF_8));

       String command = "";

       while (!command.equalsIgnoreCase(KeysI.QUIT) && _isRunning) {
         _writer.printf("\n\r%s> ", KeysI.PROMPT_K1);
         _writer.flush();
         String wireData = _reader.readLine();
         command = new WireData(wireData).getCommand();

         ResponseI commandResult =
             _responder.respond(new Request(_csocket, command, null));

         this.sendResponse(_writer, commandResult);
       }
    }
    catch (IOException e) {
       System.out.println(e);
    }
    finally{
        this.closeActivity();
    }
 }

  private void closeActivity(){
    try {
      _writer.close();
      _reader.close();
      _csocket.close();
      _csocket= null;
      System.out.printf("[Socket closed on client : %s]", _clientAddress);
    } catch (Exception e) {
      e.printStackTrace();
    }finally{
      _responder.getSocketPool().unregisterClient(_clientThreadId);
    }
  }

  private void sendResponse(PrintStream out, ResponseI response) throws IOException{
    out.printf("\n\r%s\n\r", response.getResponse());
    out.flush();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((_clientThreadId == null) ? 0 : _clientThreadId.hashCode());
    result = prime * result + ((_csocket == null) ? 0 : _csocket.hashCode());
    result = prime * result + ((_reader == null) ? 0 : _reader.hashCode());
    result = prime * result
        + ((_responder == null) ? 0 : _responder.hashCode());
    result = prime * result + ((_writer == null) ? 0 : _writer.hashCode());
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
    TerminalThread other = (TerminalThread) obj;
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
    if (_reader == null) {
      if (other._reader != null)
        return false;
    } else if (!_reader.equals(other._reader))
      return false;
    if (_responder == null) {
      if (other._responder != null)
        return false;
    } else if (!_responder.equals(other._responder))
      return false;
    if (_writer == null) {
      if (other._writer != null)
        return false;
    } else if (!_writer.equals(other._writer))
      return false;
    return true;
  }
}