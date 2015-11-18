package com.jbrown.jnet.commands.action;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import sun.org.mozilla.javascript.internal.json.JsonParser;

import com.jbrown.jnet.commands.action.AbstractAction.ActionPerformerI;
import com.jbrown.jnet.core.ErrorI;
import com.jbrown.jnet.core.RequestI;

public class MathAction implements ActionPerformerI<String> {

  @Override
  public String perform(RequestI request, ErrorI errors) {
    String[] parameters = request.getParameters();
    String response = eval(parameters);

    return response;
  }

  @Override
  public boolean validate(RequestI request, ErrorI errors) {
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
