package com.jbrown.jnet.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jbrown.jnet.client.framework.Callback;
import com.jbrown.jnet.client.framework.ClientApp;
import com.jbrown.jnet.client.framework.SimpleTask;
import com.jbrown.jnet.client.framework.Task;
import com.jbrown.jnet.utils.KeysI;
import com.jbrown.jnet.utils.StringUtils;

public class ClientSocket {
  private String _host;
  private int _port;

  private Socket _socket;
  private JFrame _frame;

  private PrintStream _socWriter;
  private BufferedReader _socReader;

  //private static boolean _threadRunnerFlag;
  
  public ClientSocket(String host, int port, JFrame frame) {
    _host = host;
    _port = port;
    _frame = frame;
    _socket = null;
    //_threadRunnerFlag = false;
  }
  
  public void open(){
    this.openSocket();
    this.openStream();
  }
  
  public void close(){
    this.closeSocket();
  }
  
  public synchronized String executeCommand(String cmd) throws IOException {
    StringBuilder builder = new StringBuilder();
    String aux = "";

    _socWriter.printf("%s\r", cmd);

    while ((aux = _socReader.readLine()) != null && _socket != null) {
      String resp = aux.trim();

      if (resp.equalsIgnoreCase(KeysI.END_K)) {
         break;
      }

      if (StringUtils.isEmpty(resp) || resp.equalsIgnoreCase(KeysI.PROMPT_K1)) {
        continue;
      }

      builder.append(aux);
    }

    return builder.toString();
  }
  
  private boolean openSocket() {
    try {
      _socket = new Socket(_host, _port);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(_frame, e.getMessage());
      e.printStackTrace();
      return false;
    }

    return true;
  }

  private boolean openStream() {
    try {
      _socWriter = new PrintStream(_socket.getOutputStream(), true);
      _socReader = new BufferedReader(new InputStreamReader(
          _socket.getInputStream(), "UTF-8"));
    } catch (IOException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(_frame, e.getMessage());
      return false;
    }

    return true;
  }

  private boolean closeSocket() {
    try {
      this.closeStream();
      _socket.close();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(_frame, e.getMessage());
      e.printStackTrace();
      return false;
    }
    finally{
      _socket = null;
    }
    
    return true;
  }

  private boolean closeStream() {
    try {
      _socWriter.close();
      _socReader.close();

      _socWriter = null;
      _socReader = null;
    } catch (IOException e) {
      JOptionPane.showMessageDialog(_frame, e.getMessage());
      e.printStackTrace();
      return false;
    }

    return true;
  }

  
  
//  public synchronized String executeCommand(String cmd) throws IOException {
//    StringBuilder builder = new StringBuilder();
//    String aux = "";
//
//    _socWriter.printf("%s\r", cmd);
//
//    while ((aux = _socReader.readLine()) != null && _socket != null) {
//      String resp = aux.trim();
//
//      if (resp.equalsIgnoreCase(KeysI.END_K)) {
//        Task task = new SimpleTask(builder.toString());
//        task.execute();
//
//        builder = new StringBuilder();
//        _socWriter.printf("%s\r", cmd);
//      }
//
//      if (StringUtils.isEmpty(resp) || resp.equalsIgnoreCase(KeysI.PROMPT_K1)) {
//        continue;
//      }
//
//      builder.append(aux);
//    }
//
//    return builder.toString();
//  }
  
//  private String read(BufferedReader reader) throws IOException {
//    StringBuilder builder = new StringBuilder();
//    String aux = "";
//
//    while ((aux = reader.readLine()) != null) {
//      String resp = aux.trim();
//      if (resp.equalsIgnoreCase(KeysI.END_K)) {
//        break;
//      }
//
//      if (StringUtils.isEmpty(resp) || resp.equalsIgnoreCase(KeysI.PROMPT_K1)) {
//        continue;
//      }
//
//      builder.append(aux);
//    }
//
//    return builder.toString();
//  }

  public static void main(String[] args) throws IOException {
     
    Callback callback = new Callback() {
      @Override
      public void call(String future) {
        System.out.printf("Callback Response:%s", future);
      }
    };

    ClientSocket socket = new ClientSocket("192.168.1.3", 22, new JFrame());
    socket.open();
     
    String resp = socket.executeCommand("get name");

    Task task = new SimpleTask(resp);
    task.executeWith(callback);
    
    socket.close();
  }

  
}
