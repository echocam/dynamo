package uk.ac.cam.echo2016.multinarrative.gui;

import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.PROPERTY_ADDED;
import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.PROPERTY_REMOVED;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
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
	@FXML
	private TitledPane nodeEditor;
	@FXML
	private RadioButton nodeSynch;
	@FXML
	private RadioButton nodeChoice;
	@FXML
	private TitledPane routeEditor;

	private Boolean itemNode = null;

	private GUIOperations operations = new GUIOperations();

	private Graph graph;

	private SelectionTool selectTool;
	private InsertTool insertTool;

	private Node propertiesSource = null;
	private ContextMenu propertiesMenu = new ContextMenu();

	public void init() {
		Debug.logInfo("Init Controller", 4, Debug.SYSTEM_GUI);
		addProperty.disableProperty().bind(propertyName.textProperty().isEmpty());
		graphArea.minHeightProperty().bind(scroll.heightProperty());
		graphArea.minWidthProperty().bind(scroll.widthProperty());
		graph = new Graph(scroll, graphArea, getOperations(), this);
		selectTool = new SelectionTool(graph);
		insertTool = new InsertTool(graph);
		selectMode();
		itemEditor.setDisable(true);
		nodeEditor.setDisable(true);
		routeEditor.setDisable(true);
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
						itemNode = false;
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
	protected void deleteItemAction(ActionEvent e) {
		Debug.logInfo("DeleteItemAction", 4, Debug.SYSTEM_GUI);
		removeSelect();
	}

	@FXML
	protected void setChoice(ActionEvent event) {
		if (itemNode != null && itemNode) {
			String node = nodes.getSelectionModel().getSelectedItem();
			try {
				if (!operations.isChoiceNode(node)) {
					operations.switchChoiceOrSynch(node);
					for(GraphNode n :selectTool.getSelection()){
						System.out.println(n.getContents().getShape());
						n.getContents().setShape(new Circle(10));
					}
				}
			} catch (IllegalOperationException e) {
				setInfo(e.getMessage(), node);
			}
		}
	}
	
	@FXML
	protected void setSynch(ActionEvent event) {
		if (itemNode != null && itemNode) {
			String node = nodes.getSelectionModel().getSelectedItem();
			try {
				if (operations.isChoiceNode(node)) {
					operations.switchChoiceOrSynch(node);
					for(GraphNode n :selectTool.getSelection()){
						n.getContents().setShape(null);
					}
				}
			} catch (IllegalOperationException e) {
				setInfo(e.getMessage(), node);
			}
		}
	}

	@FXML
	protected void onKeyPress(KeyEvent event) {
		Debug.logInfo("Key Pressed: " + event, 5, Debug.SYSTEM_GUI);
		switch (event.getCode()) {
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
		Debug.logInfo("Key Released: " + event, 5, Debug.SYSTEM_GUI);
		switch (event.getCode()) {
		case SHIFT:
			selectMode();
			break;
		default:
		}
	}

	protected void addProperty(String s) {
		Debug.logInfo("Add property: " + s, 4, Debug.SYSTEM_GUI);
		try {
			propertyName.setText("");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml_property.fxml"));
			TitledPane root = loader.load();
			FXMLPropertyController prop = (FXMLPropertyController) loader.getController();
			Menu menu = new Menu(s);
			propertiesMenu.getItems().add(menu);
			prop.init(s, this, menu);
			properties.getPanes().add(root);
			setInfo(PROPERTY_ADDED, s);
		} catch (IOException ioe) {
			// Indicates that fxml files aren't set up properly...
			throw new RuntimeException("FXML files not configured correctly", ioe);
		}
	}

	protected boolean removeProperty(String s, TitledPane pane, Menu menu) {
		Debug.logInfo("Remove property: " + s, 4, Debug.SYSTEM_GUI);
		try {
			operations.removeProperty(s);
			properties.getPanes().remove(pane);
			propertiesMenu.getItems().remove(menu);
			setInfo(PROPERTY_REMOVED, s);
			return true;
		} catch (IllegalOperationException ioe) {
			setInfo(ioe.getMessage(), s);
			return false;
		}
	}

	public void setInfo(String template, String... values) {
		String s = Strings.populateString(template, values);
		Debug.logInfo(s, 3, Debug.SYSTEM_GUI);
		infoBar.setText(s);
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
			b.setOnContextMenuRequested(event -> {
				propertiesSource = b;
				propertiesMenu.show(b, event.getScreenX(), event.getScreenY());
			});
			GraphNode newNode = new GraphNode(b, b.textProperty(), x, y, graph);
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
			setInfo(e.getMessage(), name);
		}

	}

	public GraphNode addChoiceNode(String name, double x, double y) {
		Debug.logInfo("Adding Choice " + name, 4, Debug.SYSTEM_GUI);
		try {
			operations.addChoiceNode(name, x, y);
			Button b = FXMLLoader.load(getClass().getResource("choice_button.fxml"));
			b.setText(name);
			b.setShape(new Circle(10));
			b.setOnContextMenuRequested(event -> {
				propertiesSource = b;
				propertiesMenu.show(b, event.getScreenX(), event.getScreenY());
			});
			GraphNode newNode = new GraphNode(b, b.textProperty(), x, y, graph);
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
			setInfo(e.getMessage(), name);
		}
		return null;
	}

	public void addChoiceNode(String name, GraphEdge edge) {
		GraphNode node = addChoiceNode(name, edge.getControl().getLayoutX(), edge.getControl().getLayoutY());
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
			Polygon p = new Polygon();
			p.getPoints().addAll(25.0, 0.0, 0.0, 8.0, 0.0, -8.0);
			p.setStroke(Color.rgb(127, 127, 127));
			p.setFill(Color.TRANSPARENT);
			p.setOnContextMenuRequested(event -> {
				propertiesSource = p;
				propertiesMenu.show(graphArea, event.getScreenX(), event.getScreenY());
			});
			GraphEdge edge = new GraphEdge(new SimpleStringProperty(name), from, to, c, p, 25, 0);
			graph.addEdge(edge);
			graph.updateEdge(edge);
			int i = 0;
			while (i < routes.getItems().size() && routes.getItems().get(i).compareTo(name) < 0) {
				i++;
			}
			routes.getItems().add(i, name);
		} catch (IllegalOperationException e) {
			graph.getController().setInfo(e.getMessage(), name);
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

	public void assignProperty(String property, String type, String value) {
		Debug.logInfo("Assigning " + property + ":" + type + "=" + value + " to " + propertiesSource, 4,
				Debug.SYSTEM_GUI);
		if (propertiesSource != null) {
			Object o = propertiesSource.getUserData();
			if (o instanceof GraphNode) {
				GraphNode n = ((GraphNode) o);
				operations.assignPropertyToNode(n.getName(), property, type, value);
				graph.updateNode(n);
				if (itemNode != null && itemNode) {
					initSelect();
				}
			} else if (o instanceof GraphEdge) {
				GraphEdge e = ((GraphEdge) o);
				operations.assignPropertyToRoute(e.getName(), property, type, value);
				graph.updateEdge(e);
				if (itemNode != null && !itemNode) {
					initSelect();
				}
			}
			propertiesSource = null;
		}
	}

	public void selectNode(String name) {
		nodes.getSelectionModel().select(name);
		itemNode = true;
		initSelect();
	}

	public void selectRoute(String name) {
		routes.getSelectionModel().select(name);
		itemNode = false;
		selectTool.resetSelection();
		initSelect();
	}

	public void deselect() {
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
			Debug.logInfo("[" + itemNode + "," + prevName + "," + newName + "]", 4, Debug.SYSTEM_GUI);
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
					operations.renameRoute(prevName, newName);
					graph.renameRoute(prevName, newName);
					routes.getItems().remove(prevName);
					int i = 0;
					while (i < routes.getItems().size() && routes.getItems().get(i).compareTo(newName) < 0) {
						i++;
					}
					routes.getItems().add(i, newName);
					routes.getSelectionModel().select(newName);
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
		nodeEditor.setExpanded((itemNode != null) && itemNode);
		nodeEditor.setDisable((itemNode == null) || !itemNode);
		routeEditor.setExpanded((itemNode != null) && !itemNode);
		routeEditor.setDisable((itemNode == null) || itemNode);
		if (itemNode == null) {
			itemName.setText("");
			itemProperties.getItems().clear();
		} else if (itemNode) {
			String node = nodes.getSelectionModel().getSelectedItem();
			itemNode = null;
			itemName.setText(node);
			itemNode = true;
			itemProperties.getItems().clear();
			itemProperties.getItems().addAll(operations.getNodeProperties(node));
			try {
				if (operations.isChoiceNode(node)) {
					nodeChoice.fire();
				} else {
					nodeSynch.fire();
				}
			} catch (IllegalOperationException e) {
				setInfo(e.getMessage(), node);
			}
		} else {
			itemNode = null;
			itemName.setText(routes.getSelectionModel().getSelectedItem());
			itemNode = false;
			itemProperties.getItems().clear();
			itemProperties.getItems()
					.addAll(operations.getRouteProperties(routes.getSelectionModel().getSelectedItem()));
		}
	}

	public ContextMenu getContextMenuForGraph() {
		return propertiesMenu;
	}
}
