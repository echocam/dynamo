package uk.ac.cam.echo2016.multinarrative.gui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import uk.ac.cam.echo2016.multinarrative.gui.operations.IllegalOperationException;

/**
 * @author jr650
 */
public class FXMLPropertyController implements Initializable {

	private String propName;

	private Map<String, Color> colours = new HashMap<String, Color>();

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
					colours.put(values.getSelectionModel().getSelectedItem(), newValue);
				});
		values.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
					remove.setDisable(newValue == null);
					recolour.setDisable(newValue == null);
					if (newValue != null) {
						Color c = colours.get(newValue);
						if (c == null) {
							c = Color.TRANSPARENT;
						}
						recolour.valueProperty().set(c);
					}
				});
		values.setOnEditCommit(event -> {
			String oldValue = values.getItems().get(event.getIndex());
			String newValue = event.getNewValue();
			try {
				controller.getOperations().renamePropertyValue(propName, oldValue, newValue);
				values.getItems().set(event.getIndex(), newValue);
				for (MenuItem i : menu.getItems()) {
					if (i.getText().equals(oldValue)){
						i.setText(newValue);
						break;
					}
				}
				event.consume();
			} catch (Exception e) {
				controller.setInfo(e.getMessage(), oldValue, newValue);
			}
		});

		name.textProperty()
				.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
					textChangeAction();
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
		String s = controller.getOperations().getDefaultValue(propName, type.getValue());
		try {
			controller.getOperations().addPropertyValue(propName, s);
			values.getItems().add(s);
			MenuItem item = new MenuItem(s);
			item.setOnAction(trigger -> {
				controller.assignProperty(propName, type.getValue(), s);				
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
		colours.remove(selected);
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
		System.out.println("Type CHanged " + type.getValue());
	}
}
