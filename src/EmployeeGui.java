package com.oracle.csc342.team2.problems;

/******************************************************************
 *  Course:				CSC 342 Advanced Databases
 *  Assignment:			End of semester project
 *  
 *  Code adapted & updated from assignment originally created for:
 *  
 *  COURSE:             CSC231 Computer Science and Programming II
 *	Lab:			    Number 8
 *	FILE:				PhoneBook.java
 *	TARGET:				Java 5.0 and 6.0
 *****************************************************************/

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import javax.swing.JOptionPane;

public class EmployeeGui extends Frame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private PersonDAO pdao;
	private EmployeeDAO edao;
	private WorkCenterDAO wcdao;
	private TextField firstName, lastName, address, city, zip, birthday, phone, fax;
	private Choice state, supervisor, workCenter;
	private Button addButton, updateButton, deleteButton;
	private Employee employee;
	private java.util.List<Employee> employees;
	private java.util.List<WorkCenter> workCenters;
	private FurnitureFactoryGui factory;

	public EmployeeGui(FurnitureFactoryGui furnitureFactoryGui)
	{
		super("Employee Management - Online Furniture Factory"); // set frame title
		
		employee = null;
		pdao = new PersonDAO();
		edao = new EmployeeDAO();
		wcdao = new WorkCenterDAO();
		factory = furnitureFactoryGui;
		try
		{
			employees = edao.generateEmployeeList();
		}
		catch (SQLException e)
		{
			// Skip It
		}
		try
		{
			workCenters = wcdao.generateWorkCenterList();
		}
		catch (SQLException e)
		{
			// Skip It
			System.out.println("Error loading work center information");
		}
		
		setLayout(new GridLayout(6, 4)); // set layout

		Label label = new Label("First Name", Label.LEFT);
		add(label);
		firstName = new TextField();
		add(firstName);
		label = new Label("Last Name", Label.LEFT);
		add(label);
		lastName = new TextField();
		add(lastName);
		label = new Label("Address", Label.LEFT);
		add(label);
		address = new TextField();
		add(address);
		label = new Label("City", Label.LEFT);
		add(label);
		city = new TextField();
		add(city);
		label = new Label("State", Label.LEFT);
		add(label);
		state = Utils.getListOfStates();
		state.insert("Select State", 0);
		add(state);
		label = new Label("Zip", Label.LEFT);
		add(label);
		zip = new TextField();
		add(zip);
		Panel dateLabels = new Panel(new GridLayout(2,1));
		label = new Label("Birthday", Label.LEFT);
		dateLabels.add(label);
		label = new Label("(MM/DD/YYYY)", Label.LEFT);
		dateLabels.add(label);
		add(dateLabels);
		birthday = new TextField();
		add(birthday);
		label = new Label("Phone", Label.LEFT);
		add(label);
		phone = new TextField();
		add(phone);
		label = new Label("Fax", Label.LEFT);
		add(label);
		fax = new TextField();
		add(fax);
		label = new Label("Supervisor", Label.LEFT);
		add(label);
		supervisor = Utils.listToChoice(employees, "Select Supervisor");
		add(supervisor);
		label = new Label("Work Center", Label.LEFT);
		add(label);
		workCenter = Utils.listToChoice(workCenters, "Select Work Center");
		add(workCenter);
		Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
		add(buttonPanel);
		addButton = new Button("Add");
		addButton.addActionListener(this);
		buttonPanel.add(addButton);
		updateButton = new Button("Update");
		updateButton.addActionListener(this);
		buttonPanel.add(updateButton);
		deleteButton = new Button("Delete");
		deleteButton.addActionListener(this);
		buttonPanel.add(deleteButton);
	}

	// implementing ActionListener
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if (source == addButton)
		{
			doSave();
		}
		else if (source == updateButton)
		{
			doSave();
		}
		else if (source == deleteButton)
		{
			doDelete();
		}
	}
	
	private void closeWindow()
	{
		WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
        factory.updateDisplay();
	}
	
	public boolean show(Employee employee)
	{
		if(employee == null)
		{
			JOptionPane.showMessageDialog(this, "No Employee Was Specified", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
		}
		else
		{
			this.employee = employee;
			Employee employeeSuper = null;
			try
			{
				if(!employee.isNew())
				{
					employeeSuper = edao.getSupervisorInfo(employee);
					pdao.loadPersonInfo(employeeSuper);
				}
			}
			catch (SQLException e)
			{
				// Skip It
			}
			
			System.out.println("Employee Supervisor: "+(employeeSuper==null?"NULL":employeeSuper.toString()));
			//System.out.println("Work Center: "+(employeeSuper==null?"NULL":employeeSuper.toString()));

			firstName.setText((employee.isNew()?"":employee.getFirstName()));
			lastName.setText((employee.isNew()?"":employee.getLastName()));
			address.setText((employee.isNew()?"":employee.getAddressInfo().getStreetAddress()));
			city.setText((employee.isNew()?"":employee.getAddressInfo().getCity()));
			if(employee.isNew())
				state.select(0);
			else
				state.select(employee.getAddressInfo().getState());
			zip.setText((employee.isNew()?"":employee.getAddressInfo().getPostalCode()));
			birthday.setText((employee.isNew()?"":employee.getBirthDateFormatted()));
			phone.setText((employee.isNew()?"":employee.getPhoneNum()));
			fax.setText((employee.isNew()?"":employee.getFaxNum()));
			if(employee.isNew())
			{
				supervisor.select(0);
				workCenter.select(0);
			}
			else
			{
				supervisor.select(((employeeSuper==null)?"":employeeSuper.toString()));
				workCenter.select(employee.getWorkCenter().toString());
			}
		
			if(employee.isNew())
			{
				addButton.setVisible(true);
				updateButton.setVisible(false);
				deleteButton.setVisible(false);
			}
			else
			{
				addButton.setVisible(false);
				updateButton.setVisible(true);
				deleteButton.setVisible(true);
			}
			this.setVisible(true);
			return true;
		}
	}

	private void doSave()
	{
		boolean originallyNew = employee.isNew();
		try
		{
			employee.setFirstName(firstName.getText());
			employee.setLastName(lastName.getText());
			employee.getAddressInfo().setStreetAddress(address.getText());
			employee.getAddressInfo().setCity(city.getText());
			employee.getAddressInfo().setState(state.getSelectedItem());
			employee.getAddressInfo().setPostalCode(zip.getText());
			employee.setBirthDate(Utils.stringToTimestamp(birthday.getText()));
			employee.setPhoneNum(phone.getText());
			employee.setFaxNum(fax.getText());
			String tmp = supervisor.getSelectedItem();
			Long id = Long.valueOf(tmp.substring(tmp.indexOf("(")+1, tmp.indexOf(")")));
			employee.setSupervisor(BigDecimal.valueOf(id));
			tmp = workCenter.getSelectedItem();
			tmp = tmp.substring(tmp.indexOf("(")+1, tmp.indexOf(")"));
			employee.setWorkCenter(wcdao.viewWorkCenter(tmp));

			EmployeeDAO dao = new EmployeeDAO();
			dao.saveEmployee(employee);
			closeWindow();
		}
		catch (SQLException e)
		{
			if(originallyNew)
			{
				// Reset back to new
				employee.setEmployeeID(null);
				employee.setPersonId(null);
			}

			System.out.println("An error occurred while saving.  Msg: "+e.getMessage());
			JOptionPane.showMessageDialog(this, "An error occurred while processing your request", "Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (ParseException e)
		{
			System.out.println("An error occurred while saving.  Msg: "+e.getMessage());
			JOptionPane.showMessageDialog(this, "Invalid Date", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void doDelete()
	{
		try
		{
			EmployeeDAO dao = new EmployeeDAO();
			dao.deleteEmployee(employee);
			closeWindow();
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(this, "An error occurred while processing your request", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
