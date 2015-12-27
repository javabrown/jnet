package com.jbrown.jnet.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

import com.jbrown.jnet.response.ResponseI;
import com.jbrown.jnet.utils.KeysI;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.IO;

class SocketIO {
  private SocketIOI _io;

  public SocketIO(Socket csocket) throws IOException {
    //_io = new ObjectStream(csocket);
    _io = new CharStream(csocket);
  }

  public WireData read() throws Exception {
    String input = _io.readInput();
    return new WireData(input);
  }

  public void writeOutput(String output) throws Exception {
    _io.writeOutput(output);
  }

  public void close() throws Exception {
    _io.close();
  }

  interface SocketIOI {
    String readInput() throws Exception;
    void writeOutput(String output) throws Exception;
    void close() throws Exception;
  }
}


class ObjectStream implements SocketIO.SocketIOI {
  private ObjectOutputStream _writer = null;
  private ObjectInputStream _reader = null;

  public ObjectStream(Socket csocket) throws IOException{
     _writer = new ObjectOutputStream(csocket.getOutputStream());
     _reader = new ObjectInputStream(csocket.getInputStream());
  }

  @Override
  public String readInput() throws Exception {
    Object wireRowData =  _reader.readObject();
    return (String) wireRowData;
  }

  @Override
  public void writeOutput(String output) throws Exception {
    _writer.writeObject(String.format("\r%s\r", output));
    _writer.flush();
  }

  @Override
  public void close() throws Exception {
    _writer.close();
    _reader.close();
  }
}

class CharStream implements SocketIO.SocketIOI {
  private PrintStream _writer = null;
  private BufferedReader _reader = null;

  public CharStream(Socket csocket) throws IOException{
     _reader = new BufferedReader(
         new InputStreamReader(csocket.getInputStream(), KeysI.UTF_8));

     _writer = new PrintStream(csocket.getOutputStream(), true);
  }

  @Override
  public String readInput() throws Exception {
    String wireData = _reader.readLine();
    return wireData;
  }

  @Override
  public void writeOutput(String output) throws Exception {
    _writer.printf("\n\r%s\n\r", output);
    _writer.printf("\n\r%s\n\r", KeysI.END_K);
    
    _writer.printf("\n\r%s ", KeysI.PROMPT_K1);
    _writer.flush();
  }

  @Override
  public void close() throws Exception {
    _writer.close();
    _reader.close();
  }
}
