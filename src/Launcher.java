package com.oracle.csc342.team2.problems;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Launcher
{
	/**
	 * the main method
	 */
	public static void main(String[] argv)
	{
		// create frame
		//System.out.println("Creating window ... ");
		LoginGui frame = new LoginGui();
		frame.setSize(300, 200);
		frame.setLocation(100, 100);
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
	}
}
