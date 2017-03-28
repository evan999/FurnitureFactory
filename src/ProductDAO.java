package com.oracle.csc342.team2.problems;

import java.math.BigDecimal;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO
{
	private ProductLineDAO productLine;
	public ProductDAO()
	{
		// Empty Constructor
		productLine = new ProductLineDAO();
	}

	public int numberOfColumns()
	{
		return sqlColumns("").split(",").length;
	}

	public String sqlColumns(String colPrefix)
	{
		if(colPrefix == null)
			colPrefix = "";
		else if(colPrefix.length()>0)
			colPrefix = colPrefix+".";

		return colPrefix+"product_id, "+colPrefix+"product_line_id, "+colPrefix+"product_description, "+colPrefix+"product_finish, "+colPrefix+"product_standard_price";
	}
	
	private Product loadProduct(Product product, ResultSet rs) throws SQLException
	{
		return loadProduct(product, rs, 0);
	}

	private Product loadProduct(Product product, ResultSet rs, int startingPoint) throws SQLException
	{
		if(product != null && rs != null)
		{
			int i=startingPoint;
			product.setProductId(rs.getBigDecimal(++i));
			product.setProductLine(productLine.viewProductLine(rs.getBigDecimal(++i)));
			product.setProductDescription(rs.getString(++i));
			product.setProductFinish(rs.getString(++i));
			product.setProductStandardPrice(rs.getFloat(++i));
	
			System.out.println("Product Information Loaded Successfully");
		}
		
		return product;
	}

	public void createProduct(Product product) throws SQLException
	{
		System.out.println("Inserting a new Product");

		product.setProductId(BigDecimal.valueOf(findMaxProductId().longValue()+1));

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("INSERT INTO TEAM2.product ("+sqlColumns("")+") values(?,?,?,?,?)");
		ps.setBigDecimal(1, product.getProductId());
		ps.setBigDecimal(2, product.getProductLine().getProductLineId());
		ps.setString(3, product.getProductDescription());
		ps.setString(4, product.getProductFinish());
		ps.setFloat(5, product.getProductStandardPrice());

		ps.executeUpdate();
		System.out.println("Addition Success");

		if (con != null)
			System.out.println("Closing Product create statement");
		ps.close();
	}

	public BigDecimal findMaxProductId() throws SQLException
	{
		BigDecimal maxProductId = new BigDecimal(0);

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("select max(pr.product_id) from TEAM2.Product pr");

		ResultSet rs = ps.executeQuery();
		while (rs.next())
		{
			maxProductId = rs.getBigDecimal(1);
			System.out.println("Get Max Product Id Success ");
		}

		if (con != null)
			System.out.println("Closing Product connection");
		if(rs != null)
			rs.close();
		ps.close();

		return maxProductId;
	}

	public Product viewProduct(BigDecimal productId) throws SQLException
	{
		Product outProduct = new Product();
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT "+sqlColumns("pr")+" FROM TEAM2.product pr" + "WHERE pr.product_id = ?");
		ps.setBigDecimal(1, productId);

		ResultSet rs = ps.executeQuery();
		while (rs.next())
		{
			loadProduct(outProduct, rs);
			System.out.println("View Product Success");
		}

		if (con != null)
			System.out.println("Closing Product connection");
		if(rs != null)
			rs.close();
		ps.close();

		return outProduct;
	}

	public void updateProduct(Product prod) throws SQLException
	{
		System.out.println("Product to be Updated");
		System.out.println(prod.toString());

		Connection con = DBConnect.getConnection();

		PreparedStatement ps = con.prepareStatement("UPDATE TEAM2.product SET product_description=?, product_finish=?, product_standard_price=? WHERE product_id=?");

		ps.setString(1, prod.getProductDescription());
		ps.setString(2, prod.getProductFinish());
		ps.setFloat(3, prod.getProductStandardPrice());
		ps.setBigDecimal(4, prod.getProductId());
		ps.executeQuery();
		System.out.println("Updated");

		if (con != null)
			System.out.println("Closing Product connection");
		ps.close();
	}

	public void deleteProduct(BigDecimal productId) throws SQLException
	{
		System.out.println("Product to be Deleted");
		System.out.println("Product Id = " + productId);

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("DELETE TEAM2.product WHERE product_id=?");
		ps.setBigDecimal(1, productId);
		ps.executeQuery();
		System.out.println("Deleted");

		if (con != null)
			System.out.println("Closing Product connection");
		ps.close();
	}

	public void saveProduct(Product product) throws SQLException
	{
		if(product.isNew())
			createProduct(product);
		else
			updateProduct(product);
	}

	public void saveProducts(List<Product> product) throws SQLException
	{
		String sql1 = "SELECT count(*) as product_count FROM TEAM2.product pr WHERE pr.product_id = ?";

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement(sql1);
		ResultSet rs = null;

		for (Iterator<Product> it = product.iterator(); it.hasNext();)
		{
			Product testProduct = it.next();
			ps.setBigDecimal(1, testProduct.getProductId());
			rs = ps.executeQuery();
			while (rs.next())
			{
				if (rs.getInt(1) == 1)
					updateProduct(testProduct);
				else if (rs.getInt(1) == 0)
					createProduct(testProduct);
				else
					throw new RuntimeException("More than one product has Product Id");
			}
		}

		if(rs != null)
			rs.close();
		ps.close();
	}

	public List<Product> generateProductList(String order) throws SQLException
	{
		return generateProductList(order, null);
	}
	public List<Product> generateProductList(String order, String filter) throws SQLException
	{
		if(order == null || order.length() < 1)
			order = FurnitureFactoryGui.SORT_DEFAULT;
		
		System.out.println("Looking up information for all Products");
		List<Product> list = new ArrayList<Product>();

		String sql = "SELECT "+this.sqlColumns("p")
					+" FROM TEAM2.product p";
		
		if(filter != null && filter.length() > 0)
		{
			sql = sql + " WHERE LOWER(p.product_description) LIKE ?";
		}
		
		sql = sql + " ORDER BY p.product_description "+order;
		
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		
		if(sql.indexOf("?") > -1)
		{
			ps.setString(1, "%"+filter.toLowerCase()+"%");
		}

		System.out.println(sql);

		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			Product product = loadProduct(new Product(), rs);
			list.add(product);
		}

		System.out.println("Information Retrieved");

		if (con != null)
			System.out.println("Closing Product connection");
		if(rs != null)
			rs.close();
		ps.close();
		
		return list;
	}
}
