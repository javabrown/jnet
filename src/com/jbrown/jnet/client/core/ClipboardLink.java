package com.jbrown.jnet.client.core;

import static com.jbrown.jnet.utils.StringUtils.isEmpty;
import static com.jbrown.jnet.utils.StringUtils.isEquals;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.jbrown.jnet.client.Task;
import com.jbrown.jnet.client.core.ClipboardLink.InnerTask;
import com.jbrown.jnet.client.task.ClipFetcherTask;
import com.jbrown.jnet.client.task.ClipSetterTask;
import com.jbrown.jnet.core.Command;
import com.jbrown.jnet.ui.ClipBoard;
import com.jbrown.jnet.utils.KeysI;
import com.jbrown.jnet.utils.StringUtils;
import com.jbrown.jnet.utils.Utils;

public class ClipboardLink extends TimerTask implements LinkTaskI {
  private Timer _timer;
  private JNetConnectorI _jNetLink;

  private InnerTask _clipSetter;
  private InnerTask _clipFetcher;

  private static int SECONDS = 3 * 1000;

  public ClipboardLink(JNetConnectorI jNetLink) {
    _timer = new Timer();
    _jNetLink = jNetLink;

    _clipSetter = new ClipSetter();
    _clipFetcher = new ClipFetcher();
  }


  public InnerTask getClipSetter() {
    return _clipSetter;
  }

  @Override
  public void link() {
    allocResource();

    TimerTask executer = getExecuter();
    _timer.schedule(executer, 0, 1000);
  }

  @Override
  public void unlink() {
    _timer.cancel();

    releaseResource();
  }

  @Override
  public void run() {
    getExecuter().run();
  }

  private TimerTask getExecuter() {
    InnerTask[] tasks = new InnerTask[]{_clipSetter, _clipFetcher};
    return  new LinkTaskExecuter(_jNetLink, tasks);
  }

  private void allocResource() {
    Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(
        (ClipSetter)_clipSetter);
  }

  private void releaseResource() {
    Toolkit.getDefaultToolkit().getSystemClipboard().removeFlavorListener(
        (ClipSetter)_clipSetter);
  }

  interface InnerTask{
    String trigger(JNetConnectorI connector);
  }
}


class LinkTaskExecuter extends TimerTask {
  private InnerTask[] _tasks;
  private JNetConnectorI _jNetConnector;

  public LinkTaskExecuter(JNetConnectorI jNetConnector, InnerTask...tasks){
    _jNetConnector = jNetConnector;
    _tasks = tasks;
  }

  public void execute() {
    for(InnerTask task : _tasks){
      String response = task.trigger(_jNetConnector);

      if(!StringUtils.isEmpty(response)){
        System.out.printf("\nExecute|%s|[%s]", new Date(), response);
      }
    }
  }

  @Override
  public void run() {
    execute();
  }
}

class ClipFetcher implements InnerTask {
  private ClipBoard _clipboard;

  public ClipFetcher(){
    _clipboard = new ClipBoard();
  }

  @Override
  public String trigger(JNetConnectorI connector) {
    String newClip = "";

    try {
      newClip = connector.executeCommand(String.format("%s", Command.CLIP));
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


class ClipSetter implements FlavorListener, ClipboardOwner, InnerTask {
  private String _clipContent;
  private ClipBoard _clipboard;

  public ClipSetter() {
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

  private String submit(JNetConnector socket) {
    String response = "";

    try {
      response = socket.executeCommand(_clipContent);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return response;
  }

  @Override
  public void lostOwnership(Clipboard clipboard, Transferable contents) {
  }

  @Override
  public String trigger(JNetConnectorI connector) {
    String clipData = _clipboard.getData();

    if (isEmpty(clipData)) {
      return KeysI.EMPTY_K;
    }

    String romoteData = KeysI.EMPTY_K;

    try {
      romoteData = connector.executeCommand(Command.CLIP.getName());
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (!isEmpty(romoteData) && isEquals(romoteData, clipData)) {
       return KeysI.EMPTY_K;
    }

    try {
      romoteData = connector.executeCommand(
          String.format("%s %s", Command.CLIP.getName(), clipData));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return romoteData;
 }
}