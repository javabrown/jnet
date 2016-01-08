package com.jbrown.jnet.client.core;

import java.io.IOException;

import javax.swing.JFrame;

public interface JNetConnectorI {
  void connect(String host, int port);
  void disconnect();
  String executeCommand(String cmd) throws IOException;
}
