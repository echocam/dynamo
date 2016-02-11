package uk.ac.cam.echo2016.multinarrative.gui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import javafx.stage.FileChooser;


/**
 * @author jr650
 */
public class FXMLGUI extends Application {
	
	private Stage theStage;
    
    public static void main(String[] args) {
        Application.launch(FXMLGUI.class, args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {        
        try{
        	theStage = stage;
        	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml_layout.fxml"));
            
            Parent root = loader.load();
            
            FXMLController controller = loader.getController();
            controller.init(this);
            
            stage.setTitle("Graph Editor");
            stage.setScene(new Scene(root, 900, 600));
            stage.show();
        }catch(IOException ioe){
            //Indicates that fxml files aren't set up properly...
            throw new RuntimeException("FXML files not configured correctly",ioe);
        }
    }
    
    /**
     * Opens the About dialog.
     */
    @FXML
    public void showAbout() {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml_aboutDialog.fxml"));
    		Pane page = loader.load();
    		Stage dialogStage = new Stage();
    		dialogStage.setTitle("About");
    		Scene scene = new Scene(page);
    		dialogStage.setScene(scene);
    		
    		
    	    FXMLAboutDialogController controller = loader.getController();
    	    controller.setDialogStage(dialogStage);
    		
    	    dialogStage.setAlwaysOnTop(true);
    	    dialogStage.initModality(Modality.APPLICATION_MODAL);
    		dialogStage.showAndWait();
    	} catch (IOException ioe) {
    		//Indicates that fxml files aren't set up properly...
            throw new RuntimeException("FXML files not configured correctly",ioe);
    	}
    }
    
    /**
     * Opens the Save As dialog
     */
    @FXML
    public String showSaveAs() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save As");
		File selectedFile = fileChooser.showSaveDialog(theStage);
		
		if (selectedFile != null) {
			return selectedFile.getAbsolutePath();
		} else return null;
    }
    
    /**
     * Opens the Open dialog
     */ //TODO check save as.
    @FXML
    public String showOpen() {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Open");
    	File selectedFile = fileChooser.showOpenDialog(theStage);
    	
    	if (selectedFile != null) {
    		return selectedFile.getAbsolutePath();
    	} else return null;
    }
    
    /**
     * Opens the error dialog when an IO operation fails
     */
    @FXML
    public void showError(String message) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml_errorDialog.fxml"));
    		Pane page = loader.load();
    		Stage dialogStage = new Stage();
    		dialogStage.setTitle("Error");
    		Scene scene = new Scene(page);
    		dialogStage.setScene(scene);
    		
    		
    	    FXMLErrorController controller = loader.getController();
    	    controller.setDialogStage(dialogStage);
    	    controller.submitMessage(message);
    		
    	    dialogStage.setAlwaysOnTop(true);
    	    dialogStage.initModality(Modality.APPLICATION_MODAL);
    		dialogStage.showAndWait();
    	} catch (IOException ioe) {
    		//Indicates that fxml files aren't set up properly...
            throw new RuntimeException("FXML files not configured correctly",ioe);
    	}
    }
}

