package com.oracle.csc342.team2.problems;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ReportGui extends Frame
{
	private static final long serialVersionUID = 1L;
	private String fileName;
	private Report1DAO report1;
	private Report2DAO report2;
	private Report3DAO report3;
	private Report4DAO report4;

	public static int REPORT1 = 1;
	public static int REPORT2 = 2;
	public static int REPORT3 = 3;
	public static int REPORT4 = 4;

	public ReportGui()
	{
		report1 = new Report1DAO();
		report2 = new Report2DAO();
		report3 = new Report3DAO();
		report4 = new Report4DAO();
	}

	public void runReport(int reportNumber) throws SQLException
	{
		try
		{
			System.out.println("Getting destination file");
			FileDialog fDialog = new FileDialog(this, "Save As...", FileDialog.SAVE);
			fDialog.setVisible(true);
			String name = fDialog.getFile();
			// If user canceled file selection, return without doing anything.
			if (name == null)
			{
				return;
			}
			fileName = fDialog.getDirectory() + name;

			System.out.println("Running report SQL");
			String result = null;
			switch (reportNumber)
			{
				case 1:
					result = report1.getSkillDemand().toString();
					break;
				case 2:
					result = report2.getEmployeeOrder().toString();
					break;
				case 3:
					result = report3.getProfit().toString();
					break;
				case 4:
					result = report4.getProductsToBeDiscontinued().toString();
					break;
				default:
					System.out.println("Unknown report requested.");
					JOptionPane.showMessageDialog(this, "This report has not yet been created.", "Unknown Report", JOptionPane.INFORMATION_MESSAGE);
					return;
			}

			if (result != null)
			{
				System.out.println("Writing results out to the selected file");
				FileWriter writer = new FileWriter(fileName, false);
				writer.write(result);
				writer.close();
			}
			System.out.println("Report is Complete.  Opening resulting file");

			Runtime rs = Runtime.getRuntime();
			rs.exec("notepad "+fileName);
		}
		catch (IOException ioe)
		{
			System.out.println("Error saving report to a file.  Msg: " + ioe.toString());
			JOptionPane.showMessageDialog(this, "Error saving report to a file.", "Error Message", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
}
