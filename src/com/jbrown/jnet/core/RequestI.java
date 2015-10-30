package com.jbrown.jnet.core;

import java.net.Socket;

public interface RequestI {
	Command getCommand();
	String[] getParameters();
	String getRowCommand();
	Socket getSocket();
}
