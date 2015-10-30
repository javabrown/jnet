package com.jbrown.jnet.commands.action;

import com.jbrown.jnet.core.ErrorI;
import com.jbrown.jnet.core.RequestI;

public class ExitAction extends AbstractAction<String> {

  public ExitAction(RequestI request) {
    super(request);
  }

  @Override
  String perform() {
    return "EXIT COMMAND";
  }

  @Override
  boolean validate() {
    return true;
  }

}
