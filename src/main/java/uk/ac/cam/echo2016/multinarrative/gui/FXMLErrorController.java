package uk.ac.cam.echo2016.multinarrative.gui;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * 
 * @author rjm232
 *
 */

public class FXMLErrorController {
	
	@FXML 
	private Button okButton;
	@FXML
	private Label textLabel;
	
	private Stage dialogStage;
	
	/**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }
    
    /**
     * Sets the stage of this dialog.
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void submitMessage(String message) {
    	textLabel.setText(message);
    }
    
    /**
     * Called when the user clicks the button.
     */
    @FXML
    private void handleButton() {
        dialogStage.close();
    }

}
