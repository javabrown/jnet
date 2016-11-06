package com.jbrown.jnet.core;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.io.FileWriter;

public class SharedResources {
    private Robot _robot;
    private FileWriter _fileWriter;
    
    public SharedResources(){
      try {
        _robot = new Robot(MouseInfo.getPointerInfo().getDevice());
      } catch (HeadlessException | AWTException e) {
        e.printStackTrace();
      }
    }
    
    public Robot getRobot(){
      return _robot;
    }
}
