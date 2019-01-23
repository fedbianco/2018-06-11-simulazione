/**
 * Sample Skeleton for 'Ufo.fxml' Controller Class
 */

package it.polito.tdp.ufo;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.ufo.model.Model;
import it.polito.tdp.ufo.model.SightingForYear;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class UfoController {
	
	Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<SightingForYear> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxStato"
    private ComboBox<String> boxStato; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void handleAnalizza(ActionEvent event) {
    txtResult.clear();
    	txtResult.appendText("Lista stati immediamente successivi a quello selezionato:\n");
    	for(String s : this.model.getSuccessor(this.boxStato.getValue())) {
    		if(!this.boxStato.getValue().equals(s))
    		txtResult.appendText(s +"\n");
    	}
    	txtResult.appendText("Lista stati immediamente precedenti a quello selezionato:\n");
    	for(String s : this.model.getPredecessor(this.boxStato.getValue())) {
    		if(!this.boxStato.getValue().equals(s))
    		txtResult.appendText(s +"\n");
    	}
    	txtResult.appendText("\nLista altri stati raggiungibili:\n");
    	int conta = 0;
    	for(String s : this.model.getStatiRaggiungibili(this.boxStato.getValue())) {
    		if(!this.boxStato.getValue().equals(s)) {
    		txtResult.appendText(s +"\n");
    		conta++;}
    	}
    	txtResult.appendText("Numero stati raggiungibili "+ conta +"\n");

    }

    @FXML
    void handleAvvistamenti(ActionEvent event) {
    	txtResult.clear();
    	this.model.creaGrafo(this.boxAnno.getValue().getYear());
    	this.boxStato.getItems().addAll(this.model.getState());

    }

    @FXML
    void handleSequenza(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";

    }

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
		this.boxAnno.getItems().addAll(this.model.getYearAndCount());
	}
    
}
