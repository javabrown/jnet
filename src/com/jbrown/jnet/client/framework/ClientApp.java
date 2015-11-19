package com.jbrown.jnet.client.framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jbrown.jnet.utils.KeysI;
import com.jbrown.jnet.utils.StringUtils;

public class ClientApp {
  private String _host;
  private int _port;

  private Socket _socket;
  private JFrame _frame;

  private PrintStream _socWriter;
  private BufferedReader _socReader;

  public ClientApp(String host, int port, JFrame frame){
    _host = host;
    _port = port;
    _frame = frame;
    _socket = null;
  }

  public boolean openSocket() {
    try {
      _socket = new Socket(_host, _port);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(_frame, e.getMessage());
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public boolean openStream() {
    try {
      _socWriter = new PrintStream(_socket.getOutputStream(), true);
      _socReader = new BufferedReader(new InputStreamReader(
          _socket.getInputStream(), "UTF-8"));
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public boolean closeSocket() {
    try {
       this.closeStream();
      _socket.close();
      _socket = null;
    } catch (IOException e) {
      JOptionPane.showMessageDialog(_frame, e.getMessage());
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public boolean closeStream() {
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

  public String listen() throws IOException {
    StringBuilder builder = new StringBuilder();
    String aux = "";

    _socWriter.printf("%s\r", "ping");

    while ((aux = _socReader.readLine()) != null && _socket != null) {
        String resp = aux.trim();

        if(resp.equalsIgnoreCase("END")){
            Task task = new SimpleTask(builder.toString());
            task.execute();

            builder = new StringBuilder();
            _socWriter.printf("%s\r", "ping");
        }

        if(StringUtils.isEmpty(resp) ||
              resp.equalsIgnoreCase(KeysI.PROMPT_K1)){
          continue;
        }

        builder.append(aux);
    }

    return builder.toString();
  }

//  public static void main(String[] args) throws IOException {
//    String serverAddress = "192.168.8.130";
//    Socket s = new Socket(serverAddress, 22);
//
//    PrintStream writer = new PrintStream(s.getOutputStream(), true);
//
//    BufferedReader reader = new BufferedReader(
//        new InputStreamReader( s.getInputStream(), "UTF-8"));
//
//    writer.printf("%s\r", "ping");
//    //writer.flush();
//
//    String answer = read(reader);
//
//    System.out.println(answer);
//
//
//    writer.printf("%s\r", "quit");
//    writer.flush();
//
//    writer.close();
//    reader.close();
//    s.close();
//
//    System.exit(0);
//}

private static String read(BufferedReader reader) throws IOException {
  StringBuilder builder = new StringBuilder();
  String aux = "";

  while ((aux = reader.readLine()) != null) {
      String resp = aux.trim();
      if(resp.equalsIgnoreCase("END")){
        break;
      }

      if(StringUtils.isEmpty(resp) || resp.equalsIgnoreCase(KeysI.PROMPT_K1)){
        continue;
      }

      builder.append(aux);
  }

  return builder.toString();
}

  public static void main(String[] args) throws IOException {
    Task task = new SimpleTask("");
    Callback callback = new Callback() {
      @Override
      public void call() {
        System.out.println("I'm done now.");
      }
    };
    task.executeWith(callback);

    ClientApp app = new ClientApp("192.168.8.130", 22, new JFrame());
    app.openSocket();
    app.openStream();
    app.listen();

    app.closeSocket();

  }
}