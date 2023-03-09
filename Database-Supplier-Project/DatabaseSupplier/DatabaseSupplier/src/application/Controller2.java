package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

public class Controller2 implements Initializable {
	ArrayList<Integer> supplyOrderIDList = new ArrayList<>(); // array list that contains all supplyOrders ID.
	ArrayList<Integer> ItemIDList = new ArrayList<>(); // list that contains each item id
	ObservableList<supplyOrder> obList = FXCollections.observableArrayList(); // list that has all supply orders
	ObservableList<itemsRequested> obList2 = FXCollections.observableArrayList(); // list with all items.
	static int ordId;
	static int ordId2;
	@FXML
	Button button;
	@FXML
	Button showDetails;
	@FXML
	Button viewBill;
	@FXML
	TableView<supplyOrder> myTable;
	@FXML
	TableColumn<supplyOrder, String> idColumn;
	@FXML
	TableColumn<supplyOrder, String> orderIdColumn;
	@FXML
	TableColumn<supplyOrder, String> dateColumn;
	@FXML
	TableColumn<supplyOrder, Button> detailsColumn;
	@FXML
	TableColumn<supplyOrder, Button> deleteColumn;
	@FXML
	TableView<itemsRequested> myItems;
	@FXML
	TableColumn<itemsRequested, String> SupIdColumn;
	@FXML
	TableColumn<itemsRequested, String> ordIdColumn;
	@FXML
	TableColumn<itemsRequested, String> itemNameColumn;
	@FXML
	TableColumn<itemsRequested, String> itemIDColumn;
	@FXML
	TableColumn<itemsRequested, String> copiesNumColumn;
	@FXML
	TableColumn<itemsRequested, Button> deleteRequestItemColumn;
	@FXML
	ComboBox supplierIdADD;
	@FXML
	TextField orderIdADD;
	@FXML
	DatePicker requestDateADD;
	@FXML
	TextField itemNameADD;
	@FXML
	TextField itemIdADD;
	@FXML
	TextField copiesNumADD;
	@FXML
	Button refreshItems;
	@FXML
	TextField search;
	@FXML
	Button delete;
	@FXML
	Button Delete;
	@FXML
	CheckBox addToS;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		if (supplierIdADD != null)
			fillComboBox();

		if (myTable != null) {
			supplyOrderTable();
		}
		if (myItems != null) {
			itemsTable();
		}

	}

	public void GenerateSqlTable() throws ClassNotFoundException, SQLException {
		Connection con;
		DBConn suppDB = new DBConn();
		con = suppDB.connectDB();
		String sqlQuery = "select * from supplyOrder";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while (rs.next()) {
				if (!supplyOrderIDList.contains(Integer.decode(rs.getString(2)))) {
					showDetails = new Button("View Details");
					showDetails.setId((rs.getString(2))); // show details button that displays all items requested within the order.
					showDetails.setOnAction((event) -> {
						String Id = (((Button) event.getSource()).getId());
						ordId = Integer.decode(Id);
						//System.out.println(Integer.decode(((Button) event.getSource()).getId()));
						FXMLLoader fxmlLoader = new FXMLLoader();
						fxmlLoader.setLocation(getClass().getResource("itemsView.fxml"));
						// Scene scene;
						try {
							Scene scene = new Scene(fxmlLoader.load());
							Stage stage = new Stage();
							stage.setTitle("Items View");
							stage.setScene(scene);
							stage.show();
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
					delete = new Button("Delete");
					delete.setId((rs.getString(2)));
					delete.setOnAction((event) -> {
						String id = (((Button) event.getSource()).getId());
						// String id = ((Button) event.getSource()).getText();
						DeleteSupplyOrder(id);
					});
					obList.add(
							new supplyOrder(rs.getString(1), (rs.getString(2)), rs.getString(3), showDetails, delete));
					supplyOrderIDList.add(Integer.decode(rs.getString(2)));
				}
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void RefreshTable(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		supplyOrderTable();
	}

	public void supplyOrderTable() { // function that sets the columns and fills the table from the observable list.
		// idColumn = new TableColumn<>("supplierID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
		// orderIdColumn = new TableColumn<>("orderID");
		orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
		// dateColumn = new TableColumn<>("requestDate");
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
		// detailsColumn = new TableColumn<>("showDetails");
		detailsColumn.setCellValueFactory(new PropertyValueFactory<>("showDetails"));
		deleteColumn.setCellValueFactory(new PropertyValueFactory<>("delete"));
		// myTable.getColumns().addAll(idColumn, orderIdColumn, dateColumn,
		// detailsColumn);

		try {
			GenerateSqlTable();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myTable.setItems(obList);
		search();
		myTable.setEditable(true);
		idColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		orderIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		dateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	}

	public void showDetailsSupplyOrder() throws IOException, ClassNotFoundException, SQLException {
		Connection con;
		DBConn suppDB = new DBConn();
		con = suppDB.connectDB();
		// System.out.println("in"+ordId);
		//System.out.println("s" + ordId);
		String sqlQuery = "select * from itemsRequested where orderID = " + ordId;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while (rs.next()) {
				//System.out.println(rs.getString(4)+" "+rs.getString(2));
				Button Delete = new Button("Delete");
				Delete.setId(rs.getString(4));
				Delete.setOnAction((event) -> {
					String id = (((Button) event.getSource()).getId());
					// String id = ((Button) event.getSource()).getText();
					DeleteRequestedItem(id);
				});
				if (!ItemIDList.contains(Integer.decode(rs.getString(4)))) {
					obList2.add(new itemsRequested(rs.getString(1), (rs.getString(2)), rs.getString(3), rs.getString(4),
							rs.getString(5), Delete));
					ItemIDList.add(Integer.decode(rs.getString(4)));
				}
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void RefreshItems2(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		itemsTable();
	}

	public void itemsTable() { // function that sets the columns and fills the table from the observable list.
		// idColumn = new TableColumn<>("supplierID");
		SupIdColumn.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
		// orderIdColumn = new TableColumn<>("orderID");
		ordIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
		// dateColumn = new TableColumn<>("requestDate");
		itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		// detailsColumn = new TableColumn<>("showDetails");
		itemIDColumn.setCellValueFactory(new PropertyValueFactory<>("itemID"));
		// myTable.getColumns().addAll(idColumn, orderIdColumn, dateColumn,
		// detailsColumn);
		copiesNumColumn.setCellValueFactory(new PropertyValueFactory<>("copiesNumber"));
		deleteRequestItemColumn.setCellValueFactory(new PropertyValueFactory<>("Delete"));

		try {
			showDetailsSupplyOrder();
		} catch (IOException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myItems.setItems(obList2);
		myItems.setEditable(true);
		itemNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		itemIDColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		copiesNumColumn.setCellFactory(TextFieldTableCell.forTableColumn());

	}

	public void search() { // search function, looks for the wanted string in all the observable list created from a certain class.
		FilteredList<supplyOrder> filteredData = new FilteredList<>(obList, b -> true);
		search.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(searchedSupplier -> {
				if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
					return true;
				}
				String searchedFor = newValue.toLowerCase();
				if (searchedSupplier.getSupplierID()!=null && searchedSupplier.getSupplierID().toLowerCase().contains(searchedFor)) {
					return true;
				} else if (searchedSupplier.getOrderID().toLowerCase().contains(searchedFor)) {
					return true;
				} else if (searchedSupplier.getRequestDate().toLowerCase().contains(searchedFor)) {
					return true;
				}

				return false; // Does not match.
			});
		});
		SortedList<supplyOrder> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(myTable.comparatorProperty());
		myTable.setItems(sortedData);

	}

	public void addRequestBUTTON(ActionEvent event) { // Add order button Function,adds to database the written values when clicked. 
		// fillComboBox();
		Connection con;
		DBConn suppDB = new DBConn();
		// String query = "select * from supplier";
		boolean isSelected = addToS.isSelected();
		try {
			con = suppDB.connectDB();
			String sqlQuery = "insert into supplyOrder values (?,?,?)";
			PreparedStatement addStatment = con.prepareStatement(sqlQuery);
			addStatment.setString(1, (String) supplierIdADD.getValue());
			addStatment.setString(2, orderIdADD.getText());
			addStatment.setString(3, (requestDateADD.getValue().toString()));
			addStatment.execute();
			if (isSelected) { // if the box is checked, add to the supplied table too with a default date and price.
			String sqlQuery2 = "insert into orderSupplied values (?,?,?,?)";
			PreparedStatement addStatment2 = con.prepareStatement(sqlQuery2);
			addStatment2.setString(1, (String) supplierIdADD.getValue());
			addStatment2.setString(2, orderIdADD.getText());
			addStatment2.setString(3, (requestDateADD.getValue().toString()));
			addStatment2.setString(4, "2022-9-20");
			addStatment2.execute();
			}
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("Not allowed.", "ERROR", "Failed");
			e.printStackTrace();
		}

	}

	public void addItemBUTTON(ActionEvent event) { // Add order button Function,adds to database the written values when clicked. 
		// fillComboBox();
		Connection con;
		DBConn suppDB = new DBConn();
		// String query = "select * from supplier";
		boolean isSelected = addToS.isSelected();
		try {
			con = suppDB.connectDB();
			String sqlQuery = "insert into itemsRequested values (?,?,?,?,?)";
			PreparedStatement addStatment = con.prepareStatement(sqlQuery);
			addStatment.setString(1, (String) supplierIdADD.getValue());
			addStatment.setString(2, orderIdADD.getText());
			addStatment.setString(3, itemNameADD.getText());
			addStatment.setString(4, itemIdADD.getText());
			addStatment.setString(5, copiesNumADD.getText());
			addStatment.execute();
			if (isSelected) { // if the box is checked, add to the supplied table too with a default date and price.
			String sqlQuery2 = "insert into itemsSupplied values (?,?,?,?,?,?)";
			PreparedStatement addStatment2 = con.prepareStatement(sqlQuery2);
			addStatment2.setString(1, (String) supplierIdADD.getValue());
			addStatment2.setString(2, orderIdADD.getText());
			addStatment2.setString(3, itemNameADD.getText());
			addStatment2.setString(4, itemIdADD.getText());
			addStatment2.setString(5, copiesNumADD.getText());
			int price = Integer.decode(copiesNumADD.getText()) * 5;
			addStatment2.setString(6, Integer.toString(price));
			addStatment2.execute();
			}
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("One of the values is not allowed.", "ERROR", "Failed");
			e.printStackTrace();
		}

	}

	public void fillComboBox() { // a comboBox to choose the supplier from.
		Connection con;
		try {
			DBConn suppDB = new DBConn();
			con = suppDB.connectDB();
			String sqlQuery = "select * from supplier";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while (rs.next()) {
				supplierIdADD.getItems().add(rs.getString(2));
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void DeleteSupplyOrder(String id) { // delete an order functions, takes the id.
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Confirmation!");
		alert.setHeaderText("Are you certain you want to delete this supply order?");
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
			String sqlQuery = "delete from supplyOrder where orderID=" + Integer.decode(id);
			try {
				con = suppDB.connectDB();
				PreparedStatement statement = con.prepareStatement(sqlQuery);
				statement.executeUpdate();
				obList.remove(supplyOrderIDList.indexOf(Integer.decode(id)));
				supplyOrderIDList.remove(Integer.decode(id));
			} catch (ClassNotFoundException | SQLException e) {
				userController.infoBox("NOT Allowed", "ERROR", "Failed");
				//e.printStackTrace();
			}
		}
	}

	public void DeleteRequestedItem(String id) { // delete item function button.
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
			String sqlQuery = "delete from itemsRequested where itemID=" + Integer.decode(id)+" and orderID="+ ordId;
			System.out.println(ordId +"--"+ Integer.decode(id));
			try {
				con = suppDB.connectDB();
				PreparedStatement statement = con.prepareStatement(sqlQuery);
				statement.executeUpdate();
				obList2.remove(ItemIDList.indexOf(Integer.decode(id)));
				ItemIDList.remove(Integer.decode(id));
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void updateSupplierID(TableColumn.CellEditEvent<supplyOrder, String> Event) { 
		supplyOrder supply = myTable.getSelectionModel().getSelectedItem();
		supply.setSupplierID(Event.getNewValue());
		// String updatedSname = "'" + supp.getSname() + "'";
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update supplyOrder set supplierID= " + supply.getSupplierID() + " where orderID="
				+ supply.getOrderID();
		String sqlQuery2 = "update itemsRequested set supplierID= " + supply.getSupplierID() + " where orderID="
				+ supply.getOrderID();
		try {
			con = suppDB.connectDB();
			PreparedStatement statement = con.prepareStatement(sqlQuery);
			statement.executeUpdate();
			PreparedStatement statement2 = con.prepareStatement(sqlQuery2);
			statement2.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("Update Failed, The value you entered is not allowed.", "ERROR", "Failed");
		}
	}

	public void updateID(TableColumn.CellEditEvent<supplyOrder, String> Event) {
		supplyOrder supply = myTable.getSelectionModel().getSelectedItem();
		String oldID = supply.getOrderID();
		String IdTemp = (Event.getNewValue());
		supply.setOrderID(Event.getNewValue());
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update supplyOrder set orderID= " + supply.getOrderID() + " where orderID=" + oldID;
		try {
			con = suppDB.connectDB();
			PreparedStatement statement = con.prepareStatement(sqlQuery);
			statement.executeUpdate();
			supplyOrderIDList.set(supplyOrderIDList.indexOf(Integer.decode(oldID)),
					Integer.decode(supply.getOrderID()));
			// supplyOrderIDList.remove(Integer.decode(newID));
			// supplyOrderIDList.add(Integer.decode(supply.getOrderID()));
			supply.getDelete().setId(supply.getOrderID());
			supply.getShowDetails().setId(supply.getOrderID());
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("The ID you entered already exists or is not allowed.", "ERROR", "Failed");
		}

	}

	public void updateDate(TableColumn.CellEditEvent<supplyOrder, String> Event) {
		supplyOrder supply = myTable.getSelectionModel().getSelectedItem();
		supply.setRequestDate(Event.getNewValue());
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update supplyOrder set requestDate= '" + supply.getRequestDate() + "' where orderID="
				+ supply.getOrderID();
		try {
			con = suppDB.connectDB();
			PreparedStatement statement = con.prepareStatement(sqlQuery);
			statement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("The Date you entered is not allowed.", "ERROR", "Failed");
		}

	}

	public void updateItemName(TableColumn.CellEditEvent<itemsRequested, String> Event) {
		itemsRequested supply = myItems.getSelectionModel().getSelectedItem();
		supply.setItemName(Event.getNewValue());
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update itemsRequested set itemName= '" + supply.getItemName() + "' where itemID="
				+ supply.getItemID() + " and orderID=" + supply.getOrderID();
		try {
			con = suppDB.connectDB();
			PreparedStatement statement = con.prepareStatement(sqlQuery);
			statement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("The Name you entered is not allowed.", "ERROR", "Failed");
			e.printStackTrace();
		}

	}

	public void updateItemID(TableColumn.CellEditEvent<itemsRequested, String> Event) {
		itemsRequested supply = myItems.getSelectionModel().getSelectedItem();
		String oldID = supply.getItemID();
		supply.setItemID(Event.getNewValue());
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update itemsRequested set itemID= " + supply.getItemID() + " where itemID=" + oldID
				+ " and orderID=" + supply.getOrderID();
		try {
			con = suppDB.connectDB();
			PreparedStatement statement = con.prepareStatement(sqlQuery);
			statement.executeUpdate();
			ItemIDList.set(ItemIDList.indexOf(Integer.decode(oldID)),
					Integer.decode(supply.getItemID()));
			supply.getDelete().setId(supply.getItemID());
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("The Name you entered is not allowed.", "ERROR", "Failed");
		}

	}
	public void updateCopies(TableColumn.CellEditEvent<itemsRequested, String> Event) {
		itemsRequested supply = myItems.getSelectionModel().getSelectedItem();
		supply.setCopiesNumber(Event.getNewValue());
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update itemsRequested set copiesNumber= " + supply.getCopiesNumber() + " where itemID=" + supply.getItemID()
				+ " and orderID=" + supply.getOrderID();
		try {
			con = suppDB.connectDB();
			PreparedStatement statement = con.prepareStatement(sqlQuery);
			statement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("The Name you entered is not allowed.", "ERROR", "Failed");
		}

	}

}
