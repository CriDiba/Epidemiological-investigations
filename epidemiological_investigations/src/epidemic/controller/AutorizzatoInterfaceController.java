package epidemic.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import epidemic.model.Comune;
import epidemic.model.Contratto;
import epidemic.model.Provincia;
import epidemic.model.Regione;
import epidemic.model.Ruolo;
import epidemic.model.Territorio;
import epidemic.model.Utente;
import epidemic.model.DAO.MySqlDAOFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AutorizzatoInterfaceController implements Initializable {
	
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
    private TableColumn<Comune, String> colComResposabile;
    
    
    @FXML
    private TextField textRegNome;
    @FXML
    private TextField textRegSuperficie;
    @FXML
    private TextField textRegCapoluogo;
    @FXML
    private ComboBox<Regione> comboRegione;
    
	private MySqlDAOFactory database;
	private ObservableList<Regione> listaRegioni = FXCollections.observableArrayList();
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Inizializzo colonne Tabelle
		initTableProvinceCols();
		initTableComuniCols();
		
		//Imposto comboRegione
		try {
			listaRegioni.addAll(database.getRegioneDAO().getAll());
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		comboRegione.setItems(listaRegioni);
		
	}





	private void initTableProvinceCols() {
		colProvNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		colProvSuperficie.setCellValueFactory(new PropertyValueFactory<>("superficie"));
		colProvCapoluogo.setCellValueFactory(new PropertyValueFactory<>("capoluogo"));
	}
	
	private void initTableComuniCols() {
		colComNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		colComSuperficie.setCellValueFactory(new PropertyValueFactory<>("superficie"));
		colComISTAT.setCellValueFactory(new PropertyValueFactory<>("istat"));
		colComIstituzione.setCellValueFactory(new PropertyValueFactory<>("dataIstituzione"));
		colComTerritorio.setCellValueFactory(new PropertyValueFactory<>("territorio"));
		colComMare.setCellValueFactory(new PropertyValueFactory<>("sulMare"));
		colComResposabile.setCellValueFactory(new PropertyValueFactory<>("responsabile"));
	}
    
    

    

}
