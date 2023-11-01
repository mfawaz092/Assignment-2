// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  String loginID;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	if (message.startsWith("#")) {
    		handleCommand(message);
    	}
    	else {
    		sendToServer(message);
    	}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  private void handleCommand(String message) {
	  String[] arguments = message.split(" ");
	  String command = arguments[0];
	  
	  if (command.equals("#quit")) {
		  quit();
	  }
	  
	  else if (command.equals("#logoff")) {
		  try {
			closeConnection();
		} catch (IOException e) {
			System.out.println("Error logging off");
		}
	  }
	  
	  else if (command.equals("#sethost")) {
		  if (isConnected()) {
			  System.out.println("Cannot set host while connected");
		  }
		  else {
			  this.setHost(arguments[1]);
		  }
	  }
	  
	  else if (command.equals("#setport")) {
		  if (isConnected()) {
			  System.out.println("Cannot set port while connected");
		  }
		  else {
			  this.setPort(Integer.parseInt(arguments[1]));
		  }
	  }
	  
	  else if (command.equals("#login")) {
		  if (isConnected()) {
			  System.out.println("You are already connected");
		  }
		  else {
			  try {
				openConnection();
			} catch (IOException e) {
				System.out.println("Error logging in");
			}
		  }
	  }
	  
	  else if (command.equals("#gethost")) {
		  System.out.println("Current host is " + this.getHost());
	  }
	  
	  else if (command.equals("#getport")) {
		  System.out.println("Current port is " + this.getPort());
	  }
	  
	  else {
		  System.out.println(command + " is an invalid command");
	  }
  }
  
  public void connectionClosed() {
	  
	  System.out.println("Connection to server closed");
	  
  }
  
  public void connectionException(Exception exception) {
	  
	  System.out.println("The server has shutdown. Disconnecting you from server...");
	  quit();
	  
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
