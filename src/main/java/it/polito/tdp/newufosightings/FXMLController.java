package it.polito.tdp.newufosightings;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.newufosightings.model.Model;
import it.polito.tdp.newufosightings.model.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare al branch master_turnoB per turno B

public class FXMLController {
	
	private Model model;
	private Integer anno;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtAnno;

    @FXML
    private Button btnSelezionaAnno;

    @FXML
    private ComboBox<String> cmbBoxForma;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private TextField txtT1;

    @FXML
    private TextField txtAlfa;

    @FXML
    private Button btnSimula;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	if(cmbBoxForma.getValue()!=null) {
    		model.creaGrafo(anno, cmbBoxForma.getValue());
    		txtResult.clear();
    		for(State s : model.getGrafo().vertexSet())
    			if(model.sommaPesiArchi(s)>0)
    				txtResult.appendText(s.toString() + "\nSomma pesi archi: " + model.sommaPesiArchi(s) + "\n");
    	}
    	else 
    		txtResult.setText("Selezionare una forma");
    }

    @FXML
    void doSelezionaAnno(ActionEvent event) {
    	cmbBoxForma.getItems().clear();
    	String annoString = txtAnno.getText().trim();
    	try {
    		if(annoString.length()!=4)
    			throw new NumberFormatException();
    		anno = Integer.parseInt(annoString);
    		if(model.getFormeAnno(anno).size()>0) {
    			cmbBoxForma.getItems().addAll(model.getFormeAnno(anno));
    			txtResult.setText("Forme inserite");
    		} 
    		else 
    			txtResult.setText("Nessun avvistamente nell'anno inserito");
    	} catch (NumberFormatException e) {
    		e.printStackTrace();
    		txtResult.setText("Inserito anno non valido");
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	try {
    		Integer t1 = Integer.parseInt(txtT1.getText());
    		Integer alfa = Integer.parseInt(txtAlfa.getText());
    		if(alfa<0 || alfa>100 || t1<0 || t1>365)
    			throw new NumberFormatException();
    		else {
    			if(model.getGrafo()!=null) {
    				model.simula(t1, alfa);
    				txtResult.clear();
    				for(State s : model.getGrafo().vertexSet())
    					txtResult.appendText(s.toString() + " " + s.getDefconReale() + /*" (" + s.getDefcon() + ")" + */ "\n");
    			} else 
    				txtResult.setText("Crea il grafo");
    		}
    	} catch (NumberFormatException e) {
    		e.printStackTrace();
    		txtResult.setText("Inserimento di alfa e t1 non valido");
    	}
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSelezionaAnno != null : "fx:id=\"btnSelezionaAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert cmbBoxForma != null : "fx:id=\"cmbBoxForma\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtT1 != null : "fx:id=\"txtT1\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAlfa != null : "fx:id=\"txtAlfa\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
