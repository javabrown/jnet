package com.jbrown.jnet.commands.action;

import com.jbrown.jnet.core.RequestI;

public class PingAction extends AbstractAction<String>{
  
  public PingAction(RequestI request) {
    super(request);
  }

  @Override
  String perform() {
    return "Hello World";
  }

  @Override
  boolean validate() {
    return true;
  }
}
