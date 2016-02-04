package uk.ac.cam.echo2016.multinarrative.gui;
 
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
 
/**
 * @author jr650
 */
public class FXMLPropertyController {

	private String propName;
	
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

    private FXMLController controller;

    public void init(String name, FXMLController parent){
        this.name.setText(name);
        propName = name;
        controller=parent;
        
        values.setCellFactory(TextFieldListCell.forListView());
        
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
    }
    
    @FXML
    protected void removeValueAction(ActionEvent event){
    	ObservableList<String> selected = values.getSelectionModel().getSelectedItems();
    	ObservableList<String> items = values.getItems();
    	for(String s: selected){
    		items.remove(s);
    	}
    }
 
}
