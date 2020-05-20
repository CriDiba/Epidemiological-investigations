package epidemic.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

    /**
     * initialize is the first method to be invoked
     * it initializes the columns and set the values of
     * the combobox
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initTableCols();
    	comboRuolo.getItems().addAll(Ruolo.values());
	}
	
	/**
	 * handles the click on Add button
	 */
	public void handleAddUser() {
		if(inputIsValid())
	    	tableView.getItems().add(new Utente(
	    			textNome.getText(),
	    			textCognome.getText(),
	    			textUsername.getText(),
	    			textPassword.getText(),    			
	    			comboRuolo.getValue()
	    		)
	    	);
    	clearTextFields();
    }
	
	/**
	 * checks if the username already exists or if any of the fields are empty or blank
	 * @return
	 */
	private boolean inputIsValid() {
		for(Utente u: tableView.getItems())
			if(textUsername.getText().equals(u.getUsername())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Username già occupato!");
				alert.showAndWait();
				return false;
			}
		return !(textNome.getText().isBlank() || textCognome.getText().isBlank() ||
				textUsername.getText().isBlank() || textPassword.getText().isBlank() ||
				comboRuolo.getValue() == null);
	}
	
	/**
	 * delete selected rows of the table
	 */
	public void handleDeleteSelected() {
		ObservableList<Utente> selectedItems = tableView.getSelectionModel().getSelectedItems();
		ObservableList<Utente> allItems = tableView.getItems();
		if(!allItems.isEmpty())
			selectedItems.forEach(allItems::remove);
	}
	
	/**
	 * clears textfields
	 */
	private void clearTextFields() {
		textNome.clear();
    	textCognome.clear();
    	textUsername.clear();
    	textPassword.clear();
	}
	
	/**
	 * initialize table columns contents
	 */
	private void initTableCols() {
		colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
    	colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
    	colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
    	colRuolo.setCellValueFactory(new PropertyValueFactory<>("ruolo"));
	}
    
    
}