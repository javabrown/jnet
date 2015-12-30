package com.jbrown.jnet;

import com.jbrown.jnet.utils.KeysI;
import com.jbrown.jnet.utils.StringUtils;

public enum LaunchOption {
  STOP("Stop"), HOST("Host"), LINK("Link"), EXIT("Exit"),  NONE("None");

  private String _name;

  LaunchOption(String name) {
    _name = name;
  }

  public String getName(){
    return _name;
  }

  public static LaunchOption find(String optionName) {
    if (!StringUtils.isEmpty(optionName)) {
      for (LaunchOption opt : values()) {
        if (opt.getName().equalsIgnoreCase(optionName)) {
          return opt;
        }
      }
    }

    return NONE;
  }

  public boolean typeOf(String optionName) {
    LaunchOption opt = find(optionName);
    if (this.equals(opt)) {
      return true;
    }
    return false;
  }

  public static String getAllOptions(){
     StringBuilder str = new StringBuilder();
     int index = 0;

     for(LaunchOption opt : values()){
        if(index > 0 ) {
           str.append(KeysI.COMMA_K).append(KeysI.SPACE_K);
        }

        str.append(opt);
        index++;
     }
     return String.format(" [ %s ]", str.toString());
  }
}