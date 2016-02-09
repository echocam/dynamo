package uk.ac.cam.echo2016.multinarrative.gui;

import static uk.ac.cam.echo2016.multinarrative.gui.Strings.PROPERTY_ADDED;
import static uk.ac.cam.echo2016.multinarrative.gui.Strings.PROPERTY_REMOVED;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
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
    private RadioButton select;
    @FXML
    private RadioButton insert;
        
    @FXML
    private TitledPane itemEditor;
    @FXML
    private TextField itemName;
    @FXML
    private TextField itemProperties;

    private ToggleGroup group = new ToggleGroup();
    
    private GUIOperations operations = new GUIOperations();
    
    private Graph graph;
    
    private SelectionTool selectTool;
    private InsertTool insertTool;
    
    public void init(){
	graphArea.minHeightProperty().bind(scroll.heightProperty());
	graphArea.minWidthProperty().bind(scroll.widthProperty());
	graph = new Graph(scroll, graphArea, getOperations(), this);
	selectTool = new SelectionTool(graph);
	insertTool = new InsertTool(graph, this);
	selectMode();
	itemEditor.setDisable(true);
	select.setToggleGroup(group);
	insert.setToggleGroup(group);
    }
    
    @FXML 
    protected void insertMode(){
	selectTool.resetSelection();
	graph.setTool(insertTool);
    }
    
    @FXML 
    protected void selectMode(){
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
    protected void onKeyPress(KeyEvent event){
	System.out.println("Press "+event);
	if (event.getCode()==KeyCode.SHIFT){
	    insert.fire();
	    System.out.println(graph.getTool());
	}
    }
    
    @FXML
    protected void onKeyRelease(KeyEvent event){
	System.out.println("Release "+event);
	if (event.getCode()==KeyCode.SHIFT){
	    select.fire();
	    System.out.println(graph.getTool());
	}
    }

    protected void addProperty(String s) {
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
    
    public void addNode(String name){
	nodes.getItems().add(name);
    }
    
    public void addRoute(String name){
	routes.getItems().add(name);
    }
    
    public void removeNode(String name){
	nodes.getItems().remove(name);
    }
    
    public void removeRoute(String name){
	routes.getItems().remove(name);
    }

}
