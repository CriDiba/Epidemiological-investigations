package epidemic.controller;

import java.io.IOException;
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
import javafx.scene.control.PasswordField;
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
    private PasswordField textPassword;
    @FXML
    private ComboBox<Ruolo> comboRuolo;
    
   
    private MySqlDAOFactory database;
    private ObservableList<Utente> listaUtenti = FXCollections.observableArrayList();

    /**
     * Metodo che inizializza la tabella
     * e le combobox dell'interfaccia
     */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initTableCols();
    	comboRuolo.getItems().addAll(Ruolo.values());
    	
    	database = new MySqlDAOFactory();
    	List<Utente> utenti = null;
		try {
			utenti = database.getUtenteDAO().getAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	listaUtenti.addAll(utenti);
    	
    	tableView.setItems(listaUtenti);
	}
	
	/**
	 * Metodo che gestisce l'aggiunta di 
	 * un nuovo utente
	 * 
	 * @throws IOException 
	 */
	public void handleAddUser() throws IOException {
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
	
	/**
	 * Funzione che calcola l'hash di
	 * una stringa di testo, in particolare
	 * viene usata per salvare le password in maniera sicura
	 * 
	 * @param text
	 * @return hash testo dopo essere stato modificato
	 */
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
	 * Metodo che controlla se l'input lato utente è valido
	 * 
	 * @return true se l'input è ben formato
	 */
	private boolean inputIsValid() {
		for(Utente utente: listaUtenti)
			if(textUsername.getText().equals(utente.getUsername())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Username gia' occupato!");
				alert.showAndWait();
				return false;
			}
		return !(textNome.getText().isBlank() || textCognome.getText().isBlank() ||
				textUsername.getText().isBlank() || textPassword.getText().isBlank() ||
				comboRuolo.getValue() == null);
	}
	
	/**
	 * Metodo che elimina le righe selezionate della tabella
	 * 
	 * @throws IOException 
	 */
	public void handleDeleteSelected() throws IOException {
		Utente utente = tableView.getSelectionModel().getSelectedItem();
        if (utente != null && !utente.getRuolo().equals(Ruolo.ADMIN)) {
    		database.getUtenteDAO().delete(utente);
        	tableView.getItems().remove(utente);
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Eliminazione fallita");
            alert.setHeaderText("Selezione non valida");
            alert.setContentText("Selezionare un utente (non ADMIN) da eliminare!");
            alert.showAndWait();
        }
        
        clearTextFields();
	}
	
	/**
	 * Metodo che gestisce la modifica
	 * degli utenti
	 * 
	 * @throws IOException
	 */
	public void handleEditUser() throws IOException {

		Utente utente = tableView.getSelectionModel().getSelectedItem();
		
		if(!utente.getRuolo().equals(Ruolo.ADMIN)) {
			utente.setNome(textNome.getText());
			utente.setCognome(textCognome.getText());
			utente.setUsername(textUsername.getText());
			utente.setRuolo(comboRuolo.getValue());
			
			if(utente.getPassword() != textPassword.getText())
				utente.setPassword(toHash(textPassword.getText()));
		
			database.getUtenteDAO().update(utente);
			
			tableView.refresh();
		} else {
			Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Modifica fallita");
            alert.setHeaderText("Selezione non valida");
            alert.setContentText("Non è possibile modificare il profilo di un ADMIN");
            alert.showAndWait();
		}
	}
	
	/**
	 * Metodo che carica nelle combobox e nei campi testo
	 * l'utente selezionato dalla tabella
	 * 
	 * @param event
	 */
	public void getSelected(MouseEvent event) {
		
		Utente utente = tableView.getSelectionModel().getSelectedItem();
		if (utente != null) {
			textNome.setText(utente.getNome());
			textCognome.setText(utente.getCognome());
			textUsername.setText(utente.getUsername());
			comboRuolo.setValue(utente.getRuolo());
        }
	}
	
	
	/**
	 * Metodo che svuota i campi testo
	 */
	public void clearTextFields() {
		textNome.clear();
    	textCognome.clear();
    	textUsername.clear();
    	textPassword.clear();
	}
	
	/**
	 * Metodo che inizializza le colonne della tabella
	 */
	private void initTableCols() {
		colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    	colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
    	colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
    	colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
    	colRuolo.setCellValueFactory(new PropertyValueFactory<>("ruolo"));
	}
    
    
}