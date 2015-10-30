package com.jbrown.jnet.core;

import java.util.Arrays;

import com.jbrown.jnet.utils.KeysI;
import com.jbrown.jnet.utils.StringUtils;

import static com.jbrown.jnet.utils.StringUtils.*;

public class Request implements RequestI {
  private String   _rowInput;
  private Command  _command;
  private String[] _parameters;

  public Request(String rowInput) {
    _rowInput = rowInput;
    _command = populateCommand();
    _parameters = populateParameters();
  }

  @Override
  public Command getCommand() {
    return _command;
  }

  @Override
  public String[] getParameters() {
    return _parameters;
  }

  @Override
  public String getRowCommand() {
    return _rowInput;
  }
  
  public String getRowInput() {
    return _rowInput;
  }

  private Command populateCommand() {
    try {
      String[] rowInput = beautifyRowInput();

      return Command.find(rowInput[0]);

    } catch (Exception ex) {
      System.out.printf("bad command => %s]", _rowInput);
    }

    return null;
  }

  private String[] populateParameters() {
    String[] rowInput = beautifyRowInput();
    String[] parameters = new String[0];

    for (int i = 1; i < rowInput.length; i++) {
      parameters = arrayPush(parameters, rowInput[i]);
    }

    return parameters;
  }

  /**
   * Remove null and empty chars from the row input from the user
   */
  private String[] beautifyRowInput() {
    String[] cleanArray = new String[0];

    try {
      String[] strs = _rowInput.trim().split(KeysI.SPACE_K);

      if (strs != null && strs.length > 0) {
        for (int i = 0; i < strs.length; i++) {
          if (!isEmpty(strs[i].trim())) {
            cleanArray = arrayPush(cleanArray, strs[i].trim());
          }
        }
      }
    } catch (Exception ex) {
      System.out.printf("bad command %s", _rowInput);
    }

    return cleanArray;
  }

  public static void main(String[] args) {
    Request re = new Request("1 clear p1 p2    p3 ");
    System.out.printf("\ncommand = %s", re.getCommand());
    System.out.printf("\nparams = %s", Arrays.toString(re.getParameters()));
  }
}
