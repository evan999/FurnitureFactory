package com.oracle.csc342.team2.problems;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressDAO
{
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
		
		return colPrefix+"street_address, "+colPrefix+"city, "+colPrefix+"state, "+colPrefix+"postal_code";
	}

	public Address loadAddress(Address address, ResultSet rs) throws SQLException
	{
		return loadAddress(address, rs, 0);
	}

	public Address loadAddress(Address address, ResultSet rs, int startingPoint) throws SQLException
	{
		if(address != null && rs != null)
		{
			int i=startingPoint;
			address.setStreetAddress(rs.getString(++i));
			address.setCity(rs.getString(++i));
			address.setState(rs.getString(++i));
			address.setPostalCode(rs.getString(++i));
	
			System.out.println("Address Information Loaded Successfully");
		}
		
		return address;
	}

	public Address getPersonAddress(Person person) throws SQLException
	{
		if(person == null)
			throw new SQLException("Invalid Person");
		else
			return getPersonAddress(person.getPersonId());
	}
	public Address getPersonAddress(BigDecimal personId) throws SQLException
	{
		ResultSet rs = null;
		Address outAddress = new Address();
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT " + sqlColumns("p") + " FROM TEAM2.person p WHERE p.person_id = ?");
		ps.setBigDecimal(1, personId);

		rs = ps.executeQuery();
		if(rs.next())
		{
			loadAddress(outAddress, rs);
			System.out.println("View Address Success");
		}
		if (con != null)
			System.out.println("Closing Person connection");
		if(rs != null)
			rs.close();
		ps.close();

		return outAddress;
	}

	public void updatePersonAddress(Person person) throws SQLException
	{
		System.out.println("Person to be Updated");
		System.out.println(person.toString());
	
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("UPDATE TEAM2.person p SET p.street_address = ?, p.city = ?, p.state = ?, p.postal_code = ? WHERE person_id = ?");
		ps.setString(1, person.getAddressInfo().getStreetAddress());
		ps.setString(2, person.getAddressInfo().getCity());
		ps.setString(3, person.getAddressInfo().getState());
		ps.setString(4, person.getAddressInfo().getPostalCode());
		ps.setBigDecimal(5, person.getPersonId());
		ps.executeQuery();
	
		System.out.println("Updated");

		if (con != null)
			System.out.println("Closing Person connection");
		ps.close();
	}

	public Address getWorkCenterAddress(WorkCenter wc) throws SQLException
	{
		if(wc == null)
			throw new SQLException("Invalid Work Center");
		else
			return getWorkCenterAddress(wc.getWorkCenterId());
	}

	public Address getWorkCenterAddress(String id) throws SQLException
	{
		ResultSet rs = null;
		Address outAddress = new Address();
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT " + sqlColumns("") + " FROM TEAM2.work_center WHERE work_center_id = ?");
		ps.setString(1, id);

		rs = ps.executeQuery();
		if(rs.next())
		{
			loadAddress(outAddress, rs);
			System.out.println("View Address Success");
		}
		if (con != null)
			System.out.println("Closing Work Center Connection");
		if(rs != null)
			rs.close();
		ps.close();

		return outAddress;
	}

	public void updateWorkCenterAddress(WorkCenter wc) throws SQLException
	{
		System.out.println("Work Center to be Updated");
		System.out.println(wc.toString());
	
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("UPDATE TEAM2.work_center SET street_address = ?, city = ?, state = ?, postal_code = ? WHERE work_center_id = ?");
		ps.setString(1, wc.getAddressInfo().getStreetAddress());
		ps.setString(2, wc.getAddressInfo().getCity());
		ps.setString(3, wc.getAddressInfo().getState());
		ps.setString(4, wc.getAddressInfo().getPostalCode());
		ps.setString(5, wc.getWorkCenterId());
		ps.executeQuery();
	
		System.out.println("Updated");

		if (con != null)
			System.out.println("Closing Work Center Connection");
		ps.close();
	}
}
