package com.jbrown.jnet.core;

import com.jbrown.jnet.utils.KeysI;
import com.jbrown.jnet.utils.StringUtils;

public class WireData {
  private String _wireDataInput;

  public WireData(String wireDataInput){
    _wireDataInput = wireDataInput;
  }

  public String getCommand(){
    return this.getFilteredData();
  }

  /**
   * Trim white-space and backspace char.
   */
  private String getFilteredData(){
    String filterInput = _wireDataInput.trim();
    filterInput = StringUtils.removeBackspacedChar(filterInput);

    return filterInput;
  }
}
