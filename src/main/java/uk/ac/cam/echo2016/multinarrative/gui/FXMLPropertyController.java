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
                        Color c = controller.getOperations().narrativeOperations().getColor(propName, newValue);
                        recolour.valueProperty().set(c);
                    }
                });
        values.setOnEditCommit(event -> {
            String oldValue = values.getItems().get(event.getIndex());
            String newValue = event.getNewValue();
            try {
                controller.getOperations().doOp(controller.getOperations().generator().renameValue(propName, typeName,
                        oldValue, newValue, this));
                values.getItems().set(event.getIndex(), newValue);
                menu.getItems().get(event.getIndex()).setText(newValue);
                event.consume();
            } catch (Exception e) {
                controller.setInfo(e.getMessage());
            }
        });

        name.textProperty()
                .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    textChangeAction();
                });
        routeType.selectedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    try {
                        controller.getOperations().doOp(
                                controller.getOperations().generator().setRouteType(propName, newValue, routeType));
                    } catch (IllegalOperationException ioe) {
                        controller.setInfo(ioe.getMessage());
                    }
                });
        name.addEventFilter(KeyEvent.KEY_TYPED, (KeyEvent event) -> {
            if (event.getCharacter().equals("=")) {
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
                controller.getOperations()
                        .doOp(controller.getOperations().generator().renameProperty(propName, newName, this));
            } catch (IllegalOperationException ioe) {
                controller.setInfo(ioe.getMessage());
                name.setText(propName);
            }
        }
    }

    public void setName(String newName) {
        propName = newName;
        menu.setText(newName);
        name.setText(newName);
    }

    @FXML
    protected void deleteButtonAction(ActionEvent event) {
        try {
            controller.getOperations().doOp(controller.getOperations().generator().removeProperty(this));
        } catch (IllegalOperationException ioe) {
            controller.setInfo(ioe.getMessage());
        }
    }

    public void addValue(String s, int index) {
        values.getItems().add(index, s);
        MenuItem item = new MenuItem(s);
        item.setOnAction(trigger -> {
            controller.assignProperty(propName, typeName, item.getText());
        });
        menu.getItems().add(index, item);
    }

    public void removeValue(String s) {
        int index = values.getItems().indexOf(s);
        values.getItems().remove(s);
        menu.getItems().remove(index);
    }

    @FXML
    protected void addValueAction(ActionEvent event) {
        String s = controller.getOperations().narrativeOperations().getDefaultValue(propName, typeName);
        try {
            controller.getOperations().doOp(controller.getOperations().generator().addValue(propName, typeName, s,
                    values.getItems().size(), this));

        } catch (IllegalOperationException e) {
            controller.setInfo(e.getMessage());
        }
    }

    @FXML
    protected void removeValueAction(ActionEvent event) {
        String selected = values.getSelectionModel().getSelectedItem();
        try {
            controller.getOperations()
                    .doOp(controller.getOperations().generator().removeValue(propName, typeName, selected, this));
        } catch (IllegalOperationException ioe) {
            controller.setInfo(ioe.getMessage());
        }
    }

    public int getIndexOf(String s) {
        return values.getItems().indexOf(s);
    }

    @FXML
    protected void changeTypeAction(ActionEvent event) {
        try {
            controller.getOperations()
                    .doOp(controller.getOperations().generator().setPropertyType(propName, typeName, type));
            typeName = type.getValue();
        } catch (IllegalOperationException e) {
            type.setValue(typeName);
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public TitledPane getRoot() {
        return root;
    }

    /**
     * Gets the current property name
     * 
     * @return
     */
    public String getName() {
        return propName;
    }
}
