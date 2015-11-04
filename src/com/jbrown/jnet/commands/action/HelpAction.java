package com.jbrown.jnet.commands.action;

import com.jbrown.jnet.commands.Responder;
import com.jbrown.jnet.commands.action.AbstractAction.ActionPerformerI;
import com.jbrown.jnet.core.ErrorI;
import com.jbrown.jnet.core.RequestI;

public class HelpAction implements ActionPerformerI<String> {
  @Override
  public String perform(RequestI request, ErrorI errors) {
    return request.getCommand().getCommandList();
  }

  @Override
  public boolean validate(RequestI request, ErrorI errors) {
    // TODO Auto-generated method stub
    return true;
  }
}
