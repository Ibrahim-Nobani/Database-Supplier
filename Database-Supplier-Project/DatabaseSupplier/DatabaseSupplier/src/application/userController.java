package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;

public class userController implements Initializable {
	@FXML
	private TextField email;

	@FXML
	private PasswordField password;

	@FXML
	private Button submitButton;
	@FXML
	ImageView image;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		image.setImage(new Image(getClass().getResource("suc.jpg").toString()));

	}

	public void login(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {

		// Window owner = submitButton.getScene().getWindow();

		if (email.getText().isEmpty()) {
			// showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter your
			// email id");
			return;
		}
		if (password.getText().isEmpty()) {
			// showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter a
			// password");
			return;
		}
		boolean flag= checkUser();
		if (!flag) {
			infoBox("Please enter correct Email and Password", null, "Failed");
		} else {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("Ibra.fxml"));
			Scene scene = new Scene(fxmlLoader.load());
			Stage stage = new Stage();
			stage.setTitle("Supplier Menu");
			stage.setScene(scene);
			stage.show();
		}
	}

	public static void infoBox(String infoMessage, String headerText, String title) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setContentText(infoMessage);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.showAndWait();
	}

	private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.initOwner(owner);
		alert.show();
	}

	public boolean checkUser() throws ClassNotFoundException, SQLException {
		// fillComboBox();
		Connection con;
		DBConn suppDB = new DBConn();

		// String query = "select * from supplier";
		try {
			con = suppDB.connectDB();
			String sqlQuery = "select * from users where email = ? and password = ?";
			PreparedStatement addStatment = con.prepareStatement(sqlQuery);
			addStatment.setString(1, email.getText());
			addStatment.setString(2, password.getText());
			ResultSet result = addStatment.executeQuery();
			if (result.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
}
