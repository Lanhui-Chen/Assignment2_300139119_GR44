// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
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
  //Call a detect method for checking server shut down, Change for E49 - LHC
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	
      sendToServer(message);
      checkConnection();
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  //detect method which calls the isConnected() method every 4 seconds, then quit if !isConnected(), E49 - LHC
  public void checkConnection() {
	  int count = 0;
	  
	  while(count!= 10)
	  {
		  try 
		  {
			  if(!isConnected()) 
			  {
				  quit();
				  break;
			  }
			  
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			
			clientUI.display("error");
			e.printStackTrace();
		}
		  count++;  
	  }
  }
  
  //Shut down message
  public void connectionClosed() 
  {
	  clientUI.display("Server has shut down");
  }
  
  //connection error message
  public void connectionException(Exception exception) 
  {
	  clientUI.display("An exception has occur, and the server is terminated");
  }
  
  //E50, some control command functions 
  public void control(String message) {
	  char[] ch = message.toCharArray();
	  char[] temp = new char[ch.length-1];
	  
	  for(int i = 1; i < ch.length; i++) {
		  temp[i-1] = ch[i];
	  }
	  
	  char[] tempPortOrHost = new char[ch.length-8];
	  
	  for(int i = 9; i < ch.length; i++) {
		  tempPortOrHost[i-9] = ch[i];
	  }
	  
	  String newMessage = new String(temp);
	  String setters = new String(tempPortOrHost);
	  
	  if(newMessage == "quit") {
		  quit();
		  
	  }else if(newMessage == "logoff") {
		  
	  }else if(newMessage.contains("sethost")) {
		  setHost(setters);
		  
	  }else if(newMessage.contains("setport")) {
		  try {
			  setPort(Integer.parseInt(setters));
		  }catch (Exception exception) {
			  clientUI.display("Not all digits");
		  }
		  
	  }else if(newMessage == "login") {
		  
	  }else if(newMessage == "gethost") {
		  clientUI.display(getHost());
		  
	  }else if(newMessage == "getport") {
		  clientUI.display(Integer.toString(getPort()));
	  }else {
		  clientUI.display("WRONG COMMAND");
	  }
	  
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
