package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class itemsSupplied {
	private String supplierID;
	private String orderID; 
	private String itemName;
	private String itemID;
	private String copiesNumber;
	private String price;
	@FXML
	Button Delete;
	
	public itemsSupplied(String supplierID, String orderID, String itemName, String itemID, String copiesNumber,
			String price,Button Delete) {
		super();
		this.supplierID = supplierID;
		this.orderID = orderID;
		this.itemName = itemName;
		this.itemID = itemID;
		this.copiesNumber = copiesNumber;
		this.price = price;
		this.Delete=Delete;
	}
	public Button getDelete() {
		return Delete;
	}
	public void setDelete(Button delete) {
		Delete = delete;
	}
	public String getSupplierID() {
		return supplierID;
	}
	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemID() {
		return itemID;
	}
	public void setItemID(String itemID) {
		this.itemID = itemID;
	}
	public String getCopiesNumber() {
		return copiesNumber;
	}
	public void setCopiesNumber(String copiesNumber) {
		this.copiesNumber = copiesNumber;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	

}
