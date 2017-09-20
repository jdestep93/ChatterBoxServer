//******************************************************************************************************** 
// CLASS: ChatterBoxServer (ChatterBoxServer.java) 
// 
// DESCRIPTION 
// This program implements a GUI and sockets to create an instant messaging app.
// 
// PROJECT INFO
// Project: ChatterBox
// 
// AUTHOR 
// Jonathan Estep (jdestep@asu.edu) 
//********************************************************************************************************

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class ChatterBoxServer extends JFrame {
	
	/**
	 * Version 1.0
	 */
	private static final long serialVersionUID = 1L;
	
	private static String mName;
	private static JTextField mSendText;
	private static JTextArea mMessageWindow;
	private static JButton mSendButton;
	//streams
	private static ObjectOutputStream mOut;
	private static ObjectInputStream mInput;
	//sockets and server socket
	private static ServerSocket mServer;
	private static Socket mConn;
	
	
	//constructor
	public ChatterBoxServer(String pName){
		mName = pName;
		this.setSize(450, 450);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setTitle("Chatter Box Server");
		this.createComponents();
		
		this.setVisible(true);
	}
	
	//create the components that  I need
	public void createComponents() {
		
		//make all different panels needed
		JPanel mainPanel = new JPanel();
		
//------------------------------------------------------------------------------------------------
//userPanel in the north quadrant of the GUI interface
//text fields for message
//JLabel for the ip address of the server
//label and textfield will have titled borders for their respective purposes
		
		//create the text field for sending messages-------------------------------
		mSendText = new JTextField(23);
		mSendText.setEditable(false);
		mSendText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				sendMessage(e.getActionCommand());
				mSendText.setText("");
			}
		});
		//border for the textfield and color of background
		Border sendTextBorder = BorderFactory.createTitledBorder("Type a message:");
		mSendText.setBorder(sendTextBorder);
		mSendText.setBackground(new Color(255, 160, 122));
		
		mainPanel.add(mSendText);
		
	//create the send message button--------------------------------------------
		mSendButton = new JButton("Send");
		mSendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if(e.getSource() == mSendButton){
					sendMessage(e.getActionCommand());
					mSendText.setText("");
				}
			}
		});
		mSendButton.setEnabled(false);
		
		//add the send button to the userPanel
		mainPanel.add(mSendButton);
		
//-------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------
//create the chat window in the middle of the main pane
		mMessageWindow = new JTextArea(19, 30);
		mMessageWindow.setEditable(false);
		mMessageWindow.setLineWrap(true);
		mMessageWindow.setWrapStyleWord(true);
		mMessageWindow.setBorder(BorderFactory.createTitledBorder("Start Chatting!"));
		mMessageWindow.setBackground(new Color(255, 204, 153));
		
		//add the message window in the center of the main panel
		mainPanel.setBackground(new Color(255, 69, 0));
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		mainPanel.add(new JScrollPane(mMessageWindow));
		
//-------------------------------------------------------------------------------------------------
		
	//add the main panel to the frame-------------------------------------------
		ChatterBoxServer.this.add(mainPanel);
	}//END OF createComponents() method

	//start running the server
	public static void startServer(){
		try{
			mServer = new ServerSocket(5611, 4);
			while(true){
				try{
					try{
						displayChat("Waiting for a chat buddy...");
						mConn = mServer.accept();
						displayChat("\nYou are now connected to " + mConn.getInetAddress().getHostName());
					}
					catch (IOException e1){
						displayChat("\nAn Error Has Occurred");
					}
					try {
						mOut = new ObjectOutputStream(mConn.getOutputStream());
						mOut.flush();
						mInput = new ObjectInputStream(mConn.getInputStream());
					}
					catch (IOException e2){
						displayChat("\nCould not set up connection");
					}
					try{
						String message = "\nYou may now start chatting!";
						sendMessage(message);
						talkOption(true);
						do{
							try{
								message = (String) mInput.readObject();
								displayChat("\n" + message);
							}
							catch(ClassNotFoundException e){
								displayChat("\nClient has sent an invalid message");
							}
						}while(true);
					}
					catch (IOException e3){
						displayChat("\nAn I/O Error Has Occurred");
					}
				}
				finally{
					closeSocksStreams();
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}//END OF startServer() method
	

	private static void closeSocksStreams() {
		displayChat("\nClosing the connection");
		talkOption(false);
		try{
			mOut.close();
			mInput.close();
			mConn.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}//END OF closeSocksStreams
	
	private static void sendMessage(String pMessage){
		try{
			mOut.writeObject(mName + ": " + pMessage);
			mOut.flush();
			displayChat(mName + ": " + pMessage);
		}
		catch(IOException e){
			mMessageWindow.append("\nERROR: COULD NOT SEND MESSAGE");
		}
	}//END OF sendMessage() method
	
	private static void displayChat(final String pText){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					mMessageWindow.append(pText);
				}
			}
		);
		
	} //END OF showMessage() method
	
	private static void talkOption(final boolean tof){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						mSendText.setEditable(tof);
						mSendButton.setEnabled(tof);
					}
				}
			);	
	}//END OF talkOption() method
	
	public String getName(){
		return mName;
	}
}