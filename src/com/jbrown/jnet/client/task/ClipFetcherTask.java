package com.jbrown.jnet.client.task;

import java.io.IOException;

import com.jbrown.jnet.client.Task;
import com.jbrown.jnet.client.core.ClientSocket;
import com.jbrown.jnet.core.Command;
import com.jbrown.jnet.ui.ClipBoard;
import com.jbrown.jnet.utils.KeysI;
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
    String newClip = "";

    try {
      newClip = socket.executeCommand(String.format("%s", Command.CLIP));
    } catch (IOException e) {
      e.printStackTrace();
    }

    if( isEmpty(newClip)){
      return KeysI.EMPTY_K;
    }

    String oldClip = _clipboard.getData();

    if(!isEmpty(oldClip) && isEquals(oldClip, newClip)) {
        return KeysI.EMPTY_K;
    }

    Utils.setClipboardContents(newClip);

    return newClip;
  }

}