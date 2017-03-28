package com.oracle.csc342.team2.problems;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class Reports2DAO {
	

public StringBuffer getSkillSummary() throws SQLException
{

    StringBuffer skillsReport = new StringBuffer(500);
    String inRange = null;
    String inSkillId = null;
    String inSkillDesc = null;
    int inSkillCount = 0;
    
    Connection con = null;
    PreparedStatement ps= null;
    ResultSet rs=null;
    
    
    try
    {
    
    	con = DBConnect.getConnection();
    	ps=con.prepareStatement(" select age_range, S.skill_id, B.skill_description, count(*) " +
             " from (select e.employee_id, case " +
             " when trunc((sysdate-p.birth_date)/365.2425) < 25 then \' Under 25\' " +
             " when trunc((sysdate-p.birth_date)/365.2425) between 25 and 35 then \'25  to 35\' " +
             " when trunc((sysdate-p.birth_date)/365.2425) between 36 and 45 then \'36  to 45\' " +
             " when trunc((sysdate-p.birth_date)/365.2425) between 46 and 55 then \'46  to 55\' " +
             " when trunc((sysdate-p.birth_date)/365.2425) > 55 then \'Over   55\' " +
             " else \'error\' " +
             " end as age_range " +
             " from CSC342.person P join CSC342.employee E on (p.person_id = e.employee_id)) Z " +
             " join CSC342.employee_skills S on (Z.employee_ID = S.employee_ID) join CSC342.skill B on (S.skill_id = B.skill_id) " +
             " group by age_range, S.skill_id, B.skill_description order by age_range" );
        
    	rs=ps.executeQuery();
        skillsReport.append(String.format("%1$-16s %2$-40s %3$s %4$-40s\n","Age Range", "Skill Id","Skill Description","Skill Count"));
        while(rs.next())
        {
        	
        	inRange = rs.getString(1);
        	inSkillId = rs.getString(2);
            inSkillDesc = rs.getString(3);
            inSkillCount = rs.getInt(4);
            
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

	

