package com.jbrown.jnet.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

import com.jbrown.jnet.utils.KeysI;
import com.jbrown.jnet.utils.StringUtils;

public class ObjetiveJNetTester {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String serverAddress = "192.168.8.130";
        Socket s = new Socket(serverAddress, 22);

        ObjectOutputStream writer = new ObjectOutputStream(s.getOutputStream());

        ObjectInputStream reader = new ObjectInputStream( s.getInputStream());


        //-----(1)------
        runCommand(reader, writer, String.format("%s\r", "ping"));
        //-------------

        //-----(2)------
        runCommand(reader, writer, String.format("%s\r", "who"));
        //-------------


        runCommand(reader, writer, String.format("%s\r", "quit"));

        System.out.println("**Jnet Test done!! **");
        System.exit(0);
    }

    static void runCommand(ObjectInputStream reader, ObjectOutputStream writer,
        String cmd) throws IOException, ClassNotFoundException{
      cmd = String.format("%s\r", "who");
      writer.writeObject(cmd);
      writer.flush();

      String answer = read(reader);
      System.out.printf("Command = %s \n Result = %s", cmd, answer);
    }

    private static String read(ObjectInputStream reader) throws IOException, ClassNotFoundException {
      //StringBuilder builder = new StringBuilder();
      Object obj = reader.readObject();

//      while ((aux = reader.readObject()) != null) {
//          String resp = aux.trim();
//          if(resp.equalsIgnoreCase("END")){
//            break;
//          }
//
//          if(StringUtils.isEmpty(resp) || resp.equalsIgnoreCase(KeysI.PROMPT_K1)){
//            continue;
//          }
//
//          builder.append(aux);
//      }

      return obj.toString();
    }
}