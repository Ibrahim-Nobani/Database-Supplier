package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class orderSupplied {
	private String supplierID; 
	private String orderID;
	private String requestDate;
	private String suppliedDate;
	@FXML
	Button viewBill;
	@FXML
	Button Delete;
	
	public Button getDelete() {
		return Delete;
	}

	public void setDelete(Button delete) {
		Delete = delete;
	}

	public orderSupplied(String supplierID, String orderID, String requestDate, String suppliedDate, Button viewBill,Button Delete) {
		super();
		this.supplierID = supplierID;
		this.orderID = orderID;
		this.requestDate = requestDate;
		this.suppliedDate = suppliedDate;
		this.viewBill = viewBill;
		this.Delete=Delete;
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

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public String getSuppliedDate() {
		return suppliedDate;
	}

	public void setSuppliedDate(String suppliedDate) {
		this.suppliedDate = suppliedDate;
	}

	public Button getViewBill() {
		return viewBill;
	}

	public void setViewBill(Button viewBill) {
		this.viewBill = viewBill;
	}

}
