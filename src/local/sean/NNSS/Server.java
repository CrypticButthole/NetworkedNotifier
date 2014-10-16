/* TODO && PROTOCOL
 * 		Clean up code. Maybe move some things to classes?
 * 		Server is slow to start up, speed that shit up man.
 * 		Add a scroll bar to the text area...
 */

/*
 * Protocol:
 * 		SRV -> CLNT		|	CLNT -> SRV
 * 		SRV_MSG :(MSG)		CLNT_MSG :(CMD)
 * 
 * 		Possible commands:
 * 			SRV_MSG :(MSG)
 * 			CLNT_MSG :add		// Adds a message to the system
 * 			CLNT_MSG :echo (W/e)// Echos the (W/e) string with echo at start
 * 			CLNT_MSG :exit		// Exit's the server (!! REQUIRES RESTART OF PROGRAM SERVER SIDE !!)
 * 		Possible server messages:
 * 			CONNECTED			// Replies if client is connected to server. Not entirely needed, but check for it anyway.	
 * 			SUCCESS				// Replies if message is added to queue successfully	(Only returns if :add succeeds)
 * 			ERROR				// Replies if the message is failed to add to queue		(Only returns if :add fails)			
 * 			EXIT				// Replies when the server is going down.
 */

package local.sean.NNSS;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

import javax.swing.*;

public class Server {

	public static int portNum = 9999;
	public static BufferedWriter outFile = null;
	public static JFrame frame;
	public static JTextArea textArea;
	public static ArrayList<String> messages = new ArrayList<String>();
	public static ServerSocket serverSocket;

	public static void createAndShowUi() {
		// Create Window
		frame = new JFrame("Message Viewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// add test label
		textArea = new JTextArea(20, 50);
		textArea.setEditable(false);
		textArea.setLineWrap(true);

		// make frame visible
		frame.getContentPane().add(textArea);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}

	/*
	 * Add's the current message queue to the JTextArea
	 */

	public static void updateTextArea(String message) {

		/*
		 * String str = ""; for (String s : messages) { str += s + '\n'; }
		 */
		textArea.append(message + '\n');
		frame.getContentPane();
		frame.revalidate();
		frame.repaint();
	}

	public static void main(String[] args) {
		/*
		 * Initializes the server, and opens a new messageHandler to parse the
		 * messages!
		 */
		try {
			createAndShowUi();
			outFile = new BufferedWriter(new FileWriter("C:\\messages.txt",
					true));
			serverSocket = new ServerSocket(portNum);
			System.out.println("Hey! Server works!");
			while (true) {
				messageHandler(serverSocket.accept());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Ayy, we're done!");
		frame.setVisible(false);
		frame.dispose();
		try {
			serverSocket.close();
		} catch (IOException e)
		{
			
		}
	}

	/*
	 * messageHandler parses the messages from the socket and then runs the
	 * functions related to it.
	 */

	// TODO: Move this into a class for multi clients using threads
	public static void messageHandler(Socket socket) {
		Socket client = socket;
		try {
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			out.println("SRV_MSG :CONNECTED");
			while (true) {
				/* DEBUG System.out.println("I is in loop for msg"); */
				String command = in.readLine();
				/* Command is read */
				/* DEBUG System.out.println("Command is read"); */
				int seperatorLocation = command.indexOf(" :");
				if (seperatorLocation == -1) // check if message contains
												// command separator
				{
					System.out.println("Got: " + command
							+ ". Is not a command!");
				} else // Enter into message checker loop.
				{
					String commandType = command
							.substring(0, seperatorLocation);
					/* DEBUG System.out.println(commandType); */
					command = command.substring(seperatorLocation + 2);
					/* DEBUG System.out.println(command); */
					/*
					 * DEBUG System.out.println(
					 * "what are we do. We are to start the message check loop."
					 * );
					 */
					if (commandType.equalsIgnoreCase("CLNT_MSG")) {
						System.out.println("Parsing Client Message");
						if (command.toLowerCase().startsWith(
								"Exit".toLowerCase())) {
							System.out.println("Got Exit, Closing!");
							out.println("SRV_MSG :EXIT");
							return;
						} else if (command.toLowerCase().startsWith(
								"echo ".toLowerCase())) {
							out.println(command);
						} else if (command.toLowerCase().startsWith(
								"add".toLowerCase())) {
							// System.out.println("In the Add loop");
							String msg_to_add = in.readLine();
							if (writeMessage(msg_to_add) == 0) {
								System.out.println("Added '" + msg_to_add
										+ "' to the msg file");
								messages.add(msg_to_add);
								updateTextArea(msg_to_add);
								out.println("SRV_MSG :SUCCESS");
							} else {
								out.println("SRV_MSG :ERROR");
							}

						}
					} else {
						System.out.println("What is this " + commandType + ":"
								+ command);
					}
				}
			}
		} catch (IOException e) {

		}
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Writes messages to file.
	 */

	public static int writeMessage(String msgs) {
		try {
			outFile.write(msgs);
			outFile.newLine();
			outFile.flush();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 1;
		}
	}
}
