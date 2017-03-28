package com.oracle.csc342.team2.problems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Report2DAO
{
	private static final String NEW_LINE = "\r\n";
	
	// REPORT 2
	public StringBuffer getEmployeeOrder() throws SQLException
	{
		StringBuffer empOrder = new StringBuffer(500);
		String inEmployeeID = null;
		String inNumProductsOrdered = null;
		String inFirstOrderDate = null;
		String inLastOrderDate = null;

		System.out.println("Starting Report 2");
		
		String sql =  "SELECT e.employee_id, count(p.product_id) as products_ordered, min(to_char(f.order_date, 'mm-dd-yyyy')) as \"First Order Date\", max(to_char(f.order_date, 'mm-dd-yyyy')) as \"Last Order Date\""
					+ " FROM TEAM2.employee e"
					+ " INNER JOIN TEAM2.employee_salary s ON e.employee_id = s.employee_id"
					+ " INNER JOIN TEAM2.work_center w ON e.work_center_id = w.work_center_id"
					+ " INNER JOIN TEAM2.produced_in i ON w.work_center_id = i.work_center_id"
					+ " INNER JOIN TEAM2.product p ON i.product_id = p.product_id"
					+ " INNER JOIN TEAM2.order_line o ON p.product_id = o.product_id"
					+ " INNER JOIN TEAM2.factory_order f ON o.order_id = f.order_id"
					+ " WHERE f.order_date BETWEEN s.effective_start_date AND NVL(s.effective_end_date, sysdate)"
					+ " GROUP BY e.employee_id"
					+ " ORDER BY e.employee_id";
		
		System.out.println("Report 2 SQL: "+sql);

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);

		// ps.setInt(1, 40);
		ResultSet rs = ps.executeQuery();
		empOrder.append(String.format("%1$-15s %2$-20s %3$-18s %4$s"+NEW_LINE, "Employee ID", "Products Ordered", "First Ordered", "Last Ordered"));
		while (rs.next())
		{
			inEmployeeID = rs.getString(1);
			inNumProductsOrdered = rs.getString(2);
			inFirstOrderDate = rs.getString(3);
			inLastOrderDate = rs.getString(4);

			String outRow = String.format("%1$-15s %2$-20s %3$-18s %4$s"+NEW_LINE, inEmployeeID, inNumProductsOrdered, inFirstOrderDate, inLastOrderDate);
			empOrder.append(outRow);

		}
		System.out.println("Report 2 Display Complete");

		if (con != null)
			System.out.println("Closing Report 2 connection");
		if(rs != null)
			rs.close();
		ps.close();

		return empOrder;
	}
}
