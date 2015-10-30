//package com.jbrown.jnet.core;
//
//import java.io.*;
//import java.net.*;
//
//import com.jbrown.jnet.commands.Responder;
//
//public class SyncServer {
//	public static void start() throws Exception {
//
//		System.out.println(" Server is Running  ");
//		ServerSocket mysocket = new ServerSocket(5555);
//		Socket connectionSocket = null;
//		
//		while (true) {
//			try{
//				connectionSocket = mysocket.accept();
//				System.out.println(connectionSocket.getLocalSocketAddress().toString());
//				BufferedReader reader = new BufferedReader(new InputStreamReader(
//						connectionSocket.getInputStream()));
//				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
//						connectionSocket.getOutputStream()));
//	 
//				String command = "";
//				
//				while(!command.equalsIgnoreCase("quit")) {
//					writer.write("\n\rjnet> ");
//					writer.flush();
//					command = reader.readLine().trim();
//					String commandResult = new Responder(command).respond();
//					
//					//writer.write("\n Received  : " + command+"\n\r");
//					writer.write(commandResult+"\n\r");
//					writer.flush();
//				}
//				
//			}catch(Exception ex){
//				ex.printStackTrace();
//			}
//			finally{
//				connectionSocket.close();
//			}
//		}
//	}
//}