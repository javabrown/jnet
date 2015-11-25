package com.jbrown.jnet.client;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jbrown.jnet.client.framework.LinkTask;
import com.jbrown.jnet.client.framework.LinkTaskI;

public class ClientLinker {
  private ClientSocket _socket;
  private LinkTaskI _task;
  private String _host;
  private int _port;

  public ClientLinker(String host, int port, JFrame frame){
    _socket = null;
    _task = null;
    _host = host;
    _port = port;
  }

  public void start(){
    _socket = new ClientSocket(_host, _port,  new JFrame());
    _socket.open();
    _task = new LinkTask(_socket);
    _task.link();
  }

  public void stop(){
    _socket.close();
    _task.unlink();

    _task = null;
    _socket=null;
  }

  public static void main(String[] args)  {
    ClientLinker runner = new ClientLinker("192.168.8.130", 22, new JFrame());

    runner.start();
    JOptionPane.showMessageDialog(new JFrame(), "Runner");
    runner.stop();
    runner = null;

    System.out.println("END");
  }
}
