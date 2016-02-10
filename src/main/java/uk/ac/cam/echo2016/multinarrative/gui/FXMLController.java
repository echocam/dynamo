package uk.ac.cam.echo2016.multinarrative.gui;

import static uk.ac.cam.echo2016.multinarrative.gui.Strings.PROPERTY_ADDED;
import static uk.ac.cam.echo2016.multinarrative.gui.Strings.PROPERTY_REMOVED;

import java.io.IOException;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import uk.ac.cam.echo2016.multinarrative.dev.Debug;
import uk.ac.cam.echo2016.multinarrative.gui.graph.Graph;
import uk.ac.cam.echo2016.multinarrative.gui.tool.InsertTool;
import uk.ac.cam.echo2016.multinarrative.gui.tool.SelectionTool;

/**
 * @author jr650
 */
public class FXMLController {

    @FXML
    private BorderPane root;
    @FXML
    private ScrollPane scroll;
    @FXML
    private Pane graphArea;
    @FXML
    private Text infoBar;
    @FXML
    private TextField propertyName;
    @FXML
    private Button addProperty;
    @FXML
    private Accordion properties;
    @FXML
    private ListView<String> nodes;
    @FXML
    private ListView<String> routes;

    @FXML
    private TitledPane itemEditor;
    @FXML
    private TextField itemName;
    @FXML
    private ListView<String> itemProperties;
    @FXML
    private Button itemPropertyDelete;

    private Boolean itemNode = null;

    private GUIOperations operations = new GUIOperations();

    private Graph graph;

    private SelectionTool selectTool;
    private InsertTool insertTool;

    public void init() {
	    Debug.logInfo("Init Controller", 5, Debug.SYSTEM_GUI);
	    addProperty.disableProperty().bind(propertyName.textProperty().isEmpty());
        graphArea.minHeightProperty().bind(scroll.heightProperty());
        graphArea.minWidthProperty().bind(scroll.widthProperty());
        graph = new Graph(scroll, graphArea, getOperations(), this);
        selectTool = new SelectionTool(graph);
        insertTool = new InsertTool(graph);
        selectMode();
        itemEditor.setDisable(true);
        nodes.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (newValue != null) {
                        routes.getSelectionModel().clearSelection();
                        itemNode = true;
                        selectTool.setSelection(newValue);
                        initSelect();
                    }
                });
        routes.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (newValue != null) {
                        nodes.getSelectionModel().clearSelection();
                        itemNode = true;
                        initSelect();
                    }
                });
        itemProperties.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    itemPropertyDelete.setDisable(newValue == null);
                });

        itemName.textProperty()
                .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    changeSelectName();
                });
        nodes.itemsProperty().set(FXCollections.observableArrayList());
        routes.itemsProperty().set(FXCollections.observableArrayList());
    }
    
    @FXML
    protected void close() {
    	System.exit(0);
    }

    @FXML
    protected void insertMode() {
        graph.setTool(insertTool);
    }

    @FXML
    protected void selectMode() {
        graph.setTool(selectTool);
    }

    @FXML
    protected void addPropertyButtonAction(ActionEvent event) {

        String name = propertyName.getText();
        try {
            operations.addProperty(name);// Throws
                                         // IllegalOperationException
                                         // if
                                         // fails
            addProperty(name);
        } catch (IllegalOperationException ioe) {
            setInfo(ioe.getMessage(), name);
        }
    }
    
    @FXML
    protected void deleteItemAction(ActionEvent e){
    	Debug.logInfo("DeleteItemAction", 5, Debug.SYSTEM_GUI);
    	removeSelect();
    }

    @FXML
    protected void onKeyPress(KeyEvent event) {
      	Debug.logInfo("Key Pressed: "+event, 5, Debug.SYSTEM_GUI);
      	switch(event.getCode()){
      	case SHIFT:
      		insertMode();
      		break;
      	case DELETE:
      	case BACK_SPACE:
      		removeSelect();
      		break;
      	default:
      	}
    }

    @FXML
    protected void onKeyRelease(KeyEvent event) {
	Debug.logInfo("Key Released: "+event, 5, Debug.SYSTEM_GUI);
        switch(event.getCode()){
        case SHIFT:
            selectMode();
            break;
        default:
        }
    }

    protected void addProperty(String s) {
	      Debug.logInfo("Add property: "+s, 5, Debug.SYSTEM_GUI);
        try {
            propertyName.setText("");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml_property.fxml"));
            TitledPane root = loader.load();
            FXMLPropertyController prop = (FXMLPropertyController) loader.getController();
            prop.init(s, this);
            properties.getPanes().add(root);
            setInfo(PROPERTY_ADDED, s);
        } catch (IOException ioe) {
            // Indicates that fxml files aren't set up properly...
            throw new RuntimeException("FXML files not configured correctly", ioe);
        }
    }

    protected boolean removeProperty(String s, TitledPane pane) {
	      Debug.logInfo("Remove property: "+s, 5, Debug.SYSTEM_GUI);
        try {
            operations.removeProperty(s);
            properties.getPanes().remove(pane);
            setInfo(PROPERTY_REMOVED, s);
            return true;
        } catch (IllegalOperationException ioe) {
            setInfo(ioe.getMessage(), s);
            return false;
        }
    }

    public void setInfo(String template, String... values) {
        infoBar.setText(Strings.populateString(template, values));
    }

    public GUIOperations getOperations() {
        return operations;
    }

    public void addNode(String name) {
        int i = 0;
        while (i < nodes.getItems().size() && nodes.getItems().get(i).compareTo(name) < 0) {
            i++;
        }
        nodes.getItems().add(i, name);

    }

    public void addRoute(String name) {
        int i = 0;
        while (i < routes.getItems().size() && routes.getItems().get(i).compareTo(name) < 0) {
            i++;
        }
        routes.getItems().add(i, name);
    }

    public void removeNode(String name) {
        nodes.getItems().remove(name);
    }

    public void removeRoute(String name) {
        routes.getItems().remove(name);
    }

    public void selectNode(String name) {
        nodes.getSelectionModel().select(name);
        itemNode = true;
        selectTool.setSelection(name);
        initSelect();
    }

    public void selectRoute(String name) {
        routes.getSelectionModel().select(name);
        itemNode = false;
        initSelect();
    }

    public void changeSelectName() {
        Debug.logInfo("Change Selected Item Name", 5, Debug.SYSTEM_GUI);

        if (itemNode != null) {
            String prevName = itemNode ? nodes.getSelectionModel().getSelectedItem()
                    : routes.getSelectionModel().getSelectedItem();
            String newName = itemName.getText();
            try {
                if (itemNode) {
                    operations.renameNode(prevName, newName);
                    graph.renameNode(prevName, newName);
                    removeNode(prevName);
                    addNode(newName);
                    nodes.getSelectionModel().select(newName);
                } else {
                    // TODO rename route
                }
            } catch (IllegalOperationException ioe) {
                setInfo(ioe.getMessage(), prevName, newName);
            }
        }
    }

    public void removeSelect() {
    	Debug.logInfo("Remove Selection", 5, Debug.SYSTEM_GUI);
    	if(itemNode != null){
    		if(itemNode){
    			String name = nodes.getSelectionModel().getSelectedItem();
    			operations.deleteNode(name);
    			graph.deleteNode(name);
    			nodes.getSelectionModel().clearSelection();
    			nodes.getItems().remove(name);
    			selectTool.resetSelection();
    			itemNode = null;
    		}else{
    			String name = routes.getSelectionModel().getSelectedItem();
    			operations.deleteRoute(name);
    			graph.deleteRoute(name);
    			routes.getSelectionModel().clearSelection();
    			routes.getItems().remove(name);
    			itemNode = null;
    		}
    		initSelect();
    	}
    }
    
    public void initSelect() {
        itemEditor.setExpanded(itemNode != null);
        itemEditor.setDisable(itemNode == null);
        if (itemNode == null) {
            itemName.setText("");
            itemProperties.getItems().clear();
        } else if (itemNode) {
            itemNode = null;
            itemName.setText(nodes.getSelectionModel().getSelectedItem());
            itemNode = true;
            itemProperties.getItems().clear();
            itemProperties.getItems().addAll(operations.getNodeProperties(nodes.getSelectionModel().getSelectedItem()));
        } else {
            itemNode = null;
            itemName.setText(routes.getSelectionModel().getSelectedItem());
            itemNode = false;
            itemProperties.getItems().clear();
            itemProperties.getItems().addAll(operations.getNodeProperties(routes.getSelectionModel().getSelectedItem()));
        }
    }

}
