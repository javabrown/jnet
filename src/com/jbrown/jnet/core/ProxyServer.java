package com.jbrown.jnet.core;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

public class ProxyServer {
  private String _host;
  private int _remotePort;
  private int _localPort;
  boolean _listening;

  public ProxyServer(String host, int remotePort, int localPort) {
    _host = host;
    _remotePort = remotePort;
    _localPort = localPort;
    _listening = true;
  }

  public void start() {

    try {
      System.out.printf("Starting proxy for %s:%s  on port %s", _host,
          _remotePort, _localPort);

      ServerSocket serverSocket = new ServerSocket(_localPort);

      while (_listening) {
        new ProxyThread(serverSocket.accept()).start();
      }

      serverSocket.close();

    } catch (Exception e) {
      System.err.println(e);
      System.err.println("Usage: java ProxyMultiThread "
          + "<host> <remoteport> <localport>");
    }
  }

}

class ProxyThread extends Thread {
  private Socket socket = null;
  private static final int BUFFER_SIZE = 32768;
  public ProxyThread(Socket socket) {
      super("ProxyThread");
      this.socket = socket;
  }

  public void run() {
      //get input from user
      //send request to server
      //get response from server
      //send response to user

      try {
          DataOutputStream out = new DataOutputStream(socket.getOutputStream());
          BufferedReader in =
              new BufferedReader(new InputStreamReader(socket.getInputStream()));

          String inputLine, outputLine;
          int cnt = 0;
          String urlToCall = "";
          ///////////////////////////////////
          //begin get request from client
          while ((inputLine = in.readLine()) != null) {
              try {
                  StringTokenizer tok = new StringTokenizer(inputLine);
                  tok.nextToken();
              } catch (Exception e) {
                  break;
              }
              //parse the first line of the request to find the url
              if (cnt == 0) {
                  String[] tokens = inputLine.split(" ");
                  urlToCall = tokens[1];
                  //can redirect this to output log
                  System.out.println("Request for : " + urlToCall);
              }

              cnt++;
          }
          //end get request from client
          ///////////////////////////////////


          BufferedReader rd = null;
          try {
            String resp = callURL(urlToCall);
            byte[] bytes = resp.getBytes();
            out.write(bytes);
            out.flush();
          } catch (Exception e) {
              System.err.println("Encountered exception: " + e);
              out.writeBytes("");
          }


          if (rd != null) {
              rd.close();
          }
          if (out != null) {
              out.close();
          }
          if (in != null) {
              in.close();
          }
          if (socket != null) {
              socket.close();
          }

      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  public String callURL(String myURL) {

    StringBuilder sb = new StringBuilder();
    URLConnection urlConn = null;
    InputStreamReader in = null;
    try {
      URL url = new URL(myURL);
      urlConn = url.openConnection();
      if (urlConn != null)
        urlConn.setReadTimeout(60 * 1000);
      if (urlConn != null && urlConn.getInputStream() != null) {
        in = new InputStreamReader(urlConn.getInputStream(),
            Charset.defaultCharset());
        BufferedReader bufferedReader = new BufferedReader(in);
        if (bufferedReader != null) {
          int cp;
          while ((cp = bufferedReader.read()) != -1) {
            sb.append((char) cp);
          }
          bufferedReader.close();
        }
      }
    in.close();
    } catch (Exception e) {
      sb.append(e.getMessage());
    }

    return sb.toString();
  }
}