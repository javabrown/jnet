package com.jbrown.jnet.commands;

import java.text.ParseException;

import com.jbrown.jnet.commands.action.AbstractAction;
import com.jbrown.jnet.core.ActionPerformer;
import com.jbrown.jnet.core.RequestI;
import com.jbrown.jnet.core.SharedContext;
import com.jbrown.jnet.core.SharedContextI;
import com.jbrown.jnet.core.SocketPool;
import com.jbrown.jnet.response.ResponseI;

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
  
  public ResponseI respond(RequestI request) {
    AbstractAction abs = new AbstractAction(request, _sharedContext);
    ResponseI result = abs.trigger();
//    request.getJsonMap().put("response", result);
//    try {
//      return request.getJsonMap().toJson();
//    } catch (ParseException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
    return result;
  }

  public boolean isConnectionAvailable(){
    return _socPool.isConnectionAvailable();
  }

  public SocketPool getSocketPool(){
    return _socPool;
  }
}
