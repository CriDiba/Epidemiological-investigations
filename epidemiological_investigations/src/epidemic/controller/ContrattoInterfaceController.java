package epidemic.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.mysql.cj.util.Util;

import epidemic.model.Comune;
import epidemic.model.Contagio;
import epidemic.model.SegnalazioneContagi;
import epidemic.model.Utente;
import epidemic.model.DAO.MySqlDAOFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;

public class ContrattoInterfaceController implements Initializable {
	@FXML
	private ComboBox<String> comboComune;
	
	@FXML
	private GridPane gridMalattie;
	
	@FXML
	private Spinner<Integer> spinInfluenze, spinComplicazioni, spinPolmoniti, spinMeningiti,
					spinEpatiti, spinMorbillo, spinTubercolosi, spinGastroenteriti;
	
	@FXML
	private Spinner<Integer> spinInfluenzeTI, spinComplicazioniTI, spinPolmonitiTI, spinMeningitiTI,
					spinEpatitiTI, spinMorbilloTI, spinTubercolosiTI, spinGastroenteritiTI;
	
	private MySqlDAOFactory database;
	private ObservableList<String> listaComuniResponsabilita = FXCollections.observableArrayList();
	private SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setupSpinners();
		database = new MySqlDAOFactory();
		try {
			listaComuniResponsabilita = database.getComuneDAO().getNomeComuniPerResponsabile(LoginController.getIdSession());
			comboComune.setItems(listaComuniResponsabilita);
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Non è stato possibile completare la richiesta");
			alert.showAndWait();
			e.printStackTrace();
		}
	}

	public void updateData() {
		String nomeComune = comboComune.getValue();
		if(nomeComune == null || !comboComune.getItems().contains(nomeComune)) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Seleziona un comune valido!");
			alert.showAndWait();
			return;
		}
		//temp
		Date oggi = new Date(System.currentTimeMillis());
		List<Contagio> contagi = new ArrayList<>();
		SegnalazioneContagi nuovaSegnalazione;
		
		/*if(spinInfluenze.getValue() != 0)
			contagi.add(newContagio())*/
			
		//prendi dagli spinner i dati e usali
		//per creare la nuova segnalazione
		
		//aggiorna database
		
		//disable grid
		gridMalattie.setDisable(true);
		
	}
	
	private void setupSpinners() {
		spinInfluenze.setValueFactory(svf);
		spinComplicazioni.setValueFactory(svf);
		spinPolmoniti.setValueFactory(svf);
		spinMeningiti.setValueFactory(svf);
		spinEpatiti.setValueFactory(svf);
		spinMorbillo.setValueFactory(svf);
		spinTubercolosi.setValueFactory(svf);
		spinGastroenteriti.setValueFactory(svf);
		spinInfluenzeTI.setValueFactory(svf);
		spinComplicazioniTI.setValueFactory(svf);
		spinPolmonitiTI.setValueFactory(svf);
		spinMeningitiTI.setValueFactory(svf);
		spinEpatitiTI.setValueFactory(svf);
		spinMorbilloTI.setValueFactory(svf);
		spinTubercolosiTI.setValueFactory(svf);
		spinGastroenteritiTI.setValueFactory(svf);	
	}
}
