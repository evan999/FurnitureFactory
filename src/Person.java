package com.oracle.csc342.team2.problems;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;

public class Person
{

	private BigDecimal personId;
	private String firstName;
	private String lastName;
	private Address address;
	private Timestamp birthDate;
	private String phoneNum;
	private String faxNum;

	public Person()
	{
		// Empty Constructor
		this(null);
	}

	public Person(BigDecimal personId)
	{
		this.personId = personId;
		this.address = new Address();
	}

	public boolean isNew()
	{
		return personId==null;
	}

	public BigDecimal getPersonId()
	{
		return personId;
	}

	public void setPersonId(BigDecimal personId)
	{
		this.personId = personId;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public Timestamp getBirthDate()
	{
		return birthDate;
	}
	
	public String getBirthDateFormatted()
	{
		if(getBirthDate() == null)
			return null;
		else
			return new SimpleDateFormat("MM/dd/yyyy").format(getBirthDate());
	}

	public void setBirthDate(Timestamp birthDate)
	{
		this.birthDate = birthDate;
	}

	public String getPhoneNum()
	{
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum)
	{
		this.phoneNum = phoneNum;
	}

	public String getFaxNum()
	{
		return faxNum;
	}

	public void setFaxNum(String faxNum)
	{
		this.faxNum = faxNum;
	}

	public Address getAddressInfo()
	{
		return address;
	}

	public void setAddressInfo(Address address)
	{
		this.address = address;
	}

	public String toString()
	{
		return ("Id: " + getPersonId() + "Name: " + getFirstName() + " " + getLastName() + " " + "Birthdate: "
				+ getBirthDateFormatted() + " " + "Phone Number: " + " " + getPhoneNum());
	}

}
