package com.jbrown.jnet.ui;

import static com.jbrown.jnet.utils.KeysI.COMMAND_EXIT_K;
import static com.jbrown.jnet.utils.KeysI.COMMAND_HOST_K;
import static com.jbrown.jnet.utils.KeysI.COMMAND_STOP_K;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.JFrame;

import com.jbrown.jnet.LaunchFrame;
import com.jbrown.jnet.Main;

public class WinTray {

  public enum Status {
    RUNNING("Running in host mode", "/icons/brown-logo.png"),
    LINKED("Running in linked mode", "/icons/brown-logo.png"),
    NOT_RUNNING("Not Running", "/icons/brown-logo.png");

    String _text;
    String _imageIcon;

    Status(String text, String imageIcon) {
      _text = text;
      _imageIcon = imageIcon;
    }

    String getText() {
      return _text;
    }

    Image getImage() {
      URL resource = Main.class.getResource(_imageIcon);
      Image image = Toolkit.getDefaultToolkit().getImage(resource);
      return image;
    }
  };

  private SystemTray _systemTray;
  private TrayIcon _trayIcon;
  private PopupMenu _trayPopupMenu;

  private Label _trayLabel;
  private MenuItem _startMenu;
  private MenuItem _stopMenu;
  private MenuItem _exitMenu;

  final JFrame _frame;
  final ActionListener _listener;

  public WinTray(final LaunchFrame frame, final ActionListener listener) {
    _frame = frame;
    _listener = listener;

    if (!SystemTray.isSupported()) {
      System.out.println("System tray is not supported !!! ");
      return;
    }

    _systemTray = SystemTray.getSystemTray();
    _trayPopupMenu = new PopupMenu("jNet");

    _trayLabel = new Label("jNet");

    _startMenu = new MenuItem(COMMAND_HOST_K, new MenuShortcut('S'));
    _stopMenu = new MenuItem(COMMAND_STOP_K, new MenuShortcut('o'));
    _exitMenu = new MenuItem(COMMAND_EXIT_K, new MenuShortcut('E'));
  }

  public boolean isSupported(){
    return SystemTray.isSupported();
  }

  public void launchTray() {
    this.setTrayMenu();

    _trayIcon = new TrayIcon(Status.NOT_RUNNING.getImage(),
        Status.NOT_RUNNING.getText(), _trayPopupMenu);
    _trayIcon.setImageAutoSize(true);

    _trayIcon.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          _frame.setVisible(true);
        }
      }
    });

    try {
      _systemTray.add(_trayIcon);
    } catch (AWTException awtException) {
      awtException.printStackTrace();
    }
  }

  public void setActivityStatus(Status status) {
    _trayIcon.setImage(status.getImage());
    _trayIcon.setToolTip(status.getText());
  }

  public void setTrayMenu(){
    _trayPopupMenu.setFont(new Font("Verdana", 0, 13));

    _trayPopupMenu.add("jNet Connector");
    _trayPopupMenu.addSeparator();

    _trayPopupMenu.add(_startMenu);
    _startMenu.setActionCommand(COMMAND_HOST_K);
    _startMenu.addActionListener(_listener);

    _trayPopupMenu.add(_stopMenu);
    _stopMenu.setActionCommand(COMMAND_STOP_K);
    _stopMenu.addActionListener(_listener);

    _trayPopupMenu.addSeparator();

    _trayPopupMenu.add(_exitMenu);
    _exitMenu.setActionCommand(COMMAND_EXIT_K);
    _exitMenu.addActionListener(_listener);
  }
}