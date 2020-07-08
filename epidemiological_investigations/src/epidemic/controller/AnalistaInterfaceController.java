package epidemic.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;
import com.gembox.spreadsheet.*;
import epidemic.model.CausaDecesso;
import epidemic.model.Comune;
import epidemic.model.Contagio;
import epidemic.model.Decesso;
import epidemic.model.Localita;
import epidemic.model.MalattiaContagiosa;
import epidemic.model.Provincia;
import epidemic.model.Regione;
import epidemic.model.SegnalazioneContagi;
import epidemic.model.SegnalazioneDecessi;
import epidemic.model.DAO.MySqlDAOFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

public class AnalistaInterfaceController {
    
	
	@FXML private ComboBox<Regione> comboRegione;
	@FXML private ComboBox<Provincia> comboProvincia;
	@FXML private ComboBox<Comune> comboComune;
	@FXML private ComboBox<String> comboLista;
	
	@FXML private ComboBox<MalattiaContagiosa> selezionaMalattia;
	@FXML private ComboBox<CausaDecesso> causaMorte;
    
	
    @FXML private CategoryAxis xLineChart;
    @FXML private NumberAxis yLineChart;
    @FXML private CategoryAxis xBarChart;
    @FXML private NumberAxis yBarChart;
	
    @FXML private ToggleGroup yAxys;
    @FXML private RadioButton rbRicoverati;
	@FXML private RadioButton rbInCura;
	
	@FXML private BarChart<?, ?> barChart;
	@FXML private LineChart<String, Number> lineChart;
	@FXML ToggleGroup chartGroup;
	
	@FXML private ComboBox<Regione> comboRegioneTabella;
	@FXML private ComboBox<Provincia> comboProvinciaTabella;
	@FXML private ComboBox<Comune> comboComuneTabella;
	
	@FXML private ComboBox<String> comboContagiTabella;
	@FXML private ComboBox<String> comboDecessiTabella;
	@FXML private ComboBox<String> comboStatoContagio;
	@FXML private TextField txtAnno;
	
	@FXML private TableView<DisplayData> tabellaDati;
	@FXML private TableColumn<DisplayData, Integer> colAnno;
	@FXML private TableColumn<DisplayData, Integer> colNumContagi;
	@FXML private TableColumn<DisplayData, String> colTipoContagio;
	@FXML private TableColumn<DisplayData, Integer> colNumDecessi;
	@FXML private TableColumn<DisplayData, String> colTipoDecesso;
	@FXML private TableColumn<DisplayData, Localita> colLocalita;
	
	private MySqlDAOFactory database;
	
	//liste generali contenenti tutti gli elementi
	private ObservableList<Contagio> listaContagio = FXCollections.observableArrayList();
	private ObservableList<Decesso> listaDecesso = FXCollections.observableArrayList();
	private ObservableList<Comune> listaComune = FXCollections.observableArrayList();
	private ObservableList<Provincia> listaProvincia = FXCollections.observableArrayList();
	private ObservableList<Regione> listaRegione = FXCollections.observableArrayList();	
	private ObservableList<SegnalazioneContagi> listasegnalazioneContagi = FXCollections.observableArrayList();
	private ObservableList<SegnalazioneDecessi> listasegnalazioneDecessi = FXCollections.observableArrayList();
	
	//liste generali contenenti solo gli elementi selezionati
	private ObservableList<Comune> selectedComune = FXCollections.observableArrayList();
	private ObservableList<Provincia> selectedProvincia = FXCollections.observableArrayList();
	private ObservableList<Regione> selectedRegione = FXCollections.observableArrayList();
	
	//costanti "tutti - regione/provincia/comune"
	private final static Regione tutteRegioni = new Regione("Tutte le regioni", 1, "Tutto");
	private final static Provincia tutteProvince = new Provincia("Tutte le province", 1, "Tutto", tutteRegioni);
	private final static Comune tuttiComuni = new Comune("Tutti i comuni", 0, "000000", null, null, true, null);
	private final static String tuttiDecessi = "TUTTI I DECESSI";
	private final static String tuttiContagi = "TUTTI I CONTAGI";
	private final static String pressoMedicoBase = "PRESSO MEDICO DI BASE";
	private final static String ricoverati = "RICOVERATI IN TERAPIA INTENSIVA";
	private final static String tuttiStatiContagio = "TUTTI";
	
	static {
        SpreadsheetInfo.setLicense("FREE-LIMITED-KEY");
    }
	
	/**
	 * Metodo di inizializzazione dell'interfaccia grafica
	 * 
	 * @throws IOException
	 */
	public void initialize() throws IOException {
		
		initTableCols();
		
		database = new MySqlDAOFactory();
    	
		//contagio
    	List<Contagio> contagio = database.getContagioDAO().getAll();
    	listaContagio.addAll(contagio);
    	
    	//decesso
    	List<Decesso> decesso = database.getDecessoDAO().getAll();
    	listaDecesso.addAll(decesso);
    	
    	//comune
    	List<Comune> comune = database.getComuneDAO().getAll();
   		listaComune.addAll(comune);
   		
   		//provincia
   		List<Provincia> provincia = database.getProvinciaDAO().getAll();
		listaProvincia.addAll(provincia);

   		//regione
    	List<Regione> regione = database.getRegioneDAO().getAll();
   		listaRegione.addAll(regione);	
    	  	
    	//segnalazioneContagi
    	List<SegnalazioneContagi> segnalazioneContagi = database.getSegnalazioneContagiDAO().getAll();
    	listasegnalazioneContagi.addAll(segnalazioneContagi);
    	
    	//segnalazioneDecessi
    	List<SegnalazioneDecessi> segnalazioneDecessi = database.getSegnalazioneDecessiDAO().getAll();
    	listasegnalazioneDecessi.addAll(segnalazioneDecessi);
    	
    	comboRegione.setItems(listaRegione);
    	comboProvincia.setItems(listaProvincia);
    	comboComune.setItems(listaComune);
    	
    	listaRegione.add(tutteRegioni);
    	comboRegioneTabella.setItems(listaRegione);
    	
    	//bindings
    	comboProvinciaTabella.disableProperty().bind(comboRegioneTabella.valueProperty().isNull()
    			.or(comboRegioneTabella.valueProperty().isEqualTo(tutteRegioni)));
    	comboComuneTabella.disableProperty().bind(comboProvinciaTabella.valueProperty().isNull()
    			.or(comboProvinciaTabella.disabledProperty())
    			.or(comboProvinciaTabella.valueProperty().isEqualTo(tutteProvince)));
    	
    	//inizializzazione combobox delle malattie contagiose
    	ObservableList<String> malattiaValues = FXCollections.observableArrayList();
    	malattiaValues.add(tuttiContagi);
    	for(MalattiaContagiosa m: MalattiaContagiosa.values())
    		malattiaValues.add(m.toString());
    	
    	//inizializzazione combobox delle cause di decesso
    	ObservableList<String> decessoValues = FXCollections.observableArrayList();
    	decessoValues.add(tuttiDecessi);
    	for(CausaDecesso d: CausaDecesso.values())
    		decessoValues.add(d.toString());
    	
    	comboContagiTabella.setItems(malattiaValues);
    	comboDecessiTabella.setItems(decessoValues);
    	
    	comboStatoContagio.getItems().add(pressoMedicoBase);
    	comboStatoContagio.getItems().add(ricoverati);
    	comboStatoContagio.getItems().add(tuttiStatiContagio);
    	
    	causaMorte.setItems(FXCollections.observableArrayList(CausaDecesso.values()));
		selezionaMalattia.setItems(FXCollections.observableArrayList(MalattiaContagiosa.values()));	
	}
	
	private void initTableCols() {
		colAnno.setCellValueFactory(new PropertyValueFactory<>("anno"));
		colLocalita.setCellValueFactory(new PropertyValueFactory<>("localita"));
		colNumContagi.setCellValueFactory(new PropertyValueFactory<>("numContagi"));
		colNumDecessi.setCellValueFactory(new PropertyValueFactory<>("numDecessi"));
		colTipoContagio.setCellValueFactory(new PropertyValueFactory<>("tipoContagio"));
		colTipoDecesso.setCellValueFactory(new PropertyValueFactory<>("tipoDecesso"));
	}

	@FXML
	public void loadProvince() {		
		ObservableList<Provincia> province = FXCollections.observableArrayList();
		province.add(tutteProvince);
		
		for(Provincia p: listaProvincia) {
			if(p.getRegioneAppartenenza().equals(comboRegioneTabella.getValue()))
				province.add(p);
		}
		comboProvinciaTabella.setItems(province);
	}
	
	@FXML
	public void loadComuni() {
		ObservableList<Comune> comuni = FXCollections.observableArrayList();
		comuni.add(tuttiComuni);
		
		for(Comune c: listaComune) {
			if(c.getProvinciaAppartenenza().equals(comboProvinciaTabella.getValue()))
				comuni.add(c);
		}
		comboComuneTabella.setItems(comuni);
	}
	
	@FXML
	public void fillTable() throws IOException {
		Integer annoSelezionato;
		try {
			annoSelezionato = Integer.parseInt(txtAnno.getText());
		} catch (NumberFormatException e) {
			txtAnno.clear();
			return;
		}
		
		Regione regSelezionata = comboRegioneTabella.getValue();
		Provincia provSelezionata = comboProvinciaTabella.getValue();
		Comune comuneSelezionato = comboComuneTabella.getValue();
		
		String malattiaSelezionata = comboContagiTabella.getValue();
		String causaDecessoSelezionata = comboDecessiTabella.getValue();
		String statoContagio = comboStatoContagio.getValue();
		
		ObservableList<DisplayData> datiTab = FXCollections.observableArrayList();
		
		if(regSelezionata.equals(tutteRegioni)) {
			for(Regione r: listaRegione) {
				int totContagi = 0;
				int totDecessi = 0;
				
				for(SegnalazioneContagi sc: listasegnalazioneContagi) {
					if(sc.getComuneRiferimento().getProvinciaAppartenenza().getRegioneAppartenenza().equals(r) && 
							yearCheck(sc.getData(), annoSelezionato)) 
						totContagi += getContagi(sc, statoContagio, malattiaSelezionata);
				}
				
				for(SegnalazioneDecessi sd: listasegnalazioneDecessi) {
					if(sd.getProvinciaRiferimento().getRegioneAppartenenza().equals(r) &&
							yearCheck(sd.getData(), annoSelezionato))
						totDecessi += getDecessi(sd, causaDecessoSelezionata);
				}
				
				datiTab.add(new DisplayData(annoSelezionato, r, totContagi, malattiaSelezionata, totDecessi, causaDecessoSelezionata));
				
			}
		}
		else {
			if(provSelezionata.equals(tutteProvince)) {
				for(Provincia p: listaProvincia) {
					if(p.getRegioneAppartenenza().equals(regSelezionata)) {
						int totContagi = 0;
						int totDecessi = 0;
						
						for(SegnalazioneContagi sc: listasegnalazioneContagi) {
							if(sc.getComuneRiferimento().getProvinciaAppartenenza().equals(p) && 
									yearCheck(sc.getData(), annoSelezionato)) 
								totContagi += getContagi(sc, statoContagio, malattiaSelezionata);
						}
						
						for(SegnalazioneDecessi sd: listasegnalazioneDecessi) {
							if(sd.getProvinciaRiferimento().equals(p) &&
									yearCheck(sd.getData(), annoSelezionato))
								totDecessi += getDecessi(sd, causaDecessoSelezionata);
						}
						
						datiTab.add(new DisplayData(annoSelezionato, p, totContagi, malattiaSelezionata, totDecessi, causaDecessoSelezionata));
						
					}
				}
			}
			else {
				if(comuneSelezionato.equals(tuttiComuni)) {
					for(Comune c: listaComune) {
						if(c.getProvinciaAppartenenza().equals(provSelezionata)) {
							int totContagi = 0;
							
							for(SegnalazioneContagi sc: listasegnalazioneContagi) {
								if(sc.getComuneRiferimento().equals(c) && 
										yearCheck(sc.getData(), annoSelezionato)) 
									totContagi += getContagi(sc, statoContagio, malattiaSelezionata);
							}
							
							datiTab.add(new DisplayData(annoSelezionato, c, totContagi, malattiaSelezionata, 0, null));
							
						}
					}
				}
				else {
					int totContagi = 0;
					for(SegnalazioneContagi sc: listasegnalazioneContagi) {
						if(sc.getComuneRiferimento().equals(comuneSelezionato) && 
								yearCheck(sc.getData(), annoSelezionato)) 
							totContagi += getContagi(sc, statoContagio, malattiaSelezionata);
					}

					datiTab.add(new DisplayData(annoSelezionato, comuneSelezionato, totContagi, malattiaSelezionata, 0, null));
				}
			}
		}
		tabellaDati.setItems(datiTab);
	}
	
	private boolean yearCheck(Date dataSegnalazione, int annoSelezionato) throws IOException {
		Calendar cal1 = Calendar.getInstance();
		
		cal1.setTime(dataSegnalazione);
		if(cal1.get(Calendar.YEAR) != annoSelezionato)
			return false;
		return true;
	}
	
	private int getDecessi(SegnalazioneDecessi sd, String causaDecessoSelezionata) {
		int totDecessi = 0;
		for(Decesso d: sd.getDecessi()) {
			if(causaDecessoSelezionata != null && causaDecessoSelezionata != tuttiDecessi 
					&& !d.getCausa().toString().equals(causaDecessoSelezionata))
				continue;
			
			totDecessi += d.getNumero();
		}
		
		return totDecessi;
	}

	private int getContagi(SegnalazioneContagi sc, String statoContagio, String malattiaSelezionata) {
		int totContagi = 0;
		for(Contagio c: sc.getContagi()) {
			if(malattiaSelezionata != null && malattiaSelezionata != tuttiContagi 
					&& !c.getMalattia().toString().equals(malattiaSelezionata))
				continue;
			if(statoContagio.equals(tuttiStatiContagio))
				totContagi += c.getPersoneInCura() + c.getPersoneRicoverate();
			else if (statoContagio.equals(pressoMedicoBase))
				totContagi += c.getPersoneInCura();
			else if (statoContagio.equals(ricoverati))
				totContagi += c.getPersoneRicoverate();
		}
		return totContagi;
	}

	@FXML
	public void export() throws IOException {
		ExcelFile file = new ExcelFile();
        ExcelWorksheet worksheet = file.addWorksheet("sheet");
        
        for(int column = 0; column < tabellaDati.getColumns().size(); column++)
            worksheet.getCell(0, column).setValue(tabellaDati.getColumns().get(column).getText());
        
        for (int row = 1; row < tabellaDati.getItems().size(); row++) {
            List<String> cells = new ArrayList<String>();
            
            DisplayData dati = tabellaDati.getItems().get(row - 1);
            
            cells.add(String.valueOf(dati.getAnno()));
            cells.add(dati.getLocalita().toString());
            cells.add(String.valueOf(dati.getNumContagi()));
            cells.add(dati.getTipoContagio());
            cells.add(String.valueOf(dati.getNumDecessi()));
            cells.add(dati.getTipoDecesso());
            
            for (int column = 0; column < cells.size(); column++) {
                if (cells.get(column) != null)
                    worksheet.getCell(row, column).setValue(cells.get(column).toString());
            }
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx"),
                new FileChooser.ExtensionFilter("XLS files (*.xls)", "*.xls"),
                new FileChooser.ExtensionFilter("ODS files (*.ods)", "*.ods"),
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"),
                new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html")
        );
        File saveFile = fileChooser.showSaveDialog(tabellaDati.getScene().getWindow());

        file.save(saveFile.getAbsolutePath());
	}
	
	public void fillProvincia(ActionEvent event) {		//se selezioni provincia e poi regione da errore (quindi if == null seleziona regione ?)
		
		ObservableList<Provincia> province = FXCollections.observableArrayList();
		ObservableList<Comune>  comuni = FXCollections.observableArrayList();
		
		System.out.println(comboRegione.getValue());
		for(Provincia i: listaProvincia) {
			if(i.getRegioneAppartenenza().equals(comboRegione.getValue())) {
				province.add(i);
				for (Comune j : listaComune) {
					if (j.getProvinciaAppartenenza().equals(i)) {
						comuni.add(j);
					}
						
				}
			}
		}
		
		comboProvincia.setItems(province);
		comboComune.setItems(comuni);
		
	}
	
	public void fillComune(ActionEvent event) {
		
		ObservableList<Comune>  comuni = FXCollections.observableArrayList();
		
		System.out.println(comboProvincia.getValue().getNome()); 
    	
    	System.out.println();
		for(Comune i: listaComune) {
			if(i.getProvinciaAppartenenza().equals(comboProvincia.getValue())) {
				comuni.add(i);
			}
		}
		comboComune.setItems(comuni);
	}
	
	
	@FXML
	void insertLuogo(ActionEvent event) {

		if(comboRegione.getValue() == null && comboProvincia.getValue() == null &&  comboComune.getValue() == null) {
			selectedRegione.addAll(listaRegione);
			
			System.out.println("printa tutte le regioni");
			lineChart();
		}
			
		else if(comboProvincia.getValue() == null &&  comboComune.getValue() == null) {
			if (selectedRegione.contains(comboRegione.getValue())){
				System.out.println("hai gia questa regione");
			}
			else {
				selectedRegione.add(comboRegione.getValue());				
				lineChart();
			}
		}
		
		else if(comboComune.getValue() == null) {
			if (selectedProvincia.contains(comboProvincia.getValue())){
				System.out.println("hai gia questa provincia");
			}
			else {
				selectedProvincia.add(comboProvincia.getValue());
				lineChart();
			}
		}
		
		else {
			if (selectedComune.contains(comboComune.getValue())){
				System.out.println("hai gia questo comune");
			}
			else {
				selectedComune.add(comboComune.getValue());
				lineChart();
			}
		}
		
		comboRegione.setItems(listaRegione);
		comboProvincia.setItems(listaProvincia);
		comboComune.setItems(listaComune);
	
		fillLista();	//serve per eliminare
		
	}
	
	@FXML
    public void updateGraph(ActionEvent event) {
//		System.out.println("updateGraph\n");
//		
		System.out.println(selezionaMalattia.getValue());
	
		System.out.println(causaMorte.getValue());
		
		
		lineChart();
		
		comboRegione.setItems(listaRegione);
		comboProvincia.setItems(listaProvincia);
		comboComune.setItems(listaComune);
//		
//		for(Contagio contagi: listaContagio) {
//			if(contagi.getMalattia() == selezionaMalattia.getValue()) {
//				System.out.println(contagi.getMalattia() + " è uguale a " + selezionaMalattia.getValue());
//			}
//		}
		 
	}
	
	
	private void lineChart() {	//per ora è data normale, non anno
		
		
		
		lineChart.getData().clear();
		//lineChart.setAnimated(false);									//si vuole l'animazione?
		
		XYChart.Series<String, Number> serieContagio = null;
		XYChart.Series<String, Number> serieDecesso = null;
		
		TreeMap <String, Integer> casiContagio = new TreeMap<String,Integer>();
		TreeMap <String, Integer> casiDecesso = new TreeMap<String,Integer>();
		
		
		if (!selectedComune.isEmpty()) {
			
			int totmalattie = 0;
			
			for(Comune i: selectedComune) {
				serieContagio = new XYChart.Series<String, Number>();
				serieContagio.setName(i.getNome());
	    		
	    		for(SegnalazioneContagi segna: listasegnalazioneContagi) {		//i.getSegnalazioniContagi() da problemi (NullPointerException)
	    			if(segna.getComuneRiferimento().equals(i)) {				//soluzione alternativa...
	    				totmalattie = 0;
	    				for(Contagio contagi: segna.getContagi()) {
	    					if(contagi.getMalattia() == selezionaMalattia.getValue()) {
	    						if (rbRicoverati.isSelected()) {
		    						yLineChart.setLabel("Persone Ricoverate");
									totmalattie = totmalattie + contagi.getPersoneRicoverate();
								}
								else if (rbInCura.isSelected()) {
									yLineChart.setLabel("Persone in Cura");
									totmalattie = totmalattie + contagi.getPersoneInCura();
								}
	    					}
			
						}
	    				serieContagio.getData().add(new XYChart.Data<String, Number>(segna.getData().toString(), totmalattie));
					}		
				}
	    		lineChart.getData().add(serieContagio);
	    	}
		}
		
		
		if (!selectedProvincia.isEmpty()){
			
			int totmalattie = 0;
			int totdecessi = 0;
			
			for(Provincia j: selectedProvincia) {	//inserisce dati nel grafico a linee (per ora tutti)
				casiContagio.clear();
				
				serieContagio = new XYChart.Series<String, Number>();
				
				if(!selectedComune.isEmpty())
					serieContagio.setName(j.getNome() + " (Prov.)");
				else
					serieContagio.setName(j.getNome());
				
				for(SegnalazioneContagi segnaC: listasegnalazioneContagi) {		//i.getSegnalazioniContagi() da problemi (NullPointerException)
					totmalattie = 0;
					
					for(Comune i: listaComune) {
						if(segnaC.getComuneRiferimento().equals(i)) {				//soluzione alternativa...
							if (i.getProvinciaAppartenenza().equals(j)) {
							
								for(Contagio contagi: segnaC.getContagi()) {
									if(contagi.getMalattia() == selezionaMalattia.getValue()) {
										if (rbRicoverati.isSelected()) {
											yLineChart.setLabel("Persone Ricoverate");
											totmalattie = totmalattie + contagi.getPersoneRicoverate();
										
										}
										else if (rbInCura.isSelected()) {
											yLineChart.setLabel("Persone in Cura");
											totmalattie = totmalattie + contagi.getPersoneInCura();
										}
									}
								}
							}
						}	
					}
					if ((casiContagio.get(segnaC.getData().toString()) == null) ) {			//perche se aggiungo && (totmalattie != 0) viene null pointer??
						casiContagio.put(segnaC.getData().toString(), totmalattie);
					}
					else {
						int valore = casiContagio.get(segnaC.getData().toString()).intValue();
						totmalattie = totmalattie + valore;
						casiContagio.put(segnaC.getData().toString(), totmalattie);
					}
				}
				
				for (String i: casiContagio.keySet()) {
					serieContagio.getData().add(new XYChart.Data<String, Number>(i, casiContagio.get(i)));
				}

				
				
				serieDecesso = new XYChart.Series<String, Number>();
				serieDecesso.setName(j.getNome() + " Deceduti");
				
				for(SegnalazioneDecessi segnaD: listasegnalazioneDecessi) {
	    			if(segnaD.getProvinciaRiferimento().equals(j)) {
	    				totdecessi = 0;
	    				for(Decesso decessi: segnaD.getDecessi()) {
	    					if(decessi.getCausa() == causaMorte.getValue()) {
	    						totdecessi = totdecessi + decessi.getNumero();
	    					}
	    					
	    					
	    				}
	    				serieDecesso.getData().add(new XYChart.Data<String, Number>(segnaD.getData().toString(), totdecessi));
	    				
	    			}
	    				
	    			
	    		}
				lineChart.getData().addAll(serieContagio,serieDecesso);		//rischia di dare problemi con l'add seriecontagio di prima??
			}
		}
		
		if (!selectedRegione.isEmpty()){
			int totmalattie = 0;
			int totdecessi = 0;
			
			for(Regione z: selectedRegione) {
				casiContagio.clear();
				serieContagio = new XYChart.Series<String, Number>();
				serieContagio.setName(z.getNome());
				for(Provincia j: listaProvincia) {	//inserisce dati nel grafico a linee (per ora tutti)
					if (j.getRegioneAppartenenza().equals(z)) {
						
						
						for(SegnalazioneContagi segnaC: listasegnalazioneContagi) {		//i.getSegnalazioniContagi() da problemi (NullPointerException)
							totmalattie = 0;
							for(Comune i: listaComune) {
							
								if(segnaC.getComuneRiferimento().equals(i)) {				//soluzione alternativa...
									if (i.getProvinciaAppartenenza().equals(j)) {
									
										for(Contagio contagi: segnaC.getContagi()) {
											if(contagi.getMalattia() == selezionaMalattia.getValue() || causaMorte.getValue() == null) {
												if (rbRicoverati.isSelected()) {
													yLineChart.setLabel("Persone Ricoverate");
													totmalattie = totmalattie + contagi.getPersoneRicoverate();
												
												}
												else if (rbInCura.isSelected()) {
													yLineChart.setLabel("Persone in Cura");
													totmalattie = totmalattie + contagi.getPersoneInCura();
												}
											}
										}
		
									}
								}	
							}
							if ((casiContagio.get(segnaC.getData().toString()) == null) ) {			//perche se aggiungo && (totmalattie != 0) viene null pointer??
								casiContagio.put(segnaC.getData().toString(), totmalattie);
							}
							else {
								int valore = casiContagio.get(segnaC.getData().toString()).intValue();
								totmalattie = totmalattie + valore;
								casiContagio.put(segnaC.getData().toString(), totmalattie);
							}
						}
		    		}
					
				}
				for (String i: casiContagio.keySet()) {
					serieContagio.getData().add(new XYChart.Data<String, Number>(i, casiContagio.get(i)));
				}
				
				serieDecesso = new XYChart.Series<String, Number>();
				serieDecesso.setName(z.getNome() + " Deceduti");
				
				for(Provincia j: listaProvincia) {	//inserisce dati nel grafico a linee (per ora tutti)
					if (j.getRegioneAppartenenza().equals(z)) {
						
						
						for(SegnalazioneDecessi segnaD: listasegnalazioneDecessi) {
			    			if(segnaD.getProvinciaRiferimento().equals(j)) {
			    				totdecessi = 0;
			    				for(Decesso decessi: segnaD.getDecessi()) {
			    					if(decessi.getCausa() == causaMorte.getValue() || causaMorte.getValue() == null) {
			    						totdecessi = totdecessi + decessi.getNumero();
			    					}
			    				}
			    				
			    				if ((casiDecesso.get(segnaD.getData().toString()) == null) ) {			//perche se aggiungo && (totmalattie != 0) viene null pointer??
									casiDecesso.put(segnaD.getData().toString(), totdecessi);
								}
								else {
									int valore = casiDecesso.get(segnaD.getData().toString()).intValue();
									totdecessi = totdecessi + valore;
									casiDecesso.put(segnaD.getData().toString(), totdecessi);
								}
			    			}
						
						}
					}
				}
				for (String i: casiDecesso.keySet()) {
					
					serieDecesso.getData().add(new XYChart.Data<String, Number>(i, casiDecesso.get(i)));
					System.out.println("persone morte sono " + casiDecesso.get(i));
				}
				
				
				lineChart.getData().addAll(serieContagio,serieDecesso);
//				lineChart.getData().add(serieContagio);
				
				
			}
		}
		
//		lineChart.getData().remove(arg0)			maybe in futuro
			
	}
	
	
	//per convertire il n di persone in cura/contagi/morti in annuale
	private int yearlyNumber() {	
		return 0;			
	}
	
	
	@FXML
	void delete(ActionEvent event) {
		
		selectedComune.clear();
		selectedProvincia.clear();
		selectedRegione.clear();
		 
		lineChart.getData().clear();
		barChart.getData().clear();
	 }

	void fillLista() {
		
		ObservableList<String> lista = FXCollections.observableArrayList();
		
		if(!selectedComune.isEmpty()) {
			for(Comune i : selectedComune)
				lista.add(i.getNome());
			comboLista.setItems(lista);
		}
		if(!selectedProvincia.isEmpty()) {
			for(Provincia i : selectedProvincia)
				lista.add(i.getNome());
			comboLista.setItems(lista);
		}
		if(!selectedRegione.isEmpty()) {
			for(Regione i : selectedRegione)
				lista.add(i.getNome());
			comboLista.setItems(lista);	
		
		}
	}
	
	@FXML
	void deleteCampo(ActionEvent event) {

		Comune deleteC = null;
		Provincia deleteP = null;
		Regione deleteR = null;
		
		if(!selectedComune.isEmpty()) {
			for(Comune i : selectedComune) {
				if (i.getNome().equals(comboLista.getValue())) {
					deleteC = i;
				}
			}
			selectedComune.remove(deleteC);
		}
		
		if(!selectedProvincia.isEmpty()) {
			for(Provincia i : selectedProvincia) {
				if (i.getNome().equals(comboLista.getValue())) {
					deleteP = i;
				}
			}
			selectedProvincia.remove(deleteP);
		}
		
		if(!selectedRegione.isEmpty()) {	
			for(Regione i : selectedRegione) {
				if (i.getNome().equals(comboLista.getValue())) {
					deleteR = i;
				}
			}
			selectedRegione.remove(deleteR);
		}
		
		fillLista();
		lineChart();
		//barChart();
		
		
		
	}
	
	public class DisplayData {
		int anno, numContagi, numDecessi;
		private Localita localita;
		private String tipoContagio, tipoDecesso;
		
		public DisplayData(int anno, Localita localita, int numContagi, String tipoContagio, int numDecessi, String tipoDecesso){
			this.anno = anno;
			this.localita = localita;
			this.numContagi = numContagi;
			this.tipoContagio = tipoContagio;
			this.numDecessi = numDecessi;
			this.tipoDecesso = tipoDecesso;
		}
		
		public int getAnno() {
			return anno;
		}
		
		public int getNumContagi() {
			return numContagi;
		}
		
		public Localita getLocalita() {
			return localita;
		}
		
		public String getTipoDecesso() {
			return tipoDecesso;
		}
		
		public int getNumDecessi() {
			return numDecessi;
		}
		
		public String getTipoContagio() {
			return tipoContagio;
		}
		
	}
}
