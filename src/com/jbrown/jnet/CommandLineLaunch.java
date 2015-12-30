package com.jbrown.jnet;

import java.io.IOException;
import java.util.Scanner;

public class CommandLineLaunch {
  private JNetDelegate _delegate;

  public CommandLineLaunch(JNetDelegate delegate){
    _delegate = delegate;
  }

  public void processCommand(String[] cmd){
    LaunchOption opt = LaunchOption.find(cmd[0]);

    switch(opt){
      case HOST: this.host(); break;
      case LINK: this.link(); break;

      default:
        System.out.printf("Please jNet launch options are : %s",
            LaunchOption.getAllOptions());
    }
  }

  public void host(){
    try {
      Scanner sc = new Scanner(System.in);
      System.out.printf("Enter HOST:");
      String host = sc.nextLine();

      System.out.printf("Enter PORT:");
      int port = Integer.parseInt(sc.nextLine().trim());

      System.out.printf("Creating jNet server for host: %s on port:%s\n",
          host, port);

      JNetServer server =
          _delegate.getJNetServerSpace().createServer(host, port);

      System.out.printf("jNet server created. Instance-Id: %s\n",
          server.getJNetAddress());

      server.startServer();

      System.out.printf("jNet server-id: %s is ready!!\n", server.getJNetAddress());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void link(){
    try {
      Scanner sc = new Scanner(System.in);
      System.out.printf("Enter HOST:");
      String host = sc.nextLine();

      System.out.printf("Enter PORT:");
      int port = Integer.parseInt(sc.nextLine().trim());

      System.out.printf("Creating jNet link to host: %s on port:%s\n",
          host, port);

      _delegate.getJNetLinker().startLinker(host, port);

      System.out.printf("jNet link to %s:%s is established!!\n",
          host, port);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
