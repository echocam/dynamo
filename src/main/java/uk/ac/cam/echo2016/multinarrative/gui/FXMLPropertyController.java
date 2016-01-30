package uk.ac.cam.echo2016.multinarrative.gui;
 
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
 
/**
 * @author jr650
 */
public class FXMLPropertyController {

    @FXML
    private ListView<String> values;
    @FXML
    private ComboBox<String> type;
    @FXML
    private TextField name;

    private FXMLController controller;

    public void init(String name, FXMLController parent){
        this.name.setText(name);
        controller=parent;
    }

}
