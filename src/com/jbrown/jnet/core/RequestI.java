package com.jbrown.jnet.core;

public interface RequestI {
	Command getCommand();
	String[] getParameters();
	String getRowCommand();
}
