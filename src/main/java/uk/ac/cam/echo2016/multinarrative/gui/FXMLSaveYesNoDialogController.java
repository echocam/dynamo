package uk.ac.cam.echo2016.multinarrative.gui;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;

/**
 * 
 * @author rjm232
 *
 */

public class FXMLSaveYesNoDialogController {
	
	@FXML 
	private Button yesButton;
	@FXML
	private Button noButton;
	
	private Stage dialogStage;
	
	private boolean yesPressed = false;
	
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
     * Returns true if "Yes" button pressed.
     */
    public boolean yesPressed() {
    	return yesPressed;
    }
    
    /**
     * Called when the user clicks the "Yes" button.
     */
    @FXML
    private void yesButton() {
    	yesPressed = true;
        dialogStage.close();
    }
    
    /**
     * Called when the user clicks the "No" button.
     */
    @FXML
    private void noButton() {
        dialogStage.close();
    }

}