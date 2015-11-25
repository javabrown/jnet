package com.jbrown.jnet.utils;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import sun.org.mozilla.javascript.internal.json.JsonParser;

public class JsUtils {
  public static String escape(String str) {
    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine = mgr.getEngineByName("JavaScript");
    try {
      String jsCmd = String.format("escape('%s');", str);
      return engine.eval(jsCmd) + "";
    } catch (ScriptException e) {
      e.printStackTrace();
    }

    return "";
  }
  
  public static String unescape(String str) {
    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine = mgr.getEngineByName("JavaScript");
    try {
      String jsCmd = String.format("unescape('%s');", str);
      return engine.eval(jsCmd) + "";
    } catch (ScriptException e) {
      e.printStackTrace();
    }

    return "";
  }
  


  public static void main(String[] args){
    String es = escape("hello\rworld");
    String us = escape(es);
    System.out.printf("%s|%s", es, us);
  }
}
