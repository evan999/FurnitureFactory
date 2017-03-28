package com.oracle.csc342.team2.problems;

import java.math.BigDecimal;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAO
{
	private AddressDAO address;
	private PersonDAO person;
	private WorkCenterDAO workCenter;

	public EmployeeDAO()
	{
		address = new AddressDAO();
		person = new PersonDAO();
		workCenter = new WorkCenterDAO();
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

		return colPrefix+"employee_id, "+colPrefix+"employee_supervisor, "+colPrefix+"work_center_id";
	}
	
	private Employee loadEmployee(Employee employee, ResultSet rs) throws SQLException
	{
		return loadEmployee(employee, rs, 0);
	}

	private Employee loadEmployee(Employee employee, ResultSet rs, int startingPoint) throws SQLException
	{
		if(employee != null && rs != null)
		{
			int i=startingPoint;
			employee.setEmployeeID(rs.getBigDecimal(++i));
			employee.setSupervisor(BigDecimal.valueOf(rs.getLong(++i)));
			employee.setWorkCenter(workCenter.viewWorkCenter(rs.getString(++i)));
	
			System.out.println("Employee Information Loaded Successfully");
		}
		
		return employee;
	}
	
	public Employee getSupervisorInfo(Employee employee) throws SQLException
	{
		if(employee == null || employee.getSupervisor() == null)
			return null;
		else
			return viewEmployee(employee.getSupervisor());
		
	}

	public void createEmployee(Employee employee) throws SQLException
	{
		Connection con = null;
		PreparedStatement ps = null;

		System.out.println("Employee to be inserted");
		System.out.println(employee.toString());
		
		person.createPerson(employee);

		con = DBConnect.getConnection();
		ps = con.prepareStatement("INSERT INTO TEAM2.employee ("+sqlColumns("")+") VALUES (?,?,?)");
		ps.setBigDecimal(1, employee.getEmployeeID());
		ps.setBigDecimal(2, employee.getSupervisor());
		ps.setString(3, employee.getWorkCenter().getWorkCenterId());

		ps.executeUpdate();
		System.out.println("Addition successful");

		if (con != null)
			System.out.println("Closing Employee create statement");
		ps.close();
	}

	public Employee viewEmployee(BigDecimal employeeID) throws SQLException
	{

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		Employee outEmployee = null;

		try
		{

			con = DBConnect.getConnection();
			ps = con.prepareStatement("SELECT " + sqlColumns("e") + " FROM TEAM2.employee e WHERE e.employee_id=?");
			ps.setBigDecimal(1, employeeID);

			rs = ps.executeQuery();
			if(rs.next())
			{
				outEmployee = loadEmployee(new Employee(), rs);
				System.out.println("View Person Success");
			}
		}
		catch (SQLException e)
		{
			System.out.println("Error in Employee view access" + e.getSQLState());
			System.out.println("Error Code: " + e.getErrorCode());
			System.out.println("Message: " + e.getMessage());
			System.exit(1);
		}
		catch (Exception e)
		{
			System.out.println("Unknown error in Employee view access");
			System.out.println("Message: " + e.getMessage());
			System.exit(1);
		}
		finally
		{
			if (con != null)
				System.out.println("closing Employee connection");
			rs.close();
			ps.close();
		}
		return outEmployee;
	}

	public void updateEmployee(Employee empl) throws SQLException
	{
		System.out.println("Employee to be updated");
		System.out.println(empl.toString());

		Connection con = DBConnect.getConnection();

		PreparedStatement ps = con.prepareStatement("UPDATE TEAM2.employee e set e.employee_supervisor = ?, e.work_center_id = ? WHERE e.employee_id = ?");

		ps.setBigDecimal(1, empl.getSupervisor());
		ps.setString(2, empl.getWorkCenter().getWorkCenterId());
		ps.setBigDecimal(3, empl.getEmployeeID());
		ps.executeQuery();
		
		person.updatePerson(empl);
		
		System.out.println("Updated");
		if (con != null)
			System.out.println("Closing Employee connection");
		ps.close();
	}

	public void deleteEmployee(Employee employee) throws SQLException
	{
		if(employee == null)
			throw new SQLException("Invalid Employee");
		else
			deleteEmployee(employee.getEmployeeID());
	}
	public void deleteEmployee(BigDecimal employeeID) throws SQLException
	{
		System.out.println("Employee to be deleted");
		System.out.println("Employee ID = " + employeeID);

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("DELETE TEAM2.employee WHERE employee_id=?");
		ps.setBigDecimal(1, employeeID);
		ps.executeQuery();

		System.out.println("Deleted");
		
		if (con != null)
			System.out.println("Closing Employee connection");
		ps.close();
	}

	public void countEmployee() throws SQLException
	{
		String sql1 = "SELECT count(*) from TEAM2.employee e INNER JOIN TEAM2.employee_skills k " + " ON (e.employee_id = k.employee_id)";

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement(sql1);
		int employeeCt = 0;

		ResultSet rs = ps.executeQuery();
		if(rs.next())
		{
			employeeCt = rs.getInt(1);
		}

		System.out.println("countPeople success " + employeeCt);

		if (con != null)
			System.out.println("Closing count objects");
		rs.close();
		ps.close();
	}

	public void saveEmployee(Employee employee) throws SQLException
	{
		if (employee.isNew())
			createEmployee(employee);
		else
			updateEmployee(employee);
	}

	public void saveEmployees(List<Employee> employees) throws SQLException
	{
		String sql1 = "SELECT count(*) as employee_count FROM TEAM2.employee e WHERE e.employee_id = ?";
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement(sql1);
		ResultSet rs = null;

		for (Iterator<Employee> it = employees.iterator(); it.hasNext();)
		{
			Employee testEmployee = it.next();
			ps.setBigDecimal(1, testEmployee.getEmployeeID());
			rs = ps.executeQuery();
			while (rs.next())
			{
				if (rs.getInt(1) == 1)
					updateEmployee(testEmployee);
				else if (rs.getInt(1) == 0)
					createEmployee(testEmployee);
				else
					throw new RuntimeException("More than one employee has this Employee ID.");
			}
		}
		if(rs != null)
			rs.close();
		ps.close();
	}

	public List<Employee> generateEmployeeList() throws SQLException
	{
		return generateEmployeeList(null, null);
	}
	public List<Employee> generateEmployeeList(String order) throws SQLException
	{
		return generateEmployeeList(order, null);
	}
	public List<Employee> generateEmployeeList(String order, String filter) throws SQLException
	{
		if(order == null || order.length() < 1)
			order = FurnitureFactoryGui.SORT_DEFAULT;
		
		System.out.println("Looking up information for all Employees");
		List<Employee> list = new ArrayList<Employee>();

		String sql = "SELECT "+this.sqlColumns("e")
					+","+person.sqlColumns("p")
					+","+address.sqlColumns("p")
					+" FROM TEAM2.employee e"
					+" INNER JOIN TEAM2.person p"
					+" ON e.employee_id=p.person_id"; 
		
		if(filter != null && filter.length() > 0)
		{
			sql = sql + " WHERE LOWER(p.first_name) LIKE ?"
						+ " OR LOWER(p.last_name) LIKE ?";
		}
		
		sql = sql + " ORDER BY last_name "+order+", first_name "+order;
		
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		
		if(sql.indexOf("?") > -1)
		{
			ps.setString(1, "%"+filter.toLowerCase()+"%");
			ps.setString(2, "%"+filter.toLowerCase()+"%");
		}

		System.out.println(sql);

		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			int startingPosition = 0;
			Employee employee = loadEmployee(new Employee(), rs);
			startingPosition += this.numberOfColumns();
			person.loadPerson(employee, rs, startingPosition);
			startingPosition += person.numberOfColumns();
			address.loadAddress(employee.getAddressInfo(), rs, startingPosition);
			
			// This method loads everything (above several steps) for us, but makes 
			// multiple DB calls to do so which is not efficient and very slow
			//person.loadPersonInfo(employee);
			list.add(employee);
		}

		System.out.println("Information Retrieved");

		if (con != null)
			System.out.println("Closing Employee connection");
		if(rs != null)
			rs.close();
		ps.close();
		
		return list;
	}
}
