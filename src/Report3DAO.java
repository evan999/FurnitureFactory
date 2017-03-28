package com.oracle.csc342.team2.problems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Report3DAO
{
	private static final String NEW_LINE = "\r\n";
	
	// REPORT 3
	public StringBuffer getProfit() throws SQLException
	{
		StringBuffer workCenterFinanceSummary = new StringBuffer(500);
		String inWorkCenterID = null;
		Float inSalary = null;
		Float inProdCosts = null;
		Float inRevenue = null;
		
		System.out.println("Starting Report 3");

		String sql = "SELECT work_center_id,"
					+"  ("
					+"    SELECT sum(s.salary)"
					+"    FROM team2.employee_salary s "
					+"    INNER JOIN TEAM2.employee e"
					+"    ON s.employee_id = e.employee_id"
					+"    WHERE e.work_center_id=wc.work_center_id"
					+"  ) as total_salary,"
					+"  ("
					+"    SELECT SUM(production_costs)"
					+"    FROM"
					+"    ("
					+"      SELECT p.product_id, (o.ordered_quantity *"
					+"        ("
					+"          SELECT sum((r2.material_standard_cost * u2.goes_into_quantity))"
					+"          FROM TEAM2.product p2"
					+"          INNER JOIN TEAM2.uses u2"
					+"          ON p2.product_id=u2.product_id"
					+"          INNER JOIN TEAM2.raw_material r2"
					+"          ON u2.material_id=r2.material_id"
					+"          WHERE p2.product_id=p.product_id"
					+"        )"
					+"      ) as production_costs"
					+"      FROM TEAM2.produced_in i"
					+"      INNER JOIN TEAM2.product p"
					+"      ON i.product_id = p.product_id"
					+"      INNER JOIN TEAM2.order_line o"
					+"      ON p.product_id = o.product_id"
					+"      WHERE i.work_center_id=wc.work_center_id"
					+"    )"
					+"  ) as total_production_costs,"
					+"  ("
					+"    SELECT SUM(order_revenue)"
					+"    FROM"
					+"    ("
					+"      SELECT p.product_id, (p.product_standard_price * o.ordered_quantity) as order_revenue"
					+"      FROM TEAM2.produced_in i"
					+"      INNER JOIN TEAM2.product p"
					+"      ON i.product_id = p.product_id"
					+"      INNER JOIN TEAM2.order_line o"
					+"      ON p.product_id = o.product_id"
					+"      WHERE i.work_center_id=wc.work_center_id"
					+"    )"
					+"  ) as total_revenue"
					+" FROM work_center wc";
		
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);

		// ps.setInt(1, 40);
		ResultSet rs = ps.executeQuery();
		workCenterFinanceSummary.append(String.format("%1$-18s %2$-16s %3$-26s %4$-16s"+NEW_LINE, "Work Center ID", "Total Salary", "Total Production Costs", "Total Revenue"));
		while (rs.next())
		{
			inWorkCenterID = rs.getString(1);
			inSalary = rs.getFloat(2);
			inProdCosts = rs.getFloat(3);
			inRevenue = rs.getFloat(4);

			String outRow = String.format("%1$-18s %2$,-16.2f %3$,-26.2f %4$,-16.2f"+NEW_LINE, inWorkCenterID, inSalary, inProdCosts, inRevenue);
			workCenterFinanceSummary.append(outRow);

		}
		System.out.println("Report 3 Display Complete");

		if (con != null)
			System.out.println("Closing Report 3 connection");
		if(rs != null)
			rs.close();
		ps.close();

		return workCenterFinanceSummary;
	}
}
