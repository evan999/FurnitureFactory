package com.oracle.csc342.team2.problems;

public class WorkCenter
{
	private String workCenterId;
	private Address address;

	public WorkCenter()
	{
		// Empty Constructor
		this(null);
	}

	public WorkCenter(String id)
	{
		workCenterId = id;
		address = new Address();
	}

	public boolean isNew()
	{
		return workCenterId==null;
	}

	public String getWorkCenterId()
	{
		return workCenterId;
	}

	public void setWorkCenterId(String id)
	{
		this.workCenterId = id;
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
		return (getAddressInfo().getCity() + ", " + getAddressInfo().getState() + "(" + getWorkCenterId() + ")");
	}

}
