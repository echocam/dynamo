package uk.ac.cam.echo2016.multinarrative.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author jr650
 */
public class FXMLGUI extends Application {
    
    public static void main(String[] args) {
        Application.launch(FXMLGUI.class, args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml_layout.fxml"));
        
        stage.setTitle("Graph Editor");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }
}

