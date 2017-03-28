package com.oracle.csc342.team2.problems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Report1DAO
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
		
		String sql = "SELECT wc.work_center_id, count(s.skill_id) as skill_count, s.skill_description"
					+ " FROM TEAM2.work_center wc"
					+ " INNER JOIN TEAM2.employee e"
					+ " ON wc.work_center_id = e.work_center_id"
					+ " INNER JOIN TEAM2.employee_skills es"
					+ " ON e.employee_id = es.employee_id"
					+ " INNER JOIN TEAM2.skill s"
					+ " ON es.skill_id = s.skill_id"
					+ " GROUP BY wc.work_center_id, s.skill_id, s.skill_description"
					+ " ORDER BY wc.work_center_id ASC, skill_count DESC";
		
		System.out.println("Report 1 SQL: "+sql);

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		// ps.setInt(1, 40);
		ResultSet rs = ps.executeQuery();
		skillReport.append(String.format("%1$-14s %2$-11s %3$s"+NEW_LINE, "Work Center ID", "Skill Count", "Skill Description"));
		while (rs.next())
		{
			inWorkCID = rs.getString(1);
			inSkillCount = rs.getInt(2);
			inSkillDesc = rs.getString(3);

			String outRow = String.format("%1$-14s %2$-11d %3$s"+NEW_LINE, inWorkCID, inSkillCount, inSkillDesc);
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
}
