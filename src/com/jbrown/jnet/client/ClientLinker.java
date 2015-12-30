package com.jbrown.jnet.client;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jbrown.jnet.client.core.ClientSocket;
import com.jbrown.jnet.client.core.LinkTask;
import com.jbrown.jnet.client.core.LinkTaskI;

public class ClientLinker {
  private ClientSocket _socket;
  private LinkTaskI _task;
  private String _host;
  private int _port;

  public ClientLinker(String host, int port){
    _socket = null;
    _task = null;
    _host = host;
    _port = port;
  }

  public void start(){
    _socket = new ClientSocket(_host, _port);
    _socket.open();
    _task = new LinkTask(_socket);
    _task.link();
  }

  public void stop(){
    _socket.close();
    _task.unlink();

    _task = null;
    _socket = null;
  }

  public static void main(String[] args)  {
    ClientLinker runner = new ClientLinker("192.168.1.6", 22);

    runner.start();
    JOptionPane.showMessageDialog(new JFrame(), "Runner");
    runner.stop();
    runner = null;

    System.out.println("END");
  }
}
