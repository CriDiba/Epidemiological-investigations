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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * Controller dell'interfaccia dedicata agli
 * utenti che identificano il personale a contratto.
 * 
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */

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
	private Label lblForza, lblSegnalazione, lblMalattia, lblPressoMedico, lblTerapieIntensive;
	
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
	
	@FXML
	private Button btnModifica;
	
	private MySqlDAOFactory database;
	private ObservableList<Comune> listaComuniResponsabilita = FXCollections.observableArrayList();
	private List<String> nomiComuniResponsabilita = new ArrayList<>();
	private HashMap<Comune, ArrayList<SegnalazioneContagi>> mappaComuneSegnalazioni = new HashMap<>();
	private HashSet<Spinner<Integer>> spinnerSet;
	private static double WEEK_MS = 6.048e+8;
	private final static Comune tuttiComuni = new Comune("Tutti i comuni", 0, "000000", null, null, true, null);
	
	
	/**
	 * Metodo inizializzatore per gli oggetti
	 * che popolano l'interfaccia grafica e per
	 * quelli di utilità che stanno in sottofondo
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		gridMalattie.setDisable(true);
		setupSpinners();
		setupBindingsAndListeners();
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

	@FXML
	public void enableGrid() {
		gridMalattie.setDisable(false);
	}
	
	/**
	 * Metodo che permette di forzare
	 * l'inserimento di una segnalazione
	 * dal momento che regolarmente è possibile
	 * un solo inserimento a settimana.
	 */
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
		
		if(comboComune.getSelectionModel().getSelectedItem() != null)
			enableGrid();
	}
	
	/**
	 * Metodo che aggiorna le combobox
	 * in base alla selezione di un comune
	 */
	@FXML
	public void handleSceltaComune() {
		Comune comuneSelezionato = getComuneSelezionato();
		if(comuneSelezionato == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Comune non presente nel database!");
			alert.showAndWait();
			return;
		}
		
		ArrayList<SegnalazioneContagi> segnalazioni = mappaComuneSegnalazioni.get(comuneSelezionato);
		
		comboIdSegnalazione.setItems(FXCollections.observableArrayList(getIdSegnalazioni(segnalazioni)));
		tableView.setItems(FXCollections.observableArrayList(segnalazioni));
		comboIdSegnalazione.setValue(null);
		comboMalattia.setValue(null);
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
		return null;
	}
	
	/**
	 * Metodo che permette di aggiornare il database
	 * con una nuova segnalazione, o avverte l'utente
	 * che non è possibile effettuare tale segnalazione
	 * 
	 * @throws IOException
	 */
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
		
		try {
			fillContagi(contagi);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Uno o più campi non hanno un valore valido!");
			alert.showAndWait();
			return;
		}
		
		
		SegnalazioneContagi nuovaSegnalazione = new SegnalazioneContagi(contagi, dataOggi, comuneRiferimento);
		
		//aggiorna database
		int idNuovaSegnalazione = database.getSegnalazioneContagiDAO().create(nuovaSegnalazione);
		nuovaSegnalazione.setId(idNuovaSegnalazione);
		
		//clear and disable grid
		for(Spinner<Integer> sp: spinnerSet)
			sp.getValueFactory().setValue(0);
		comboComune.setValue(null);
		gridMalattie.setDisable(true);
		
		for(Comune comune: mappaComuneSegnalazioni.keySet())
			if(comune.equals(comuneRiferimento)) {
				comuneRiferimento = comune;
				break;
			}
		
		mappaComuneSegnalazioni.get(comuneRiferimento).add(nuovaSegnalazione);
		mappaComuneSegnalazioni.get(tuttiComuni).add(nuovaSegnalazione);
		handleSceltaComune();
	}

	/**
	 * Metodo che invia al database
	 * le modifiche effettuate su un
	 * contagio di una segnalazione
	 * @throws IOException
	 */
	@FXML
	public void submitEdit() throws IOException {
		Integer personeInCura = spinPressoMedico.getValue();
		Integer personeRicoverate = spinTerapieIntensive.getValue();
		
		if(personeInCura == null || personeRicoverate == null)
			return;
		
		if(getComuneSelezionato() == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Impostare un comune valido!");
			alert.showAndWait();
			return;
		}
		
		for(SegnalazioneContagi segnalazione: mappaComuneSegnalazioni.get(getComuneSelezionato()))
			if(segnalazione.getId() == comboIdSegnalazione.getValue()) {
				for(Contagio contagio: segnalazione.getContagi())
					if(contagio.getMalattia().equals(comboMalattia.getValue())) {
						contagio.setPersoneInCura(personeInCura);
						contagio.setPersoneRicoverate(personeRicoverate);
						contagio.setSegnalazione(segnalazione);
						database.getContagioDAO().update(contagio);
						lblPressoMedico.setText(String.valueOf(personeInCura));
						lblTerapieIntensive.setText(String.valueOf(personeRicoverate));
						comboMalattia.setValue(null);
						spinPressoMedico.getValueFactory().setValue(0);
						spinTerapieIntensive.getValueFactory().setValue(0);
						return;
					}
			}
	}
	
	/**
	 * Metodo che prende l'id della
	 * segnalazione selezionata e lo imposta
	 * come id nella combobox di selezione segnalazioni
	 */
	@FXML
	public void handleSceltaSegnalazione() {
		if(tableView.getSelectionModel().getSelectedItem() != null)
			comboIdSegnalazione.setValue(tableView.getSelectionModel().getSelectedItem().getId());
	}
	
	/**
	 * Metodo che controlla se è passata
	 * almeno una settimana dall'ultima segnalazione
	 * 
	 * @param dataOggi data della nuova segnalazione
	 * @param comuneRiferimento comune di cui dobbiamo controllare l'ultima segnalazione
	 * @return true se la è possibile fare una nuova segnalazione oggi
	 * @throws IOException
	 */
	private boolean dateIsValid(Date dataOggi, Comune comuneRiferimento) throws IOException {
		SegnalazioneContagi ultimaSegnalazione = database.getSegnalazioneContagiDAO().getLastForComune(comuneRiferimento);
		if(ultimaSegnalazione != null && dataOggi.getTime() < ultimaSegnalazione.getData().getTime() + WEEK_MS)
			return false;
		return true;
	}
	
	/**
	 * Metodo che riempie una lista di
	 * oggetti Contagi sulla base del valore
	 * degli spinners dell'interfaccia grafica.
	 * 
	 * @param contagi lista di Contagi
	 */
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
	
	/**
	 * Metodo che inizializza gli spinner
	 */
	private void setupSpinners() {
		setSpinnerSet();
		for(Spinner<Integer> sp: spinnerSet)
			sp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
		spinPressoMedico.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
		spinTerapieIntensive.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
	}
	
	/**
	 * Metodo che riempie un HashSet con gli
	 * spinner dell'interfaccia per permettere
	 * una più facile gestione di questi.
	 */
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
	
	/**
	 * Metodo di supporto che inizializza
	 * la tabella
	 */
	private void initTableColumns() {
		colIdSegnalazione.setCellValueFactory(new PropertyValueFactory<>("id"));
		colDataSegnalazione.setCellValueFactory(new PropertyValueFactory<>("data"));
		colComune.setCellValueFactory(new PropertyValueFactory<>("comuneRiferimento"));
	}
	
	/**
	 * Metodo di supporto che inizializza
	 * la mappa Comune - Segnalazioni
	 * 
	 * @throws IOException
	 */
	private void initMap() throws IOException {		
		List<SegnalazioneContagi> tutteSegnalazioni = database.getSegnalazioneContagiDAO().getAll();
		
		mappaComuneSegnalazioni.put(tuttiComuni, new ArrayList<>());
		
		if(tutteSegnalazioni.isEmpty()) {
			for(Comune comune: listaComuniResponsabilita) {
				mappaComuneSegnalazioni.put(comune, new ArrayList<>());
				nomiComuniResponsabilita.add(comune.getNome());
			}
			return;
		}
		
		for(Comune comune: listaComuniResponsabilita) {
			nomiComuniResponsabilita.add(comune.getNome());
			mappaComuneSegnalazioni.put(comune, new ArrayList<>());
			for(SegnalazioneContagi s: tutteSegnalazioni)
				if(s.getComuneRiferimento().equals(comune)) {
					mappaComuneSegnalazioni.get(comune).add(s);
					mappaComuneSegnalazioni.get(tuttiComuni).add(s);
				}
		}
	}
	
	/**
	 * Metodo che imposta i bindings
	 * e i listeners per gli elementi
	 * dell'interfaccia grafica
	 */
	private void setupBindingsAndListeners() {
		spinPressoMedico.disableProperty().bind(comboMalattia.valueProperty().isNull().or(comboIdSegnalazione.valueProperty().isNull()));
		spinTerapieIntensive.disableProperty().bind(comboMalattia.valueProperty().isNull().or(comboIdSegnalazione.valueProperty().isNull()));
		btnModifica.disableProperty().bind(comboMalattia.valueProperty().isNull().or(comboIdSegnalazione.valueProperty().isNull()));
		lblSegnalazione.textProperty().bind(comboIdSegnalazione.valueProperty().asString());
		lblMalattia.textProperty().bind(comboMalattia.valueProperty().asString());
		lblSegnalazione.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				handleLabelTextChange();
			}
        });
		lblMalattia.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				handleLabelTextChange();
			}
        });
		
		for(Spinner<Integer> sp: spinnerSet)
			inputControlOnSpinner(sp);
		inputControlOnSpinner(spinPressoMedico);
		inputControlOnSpinner(spinTerapieIntensive);
	}
	
	private void inputControlOnSpinner(Spinner<Integer> sp) {
		sp.getEditor().textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	sp.getEditor().setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
	}
	
	
	/**
	 * Metodo che serve a modificare
	 * il testo dei labels in base alla selezione
	 * dell'utente
	 */
	private void handleLabelTextChange() {
		if(!lblSegnalazione.getText().equals("null") && !lblMalattia.getText().equals("null")) {
			Contagio found = null;
			for(SegnalazioneContagi segnalazione: mappaComuneSegnalazioni.get(getComuneSelezionato()))
				if(segnalazione.getId() == comboIdSegnalazione.getValue()) {
					for(Contagio c: segnalazione.getContagi())
						if(c.getMalattia().equals(comboMalattia.getValue()))
							found = c;
					break;
				}
			lblPressoMedico.setText(String.valueOf(found.getPersoneInCura()));
			lblTerapieIntensive.setText(String.valueOf(found.getPersoneRicoverate()));
		}
		else {
			lblPressoMedico.setText("-");
			lblTerapieIntensive.setText("-");
		}
	}
}


