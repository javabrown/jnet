package com.jbrown.jnet.utils;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JsonMap {
  private ScriptEngine        _js;
  private Map<String, Object> _map;

  public JsonMap() {
    _js = new ScriptEngineManager().getEngineByName("JavaScript");
    _map = new HashMap<String, Object>();
  }

  public boolean put(String key, Object value) {
    if (!isSupported(value)) {
      return false;
    }

    _map.put(key, value);
    return true;
  }

  public String toJson() throws ParseException {
    try {
      return mapToJson();
    } catch (ScriptException e) {
      throw new ParseException(
          String.format("Invalid/unsupported object type found: %s",
              e.getLocalizedMessage()), 0);
    }
  }

  public boolean isSupported(Object value) {
    return isNoQuotableNode(value) || value instanceof CharSequence;
  }

  private boolean isNoQuotableNode(Object value) {
    return value instanceof Number || value instanceof Boolean;
  }

  private String arrayToJson(Object[] arrays) throws ScriptException {
    StringBuilder jsObjects = new StringBuilder("var obj = new Array ();");

    for (int i = 0; i < arrays.length; i++) {
      String jsObj = "";

      if (isNoQuotableNode(arrays[i])) {
        jsObj = String.format("obj.push (%s);", arrays[i]);
      } else {
        jsObj = String.format("obj.push ('%s');", arrays[i]);
      }

      jsObjects.append(jsObj);
    }

    String jsMethod = String.format(
        "(function() {%s; return JSON.stringify(%s); })()", jsObjects, "obj");

    return (String) _js.eval(jsMethod);
  }

  private String mapToJson() throws ScriptException {
    StringBuilder jsObjects = new StringBuilder("var obj = new Object ();");

    for (String key : _map.keySet()) {
      Object value = _map.get(key);

      String jsObj = "";

      if (value.getClass().isArray()) {
        value = arrayToJson((Object[]) value);
        jsObj = String.format("obj['%s'] = %s;", key, value);
      } else if (isNoQuotableNode(value)) {
        jsObj = String.format("obj['%s'] = %s;", key, value);
      } else {
        jsObj = String.format("obj['%s'] = '%s';", key, value);
      }

      jsObjects.append(jsObj);
    }

    String jsMethod = String.format(
        "(function() {%s; return JSON.stringify(%s); })()", jsObjects, "obj");

    return (String) _js.eval(jsMethod);
  }
}