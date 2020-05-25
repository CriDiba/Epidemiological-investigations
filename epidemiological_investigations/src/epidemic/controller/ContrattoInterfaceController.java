package epidemic.controller;

import java.io.IOException;

import epidemic.model.Comune;
import epidemic.model.Segnalazione;
import epidemic.model.DAO.MySqlDAOFactory;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;

public class ContrattoInterfaceController {
	@FXML
	private ComboBox comboComune;
	
	@FXML
	private ComboBox comboStatoPazienti;
	
	@FXML
	private GridPane gridMalattie;
	
	@FXML
	private Spinner spinInfluenze, spinComplicazioni, spinPolmoniti, spinMeningiti,
					spinEpatiti, spinMorbillo, spinTubercolosi,
					spinGastroenteriti;
	
	@FXML
	private Label lblInfluenze, lblComplicazioni, lblPolmoniti, lblMeningiti,
					lblEpatiti, lblTubercolosi, lblMorbillo, lblGastroenteriti;
	
	
	
	MySqlDAOFactory database = new MySqlDAOFactory();
	
	public void loadCases() throws IOException {
		if(comboComune.getValue() == null || comboStatoPazienti.getValue() == null)
			return;
		
		//controlla nel database
		Comune comuneTrovato = database.getComuneDAO().getComuneDaNome(comboComune.getValue().toString());
		
		//se c'è carica i dati precedenti e 
		//sovrascrivi i labels
		if(comuneTrovato != null) {
			//fai il totale dei
			//casi di tutte le segnalazioni per il comune
			
			//sovrascrivi i labels
		}
		
		
		//enable the grid
		gridMalattie.setDisable(false);
		
	}
	
	public void updateData() {
		//temp
		Segnalazione nuovaSegnalazione = new Segnalazione();
		//prendi dagli spinner i dati e usali
		//per creare la nuova segnalazione
		
		//aggiorna database
		
	}
}
