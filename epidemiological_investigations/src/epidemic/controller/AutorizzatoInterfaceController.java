package epidemic.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import epidemic.model.Comune;
import epidemic.model.Provincia;
import epidemic.model.Regione;
import epidemic.model.Territorio;
import epidemic.model.DAO.MySqlDAOFactory;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    private TextField textRegNome;
    @FXML
    private Spinner<Double> spinRegSuperficie;
    @FXML
    private TextField textRegCapoluogo;
    @FXML
    private ComboBox<Regione> comboRegione;
    
    @FXML
	private ButtonBar btnProvincia;
    @FXML
	private ButtonBar btnComune;
    @FXML
    private Button btnAddRegione;
    @FXML
    private Button btnEditRegione;
    @FXML
    private Button btnDeleteRegione;
    
	private MySqlDAOFactory database;
	private ObservableList<Regione> listaRegioni = FXCollections.observableArrayList();
	private ObservableList<Provincia> listaProvince = FXCollections.observableArrayList();
	private ObservableList<Comune> listaComuni = FXCollections.observableArrayList();
	
	private Regione regioneSelezionata;
	private Provincia provinciaSelezionata;
	private Comune comuneSelezionato;
	
	boolean insertRegioneMode = false;
    
    
	/**
     * Inizializza il controller. Questo metodo viene invocato automaticamente 
     * dopo che il file fxml è stato caricato
	 */
	@FXML
	public void initialize() {

		initTableProvinceCols();
		initTableComuniCols();
		spinRegSuperficie.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, 0.0, 0.1));
		
		database = new MySqlDAOFactory();
		try {
			listaRegioni.addAll(database.getRegioneDAO().getAll());
		} catch (IOException e) {
			e.printStackTrace();
		}

		comboRegione.setItems(listaRegioni);
		btnComune.setDisable(true);
		btnProvincia.setDisable(true);
	}

	
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
				database.getRegioneDAO().create(nuovaRegione);
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
	        database.getProvinciaDAO().create(provinciaSelezionata);
	        tableProvince.getItems().add(provinciaSelezionata);
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
	        database.getComuneDAO().create(comuneSelezionato);
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
	 * 
	 * @param messaggio
	 * @return
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
	 * 
	 * @param messaggio
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
		
		 if (textRegNome.getText() == null || textRegNome.getText().length() == 0)
	            errorMessage += "Nome non valido\n"; 
		 if (textRegCapoluogo.getText() == null || textRegCapoluogo.getText().length() == 0)
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
    
}
