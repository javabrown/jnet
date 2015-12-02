package com.jbrown.jnet.commands.action;

import com.jbrown.jnet.core.RequestI;
import com.jbrown.jnet.response.ResponseI;

public interface ActionI<T>  {
  ResponseI trigger();
}
