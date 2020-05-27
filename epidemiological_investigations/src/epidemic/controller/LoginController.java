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
import javafx.scene.control.TextField;

public class LoginController {
	private MainClass mainReference;
	
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	private static int idUtente;
	
	public void setMainReference(MainClass mainReference) {
		this.mainReference = mainReference;
	}
	
	
	@FXML
	public void handleLogin(ActionEvent e) throws IOException {
		
		MySqlDAOFactory database = new MySqlDAOFactory();

    	Utente utente = database.getUtenteDAO().getUsername(username.getText());
    	
    	if(toHash(password.getText()).equals(utente.getPassword())) {
    		switch(utente.getRuolo()) {
		    	case ADMIN: 
		    		adminInterface();
		    		break;
		    	case ANALISTA:
		    		//analistaInterface();
		    		break;
		    	case AUTORIZZATO: 
		    		//autorizzatoInterface();
		    		break;
		    	case CONTRATTO:
		    		contrattoInterface(utente.getId());
		    		break;
			}
    	}
    
    	
    	password.clear();
		
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
	
	
	private void adminInterface() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainClass.class.getResource("/epidemic/view/AdminInterface.fxml"));
		Scene scene = new Scene(loader.load());
		mainReference.getPrimaryStage().setScene(scene);
	}
	
	private void contrattoInterface(int idUtenteContratto) throws IOException{
		idUtente = idUtenteContratto;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainClass.class.getResource("/epidemic/view/ContrattoInterface.fxml"));
		Scene scene = new Scene(loader.load());
		mainReference.getPrimaryStage().setScene(scene);
	}
	
	public static int getIdSession() {
		return idUtente;
	}
}
