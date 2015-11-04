package com.jbrown.jnet.core;

public interface SharedContextI {
   String getCache(String key);
   void putCache(String key, String value);
}
