package com.jbrown.jnet.core;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.jbrown.jnet.commands.Responder;


public class JnetContainer {
  private Responder _responder;
  private final BrownServer _server;

  public JnetContainer(String host, int port) throws IOException {
    _responder = new Responder();
    _server = new BrownServer(host, port, _responder);
  }

  public boolean start() {
    if(_server == null || _server.isRunning()){
      return false;
    }

    new Thread(_server).start();

    return true;
  }

  public boolean stop() {
    if(_server == null || !_server.isRunning()){
      return false;
    }

    return _server.stop();
  }
}

