package com.jbrown.jnet.client.task;

import com.jbrown.jnet.client.core.ClientSocket;

public interface TaskI {
  String execute(ClientSocket socket);
}
