package com.oracle.csc342.team2.problems;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Report4DAO
{
	private static final String NEW_LINE = "\r\n";
	
	// REPORT 4
	public StringBuffer getProductsToBeDiscontinued() throws SQLException
	{
		StringBuffer discontinueReport = new StringBuffer(500);
		BigDecimal inProductID = null;
		int inSalesCount = 0;
		String inLastSoldDate = null;

		System.out.println("Starting Report 4");
		
		String sql = "SELECT p.product_id, SUM(NVL(o.ordered_quantity, 0)) as number_sold, NVL(MAX(TO_CHAR(f.order_date, 'mm-dd-yyyy')), 'Never') as last_ordered"
					+" FROM product p"
					+" LEFT OUTER JOIN order_line o"
					+" ON p.product_id=o.product_id"
					+" LEFT OUTER JOIN factory_order f"
					+" ON o.order_id=f.order_id"
					+" GROUP BY p.product_id"
					+" HAVING SUM(NVL(o.ordered_quantity, 0)) < 500"
					+" ORDER BY SUM(NVL(o.ordered_quantity, 0)) ASC";
		

		System.out.println("Report 4 SQL: "+sql);

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		// ps.setInt(1, 40);
		ResultSet rs = ps.executeQuery();
		discontinueReport.append(String.format("%1$-14s %2$-15s %3$s"+NEW_LINE, "Product ID", "Number Sold", "Last Ordered"));
		while (rs.next())
		{
			inProductID = rs.getBigDecimal(1);
			inSalesCount = rs.getInt(2);
			inLastSoldDate = rs.getString(3);

			String outRow = String.format("%1$-14s %2$-15d %3$s"+NEW_LINE, inProductID, inSalesCount, inLastSoldDate);
			discontinueReport.append(outRow);
		}
		System.out.println("Report 4 Display Complete");

		if (con != null)
			System.out.println("Closing Report 4 Connection");
		if (rs != null)
			rs.close();
		ps.close();

		return discontinueReport;
	}
}
