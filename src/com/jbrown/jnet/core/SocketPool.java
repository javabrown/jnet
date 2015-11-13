package com.jbrown.jnet.core;

import static java.lang.String.format;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SocketPool {
  private static int MAX_CONNECTION = 10;

  private Map<String, WorkerThread> _workerThreadMap;
  private ThreadPoolExecutor  _threadExecutor;
  private int _clientThreadIndex;

  public SocketPool(){
    _workerThreadMap = new HashMap<String, WorkerThread>();
    _threadExecutor =
        (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_CONNECTION);
    _clientThreadIndex = 0;
  }

  public boolean isConnectionAvailable(){
    return _workerThreadMap.size() < MAX_CONNECTION;
  }

  public boolean registerClient(WorkerThread clientThread){
    if(this.isConnectionAvailable()){
      _workerThreadMap.put(clientThread.getThreadId(), clientThread);
      _threadExecutor.execute(clientThread);
      return true;
    }

    System.out.printf("Rejected Thread %s", clientThread.getThreadId());

    return false;
  }

  public void unregisterClient(String clientThreadId){
    _workerThreadMap.remove(clientThreadId);
  }

  public ThreadPoolExecutor getThreadPoolExecutor(){
    return _threadExecutor;
  }

  public boolean shutdown() {
    try {
      WorkerThread[] threads =
          _workerThreadMap.values().toArray(new WorkerThread[0]);

      for (WorkerThread client : threads) {
        client.stop();
        System.out.printf("Client thread %s stopped.", client.getThreadId());
        _workerThreadMap.remove(client.getThreadId());
      }

      JOptionPane.showMessageDialog(new JFrame(), "Server Stopped");
    } finally {
      _threadExecutor.shutdownNow();
    }

    return true;
  }
}