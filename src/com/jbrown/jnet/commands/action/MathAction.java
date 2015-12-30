package com.jbrown.jnet.commands.action;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import sun.org.mozilla.javascript.internal.json.JsonParser;

import com.jbrown.jnet.commands.action.AbstractAction.ActionPerformerI;
import com.jbrown.jnet.core.ErrorI;
import com.jbrown.jnet.core.RequestI;
import com.jbrown.jnet.response.DefaultResponse;
import com.jbrown.jnet.response.ResponseI;

public class MathAction implements ActionPerformerI  {

  @Override
  public ResponseI perform(RequestI request, ErrorI errors) {
    String[] parameters = request.getParameters();
    String response = eval(parameters);

    return new DefaultResponse(response);
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
