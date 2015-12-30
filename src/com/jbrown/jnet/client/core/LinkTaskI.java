package com.jbrown.jnet.client.core;

import com.jbrown.jnet.client.Task;


public interface LinkTaskI {
  Task getClipSetter();
  void link();
  void unlink();
}
