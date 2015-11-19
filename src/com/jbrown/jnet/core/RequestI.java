package com.jbrown.jnet.core;

import java.net.Socket;

import com.jbrown.jnet.utils.JsonMap;

public interface RequestI {
	Command getCommand();
	String[] getParameters();
	String getRowCommand();
	String getRowParamsExcludingCommand();

	Socket getSocket();
  void setContext(SharedContextI context);
  SharedContextI getContext();
  JsonMap getJsonMap();
}
