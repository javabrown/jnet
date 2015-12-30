package com.jbrown.jnet.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyMaker {
  public static String getServerKey(String host, int port) {
    return String.format("JNetServer-Thread-[%s:%s]", host, port);
  }

  public static Pair<String, String> getServerAddress(String serverKey) {
    String pattern = "\\[(.*?)\\]";
    Pattern r = Pattern.compile(pattern);
    Matcher m = r.matcher(serverKey);
    Pair<String, String> pair = null;

    if (m.find()) {
      String address = m.group(0);
      System.out.println("Found value: " + address);
      String[] arr = address.split(KeysI.COLON_K);

      if (!StringUtils.isEmpty(arr)) {
        String host = arr[0].replace('[', ' ').trim();
        String port = arr[1].replace(']', ' ').trim();
        pair = new Pair<String, String>(host, port);
      }
    }

    return pair;
  }

//  public static void main(String[] args) {
//    String serverKey = getServerKey(" 192.168.1.2", 24);
//    System.out.printf("Key = %s\n", serverKey);
//
//    Pair<String, String> address = getServerAddress(serverKey);
//
//    if(address != null) {
//      System.out.printf("HOST = %s\nPORT = %s",
//          address.getLeft(), address.getRight());
//    }
//  }

}
