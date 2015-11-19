package com.jbrown.jnet.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import com.jbrown.jnet.utils.KeysI;
import com.jbrown.jnet.utils.StringUtils;

public class JNetTester {

    public static void main(String[] args) throws IOException {
        String serverAddress = "192.168.8.130";
        Socket s = new Socket(serverAddress, 22);

        PrintStream writer = new PrintStream(s.getOutputStream(), true);

        BufferedReader reader = new BufferedReader(
            new InputStreamReader( s.getInputStream(), "UTF-8"));

        writer.printf("%s\r", "ping");
        //writer.flush();

        String answer = read(reader);

        System.out.println(answer);


        writer.printf("%s\r", "quit");
        writer.flush();

        writer.close();
        reader.close();
        s.close();

        System.exit(0);
    }

    private static String read(BufferedReader reader) throws IOException {
      StringBuilder builder = new StringBuilder();
      String aux = "";

      while ((aux = reader.readLine()) != null) {
          String resp = aux.trim();
          if(resp.equalsIgnoreCase("END")){
            break;
          }

          if(StringUtils.isEmpty(resp) || resp.equalsIgnoreCase(KeysI.PROMPT_K1)){
            continue;
          }

          builder.append(aux);
      }

      return builder.toString();
    }
}