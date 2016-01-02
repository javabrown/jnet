package com.jbrown.jnet.client.task;

import com.jbrown.jnet.client.core.JNetConnector;

public interface TaskI {
  String execute(JNetConnector socket);
}
