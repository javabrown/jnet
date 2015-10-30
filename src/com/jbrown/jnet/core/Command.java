package com.jbrown.jnet.core;

public enum Command {
   CLEAR("clear"), 
   EXIT("exit"), 
   PING("ping");

   private String _name;

   Command(String name) {
     _name = name;
   }

   public String getName() {
     return _name;
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
}
