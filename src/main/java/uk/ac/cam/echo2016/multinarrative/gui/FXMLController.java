package uk.ac.cam.echo2016.multinarrative.gui;
 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;

import static uk.ac.cam.echo2016.multinarrative.gui.Strings.*;
 
/**
 * @author jr650
 */
public class FXMLController {
    @FXML 
    private Text infoBar;
    @FXML 
    private TextField propertyName;
    @FXML 
    private Button addProperty;

    private GUIOperations operations = new GUIOperations();

    @FXML 
    protected void addPropertyButtonAction(ActionEvent event) {
        String name = propertyName.getText();
        try{
            operations.addProperty(name);
            propertyName.setText("");
            setInfo(PROPERTY_ADDED,name);
        }catch(IllegalOperationException ioe){
            setInfo(ioe.getMessage(),name);
        }
    }

    protected void setInfo(String template, String... values){
        infoBar.setText(Strings.populateString(template,values));
    }

}
