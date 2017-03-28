package com.oracle.csc342.team2.problems;

import java.awt.Frame;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Report1Gui extends Frame
{
	private static final long serialVersionUID = 1L;
	private String fileName;
	private Report1DAO report;
	
	public Report1Gui()
	{
		report = new Report1DAO();
		fileName = "Report1_Results.txt";
	}

	public void runReport() throws SQLException
	{
		try
		{
			FileWriter writer = new FileWriter(fileName, false);
			writer.write(report.getSkillDemand().toString());
			writer.close();
		}
		catch (IOException ioe)
		{
			System.out.println("Error saving report to a file.  Msg: " + ioe.toString());
			JOptionPane.showMessageDialog(this, "Error saving report to a file.", "Error Message", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
}
