package com.jbrown.jnet.commands.action;

import com.jbrown.jnet.commands.action.AbstractAction.ActionPerformerI;
import com.jbrown.jnet.core.ErrorI;
import com.jbrown.jnet.core.RequestI;

public class GetAction implements ActionPerformerI<String>{
  
  @Override
  public String perform(RequestI request, ErrorI errors) {
    String[] params = request.getParameters();
    
    String key = String.format("%s.%s", 
        request.getSocket().getInetAddress(), params[0].trim());
      
    String value = request.getContext().getCache(key);
    
    return (value == null) ? "Not Found" : value;
  }

  @Override
  public boolean validate(RequestI request, ErrorI errors) {
    String[] params = request.getParameters();
    if(params == null || params.length <= 0){
     errors.addErrors("Invalid get request: key is missing in your request"); 
     return false;
    }
    
    try{
      String key = String.format("%s.%s", 
        request.getSocket().getInetAddress(), params[0].trim());
      
    }catch(Exception ex){
      errors.addErrors("Invalid put request: key/value missing."); 
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
