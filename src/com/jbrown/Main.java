package com.jbrown;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {
  public static void main(String[] args) {
    try {
      new Launcher();
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
    }
  }
}
