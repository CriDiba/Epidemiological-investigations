package epidemic.controller;

import epidemic.model.Provincia;
import epidemic.model.Regione;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Finestra per l'inserimento e la modifica dei dati relativi alle province
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class ProvinciaEditDialogController {
	
	private Stage dialogStage;
    private Provincia provincia;
    private boolean salvaClicked = false;
    private Regione regione;
    
    @FXML
    private TextField textNome;
    @FXML
    private Spinner<Double> spinSuperficie;
    @FXML
    private TextField textCapoluogo;
    
    /**
     * Inizializza il controller. Questo metodo viene invocato automaticamente 
     * dopo che il file fxml è stato caricato
     */
    @FXML
    private void initialize() {
    	spinSuperficie.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, 0.0, 0.1));
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
     * Ritorna la provincia modificata
     * 
     * @return la provincia modificata
     */
    public Provincia getProvincia() {
    	return provincia;
    }
    
    
    /**
     * Imposta nei campi i dati della provincia da modificare.
     * 
     * @param provincia provincia da modificare, null se voglio creare una nuova provincia
     * @param regione regione a cui appartiene la provincia
     */
    public void setProvincia(Provincia provincia, Regione regione) {
    	if(provincia != null) {
            this.provincia = provincia;
            textNome.setText(provincia.getNome());
            spinSuperficie.getValueFactory().setValue(provincia.getSuperficie());
            textCapoluogo.setText(provincia.getCapoluogo());
    	}
    	
    	this.regione = regione;
    }
    
    
    /**
     * Ritorna true se l'utente ha premuto il pulsante Salva, falso altrimenti
     * 
     * @return true se l'utente ha premuto il pulsante Salva
     */
    public boolean isSalvaClicked() {
        return salvaClicked;
    }
    
    
    /**
     * Salva i nuovi dati della provincia
     */
    @FXML
    private void handleSalva() {
        if (isInputValid()) {
        	if(provincia != null) {
        		provincia.setNome(textNome.getText());
            	provincia.setSuperficie(spinSuperficie.getValue());
            	provincia.setCapoluogo(textCapoluogo.getText());
        	} else {
        		provincia = new Provincia(textNome.getText(), spinSuperficie.getValue(), textCapoluogo.getText(), regione);
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

		 if (textCapoluogo.getText() == null || textCapoluogo.getText().length() == 0 || textCapoluogo.getText().matches(".*\\d+.*"))
	            errorMessage += "Capoluogo non valido\n"; 
		 
        if (spinSuperficie.getValue() == null || spinSuperficie.getValue() <= 0)
            errorMessage += "Superficie non valida!\n"; 
                
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
