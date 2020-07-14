package epidemic.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import epidemic.model.Comune;
import epidemic.model.Contratto;
import epidemic.model.Provincia;
import epidemic.model.Territorio;
import epidemic.model.DAO.MySqlDAOFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


/**
 * Finestra per l'inserimento e la modifica dei dati relativi ai comuni
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class ComuneEditDialogController {
	
	private Stage dialogStage;
    private Comune comune;
    private boolean salvaClicked = false;
    private Provincia provincia;
    ObservableList<Contratto> listaUtentiContratto = FXCollections.observableArrayList();
	
    @FXML
    private TextField textNome;
    @FXML
    private TextField textIstat;
    @FXML
    private ComboBox<Territorio> comboTerritorio;
    @FXML
    private ComboBox<Contratto> comboResponsabile;
    @FXML
    private Spinner<Double> spinSuperficie;
    @FXML
    private DatePicker dateIstituzione;
    @FXML
    private CheckBox checkMare;
    
    
    
    /**
     * Inizializza il controller. Questo metodo viene invocato automaticamente 
     * dopo che il file fxml è stato caricato
     */
    @FXML
    private void initialize() throws IOException {
    	comboTerritorio.getItems().addAll(Territorio.values());
    	spinSuperficie.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, 0.0, 0.1));
    	
    	MySqlDAOFactory database = new MySqlDAOFactory();
    	listaUtentiContratto.addAll(database.getUtenteDAO().getContratto());
    	comboResponsabile.setItems(listaUtentiContratto);
}

    
    /**
     * Imposta lo stage per questo dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    /**
     * Ritorna il comune modificato
     * 
     * @return il comune modificato
     */
    public Comune getComune() {
    	return comune;
    }
    
    
    /**
     * Imposta nei campi i dati del comune da modificare.
     * 
     * @param comune comune da modificare, null se voglio creare una nuovo comune
     * @param provincia la provincia a cui appartiene il comune
     */
    public void setComune(Comune comune, Provincia provincia) {
    	if(comune != null) {
            this.comune = comune;
            textNome.setText(comune.getNome());
            textIstat.setText(comune.getIstat());
            comboTerritorio.setValue(comune.getTerritorio());
            spinSuperficie.getValueFactory().setValue(comune.getSuperficie());
            dateIstituzione.setValue(comune.getDataIstituzione().toLocalDate());
            checkMare.setSelected(comune.getSulMare());
            comboResponsabile.setValue(comune.getResponsabile());
    	}
    	
    	this.provincia = provincia;
    }
    
    
    /**
     * Ritorna true se l'utente ha premuto il pulsante Salva, falso altrimenti
     *  
     * @return
     */
    public boolean isSalvaClicked() {
        return salvaClicked;
    }
    
    
    /**
     * Salva i nuovi dati del comune
     */
    @FXML
    private void handleSalva() {
        if (isInputValid()) {
        	if(comune != null) {
        		comune.setNome(textNome.getText());
        		comune.setSuperficie(spinSuperficie.getValue());
        		comune.setIstat(textIstat.getText());
        		comune.setDataIstituzione(Date.valueOf(dateIstituzione.getValue()));
        		comune.setTerritorio(comboTerritorio.getValue());
        		comune.setSulMare(checkMare.isSelected());
        		comune.setResponsabile((Contratto)comboResponsabile.getValue());
        	} else {
        		comune = new Comune(textNome.getText(), spinSuperficie.getValue(), textIstat.getText(), Date.valueOf(dateIstituzione.getValue()), comboTerritorio.getValue(), checkMare.isSelected(), provincia);
        		comune.setResponsabile((Contratto)comboResponsabile.getValue());
        	}
        	
            salvaClicked = true;
            dialogStage.close();
        }
    }
    
    
    /**
     * Annulla l'inserimento e chiude la finestra
     */
    @FXML
    private void handleAnnulla() {
        dialogStage.close();
    }


    /**
     * Verifica che i dati di input passati ai campi siano validi
     * 
     * @return true se gli input sono validi, falso altrimenti
     */
	private boolean isInputValid() {
		String errorMessage = "";
		
		 if (textNome.getText() == null || textNome.getText().length() == 0 || textNome.getText().matches(".*\\d+.*"))
	            errorMessage += "Nome non valido\n"; 

		 if (textIstat.getText() == null || textIstat.getText().length() != 6 || !textIstat.getText().chars().allMatch(c -> Character.isDigit(c)))
	            errorMessage += "Codice ISTAT non valido\n"; 
		 
        if (spinSuperficie.getValue() == null || spinSuperficie.getValue() <= 0)
            errorMessage += "Superficie non valida!\n"; 
        
        if (dateIstituzione.getValue() == null || dateIstituzione.getValue().isAfter(LocalDate.now()))
            errorMessage += "Data di istituzione non valida!\n"; 
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
        	Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Campi non validi");
            alert.setHeaderText("Correggere i campi non validi");
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
            
            return false;
        }
	}
    
    

}
