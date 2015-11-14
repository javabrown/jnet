package com.jbrown.jnet.utils;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

public class Utils {
  public static String getIP() {
    try {
      InetAddress ip = InetAddress.getLocalHost();
      return ip.getHostAddress().toString();
    } catch (UnknownHostException e) {
    }

    return KeysI.LOCAL_HOST;
  }

  public static String getIPAddress(InetAddress ip) {
    if (ip != null) {
      return ip.getHostAddress().toString();
    }

    return null;
  }

  public static byte[] getKeyBytes(String k) {
    try {
      return k.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public static Collection<byte[]> getKeyBytes(Collection<String> keys) {
    Collection<byte[]> rv = new ArrayList<byte[]>(keys.size());
    for (String s : keys) {
      rv.add(getKeyBytes(s));
    }
    return rv;
  }
}
