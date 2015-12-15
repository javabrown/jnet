package com.jbrown.jnet.commands;

import java.text.ParseException;

import com.jbrown.jnet.commands.action.AbstractAction;
import com.jbrown.jnet.core.ActionPerformer;
import com.jbrown.jnet.core.RequestI;
import com.jbrown.jnet.core.SharedContext;
import com.jbrown.jnet.core.SharedContextI;
import com.jbrown.jnet.core.SocketPool;

public class Responder {
  private ActionPerformer _actionPerformer;
  private SharedContextI _sharedContext;
  private SocketPool _socPool;

  public Responder() {
    _actionPerformer = new ActionPerformer();
    _sharedContext = new SharedContext();
     initializeSocPool();
  }

  public void initializeSocPool(){
    _socPool = new SocketPool();
  }

  public String respond(RequestI request) {
    AbstractAction<String> abs = new AbstractAction(request, _sharedContext);
    String result = abs.trigger();

    return result;
  }

  public boolean isConnectionAvailable(){
    return _socPool.isConnectionAvailable();
  }

  public SocketPool getSocketPool(){
    return _socPool;
  }
}
