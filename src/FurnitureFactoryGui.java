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
import java.sql.SQLException;
import javax.swing.*;

public class FurnitureFactoryGui extends Frame implements ActionListener, ItemListener, KeyListener
{
	private static final long serialVersionUID = 1L;
	private Frame factory;
	private EmployeeGui employeeFrame;
	private ProductGui productFrame;
	private ReportGui reports;
	private MenuItem exitMI, searchMI, sortMI;
	private Label status;
	private TextField listFilter;
	private java.awt.List listChoices;
	private java.util.List<Product> products = null;
	private java.util.List<Employee> employees = null;
	private Button employeeButton, productButton, report1Button, report2Button, report3Button, report4Button, filterButton, sortButton, cancelSortButton;
	private JDialog getSearchType;
	private JComboBox<String> sortList;
	private String sort;
	
	private static String DEFAULT_STATUS = "Select Something To List On The Left";

	public static int SORT_DESCENDING = 1;
	public static int SORT_ASCENDING = 2;
	private static String DESCENDING = "DESC";
	private static String ASCENDING = "ASC";
	public static String SORT_DEFAULT = ASCENDING;
	
	private static int LIST_EMPLOYEES = 10;
	private static int LIST_PRODUCTS = 11;

	/**
	 * Constructor
	 */
	public FurnitureFactoryGui()
	{
		super("Online Furniture Factory"); // set frame title
		
		factory = this;
		reports = new ReportGui();
		
		sort = SORT_DEFAULT;
		
		productFrame = new ProductGui(this);
		productFrame.setSize(500, 300);
		productFrame.setLocation(100, 200);

		// add window closing listener
		productFrame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				productFrame.setVisible(false);
				factory.setVisible(true);
			}
		});

		// show the frame
		productFrame.setVisible(false);

		
		employeeFrame = new EmployeeGui(this);
		employeeFrame.setSize(500, 300);
		employeeFrame.setLocation(100, 200);

		// add window closing listener
		employeeFrame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				employeeFrame.setVisible(false);
				factory.setVisible(true);
			}
		});

		// show the frame
		employeeFrame.setVisible(false);
		
		
		setLayout(new BorderLayout()); // set layout

		// create menu bar
		MenuBar menubar = new MenuBar();
		setMenuBar(menubar);

		// create file menu
		Menu fileMenu = new Menu("File");
		menubar.add(fileMenu);
		exitMI = fileMenu.add(new MenuItem("Exit"));
		exitMI.addActionListener(this);

		// create edit menu
		Menu editMenu = new Menu("Options");
		menubar.add(editMenu);
		searchMI = editMenu.add(new MenuItem("Search"));
		searchMI.addActionListener(this);
		sortMI = editMenu.add(new MenuItem("Sort"));
		sortMI.addActionListener(this);

		Panel listPanel = new Panel(new BorderLayout());
		add(listPanel, BorderLayout.CENTER);
		Panel topPanel = new Panel(new BorderLayout());
		listPanel.add(topPanel, BorderLayout.NORTH);
		status = new Label(DEFAULT_STATUS, Label.LEFT);
		topPanel.add(status, BorderLayout.WEST);
		Panel filterPanel = new Panel(new GridLayout(1,2));
	    topPanel.add(filterPanel, BorderLayout.EAST);
	    listFilter = new TextField();
	    listFilter.addKeyListener(this);
	    filterPanel.add(listFilter);
	    filterButton = new Button("Filter");
	    filterButton.addActionListener(this);
	    filterPanel.add(filterButton);
		listChoices = new List();
		listChoices.addItemListener(this);
		listPanel.add(listChoices, BorderLayout.CENTER);
		Panel panel = new Panel(new BorderLayout());
		add(panel, BorderLayout.WEST);
		Panel editPanel = new Panel(new GridLayout(14, 1));
		panel.add(editPanel, BorderLayout.NORTH);
		Label label = new Label("", Label.LEFT);
		editPanel.add(label);
		label = new Label("Actions:", Label.LEFT);
		editPanel.add(label);
		employeeButton = new Button("Employees");
		employeeButton.addActionListener(this);
		editPanel.add(employeeButton);
		label = new Label("", Label.LEFT);
		editPanel.add(label);
		productButton = new Button("Products");
		productButton.addActionListener(this);
		editPanel.add(productButton);
		label = new Label("", Label.LEFT);
		editPanel.add(label);
		label = new Label("Reports:", Label.LEFT);
		editPanel.add(label);
		report1Button = new Button("Report 1");
		report1Button.addActionListener(this);
		editPanel.add(report1Button);
		label = new Label("", Label.LEFT);
		editPanel.add(label);
		report2Button = new Button("Report 2");
		report2Button.addActionListener(this);
		editPanel.add(report2Button);
		label = new Label("", Label.LEFT);
		editPanel.add(label);
		report3Button = new Button("Report 3");
		report3Button.addActionListener(this);
		editPanel.add(report3Button);
		label = new Label("", Label.LEFT);
		editPanel.add(label);
		report4Button = new Button("Report 4");
		report4Button.addActionListener(this);
		editPanel.add(report4Button);
	}

    /**
     * Implementing ItemListener to display the selected entry
     */
    public void itemStateChanged(ItemEvent event) 
    {
		boolean windowOpened = false;
		if(products != null)
		{
			System.out.println("Selected Product Index: "+listChoices.getSelectedIndex());
			int selectedOption = listChoices.getSelectedIndex();
			if(selectedOption == 0)
				windowOpened = productFrame.show(new Product());
			else
				windowOpened = productFrame.show(products.get(listChoices.getSelectedIndex()-1));
		}
		else if(employees != null)
		{
			System.out.println("Selected Employee Index: "+listChoices.getSelectedIndex());
			int selectedOption = listChoices.getSelectedIndex();
			if(selectedOption == 0)
				windowOpened = employeeFrame.show(new Employee());
			else
				windowOpened = employeeFrame.show(employees.get(listChoices.getSelectedIndex()-1));
		}

		if(windowOpened)
			factory.setVisible(false);
    }

	// implementing ActionListener
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if (source == exitMI)
		{
			System.exit(0);
		}
		else if (source == employeeButton)
		{
			display(LIST_EMPLOYEES);
		}
		else if (source == productButton)
		{
			display(LIST_PRODUCTS);
		}
		else if (source == filterButton)
		{
			updateDisplay();
		}
		else if (source == report1Button)
		{
			runReport(ReportGui.REPORT1);
		}
		else if (source == report2Button)
		{
			runReport(ReportGui.REPORT2);
		}
		else if (source == report3Button)
		{
			runReport(ReportGui.REPORT3);
		}
		else if (source == report4Button)
		{
			runReport(ReportGui.REPORT4);
		}
		else if (source == searchMI)
		{
			String searchName = JOptionPane.showInputDialog(this, "Please enter a name to search for:");
			listFilter.setText(searchName);
			updateDisplay();
		}
		else if (source == sortMI)
		{
			getSearchType = new JDialog(this, "Choose How To Sort", true);
			getSearchType.setLayout(new BorderLayout());
			getSearchType.setSize(350, 100);
			Panel searchTypePanel = new Panel(new FlowLayout(FlowLayout.LEFT));
			getSearchType.add(searchTypePanel, BorderLayout.NORTH);
			searchTypePanel.add(new Label("Sorting Order: "));
			String[] sortTypes = { "Select a Sort Type", "Descending", "Asending" };
			sortList = new JComboBox<String>(sortTypes);
			searchTypePanel.add(sortList);
			Panel sortingButtons = new Panel(new FlowLayout(FlowLayout.LEFT));
			getSearchType.add(sortingButtons, BorderLayout.SOUTH);
			sortButton = new Button("Continue");
			cancelSortButton = new Button("Cancel");
			sortingButtons.add(sortButton);
			sortButton.addActionListener(this);
			sortingButtons.add(cancelSortButton);
			cancelSortButton.addActionListener(this);

			getSearchType.setVisible(true);
		}
		else if (source == sortButton)
		{
			getSearchType.setVisible(false);
			doSort(sortList.getSelectedIndex());
		}
		else if (source == cancelSortButton)
		{
			getSearchType.setVisible(false);
		}
	}

	public void keyPressed(KeyEvent e)
	{
		//System.out.println("Key Pressed: "+e.getKeyChar()+" ("+e.getKeyCode()+")");
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			updateDisplay();
		}
	}

	public void keyReleased(KeyEvent e)
	{
		// We don't care when the key is let up	
	}

	public void keyTyped(KeyEvent e)
	{
		// The key code is not available when typed
	}

	private void doSort(int sortType)
	{
		switch (sortType)
		{
			case 1: // DESCENDING
				sort = DESCENDING;
				break;
			case 2: // ASENDING
				sort = ASCENDING;
				break;
			default:
				JOptionPane.showMessageDialog(this, "Invalid Sorting Choice.", "Error", JOptionPane.ERROR_MESSAGE);
				getSearchType.setVisible(true);
				return;
		}
		updateDisplay();
	}
	
	private void runReport(int reportNumber)
	{
		String previousStatus = status.getText();
		try
		{
			employeeButton.setEnabled(false);
			productButton.setEnabled(false);
			report1Button.setEnabled(false);
			report2Button.setEnabled(false);
			report3Button.setEnabled(false);

			status.setText("Report #"+reportNumber+" is being created");
			reports.runReport(reportNumber);
		}
		catch (SQLException e)
		{
			System.out.println("There was an error while running Report #"+reportNumber+".  Msg: "+e.getMessage());
			JOptionPane.showMessageDialog(this, "An error occurred while running this report", "Error", JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			status.setText(previousStatus);
			employeeButton.setEnabled(true);
			productButton.setEnabled(true);
			report1Button.setEnabled(true);
			report2Button.setEnabled(true);
			report3Button.setEnabled(true);
		}
	}
	
	public void updateDisplay()
	{
		int mode = 0;
		if(employees != null)
			mode = LIST_EMPLOYEES;
		else if(products != null)
			mode = LIST_PRODUCTS;
		
		display(mode);
	}

	private void display(int mode)
	{
		try
		{
			status.setText("Loading....");
			switch(mode)
			{
				case 10:
					products = null;
					employees = new EmployeeDAO().generateEmployeeList(sort, listFilter.getText());
					
					listChoices.removeAll();
					listChoices.add("Hire an Employee", -1);
					for(Employee employee : employees)
					{
						listChoices.add(employee.toString());
					}
					status.setText("Select an Employee....");
					break;
				case 11:
					employees = null;
					products = new ProductDAO().generateProductList(sort, listFilter.getText());
					
					listChoices.removeAll();
					listChoices.add("Add a new Product", -1);
					for(Product product : products)
					{
						listChoices.add(product.toString());
					}
					status.setText("Select a Product....");
					break;
				default:
					products = null;
					employees = null;
					listChoices.removeAll();
					status.setText(DEFAULT_STATUS);
					JOptionPane.showMessageDialog(this, "Please choose an action on the left", "Notice",JOptionPane.INFORMATION_MESSAGE);
			}
		}
		catch(SQLException e)
		{
			status.setText("An Error Occurred");
			System.out.println("An error occurred while loading the selected list.  Msg: "+e.getMessage());
		}
	}
}
