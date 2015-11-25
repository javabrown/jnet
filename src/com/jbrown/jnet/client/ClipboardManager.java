package com.jbrown.jnet.client;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Date;
import java.util.Observable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

import com.jbrown.jnet.utils.Utils;


public class ClipboardManager implements Observer {
  private Clip _clipboard = null;
  private ArrayList<Entry> _entries;
  private HashMap<String, String> _profiles;

  public ClipboardManager(){
    _clipboard = new Clip();
    _entries = new ArrayList<Entry>();
    _profiles = new HashMap<String, String>();

    initClipboard();
    addManagerToObservers();

    Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(new FlavorListener() {
      @Override
      public void flavorsChanged(FlavorEvent e) {

         System.out.println("ClipBoard UPDATED: " + e.getSource() + " " + e.toString());



        System.out.println(
            Utils.getClipboardContents((Clipboard)e.getSource()));

      }
   });
  }

  public void initClipboard(){
    Thread thread = new Thread(_clipboard);
    thread.start();
  }

  public void addManagerToObservers(){
    _clipboard.addObserver(this);
  }

  @Override
  public void update(Observable obs, Object o){
    if(o instanceof String){
      _entries.add(new Entry((String)o));
    }
  }

  public static void main(String[] args){
    ClipboardManager manager = new ClipboardManager();
  }
}

class Clip  extends Observable implements Runnable {
  private String _entry;

  public Clip() {
    _entry = null;
  }

  @Override
  public void run() {
    for (;;) {
      String clip = "";//getClipboard();
      if (clip != null && !clip.equals(getEntry())) {
        setEntry(clip);
        setChanged();
        notifyObservers(clip);
      }
      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
      } catch (Exception e) {
        System.out.println("Erwarteter Fehler beim Warten auf die Anwendung.");
      }
    }
  }

  public static String getClipboard() {
    return Utils.getClipboardContents();
  }

  public static void setClipboard(String str) {
     Utils.setClipboardContents(str);
  }

  public String getEntry() {
    return _entry;
  }

  public void setEntry(String entry) {
    this._entry = entry;
  }
}

class Entry {
  private String original = null;
  private String modified = null;
  private Date creationTime = null;

  public Entry(String orig){
    creationTime = new Date();
    original = orig;
  }
}