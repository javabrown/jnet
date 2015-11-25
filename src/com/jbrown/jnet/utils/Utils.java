package com.jbrown.jnet.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

import sun.awt.windows.WClipboard;

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

  public static void setClipboardContents(String content) {
    StringSelection stringSelection = new StringSelection(content);
    Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
    clpbrd.setContents(stringSelection, null);
  }

  public static String getClipboardContents() {
    String result = "";
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    Transferable contents = clipboard.getContents(null);
    boolean hasTransferableText = (contents != null)
        && contents.isDataFlavorSupported(DataFlavor.stringFlavor);

    if (hasTransferableText) {
        try {
          result = (String) contents
              .getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
    }

    return result;
  }

  public static String getClipboardContents(Clipboard clipboardSource) {
    String result = "";

    if(clipboardSource == null) {
      clipboardSource = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    Transferable contents = clipboardSource.getContents(null);

    boolean hasTransferableText = (contents != null)
        && contents.isDataFlavorSupported(DataFlavor.stringFlavor);

    if (hasTransferableText) {
        try {
          result = (String) contents
              .getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
    }

    return result;
  }

}
