package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class supplyOrder {
	private String supplierID; 
	private String orderID;
	private String requestDate;
	@FXML
	Button showDetails;
	@FXML
	Button delete;
	public supplyOrder(String supplierID, String orderID, String requestDate, Button showDetails,Button delete) {
		this.supplierID = supplierID;
		this.orderID = orderID;
		this.requestDate = requestDate;
		this.showDetails = showDetails;
		this.delete=delete;
	}
	public Button getDelete() {
		return delete;
	}
	public void setDelete(Button delete) {
		this.delete = delete;
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
	public Button getShowDetails() {
		return showDetails;
	}
	public void setShowDetails(Button showDetails) {
		this.showDetails = showDetails;
	}
	

}
