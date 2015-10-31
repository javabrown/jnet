package com.jbrown.jnet.core;

import com.jbrown.jnet.utils.KeysI;

public enum Command {
   CLEAR("clear", "Clear Screen"), 
   PING("ping", "Perform test I/O"),
   CALC("calc", "Evaluate mathematical expression\n\r\t  "+"Sample > calc 10 + 2 * 9"),
   HELP("help", "Return list of command"),
   WGET("wget", "Perform HTTP GET operation\n\r\t  "+"Sample > wget http://www.javabrown.com"),
   
   ;
   
   private String _name;
   private String _desc;
   
   Command(String name, String desc) {
     _name = name;
     _desc = desc;
   }

   public String getName() {
     return _name;
   }

   public String getDesc() {
     return _desc;
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
       result = String.format("%s\n\r%s\t: %s\n", 
           result, cmd.getName(), cmd.getDesc());
     }
 
     result = String.format("%s\n\r%s", 
         result, "----------------------------------");
     
     return result;
   }
}
