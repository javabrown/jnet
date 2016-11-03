package com.jbrown.jnet.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
 
import com.jbrown.jnet.response.ResponseI;
import com.jbrown.jnet.utils.KeysI;
//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.IO;

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
  
  public List<String> getHeaders() {
    List<String> list = null;
    
    if(_io.getHttpRequestData() != null){
       list = _io.getHttpRequestData().getHeader();
    }

    return list;
  }
  
  public String getBody() {
    String body = null;

    if (_io.getHttpRequestData() != null) {
      body = _io.getHttpRequestData().getBody();
    }

    return body;
  }

  interface SocketIOI {
    String readInput() throws Exception;
    void writeOutput(String output) throws Exception;
    HttpRequestData getHttpRequestData();
    void close() throws Exception;
  }
 
}

//
//class ObjectStream implements SocketIO.SocketIOI {
//  private ObjectOutputStream _writer = null;
//  private ObjectInputStream _reader = null;
//
//  public ObjectStream(Socket csocket) throws IOException{
//     _writer = new ObjectOutputStream(csocket.getOutputStream());
//     _reader = new ObjectInputStream(csocket.getInputStream());
//  }
//
//  @Override
//  public String readInput() throws Exception {
//    Object wireRowData =  _reader.readObject();
//    return (String) wireRowData;
//  }
//
//  @Override
//  public void writeOutput(String output) throws Exception {
//    _writer.writeObject(String.format("\r%s\r", output));
//    _writer.flush();
//  }
//
//  @Override
//  public void close() throws Exception {
//    _writer.close();
//    _reader.close();
//  }
//  
//  @Override
//  public HttpRequestData getHttpRequestData() {
//    return null;
//  }
//}


class CharStream implements SocketIO.SocketIOI {
  private PrintStream _writer = null;
  private BufferedReader _reader = null;
  private HttpRequestData _httpRequestData = null;
  
  public CharStream(Socket csocket) throws IOException{
     _reader = new BufferedReader(
         new InputStreamReader(csocket.getInputStream(), KeysI.UTF_8));

     _writer = new PrintStream(csocket.getOutputStream(), true);
     
     _httpRequestData = getHttpRequestData0();
  }

  @Override
  public String readInput() throws Exception {
    String wireData = _httpRequestData.getHeader().get(0);
    return wireData;
  }

  @Override
  public HttpRequestData getHttpRequestData() {
    return _httpRequestData;
  }
  
  private HttpRequestData getHttpRequestData0()  {
    BufferedReader in = _reader;

    String line;
    List<String> headers = new LinkedList<>();
    StringBuilder body = null;
    try {
      while ( (line = in.readLine()) != null) {
        if(line == null || line.isEmpty()){
           break;  
        }
        
        // - Here we test if we've reach the body part.
        if (line.isEmpty() && body == null) {
          body = new StringBuilder();
          break;
        }
        if (body != null) {
          body.append(line).append('\n');
        } else {
          headers.add(line);
        }

      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.out.println("--- Headers ---");
    for (String h : headers) {
      System.out.println(h);
    }
    System.out.println("--- Body ---");
    System.out.println(body != null ? body.toString() : "");
    
    return new HttpRequestData(headers, body);
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
