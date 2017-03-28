package com.oracle.csc342.team2.problems;

import java.math.BigDecimal;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonDAO
{
	private AddressDAO address;
	
	public PersonDAO()
	{
		 address = new AddressDAO();
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

		return colPrefix+"person_id, "+colPrefix+"first_name, "+colPrefix+"last_name, "+colPrefix+"birth_date, "+colPrefix+"phone, "+colPrefix+"fax";
	}

	public Person loadPerson(Person person, ResultSet rs) throws SQLException
	{
		return loadPerson(person, rs, 0);
	}

	public Person loadPerson(Person person, ResultSet rs, int startingPoint) throws SQLException
	{
		if(person != null && rs != null)
		{
			int i=startingPoint;
			person.setPersonId(rs.getBigDecimal(++i));
			person.setFirstName(rs.getString(++i));
			person.setLastName(rs.getString(++i));
			person.setBirthDate(rs.getTimestamp(++i));
			person.setPhoneNum(rs.getString(++i));
			person.setFaxNum(rs.getString(++i));
	
			System.out.println("Person Information Loaded Successfully");
		}
		
		return person;
	}

	public void createPerson(Person person) throws SQLException
	{
		System.out.println("Person to be Inserted");
		System.out.println(person.toString());

		person.setPersonId(BigDecimal.valueOf(findMaxPersonId().longValue()+1));
		
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("INSERT INTO TEAM2.person ("+sqlColumns("")+") VALUES (?,?,?,?,?,?)");
		ps.setBigDecimal(1, person.getPersonId());
		ps.setString(2, person.getFirstName());
		ps.setString(3, person.getLastName());
		ps.setTimestamp(4, person.getBirthDate());
		ps.setString(5, person.getPhoneNum());
		ps.setString(6, person.getFaxNum());

		ps.executeUpdate();
		address.updatePersonAddress(person);
		System.out.println("Addition Success");

		if (con != null)
			System.out.println("Closing Person create statement");
		ps.close();
	}

	public BigDecimal findMaxPersonId() throws SQLException
	{
		BigDecimal maxPersonId = new BigDecimal(0);

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT MAX(p.person_id) FROM TEAM2.person p");

		ResultSet rs = ps.executeQuery();
		if(rs.next())
		{
			maxPersonId = rs.getBigDecimal(1);
			System.out.println("Get Max Person Id Success ");

		}

		if(con != null)
			System.out.println("Closing Person connection");
		if(rs != null)
			rs.close();
		ps.close();

		return maxPersonId;
	}

	public Employee loadPersonInfo(Employee employee) throws SQLException
	{
		Person info = viewPerson(employee.getEmployeeID());
		if(info != null)
		{
			employee.setPersonId(info.getPersonId());
			employee.setFirstName(info.getFirstName());
			employee.setLastName(info.getLastName());
			employee.setBirthDate(info.getBirthDate());
			employee.setPhoneNum(info.getPhoneNum());
			employee.setFaxNum(info.getFaxNum());
		}
			
		employee.setAddressInfo(address.getPersonAddress(employee));

		System.out.println("Load Person Information Success");
		return employee;
	}

	public Person viewPerson(BigDecimal personId) throws SQLException
	{
		Person outPerson = new Person();
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT "+sqlColumns("")+" FROM TEAM2.person p WHERE p.person_id=?");
		ps.setBigDecimal(1, personId);

		ResultSet rs = ps.executeQuery();
		if(rs.next())
		{
			loadPerson(outPerson, rs);
			outPerson.setAddressInfo(address.getPersonAddress(personId));

			System.out.println("View Person Success");
		}

		if(con != null)
			System.out.println("Closing Person connection");
		if(rs != null)
			rs.close();
		ps.close();

		return outPerson;
	}

	public void updatePerson(Person person) throws SQLException
	{
		System.out.println("Person to be Updated");
		System.out.println(person.toString());

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("UPDATE TEAM2.person p SET p.first_name = ?, p.last_name = ?, p.birth_date = ?, p.phone = ?, p.fax = ? WHERE p.person_id=?");
		ps.setString(1, person.getFirstName());
		ps.setString(2, person.getLastName());
		ps.setTimestamp(3, person.getBirthDate());
		ps.setString(4, person.getPhoneNum());
		ps.setString(5, person.getFaxNum());
		ps.setBigDecimal(6, person.getPersonId());
		ps.executeQuery();
		
		address.updatePersonAddress(person);
		System.out.println("Updated");

		if (con != null)
			System.out.println("Closing Person connection");
		ps.close();
	}

	public void deletePerson(BigDecimal personId) throws SQLException
	{
		System.out.println("Person to be Deleted");
		System.out.println("Person Id = " + personId);

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("DELETE TEAM2.person WHERE person_id = ?");
		ps.setBigDecimal(1, personId);
		ps.executeQuery();
		System.out.println("Deleted");

		if (con != null)
			System.out.println("Closing Person connection");
		ps.close();
	}

	public void countPeople() throws SQLException
	{
		String sql1 = "SELECT COUNT(*) FROM TEAM2.person p INNER JOIN TEAM2.customer c " + " ON (p.person_id = c.customer_id)";

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement(sql1);
		int personCt = 0;

		ResultSet rs = ps.executeQuery();
		while (rs.next())
		{
			personCt = rs.getInt(1);
		}

		System.out.println("countPeople success " + personCt);

		if (con != null)
			System.out.println("closing count objects");
		if(rs != null)
			rs.close();
		ps.close();
	}

	public void savePeople(List<Person> people) throws SQLException
	{
		String sql1 = "SELECT COUNT(*) as person_count FROM TEAM2.person p WHERE p.person_id = ?";

		ResultSet rs = null;
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement(sql1);

		for (Iterator<Person> it = people.iterator(); it.hasNext();)
		{
			Person testPerson = it.next();
			ps.setBigDecimal(1, testPerson.getPersonId());
			rs = ps.executeQuery();
			while (rs.next())
			{
				if (rs.getInt(1) == 1)
					updatePerson(testPerson);
				else if (rs.getInt(1) == 0)
					createPerson(testPerson);
				else
					throw new RuntimeException("More than one person has Person Id");
			}
		}
		if(rs != null)
			rs.close();
		ps.close();
	}
}
