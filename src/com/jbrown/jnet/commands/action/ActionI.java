package com.jbrown.jnet.commands.action;

import com.jbrown.jnet.core.RequestI;
import com.jbrown.jnet.core.ResponseI;

public interface ActionI<T>  {
  T trigger();
}
