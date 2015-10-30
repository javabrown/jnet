package com.jbrown.jnet.commands.action;

import com.jbrown.jnet.core.RequestI;

public class HelpAction extends AbstractAction<String> {

  public HelpAction(RequestI request) {
    super(request);
  }

  @Override
  String perform() {
    return _request.getCommand().getCommandList();
  }

  @Override
  boolean validate() {
    return true;
  }
}
