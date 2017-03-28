package com.oracle.csc342.team2.problems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkCenterDAO
{
	private AddressDAO address;
	
	public WorkCenterDAO()
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

		return colPrefix+"work_center_id";
	}

	public WorkCenter loadWorkCenter(WorkCenter wc, ResultSet rs) throws SQLException
	{
		return loadWorkCenter(wc, rs, 0);
	}

	public WorkCenter loadWorkCenter(WorkCenter wc, ResultSet rs, int startingPoint) throws SQLException
	{
		if(wc != null && rs != null)
		{
			int i=startingPoint;
			wc.setWorkCenterId(rs.getString(++i));
	
			System.out.println("Work Center Information Loaded Successfully");
		}
		
		return wc;
	}

	public void createWorkCenter(WorkCenter wc) throws SQLException
	{
		System.out.println("Work Center to be Inserted");
		System.out.println(wc.toString());

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("INSERT INTO TEAM2.work_center ("+sqlColumns("")+") VALUES (?)");
		ps.setString(1, wc.getWorkCenterId());

		ps.executeUpdate();
		address.updateWorkCenterAddress(wc);
		System.out.println("Addition Success");

		if (con != null)
			System.out.println("Closing Work Center create statement");
		ps.close();
	}


	public WorkCenter loadWorkCenterInfo(WorkCenter wc) throws SQLException
	{
		wc.setAddressInfo(address.getWorkCenterAddress(wc));

		System.out.println("Load WorkCenter Information Success");
		return wc;
	}

	public WorkCenter viewWorkCenter(String id) throws SQLException
	{
		WorkCenter result = new WorkCenter();
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("SELECT "+sqlColumns("")+" FROM TEAM2.work_center WHERE work_center_id=?");
		ps.setString(1, id);

		ResultSet rs = ps.executeQuery();
		if(rs.next())
		{
			loadWorkCenter(result, rs);
			result.setAddressInfo(address.getWorkCenterAddress(id));

			System.out.println("View WorkCenter Success");
		}

		if(con != null)
			System.out.println("Closing WorkCenter connection");
		if(rs != null)
			rs.close();
		ps.close();

		return result;
	}

	public void updateWorkCenter(WorkCenter wc) throws SQLException
	{
		System.out.println("WorkCenter to be Updated");
		System.out.println(wc.toString());

		/*
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("UPDATE TEAM2.work_center p SET p.first_name = ?, p.last_name = ?, p.birth_date = ?, p.phone = ?, p.fax = ? WHERE p.work_center_id=?");
		ps.setString(1, wc.getFirstName());
		ps.executeQuery();
		*/
		address.updateWorkCenterAddress(wc);
		System.out.println("Updated");

		/*
		if (con != null)
			System.out.println("Closing WorkCenter connection");
		ps.close();
		*/
	}

	public void deleteWorkCenter(String wcId) throws SQLException
	{
		System.out.println("WorkCenter to be Deleted");
		System.out.println("WorkCenter Id = " + wcId);

		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement("DELETE TEAM2.work_center WHERE work_center_id = ?");
		ps.setString(1, wcId);
		ps.executeQuery();
		System.out.println("Deleted");

		if (con != null)
			System.out.println("Closing WorkCenter connection");
		ps.close();
	}

	public List<WorkCenter> generateWorkCenterList() throws SQLException
	{
		return generateWorkCenterList(null, null);
	}
	public List<WorkCenter> generateWorkCenterList(String order) throws SQLException
	{
		return generateWorkCenterList(order, null);
	}
	public List<WorkCenter> generateWorkCenterList(String order, String filter) throws SQLException
	{
		if(order == null || order.length() < 1)
			order = FurnitureFactoryGui.SORT_DEFAULT;
		
		System.out.println("Looking up information for all WorkCenters");
		List<WorkCenter> list = new ArrayList<WorkCenter>();

		String sql = "SELECT "+this.sqlColumns("")+","+address.sqlColumns("")
					+" FROM TEAM2.work_center";
		
		if(filter != null && filter.length() > 0)
		{
			sql = sql + " WHERE state LIKE ?"
						+ " OR city LIKE ?";
		}
		
		sql = sql + " ORDER BY state "+order+", city "+order;
		
		Connection con = DBConnect.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		
		if(sql.indexOf("?") > -1)
		{
			ps.setString(1, "%"+filter+"%");
			ps.setString(2, "%"+filter+"%");
		}

		System.out.println(sql);

		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			WorkCenter wc = loadWorkCenter(new WorkCenter(), rs);
			address.loadAddress(wc.getAddressInfo(), rs, numberOfColumns());
			list.add(wc);
		}

		System.out.println("Information Retrieved");

		if (con != null)
			System.out.println("Closing WorkCenter connection");
		if(rs != null)
			rs.close();
		ps.close();
		
		return list;
	}
}
