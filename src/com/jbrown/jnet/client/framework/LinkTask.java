package com.jbrown.jnet.client.framework;

import java.awt.Toolkit;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.jbrown.jnet.client.ClientSocket;
import com.jbrown.jnet.client.task.ClipFetcherTask;
import com.jbrown.jnet.client.task.ClipSetterTask;
import com.jbrown.jnet.utils.StringUtils;

public class LinkTask extends TimerTask implements LinkTaskI {
  private Timer _timer;
  private ClientSocket _client;

  private Task _clipSetter;
  private Task _clipFetcher;

  private static int SECONDS = 3 * 1000;

  public LinkTask(ClientSocket client){
    _timer = new Timer();
    _client = client;

    _clipSetter = new ClipSetterTask();
    _clipFetcher = new ClipFetcherTask();
  }

  @Override
  public Task getClipSetter() {
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

  private TimerTask getExecuter(){
    Task[] tasks = new Task[]{_clipSetter, _clipFetcher};
    return  new LinkTaskExecuter(_client, tasks);
  }

  private void allocResource(){
    Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(
        (ClipSetterTask)_clipSetter);
  }

  private void releaseResource(){
    Toolkit.getDefaultToolkit().getSystemClipboard().removeFlavorListener(
        (ClipSetterTask)_clipSetter);
  }
}


class LinkTaskExecuter extends TimerTask {
  private Task[] _tasks;
  private ClientSocket _client;

  public LinkTaskExecuter(ClientSocket client, Task...tasks){
    _client = client;
    _tasks = tasks;
  }

  public void execute() {
    for(Task task : _tasks){
      String response = task.execute(_client);

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


//class SyncBoard extends Observable {
//  private String watchedValue;
//
//  public ClipBoard(String value) {
//    watchedValue = value;
//  }
//
//  public void watch() {
//    Toolkit  toolkit = Toolkit.getDefaultToolkit();
//    Timer timer = new Timer();
//    int second = 1;
//    timer.schedule(new RemindTask(), second * 1000);
//  }
//
//  public void hello(){
//    System.out.printf("Hello - [%s]", new Date());
//  }
//}
//
//class RemindTask extends TimerTask {
//
//
//  public void run() {
//    System.out.printf("Hello - [%s]", new Date());
//  }
//}