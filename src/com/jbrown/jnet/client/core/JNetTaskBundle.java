package com.jbrown.jnet.client.core;

import java.util.ArrayList;
import java.util.List;

import com.jbrown.jnet.utils.reflect.JavaClassFinder;

public class JNetTaskBundle {
  private JNetConnectorI _jNetConnector;

  public JNetTaskBundle(JNetConnectorI jNetConnector) {
    _jNetConnector = jNetConnector;
  }

  void trigger(Class<? extends JNetTaskI> klass){
    List<Class<? extends JNetTaskI>> jNetTaskList =
        new JavaClassFinder().findAllMatchingTypes(JNetTaskI.class);

    for(Class<? extends JNetTaskI> task : jNetTaskList){
      if(task.getCanonicalName().equals(klass.getCanonicalName())){
        //task.newInstance();
      }
    }
  }
}
