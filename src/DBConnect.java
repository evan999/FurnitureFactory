package com.oracle.csc342.team2.problems;

import java.sql.*;

public class DBConnect
{
	private static String		urlLead			= "jdbc:oracle:thin:@//";
	private static String		dbURL			= null;
	private static String		dbDriverName	= "oracle.jdbc.OracleDriver";
	private static String		hostName;
	private static String		port;
	private static String		sid;
	private static String		id;
	private static String		pwrd;
	private static Connection	conn			= null;
	private static boolean		driverLoaded	= false;

	// private static boolean connected = false;

	private DBConnect()
	{
		return;
	}

	public static void loadDriver()
	{
		try
		{
			Class.forName(dbDriverName);
		}
		catch (Exception e)
		{
			System.out.println("Driver could not be loaded");
			System.out.println("Message: " + e.getMessage());
			e.printStackTrace();
		}
		return;
	}
	
	public static boolean isConnected()
	{
		try
		{
			return (conn != null && conn.isValid(5));
		}
		catch (SQLException e)
		{
			return false;
		}
	}

	public static Connection getConnection(String inHostname, String inPort, String inSid, String inId, String inPwrd) throws SQLException
	{
		if (conn != null)
			return conn;

		if (!driverLoaded)
		{
			loadDriver();
			driverLoaded = true;
		}

		setHostName(inHostname);
		setPort(inPort);
		setSid(inSid);
		setId(inId);
		setPwrd(inPwrd);
		setDbURL();

		conn = DriverManager.getConnection(dbURL, id, pwrd);
		conn.setAutoCommit(false);
		// connected = true;

		System.out.println("Connected");

		return conn;
	}

	public static Connection getConnection() throws SQLException
	{
		if (isConnected())
		{
			System.out.println("Connected");
			return conn;
		}
		else
		{
			System.out.println("NOT Connected");
			throw new SQLException("Connection has not been created."); // Make it clear this is not usable
		}
	}

	public static String getDbURL()
	{
		return dbURL;
	}

	public static void setDbURL()
	{
		dbURL = urlLead + hostName + ':' + port + '/' + sid;
	}

	public static String getDbDriverName()
	{
		return dbDriverName;
	}

	public static void setDbDriverName(String name)
	{
		dbDriverName = name;
	}

	public static String getHostName()
	{
		return hostName;
	}

	public static void setHostName(String inHostName)
	{
		hostName = inHostName;
	}

	public static String getPort()
	{
		return port;
	}

	public static void setPort(String inPort)
	{
		port = inPort;
	}

	public static String getSid()
	{
		return sid;
	}

	public static void setSid(String inSid)
	{
		sid = inSid;
	}

	public static String getId()
	{
		return id;
	}

	public static void setId(String inId)
	{
		id = inId;
	}

	public static String getPwrd()
	{
		return pwrd;
	}

	public static void setPwrd(String inPwrd)
	{
		pwrd = inPwrd;
	}

}
