package com.jbrown.jnet.commands.action;

import com.jbrown.jnet.core.ErrorI;
import com.jbrown.jnet.core.RequestI;

public class ClearAction extends AbstractAction<String>{
  
  public ClearAction(RequestI request) {
    super(request);
  }

  @Override
  String perform() {
    return "\u001B[2J";
  }

  @Override
  boolean validate() {
    return true;
  }

}
