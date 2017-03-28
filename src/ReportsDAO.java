package com.oracle.csc342.team2.problems;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class ReportsDAO {
	

public StringBuffer getSkillSummary() throws SQLException
{

    StringBuffer skillsReport = new StringBuffer(500);
	String inSkillId = null;
    String inSkillDesc = null;
    int inSkillCount = 0;
    
    Connection con = null;
    PreparedStatement ps= null;
    ResultSet rs=null;
    
    
    try
    {
    
    	con = DBConnect.getConnection();
    	ps=con.prepareStatement("select A.skill_id, B.skill_description, count(*) from CSC342.employee_skills A " +
    			                 "JOIN CSC342.Skill B ON (A.SKILL_ID = B.SKILL_ID) " +
    			                 "group by A.skill_id, B.skill_description having count(*) > ? ");
        
        ps.setInt(1, 40);
    	rs=ps.executeQuery();
        skillsReport.append(String.format("%1$-16s %2$-40s %3$s \n","Skill Id","Skill Description","Skill Count"));
        while(rs.next())
        {
        	
        	inSkillId = rs.getString(1);
            inSkillDesc = rs.getString(2);
            inSkillCount = rs.getInt(3);
            
            String outRow = String.format("%1$-16s %2$-40s %3$d \n", inSkillId, inSkillDesc, inSkillCount);
            skillsReport.append(outRow);
                
        }
        System.out.println("Display Complete");
    }
    catch(SQLException e)
    {
        System.out.println("SQL Error in get skill summary" + e.getSQLState());
        System.out.println("/nError Code: " + e.getErrorCode());
        System.out.println("/nMessage: " + e.getMessage());
        System.exit( 1 );
    }
    catch(Exception e)
    {
        System.out.println("unknown Error in get skill summary");
        System.out.println("/nMessage: " + e.getMessage());
        System.exit( 1 );
    }
    finally
	{
		if (con != null)
			System.out.println("closing Person connection \n");
			rs.close();
			ps.close();
	}
    return skillsReport;
}

}

	

