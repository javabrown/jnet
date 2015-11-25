package com.jbrown.jnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Trivial client for the date server.
 */
public class Test1 {

    public static void main(String[] args) throws IOException {
        String serverAddress = "192.168.8.130";
        Socket s = new Socket(serverAddress, 22);

        OutputStream os = s.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);
        pw.print("help\r");

        BufferedReader input =
            new BufferedReader(new InputStreamReader(s.getInputStream()));
        String answer = input.readLine();

        input.close();
        os.close();

        System.out.println(answer);
        timer();

        JOptionPane.showMessageDialog(new JFrame(), "Wait");
        System.exit(0);
    }

    public static void timer(){
      Timer timer = new Timer();
      timer.schedule(new SayHello(), 0, 1000);
    }
}

class SayHello extends TimerTask {
  public void run() {
     System.out.printf("\nHello World [ %s ]!", new Date());
  }
}
