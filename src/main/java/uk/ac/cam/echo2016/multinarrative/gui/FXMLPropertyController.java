package uk.ac.cam.echo2016.multinarrative.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import uk.ac.cam.echo2016.multinarrative.gui.operations.IllegalOperationException;

/**
 * @author jr650
 */
public class FXMLPropertyController implements Initializable {


    private String propName;

    private String typeName = "String";

    @FXML
    private TitledPane root;
    @FXML
    private ListView<String> values;
    @FXML
    private ComboBox<String> type;
    @FXML
    private TextField name;
    @FXML
    private BorderPane propertyPane;
    @FXML
    private Button remove;
    @FXML
    private ColorPicker recolour;
    @FXML
    private CheckBox routeType;

    private FXMLController controller;

    private Menu menu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        values.setCellFactory(TextFieldListCell.forListView());

        remove.setDisable(true);

        recolour.getStyleClass().add("button");
        recolour.setDisable(true);
        recolour.valueProperty()
                .addListener((ObservableValue<? extends Color> observable, Color oldValue, Color newValue) -> {

                });
        values.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    remove.setDisable(newValue == null);
                    recolour.setDisable(newValue == null);
                    if (newValue != null) {
                        Color c = controller.getOperations().getColor(propName, newValue);
                        recolour.valueProperty().set(c);
                    }
                });
        values.setOnEditCommit(event -> {
            String oldValue = values.getItems().get(event.getIndex());
            String newValue = event.getNewValue();
            try {
                controller.getOperations().renamePropertyValue(propName, oldValue, newValue);
                values.getItems().set(event.getIndex(), newValue);
                menu.getItems().get(event.getIndex()).setText(newValue);
                event.consume();
            } catch (Exception e) {
                controller.setInfo(e.getMessage(), oldValue, newValue);
            }
        });

        name.textProperty()
                .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    textChangeAction();
                });
        routeType.selectedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (routeType.isSelected()) {
                        controller.getOperations().setAsRouteType(propName);
                    } else {
                        controller.getOperations().clearAsRouteType(propName);
                    }
                });
        name.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if(event.getCode()==KeyCode.EQUALS){
                event.consume();
            }
        });
    }

    public void init(String name, FXMLController parent, Menu menu) {
        propName = name;
        controller = parent;
        this.name.setText(name);
        this.menu = menu;
    }

    protected void textChangeAction() {
        String newName = name.getText();
        if (!newName.equals(propName)) {
            try {
                controller.getOperations().renameProperty(propName, newName);
                menu.setText(newName);
                propName = name.getText();

            } catch (IllegalOperationException ioe) {
                controller.setInfo(ioe.getMessage(), propName, newName);
                name.setText(propName);
            }
        }
    }

    @FXML
    protected void deleteButtonAction(ActionEvent event) {
        controller.removeProperty(propName, root, menu);
    }

    @FXML
    protected void addValueAction(ActionEvent event) {
        String s = controller.getOperations().getDefaultValue(propName, typeName);
        try {
            controller.getOperations().addPropertyValue(propName, s);
            values.getItems().add(s);
            MenuItem item = new MenuItem(s);
            item.setOnAction(trigger -> {
                controller.assignProperty(propName, typeName, item.getText());
            });
            menu.getItems().add(item);
        } catch (IllegalOperationException e) {
            controller.setInfo(e.getMessage(), s);
        }
    }

    @FXML
    protected void removeValueAction(ActionEvent event) {
        String selected = values.getSelectionModel().getSelectedItem();
        controller.getOperations().removePropertyValue(propName, selected);
        values.getItems().remove(selected);
        MenuItem remove = null;
        for (MenuItem item : menu.getItems()) {
            if (item.getText().equals(selected))
                remove = item;
        }
        if (remove != null) {
            menu.getItems().remove(remove);
        }

    }

    @FXML
    protected void changeTypeAction(ActionEvent event) {
        try {
            controller.getOperations().setPropertyType(propName, type.getValue());
            typeName = type.getValue();
        } catch (IllegalOperationException e) {
            type.setValue(typeName);
        }
    }

}
