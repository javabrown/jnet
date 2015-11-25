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
      return engine.eval("escape('"+ str +"')") + "";
    } catch (ScriptException e) {
      e.printStackTrace();
    }

    return "";
  }


  public static void main(String[] args){
    System.out.println(escape("hello\rworld"));
  }
}
