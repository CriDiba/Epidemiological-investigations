package epidemic.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import epidemic.model.CausaDecesso;
import epidemic.model.Comune;
import epidemic.model.Decesso;
import epidemic.model.Provincia;
import epidemic.model.Regione;
import epidemic.model.SegnalazioneDecessi;
import epidemic.model.Territorio;
import epidemic.model.DAO.MySqlDAOFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Interfaccia del sistema per utenti con ruolo di Autorizzato
 * Tramite questa interfaccia è possibile modifcare le informazioni geografiche,
 * assegnare ad ogni comune un resposabile, inserire annualmente le informazioni
 * relative ai decessi avvenuti nelle varie provincie
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class AutorizzatoInterfaceController {
	
	// Tab Dati Geografici
	
    @FXML
    private TableView<Provincia> tableProvince;
    @FXML
    private TableColumn<Provincia, String> colProvNome;
    @FXML
    private TableColumn<Provincia, Double> colProvSuperficie;
    @FXML
    private TableColumn<Provincia, String> colProvCapoluogo;

    @FXML
    private TableView<Comune> tableComuni; 
    @FXML
    private TableColumn<Comune, String> colComNome;
    @FXML
    private TableColumn<Comune, Double> colComSuperficie;
    @FXML
    private TableColumn<Comune, String> colComISTAT;
    @FXML
    private TableColumn<Comune, Date> colComIstituzione;
    @FXML
    private TableColumn<Comune, Territorio> colComTerritorio;
    @FXML
    private TableColumn<Comune, Boolean> colComMare;
    @FXML
    private TableColumn<Comune, String> colComResponsabile;
    
    @FXML
    private TextField textRegNome, textRegCapoluogo;
    @FXML
    private Spinner<Double> spinRegSuperficie;
    @FXML
    private ComboBox<Regione> comboRegione;
    
    @FXML
	private ButtonBar btnProvincia, btnComune;
    @FXML
    private Button btnAddRegione, btnEditRegione, btnDeleteRegione;
    
	private MySqlDAOFactory database;
	private ObservableList<Regione> listaRegioni = FXCollections.observableArrayList();
	private ObservableList<Provincia> listaProvince = FXCollections.observableArrayList();
	private ObservableList<Comune> listaComuni = FXCollections.observableArrayList();
	private Regione regioneSelezionata;
	private Provincia provinciaSelezionata;
	private Comune comuneSelezionato;
	boolean insertRegioneMode = false;
	
	
	// Tab Segnalazioni di Decesso
	
	@FXML
	private ComboBox<Regione> comboRegioneSegnalazione;
	@FXML
	private ComboBox<Provincia> comboProvinciaSegnalazione;
	@FXML
	private VBox boxNuovaSegnalazione;
	
	@FXML
	private TableView<SegnalazioneDecessi> tableSegnalazioni;
	@FXML
	private TableColumn<SegnalazioneDecessi, Integer> colIdSegnalazione;
	@FXML
	private TableColumn<SegnalazioneDecessi, Date> colDataSegnalazione;
	@FXML
	private TableColumn<SegnalazioneDecessi, Provincia> colProvinciaSegnalazione;
	
	@FXML
	private Button btnNuovaSegnalazione, btnForzaSegnalazione;

	@FXML
	private Spinner<Integer> spinIncidenti, spinCardio, spinTumori, spinContagio;
	
	@FXML
	private Label lblIncidenti, lblTumori, lblCardio, lblContagio;
	
	private Regione regioneSegnalazione;
	private Provincia provinciaSegnalazione;
	private ObservableList<Provincia> listaProvinceSegnalazione = FXCollections.observableArrayList();
	private ObservableList<SegnalazioneDecessi> listaSegnalazioni = FXCollections.observableArrayList();
	private boolean forzaSegnalazione = false;
    
	
	/**
     * Inizializza il controller. Questo metodo viene invocato automaticamente 
     * dopo che il file fxml è stato caricato
	 * @throws IOException 
	 */
	@FXML
	public void initialize() throws IOException {
		
		//Setup input control sugli spinner
		inputControlOnSpinner(spinIncidenti);
		inputControlOnSpinner(spinCardio);
		inputControlOnSpinner(spinTumori);
		inputControlOnSpinner(spinContagio);
		
		// Tab Dati Geografici
		initTableProvinceCols();
		initTableComuniCols();
		spinRegSuperficie.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, 0.0, 0.1));
		database = new MySqlDAOFactory();
		listaRegioni.addAll(database.getRegioneDAO().getAll());
		comboRegione.setItems(listaRegioni);
		btnComune.setDisable(true);
		btnProvincia.setDisable(true);
		
		
		//Tab Segnalazioni
		boxNuovaSegnalazione.setDisable(true);
		comboRegioneSegnalazione.setItems(listaRegioni);
		spinIncidenti.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
		spinCardio.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
		spinTumori.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
		spinContagio.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
		initTableSegnalazioniCols();
	}

	
	//-------------------------------------------------------------
	// TAB DATI GEOGRAFICI
	//-------------------------------------------------------------
	
	/**
	 * Se viene selezionata una regione attiva la tabella con le province corrispondenti
	 */
	@FXML
	public void handleSceltaRegione() {
		regioneSelezionata = comboRegione.getValue();
		
		if (regioneSelezionata != null) {
			textRegNome.setText(regioneSelezionata.getNome());
			spinRegSuperficie.getValueFactory().setValue(regioneSelezionata.getSuperficie());
			textRegCapoluogo.setText(regioneSelezionata.getCapoluogo());
			
			btnComune.setDisable(true);
			listaProvince.clear();
			listaComuni.clear();
			
			try {
				for (Provincia provincia : database.getProvinciaDAO().getAll()) {
					if(provincia.getRegioneAppartenenza().equals(regioneSelezionata))
						listaProvince.add(provincia);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			tableProvince.setItems(listaProvince);
			btnProvincia.setDisable(false);
		}
	}
	
	
	/**
	 * Se viene selezionata una provincia attiva la tabella con i comuni corrispondenti
	 * @param event
	 */
	@FXML
	public void handleSceltaProvincia(MouseEvent event) {
		provinciaSelezionata = tableProvince.getSelectionModel().getSelectedItem();
		listaComuni.clear();
		
		try {
			for (Comune comune : database.getComuneDAO().getAll()) {
				if(comune.getProvinciaAppartenenza().equals(provinciaSelezionata))
					listaComuni.add(comune);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tableComuni.setItems(listaComuni);
		btnComune.setDisable(false);
	}
	
	
	/**
	 * Crea una nuova regione
	 * @throws IOException 
	 */
	@FXML
	private void handleAddRegione() throws IOException {
		if(insertRegioneMode) {
			if (isInputValid()) {
				Regione nuovaRegione = new Regione(textRegNome.getText(), spinRegSuperficie.getValue(), textRegCapoluogo.getText());
				nuovaRegione.setId(database.getRegioneDAO().create(nuovaRegione));
				listaRegioni.add(nuovaRegione);
				comboRegione.setValue(nuovaRegione);
				comboRegione.setDisable(false);
				btnDeleteRegione.setDisable(false);
				btnAddRegione.setText("Nuovo");
				btnEditRegione.setText("Modifica");
				insertRegioneMode = false;
				regioneSelezionata = nuovaRegione;
			}
		} else {
			listaProvince.clear();
			listaComuni.clear();
			comboRegione.setValue(null);
			textRegNome.clear();
			spinRegSuperficie.getValueFactory().setValue(0.0);
			textRegCapoluogo.clear();
			regioneSelezionata = null;
			comboRegione.setDisable(true);
			btnProvincia.setDisable(true);
			btnComune.setDisable(true);
			btnDeleteRegione.setDisable(true);
			btnAddRegione.setText("Salva");
			btnEditRegione.setText("Annulla");
			insertRegioneMode = true;
		}
	}
	
	/**
	 * Modifica i dati di una regione
	 */
	@FXML
	private void handleEditRegione() throws IOException {
		if(insertRegioneMode) {
			comboRegione.setDisable(false);
			btnDeleteRegione.setDisable(false);
			btnAddRegione.setText("Nuovo");
			btnEditRegione.setText("Modifica");
			insertRegioneMode = false;
		} else {
			if(regioneSelezionata != null) {
				if(isInputValid()) {
					regioneSelezionata.setNome(textRegNome.getText());
					regioneSelezionata.setCapoluogo(textRegCapoluogo.getText());
					regioneSelezionata.setSuperficie(spinRegSuperficie.getValue());
					database.getRegioneDAO().update(regioneSelezionata);
				}
			} else {
				noSelectionWarning("Selezionare una Regione da modificare");
			}
		}
		
	}
	
	/**
	 * Elimina una regione esistente
	 */
	@FXML
	private void handleDeleteRegione() throws IOException {
		Regione regione = comboRegione.getValue();
        if (regione != null) {
        	if(!confirmElimination("Verrà eliminata la regione, le provincie ed i comuni che ne fanno parte"))
    			return;
        	
    		database.getRegioneDAO().delete(regione);
    		listaRegioni.remove(regione);
        } else {
        	noSelectionWarning("Selezionare una Regione da eliminare");
        }
	}
	
	
	/**
	 * Apre un dialog per l'inserimento dei dati e la creazione
	 * di una nuova Provincia
	 */
	@FXML
	private void handleAddProvincia() throws IOException {
	    boolean salvaClicked = showProvinciaEditDialog(null);
	    if (salvaClicked) {
	        provinciaSelezionata.setId(database.getProvinciaDAO().create(provinciaSelezionata));
	        tableProvince.getItems().add(provinciaSelezionata);
	        if(regioneSelezionata.equals(regioneSegnalazione))
	        	listaProvinceSegnalazione.add(provinciaSelezionata);
	    }
	}
	
	
	/**
	 * Apre un dialog per l'inserimento dei dati e la modifica
	 * di una Provincia selezionata
	 */
	@FXML
	private void handleEditProvincia() throws IOException {
		provinciaSelezionata = tableProvince.getSelectionModel().getSelectedItem();
	    if (provinciaSelezionata != null) {
	    	boolean salvaClicked = showProvinciaEditDialog(provinciaSelezionata);
	        if (salvaClicked) {
		        database.getProvinciaDAO().update(provinciaSelezionata);
		        tableProvince.refresh();
		    }
	    } else {
	    	noSelectionWarning("Selezionare una Provincia da modificare");
	    }
	}
	
	
	/**
	 * Elimina la provincia selezionata
	 */
	@FXML
	private void handleDeleteProvincia() throws IOException {
		Provincia provincia = tableProvince.getSelectionModel().getSelectedItem();
        if (provincia != null) {
        	if(!confirmElimination("Verrà eliminata la provincia ed i comuni che ne fanno parte"))
    			return;
    		database.getProvinciaDAO().delete(provincia);
    		tableProvince.getItems().remove(provincia);
    		listaComuni.clear();
        } else {
        	noSelectionWarning("Selezionare una Provincia da eliminare");
        }
	}
	
	
	/**
	 * Apre un dialog per l'inserimento dei dati e la creazione
	 * di una nuovo Comune
	 */
	@FXML
	private void handleAddComune() throws IOException {
	    boolean salvaClicked = showComuneEditDialog(null);
	    if (salvaClicked) {
	        comuneSelezionato.setId(database.getComuneDAO().create(comuneSelezionato));
	        tableComuni.getItems().add(comuneSelezionato);
	    }
	}
	
	/**
	 * Apre un dialog per l'inserimento dei dati e la modifica
	 * di un Comune selezionato
	 */
	@FXML
	private void handleEditComune() throws IOException {
		comuneSelezionato = tableComuni.getSelectionModel().getSelectedItem();
	    if (comuneSelezionato != null) {
	    	boolean salvaClicked = showComuneEditDialog(comuneSelezionato);
	        if (salvaClicked) {
		        database.getComuneDAO().update(comuneSelezionato);
		        tableComuni.refresh();
		    }
	    } else {
	    	noSelectionWarning("Selezionare un Comune da modificare");
	    }
	}
	
	
	/**
	 * Elimina un comune esistente
	 */
	@FXML
	private void handleDeleteComune() throws IOException {
		Comune comune = tableComuni.getSelectionModel().getSelectedItem();
        if (comune != null) {
        	if(!confirmElimination("Verrà eliminato il comune selezionato"))
    			return;
        	System.out.println(comune.getId());
    		database.getComuneDAO().delete(comune);
    		tableComuni.getItems().remove(comune);
        } else {
        	noSelectionWarning("Selezionare un Comune da modificare");
        }
	}
	
	/**
	 * Inizializza la tabella delle province
	 */
	private void initTableProvinceCols() {
		colProvNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		colProvSuperficie.setCellValueFactory(new PropertyValueFactory<>("superficie"));
		colProvCapoluogo.setCellValueFactory(new PropertyValueFactory<>("capoluogo"));
	}
	
	/**
	 * Inizializza la tabella dei comuni
	 */
	private void initTableComuniCols() {
		colComNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		colComSuperficie.setCellValueFactory(new PropertyValueFactory<>("superficie"));
		colComISTAT.setCellValueFactory(new PropertyValueFactory<>("istat"));
		colComIstituzione.setCellValueFactory(new PropertyValueFactory<>("dataIstituzione"));
		colComTerritorio.setCellValueFactory(new PropertyValueFactory<>("territorio"));
		colComMare.setCellValueFactory(new PropertyValueFactory<>("sulMare"));
		colComResponsabile.setCellValueFactory(new PropertyValueFactory<>("responsabile"));
	}
	
	
	//-------------------------------------------------------------
	// TAB SEGNALAZIONI
	//-------------------------------------------------------------
	
	/**
	 * Sblocca la creazione di una nuova segnalazione di decesso
	 */
	@FXML
	private void handleNuovaSegnalazione() {
		regioneSegnalazione = comboRegioneSegnalazione.getValue();
		provinciaSegnalazione = comboProvinciaSegnalazione.getValue();
		
        if (regioneSegnalazione != null) {
        	if(provinciaSegnalazione != null) {
        		boxNuovaSegnalazione.setDisable(false);
        		comboRegioneSegnalazione.setDisable(true);
        		comboProvinciaSegnalazione.setDisable(true);
        		btnNuovaSegnalazione.setDisable(true);
        	} else {
        		noSelectionWarning("Selezionare una Provincia per la quale effettuare una segnalazione");
        	}
        } else {
        	noSelectionWarning("Selezionare una Regione per la quale effettuare una segnalazione");
        }
	}
	
	/**
	 * Crea una nuova segnalazione di decesso se è trascorso un 
	 * anno dalla data dell'ultima segnalazione
	 * 
	 * @throws IOException
	 */
	@FXML
	private void handleAddSegnlazione() throws IOException {
		Date dataOggi = new Date(System.currentTimeMillis());
		if(!forzaSegnalazione && !dateIsValid(dataOggi, provinciaSegnalazione)) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Segnalazione già effettuata per questa settimana!");
			alert.showAndWait();
			handleAnnullaSegnalazione();
			return;
		}
		
		if(spinCardio.getValue() == null || spinContagio.getValue() == null || 
				spinIncidenti.getValue() == null || spinTumori.getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Correggere i campi non validi!");
			alert.showAndWait();
			return;
		}
		
		List<Decesso> decessi = new ArrayList<Decesso>();
		fillDecessi(decessi);
		SegnalazioneDecessi nuovaSegnalazione = new SegnalazioneDecessi(decessi, dataOggi, provinciaSegnalazione);
		int idSegnalazione = database.getSegnalazioneDecessiDAO().create(nuovaSegnalazione);
		nuovaSegnalazione.setId(idSegnalazione);	
		tableSegnalazioni.getItems().add(nuovaSegnalazione);
		handleAnnullaSegnalazione();
	}
	
	/**
	 * Se viene selezionata una regione attiva la comboBox con le provincie
	 * che fanno parte di quella regione
	 * 
	 * @throws IOException
	 */
	@FXML
	private void handleSceltaRegSegnlazione() throws IOException {
		regioneSegnalazione = comboRegioneSegnalazione.getValue();
		listaSegnalazioni.clear();
		listaProvinceSegnalazione.clear();
		clearLbl();
		if(regioneSegnalazione != null) {
			for (Provincia provincia : database.getProvinciaDAO().getAll()) {
				if(provincia.getRegioneAppartenenza().equals(regioneSegnalazione))
					listaProvinceSegnalazione.add(provincia);
			}
			comboProvinciaSegnalazione.setItems(listaProvinceSegnalazione);
		}
	}
	
	/**
	 * Se viene selezionata una provincia attiva la tabella
	 * con le segnalazioni di decessi effettuate per quella
	 * provincia
	 * 
	 * @throws IOException
	 */
	@FXML
	private void handleSceltaProvSegnlazione() throws IOException {
		provinciaSegnalazione = comboProvinciaSegnalazione.getValue();
		listaSegnalazioni.clear();
		clearLbl();
		if(provinciaSegnalazione != null) {
			listaSegnalazioni.addAll(database.getSegnalazioneDecessiDAO().getForProvincia(provinciaSegnalazione));
		}
		tableSegnalazioni.setItems(listaSegnalazioni);
	}
	
	/**
	 * Annulla la creazione di una nuova segnalazione di decesso
	 */
	@FXML
	private void handleAnnullaSegnalazione() {
		spinIncidenti.getValueFactory().setValue(0);
		spinTumori.getValueFactory().setValue(0);
		spinCardio.getValueFactory().setValue(0);
		spinContagio.getValueFactory().setValue(0);
		boxNuovaSegnalazione.setDisable(true);
		btnNuovaSegnalazione.setDisable(false);
		comboRegioneSegnalazione.setDisable(false);
		comboProvinciaSegnalazione.setDisable(false);
		if(forzaSegnalazione)
			handleForzaSegnalazione();
	}
	
	/**
	 * Mostra i dati di una segnalazione di decesso divisi per causa di morte
	 */
	@FXML
	private void handleSceltaSegnalazione() {
		SegnalazioneDecessi segnalazione = tableSegnalazioni.getSelectionModel().getSelectedItem();
		for (Decesso decesso : segnalazione.getDecessi()) {
			switch (decesso.getCausa()) {
			case INCIDENTE_STRADALE:
				lblIncidenti.setText(decesso.getNumero() + "");
				break;
			case MALATTIA_TUMORALE:
				lblTumori.setText(decesso.getNumero() + "");
				break;
			case MALATTIA_CARDIOVASCOLARE:
				lblCardio.setText(decesso.getNumero() + "");
				break;
			case MALATTIA_CONTAGIOSA:
				lblContagio.setText(decesso.getNumero() + "");
				break;
			}
		}
	}
	
	/**
	 * Pulisce i campi Label
	 */
	private void clearLbl() {
		lblIncidenti.setText("-");
		lblTumori.setText("-");
		lblCardio.setText("-");
		lblContagio.setText("-");
	}
	
	/**
	 * Riempie la lista dei decessi di una segnalazione
	 * con i valori degli Spinner
	 * 
	 * @param decessi
	 */
	private void fillDecessi(List<Decesso> decessi) {
		decessi.add(new Decesso(CausaDecesso.INCIDENTE_STRADALE, spinIncidenti.getValue()));
		decessi.add(new Decesso(CausaDecesso.MALATTIA_TUMORALE, spinTumori.getValue()));
		decessi.add(new Decesso(CausaDecesso.MALATTIA_CARDIOVASCOLARE, spinCardio.getValue()));
		decessi.add(new Decesso(CausaDecesso.MALATTIA_CONTAGIOSA, spinContagio.getValue()));
	}
	
		
	/**
	 * Apre una finestra di dialogo per inserire i dati di una Provincia. 
	 * Se l'utente selziona 'Salva', le modifice vengono salvate nel database
	 * e viene restituito il valore true
	 * 
	 * @param provincia la provincia da modificare
	 * @return true se l'utente seleziona 'Salva'
	 */
	public boolean showProvinciaEditDialog(Provincia provincia) {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainClass.class.getResource("/epidemic/view/ProvinciaEditDialog.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Modifica Provincia");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the person into the controller.
	        ProvinciaEditDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setProvincia(provincia, regioneSelezionata);

	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();

	        if (controller.isSalvaClicked()) {
	        	provinciaSelezionata = controller.getProvincia();
	        	return true;
	        }
	        
	        return controller.isSalvaClicked();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	/**
	 * Inizializza la tabella delle segnalazioni effettuate
	 */
	private void initTableSegnalazioniCols() {
		colIdSegnalazione.setCellValueFactory(new PropertyValueFactory<>("id"));
		colDataSegnalazione.setCellValueFactory(new PropertyValueFactory<>("data"));
		colProvinciaSegnalazione.setCellValueFactory(new PropertyValueFactory<>("provinciaRiferimento"));
	}
	
	
	/**
	 * Apre una finestra di dialogo per inserire i dati di un Comune. 
	 * Se l'utente selziona 'Salva', le modifice vengono salvate nel database
	 * e viene restituito il valore true
	 * 
	 * @param comune il comune da modificare
	 * @return true se l'utente seleziona 'Salva'
	 */
	public boolean showComuneEditDialog(Comune comune) {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainClass.class.getResource("/epidemic/view/ComuneEditDialog.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Modifica Comune");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the person into the controller.
	        ComuneEditDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setComune(comune, provinciaSelezionata);

	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();

	        if (controller.isSalvaClicked()) {
	        	comuneSelezionato = controller.getComune();
	        	return true;
	        }
	        
	        return controller.isSalvaClicked();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	/**
	 * Genera un alert di conferma con un messaggio a video
	 * 
	 * @param messaggio da mostrare nell'alert
	 * @return true se l'utente conferma, falso altrimenti
	 */
	private boolean confirmElimination(String messaggio) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Eliminazione");
		alert.setHeaderText("Vuoi procedere con l'eliminazione?");
		alert.setContentText(messaggio);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    return true;
		} else {
		    return false;
		}
	}
	
	/**
	 * Genera un alert di waring con un messaggio a video
	 * @param messaggio da mostrare nell'alert
	 */
	private void noSelectionWarning(String messaggio) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Nessuna Selezione");
        alert.setHeaderText("Nessun elemento selezionato");
        alert.setContentText(messaggio);
        alert.showAndWait();
	}
	
    /**
     * Verifica che i dati di input passati ai campi siano validi
     * 
     * @return true se gli input sono validi, falso altrimenti
     */
	private boolean isInputValid() {
		String errorMessage = "";
		
		 if (textRegNome.getText() == null || textRegNome.getText().length() == 0 || textRegNome.getText().matches(".*\\d+.*"))
	            errorMessage += "Nome non valido\n"; 
		 if (textRegCapoluogo.getText() == null || textRegCapoluogo.getText().length() == 0 || textRegCapoluogo.getText().matches(".*\\d+.*"))
	            errorMessage += "Capoluogo non valido\n"; 
        if (spinRegSuperficie.getValue() == null || spinRegSuperficie.getValue() <= 0)
            errorMessage += "Superficie non valida!\n"; 

        if (errorMessage.length() == 0) {
            return true;
        } else {
        	Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Campi non validi");
            alert.setHeaderText("Correggere i campi non validi");
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
            
            return false;
        }
	}
	
	/**
	 * Controlla se è passato un Anno dall'ultima segnalazione
	 * 
	 * @param dataOggi data della nuova segnalazione
	 * @param comuneRiferimento comune di cui dobbiamo controllare l'ultima segnalazione
	 * @return true se la è possibile fare una nuova segnalazione oggi
	 * @throws IOException
	 */
	private boolean dateIsValid(Date dataOggi, Provincia provincia) throws IOException {
		SegnalazioneDecessi ultimaSegnalazione = database.getSegnalazioneDecessiDAO().getLastForProvincia(provincia);
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		
		if(ultimaSegnalazione != null) {
			cal1.setTime(dataOggi);
			cal2.setTime(ultimaSegnalazione.getData());
			if(cal1.get(Calendar.YEAR) <= cal2.get(Calendar.YEAR))
				return false;
		}
		return true;
	}
	
	
	/**
	 * Metodo che permette di forzare  l'inserimento di una segnalazione
	 * dal momento che regolarmente è possibile un solo inserimento all'anno.
	 */
	@FXML
	public void handleForzaSegnalazione() {
		if(!forzaSegnalazione) {
			forzaSegnalazione = true;
			btnForzaSegnalazione.setStyle("-fx-background-color:#43C59E");
			btnForzaSegnalazione.setText("Ripristina");
		}
		else {
			forzaSegnalazione = false;
			btnForzaSegnalazione.setStyle("-fx-background-color:#E94F37");
			btnForzaSegnalazione.setText("Forza Segnalazione");
		}
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
    
}
