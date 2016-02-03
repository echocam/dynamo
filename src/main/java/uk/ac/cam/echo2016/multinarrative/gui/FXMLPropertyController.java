package uk.ac.cam.echo2016.multinarrative.gui;
 
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
 
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

}
