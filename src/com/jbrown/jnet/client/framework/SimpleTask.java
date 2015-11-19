package com.jbrown.jnet.client.framework;

public class SimpleTask extends Task {
  private String _command;

  public SimpleTask(String command){
    _command = command;
  }

  @Override
  public void execute() {
    System.out.printf("Performed %s.\n", _command);
  }
}