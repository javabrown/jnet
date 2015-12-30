package com.jbrown.jnet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jbrown.jnet.utils.StringUtils;

public class Main {
  public static void main(String[] args) {
    try {
      if(!StringUtils.isEmpty(args) && LaunchOption.NONE.typeOf(args[0])){
        System.err.printf("Inavlid option! Valid jNet options are: %s",
            LaunchOption.getAllOptions());
        System.exit(0);
      }
      else if(!StringUtils.isEmpty(args))
      {
        new CommandLineLaunch(JNetDelegate.getInstance()).processCommand(args);
      }
      else
      {
        new LaunchFrame(JNetDelegate.getInstance()).launch();
      }
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
    }
  }
}
