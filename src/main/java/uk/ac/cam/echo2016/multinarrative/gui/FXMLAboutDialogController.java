package uk.ac.cam.echo2016.multinarrative.gui;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;

/**
 * 
 * @author rjm232
 *
 */
public class FXMLAboutDialogController {
	
	@FXML 
	private Button okButton;
	
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
    
    /**
     * Called when the user clicks the button.
     */
    @FXML
    private void handleButton() {
        dialogStage.close();
    }

}
