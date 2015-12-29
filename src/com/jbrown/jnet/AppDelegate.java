package com.jbrown.jnet;

import com.jbrown.jnet.client.ClientLinker;
import com.jbrown.jnet.core.JnetContainer;

public class AppDelegate {
  private JnetContainer _server;
  private ClientLinker _linker;

  public AppDelegate(){
    _server = null;
    _linker = null;
  }
}
