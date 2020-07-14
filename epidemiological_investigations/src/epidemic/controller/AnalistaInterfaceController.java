package epidemic.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

public class AnalistaInterfaceController {
    
	
	@FXML private ComboBox<Regione> comboRegioneGrafico;
	@FXML private ComboBox<Provincia> comboProvinciaGrafico;
	@FXML private ComboBox<Comune> comboComuneGrafico;
	@FXML private ComboBox<String> comboLista;
	
	@FXML private ComboBox<String> comboContagiGrafico;
	@FXML private ComboBox<String> comboDecessiGrafico;
	@FXML private ComboBox<String> comboStatoContagioGrafico;
	@FXML private CheckBox raggruppaAnno;
	
	@FXML private TextField selectAnnoIniz;
	@FXML private TextField selectAnnoFinal;
	
    @FXML private NumberAxis xLineChart;
    @FXML private NumberAxis yLineChart;
    @FXML private CategoryAxis xBarChart;
    @FXML private NumberAxis yBarChart;
    
	
	@FXML private BarChart<String, Number> barChart;
	@FXML private LineChart<Long, Number> lineChart;
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
	private final static String nessunDecesso = "NESSUNO DECESSO";
	private final static String tuttiContagi = "TUTTI I CONTAGI";
	private final static String pressoMedicoBase = "PRESSO MEDICO DI BASE";
	private final static String ricoverati = "RICOVERATI IN TERAPIA INTENSIVA";
	private final static String tuttiStatiContagio = "TUTTI";

	//String formatter for xAxis
	StringConverter<Number> stringConverter = new StringConverter<Number>() {

		@Override
		public Number fromString(String arg0) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(formatter.parse(arg0));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return calendar.getTimeInMillis();
		}

		@Override
		public String toString(Number arg0) {
			Date date = new Date(arg0.longValue());
			return date.toString();
		}

		
	};
	
	//
	private long lowerBound = Long.MAX_VALUE;
	private long upperBound = 0;
	
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
		
		xLineChart.setTickLabelFormatter(stringConverter);
		
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
    	
    	comboRegioneGrafico.setItems(listaRegione);
    	comboProvinciaGrafico.setItems(listaProvincia);
    	comboComuneGrafico.setItems(listaComune);
    	
    	listaRegione.add(tutteRegioni);
    	comboRegioneTabella.setItems(listaRegione);
    	
    	//bindings
    	comboProvinciaTabella.disableProperty().bind(comboRegioneTabella.valueProperty().isNull()
    			.or(comboRegioneTabella.valueProperty().isEqualTo(tutteRegioni)));
    	comboComuneTabella.disableProperty().bind(comboProvinciaTabella.valueProperty().isNull()
    			.or(comboProvinciaTabella.disabledProperty())
    			.or(comboProvinciaTabella.valueProperty().isEqualTo(tutteProvince)));
    	
    	comboProvinciaGrafico.disableProperty().bind(comboRegioneGrafico.valueProperty().isNull()
    			.or(comboRegioneGrafico.valueProperty().isEqualTo(tutteRegioni)));
    	comboComuneGrafico.disableProperty().bind(comboProvinciaGrafico.valueProperty().isNull()
    			.or(comboProvinciaGrafico.disabledProperty())
    			.or(comboProvinciaGrafico.valueProperty().isEqualTo(tutteProvince)));
    	
    	//inizializzazione combobox delle malattie contagiose
    	ObservableList<String> malattiaValues = FXCollections.observableArrayList();
    	malattiaValues.add(tuttiContagi);
    	for(MalattiaContagiosa m: MalattiaContagiosa.values())
    		malattiaValues.add(m.toString());
    	
    	//inizializzazione combobox delle cause di decesso
    	ObservableList<String> decessoValues = FXCollections.observableArrayList();
    	decessoValues.add(tuttiDecessi);
    	decessoValues.add(nessunDecesso);
    	for(CausaDecesso d: CausaDecesso.values())
    		decessoValues.add(d.toString());
    	
    	comboContagiTabella.setItems(malattiaValues);
    	comboDecessiTabella.setItems(decessoValues);
    	
    	comboStatoContagio.getItems().add(pressoMedicoBase);
    	comboStatoContagio.getItems().add(ricoverati);
    	comboStatoContagio.getItems().add(tuttiStatiContagio);
    	
    	comboDecessiGrafico.setItems(decessoValues);
    	comboContagiGrafico.setItems(malattiaValues);
    	
    	comboStatoContagioGrafico.getItems().add(pressoMedicoBase);
    	comboStatoContagioGrafico.getItems().add(ricoverati);
    	comboStatoContagioGrafico.getItems().add(tuttiStatiContagio);
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
		comboProvinciaTabella.setValue(tutteProvince);
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
		comboComuneTabella.setValue(tuttiComuni);
	}

	@FXML
	public void loadProvinceGrafico() {	
		ObservableList<Provincia> province = FXCollections.observableArrayList();
		province.add(tutteProvince);
		
		for(Provincia p: listaProvincia) {
			if(p.getRegioneAppartenenza().equals(comboRegioneGrafico.getValue()))
				province.add(p);
		}
		comboProvinciaGrafico.setItems(province);
	}
	
	@FXML
	public void loadComuniGrafico() {
		ObservableList<Comune> comuni = FXCollections.observableArrayList();
		comuni.add(tuttiComuni);
		for(Comune c: listaComune) {
			if(c.getProvinciaAppartenenza().equals(comboProvinciaGrafico.getValue()))
				comuni.add(c);
		}
		comboComuneGrafico.setItems(comuni);
	}
	
	@FXML
	public void fillTable() throws IOException {
		Integer annoSelezionato;
		try {
			annoSelezionato = Integer.parseInt(txtAnno.getText());
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Selezionare un anno valido!");
			alert.showAndWait();
			txtAnno.clear();
			return;
		}
		
		Regione regSelezionata = comboRegioneTabella.getValue();
		Provincia provSelezionata = comboProvinciaTabella.getValue();
		Comune comuneSelezionato = comboComuneTabella.getValue();
		
		String malattiaSelezionata = comboContagiTabella.getValue();
		String causaDecessoSelezionata = comboDecessiTabella.getValue();
		String statoContagio = comboStatoContagio.getValue();
		
		if(malattiaSelezionata == null || causaDecessoSelezionata == null || statoContagio == null || regSelezionata == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Le combobox devono essere riempite!");
			alert.showAndWait();
			return;
		}
		
		ObservableList<DisplayData> datiTab = FXCollections.observableArrayList();
		
		if(regSelezionata.equals(tutteRegioni)) {
			for(Regione r: listaRegione) {
				
				if(r.equals(tutteRegioni))
					continue;
				
				int totContagi = 0;
				int totDecessi = 0;
				
				for(SegnalazioneContagi sc: listasegnalazioneContagi) {
					if(sc.getComuneRiferimento().getProvinciaAppartenenza().getRegioneAppartenenza().equals(r) && 
							yearCheck(sc.getData(), annoSelezionato) == 0) 
						totContagi += getContagi(sc, statoContagio, malattiaSelezionata);
				}
				
				for(SegnalazioneDecessi sd: listasegnalazioneDecessi) {
					if(sd.getProvinciaRiferimento().getRegioneAppartenenza().equals(r) &&
							yearCheck(sd.getData(), annoSelezionato) == 0)
						totDecessi += getDecessi(sd, causaDecessoSelezionata);
				}
				
				datiTab.add(new DisplayData(annoSelezionato, r, totContagi, malattiaSelezionata, totDecessi, causaDecessoSelezionata));
				
			}
		}
		else {
			if(provSelezionata.equals(tutteProvince)) {
				for(Provincia p: listaProvincia) {
					if(p.equals(tutteProvince))
						continue;
					
					if(p.getRegioneAppartenenza().equals(regSelezionata)) {
						int totContagi = 0;
						int totDecessi = 0;
						
						for(SegnalazioneContagi sc: listasegnalazioneContagi) {
							if(sc.getComuneRiferimento().getProvinciaAppartenenza().equals(p) && 
									yearCheck(sc.getData(), annoSelezionato) == 0) 
								totContagi += getContagi(sc, statoContagio, malattiaSelezionata);
						}
						
						for(SegnalazioneDecessi sd: listasegnalazioneDecessi) {
							if(sd.getProvinciaRiferimento().equals(p) &&
									yearCheck(sd.getData(), annoSelezionato) == 0)
								totDecessi += getDecessi(sd, causaDecessoSelezionata);
						}
						
						datiTab.add(new DisplayData(annoSelezionato, p, totContagi, malattiaSelezionata, totDecessi, causaDecessoSelezionata));
						
					}
				}
			}
			else {
				if(comuneSelezionato.equals(tuttiComuni)) {
					for(Comune c: listaComune) {
						if(c.equals(tuttiComuni))
							continue;
						
						if(c.getProvinciaAppartenenza().equals(provSelezionata)) {
							int totContagi = 0;
							
							for(SegnalazioneContagi sc: listasegnalazioneContagi) {
								if(sc.getComuneRiferimento().equals(c) && 
										yearCheck(sc.getData(), annoSelezionato) == 0) 
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
								yearCheck(sc.getData(), annoSelezionato) == 0) 
							totContagi += getContagi(sc, statoContagio, malattiaSelezionata);
					}

					datiTab.add(new DisplayData(annoSelezionato, comuneSelezionato, totContagi, malattiaSelezionata, 0, null));
				}
			}
		}
		tabellaDati.setItems(datiTab);
	}
	
	private int yearCheck(Date dataSegnalazione, int annoSelezionato) throws IOException {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(dataSegnalazione);
		return cal1.get(Calendar.YEAR) - annoSelezionato;
	}
	
	
	/**
	 * Restituisce il numero dei decessi
	 */
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
        
        for (int row = 1; row <= tabellaDati.getItems().size(); row++) {
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
	
	/**
	 * @return se il campo luogo Ã¨ stato compilato in modo corretto
	 */
	private boolean luogoIsValid() {
		if (comboRegioneGrafico.getValue() != null){		
			return true;
		}
		else {
			return false;
		}
	}
		
		
	
	@FXML
	public void fillGraph(ActionEvent event) throws IOException {
		
		if (luogoIsValid()) {			
			if(comboRegioneGrafico.getValue().equals(tutteRegioni)) {							//se selezionato tutteRegioni
				for(Regione r : listaRegione) {
					if (!selectedRegione.contains(r)){
						selectedRegione.add(r);
					}
				}
			}
			else if ((!comboRegioneGrafico.getValue().equals(tutteRegioni)) && comboProvinciaGrafico.getValue() == null) {	//se selezionato regione specifica, ma non la provincia
				if (!selectedRegione.contains(comboRegioneGrafico.getValue()))
					selectedRegione.add(comboRegioneGrafico.getValue());
			}
			else {																	//se selezionata una regione
				if(comboProvinciaGrafico.getValue().equals(tutteProvince)) {						//se selezionato tutteProvince
					for(Provincia p : listaProvincia) 
						if (p.getRegioneAppartenenza().equals(comboRegioneGrafico.getValue()) && !selectedProvincia.contains(p))
							selectedProvincia.add(p);
				}
				
				else if ((!comboProvinciaGrafico.getValue().equals(tutteProvince)) && comboComuneGrafico.getValue() == null) {	//se selezionato provincia specifica, ma non il comune
					if (!selectedProvincia.contains(comboProvinciaGrafico.getValue()))
						selectedProvincia.add(comboProvinciaGrafico.getValue());	
				}
				else {
					if(comboComuneGrafico.getValue().equals(tuttiComuni)) { 			//tutte i comuni di una provincia
						for(Comune c : listaComune)
							if (c.getProvinciaAppartenenza().equals(comboProvinciaGrafico.getValue())&& !selectedComune.contains(c))
								selectedComune.add(c);
						
						
					}
					else if (!comboComuneGrafico.getValue().equals(tuttiComuni)) {
						if (!selectedComune.contains(comboComuneGrafico.getValue()))
							selectedComune.add(comboComuneGrafico.getValue());

					}
				}
			}
			charts();		
			fillLista();	//serve per eliminare
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Selezionare un luogo");
			alert.showAndWait();
		}
		
		comboRegioneGrafico.setValue(tutteRegioni);
		comboProvinciaGrafico.setValue(null);
		comboComuneGrafico.setValue(null);
	}
	
	@FXML
    public void updateGraph(ActionEvent event) throws IOException {
		lowerBound = Long.MAX_VALUE;
		upperBound = 0;
		charts(); 
	}
	
	/**
	 * @param data selezionata
	 * @return stringa contenente l'anno della data
	 */
	private Long endYear(Date selectedDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(selectedDate);
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		return calendar.getTimeInMillis();
	}

    private Long getMillisFromYear(Integer year, boolean upperBound) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		if(upperBound) {
			calendar.set(Calendar.MONTH, 11);
			calendar.set(Calendar.DAY_OF_MONTH, 31);
		}
		else {
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
		}
		return calendar.getTimeInMillis();
    }
	/**
	 * cicla su tutti i luoghi e completa il linechat e il barchart con i dati di contagio e decesso selezionati
	 * linechart con visione annuale o in base a quando inserito
	 * barchart con il risultato totale di malattie e contagi del periodo selezionato
	 */
	private void charts() throws IOException{
		lineChart.getData().clear();
		barChart.getData().clear();
		
		Integer annoIniziale;
		Integer annoFinale;
		
		try {
			annoIniziale = Integer.parseInt(selectAnnoIniz.getText());
		} catch (NumberFormatException e) {
			selectAnnoIniz.clear();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Impostare un anno valido!");
			alert.showAndWait();
			return;
		}
		try {
			annoFinale = Integer.parseInt(selectAnnoFinal.getText());
		} catch (NumberFormatException e) {
			selectAnnoFinal.clear();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Impostare un anno valido!");
			alert.showAndWait();
			return;
		}
		
		if(annoIniziale > annoFinale) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("L'anno iniziale non può essere maggiore di quello finale!");
			alert.showAndWait();
			return;
		}
		
		xLineChart.setLowerBound(getMillisFromYear(annoIniziale, false));
		xLineChart.setUpperBound(getMillisFromYear(annoFinale, true));		
		
		XYChart.Series<Long, Number> serieContagioline = null;
		XYChart.Series<Long, Number> serieDecessoline = null;
		XYChart.Series<String, Number> serieContagiobar = null;
		XYChart.Series<String, Number> serieDecessobar = null;
		
		
		String malattiaSelezionata = comboContagiGrafico.getValue();
		String causaDecessoSelezionata = comboDecessiGrafico.getValue();
		String statoContagio = comboStatoContagioGrafico.getValue();
		
		TreeMap <Long, Integer> casiContagio = new TreeMap<Long,Integer>();
		TreeMap <Long, Integer> casiDecesso = new TreeMap<Long,Integer>();
		
		if (!selectedRegione.isEmpty()){
			for(Regione r: selectedRegione) {
				if(!r.equals(tutteRegioni)) {
					
					int totContagi = 0;
					int totDecessi = 0;
					
					serieContagioline = new XYChart.Series<Long, Number>();
					serieContagioline.setName(r.getNome());
					serieContagiobar = new XYChart.Series<String, Number>();
					serieContagiobar.setName(r.getNome());
					
					for(SegnalazioneContagi sc: listasegnalazioneContagi) {
						if(sc.getComuneRiferimento().getProvinciaAppartenenza().getRegioneAppartenenza().equals(r) && 
								yearCheck(sc.getData(), annoIniziale) >= 0 && yearCheck(sc.getData(), annoFinale) <= 0) {
							
							int contagi = getContagi(sc, statoContagio, malattiaSelezionata);
							totContagi += contagi;
							
							casiContagio = fillCasiContagio(casiContagio, sc, statoContagio, malattiaSelezionata);
						}
					}
					
					serieContagiobar.getData().add(new XYChart.Data<String, Number>(r.getNome(), totContagi));
					serieContagioline = getContagioLineChart(casiContagio, serieContagioline);
					
					casiContagio.clear();
					
					if(!comboDecessiGrafico.getValue().equals(nessunDecesso)) {
						serieDecessoline = new XYChart.Series<Long, Number>();
						serieDecessoline.setName(r.getNome() + " Deceduti");
						serieDecessobar = new XYChart.Series<String, Number>();
						serieDecessobar.setName(r.getNome() + " Deceduti");
						
						
						for(SegnalazioneDecessi sd: listasegnalazioneDecessi) {
							if(sd.getProvinciaRiferimento().getRegioneAppartenenza().equals(r) &&
									yearCheck(sd.getData(), annoIniziale) >= 0 && yearCheck(sd.getData(), annoFinale) <= 0) {
								
								totDecessi += getDecessi(sd, causaDecessoSelezionata);
								
								casiDecesso = fillCasiDecesso(casiDecesso, sd, causaDecessoSelezionata);
							}
						}
						serieDecessobar.getData().add(new XYChart.Data<String, Number>(r.getNome(), totDecessi));
						serieDecessoline = getDecessoLineChart(casiDecesso, serieDecessoline);
						
						casiDecesso.clear();
						lineChart.getData().add(serieDecessoline);
						barChart.getData().add(serieDecessobar);
					}
					
					lineChart.getData().add(serieContagioline);		
					barChart.getData().add(serieContagiobar);
				}
			}
			
		}
		
		if (!selectedProvincia.isEmpty()){
			for(Provincia p: selectedProvincia) {
				if(!p.equals(tutteProvince)) {
					int totContagi = 0;
					int totDecessi = 0;
					
					serieContagioline = new XYChart.Series<Long, Number>();
					serieContagioline.setName(p.getNome()+ " (Provincia)");
					serieContagiobar = new XYChart.Series<String, Number>();
					serieContagiobar.setName(p.getNome() + " (Provincia)");
					
					for(SegnalazioneContagi sc: listasegnalazioneContagi) {
						if(sc.getComuneRiferimento().getProvinciaAppartenenza().equals(p) && 
								yearCheck(sc.getData(), annoIniziale) >= 0 && yearCheck(sc.getData(), annoFinale) <= 0) {
						
							totContagi += getContagi(sc, statoContagio, malattiaSelezionata);
							casiContagio = fillCasiContagio(casiContagio, sc, statoContagio, malattiaSelezionata);
						}
					}
					
					serieContagiobar.getData().add(new XYChart.Data<String, Number>(p.getNome()+" (Provincia)", totContagi));
					serieContagioline = getContagioLineChart(casiContagio, serieContagioline);
					
					
					
					casiContagio.clear();
					if(!comboDecessiGrafico.getValue().equals(nessunDecesso)) {
						serieDecessoline = new XYChart.Series<Long, Number>();
						serieDecessoline.setName(p.getNome() + " (Provincia Deceduti)");
						serieDecessobar = new XYChart.Series<String, Number>();
						serieDecessobar.setName(p.getNome() + " (Provincia Deceduti)");
						
						for(SegnalazioneDecessi sd: listasegnalazioneDecessi) {
							if(sd.getProvinciaRiferimento().equals(p) &&
									yearCheck(sd.getData(), annoIniziale) >= 0 && yearCheck(sd.getData(), annoFinale) <= 0) {
								
								totDecessi += getDecessi(sd, causaDecessoSelezionata);
								
								casiDecesso = fillCasiDecesso(casiDecesso, sd, causaDecessoSelezionata);
							}
						}
						serieDecessobar.getData().add(new XYChart.Data<String, Number>(p.getNome()+" (Provincia)", totDecessi));
						serieDecessoline = getDecessoLineChart(casiDecesso, serieDecessoline);
						
						casiDecesso.clear();
						lineChart.getData().add(serieDecessoline);
						barChart.getData().add(serieDecessobar);
					}
					
					lineChart.getData().add(serieContagioline);		
					barChart.getData().add(serieContagiobar);
			
				}
			}
		}
		
		if (!selectedComune.isEmpty()) {
			for(Comune c: selectedComune) {
				if(!c.equals(tuttiComuni)) {
					
					int totContagi = 0;
					
					serieContagioline = new XYChart.Series<Long, Number>();
					serieContagioline.setName(c.getNome());
					serieContagiobar = new XYChart.Series<String, Number>();
					serieContagiobar.setName(c.getNome());
				
				
					for(SegnalazioneContagi sc: listasegnalazioneContagi) {
						if(sc.getComuneRiferimento().equals(c) && 
								yearCheck(sc.getData(), annoIniziale) >= 0 && yearCheck(sc.getData(), annoFinale) <= 0) {
						
							totContagi += getContagi(sc, statoContagio, malattiaSelezionata);
							casiContagio = fillCasiContagio(casiContagio, sc, statoContagio, malattiaSelezionata);
						}
						
					}
				
					serieContagiobar.getData().add(new XYChart.Data<String, Number>(c.toString(), totContagi));
					serieContagioline = getContagioLineChart(casiContagio, serieContagioline);
					
					casiContagio.clear();
					
					lineChart.getData().add(serieContagioline);	
					barChart.getData().add(serieContagiobar);
				
				}	
			}
			
		}
		
		xLineChart.setUpperBound(upperBound + 86400000);
		xLineChart.setLowerBound(lowerBound - 86400000);
	}
	
	/**
	 * 
	 * @param TreeMap casiContagio
	 * @param serie "linea" del grafico
	 * @param luogo (nome della linea) 
	 * @return serie "linea" casi contagio nel tempo da aggiungere al grafico
	 */
	private Series<Long, Number> getContagioLineChart(TreeMap<Long, Integer> casiContagio, Series<Long, Number> serieContagioline) {
		for (Long i: casiContagio.keySet()) {
			if (casiContagio.get(i)!= 0) 
				serieContagioline.getData().add(new XYChart.Data<Long, Number>(i, casiContagio.get(i)));
		}
		return serieContagioline;
	}

	/**
	 * @param TreeMap casiDecesso
	 * @param serie "linea" del grafico
	 * @return serie "linea" casi decesso nel tempo da aggiungere al grafico
	 */
	private Series<Long, Number> getDecessoLineChart(TreeMap<Long, Integer> casiDecesso, Series<Long, Number> serieDecessoline) {
		for (Long i: casiDecesso.keySet()) {
			if (casiDecesso.get(i)!= 0) {
				serieDecessoline.getData().add(new XYChart.Data<Long, Number>(i, casiDecesso.get(i)));
			}
		}
		return serieDecessoline;
	}

	/**
	 * @param TreeMap casiContagio
	 * @param segnalazione contagio 
	 * @param stato contagio
	 * @param malattia selezionata
	 * @return treemap con il numero di casi di decesso
	 */
	private TreeMap<Long, Integer> fillCasiDecesso(TreeMap<Long, Integer> casiDecesso, SegnalazioneDecessi sd, String causaDecessoSelezionata) {
		if ((casiDecesso.get(endYear(sd.getData())) == null) ) {
			if (getDecessi(sd, causaDecessoSelezionata) != 0)
				casiDecesso.put(endYear(sd.getData()), getDecessi(sd, causaDecessoSelezionata));
		}
		else {
			int newValue = casiDecesso.get(endYear(sd.getData()));
			newValue += getDecessi(sd, causaDecessoSelezionata);
			casiDecesso.put(endYear(sd.getData()), newValue);
		}
		
		if(!casiDecesso.isEmpty()) {
			if(casiDecesso.firstKey() < lowerBound)
				lowerBound = casiDecesso.firstKey();
			if(casiDecesso.lastKey() > upperBound)
				upperBound = casiDecesso.lastKey();
		}
		
		return casiDecesso;
	}
	
	private long dateToLong(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	/**
	 * @param TreeMap casiContagio
	 * @param segnalazione contagio 
	 * @param stato contagio
	 * @param malattia selezionata
	 * @return treemap con il numero di casi di contagio
	 */
	private TreeMap<Long,Integer> fillCasiContagio(TreeMap<Long,Integer> casiContagio, SegnalazioneContagi sc, String statoContagio, String malattiaSelezionata) {
		long dateAsLong = dateToLong(sc.getData());
		int contagi = getContagi(sc, statoContagio, malattiaSelezionata);
		
		if(!raggruppaAnno.isSelected()) {
			if(casiContagio.containsKey(dateAsLong))
				casiContagio.put(dateAsLong, 
						casiContagio.get(dateAsLong) + contagi);
			else
				casiContagio.put(dateAsLong, contagi);
			
			if(casiContagio.firstKey() < lowerBound)
				lowerBound = casiContagio.firstKey();
			if(casiContagio.lastKey() > upperBound)
				upperBound = casiContagio.lastKey();
			return casiContagio;
		}
		
		if (contagi != 0) {
			if (casiContagio.containsKey(endYear(sc.getData()))) {
				int newValue = casiContagio.get(endYear(sc.getData())) + contagi;
				casiContagio.put(endYear(sc.getData()), newValue);
			}
			else
			 casiContagio.put(endYear(sc.getData()), contagi);
		}
		
		if(casiContagio.firstKey() < lowerBound)
			lowerBound = casiContagio.firstKey();
		if(casiContagio.lastKey() > upperBound)
			upperBound = casiContagio.lastKey();
		return casiContagio;
	}

	/**
	 * elimina tutti i comuni/province/regioni selezionati
	 */
	@FXML
	void delete(ActionEvent event) {
		
		selectedComune.clear();
		selectedProvincia.clear();
		selectedRegione.clear();
		 
		lineChart.getData().clear();
		barChart.getData().clear();
	 }

	/**
	 * lista dei luoghi selezionati
	 */
	void fillLista() {
		
		ObservableList<String> lista = FXCollections.observableArrayList();
		lista.clear();
		
		if(!selectedComune.isEmpty()) {
			for(Comune i : selectedComune)
				lista.add(i.getNome());
		}
		if(!selectedProvincia.isEmpty()) {
			for(Provincia i : selectedProvincia)
				lista.add(i.getNome() + " (Provincia)");
		}
		if(!selectedRegione.isEmpty()) {
			for(Regione i : selectedRegione)
				lista.add(i.getNome());			
		}
		
		comboLista.setItems(lista);
	}
	
	/**
	 * elimina comune/provincia/regione tra quelli selezionati
	 */
	@FXML
	void deleteCampo(ActionEvent event) throws IOException {

		Comune deleteC = null;
		Provincia deleteP = null;
		Regione deleteR = null;
		
		if(!selectedComune.isEmpty()) {
			if(comboLista.getValue().equals(tuttiComuni.toString())) {
				selectedComune.clear();
			}
			else {
				for(Comune i : selectedComune) {
					if (i.getNome().equals(comboLista.getValue())) {
						deleteC = i;
					}
				}
				selectedComune.remove(deleteC);
			}
			
		}
		
		if(!selectedProvincia.isEmpty()) {
			if(comboLista.getValue().equals(tutteProvince.toString())) {
				selectedProvincia.clear();
			}
			else{
				for(Provincia i : selectedProvincia) {
					String provincia = i.getNome() + " (Provincia)";
					if (provincia.equals(comboLista.getValue())) {
						deleteP = i;
					}
				}
				selectedProvincia.remove(deleteP);
			}
		}
		
		if(!selectedRegione.isEmpty()) {
			if(comboLista.getValue().equals(tutteRegioni.toString())) {
				selectedRegione.clear();
			}
			else {
				for(Regione i : selectedRegione) {
					if (i.getNome().equals(comboLista.getValue())) {
						deleteR = i;
					}
				}
				selectedRegione.remove(deleteR);
			}
			
		}
		
		charts();
		fillLista();	
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
