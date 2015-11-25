package com.jbrown.jnet.client.task;

import java.io.IOException;

import com.jbrown.jnet.client.ClientSocket;
import com.jbrown.jnet.client.framework.Task;
import com.jbrown.jnet.core.Command;
import com.jbrown.jnet.ui.ClipBoard;
import com.jbrown.jnet.utils.Utils;

import static com.jbrown.jnet.utils.StringUtils.isEquals;
import static com.jbrown.jnet.utils.StringUtils.isEmpty;

public class ClipFetcherTask extends Task {
  private ClipBoard _clipboard;

  public ClipFetcherTask(){
    _clipboard = new ClipBoard();
  }

  @Override
  public String execute(ClientSocket socket) {
    String newContent = "";

    try {
      newContent = socket.executeCommand(String.format("%s", Command.CLIP));
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (!isEmpty(newContent)) {
      String existingContent = _clipboard.getData();//Utils.getClipboardContents();

      if (!isEquals(newContent, existingContent)) {
        Utils.setClipboardContents(newContent);
      }
    }

    return newContent;
  }

}