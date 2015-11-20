package com.jbrown.jnet.client.framework;

public abstract class Task {

  public final void executeWith(Callback callback) {
    String response = execute();
    if (callback != null) {
      callback.call(response);
    }
  }

  public abstract String execute();
}