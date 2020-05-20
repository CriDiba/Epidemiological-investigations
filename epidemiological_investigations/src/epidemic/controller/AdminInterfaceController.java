package epidemic.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ResourceBundle;

import epidemic.model.*;
import epidemic.model.DAO.DAO;
import epidemic.model.DAO.MySqlDAOFactory;
import javafx.collections.FXCollections;
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
import javafx.scene.input.MouseEvent;

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
    
    private int index = -1;
    
    private MySqlDAOFactory database;

    /**
     * initialize is the first method to be invoked
     * it initializes the columns and set the values of
     * the combobox
     */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initTableCols();
    	comboRuolo.getItems().addAll(Ruolo.values());
    	
    	//Visualizzo le entry del DB
    	database = new MySqlDAOFactory();
    	DAO<Utente> utenteDAO = database.getUtenteDAO();
    	List<Utente> utenti = utenteDAO.getAll();
    	ObservableList<Utente> listaUtenti = FXCollections.observableArrayList(utenti);
    	tableView.setItems(listaUtenti);
	}
	
	/**
	 * handles the click on Add button
	 */
	public void handleAddUser() {
		DAO<Utente> utenteDAO = database.getUtenteDAO();
		
		if(inputIsValid()) {
			Ruolo ruolo = comboRuolo.getValue();
			Utente utente = null;
			switch(ruolo) {
		    	case ADMIN: 
		    		utente = new Admin(textNome.getText(), textCognome.getText(), textUsername.getText(), toHash(textPassword.getText()));
		    		break;
		    	case ANALISTA:
		    		utente = new Analista(textNome.getText(), textCognome.getText(), textUsername.getText(), toHash(textPassword.getText()));
		    		break;
		    	case AUTORIZZATO: 
		    		utente = new Autorizzato(textNome.getText(), textCognome.getText(), textUsername.getText(), toHash(textPassword.getText()));
		    		break;
		    	case CONTRATTO:
		    		utente = new Contratto(textNome.getText(), textCognome.getText(), textUsername.getText(), toHash(textPassword.getText()));
		    		break;
			}
			utente.setId(utenteDAO.create(utente));
			
			tableView.getItems().add(utente);
		}
						
    	clearTextFields();
    }
	
	public static String toHash(String text) {
        String hash = null;
        try {
            byte[] textData = text.getBytes("UTF-8");
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digest = messageDigest.digest(textData);
            hash = new BigInteger(digest).toString(16);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
        	e.printStackTrace();
        }
        return hash;
    }
	
	/**
	 * checks if the username already exists or if any of the fields are empty or blank
	 * @return
	 */
	private boolean inputIsValid() {
		for(Utente u: tableView.getItems())
			if(textUsername.getText().equals(u.getUsername())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Username gi� occupato!");
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
	
	
	public void handleEditUser() {
		/*
		 * update in the database
		 */
	}
	
	public void getSelected(MouseEvent event) {
		index = tableView.getSelectionModel().getSelectedIndex();
		if(index <= -1)
			return;
		
		textNome.setText(colNome.getCellData(index));
		textCognome.setText(colCognome.getCellData(index));
		textUsername.setText(colUsername.getCellData(index));
		textPassword.setText(colPassword.getCellData(index));
		comboRuolo.setValue(colRuolo.getCellData(index));
	}
	
	/**
	 * clears textfields
	 */
	public void clearTextFields() {
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