package com.oracle.csc342.team2.problems;

/******************************************************************
 *  Course:				CSC 342 Advanced Databases
 *  Assignment:			End of semester project
 *  
 *  Code adapted & updated from assignment originally created for:
 *  
 *  COURSE:             CSC231 Computer Science and Programming II
 *	Lab:			    Number 8
 *	FILE:				PhoneBook.java
 *	TARGET:				Java 5.0 and 6.0
 *****************************************************************/

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class LoginGui extends Frame implements ActionListener, KeyListener
{
	private static final long serialVersionUID = 1L;
	private TextField hostname, port, database, username, password;
	private Button loginButton, workOfflineButton;
	private Frame loginWindow;
	
	private static final String DEFAULT_HOSTNAME = "oracle.homenet.local";
	private static final String DEFAULT_PORT = "1521";
	private static final String DEFAULT_DATABASE = "CSC342";
	private static final String DEFAULT_USERNAME = "TEAM2";
	private static final String DEFAULT_PASSWORD = "Zeus569";

	/**
	 * Constructor
	 */
	public LoginGui()
	{
		super("Login - Online Furniture Factory"); // set frame title
		
		loginWindow = this;
		
		setLayout(new GridLayout(7, 2)); // set layout

		Label label = new Label("Host", Label.LEFT);
		add(label);
		hostname = new TextField(DEFAULT_HOSTNAME);
		hostname.addKeyListener(this);
		add(hostname);
		label = new Label("Port", Label.LEFT);
		add(label);
		port = new TextField(DEFAULT_PORT);
		port.addKeyListener(this);
		add(port);
		label = new Label("Database", Label.LEFT);
		add(label);
		database = new TextField(DEFAULT_DATABASE);
		database.addKeyListener(this);
		add(database);
		label = new Label("Username", Label.LEFT);
		add(label);
		username = new TextField(DEFAULT_USERNAME);
		username.addKeyListener(this);
		add(username);
		label = new Label("Password", Label.LEFT);
		add(label);
		password = new TextField(DEFAULT_PASSWORD);
		password.addKeyListener(this);
		add(password);
		label = new Label("", Label.LEFT);
		add(label);
		label = new Label("", Label.LEFT);
		add(label);
		loginButton = new Button("Login");
		loginButton.addActionListener(this);
		add(loginButton);
		workOfflineButton = new Button("Work Offline");
		workOfflineButton.addActionListener(this);
		add(workOfflineButton);
	}

	public void keyPressed(KeyEvent e)
	{
		// We don't care when the key is pressed down
	}

	public void keyReleased(KeyEvent e)
	{
		//System.out.println("Key Pressed: "+e.getKeyChar()+" ("+e.getKeyCode()+")");
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			if(canConnectToDatabase())
				launchApplication();
		}
	}

	public void keyTyped(KeyEvent e)
	{
		// The key code is not passed for this event
	}

	// implementing ActionListener
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if (source == loginButton)
		{
			if(canConnectToDatabase())
				launchApplication();
		}
		else if (source == workOfflineButton)
		{
			launchApplication();
		}
	}
	
	private boolean canConnectToDatabase()
	{
		try
		{
			loginButton.setEnabled(false);
			workOfflineButton.setEnabled(false);
			System.out.println("Attempting to login to the DB");
			DBConnect.getConnection(hostname.getText(), port.getText(), database.getText(), username.getText(), password.getText());
			System.out.println("Checking Result: "+DBConnect.isConnected());
		}
		catch(SQLException ex)
		{
			loginButton.setEnabled(true);
			workOfflineButton.setEnabled(true);
			JOptionPane.showMessageDialog(this, "Invalid Connection/Login Information.", "Error", JOptionPane.ERROR_MESSAGE);
		}

		return DBConnect.isConnected();
	}
	
	private void launchApplication()
	{
		FurnitureFactoryGui frame = new FurnitureFactoryGui();
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(size.width / 2, size.height / 2);
		frame.setLocation(100, 200);

		//System.out.println("Your Screen Size: " + size.width + " (width) x " + size.height + " (height)");

		// add window closing listener
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});

		// show the frame
		frame.setVisible(true);
		loginWindow.setVisible(false);
	}
}
