package com.jbrown.jnet.core;

import java.util.LinkedHashMap;
import java.util.Map;

public class SharedContext implements SharedContextI {
  private Map<String, String> _jnetSharedCache;

  public SharedContext() {
    _jnetSharedCache = new LinkedHashMap<String, String>();
  }

  @Override
  public void putCache(String key, String value) {
    _jnetSharedCache.put(key, value);
  }

  @Override
  public String getCache(String key) {
    return _jnetSharedCache.get(key);
  }
}
