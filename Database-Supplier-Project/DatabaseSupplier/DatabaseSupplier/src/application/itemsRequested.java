package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class itemsRequested {
	private String supplierID;
	private String orderID; 
	private String itemName;
	private String itemID;
	private String copiesNumber;
	@FXML
	Button Delete;
	public itemsRequested(String supplierID, String orderID, String itemName, String itemID,String copiesNumber,Button Delete) {
		super();
		this.supplierID = supplierID;
		this.orderID = orderID;
		this.itemName = itemName;
		this.itemID = itemID;
		this.copiesNumber=copiesNumber;
		this.Delete=Delete;
	}
	public Button getDelete() {
		return Delete;
	}
	public void setDelete(Button delete) {
		Delete = delete;
	}
	public String getCopiesNumber() {
		return copiesNumber;
	}
	public void setCopiesNumber(String copiesNumber) {
		this.copiesNumber = copiesNumber;
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

}
