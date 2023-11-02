package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import edu.seg2105.client.common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  ChatIF serverUI;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }
  
  public void clientConnected(ConnectionToClient client) {
	  
	  System.out.println("A new client has connected to the server");
  }
  
  public synchronized void clientDisconnected(ConnectionToClient client) {
	  
	  System.out.println(client.getInfo("loginID") + " has disconnected from the server");
  }

  public synchronized void clientException(ConnectionToClient client, Throwable exception) {
	  
	  System.out.println(client.getInfo("loginID") + " has disconnected from the server");
  }
  
  //Instance methods ************************************************
  
  
  public void handleMessageFromServerConsole(String message)
  {
    try
    {
    	if (message.startsWith("#")) {
    		handleCommand(message);
    	}
    	else {
    		sendToAllClients("SERVER MSG> " + message);
    	}
    }
    catch(Exception e)
    {
      serverUI.display
        ("Could not send message to clients");
    }
  }
  
  private void handleCommand(String message) {
	  String[] arguments = message.split(" ");
	  String command = arguments[0];
	  
	  if (command.equals("#quit")) {
		  try {
			this.close();
		  } catch (IOException e) {
			  System.exit(1); 
		  }
	
		  System.exit(0);
	  }
	  
	  else if (command.equals("#stop")) {
		  this.stopListening();
	  }
	  
	  else if (command.equals("#close")) {
		  try {
			this.close();
		  } catch (IOException e) {
			  System.out.println("Error closing the server");  
		  }
	  }
	  
	  else if (command.equals("#setport")) {
		  if (this.isListening() || this.getNumberOfClients() > 0) {
			  System.out.println("Cannot set port while server is open");
		  }
		  else {
			  super.setPort(Integer.parseInt(arguments[1]));
		  }
	  }
	  
	  else if (command.equals("#start")) {
		  if (this.isListening()) {
			  System.out.println("You are already listening for new clients");
		  }
		  else {
			  try {
				this.listen();
				
			  } catch (IOException e) {
				  System.out.println("Error listening for new clients");
			  }
		  }
	  }
	  
	  else if (command.equals("#getport")) {
		  System.out.println("Current port is " + this.getPort());
	  }
	  
	  else {
		  System.out.println(command + " is an invalid command");
	  }
  }
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	String message = msg.toString();
	
	if (message.startsWith("#loginID")) {
		handleLoginID(message, client);
	}
	else {
		System.out.println("Message received: " + msg + " from " + client.getInfo("loginID"));
	    this.sendToAllClients(client.getInfo("loginID") + ">" + " " + message);
	}
  }
  
  private void handleLoginID(String message, ConnectionToClient client) {
	  String[] arguments = message.split(" ");
	  String command = arguments[0];
	  
	  System.out.println("Message received: #login " + arguments[1] + " from " + client.getInfo("loginID"));
	  System.out.println(arguments[1] + " has logged on");
	  sendToAllClients("SERVER MSG> " + arguments[1] + " has logged on");
	  
	  if (command.equals("#loginID") && client.getInfo("loginID") == null) {
		  client.setInfo("loginID", arguments[1]);
	  }
	  
	  else {
		  System.out.println(command + " is an invalid command");
	  }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    ServerConsole sv = new ServerConsole(port);
    sv.accept();
  }
}
//End of EchoServer class
