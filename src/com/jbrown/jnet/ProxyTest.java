package com.jbrown.jnet;

import com.jbrown.jnet.core.ProxyServer;

public class ProxyTest {
  public static void main(String[] args){
    new ProxyServer("192.168.8.130", 22, 9999).start();
  }
}
