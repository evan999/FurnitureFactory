package com.oracle.csc342.team2.problems;

import java.math.BigDecimal;

public class Product
{
	private BigDecimal productId;
	private ProductLine productLine;
	private String product_description;
	private String product_finish;
	private Float product_standard_price;

	public Product()
	{
		this(null);
	}

	public Product(BigDecimal productId)
	{
		this.productId = productId;
	}

	public boolean isNew()
	{
		return productId==null;
	}

	public BigDecimal getProductId()
	{
		return productId;
	}

	public void setProductId(BigDecimal productId)
	{
		this.productId = productId;
	}

	public ProductLine getProductLine()
	{
		return productLine;
	}

	public void setProductLine(ProductLine pl)
	{
		this.productLine = pl;
	}

	public String getProductDescription()
	{
		return product_description;
	}

	public void setProductDescription(String product_description)
	{
		this.product_description = product_description;
	}

	public String getProductFinish()
	{
		return product_finish;
	}

	public void setProductFinish(String product_finish)
	{
		this.product_finish = product_finish;
	}

	public Float getProductStandardPrice()
	{
		return product_standard_price;
	}

	public String getProductStandardPriceFormatted()
	{
		return Utils.formatPrice(product_standard_price);
	}

	public void setProductStandardPrice(Float product_standard_price)
	{
		this.product_standard_price = product_standard_price;
	}

	public String toString()
	{
		return ("ID: " + getProductId() + " - " + getProductDescription());
	}
}
