//package com.jbrown.jnet.client.framework;
//
//import com.jbrown.jnet.client.ClientSocket;
//
//public class SimpleTask extends Task {
//  private String _command;
//
//  public SimpleTask(String command){
//    _command = command;
//  }
//
//  @Override
//  public String execute() {
//    return String.format("Performed %s.\n", _command);
//  }
//
//  @Override
//  public String execute(ClientSocket scoket) {
//    return String.format("Performed %s.\n", _command);
//  }
//}