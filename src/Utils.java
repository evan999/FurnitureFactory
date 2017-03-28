package com.oracle.csc342.team2.problems;

import java.awt.Choice;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

public class Utils
{
	public static Timestamp stringToTimestamp(String value) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date parsedDate = sdf.parse(value);
		java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());	
		return timestamp;
	}
	
	public static String formatPrice(Float value)
	{
		return new DecimalFormat("#,###,###,##0.00").format(value);
	}
	
	public static Choice listToChoice(List<?> choices)
	{
		return listToChoice(choices, null);
	}
	public static Choice listToChoice(List<?> choices, String defaultChoice)
	{
		Choice list = new Choice();
		if(defaultChoice != null && defaultChoice.length() > 0)
			list.add(defaultChoice);

		if(choices != null)
		{
			for (Iterator<?> it = choices.iterator(); it.hasNext();)
			{
				list.add(it.next().toString());
			}
		}
		return list;
	}
	
	public static Choice getListOfStates()
	{
		Choice state = new Choice();
		state.add("Alabama");
		state.add("Alaska");
		state.add("American Samoa");
		state.add("Arizona");
		state.add("Arkansas");
		state.add("California");
		state.add("Colorado");
		state.add("Connecticut");
		state.add("Delaware");
		state.add("District of Columbia");
		state.add("Florida");
		state.add("Georgia");
		state.add("Guam");
		state.add("Hawaii");
		state.add("Idaho");
		state.add("Illinois");
		state.add("Indiana");
		state.add("Iowa");
		state.add("Kansas");
		state.add("Kentucky");
		state.add("Louisiana");
		state.add("Maine");
		state.add("Maryland");
		state.add("Massachusetts");
		state.add("Michigan");
		state.add("Minnesota");
		state.add("Mississippi");
		state.add("Missouri");
		state.add("Montana");
		state.add("Nebraska");
		state.add("Nevada");
		state.add("New Hampshire");
		state.add("New Jersey");
		state.add("New Mexico");
		state.add("New York");
		state.add("North Carolina");
		state.add("North Dakota");
		state.add("Northern Marianas Islands");
		state.add("Ohio");
		state.add("Oklahoma");
		state.add("Oregon");
		state.add("Pennsylvania");
		state.add("Puerto Rico");
		state.add("Rhode Island");
		state.add("South Carolina");
		state.add("South Dakota");
		state.add("Tennessee");
		state.add("Texas");
		state.add("Utah");
		state.add("Vermont");
		state.add("Virginia");
		state.add("Virgin Islands");
		state.add("Washington");
		state.add("West Virginia");
		state.add("Wisconsin");
		state.add("Wyoming");
		return state;
	}
}
