package com.jbrown.jnet.commands.action;

import java.awt.MouseInfo;

import javax.swing.SwingUtilities;

import com.jbrown.jnet.commands.action.AbstractAction.ActionPerformerI;
import com.jbrown.jnet.core.ErrorI;
import com.jbrown.jnet.core.RequestI;
import com.jbrown.jnet.response.DefaultResponse;
import com.jbrown.jnet.response.ResponseI;
import com.jbrown.jnet.utils.KeysI;

public class MouseMoveAction implements ActionPerformerI {

  @Override
  public ResponseI perform(final RequestI request, final ErrorI errors) {
    int x = getPosition(request, "x");
    int y = getPosition(request, "y");
    
    final int x1 = (int) MouseInfo.getPointerInfo().getLocation().getX() + x;
    final int y1 = (int) MouseInfo.getPointerInfo().getLocation().getY() + y;
    SwingUtilities.invokeLater(new Runnable() {
      
      @Override
      public void run() {
        request.getContext().getSharedResources().getRobot().mouseMove(x1, y1);
      }
    });
    
   
    return new DefaultResponse("true");
  }

  @Override
  public boolean validate(RequestI request, ErrorI errors) {
    int x = getPosition(request, "x");
    int y = getPosition(request, "y");
    
    if(x == KeysI.ERROR_CODE || y == KeysI.ERROR_CODE){
       errors.addErrors("value for 'x' and 'y' is missing."
           + "Example: http://jnethost:port/mouse?x=10&y=10");
       return false;
    }
    
    return true;
  }
  
  public static int getPosition(RequestI request, String key){
    String posStr = request.getHttpParams().get(key);
    int pos = KeysI.ERROR_CODE;
    
    try{
      pos = Integer.parseInt(posStr);
    }catch(Exception ex){
      ex.printStackTrace();
    }
    
    return pos;
  }

}
