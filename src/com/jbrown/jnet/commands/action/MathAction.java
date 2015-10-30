package com.jbrown.jnet.commands.action;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.jbrown.jnet.core.RequestI;

public class MathAction extends AbstractAction<String> {

  public MathAction(RequestI request) {
    super(request);
  }

  @Override
  String perform() {
    String[] parameters = _request.getParameters();
    String response = eval(parameters);
    return response;
  }

  @Override
  boolean validate() {
    return true;
  }

  /**
   * Evaluate mathematical expression
   */
  public String eval(String[] parameters) {

    String result = "Invalid Expression";
    
    try {
      String mathExpression = "";
      for (String param : parameters) {
        mathExpression = String.format("%s%s", param, mathExpression);
      }

      ScriptEngineManager mgr = new ScriptEngineManager();
      ScriptEngine engine = mgr.getEngineByName("JavaScript");
      result = engine.eval(mathExpression) + "";

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return result;
  }
}
