package com.jbrown.jnet.client;

import com.jbrown.jnet.client.core.JNetConnector;


public interface Callback {
  void call(String future);
  JNetConnector getClientSocket();
}