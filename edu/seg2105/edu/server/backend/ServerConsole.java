package edu.seg2105.edu.server.backend;

import edu.seg2105.client.common.ChatIF;
import java.util.Scanner;

public class ServerConsole implements ChatIF {

	final public static int DEFAULT_PORT = 5555;
	
	EchoServer server;
	Scanner fromConsole;
	
	public ServerConsole(int port) {
		
		server= new EchoServer(port);
	    try 
	    {
	      server.listen(); //Start listening for connections
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("ERROR - Could not listen for clients!");
	    }
	    
	    fromConsole = new Scanner(System.in);
	}
	
	public void accept() {
		try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        server.handleMessageFromServerConsole(message);
	        if(!message.startsWith("#")) {
	        	this.display(message);
	        }
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	}
	
	public void display(String message) {
		System.out.println("SERVER MSG> " + message);
	}
}