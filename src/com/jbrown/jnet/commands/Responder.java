package com.jbrown.jnet.commands;

import java.lang.reflect.Constructor;
import java.net.Socket;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.jbrown.jnet.commands.action.AbstractAction;
import com.jbrown.jnet.commands.action.ActionI;
import com.jbrown.jnet.commands.action.ClearAction;
import com.jbrown.jnet.commands.action.HelpAction;
import com.jbrown.jnet.commands.action.MathAction;
import com.jbrown.jnet.commands.action.NoAction;
import com.jbrown.jnet.commands.action.PingAction;
import com.jbrown.jnet.commands.action.WGetAction;
import com.jbrown.jnet.core.ActionPerformer;
import com.jbrown.jnet.core.Command;
import com.jbrown.jnet.core.Request;
import com.jbrown.jnet.core.RequestI;
import com.jbrown.jnet.core.SharedContext;
import com.jbrown.jnet.core.SharedContextI;

public class Responder {
  private ActionPerformer _actionPerformer;
  private SharedContextI _sharedContext;

  public Responder() {
//    _actionPerformerMap = new EnumMap<Command, Class>(Command.class);
//
//    _actionPerformerMap.put(Command.CLEAR, ClearAction.class);
//    _actionPerformerMap.put(Command.PING, PingAction.class);
//    _actionPerformerMap.put(Command.HELP, HelpAction.class);
//    _actionPerformerMap.put(Command.CALC, MathAction.class);
//    _actionPerformerMap.put(Command.WGET, WGetAction.class);

    _actionPerformer = new ActionPerformer();
    _sharedContext = new SharedContext();
  }

  public String respond(RequestI request) {
    AbstractAction<String> abs = new AbstractAction(request, _sharedContext);
    return abs.trigger();
  }
}
