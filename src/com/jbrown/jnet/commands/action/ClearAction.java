package com.jbrown.jnet.commands.action;

import com.jbrown.jnet.core.ErrorI;
import com.jbrown.jnet.core.RequestI;
import com.jbrown.jnet.commands.action.AbstractAction.ActionPerformerI;

public class ClearAction implements ActionPerformerI<String>  {
  @Override
  public String perform(RequestI request, ErrorI errors) {
    return "\u001B[2J";
  }

  @Override
  public boolean validate(RequestI request, ErrorI errors) {
    return true;
  }
}
