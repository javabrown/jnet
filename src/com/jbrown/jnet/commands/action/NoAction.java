package com.jbrown.jnet.commands.action;

import com.jbrown.jnet.commands.action.AbstractAction.ActionPerformerI;
import com.jbrown.jnet.core.ErrorI;
import com.jbrown.jnet.core.RequestI;

public class NoAction implements ActionPerformerI<String> {

  @Override
  public String perform(RequestI request, ErrorI errors) {
    return "Bad Command";
  }

  @Override
  public boolean validate(RequestI request, ErrorI errors) {
    return true;
  }

}
