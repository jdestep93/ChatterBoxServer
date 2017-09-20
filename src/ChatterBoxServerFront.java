//******************************************************************************************************** 
// CLASS: ChatterBoxServerFront (ChatterBoxServerFront.java) 
// 
// DESCRIPTION 
// Implements a Java Swing GUI for a simple log in screen for the instant messaging application
// 
// Project: ChatterBox
// 
// AUTHOR 
// Jonathan Estep (jdestep@asu.edu) 
//********************************************************************************************************

import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ChatterBoxServerFront extends JFrame{
	
	/**
	 * Version 1.0
	 */
	private static final long serialVersionUID = 1L;
	
	private static JLabel mTitleLabel;
	private static JTextField mUserName;
	private static JButton mQuitBut, mStartBut;
	private String mPath = "chatterbox.png";
	
	public static void main (String[] args) throws IOException{
		ChatterBoxServerFront serverFront = new ChatterBoxServerFront();
		serverFront.run();
	}
	
	private void run() throws IOException{
		new ChatterBoxServerFront();
	}
	
	public ChatterBoxServerFront() throws IOException{
		this.setSize(400,400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setTitle("ChatterBox Server Login");
		createComponents();
		
		//actionlisteners for buttons
		mStartBut.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == mStartBut){
					int nameLength = mUserName.getText().length();
					if (mUserName.getText().equals("") || mUserName.getText().equals(null)) {
						JOptionPane.showMessageDialog(ChatterBoxServerFront.this, "You must enter a valid username", "Invalid Username", JOptionPane.WARNING_MESSAGE);
					}
					else if (nameLength > 10) {
						JOptionPane.showMessageDialog(ChatterBoxServerFront.this, "User name length must be 10 characters or less", "Invalid Username", JOptionPane.WARNING_MESSAGE);
					}
					else {
						String name = mUserName.getText();
						JFrame server = new ChatterBoxServer(name);
						server.setVisible(true);
						ChatterBoxServerFront.this.dispose();
					}
				}
			}
			
		});
		
		mQuitBut.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				if (e.getSource() == mQuitBut){
					ChatterBoxServerFront.this.dispose();
					System.exit(0);
				}
			}
		});
		
		this.setVisible(true);
	}
	
	public void createComponents() throws IOException{
		JPanel thePanel = new JPanel();
		thePanel.setLayout(new GridLayout(6, 6));
		File file = new File(mPath);
		BufferedImage logo = ImageIO.read(file);
		mTitleLabel = new JLabel(new ImageIcon(logo));
		thePanel.add(mTitleLabel);
		
		JLabel empty1 = new JLabel();
		JLabel empty2 = new JLabel();
		thePanel.add(empty1);
		thePanel.add(empty2);
		
		mUserName = new JTextField(20);
		mUserName.setBorder(BorderFactory.createTitledBorder("Enter Your Username"));
		thePanel.add(mUserName);
		
		mStartBut = new JButton("Start");
		thePanel.add(mStartBut);
		mQuitBut = new JButton("Quit");
		thePanel.add(mQuitBut);
		
		ChatterBoxServerFront.this.add(thePanel);
	}
	
}
