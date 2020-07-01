package epidemic.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
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
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;

public class ContrattoInterfaceController implements Initializable {
	@FXML
	private ComboBox<String> comboComune, comboComuneTueSegnalazioni;
	
	@FXML
	private ComboBox<Integer> comboIdSegnalazione;
	
	@FXML
	private ComboBox<MalattiaContagiosa> comboMalattia;
	
	@FXML
	private GridPane gridMalattie;
	
	@FXML
	private Spinner<Integer> spinInfluenze, spinComplicazioni, spinPolmoniti, spinMeningiti,
					spinEpatiti, spinMorbillo, spinTubercolosi, spinGastroenteriti;
	
	@FXML
	private Spinner<Integer> spinInfluenzeTI, spinComplicazioniTI, spinPolmonitiTI, spinMeningitiTI,
					spinEpatitiTI, spinMorbilloTI, spinTubercolosiTI, spinGastroenteritiTI;
	
	@FXML
	private ToggleButton btnForza;
	
	@FXML
	private Label lblForza;
	
	@FXML
	private Spinner<Integer> spinPressoMedico, spinTerapieIntensive;
	
	@FXML
	private TableView<SegnalazioneContagi> tableView;
	
	@FXML
	private TableColumn<SegnalazioneContagi, Integer> colIdSegnalazione;
	
	@FXML
	private TableColumn<SegnalazioneContagi, Date> colDataSegnalazione;
	
	@FXML
	private TableColumn<SegnalazioneContagi, Comune> colComune;
	
	private MySqlDAOFactory database;
	private ObservableList<Comune> listaComuniResponsabilita = FXCollections.observableArrayList();
	private List<String> nomiComuniResponsabilita = new ArrayList<>();
	private HashMap<Comune, ArrayList<SegnalazioneContagi>> mappaComuneSegnalazioni = new HashMap<>();
	private HashSet<Spinner<Integer>> spinnerSet;
	private static double WEEK_MS = 6.048e+8;
	private final static Comune tuttiComuni = new Comune("Tutti i comuni", 0, "000000", null, null, true, null);
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		gridMalattie.setDisable(true);
		setupSpinners();
		comboMalattia.getItems().addAll(MalattiaContagiosa.values());
		database = new MySqlDAOFactory();
		try {
			listaComuniResponsabilita = database.getComuneDAO().getComuniPerResponsabile(LoginController.getIdSession());
			initMap();
			initTableColumns();
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Non è stato possibile completare la richiesta");
			alert.showAndWait();
			e.printStackTrace();
		}
		
		comboComune.getItems().addAll(nomiComuniResponsabilita);
		nomiComuniResponsabilita.add(0, tuttiComuni.getNome());
		comboComuneTueSegnalazioni.getItems().addAll(nomiComuniResponsabilita);
		comboComuneTueSegnalazioni.setValue(tuttiComuni.getNome());
		handleSceltaComune();
	}
	
	private void initTableColumns() {
		colIdSegnalazione.setCellValueFactory(new PropertyValueFactory<>("id"));
		colDataSegnalazione.setCellValueFactory(new PropertyValueFactory<>("data"));
		colComune.setCellValueFactory(new PropertyValueFactory<>("comuneRiferimento"));
	}

	private void initMap() throws IOException {
		
		for(Comune c: listaComuniResponsabilita)
			nomiComuniResponsabilita.add(c.getNome());
		
		List<SegnalazioneContagi> tutteSegnalazioni = database.getSegnalazioneContagiDAO().getAll();
		ArrayList<SegnalazioneContagi> tempArrayList;
		
		mappaComuneSegnalazioni.put(tuttiComuni, new ArrayList<>());
		for(Comune comune: listaComuniResponsabilita) {
			for(SegnalazioneContagi s: tutteSegnalazioni)
				if(s.getComuneRiferimento().equals(comune)) {
					mappaComuneSegnalazioni.putIfAbsent(comune, new ArrayList<>());
					tempArrayList = mappaComuneSegnalazioni.get(comune);
					tempArrayList.add(s);
					tempArrayList = mappaComuneSegnalazioni.get(tuttiComuni);
					tempArrayList.add(s);
				}
		}
	}

	@FXML
	public void enableGrid() {
		gridMalattie.setDisable(false);
	}
	
	@FXML
	public void handleForzaSegnalazione() {
		if(WEEK_MS != 0) {
			WEEK_MS = 0;
			btnForza.setStyle("-fx-background-color:#43C59E");
			btnForza.setText("Ripristina");
			lblForza.setVisible(true);
		}
		else {
			WEEK_MS = 6.048e+8;
			btnForza.setStyle("-fx-background-color:#E94F37");
			btnForza.setText("Forza segnalazione");
			lblForza.setVisible(false);
		}
	}
	
	@FXML
	public void handleSceltaComune() {
		Comune comuneSelezionato = getComuneSelezionato();
		ArrayList<SegnalazioneContagi> segnalazioni = mappaComuneSegnalazioni.get(comuneSelezionato);
		comboIdSegnalazione.setItems(FXCollections.observableArrayList(getIdSegnalazioni(segnalazioni)));
		
		tableView.setItems(FXCollections.observableArrayList(mappaComuneSegnalazioni.get(comuneSelezionato)));
	}
	
	private ArrayList<Integer> getIdSegnalazioni(List<SegnalazioneContagi> segnalazioni) {
		ArrayList<Integer> result = new ArrayList<>();
		for(SegnalazioneContagi s: segnalazioni)
			result.add(s.getId());
		return result;
	}

	private Comune getComuneSelezionato() {
		for(Comune comune: mappaComuneSegnalazioni.keySet())
			if(comune.getNome().equals(comboComuneTueSegnalazioni.getValue()))
				return comune;
		return tuttiComuni;
	}

	@FXML
	public void updateData() throws IOException {
		String nomeComune = comboComune.getValue();
		if(nomeComune == null || !comboComune.getItems().contains(nomeComune)) {
			gridMalattie.setDisable(true);
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Seleziona un comune valido!");
			alert.showAndWait();
			return;
		}
		
		Comune comuneRiferimento = database.getComuneDAO().getComuneDaNome(nomeComune);
		Date dataOggi = new Date(System.currentTimeMillis());
		
		if(!dateIsValid(dataOggi, comuneRiferimento)) {
			gridMalattie.setDisable(true);
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Segnalazione già effettuata per questa settimana!");
			alert.showAndWait();
			return;
		}
		
		List<Contagio> contagi = new ArrayList<>();
		
		fillContagi(contagi);
		
		
		SegnalazioneContagi nuovaSegnalazione = new SegnalazioneContagi(contagi, dataOggi, comuneRiferimento);
		
		//aggiorna database
		int idNuovaSegnalazione = database.getSegnalazioneContagiDAO().create(nuovaSegnalazione);
		nuovaSegnalazione.setId(idNuovaSegnalazione);
		
		//clear and disable grid
		for(Spinner<Integer> sp: spinnerSet)
			sp.getValueFactory().setValue(0);
		comboComune.getSelectionModel().clearSelection();
		gridMalattie.setDisable(true);
		
		comuneRiferimento = getReferenceToComune(comuneRiferimento);
		mappaComuneSegnalazioni.get(comuneRiferimento).add(nuovaSegnalazione);
		mappaComuneSegnalazioni.get(tuttiComuni).add(nuovaSegnalazione);
		handleSceltaComune();
	}
	
	private Comune getReferenceToComune(Comune comuneRiferimento) {
		for(Comune comune: mappaComuneSegnalazioni.keySet())
			if(comune.equals(comuneRiferimento))
				return comune;
		return null;
	}

	public void submitEdit() {
		
	}
	
	private boolean dateIsValid(Date dataOggi, Comune comuneRiferimento) throws IOException {
		SegnalazioneContagi ultimaSegnalazione = database.getSegnalazioneContagiDAO().getLastForComune(comuneRiferimento);
		if(dataOggi.getTime() < ultimaSegnalazione.getData().getTime() + WEEK_MS)
			return false;
		return true;
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
		spinPressoMedico.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
		spinTerapieIntensive.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
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
