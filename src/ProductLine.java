package com.oracle.csc342.team2.problems;

import java.math.BigDecimal;

public class ProductLine
{
	private BigDecimal productLineId;
	private String productLineName;

	public ProductLine()
	{
		// Empty Constructor
		this(null);
	}

	public ProductLine(BigDecimal id)
	{
		productLineId = id;
	}

	public boolean isNew()
	{
		return productLineId==null;
	}

	public BigDecimal getProductLineId()
	{
		return productLineId;
	}

	public void setProductLineId(BigDecimal id)
	{
		this.productLineId = id;
	}

	public String getProductLineName()
	{
		return productLineName;
	}

	public void setProductLineName(String name)
	{
		this.productLineName = name;
	}

	public String toString()
	{
		return (getProductLineName() + " (" + getProductLineId() + ")");
	}

}
