package epidemic.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminInterfaceController implements Initializable {
    @FXML
    private TableView<Utente> tableView;
    @FXML
    private TableColumn<Utente, String> colNome;
    @FXML
    private TableColumn<Utente, String> colCognome;
    @FXML
    private TableColumn<Utente, String> colUsername;
    @FXML
    private TableColumn<Utente, String> colPassword;
    @FXML
    private TableColumn<Utente, Ruolo> colRuolo;
    
    @FXML
    private TextField textNome;
    @FXML
    private TextField textCognome;
    @FXML
    private TextField textUsername;
    @FXML
    private TextField textPassword;
    @FXML
    private ComboBox<Ruolo> comboRuolo;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
    	colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
    	colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
    	colRuolo.setCellValueFactory(new PropertyValueFactory<>("ruolo"));
    	comboRuolo.getItems().addAll(Ruolo.values());
	}
	
	public void handleAddUser() {
    	tableView.getItems().add(new Utente(
    			textNome.getText(),
    			textCognome.getText(),
    			textUsername.getText(),
    			textPassword.getText(),    			
    			comboRuolo.getValue()
    		)
    	);
    }
	
	public void handleDeleteSelected() {
		ObservableList<Utente> selectedItems = tableView.getSelectionModel().getSelectedItems();
		ObservableList<Utente> allItems = tableView.getItems();
		if(!allItems.isEmpty())
			selectedItems.forEach(allItems::remove);
	}
    
    
}