package com.jbrown.jnet;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import com.jbrown.jnet.core.JnetContainer;
import com.jbrown.jnet.ui.MoveMouseListener;
import com.jbrown.jnet.ui.SpringUtilities;
import com.jbrown.jnet.ui.WinTray;
import com.jbrown.jnet.utils.KeysI;
import com.jbrown.jnet.utils.Utils;

import static com.jbrown.jnet.utils.KeysI.*;

public class LaunchFrame extends JFrame implements ActionListener {
  private JButton _start;
  private JButton _link;
  private JButton _stop;
  private JButton _exit;

  private JLabel _statusLabel;

  private JTextField _hostField;
  private JLabel _hostLabel;

  private JTextField _portField;
  private JLabel _portLabel;

  private WinTray _winTray; //RK : For now lazy initialization is needed
  private JnetContainer _server;

  public static void main(String[] args) {
    new LaunchFrame();
  }

  public LaunchFrame() {
    _start = new JButton(COMMAND_HOST_K);
    _link = new JButton(COMMAND_LINK_K);
    _stop = new JButton(COMMAND_STOP_K);
    _exit = new JButton(COMMAND_EXIT_K);

    _hostLabel = new JLabel("HOST");
    _hostField = new JTextField(Utils.getIP(), 10);

    _portLabel = new JLabel("PORT");
    _portField = new JTextField("22", 4);


    InputStream inputStream = this.getClass().getResourceAsStream(
        "/icons/brown-logo.png");
    BufferedInputStream in = new BufferedInputStream(inputStream);

    Image image = null;
    try {
      image = ImageIO.read(in);
      image = image.getScaledInstance(28, 28,Image.SCALE_AREA_AVERAGING);
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, e.getMessage());
    }

    if(image != null){
      _statusLabel = new JLabel(KeysI.PROMPT_K, new ImageIcon(image), JLabel.LEADING);
    }
    else{
      _statusLabel = new JLabel(KeysI.PROMPT_K, JLabel.CENTER);
    }

    JPanel container = (JPanel) getContentPane();
    container.setLayout(new BorderLayout(3, 1));
    container.add(getControl());
  }

  public void launch(){
    this.initFrame();

    _winTray = new WinTray(this, this);
    _winTray.launchTray();
  }

  private void setEditableHost(boolean editable){
    _hostField.setEditable(editable);
    _portField.setEditable(editable);
    _start.setEnabled(editable);
  }

  public void startServer(){
    setEditableHost(false);

    try {
      _server = new JnetContainer(_hostField.getText(),
          Integer.parseInt(_portField.getText()));
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          _server.start();
        }
      });

    } catch (NumberFormatException | IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, e.getMessage());

        if(_server == null || !_server.isRunning()){
          setEditableHost(true);
        }
    }
  }

  public void stopServer(){
    try {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          _server.stop();
        }
      });

    } catch (NumberFormatException e) {
    }

    _hostField.setEditable(true);
    _portField.setEditable(true);
    _start.setEnabled(true);
  }

  private Component getControl() {
    JPanel jp = new JPanel();

    jp.setLayout(new SpringLayout());
    jp.add(_statusLabel);
    jp.add(new JSeparator());

    JPanel txtJp = new JPanel();
    txtJp.setLayout(new FlowLayout(FlowLayout.LEFT));
    txtJp.add(_hostLabel);
    txtJp.add(_hostField);
    txtJp.add(new JSeparator());
    txtJp.add(_portLabel);
    txtJp.add(_portField);


    JPanel btnJp = new JPanel();
    btnJp.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
    btnJp.setLayout(new FlowLayout(FlowLayout.CENTER));
    btnJp.add(_start);
    btnJp.add(new JSeparator(JSeparator.VERTICAL));
    btnJp.add(_link);
    btnJp.add(new JSeparator(JSeparator.VERTICAL));
    btnJp.add(_stop);
    btnJp.add(new JSeparator(JSeparator.VERTICAL));
    btnJp.add(_exit);

    jp.add(txtJp);
    jp.add(btnJp);


    SpringUtilities.makeCompactGrid(jp, 4, 1, // rows, cols
        6, 6, // initX, initY
        6, 6); // padX, padY

    setUIFont();

    return jp;
  }

  private void initFrame() {
    int state = this.getExtendedState();
    state = state | this.ICONIFIED;
    super.setExtendedState(state);
    this.setIcon();
    this.setLocation();
    super.setTitle(PROMPT_K);
    super.setDefaultCloseOperation(HIDE_ON_CLOSE);
    super.setSize(305, 190);
    super.setResizable(false);
    super.setAlwaysOnTop(true);

    //super.setVisible(true);

//    try {
//      UIManager.setLookAndFeel(
//          UIManager.getSystemLookAndFeelClassName());
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
    initEventListeners();
  }

  private void initEventListeners(){
    MoveMouseListener mml = new MoveMouseListener(this.getRootPane());
    this.addMouseListener(mml);
    this.addMouseMotionListener(mml);

    if(SystemTray.isSupported()){
      //this.setUndecorated(true);

      this.addWindowListener(new WindowAdapter() {
        public void windowDeactivated(WindowEvent e) {
          e.getWindow().setVisible(false);
        }
      });

      ((JPanel)this.getContentPane()).setBorder(
          new BevelBorder(BevelBorder.LOWERED));
      this.getRootPane().setBorder(new BevelBorder(EtchedBorder.RAISED));
    }

    _start.setActionCommand(COMMAND_HOST_K);
    _start.setActionCommand(COMMAND_LINK_K);
    _stop.setActionCommand(COMMAND_STOP_K);
    _exit.setActionCommand(COMMAND_EXIT_K);

    _start.addActionListener(this);
    _link.addActionListener(this);
    _stop.addActionListener(this);
    _exit.addActionListener(this);
  }

  private void setIcon() {
    try {
      List<Image> icons = new ArrayList<Image>();

      InputStream inputStream = this.getClass().getResourceAsStream(
          "/icons/brown-logo.png");
      BufferedInputStream in = new BufferedInputStream(inputStream);
      Image image = ImageIO.read(in);
      icons.add(image);
      this.setIconImages(icons);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void setUIFont() {
    _statusLabel.setFont(new Font("Verdana", 0, 20));

    _hostLabel.setFont(new Font("Verdana", 1, 10));
    _hostField.setFont(new Font("Verdana", 0, 12));

    _portLabel.setFont(new Font("Verdana", 1, 10));
    _portField.setFont(new Font("Verdana", 0, 12));
  }

  private void setLocation() {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
    Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
    int x = (int) rect.getMinX();
    int y = (int) rect.getMaxY() - super.getHeight();
    //super.setLocation(x, y);

    Dimension scrSize = rect.getSize();
    super.setLocation( (int) (scrSize.width - getWidth()) - 100, scrSize.height - getHeight());

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equalsIgnoreCase(COMMAND_HOST_K)) {
       _winTray.setActivityStatus(WinTray.Status.RUNNING);
       this.startServer();
    }

    if (e.getActionCommand().equalsIgnoreCase(COMMAND_LINK_K)) {
      _winTray.setActivityStatus(WinTray.Status.LINKED);
      this.startServer();
    }

    if (e.getActionCommand().equalsIgnoreCase(COMMAND_STOP_K)) {
      _winTray.setActivityStatus(WinTray.Status.NOT_RUNNING);
      this.stopServer();
    }

    if (e.getActionCommand().equalsIgnoreCase(COMMAND_EXIT_K)) {
      System.exit(0);
    }

    //_statusLabel.setText(getStatus());
  }
}


