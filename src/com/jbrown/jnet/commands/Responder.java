package com.jbrown.jnet.commands;

import java.lang.reflect.Constructor;
import java.net.Socket;
import java.util.EnumMap;

import com.jbrown.jnet.commands.action.ActionI;
import com.jbrown.jnet.commands.action.ClearAction;
import com.jbrown.jnet.commands.action.HelpAction;
import com.jbrown.jnet.commands.action.MathAction;
import com.jbrown.jnet.commands.action.NoAction;
import com.jbrown.jnet.commands.action.PingAction;
import com.jbrown.jnet.commands.action.WGetAction;
import com.jbrown.jnet.core.Command;
import com.jbrown.jnet.core.Request;
import com.jbrown.jnet.core.RequestI;

public class Responder {
  private EnumMap<Command, Class> _commandActionMap;

  public Responder() {
    _commandActionMap = new EnumMap<Command, Class>(Command.class);

    _commandActionMap.put(Command.CLEAR, ClearAction.class);
    _commandActionMap.put(Command.PING, PingAction.class);
    _commandActionMap.put(Command.HELP, HelpAction.class);
    _commandActionMap.put(Command.CALC, MathAction.class);
    _commandActionMap.put(Command.WGET, WGetAction.class);
  }

  public String respond(Socket socket, String rowSocketInput) {
    RequestI request = new Request(socket, rowSocketInput);
    ActionI<String> action = getAction(request);
    return action.trigger();
  }

  private ActionI getAction(RequestI request) {
    Class klass = _commandActionMap.get(request.getCommand());
    ActionI action = new NoAction(request);

    try {
      Constructor<?> ctor = klass.getConstructor(RequestI.class);
      Object object = ctor.newInstance(new Object[] { request });

      if (object != null && object instanceof ActionI) {
        action = (ActionI) object;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return action;
  }
}
