package com.jbrown.jnet.commands.action;

import com.jbrown.jnet.commands.action.AbstractAction.ActionPerformerI;
import com.jbrown.jnet.core.ErrorI;
import com.jbrown.jnet.core.RequestI;
import com.jbrown.jnet.response.DefaultResponse;
import com.jbrown.jnet.response.ResponseI;
import com.jbrown.jnet.utils.StringUtils;

import static java.lang.String.format;

public class SetAction  implements ActionPerformerI {

  @Override
  public ResponseI perform(RequestI request, ErrorI errors) {
    String[] params = request.getParameters();

    //String key = String.format("%s.%s",
    //    request.getSocket().getInetAddress(), params[0]);
    String key = String.format("%s", params[0]);

    String value = getCacheValue(request);

    request.getContext().putCache(key, value);

    return new DefaultResponse("saved!");
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
      String value = getCacheValue(request);

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

//  private String joinValue(String[] params){
//    StringBuilder val = new StringBuilder();
//
//    for(int i=1; i < params.length; i++){
//      val.append(params[i]);
//    }
//
//    return val.toString();
//  }


  public String getCacheValue(RequestI request){
    String line = request.getRowCommand().trim();
    String[] arr = line.split(" ");

    String command = arr[0];
    String key = arr[1];

    String value = line.replaceAll( format(".*\\b%s\\b", command), "" );
           value = line.replaceAll( format(".*\\b%s\\b", key), "" ).trim();

    return value;
  }
}
