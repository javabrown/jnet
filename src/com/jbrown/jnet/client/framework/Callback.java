package com.jbrown.jnet.client.framework;

import com.jbrown.jnet.client.ClientSocket;

public interface Callback {
  void call(String future);
  ClientSocket getClientSocket();
}