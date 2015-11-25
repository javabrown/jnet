package com.jbrown.jnet.client.task;

import com.jbrown.jnet.client.ClientSocket;

public interface TaskI {
  String execute(ClientSocket socket);
}
