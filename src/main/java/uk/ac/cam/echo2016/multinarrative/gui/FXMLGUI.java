package uk.ac.cam.echo2016.multinarrative.gui;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
        try {
            theStage = stage;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml_layout.fxml"));

            Parent root = loader.load();

            FXMLController controller = loader.getController();
            controller.init(this);
            // controller.init(this, stage);

            stage.setTitle("DyNaMo");
            stage.setScene(new Scene(root, 900, 600));
            stage.show();
        } catch (IOException ioe) {
            // Indicates that fxml files aren't set up properly...
            throw new RuntimeException("FXML files not configured correctly", ioe);
        }
    }

    /**
     * Opens the About dialog.
     */
    public void showAboutDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText(Strings.ABOUT_TEXT);
        String css = getClass().getResource("Style.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(css);
        alert.showAndWait();
    }

    /**
     * Opens the Save As dialog
     */
    public String showSaveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");
        File selectedFile = fileChooser.showSaveDialog(theStage);

        if (selectedFile != null) {
            return selectedFile.getAbsolutePath();
        } else
            return null;
    }
    
    /**
     * Opens the Save As dialog
     */
    public String showExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export");
        File selectedFile = fileChooser.showSaveDialog(theStage);
        
        if (selectedFile != null) {
            return selectedFile.getAbsolutePath();
        } else
            return null;
    }

    /**
     * Opens the Open dialog
     */
    public String showOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        File selectedFile = fileChooser.showOpenDialog(theStage);

        if (selectedFile != null) {
            return selectedFile.getAbsolutePath();
        } else
            return null;
    }

    /**
     * Checks if a user wants to save before executing the next action
     */
    public boolean checkIfShouldSave() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Do you want to save?");
        alert.setHeaderText(null);
        alert.setContentText("Your work will be lost if you don't save.");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, noButton);
        String css = getClass().getResource("Style.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(css);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton)
            return true;
        else
            return false;
    }

    /**
     * Opens the error dialog when an IO operation fails
     */
    public void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Operation Failed");
        alert.setHeaderText(null);
        alert.setContentText(message);
        String css = getClass().getResource("Style.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(css);
        alert.showAndWait();
    }
}
