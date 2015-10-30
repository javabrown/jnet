package com.jbrown.jnet.commands.action;

import com.jbrown.jnet.core.ErrorI;
import com.jbrown.jnet.core.RequestI;

public class NoAction extends AbstractAction<String> {

  public NoAction(RequestI request) {
    super(request);
  }

  @Override
  String perform() {
    return "Bad Command";
  }

  @Override
  boolean validate() {
    return true;
  }

}
