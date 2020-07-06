package epidemic.controller;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class MainClass extends Application{
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Epidemiological Investigations");
		
		initRootLayout();
		showLoginScreen();
	}

	/**
	 * Initialize the root
	 */
	public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("/epidemic/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void showLoginScreen() {
        try {
            // Load login screen.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("/epidemic/view/Login.fxml"));
            VBox LoginScreen = (VBox)loader.load();
            
            // Set login screen into the center of root layout.
            rootLayout.setCenter(LoginScreen);
            
            //set reference to main class
            LoginController controller = loader.getController();
            controller.setMainReference(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	/**
	 * @return primaryStage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	/**
	 * main method
	 */
	public static void main(String[] args) {
		launch(args);
	}

	

}
