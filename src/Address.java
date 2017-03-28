package com.oracle.csc342.team2.problems;

public class Address
{
	String	streetAddress;
	String	city;
	String	state;
	String	postalCode;

	public Address()
	{
		// Empty Constructor
		this(null, null, null, null);
	}

	public Address(String streetAddress, String city, String state, String postalCode)
	{
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
	}

	public String getStreetAddress()
	{
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress)
	{
		this.streetAddress = streetAddress;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getPostalCode()
	{
		return postalCode;
	}

	public void setPostalCode(String postalCode)
	{
		this.postalCode = postalCode;
	}
}
