package com.jbrown.jnet.client.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jbrown.jnet.utils.KeysI;
import com.jbrown.jnet.utils.StringUtils;

public class ClientSocket {
  private String _host;
  private int _port;

  private Socket _socket;
  private JFrame _frame;

  private PrintStream _socWriter;
  private BufferedReader _socReader;

  public ClientSocket(String host, int port) {
    _host = host;
    _port = port;
    _frame = new JFrame();
    _socket = null;
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

    return builder.toString().trim();
  }

  private boolean openSocket() {
    try {
      _socket = new Socket(_host, _port);
    } catch (IOException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(_frame, e.getMessage());
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
      e.printStackTrace();
      JOptionPane.showMessageDialog(_frame, e.getMessage());
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
      e.printStackTrace();
      JOptionPane.showMessageDialog(_frame, e.getMessage());
      return false;
    }

    return true;
  }
}