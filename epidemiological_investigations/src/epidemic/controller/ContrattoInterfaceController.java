package epidemic.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import epidemic.model.Comune;
import epidemic.model.Contagio;
import epidemic.model.MalattiaContagiosa;
import epidemic.model.SegnalazioneContagi;
import epidemic.model.DAO.MySqlDAOFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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
	//private SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
	private HashSet<Spinner<Integer>> spinnerSet;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setupSpinners();
		gridMalattie.setDisable(true);
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
	
	public void enableGrid() {
		gridMalattie.setDisable(false);
	}

	public void updateData() throws IOException {
		String nomeComune = comboComune.getValue();
		if(nomeComune == null || !comboComune.getItems().contains(nomeComune)) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Seleziona un comune valido!");
			alert.showAndWait();
			return;
		}
		
		Comune comuneRiferimento = database.getComuneDAO().getComuneDaNome(nomeComune);
		if(comuneRiferimento == null) {
			System.out.println("somethings wrong here");
			return;
		}
		Date dataOggi = new Date(System.currentTimeMillis());
		List<Contagio> contagi = new ArrayList<>();
		
		fillContagi(contagi);
		
		
		SegnalazioneContagi nuovaSegnalazione = new SegnalazioneContagi(contagi, dataOggi, comuneRiferimento);
		
		//aggiorna database
		database.getSegnalazioneContagiDAO().create(nuovaSegnalazione);
		
		
		//clear and disable grid
		for(Spinner<Integer> sp: spinnerSet)
			sp.getValueFactory().setValue(0);
		comboComune.getSelectionModel().clearSelection();
		gridMalattie.setDisable(true);
	}
	
	private void fillContagi(List<Contagio> contagi) {
		contagi.add(new Contagio(MalattiaContagiosa.INFLUENZA, spinInfluenze.getValue(), spinInfluenzeTI.getValue()));
		contagi.add(new Contagio(MalattiaContagiosa.INFLUENZA_COMPLICAZIONI, spinComplicazioni.getValue(), spinComplicazioniTI.getValue()));
		contagi.add(new Contagio(MalattiaContagiosa.EPATITE, spinEpatiti.getValue(), spinEpatitiTI.getValue()));
		contagi.add(new Contagio(MalattiaContagiosa.MENINGITE, spinMeningiti.getValue(), spinMeningitiTI.getValue()));
		contagi.add(new Contagio(MalattiaContagiosa.POLMONITE, spinPolmoniti.getValue(), spinPolmonitiTI.getValue()));
		contagi.add(new Contagio(MalattiaContagiosa.MORBILLO, spinMorbillo.getValue(), spinMorbilloTI.getValue()));
		contagi.add(new Contagio(MalattiaContagiosa.TUBERCOLOSI, spinTubercolosi.getValue(), spinTubercolosiTI.getValue()));
		contagi.add(new Contagio(MalattiaContagiosa.GASTROENTERITE, spinGastroenteriti.getValue(), spinGastroenteritiTI.getValue()));
	}
	
	private void setupSpinners() {
		setSpinnerSet();
		for(Spinner<Integer> sp: spinnerSet)
			sp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
	}
	
	private void setSpinnerSet() {
		spinnerSet = new HashSet<>();
		this.spinnerSet.add(spinInfluenze);
		this.spinnerSet.add(spinComplicazioni);
		this.spinnerSet.add(spinPolmoniti);
		this.spinnerSet.add(spinMeningiti);
		this.spinnerSet.add(spinEpatiti);
		this.spinnerSet.add(spinMorbillo);
		this.spinnerSet.add(spinTubercolosi);
		this.spinnerSet.add(spinGastroenteriti);
		this.spinnerSet.add(spinInfluenzeTI);
		this.spinnerSet.add(spinComplicazioniTI);
		this.spinnerSet.add(spinPolmonitiTI);
		this.spinnerSet.add(spinMeningitiTI);
		this.spinnerSet.add(spinEpatitiTI);
		this.spinnerSet.add(spinMorbilloTI);
		this.spinnerSet.add(spinTubercolosiTI);
		this.spinnerSet.add(spinGastroenteritiTI);
	}
	
}
