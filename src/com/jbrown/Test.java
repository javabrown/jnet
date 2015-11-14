package com.jbrown;

import java.net.UnknownHostException;

public class Test {
  public static void main(String[] args) throws UnknownHostException{
    String line = " set name set raja khan set ".trim();
    String[] arr = line.split(" ");

    String command = arr[0];
    String key = arr[1];

    String value = line.replaceAll(".*\\b"+ command +"\\b","");
           value = line.replaceAll(".*\\b"+ key +"\\b","").trim();

    System.out.printf("command=%s | Key=%s | value=%s", command, key, value);
  }

}
