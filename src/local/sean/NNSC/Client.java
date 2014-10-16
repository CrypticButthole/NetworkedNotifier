/* TODO && PROTOCOL
 * 		Interact with the server			DONE
 * 		Send the server the messages		DONE
 * 		Add log of sent messages
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

package local.sean.NNSC;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.*;

public class Client {
	// Strings;
	public static String host = "localhost"; // Change for your needs
	public static ArrayList<String> messages = new ArrayList<String>(); 
	// Ints;
	public static int port = 9999; // Change relative to server info
	// Socket related stuffs;
	static Socket clientSocket;
	static PrintWriter out;
	static BufferedReader in;
	// UI related objects;
	public static JFrame frame;
	public static JTextArea textArea;
	public static JButton sendMessage;

	public static void createUI() {
		frame = new JFrame("Message Sender");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		sendMessage = new JButton("Send");

		textArea = new JTextArea(1, 50);
		textArea.setEditable(true);

		sendMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				messageSender(out, textArea.getText());
			}
		});
		frame.getContentPane().add(textArea, BorderLayout.WEST);
		frame.getContentPane().add(sendMessage, BorderLayout.EAST);

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		/*
		 * Initialize the client
		 */
		try {
			createUI();
			clientSocket = new Socket(host, port);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			messageHandler(out, in);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void messageSender(PrintWriter out, String message) {
		out.println("CLNT_MSG :ADD");
		out.println(message);
		messages.add(message);
	}

	public static void messageHandler(PrintWriter out, BufferedReader in) {
		// TODO create this function
		while (true) {
			try {
				String srv_msg = in.readLine().toLowerCase();
				if (srv_msg.equalsIgnoreCase("SRV_MSG :CONNECTED")) {
					System.out.println("Connected");
				} else if (srv_msg.equalsIgnoreCase("SRV_MSG :SUCCESS")) {
					System.out.println("Success!");
				} else if (srv_msg.equalsIgnoreCase("SRV_MSG :ERROR")) {
					System.out.println("Error");
				} else if (srv_msg.equalsIgnoreCase("SRV_MSG :EXIT")) {
					System.out.println("Server going down!");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void updateMessageBox() {
		// TODO create this function
	}
}
