package uk.ac.cam.echo2016.multinarrative.gui;
 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Accordion;
import javafx.scene.input.KeyEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import java.io.IOException;

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
    @FXML
    private Accordion properties;

    private GUIOperations operations = new GUIOperations();

    @FXML 
    protected void addPropertyButtonAction(ActionEvent event) {
        String name = propertyName.getText();
        try{
            operations.addProperty(name);
            propertyName.setText("");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml_property.fxml"));
            TitledPane root = loader.load();
            FXMLPropertyController prop = (FXMLPropertyController) loader.getController();
            prop.init(name,this);
            properties.getPanes().add(root);
            setInfo(PROPERTY_ADDED,name);
        }catch(IllegalOperationException ioe){
            setInfo(ioe.getMessage(),name);
        }catch(IOException ioe){
            //Indicates that fxml files aren't set up properly...
            throw new RuntimeException("FXML files not configured correctly",ioe);
        }
    }

    protected void setInfo(String template, String... values){
        infoBar.setText(Strings.populateString(template,values));
    }

}
