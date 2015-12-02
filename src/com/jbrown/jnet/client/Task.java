package com.jbrown.jnet.client;

import static com.jbrown.jnet.utils.StringUtils.isEmpty;
import static com.jbrown.jnet.utils.StringUtils.isEquals;

import java.io.IOException;

import com.jbrown.jnet.client.core.ClientSocket;
import com.jbrown.jnet.core.Command;
import com.jbrown.jnet.utils.KeysI;

public abstract class Task {

  public final void executeWith(Callback callback) {
      String response = execute(callback.getClientSocket());

      if (callback != null) {
        callback.call(response);
      }
  }

  public abstract String execute(ClientSocket socket);


  protected String socketExecute(ClientSocket socket, String command) {
    try {
      return socket.executeCommand(command);

    } catch (IOException e) {
      e.printStackTrace();
    }

    return KeysI.EMPTY_K;
  }

}