package com.jbrown.jnet.client.framework;

import java.io.IOException;

import com.jbrown.jnet.client.ClientSocket;

public abstract class Task {
//  protected ClientSocket _socket;
//
//
//  public Task(ClientSocket socket){
//    _socket = socket;
//  }

  public final void executeWith(Callback callback) {
      String response = execute(callback.getClientSocket());

      if (callback != null) {
        callback.call(response);
      }
  }

//  public String executeCommand(String command) {
//      String response = "";
//      try {
//        response = _socket.executeCommand(command);
//        System.out.printf("\n[%s] command executed|result=%s", command);
//      } catch (IOException e1) {
//        e1.printStackTrace();
//      }
//
//      return response;
//  }

  public abstract String execute(ClientSocket socket);


}