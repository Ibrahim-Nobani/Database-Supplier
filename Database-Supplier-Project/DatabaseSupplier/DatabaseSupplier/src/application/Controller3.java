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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

public class Controller3 implements Initializable {
	ArrayList<Integer> suppliedItemIDList = new ArrayList<>();
	ArrayList<Integer> suppliedOrderIDList = new ArrayList<>();
	ObservableList<orderSupplied> suppliedList = FXCollections.observableArrayList();
	ObservableList<itemsSupplied> suppliedItemsList = FXCollections.observableArrayList();
	static int ordId;
	static int ordId2;
	@FXML
	Button button;
	@FXML
	Button showDetails;
	@FXML
	Button viewBill;
	@FXML
	TableView<orderSupplied> SuppliedTable;
	@FXML
	TableColumn<orderSupplied, String> suppliedIDColumn;
	@FXML
	TableColumn<orderSupplied, String> suppliedOrderIdColumn;
	@FXML
	TableColumn<orderSupplied, String> requestDateColumn;
	@FXML
	TableColumn<orderSupplied, String> suppliedDateColumn;
	@FXML
	TableColumn<orderSupplied, Button> billColumn;
	@FXML
	TableColumn<orderSupplied, Button> suppliedDeleteColumn;

	@FXML
	TableView<itemsSupplied> SuppliedItemsTable;
	@FXML
	TableColumn<itemsSupplied, String> suppliedItemsIDColumn;
	@FXML
	TableColumn<itemsSupplied, String> suppliedItemsOrderIdColumn;
	@FXML
	TableColumn<itemsSupplied, String> suppliedItemNameColumn;
	@FXML
	TableColumn<itemsSupplied, String> suppliedItemIDColumn;
	@FXML
	TableColumn<itemsSupplied, String> suppliedCopiesNumColumn;
	@FXML
	TableColumn<itemsSupplied, String> priceColumn;
	@FXML
	TableColumn<itemsSupplied, Button> deleteSuppliedItemColumn;
	@FXML
	Button refreshItems;
	@FXML
	TextField search;
	@FXML
	Button delete;
	@FXML
	Button Delete;
	@FXML
	ComboBox supplierIdADD;
	@FXML
	TextField orderIdADD;
	@FXML
	DatePicker requestDateADD;
	@FXML
	DatePicker suppliedDateADD;
	@FXML
	TextField itemNameADD;
	@FXML
	TextField itemIdADD;
	@FXML
	TextField copiesNumADD;
	@FXML
	TextField priceADD;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		if (supplierIdADD != null)
			fillComboBox();
		
		if (SuppliedTable != null) {
			suppliedTable();
		}
		if (SuppliedItemsTable != null) {
			sItemsTable();
		}

	}

	public void GenerateSuppliedSqlTable() throws ClassNotFoundException, SQLException {
		Connection con;
		DBConn suppDB = new DBConn();
		con = suppDB.connectDB();
		String sqlQuery = "select * from orderSupplied";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while (rs.next()) {
				if (!suppliedOrderIDList.contains(Integer.decode(rs.getString(2)))) {
					//System.out.println(rs.getString(4));
					viewBill = new Button("View Bill");
					viewBill.setId((rs.getString(2)));
					viewBill.setOnAction((event) -> {
						String Id = (((Button) event.getSource()).getId());
						ordId2 = Integer.decode(Id);
						//System.out.println(Integer.decode(((Button) event.getSource()).getId()));
						FXMLLoader fxmlLoader = new FXMLLoader();
						fxmlLoader.setLocation(getClass().getResource("itemsSupplied.fxml"));
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
					Delete = new Button("Delete");
					Delete.setId((rs.getString(2)));
					Delete.setOnAction((event) -> {
						String id = (((Button) event.getSource()).getId());
						// String id = ((Button) event.getSource()).getText();
						DeleteOrderSupplied(id);
					});
					suppliedList.add(new orderSupplied(rs.getString(1), (rs.getString(2)), rs.getString(3),
							rs.getString(4), viewBill, Delete));
					suppliedOrderIDList.add(Integer.decode(rs.getString(2)));
				}
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void RefreshSuppliedTable(ActionEvent event) throws ClassNotFoundException, IOException, SQLException {
		suppliedTable();
	}

	public void suppliedTable() { // function that sets the columns and fills the table from the observable list.
		// idColumn = new TableColumn<>("supplierID");
		suppliedIDColumn.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
		// orderIdColumn = new TableColumn<>("orderID");
		suppliedOrderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
		// dateColumn = new TableColumn<>("requestDate");
		requestDateColumn.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
		suppliedDateColumn.setCellValueFactory(new PropertyValueFactory<>("suppliedDate"));
		// detailsColumn = new TableColumn<>("showDetails");
		billColumn.setCellValueFactory(new PropertyValueFactory<>("viewBill"));
		suppliedDeleteColumn.setCellValueFactory(new PropertyValueFactory<>("Delete"));
		// myTable.getColumns().addAll(idColumn, orderIdColumn, dateColumn,
		// detailsColumn);

		try {
			GenerateSuppliedSqlTable();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SuppliedTable.setItems(suppliedList);
		search();
		SuppliedTable.setEditable(true);
		suppliedIDColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		suppliedOrderIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		requestDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		suppliedDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	}

	public void showSuppliedBill() throws IOException, ClassNotFoundException, SQLException {
		Random rand = new Random(); // instance of random class
		Connection con;
		DBConn suppDB = new DBConn();
		con = suppDB.connectDB();
		// System.out.println("in"+ordId);
		String price;
		String sqlQuery = "select * from itemsSupplied where orderID = " + ordId2;
		int copiesNum;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while (rs.next()) {
				// copiesNum = rand.nextInt(Integer.decode(rs.getString(5))+1);
				//System.out.println("--"+rs.getString(6));
				price = Integer.toString(Integer.decode(rs.getString(5)) * 5);
				String sqlQuery2 = "update itemsSupplied set price = " + price + " where orderID= " + ordId2 + " and where itemID="+rs.getString(4);
				Button Delete = new Button("Delete");
				Delete.setId(rs.getString(4));
				Delete.setOnAction((event) -> {
					String id = (((Button) event.getSource()).getId());
					// String id = ((Button) event.getSource()).getText();
					DeleteSuppliedItem(id);
				});
				//PreparedStatement addStatment = con.prepareStatement(sqlQuery2);
				//addStatment.executeUpdate();
				//System.out.println("-"+rs.getString(6));
				if (!suppliedItemIDList.contains(Integer.decode(rs.getString(4)))) {
					suppliedItemsList.add(new itemsSupplied(rs.getString(1), (rs.getString(2)), rs.getString(3),
							rs.getString(4), rs.getString(5), rs.getString(6), Delete));
					suppliedItemIDList.add(Integer.decode(rs.getString(4)));
				}
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void RefreshItems3(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		sItemsTable();
	}

	public void sItemsTable() { // function that sets the columns and fills the table from the observable list.
		suppliedItemsIDColumn.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
		suppliedItemsOrderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
		suppliedItemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		suppliedItemIDColumn.setCellValueFactory(new PropertyValueFactory<>("itemID"));
		suppliedCopiesNumColumn.setCellValueFactory(new PropertyValueFactory<>("copiesNumber"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		deleteSuppliedItemColumn.setCellValueFactory(new PropertyValueFactory<>("Delete"));

		try {
			showSuppliedBill();
		} catch (IOException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SuppliedItemsTable.setItems(suppliedItemsList);
		SuppliedItemsTable.setEditable(true);
		//suppliedItemsIDColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		//suppliedItemsOrderIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		suppliedItemNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		//suppliedItemIDColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		suppliedItemIDColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		suppliedCopiesNumColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		priceColumn.setCellFactory(TextFieldTableCell.forTableColumn());

	}

	public void DeleteOrderSupplied(String id) {
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
			String sqlQuery = "delete from orderSupplied where orderID=" + Integer.decode(id);
			try {
				con = suppDB.connectDB();
				PreparedStatement statement = con.prepareStatement(sqlQuery);
				statement.executeUpdate();
				suppliedList.remove(suppliedOrderIDList.indexOf(Integer.decode(id)));
				suppliedOrderIDList.remove(Integer.decode(id));
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void DeleteSuppliedItem(String id) {// delete item function button. Takes the id of the item and its order id.
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Confirmation!");
		alert.setHeaderText("Are you certain you want to delete supplier with id?");
		alert.setContentText("Press OK to delete and Cancel to cancel");
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == null || option.get() == ButtonType.CANCEL) {
			// close the program
		} else if (option.get() == ButtonType.OK) {
			Connection con;
			DBConn suppDB = new DBConn();
			String sqlQuery = "delete from itemsSupplied where itemID=" + Integer.decode(id)+" and orderID="+ ordId2;
			//System.out.println(ordId2 +"--"+ Integer.decode(id));
			try {
				con = suppDB.connectDB();
				PreparedStatement statement = con.prepareStatement(sqlQuery);
				statement.executeUpdate();
				suppliedItemsList.remove(suppliedItemIDList.indexOf(Integer.decode(id)));
				suppliedItemIDList.remove(Integer.decode(id));
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void search() {
		FilteredList<orderSupplied> filteredData = new FilteredList<>(suppliedList, b -> true);
		search.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(searchedSupplier -> {
				if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
					return true;
				}
				String searchedFor = newValue.toLowerCase();
				if (searchedSupplier.getSupplierID() != null
						&& searchedSupplier.getSupplierID().toLowerCase().contains(searchedFor)) {
					return true;
				} else if (searchedSupplier.getOrderID().toLowerCase().contains(searchedFor)) {
					return true;
				} else if (searchedSupplier.getRequestDate().toLowerCase().contains(searchedFor)) {
					return true;
				}
				else if (searchedSupplier.getSuppliedDate().toLowerCase().contains(searchedFor)) {
					return true;
				}

				return false; // Does not match.
			});
		});
		SortedList<orderSupplied> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(SuppliedTable.comparatorProperty());
		SuppliedTable.setItems(sortedData);

	}
	public void updateSupplierID(TableColumn.CellEditEvent<orderSupplied, String> Event) {
		orderSupplied supply = SuppliedTable.getSelectionModel().getSelectedItem();
		supply.setSupplierID(Event.getNewValue());
		// String updatedSname = "'" + supp.getSname() + "'";
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update orderSupplied set supplierID= " + supply.getSupplierID() + " where orderID="
				+ supply.getOrderID();
		String sqlQuery2 = "update itemsSupplied set supplierID= " + supply.getSupplierID() + " where orderID="
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
	public void updateRequestDate(TableColumn.CellEditEvent<orderSupplied, String> Event) {
		orderSupplied supply = SuppliedTable.getSelectionModel().getSelectedItem();
		supply.setRequestDate(Event.getNewValue());
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update orderSupplied set requestDate= '" + supply.getRequestDate() + "' where orderID="
				+ supply.getOrderID();
		try {
			con = suppDB.connectDB();
			PreparedStatement statement = con.prepareStatement(sqlQuery);
			statement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("The Date you entered is not allowed.", "ERROR", "Failed");
		}

	}
	public void updateSuppliedDate(TableColumn.CellEditEvent<orderSupplied, String> Event) {
		orderSupplied supply = SuppliedTable.getSelectionModel().getSelectedItem();
		supply.setSuppliedDate(Event.getNewValue());
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update orderSupplied set suppliedDate= '" + supply.getSuppliedDate() + "' where orderID="
				+ supply.getOrderID();
		try {
			con = suppDB.connectDB();
			PreparedStatement statement = con.prepareStatement(sqlQuery);
			statement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("The Date you entered is not allowed.", "ERROR", "Failed");
		}

	}
	public void updateID(TableColumn.CellEditEvent<orderSupplied, String> Event) {
		orderSupplied supply = SuppliedTable.getSelectionModel().getSelectedItem();
		String oldID = supply.getOrderID();
		String IdTemp = (Event.getNewValue());
		supply.setOrderID(Event.getNewValue());
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update orderSupplied set orderID= " + supply.getOrderID() + " where orderID=" + oldID;
		try {
			con = suppDB.connectDB();
			PreparedStatement statement = con.prepareStatement(sqlQuery);
			statement.executeUpdate();
			suppliedOrderIDList.set(suppliedOrderIDList.indexOf(Integer.decode(oldID)),
					Integer.decode(supply.getOrderID()));
			// supplyOrderIDList.remove(Integer.decode(newID));
			// supplyOrderIDList.add(Integer.decode(supply.getOrderID()));
			supply.getDelete().setId(supply.getOrderID());
			supply.getViewBill().setId(supply.getOrderID());
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("The ID you entered already exists or is not allowed.", "ERROR", "Failed");
		}

	}
	public void updateItemName(TableColumn.CellEditEvent<itemsSupplied, String> Event) {
		itemsSupplied supply = SuppliedItemsTable.getSelectionModel().getSelectedItem();
		supply.setItemName(Event.getNewValue());
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update itemsSupplied set itemName= '" + supply.getItemName() + "' where itemID="
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

	public void updateItemID(TableColumn.CellEditEvent<itemsSupplied, String> Event) {
		itemsSupplied supply = SuppliedItemsTable.getSelectionModel().getSelectedItem();
		String oldID = supply.getItemID();
		supply.setItemID(Event.getNewValue());
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update itemsSupplied set itemID= " + supply.getItemID() + " where itemID=" + oldID
				+ " and orderID=" + supply.getOrderID();
		try {
			con = suppDB.connectDB();
			PreparedStatement statement = con.prepareStatement(sqlQuery);
			statement.executeUpdate();
			suppliedItemIDList.set(suppliedItemIDList.indexOf(Integer.decode(oldID)),
					Integer.decode(supply.getItemID()));
			supply.getDelete().setId(supply.getItemID());
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("The Name you entered is not allowed.", "ERROR", "Failed");
		}

	}
	public void updateCopies(TableColumn.CellEditEvent<itemsSupplied, String> Event) {
		itemsSupplied supply = SuppliedItemsTable.getSelectionModel().getSelectedItem();
		supply.setCopiesNumber(Event.getNewValue());
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update itemsSupplied set copiesNumber= " + supply.getCopiesNumber() + " where itemID=" + supply.getItemID()
				+ " and orderID=" + supply.getOrderID();
		try {
			con = suppDB.connectDB();
			PreparedStatement statement = con.prepareStatement(sqlQuery);
			statement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("The Name you entered is not allowed.", "ERROR", "Failed");
		}

	}
	public void updatePrice(TableColumn.CellEditEvent<itemsSupplied, String> Event) {
		itemsSupplied supply = SuppliedItemsTable.getSelectionModel().getSelectedItem();
		supply.setPrice(Event.getNewValue());
		Connection con;
		DBConn suppDB = new DBConn();
		String sqlQuery = "update itemsSupplied set price= " + supply.getPrice() + " where itemID=" + supply.getItemID()
				+ " and orderID=" + supply.getOrderID();
		try {
			con = suppDB.connectDB();
			PreparedStatement statement = con.prepareStatement(sqlQuery);
			statement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("The Name you entered is not allowed.", "ERROR", "Failed");
		}

	}
	public void addRequestBUTTON(ActionEvent event)  { // Add supplied order button Function,adds to database the written values when clicked. 
		// fillComboBox();
		Connection con;
		DBConn suppDB = new DBConn();
		// String query = "select * from supplier";
		try {
			con = suppDB.connectDB();
			String sqlQuery2 = "insert into orderSupplied values (?,?,?,?)";
			PreparedStatement addStatment2 = con.prepareStatement(sqlQuery2);
			addStatment2.setString(1, (String) supplierIdADD.getValue());
			addStatment2.setString(2, orderIdADD.getText());
			addStatment2.setString(3, (requestDateADD.getValue().toString()));
			addStatment2.setString(4, (suppliedDateADD.getValue().toString()));
			addStatment2.execute();
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("One OF the values is incorrect.", "ERROR", "Failed");
		}

	}

	public void addItemBUTTON(ActionEvent event) { // Add supplied item Function,adds to database the written values when clicked. 
		// fillComboBox();
		Connection con;
		DBConn suppDB = new DBConn();
		// String query = "select * from supplier";
		try {
			con = suppDB.connectDB();
			String sqlQuery2 = "insert into itemsSupplied values (?,?,?,?,?,?)";
			PreparedStatement addStatment2 = con.prepareStatement(sqlQuery2);
			addStatment2.setString(1, (String) supplierIdADD.getValue());
			addStatment2.setString(2, orderIdADD.getText());
			addStatment2.setString(3, itemNameADD.getText());
			addStatment2.setString(4, itemIdADD.getText());
			addStatment2.setString(5, copiesNumADD.getText());
			//int price = Integer.decode(copiesNumADD.getText()) * 5;
			addStatment2.setString(6, priceADD.getText());
			addStatment2.execute();
		} catch (ClassNotFoundException | SQLException e) {
			userController.infoBox("One OF the values is incorrect.", "ERROR", "Failed");
			e.printStackTrace();
		}

	}
	public void fillComboBox() { // combobox for suppliers.
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


}
