package com.jbrown.jnet.commands.action;

import com.jbrown.jnet.commands.action.AbstractAction.ActionPerformerI;
import com.jbrown.jnet.core.ErrorI;
import com.jbrown.jnet.core.RequestI;
import com.jbrown.jnet.response.DefaultResponse;
import com.jbrown.jnet.response.ResponseI;

public class PingAction implements ActionPerformerI {
  @Override
  public ResponseI perform(RequestI request, ErrorI errors) {
    return new DefaultResponse("Hello World");
  }

  @Override
  public boolean validate(RequestI request, ErrorI errors) {
    return true;
  }
}
