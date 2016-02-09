package uk.ac.cam.echo2016.multinarrative.gui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

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
		;

		name.textProperty()
				.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
					textChangeAction();
				});
	}

	public void init(String name, FXMLController parent) {
		propName = name;
		controller = parent;
		this.name.setText(name);
	}

	protected void textChangeAction() {
		String newName = name.getText();
		if (!newName.equals(propName)) {
			try {
				controller.getOperations().renameProperty(propName, newName);
				propName = name.getText();

			} catch (IllegalOperationException ioe) {
				controller.setInfo(ioe.getMessage(), propName, newName);
				name.setText(propName);
			}
		}
	}

	@FXML
	protected void deleteButtonAction(ActionEvent event) {
		controller.removeProperty(propName, root);
	}

	@FXML
	protected void addValueAction(ActionEvent event) {
		ObservableList<String> items = values.getItems();
		int i = 1;
		String s = Strings.populateString(Strings.PROPERTY_VALUE, "" + i);
		while (items.contains(s)) {
			i++;
			s = Strings.populateString(Strings.PROPERTY_VALUE, "" + i);
		}
		items.add(s);
	}

	@FXML
	protected void removeValueAction(ActionEvent event) {
		ObservableList<String> selected = values.getSelectionModel().getSelectedItems();
		ObservableList<String> items = values.getItems();
		for (String s : selected) {
			items.remove(s);
			colours.remove(s);
		}

	}

	@FXML
	protected void changeTypeAction(ActionEvent event) {
		System.out.println("Type CHanged " + type.getValue());
	}
}
