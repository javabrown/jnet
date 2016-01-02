package com.jbrown.jnet.client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jbrown.jnet.client.core.JNetConnector;
import com.jbrown.jnet.client.core.JNetConnectorI;
import com.jbrown.jnet.client.core.ClipboardLink;
import com.jbrown.jnet.client.core.LinkTaskI;

public class LinkRunner {
  private JNetConnectorI _socket;
  private LinkTaskI _task;
  private String _host;
  private int _port;

  public LinkRunner(String host, int port){
    _socket = new JNetConnector();
    _task = null;
    _host = host;
    _port = port;
  }

  public void start(){
    _socket.connect(_host, _port);
    _task = new ClipboardLink(_socket);
    _task.link();
  }

  public void stop(){
    _socket.disconnect();
    _task.unlink();

    _task = null;
    _socket = null;
  }

  public static void main(String[] args)  {
    LinkRunner runner = new LinkRunner("localhost", 22);

    runner.start();
    JOptionPane.showMessageDialog(new JFrame(), "Runner");
    runner.stop();
    runner = null;

    System.out.println("END");
  }
}
