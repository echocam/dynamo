package uk.ac.cam.echo2016.multinarrative.gui;
 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
 
/**
 * @author jr650
 */
public class FXMLController {
    @FXML private Text infoBar;
    
    @FXML protected void addPropertyButtonAction(ActionEvent event) {
        infoBar.setText("Add Property button pressed");
    }

}
