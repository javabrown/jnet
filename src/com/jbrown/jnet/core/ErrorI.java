package com.jbrown.jnet.core;

public interface ErrorI {
  String[] getError();
  void clear();
  boolean noError();
  void addErrors(String error);
}
