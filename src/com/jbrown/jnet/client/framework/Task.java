package com.jbrown.jnet.client.framework;

public abstract class Task {

  public final void executeWith(Callback callback) {
    execute();
    if (callback != null) {
      callback.call();
    }
  }

  public abstract void execute();
}