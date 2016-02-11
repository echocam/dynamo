package uk.ac.cam.echo2016.multinarrative.gui;

import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.PROPERTY_ADDED;
import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.PROPERTY_REMOVED;

import java.io.IOException;
import java.util.Iterator;

import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import uk.ac.cam.echo2016.multinarrative.GUINarrative;
import uk.ac.cam.echo2016.multinarrative.Node;
import uk.ac.cam.echo2016.multinarrative.Route;
import uk.ac.cam.echo2016.multinarrative.SynchronizationNode;
import uk.ac.cam.echo2016.multinarrative.dev.Debug;
import uk.ac.cam.echo2016.multinarrative.gui.graph.Graph;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphEdge;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphNode;
import uk.ac.cam.echo2016.multinarrative.gui.operations.GUIOperations;
import uk.ac.cam.echo2016.multinarrative.gui.operations.IllegalOperationException;
import uk.ac.cam.echo2016.multinarrative.gui.operations.Strings;
import uk.ac.cam.echo2016.multinarrative.gui.tool.InsertTool;
import uk.ac.cam.echo2016.multinarrative.gui.tool.SelectionTool;

/**
 * The class used by SceneBuilder as the source of all objects and methods.
 * 
 * @author jr650
 * @author rjm232
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
    
    private String currentFile;
    
    private FXMLGUI mainApp;

    public void init(FXMLGUI main) {
    	Debug.logInfo("Init Controller", 5, Debug.SYSTEM_GUI);
    	mainApp = main;
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
    
    /**
     * Code run when the "Close" menu item is clicked in the File menu.
     */
    @FXML
    protected void close() {
    	System.exit(0);
    }
    
    /**
     * Code run when "About" is clicked in Help menu.
     */
    @FXML
    protected void registerAboutClicked() {
    	mainApp.showAbout();
    }
    
    /**
     * Code run when "Save" clicked in File menu
     */
    @FXML
    protected void registerSaveClicked() {
    	if (currentFile == null) {
    		registerSaveAsClicked();
    	} else {
    		try {
    			operations.saveInstance(currentFile);
    		} catch (IOException ioe){
    			showErrorDialog("Error when trying to save file.");
    		}
    	}
    }
    
    /**
     * Code run when "Save as" clicked in the File menu
     */
    @FXML
    protected void registerSaveAsClicked() {
    	String returnedFile = mainApp.showSaveAs();
    	if (returnedFile == null) {
    		return;
    	}
    	
    	currentFile = returnedFile;
    	try {
    		operations.saveInstance(currentFile);
    	} catch (IOException ioe) {
    		showErrorDialog("Error when trying to save file.");
    	}
    }
    
    /**
     * Code run when saving fails
     */
    @FXML
    protected void showErrorDialog(String message) {
    	mainApp.showError(message);
    }
    
    /**
     * Code run when "Open" clicked in the File menu
     */
    @FXML
    protected void registerOpenClicked() {
    	String returnedFile = mainApp.showOpen();
    	if (returnedFile == null) {
    		return;
    	}
    	
    	currentFile = returnedFile;
    	try {
    		GUINarrative loaded = operations.loadInstance(currentFile);
    		if (loaded == null) {
    			throw new IOException();
    		}
    		buildGraph(loaded);
    	} catch (IOException ioe) {
    		showErrorDialog("Error when trying to open file");
    	}
    }

    /**
     * Mode entered when the Shift key on the keyboard is pressed.
     */
    @FXML
    protected void insertMode() {
        graph.setTool(insertTool);
    }

    /**
     * Default mode. Also entered when Shift key is released on keyboard.
     */
    @FXML
    protected void selectMode() {
        graph.setTool(selectTool);
    }

    /**
     * Code run when "+" button is pressed. "+" button is disabled when the text entry is empty. Adds
     * node to internal graph and then adds node to GUI graph.
     * @param event
     */
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

    /**
     * Registers a key being pressed on the keyboard and responds depending on the button pressed..
     * @param event
     */
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
	protected void deleteItemAction(ActionEvent e) {
		Debug.logInfo("DeleteItemAction", 4, Debug.SYSTEM_GUI);
		removeSelect();
	}

    /**
     * Registers a key being released on the keyboard and responds only if said key is the Shift key.
     * @param event
     */
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

    /**
     * Code to add the string to the list of properties on the right side of the screen. 
     * @param s
     */
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

    /**
     * Code to remove the string from the list of properties on the right side of the screen. 
     * @param s
     */
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

    /**
     * Adds the string formed by populating the string {@code template} with the list of strings in 
     * {@code values} to the info bar on the bottom right of the screen.
     * @param template
     * @param values
     */
    public void setInfo(String template, String... values) {
        infoBar.setText(Strings.populateString(template, values));
    }

    /**
     * Adds the node Id {@code name} to the list of nodes displayed on the left.
     * @param name
     */
    public void addNode(String name) {
        int i = 0;
        while (i < nodes.getItems().size() && nodes.getItems().get(i).compareTo(name) < 0) {
            i++;
        }
        nodes.getItems().add(i, name);
	}

	public GUIOperations getOperations() {
		return operations;
	}

	public void addSynchNode(String name, double x, double y) {
		Debug.logInfo("Adding Synch " + name, 4, Debug.SYSTEM_GUI);
		try {
			operations.addSynchNode(name, x, y);
			Button b = FXMLLoader.load(getClass().getResource("synch_button.fxml"));
			b.setText(name);
			GraphNode newNode = new GraphNode(b, b.textProperty(),x,y);
			graph.addNode(newNode);
			int i = 0;
			while (i < nodes.getItems().size() && nodes.getItems().get(i).compareTo(name) < 0) {
				i++;
			}
			nodes.getItems().add(i, name);
		} catch (IOException ioe) {
			// Error with fxml files
			throw new RuntimeException("FXML files not configured correctly", ioe);
		} catch (IllegalOperationException e) {
			setInfo(e.getMessage());
		}
	}


    /**
     * Adds the route Id {@code name} to the list of routes displayed on the left.
     * @param name
     */
    public void addRoute(String name) {
        int i = 0;
        while (i < routes.getItems().size() && routes.getItems().get(i).compareTo(name) < 0) {
            i++;
        }
        routes.getItems().add(i, name);
    }

	public GraphNode addChoiceNode(String name, double x, double y) {
		Debug.logInfo("Adding Choice " + name, 4, Debug.SYSTEM_GUI);
		try {
			operations.addChoiceNode(name, x, y);
			Button b = FXMLLoader.load(getClass().getResource("choice_button.fxml"));
			b.setText(name);
			b.setShape(new Circle(10));
			GraphNode newNode = new GraphNode(b, b.textProperty(),x,y);
			graph.addNode(newNode);
			int i = 0;
			while (i < nodes.getItems().size() && nodes.getItems().get(i).compareTo(name) < 0) {
				i++;
			}
			nodes.getItems().add(i, name);
			return newNode;
		} catch (IOException ioe) {
			// Error with fxml files
			throw new RuntimeException("FXML files not configured correctly", ioe);
		} catch (IllegalOperationException e) {
			setInfo(e.getMessage());
		}
		return null;
	}

	public void addChoiceNode(String name, GraphEdge edge) {
		GraphNode node = addChoiceNode(name, edge.getControl().getCenterX(), edge.getControl().getCenterY());
		if (node != null) {
			GraphNode end = edge.getTo();
			operations.setEnd(edge.getName(), name);
			edge.setTo(node);
			edge.zeroOffset();
			String s = operations.getUniqueNarrativeName();
			addRoute(s, node, end);
			graph.updateNode(node);
		}
	}

	public void addRoute(String name, GraphNode from, GraphNode to) {
		Debug.logInfo("Adding Route " + name, 4, Debug.SYSTEM_GUI);
		try {

			graph.getOperations().addRoute(name, from.getName(), to.getName());
			CubicCurve c = new CubicCurve();
			c.setStroke(Color.WHITE);
			c.setStrokeWidth(2);
			c.setStrokeLineCap(StrokeLineCap.ROUND);
			c.setFill(Color.TRANSPARENT);
			Circle ci = new Circle(4, Color.rgb(51, 51, 51));
			GraphEdge edge = new GraphEdge(new SimpleStringProperty(name), from, to, c, ci);
			graph.addEdge(edge);
			graph.updateEdge(edge);
			int i = 0;
			while (i < routes.getItems().size() && routes.getItems().get(i).compareTo(name) < 0) {
				i++;
			}
			routes.getItems().add(i, name);
		} catch (IllegalOperationException e) {
			graph.getController().setInfo(e.getMessage());
		}

	}
	
	public void buildGraph(GUINarrative toBuild) {
		operations = new GUIOperations();
		
		for (Node node : toBuild.getNodes().values()) {
			if (node.getProperties() == null) {
				showErrorDialog("Node properties null");
				return;
			}
			if (node instanceof SynchronizationNode) {
				addSynchNode(node.getId(), node.getProperties().getDouble("GUI.X"), node.getProperties().getDouble("GUI.Y"));
			} else {
				addChoiceNode(node.getId(), node.getProperties().getDouble("GUI.X"), node.getProperties().getDouble("GUI.Y"));
			}
		}
		
		for (Route route : toBuild.getRoutes().values()) {
			GraphNode temp;
			GraphNode start = new GraphNode();
			GraphNode end = new GraphNode();
			Iterator<GraphNode> it = graph.getNodes().iterator();
			while (it.hasNext()) {
				temp = it.next();
				if (temp.getName() == route.getStart().getId()) {
					 start = temp;
				}
			}
			Iterator<GraphNode> it2 = graph.getNodes().iterator();
			while (it2.hasNext()) {
				temp = it2.next();
				if (temp.getName() == route.getEnd().getId()) {
					end = temp;
				}
			}
			addRoute(route.getId(), start, end);
		}
		
	}

	public void removeNode(String name) { 
		operations.deleteNode(name);
		graph.deleteNode(name);
		nodes.getSelectionModel().clearSelection();
		nodes.getItems().remove(name);
		selectTool.resetSelection();
	}

	public void removeRoute(String name) { 
		operations.deleteRoute(name);
		graph.deleteRoute(name);
		routes.getSelectionModel().clearSelection();
		routes.getItems().remove(name);
	}

	public void selectNode(String name) {
		nodes.getSelectionModel().select(name);
		itemNode = true;
		initSelect();
	}

	public void selectRoute(String name) {
		routes.getSelectionModel().select(name);
		itemNode = false;
		initSelect();
	}

	public void deselect(){
		nodes.getSelectionModel().clearSelection();
		routes.getSelectionModel().clearSelection();
		itemNode = null;
		initSelect();
	}
	
	public void changeSelectName() {
		Debug.logInfo("Change Selected Item Name", 4, Debug.SYSTEM_GUI);

		if (itemNode != null) {
			String prevName = itemNode ? nodes.getSelectionModel().getSelectedItem()
					: routes.getSelectionModel().getSelectedItem();
			String newName = itemName.getText();
			try {
				if (itemNode) {
					operations.renameNode(prevName, newName);
					graph.renameNode(prevName, newName);
					nodes.getItems().remove(prevName);
					int i = 0;
					while (i < nodes.getItems().size() && nodes.getItems().get(i).compareTo(newName) < 0) {
						i++;
					}
					nodes.getItems().add(i, newName);
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
		Debug.logInfo("Remove Selection", 4, Debug.SYSTEM_GUI);
		if (itemNode != null) {
			if (itemNode) {
				String name = nodes.getSelectionModel().getSelectedItem();
				removeNode(name);
				itemNode = null;
			} else {
				String name = routes.getSelectionModel().getSelectedItem();
				removeRoute(name);
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
			itemProperties.getItems()
					.addAll(operations.getNodeProperties(routes.getSelectionModel().getSelectedItem()));
		}
	}
}
