package com.jbrown.jnet.commands.action;

import com.jbrown.jnet.commands.action.AbstractAction.ActionPerformerI;
import com.jbrown.jnet.core.Command;
import com.jbrown.jnet.core.ErrorI;
import com.jbrown.jnet.core.RequestI;
import com.jbrown.jnet.utils.KeysI;
import com.jbrown.jnet.utils.StringUtils;
import com.jbrown.jnet.utils.Utils;

public class ClipAction implements ActionPerformerI<String>  {
  @Override
  public String perform(RequestI request, ErrorI errors) {

    String params = request.getRowParamsExcludingCommand();

    if(!StringUtils.isEmpty(params)){
      //Utils.setClipboardContents(params);
      request.getContext().putCache(Command.CLIP.getName(), params);
    }

    return request.getContext().getCache(Command.CLIP.getName());
  }

  @Override
  public boolean validate(RequestI request, ErrorI errors) {
    return true;
  }
}