package epidemic.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginController {
	private MainClass mainReference;
	
	public void setMainReference(MainClass mainReference) {
		this.mainReference = mainReference;
	}
	
	@FXML
	public void handleLogin(ActionEvent e) {
		Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(mainReference.getPrimaryStage());
        alert.setTitle("Authentication problem");
        alert.setHeaderText("You failed to login. Try again!");
        alert.showAndWait();
	}
}
