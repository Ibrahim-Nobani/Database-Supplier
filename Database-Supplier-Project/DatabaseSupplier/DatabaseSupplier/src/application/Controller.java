package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class Controller implements Initializable {
	ArrayList<Integer> supplierIDList = new ArrayList<>(); // array list that contains all suppliers ID.
	ObservableList<supplier> supplierList = FXCollections.observableArrayList(); //obs list to add database orders in.
	@FXML
	Pane pane;
	@FXML
	Button button;
	@FXML
	Button showSuppliers;
	@FXML
	Button showBuyouts;
	@FXML
	Button showSupplied;
	@FXML
	Button Delete;
	@FXML
	Button RefreshTable;
	@FXML
	TableView<supplier> myTable;
	@FXML
	TableColumn<supplier, String> snameColumn;
	@FXML
	TableColumn<supplier, String> idColumn;
	@FXML
	TableColumn<supplier, String> addressColumn;
	@FXML
	TableColumn<supplier, String> phoneColumn;
	@FXML
	TableColumn<supplier, Button> deleteColumn;
	@FXML
	TextField snameADD;
	@FXML
	TextField idADD;
	@FXML
	TextField addressADD;
	@FXML
	TextField phoneADD;
	@FXML
	Button AddSupplier;
	@FXML
	TextField search;
	@FXML
	ImageView image2;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Delete = new Button("Delete");
		// snameColumn = new TableColumn<>("Supplier Name");
		if (image2 != null)
		image2.setImage(new Image(getClass().getResource("mind.jpg").toString()));
		
		if (myTable != null) { // if the table is not null, upload its components.
			supplierTable();
		}
	}

	public void buttonConnection(ActionEvent event) throws IOException { // button that goes to the edit supplier UI.
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("EditSupplier.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		Stage stage = new Stage();
		stage.setTitle("Add Supplier");
		stage.setScene(scene);
		stage.show();
	}

	public void showBuyouts(ActionEvent event) throws IOException { // show orders button
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("ShowBuyouts.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		Stage stage = new Stage();
		stage.setTitle("Buyouts Record");
		stage.setScene(scene);
		stage.show();
	}

	public void showSupplied(ActionEvent event) throws IOException { // show order supplied buttons
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("ordersSupplied.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		Stage stage = new Stage();
		stage.setTitle("Buyouts Supplied Items Record");
		stage.setScene(scene);
		stage.show();
	}

	public void showSuppliersBUTTON(ActionEvent event) throws IOException { // show menu interface.
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("ibra2.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		Stage stage = new Stage();
		stage.setTitle("Suppliers");
		stage.setScene(scene);
		stage.show();
	}

	public void requestBuyBUTTON(ActionEvent event) throws IOException { // show request order
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("requestBuyout.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		Stage stage = new Stage();
		stage.setTitle("Buyout Request");
		stage.setScene(scene);
		stage.show();
	}
	public void addSupplied(ActionEvent event) throws IOException { // show add supplied order.
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("addSuppliedOrder.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		Stage stage = new Stage();
		stage.setTitle("Buyout Request");
		stage.setScene(scene);
		stage.show();
	}

	public void GenerateSqlTable() {
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "select * from supplier";
		try {
			con = suppDB.connectDB();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while (rs.next()) {
				if (!supplierIDList.contains(Integer.decode(rs.getString(2)))) {
					Delete = new Button("Delete");
					Delete.setId((rs.getString(2))); // giving the delete button an id that is the same as the supplier id.
					Delete.setOnAction((event) -> { //making buttons as a click then generating its id.
						String id = (((Button) event.getSource()).getId());
						// String id = ((Button) event.getSource()).getText();
						DeleteSupplier(id);
					});
					supplierList.add(new supplier(rs.getString(1), (rs.getString(2)), rs.getString(3),
							(rs.getString(4)), Delete)); // adding to the obs lists
					supplierIDList.add(Integer.decode(rs.getString(2))); // adding keys to a list so we can control them.
				}
			}
			rs.close();
			stmt.close();
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("Sql Error or database failed to connect", "ERROR", "Failed");
		}

	}

	public void RefreshTable(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		supplierTable();
	}

	public void supplierTable() {
		// snameColumn = new TableColumn<>("Supplier Name");
		snameColumn.setCellValueFactory(new PropertyValueFactory<>("sname"));
		// idColumn = new TableColumn<>("supplierID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
		// addressColumn = new TableColumn<>("Address");
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
		// phoneColumn = new TableColumn<>("phone");
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
		// deleteColumn = new TableColumn<>("");
		deleteColumn.setCellValueFactory(new PropertyValueFactory<>("Delete"));
		// myTable.getColumns().addAll(snameColumn, idColumn, addressColumn,
		// phoneColumn,deleteColumn);
		myTable.refresh();
		GenerateSqlTable();
		myTable.setItems(supplierList);
		// set the table and columns editable
		myTable.setEditable(true);
		snameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		addressColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		idColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		// IntegerStringConverter()));
		search(); // enabling the search
	}

	public void addSupplierBUTTON(ActionEvent event) {
		Connection con;
		DBConn suppDB = new DBConn();
		// String query = "select * from supplier";
		try {
			con = suppDB.connectDB();
			String sqlQuery = "insert into supplier values (?,?,?,?)";
			PreparedStatement addStatment = con.prepareStatement(sqlQuery);
			addStatment.setString(1, snameADD.getText());
			addStatment.setString(2, idADD.getText());
			addStatment.setString(3, addressADD.getText());
			addStatment.setString(4, phoneADD.getText());
			addStatment.execute();
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("One Of the inputs you entered is not allowed Or the ID already exists.", "ERROR",
					"Failed");
		}

	}

	public void DeleteSupplier(String id) { // when delete is clicked, takes the id of the button.
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Confirmation!");
		alert.setHeaderText("Are you certain you want to delete supplier with id?");
		alert.setContentText("Press OK to delete and Cancel to cancel");
		Optional<ButtonType> option = alert.showAndWait();
		// ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("YES");
		if (option.get() == null || option.get() == ButtonType.CANCEL) {
			// close the program
		} else if (option.get() == ButtonType.OK) {
			// System.out.println("aaa");
			Connection con;
			DBConn suppDB = new DBConn();
			// id = id.replace("Delete", "");
			// System.out.println(Integer.decode(id));
			String sqlQuery = "delete from supplier where supplierID=" + Integer.decode(id);
			try {
				con = suppDB.connectDB();
				PreparedStatement statement = con.prepareStatement(sqlQuery);
				statement.executeUpdate();
				// System.out.println(id);
				supplierList.remove(supplierIDList.indexOf(Integer.decode(id)));
				supplierIDList.remove(Integer.decode(id));
			} catch (ClassNotFoundException | SQLException e) {
				userController.infoBox("Failed To delete", "ERROR", "Failed");
			}
		}
	}

	public void updateSname(TableColumn.CellEditEvent<supplier, String> Event) {
		supplier supp = myTable.getSelectionModel().getSelectedItem();
		supp.setSname(Event.getNewValue());
		String updatedSname = "'" + supp.getSname() + "'";
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update supplier set sname= " + updatedSname + " where supplierID=" + supp.getSupplierID();
		try {
			con = suppDB.connectDB();
			PreparedStatement statement = con.prepareStatement(sqlQuery);
			statement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("Update Failed, The value you entered is not allowed.", "ERROR", "Failed");
		}
	}

	public void updateID(TableColumn.CellEditEvent<supplier, String> Event) {
		supplier supp = myTable.getSelectionModel().getSelectedItem();
		String oldID = supp.getSupplierID();
		String IdTemp = (Event.getNewValue());
		supp.setSupplierID(Event.getNewValue());
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update supplier set supplierID= " + supp.getSupplierID() + " where supplierID=" + oldID;
		try {
			con = suppDB.connectDB();
			PreparedStatement statement = con.prepareStatement(sqlQuery);
			statement.executeUpdate();
			supplierIDList.set(supplierIDList.indexOf(Integer.decode(oldID)), Integer.decode(supp.getSupplierID()));
			// supplierIDList.remove(Integer.decode(newID));
			// supplierIDList.add(Integer.decode(supp.getSupplierID()));
			supp.getDelete().setId(supp.getSupplierID());
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("The ID you entered already exists or is not allowed.", "ERROR", "Failed");
		}
	}

	

	public void updateAddress(TableColumn.CellEditEvent<supplier, String> Event) {
		supplier supp = myTable.getSelectionModel().getSelectedItem();
		supp.setAddress(Event.getNewValue());
		String updatedAddress = "'" + supp.getAddress() + "'";
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update supplier set address= " + updatedAddress + " where supplierID="
				+ supp.getSupplierID();
		try {
			con = suppDB.connectDB();
			PreparedStatement statement = con.prepareStatement(sqlQuery);
			statement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("The input you entered is not allowed.", "ERROR", "Failed");
		}
	}

	public void updatePhone(TableColumn.CellEditEvent<supplier, String> Event) {
		supplier supp = myTable.getSelectionModel().getSelectedItem();
		supp.setPhone(Event.getNewValue());
		// int updatedPhone = Integer.decode(supp.getPhone());
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update supplier set phone= " + supp.getPhone() + " where supplierID=" + supp.getSupplierID();
		try {
			con = suppDB.connectDB();
			PreparedStatement statement = con.prepareStatement(sqlQuery);
			statement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("The phone number you entered is not allowed.", "ERROR", "Failed");
		}
	}

	public void search() {
		FilteredList<supplier> filteredData = new FilteredList<>(supplierList, b -> true);
		search.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(searchedSupplier -> {
				if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
					return true;
				}
				String searchedFor = newValue.toLowerCase();
				if (searchedSupplier.getSname().toLowerCase().contains(searchedFor)) {
					return true;
				} else if (searchedSupplier.getAddress().toLowerCase().contains(searchedFor)) {
					return true;
				} else if (searchedSupplier.getSupplierID().toLowerCase().contains(searchedFor)) {
					return true;
				} else if (searchedSupplier.getPhone().toLowerCase().contains(searchedFor)) {
					return true;
				}
				return false; // Does not match.
			});
		});
		SortedList<supplier> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(myTable.comparatorProperty());
		myTable.setItems(sortedData);

	}

}
