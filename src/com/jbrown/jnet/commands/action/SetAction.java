package com.jbrown.jnet.commands.action;

import com.jbrown.jnet.commands.action.AbstractAction.ActionPerformerI;
import com.jbrown.jnet.core.ErrorI;
import com.jbrown.jnet.core.RequestI;
import com.jbrown.jnet.utils.StringUtils;

public class SetAction  implements ActionPerformerI<String> {

  @Override
  public String perform(RequestI request, ErrorI errors) {
    String[] params = request.getParameters();

    //String key = String.format("%s.%s",
    //    request.getSocket().getInetAddress(), params[0]);
    String key = String.format("%s", params[0]);

    String value = joinValue(params);

    request.getContext().putCache(key, value);
    return "saved!";
  }

  @Override
  public boolean validate(RequestI request, ErrorI errors) {
    String[] params = request.getParameters();
    if(params == null || params.length < 2){
     errors.addErrors("Invalid put request: key/value missing.");
     return false;
    }

    try{
      String key = String.format("%s.%s",
        request.getSocket().getInetAddress(), params[0].trim());
      String value = joinValue(params);

      if(StringUtils.isEmpty(key)){
        errors.addErrors("cache {key} can not be empty or null");
      }

      if(StringUtils.isEmpty(value)){
        errors.addErrors("cache {value} can not be empty or null");
      }

    }catch(Exception ex){
      errors.addErrors("Invalid put request: missing {key} or {value}");
    }

    if(!errors.noError()){
      return false;
    }

    return true;
  }

  private String joinValue(String[] params){
    StringBuilder val = new StringBuilder();

    for(int i=1; i < params.length; i++){
      val.append(params[i]);
    }

    return val.toString();
  }

}
