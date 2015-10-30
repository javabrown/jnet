package com.jbrown.jnet.commands;

import java.util.EnumMap;

import com.jbrown.jnet.commands.action.ActionI;
import com.jbrown.jnet.commands.action.ClearAction;
import com.jbrown.jnet.commands.action.ExitAction;
import com.jbrown.jnet.commands.action.NoAction;
import com.jbrown.jnet.commands.action.PingAction;
import com.jbrown.jnet.core.Command;
import com.jbrown.jnet.core.Request;
import com.jbrown.jnet.core.RequestI;

public class Responder {
	private EnumMap<Command, ActionI<String>> _commandActionMap;
	private RequestI _request;
	
	public Responder(String rowSocketInput){
	  _request = new Request(rowSocketInput);
	  _commandActionMap = new EnumMap<Command, ActionI<String>>(Command.class);
	  _commandActionMap.put(Command.CLEAR, new ClearAction(_request));
	  _commandActionMap.put(Command.PING, new PingAction(_request));
	  _commandActionMap.put(Command.EXIT, new ExitAction(_request));
	   
	}
	
	public String respond(){
	  ActionI<String> action = _commandActionMap.get(_request.getCommand());
	  
	  if(action == null){
	    action = new NoAction(_request);
	  }
	  
	  return action.trigger();
	}
}
