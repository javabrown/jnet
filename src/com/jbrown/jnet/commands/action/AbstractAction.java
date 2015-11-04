package com.jbrown.jnet.commands.action;

import java.lang.reflect.Constructor;

import sun.misc.PerformanceLogger;

import com.jbrown.jnet.core.ActionPerformer;
import com.jbrown.jnet.core.ErrorI;
import com.jbrown.jnet.core.RequestI;
import com.jbrown.jnet.core.ResponseI;
import com.jbrown.jnet.core.SharedContextI;
import com.jbrown.jnet.utils.StringUtils;

public class AbstractAction<T> implements ActionI<T> {
  protected RequestI _request;
  protected ErrorI _errors;

  protected SharedContextI _sharedContext;

  public AbstractAction(RequestI request, SharedContextI context){
    _request = request;
    _errors = new com.jbrown.jnet.core.Error();
    _sharedContext = context;

    request.setContext(context);
  }

  public ActionPerformerI getActionPerformer() {
    Class klass = new ActionPerformer().getActionClass(_request);
    ActionPerformerI action = new NoAction();

    try {
      Constructor<?> ctor = klass.getConstructor();
      Object object = ctor.newInstance(new Object[] {});

      if (object != null && object instanceof ActionPerformerI) {
        action = (ActionPerformerI) object;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return action;
  }

//  @Override
//  public T trigger() {
//    T response = null;
//
//    if(_request.getCommand() == null){
//      _errors.addErrors(
//          String.format("Bad command => %s", _request.getRowCommand()));
//    }
//
//    if(_errors.noError()){
//      boolean isValid = this.validate();
//
//      if(isValid){
//        response = this.perform();
//
//        if(_errors.noError() && response != null){
//          return response;
//        }
//      }
//      else{
//        response = (T) StringUtils.stringify(_errors.getError());
//      }
//    }
//    else{
//      response = (T) StringUtils.stringify(_errors.getError());
//    }
//
//    return response;
//  }

  @Override
  public T trigger() {
    T response = null;

    if(_request.getCommand() == null){
      _errors.addErrors(
          String.format("Bad command => %s", _request.getRowCommand()));
    }

     ActionPerformerI performer = this.getActionPerformer();

    if(performer!= null && _errors.noError()){
      boolean isValid = performer.validate(_request, _errors);

      if(isValid){
        response = (T) performer.perform(_request, _errors);

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

//  abstract T perform();
//  abstract boolean validate();

  interface ActionPerformerI<T> {
      T perform(RequestI request, ErrorI errors);
      boolean validate(RequestI request, ErrorI errors);
  }
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
