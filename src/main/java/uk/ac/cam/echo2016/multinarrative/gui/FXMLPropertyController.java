package uk.ac.cam.echo2016.multinarrative.gui;
 
import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
 
/**
 * @author jr650
 */
public class FXMLPropertyController {

	private String propName;
	
	private Map<String,Color> colours = new HashMap<String,Color>();
	
	@FXML
	private TitledPane root;
    @FXML
    private ListView<String> values;
    @FXML
    private ComboBox<String> type;
    @FXML
    private TextField name;
    @FXML
    private BorderPane propertyPane;
    @FXML
    private Button remove;
    @FXML
    private Button recolour;

    private FXMLController controller;

    public void init(String name, FXMLController parent){
        this.name.setText(name);
        propName = name;
        controller=parent;
        
        values.setCellFactory(TextFieldListCell.forListView());
        remove.setDisable(true);
    	recolour.setDisable(true);
        
        this.name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                textChangeAction();
            }
        });
    }
    
    protected void textChangeAction(){
    	String newName = name.getText();
    	if(!newName.equals(propName)){
    		try{
    			controller.getOperations().renameProperty(propName,newName);
    			propName = name.getText();
    			
    		}catch(IllegalOperationException ioe){
    			controller.setInfo(ioe.getMessage(), propName, newName);
    			name.setText(propName);
    		}
    	}
    }
    
    @FXML
    protected void deleteButtonAction(ActionEvent event){
    	controller.removeProperty(propName, root);
    }
    
    @FXML
    protected void addValueAction(ActionEvent event){
    	ObservableList<String> items = values.getItems();
    	int i = 1;
    	String s = Strings.populateString(Strings.PROPERTY_VALUE, ""+i);
    	while(items.contains(s)){
    		i++;
    		s=Strings.populateString(Strings.PROPERTY_VALUE, ""+i);
    	}
    	items.add(s);
    	remove.setDisable(false);
    	recolour.setDisable(false);
    }
    
    @FXML
    protected void removeValueAction(ActionEvent event){
    	ObservableList<String> selected = values.getSelectionModel().getSelectedItems();
    	ObservableList<String> items = values.getItems();
    	for(String s: selected){
    		items.remove(s);
    		colours.remove(s);
    	}
    	if(items.isEmpty()){
    		remove.setDisable(true);
        	recolour.setDisable(true);
    	}
    }
 
}
