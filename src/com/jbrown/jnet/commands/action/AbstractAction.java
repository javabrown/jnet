package com.jbrown.jnet.commands.action;

import com.jbrown.jnet.core.ErrorI;
import com.jbrown.jnet.core.RequestI;
import com.jbrown.jnet.core.ResponseI;
import com.jbrown.jnet.utils.StringUtils;

public abstract class AbstractAction<T> implements ActionI<T> {
  protected RequestI _request;
  protected ErrorI _errors;
  //protected SocketActionI _socketAction;
  
  public AbstractAction(RequestI request){
    _request = request;
    _errors = new com.jbrown.jnet.core.Error();
    //_socketAction = getSocketAction();
  }
  
  @Override
  public T trigger() {
    T response = null;
    
    if(_request.getCommand() == null){
      _errors.addErrors(
          String.format("Bad command => %s", _request.getRowCommand()));
    }
    
    if(_errors.noError()){
      boolean isValid = this.validate();
      
      if(isValid){
        response = this.perform();
        
        if(_errors.noError() && response != null){
          return response;
        }
      }
      else{
        response = (T) StringUtils.stringify(_errors.getError());
      }
    }
    else{
      response = (T) StringUtils.stringify(_errors.getError());
    }
    
    return response;
  }
  
  abstract T perform();
  abstract boolean validate();
   
//  interface SocketActionI {
//    abstract T perform(RequestI request, ErrorI errors);
//    abstract boolean validate(RequestI request, ErrorI errors);
//  }
}



class SocketResponse implements ResponseI {
  private String _socketOutput;
  
  public SocketResponse(String socketOutput) {
    _socketOutput = socketOutput;
  }
  
  @Override
  public String getResponse() {
    return _socketOutput;
  }
}
