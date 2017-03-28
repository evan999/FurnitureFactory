package com.oracle.csc342.team2.problems;

import java.math.BigDecimal;

public class Employee extends Person
{
	private BigDecimal employeeID;
	private BigDecimal supervisor;
	private WorkCenter workCenter;

	public Employee()
	{
		// Empty Constructor
	}

	public Employee(BigDecimal employeeID)
	{
		this.employeeID = employeeID;
	}

	public boolean isNew()
	{
		return employeeID==null;
	}

	public BigDecimal getEmployeeID()
	{
		if(employeeID != null)
			return employeeID;
		else if(getPersonId() != null)
			return getPersonId();
		else
			return null;
	}

	public void setEmployeeID(BigDecimal employeeID)
	{
		this.employeeID = employeeID;
	}

	public BigDecimal getSupervisor()
	{
		return supervisor;
	}

	public void setSupervisor(BigDecimal supervisor)
	{
		this.supervisor = supervisor;
	}

	public WorkCenter getWorkCenter()
	{
		return workCenter;
	}

	public void setWorkCenter(WorkCenter wc)
	{
		this.workCenter = wc;
	}

	public String toString()
	{
		return ((getLastName()==null?"NULL":getLastName())
				+", "+(getFirstName()==null?"NULL":getFirstName())
				+" (" + getEmployeeID() + ")"
		);
	}
}
