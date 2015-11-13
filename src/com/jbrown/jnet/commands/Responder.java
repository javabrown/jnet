package com.jbrown.jnet.commands;

import com.jbrown.jnet.commands.action.AbstractAction;
import com.jbrown.jnet.core.ActionPerformer;
import com.jbrown.jnet.core.RequestI;
import com.jbrown.jnet.core.SharedContext;
import com.jbrown.jnet.core.SharedContextI;
import com.jbrown.jnet.core.SocketPool;

public class Responder {
  private ActionPerformer _actionPerformer;
  private SharedContextI _sharedContext;
  private SocketPool _socPool;

  public Responder() {
    _actionPerformer = new ActionPerformer();
    _sharedContext = new SharedContext();
     initializeSocPool();
  }

  public void initializeSocPool(){
    _socPool = new SocketPool();
  }

  public String respond(RequestI request) {
    AbstractAction<String> abs = new AbstractAction(request, _sharedContext);
    return abs.trigger();
  }

  public boolean isConnectionAvailable(){
    return _socPool.isConnectionAvailable();
  }

  public SocketPool getSocketPool(){
    return _socPool;
  }
}

//class SocketPool {
//  private static int MAX_CONNECTION = 10;
//
//  private Map<String, WorkerThread> _workerThreadMap;
//  private ThreadPoolExecutor  _threadExecutor;
//  private int _clientThreadIndex;
//
//  public SocketPool(){
//    _threadExecutor =
//        (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_CONNECTION);
//    _clientThreadIndex = 0;
//  }
//
//  public boolean isConnectionAvailable(){
//    return _workerThreadMap.size() < MAX_CONNECTION;
//  }
//
//  public boolean registerClient(WorkerThread clientThread){
//    String clientThreadId = format("client-thread-%s", _clientThreadIndex++);
//
//    if(_workerThreadMap.size() < MAX_CONNECTION){
//      _workerThreadMap.put(clientThreadId, clientThread);
//      _threadExecutor.execute(clientThread);
//      return true;
//    }
//
//    System.out.printf("Rejected %s", clientThreadId);
//
//    return false;
//  }
//
//  public void unregisterClient(String clientThreadId){
//    _workerThreadMap.remove(clientThreadId);
//  }
//}
