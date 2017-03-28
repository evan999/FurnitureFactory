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
import javax.swing.JOptionPane;

public class ProductGui extends Frame implements ActionListener
{

	private static final long serialVersionUID = 1L;
	private ProductLineDAO pdao;
	private TextField finish, standardPrice;
	private Choice productLine;
	private TextArea description;
	private Button addButton, updateButton, deleteButton;
	private FurnitureFactoryGui factory;
	private Product product;
	private java.util.List<ProductLine> products;

	public ProductGui(FurnitureFactoryGui furnitureFactoryGui)
	{
		super("Product Management - Online Furniture Factory"); // set frame title
		
		factory = furnitureFactoryGui;
		pdao = new ProductLineDAO();
		try
		{
			products = pdao.generateProductLineList();
		}
		catch (SQLException e)
		{
			// Skip It
		}
		
		setLayout(new GridLayout(5, 2)); // set layout

		Label label = new Label("Product Line", Label.LEFT);
		add(label);
		productLine = Utils.listToChoice(products, "Select Product Line");
		add(productLine);
		label = new Label("Description", Label.LEFT);
		add(label);
		description = new TextArea();
		add(description);
		label = new Label("Finish", Label.LEFT);
		add(label);
		finish = new TextField();
		add(finish);
		label = new Label("Standard Price", Label.LEFT);
		add(label);
		standardPrice = new TextField();
		add(standardPrice);
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
	
	public boolean show(Product product)
	{
		if(product == null)
		{
			JOptionPane.showMessageDialog(this, "No Product Was Specified", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
		}
		else
		{
			this.product = product;

			if(product.isNew())
				productLine.select(0);
			else
				productLine.select(product.getProductLine().toString());
			description.setText((product.isNew()?"":product.getProductDescription()));
			finish.setText((product.isNew()?"":product.getProductFinish()));
			standardPrice.setText((product.isNew()?"":product.getProductStandardPriceFormatted()));
		
			if(product.isNew())
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
		boolean originallyNew = product.isNew();
		try
		{
			String tmp = productLine.getSelectedItem();
			Long id = Long.valueOf(tmp.substring(tmp.indexOf("(")+1, tmp.indexOf(")")));
			product.setProductLine(pdao.viewProductLine(BigDecimal.valueOf(id)));
			product.setProductDescription(description.getText());
			product.setProductFinish(finish.getText());
			product.setProductStandardPrice(Float.valueOf(standardPrice.getText()));

			ProductDAO dao = new ProductDAO();
			dao.saveProduct(product);
			closeWindow();
		}
		catch (SQLException e)
		{
			if(originallyNew)
			{
				// Reset back to new
				product.setProductId(null);
			}

			System.out.println("An error occurred while saving.  Msg: "+e.getMessage());
			JOptionPane.showMessageDialog(this, "An error occurred while processing your request", "Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (NumberFormatException e)
		{
			System.out.println("An error occurred while saving.  Msg: "+e.getMessage());
			JOptionPane.showMessageDialog(this, "Invalid ID/Price", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void doDelete()
	{
		try
		{
			ProductDAO dao = new ProductDAO();
			dao.deleteProduct(product.getProductId());
			closeWindow();
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(this, "An error occurred while processing your request", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
