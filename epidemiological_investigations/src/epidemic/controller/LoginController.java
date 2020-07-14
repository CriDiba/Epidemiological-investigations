package epidemic.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import epidemic.model.Utente;
import epidemic.model.DAO.MySqlDAOFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginController {
	private MainClass mainReference;
	
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private ProgressIndicator progressIndicator;
	
	@FXML 
	private Text txtCredenziali;
	
	private static int idUtente;
	
	public void setMainReference(MainClass mainReference) {
		this.mainReference = mainReference;
	}
	
	/**
	 * Metodo che apre l'interfaccia relativa al
	 * tipo di utente che sta facendo login
	 * 
	 * @param e
	 * @throws IOException
	 */
	@FXML
	public void handleLogin(ActionEvent e) throws IOException {
		
		progressIndicator.setVisible(true);
		
		MySqlDAOFactory database = new MySqlDAOFactory();
		
		Utente utente = database.getUtenteDAO().getUsername(username.getText());
    	
    	if(utente != null && toHash(password.getText()).equals(utente.getPassword())) {
    		switch(utente.getRuolo()) {
		    	case ADMIN: 
		    		adminInterface();
		    		break;
		    	case ANALISTA:
		    		analistaInterface();
		    		break;
		    	case AUTORIZZATO: 
		    		autorizzatoInterface();
		    		break;
		    	case CONTRATTO:
		    		contrattoInterface(utente.getId());
		    		break;
			}
    	}
    	
    	txtCredenziali.setVisible(true);
    	
    
    	progressIndicator.setVisible(false);
    	password.clear();
		
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
	 * Metodo che carica l'interfaccia per gli admin
	 * 
	 * @throws IOException
	 */
	private void adminInterface() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainClass.class.getResource("/epidemic/view/AdminInterface.fxml"));
		Scene scene = new Scene(loader.load());
		mainReference.getPrimaryStage().setScene(scene);
	}
	
	/**
	 * Metodo che carica l'interfaccia per il personale
	 * a contratto
	 * 
	 * @param idUtenteContratto per gestire i comuni di responsabilità
	 * @throws IOException
	 */
	private void contrattoInterface(int idUtenteContratto) throws IOException{
		idUtente = idUtenteContratto;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainClass.class.getResource("/epidemic/view/ContrattoInterface.fxml"));
		Scene scene = new Scene(loader.load());
		mainReference.getPrimaryStage().setScene(scene);
	}
	
	/**
	 * Metodo che carica l'interfaccia per gli utenti
	 * autorizzati dell'ente
	 * 
	 * @throws IOException
	 */
	private void autorizzatoInterface() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainClass.class.getResource("/epidemic/view/AutorizzatoInterface.fxml"));
		Scene scene = new Scene(loader.load());
		mainReference.getPrimaryStage().setScene(scene);
	}
	
	private void analistaInterface() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainClass.class.getResource("/epidemic/view/AnalistaInterface.fxml"));
		Scene scene = new Scene(loader.load());
		mainReference.getPrimaryStage().setScene(scene);
	}
	
	/**
	 * Metodo che viene utilizzato dall'interfaccia
	 * ContrattoInterfaceController per caricare i comuni
	 * di responsabilità per l'utente con ID 'idUtente'
	 * 
	 * @return idUtente
	 */
	public static int getIdSession() {
		return idUtente;
	}
}
