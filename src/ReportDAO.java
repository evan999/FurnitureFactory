package com.oracle.csc342.team2.problems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDAO
{
	private static final String NEW_LINE = "\r\n";
	
	// REPORT 1
	public StringBuffer getSkillDemand() throws SQLException
	{
		StringBuffer skillReport = new StringBuffer(500);
		String inWorkCID = null;
		int inSkillCount = 0;
		String inSkillDesc = null;

		System.out.println("Starting Report 1");
		
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT wc.work_center_id, count(es.skill_id), s.skill_description"
												+ " FROM TEAM2.work_center wc"
												+ " INNER JOIN TEAM2.employee e"
												+ " ON wc.work_center_id = e.work_center_id"
												+ " INNER JOIN TEAM2.employee_skills es"
												+ " ON e.employee_id = es.employee_id"
												+ " INNER JOIN TEAM2.skill s"
												+ " ON es.skill_id = s.skill_id"
												+ " GROUP BY wc.work_center_id, s.skill_id, s.skill_description"
		);

		// ps.setInt(1, 40);
		ResultSet rs = ps.executeQuery();
		skillReport.append(String.format("%1$-14s %2$-11s %3$s"+NEW_LINE, "Work Center ID", "Skill Count", "Skill Description"));
		while (rs.next())
		{
			inWorkCID = rs.getString(1);
			inSkillCount = rs.getInt(2);
			inSkillDesc = rs.getString(3);

			String outRow = String.format("%1$-14s %2$-11o %3$s"+NEW_LINE, inWorkCID, inSkillCount, inSkillDesc);
			skillReport.append(outRow);
		}
		System.out.println("Report 1 Display Complete");

		if (con != null)
			System.out.println("Closing Report 1 connection");
		if (rs != null)
			rs.close();
		ps.close();

		return skillReport;
	}

	// REPORT 2
	public StringBuffer getEmployeeOrder() throws SQLException
	{
		StringBuffer empOrder = new StringBuffer(500);
		String inEmployeeID = null;
		String inProductID = null;
		String inOrderID = null;

		System.out.println("Starting Report 2");
		
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT e.employee_id, p.product_id, o.order_id"
												+ " FROM TEAM2.employee e"
												+ " INNER JOIN TEAM2.employee_salary s ON e.employee_id = s.employee_id"
												+ " INNER JOIN TEAM2.work_center w ON e.work_center_id = w.work_center_id"
												+ " INNER JOIN TEAM2.produced_in i ON w.work_center_id = i.work_center_id"
												+ " INNER JOIN TEAM2.product p ON i.product_id = p.product_id"
												+ " INNER JOIN TEAM2.order_line o ON p.product_id = o.product_id"
												+ " INNER JOIN TEAM2.factory_order f ON o.order_id = f.order_id"
												+ " WHERE f.order_date < s.effective_end_date"
												+ " AND f.order_date > s.effective_start_date"
												+ " GROUP BY e.employee_id, p.product_id, o.order_id"
		);

		// ps.setInt(1, 40);
		ResultSet rs = ps.executeQuery();
		empOrder.append(String.format("%1$-11s %2$-10s %3$s"+NEW_LINE, "Employee ID", "Product ID", "Order ID"));
		while (rs.next())
		{
			inEmployeeID = rs.getString(1);
			inProductID = rs.getString(2);
			inOrderID = rs.getString(3);

			String outRow = String.format("%1$-11s %2$-10s %3$s"+NEW_LINE, inEmployeeID, inProductID, inOrderID);
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

	// REPORT 3
	public StringBuffer getProfit() throws SQLException
	{
		StringBuffer WorkCProfit = new StringBuffer(500);
		String inWorkCID = null;
		String inCity = null;
		String inState = null;
		String inTotSalary = null;
		String inRevenue = null;
		
		System.out.println("Starting Report 3");

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("select * from work_center w, "
				+ "(select sum(s.salary) as TotalSalary, (p.product_standard_price*o.ordered_quantity) as Revenue" + "from team2.employee_salary s"
				+ "inner join team2.employee e on team2.s.employee_id = team2.e.employee_id"
				+ "inner join team2.work_center w on team2.e.work_center_id = team2.w.work_center_id"
				+ "inner join team2.produced_in i on team2.w.work_center_id = team2.i.work_center_id"
				+ "inner join team2.product p on team2.i.product_id = team2.p.product_id"
				+ "inner join team2.order_line o on team2.p.product_id = team2.o.product_id" + "group by (p.product_standard_price*o.ordered_quantity));");

		// ps.setInt(1, 40);
		ResultSet rs = ps.executeQuery();
		WorkCProfit.append(String.format("%1$-16s %2$-40s %3$s"+NEW_LINE, "Work Center ID", "City", "State", "Total Salary", "Revenue"));
		while (rs.next())
		{

			inWorkCID = rs.getString(1);
			inCity = rs.getString(2);
			inState = rs.getString(3);
			inTotSalary = rs.getString(4);
			inRevenue = rs.getString(5);

			String outRow = String.format("%1$-16s %2$-40s %3$d"+NEW_LINE, inWorkCID, inCity, inState, inTotSalary, inRevenue);
			WorkCProfit.append(outRow);

		}
		System.out.println("Report 3 Display Complete");

		if (con != null)
			System.out.println("Closing Report 3 connection");
		if(rs != null)
			rs.close();
		ps.close();

		return WorkCProfit;
	}
}
