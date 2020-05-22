package epidemic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
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
	private Spinner spinInfluenze, spinPolmoniti, spinMeningiti,
					spinEpatiti, spinMorbillo, spinTubercolosi,
					spinGastroenteriti;
	
	public void loadCases() {
		if(comboComune.getValue() == null || comboStatoPazienti.getValue() == null)
			return;
		
		//controlla nel database
		//se è presente il comune
		
		//se c'è carica i dati precedenti e 
		//sovrascrivi i labels
		
		
		//enable the grid
		gridMalattie.setDisable(false);
		
	}
	
	public void updateData() {
		
	}
}
