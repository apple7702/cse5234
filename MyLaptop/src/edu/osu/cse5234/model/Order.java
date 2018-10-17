package edu.osu.cse5234.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
	private int id;
	private String customerName;
	private String emailAddress;
	private List<LineItem> lineItems;



	public Order() { }

	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getCustomerName() {
		return customerName;
	}


	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


	public List<LineItem> getLineItems() {
		return lineItems;
	}


	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}
	
	public List<Item> getItems(){
		List<Item> res=new ArrayList<>();
		for(LineItem lItem:this.lineItems) {
			if(lItem.getQuantity()>0) res.add(lItem.getItem());
		}
		
		return res;
		
	}


	
}
