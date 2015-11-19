package com.jbrown.jnet.core;

import com.jbrown.jnet.utils.KeysI;

public enum Command {
   CLEAR("clear", "Clear Screen"),
   PING("ping", "Perform test I/O"),
   CALC("calc", "Evaluate mathematical expression\n\r\t  "+"Sample > calc 10 + 2 * 9"),
   HELP("help", "Return list of command"),
   WGET("wget", "Perform HTTP GET operation\n\r\t  "+"Sample > wget http://www.javabrown.com"),
   GET("get", "Retrieve cached {VALUE} from jNet for a given {KEY}\n\r\t  "+"Sample > get key-name"),
   SET("set", "Store {KEY, VALUE} pair of data in jNet.\n\r\t  "+"Sample > set key-name key-value"),
   WHO("who", "Returns ip-address of the remote client"),
   QUIT("quit", "Quits JNET terminal."),
   CLIP("clip", "Store clipboard content into socket-cache with key='clip'"),
   ABOUT("about", "\n\rJNET 1.0\n\rDesigned & Developed by Raja Khan (getrk@yahoo.com)", false)

   ;


   private String _name;
   private String _desc;
   private boolean _displayable;

   Command(String name, String desc) {
     _name = name;
     _desc = desc;
     _displayable = true;
   }

   Command(String name, String desc, boolean displayable) {
     _name = name;
     _desc = desc;
     _displayable = displayable;
   }

   public String getName() {
     return _name;
   }

   public String getDesc() {
     return _desc;
   }

   public boolean isDisplayable(){
     return _displayable;
   }

   public static Command find(String commandName) {
      for (Command type : Command.values()) {
        if (type.getName().equalsIgnoreCase(commandName)) {
          return type;
        }
      }

      throw new RuntimeException("Unknown Command:" + commandName);
   }

   public boolean typeOf(Command command) {
      return find(command.getName()) != null;
   }

   public String getCommandList(){
     String result =
         String.format("----- List of %s commands: -----",
             KeysI.PROMPT_K.toUpperCase());

     for (Command cmd : Command.values()) {
       if(!cmd.isDisplayable()) continue;

       result = String.format("%s\n\r%s\t: %s\n",
           result, cmd.getName(), cmd.getDesc());
     }

     result = String.format("%s\n\r%s",
         result, "----------------------------------");

     return result;
   }
}
