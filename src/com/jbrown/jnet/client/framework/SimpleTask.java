package com.jbrown.jnet.client.framework;

public class SimpleTask extends Task {
  private String _command;

  public SimpleTask(String command){
    _command = command;
  }

  @Override
  public String execute() {
    return String.format("Performed %s.\n", _command);
  }
}