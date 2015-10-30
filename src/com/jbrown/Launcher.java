package com.jbrown;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.jbrown.jnet.core.BrownServer;


public class Launcher {
  private final BrownServer _server;
  private LaunchFrame _frame;
  
  private static int PORT = 22;
  
  public Launcher() throws IOException {
    _server = new BrownServer(PORT);
    _frame = new LaunchFrame();
  }
  
  public boolean start() {
    if(_server == null || _server.isRunning()){
      return false;
    }
    
    new Thread(_server).start();
    
    return true;
  }
  
  public boolean stop() {
    if(_server == null || !_server.isRunning()){
      return false;
    }
    
    return _server.stop();
  }
  
  class LaunchFrame extends JFrame implements ActionListener{
    private JButton _start;
    private JButton _stop;
    private JLabel _statusLabel;
    
    static final String START = "Start";
    static final String STOP = "Stop";
    
    public LaunchFrame(){
      _start = new JButton(START);
      _stop = new JButton(STOP);
      _statusLabel = new JLabel(getStatus());
      
      JPanel container = (JPanel) getContentPane();
      container.setLayout(new GridLayout(3,1));
      
      container.add(_statusLabel);
      container.add(_start);
      container.add(_stop);
      
      _start.setActionCommand(START);
      _stop.setActionCommand(STOP);
      _start.addActionListener(this);
      _stop.addActionListener(this);
      
      setUIFont(new FontUIResource(new Font("Arial", 0, 20)));
      setSize(100, 100);
      setDefaultCloseOperation(3);
      setVisible(true);
    }
    
    public void setUIFont(FontUIResource f) {
      Enumeration  keys = UIManager.getDefaults().keys();
      while (keys.hasMoreElements()) {
          Object key = keys.nextElement();
          Object value = UIManager.get(key);
          if (value instanceof FontUIResource) {
              FontUIResource orig = (FontUIResource) value;
              Font font = new Font(f.getFontName(), orig.getStyle(), f.getSize());
              UIManager.put(key, new FontUIResource(font));
          }
      }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
      if(e.getActionCommand().equalsIgnoreCase(START)) {
        start();
      }
      
      if(e.getActionCommand().equalsIgnoreCase(STOP)) {
        stop();
      }
      
      _statusLabel.setText(getStatus());
    }
    
    private String getStatus(){
      return _server.isRunning() ? "Running" : "Stopped";
    }
    
  }
}

