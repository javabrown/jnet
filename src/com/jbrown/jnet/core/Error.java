package com.jbrown.jnet.core;

import com.jbrown.jnet.utils.StringUtils;

public class Error implements ErrorI {
  String[] _errors;
  
  public Error(){
    _errors = new String[0];
  }
  
  public void addErrors(String error){
    _errors = StringUtils.arrayPush(_errors, error);
  }
  
  public void clear(){
    _errors = new String[0];
  }
  
  public boolean noError(){
    return _errors == null || _errors.length == 0;
  }
  
  @Override
  public String[] getError() {
    return _errors;
  } 
}
