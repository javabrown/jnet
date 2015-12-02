package com.jbrown.jnet.client;

import com.jbrown.jnet.client.core.ClientSocket;


public interface Callback {
  void call(String future);
  ClientSocket getClientSocket();
}