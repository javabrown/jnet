package com.jbrown.jnet.client.task;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.Transferable;
import java.io.IOException;

import com.jbrown.jnet.client.ClientSocket;
import com.jbrown.jnet.client.framework.Task;
import com.jbrown.jnet.core.Command;
import com.jbrown.jnet.ui.ClipBoard;
import com.jbrown.jnet.utils.StringUtils;
import com.jbrown.jnet.utils.Utils;

public class ClipSetterTask extends Task implements FlavorListener, ClipboardOwner {
  private String _clipContent;
  private ClipBoard _clipboard;

  public ClipSetterTask() {
    _clipContent = "";
    _clipboard = new ClipBoard();
  }

  @Override
  public void flavorsChanged(FlavorEvent e) {
    //String clipContent = _clipboard.getData();//Utils.getClipboardContents();

    //if (!StringUtils.isEmpty(clipContent) && !_clipContent.equals(clipContent)){
    //  _clipContent = clipContent;
    //  //submit();
    //}
  }

  private String submit(ClientSocket socket) {
    String response = "";

    try {
      response = socket.executeCommand(_clipContent);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return response;
  }

  @Override
  public String execute(ClientSocket socket) {
    String clipContent = _clipboard.getData();

    //clipContent = clipContent.replaceAll("\n\r", System.getProperty("line.separator"));

    boolean isNewContent = false;

    if (!StringUtils.isEmpty(clipContent) && !_clipContent.equals(clipContent)){
      _clipContent = clipContent;
      isNewContent = true;
    }

    String response = "";

    if(isNewContent){
      try {
        response = socket.executeCommand(String.format("%s %s", Command.CLIP.getName(), _clipContent));
        //response = socket.executeCommand(String.format("%s %s",
        //        Command.CLIP.getName(), Utils.encrypt(_clipContent)));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return response;
  }

  @Override
  public void lostOwnership(Clipboard clipboard, Transferable contents) {
    // TODO Auto-generated method stub

  }
}