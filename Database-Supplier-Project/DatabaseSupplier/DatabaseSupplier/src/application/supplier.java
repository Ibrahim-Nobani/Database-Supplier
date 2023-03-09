package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class supplier {
	private String sname;
	private String supplierID ;
	private String address;
	private String phone;
	@FXML
	Button Delete;
	
	 public Button getDelete() {
		return Delete;
	}


	public void setDelete(Button delete) {
		Delete = delete;
	}


	supplier (String sname,String supplierID,String address,String phone,Button Delete) {
		this.sname=sname;
		this.supplierID=supplierID;
		this.address=address;
		this.phone=phone;
		this.Delete=Delete;
	}

	
	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
