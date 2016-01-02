package com.jbrown.jnet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jbrown.jnet.client.LinkRunner;
import com.jbrown.jnet.core.JnetContainer;
import com.jbrown.jnet.utils.KeyMaker;

public class JNetDelegate {
  private JNetServerSpace _serverSpace;
  private JNetLinker _linker;

  private static JNetDelegate _instance;

  public static JNetDelegate getInstance(){
    if(_instance == null) {
      _instance = new JNetDelegate();
    }

    return _instance;
  }

  private JNetDelegate() {
    _serverSpace = JNetServerSpace.getInstance();
    _linker = JNetLinker.getInstance();
  }

  public JNetServerSpace getJNetServerSpace() {
    return _serverSpace;
  }

  public JNetLinker getJNetLinker() {
    return _linker;
  }
}

class JNetServerSpace {
  private Map<String, JNetServer> _map;

  private static JNetServerSpace _instance;

  public static JNetServerSpace getInstance() {
    if (_instance == null) {
      _instance = new JNetServerSpace();
    }

    return _instance;
  }

  private int getRunningServerCount(){
    return _map.size();
  }

  private JNetServerSpace() {
    _map = new HashMap<String, JNetServer>();
  }

  public JNetServer createServer(String host, int port) throws IOException {
    JNetServer server = new JNetServer(host, port);
    String key = KeyMaker.getServerKey(host, port);
    _map.put(key, server);

    return server;
  }

  public JNetServer getServerInstance(String serverKey){
    return _map.get(serverKey);
  }

  public boolean distroyServer(String serverKey) {
    JNetServer server = this.getServerInstance(serverKey);

    boolean flag = false;

    if(server != null){
      try{
        flag = server.stopServer();
      }
      finally{
        _map.remove(serverKey);
        System.out.printf("running servers: %s\n", getRunningServerCount());
      }
    }

    return flag;
  }
}

class JNetServer {
  private JnetContainer _server;

  public JNetServer(String host, int port) throws IOException {
    _server =  this.createJNetServer(host, port);
  }

  private JnetContainer createJNetServer(String host, int port)
      throws IOException {
    if (_server == null) {
      _server = new JnetContainer(host, port);
    }

    return _server;
  }

  public void startServer() {
    if (!_server.isRunning()) {
      _server.start();
    }
  }

  public boolean isRunning(){
    return _server.isRunning();
  }

  public boolean stopServer() {
    return _server.stop();
  }

  public String getHost(){
    return _server.getHost();
  }

  public int getPort(){
    return _server.getPort();
  }

  public String getJNetAddress(){
    return KeyMaker.getServerKey(getHost(), getPort());
  }
}


class JNetLinker {
  private LinkRunner _linker;

  private static JNetLinker _instance;

  public static JNetLinker getInstance() {
    if (_instance == null) {
      _instance = new JNetLinker();
    }

    return _instance;
  }

  private JNetLinker() {
    _linker = null;
  }

  private LinkRunner createJNetLink(String host, int port) {
    if(_linker == null){
      _linker = new LinkRunner(host, port);
    }

    return _linker;
  }

  public void startLinker(String host, int port) {
    _linker = this.createJNetLink(host, port);
    _linker.start();
  }

  public void stopLinker(){
     if(_linker != null){
      _linker.stop();
      _linker = null;
     }
  }


}