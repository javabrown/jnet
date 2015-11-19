package com.jbrown.jnet.utils;

public class IpEncoder {
  private String _ip;

  public IpEncoder(String ip) {
    _ip = ip;
  }

  public String getIp(){
    return _ip;
  }

  public long toLong() {
    String[] ipAddressInArray = _ip.split("\\.");

    long result = 0;
    for (int i = 0; i < ipAddressInArray.length; i++) {

      int power = 3 - i;
      int ip = Integer.parseInt(ipAddressInArray[i]);
      result += ip * Math.pow(256, power);

    }

    return result;
  }

  public long toLong2() {
    long result = 0;

    String[] ipAddressInArray = _ip.split("\\.");

    for (int i = 3; i >= 0; i--) {
      long ip = Long.parseLong(ipAddressInArray[3 - i]);
      result |= ip << (i * 8);
    }

    return result;
  }

  public String toIp(long i) {
    return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
        + ((i >> 8) & 0xFF) + "." + (i & 0xFF);
  }

  public String toIp2(long ip) {
    StringBuilder sb = new StringBuilder(15);

    for (int i = 0; i < 4; i++) {
      sb.insert(0, Long.toString(ip & 0xff));

      if (i < 3) {
        sb.insert(0, '.');
      }

      ip = ip >> 8;
    }

    return sb.toString();
  }


  public static void main(String[] args){
    IpEncoder ip = new IpEncoder("192.168.8.130");
    long ipLong = ip.toLong();

    System.out.printf("%s ==> %s\n", ip.getIp(), ipLong);
    System.out.printf("%s ==> %s\n", ipLong, ip.toIp2(ipLong));
  }
}
