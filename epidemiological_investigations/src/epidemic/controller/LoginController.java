package epidemic.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;

public class LoginController {
	private MainClass mainReference;
	
	@FXML
	private PasswordField pwd;
	
	public void setMainReference(MainClass mainReference) {
		this.mainReference = mainReference;
	}
	
	@FXML
	public void handleLogin(ActionEvent e) throws IOException {
		pwd.clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainClass.class.getResource("/epidemic/view/AdminInterface.fxml"));
		Scene scene = new Scene(loader.load());
		mainReference.getPrimaryStage().setScene(scene);
	}
}
