package com.oracle.csc342.team2.problems;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductLineDAO
{
	
	public ProductLineDAO()
	{
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

		return colPrefix+"product_line_id, "+colPrefix+"product_line_name";
	}

	public ProductLine loadProductLine(ProductLine wc, ResultSet rs) throws SQLException
	{
		return loadProductLine(wc, rs, 0);
	}

	public ProductLine loadProductLine(ProductLine wc, ResultSet rs, int startingPoint) throws SQLException
	{
		if(wc != null && rs != null)
		{
			int i=startingPoint;
			wc.setProductLineId(rs.getBigDecimal(++i));
			wc.setProductLineName(rs.getString(++i));
	
			System.out.println("Product Line Information Loaded Successfully");
		}
		
		return wc;
	}

	public void createProductLine(ProductLine pl) throws SQLException
	{
		System.out.println("Product Line to be Inserted");
		System.out.println(pl.toString());

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("INSERT INTO TEAM2.product_line ("+sqlColumns("")+") VALUES (?,?)");
		ps.setBigDecimal(1, pl.getProductLineId());
		ps.setString(2, pl.getProductLineName());

		ps.executeUpdate();
		System.out.println("Addition Success");

		if (con != null)
			System.out.println("Closing Product Line create statement");
		ps.close();
	}


	public ProductLine viewProductLine(BigDecimal id) throws SQLException
	{
		ProductLine result = new ProductLine();
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT "+sqlColumns("")+" FROM TEAM2.product_line WHERE product_line_id=?");
		ps.setBigDecimal(1, id);

		ResultSet rs = ps.executeQuery();
		if(rs.next())
		{
			loadProductLine(result, rs);

			System.out.println("View ProductLine Success");
		}

		if(con != null)
			System.out.println("Closing ProductLine connection");
		if(rs != null)
			rs.close();
		ps.close();

		return result;
	}

	public void updateProductLine(ProductLine pl) throws SQLException
	{
		System.out.println("ProductLine to be Updated");
		System.out.println(pl.toString());

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("UPDATE TEAM2.product_line p SET p.product_line_name = ? WHERE p.product_line_id=?");
		ps.setString(1, pl.getProductLineName());
		ps.executeQuery();

		System.out.println("Updated");

		if (con != null)
			System.out.println("Closing ProductLine connection");
		ps.close();
	}

	public void deleteProductLine(BigDecimal id) throws SQLException
	{
		System.out.println("ProductLine to be Deleted");
		System.out.println("ProductLine Id = " + id);

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("DELETE TEAM2.product_line WHERE product_line_id = ?");
		ps.setBigDecimal(1, id);
		ps.executeQuery();
		System.out.println("Deleted");

		if (con != null)
			System.out.println("Closing ProductLine connection");
		ps.close();
	}

	public List<ProductLine> generateProductLineList() throws SQLException
	{
		return generateProductLineList(null, null);
	}
	public List<ProductLine> generateProductLineList(String order) throws SQLException
	{
		return generateProductLineList(order, null);
	}
	public List<ProductLine> generateProductLineList(String order, String filter) throws SQLException
	{
		if(order == null || order.length() < 1)
			order = FurnitureFactoryGui.SORT_DEFAULT;
		
		System.out.println("Looking up information for all ProductLines");
		List<ProductLine> list = new ArrayList<ProductLine>();

		String sql = "SELECT "+this.sqlColumns("")
					+" FROM TEAM2.product_line";
		
		if(filter != null && filter.length() > 0)
		{
			sql = sql + " WHERE product_line_name LIKE ?";
		}
		
		sql = sql + " ORDER BY product_line_name "+order;
		
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		
		if(sql.indexOf("?") > -1)
		{
			ps.setString(1, "%"+filter+"%");
		}

		System.out.println(sql);

		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			ProductLine pl = loadProductLine(new ProductLine(), rs);
			list.add(pl);
		}

		System.out.println("Information Retrieved");

		if (con != null)
			System.out.println("Closing ProductLine connection");
		if(rs != null)
			rs.close();
		ps.close();
		
		return list;
	}
}
