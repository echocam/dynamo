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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import uk.ac.cam.echo2016.multinarrative.GraphElementNotFoundException;
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
    @FXML
    private TitledPane nodeEditor;
    @FXML
    private RadioButton nodeSynch;
    @FXML
    private RadioButton nodeChoice;
    @FXML
    private TitledPane routeEditor;
    @FXML
    private ComboBox<String> routeStart;
    @FXML
    private ComboBox<String> routeEnd;

    private Boolean itemNode = null;

    private GUIOperations operations = new GUIOperations();

    private Graph graph;

    private SelectionTool selectTool;
    private InsertTool insertTool;

    private String currentFile;

    private FXMLGUI mainApp;

    private Node propertiesSource = null;
    private ContextMenu propertiesMenu = new ContextMenu();

    public Graph getGraph() {
        return graph;
    }

    /**
     * Initialise the controller. Called from the GUI bootstrap
     */
    public void init(FXMLGUI main) {
        Debug.logInfo("Init Controller", 4, Debug.SYSTEM_GUI);
        mainApp = main;
        reInit();

    }

    private void reInit() {
        currentFile = null;
        propertyName.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (event.getCode() == KeyCode.EQUALS) {
                event.consume();
            }
        });
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
                    try {
                        changeSelectName();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
        itemPropertyDelete.disableProperty()
                .bind(itemProperties.getSelectionModel().selectedIndexProperty().lessThan(0));
        nodes.itemsProperty().set(FXCollections.observableArrayList());
        routes.itemsProperty().set(FXCollections.observableArrayList());

        routeStart.itemsProperty().bind(nodes.itemsProperty());
        routeEnd.itemsProperty().bind(nodes.itemsProperty());
    }

    /**
     * Code run when the "New" menu item is clicked in the File menu.
     */
    @FXML
    protected void registerNewClicked() {
        if (mainApp.checkIfShouldSave()) {
            registerSaveClicked();
        }

        GraphNode[] setCopy = graph.getNodes().values().toArray(new GraphNode[0]);

        for (GraphNode n : setCopy) {
            removeNode(n.getName());
        }

        reInit();
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
        showErrorDialog("This is an alert");
        if (currentFile == null) {
            registerSaveAsClicked();
        } else {
            try {
                operations.saveInstance(currentFile);
            } catch (IOException ioe) {
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
        if (mainApp.checkIfShouldSave()) {
            registerSaveClicked();
        }
        String returnedFile = mainApp.showOpen();
        if (returnedFile == null) {
            return;
        }

        currentFile = returnedFile;
        try {
            GraphNode[] setCopy = graph.getNodes().values().toArray(new GraphNode[0]);

            for (GraphNode n : setCopy) {
                removeNode(n.getName());
            }

            reInit();
            try {
                operations.buildGraph(currentFile, this);
            } catch (GraphElementNotFoundException ge) {
                showErrorDialog(ge.getMessage());
            }
        } catch (IOException ioe) {
            showErrorDialog("Error when trying to open file");
        }
    }

    /**
     * Sets the graph into insert mode
     */
    @FXML
    protected void insertMode() {
        graph.setTool(insertTool);
    }

    /**
     * Sets the graph into select mode
     */
    @FXML
    protected void selectMode() {
        graph.setTool(selectTool);
    }

    /**
     * FXML hook. Adds a property.
     * 
     * @param event
     *            Triggering event
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
     * FXML hook. Deletes the selected item
     * 
     * @param e
     *            Trigger event
     */
    @FXML
    protected void deleteItemAction(ActionEvent e) {
        Debug.logInfo("DeleteItemAction", 4, Debug.SYSTEM_GUI);
        removeSelect();
    }

    /**
     * Deletes a property from an item
     * 
     * @param e
     */
    @FXML
    protected void deleteItemProperty(ActionEvent e) {
        deleteProperty(itemProperties.getSelectionModel().getSelectedItem());
    }

    /**
     * FXML hook. Sets a node to choice
     * 
     * @param event
     *            Trigger event.
     */
    @FXML
    protected void setChoice(ActionEvent event) {
        if (itemNode != null && itemNode) {
            String node = nodes.getSelectionModel().getSelectedItem();
            try {
                if (!operations.isChoiceNode(node)) {
                    operations.switchChoiceOrSynch(node);
                    for (GraphNode n : selectTool.getSelection()) {
                        n.getContents().setShape(new Circle(10));
                    }
                }
            } catch (IllegalOperationException e) {
                setInfo(e.getMessage(), node);
            }
        }
    }

    /**
     * FXML hook. Sets a node to synch
     * 
     * @param event
     *            Trigger event.
     */
    @FXML
    protected void setSynch(ActionEvent event) {
        if (itemNode != null && itemNode) {
            String node = nodes.getSelectionModel().getSelectedItem();
            try {
                if (operations.isChoiceNode(node)) {
                    operations.switchChoiceOrSynch(node);
                    for (GraphNode n : selectTool.getSelection()) {
                        n.getContents().setShape(null);
                    }
                }
            } catch (IllegalOperationException e) {
                setInfo(e.getMessage(), node);
            }
        }
    }

    /**
     * FXML hook. Sets a routes start
     * 
     * @param event
     *            Trigger event.
     */
    @FXML
    protected void changeStartAction(ActionEvent event) {
        if (itemNode != null && !itemNode) {
            String edge = routes.getSelectionModel().getSelectedItem();
            String start = routeStart.getValue();
            GraphEdge gEdge = graph.getEdges().get(edge);
            try {
                operations.setStart(edge, start);
                gEdge.setFrom(graph.getNodes().get(start));
            } catch (IllegalOperationException e) {
                setInfo(e.getMessage(), edge, start);
                routeStart.setValue(gEdge.getFrom().getName());
            }
        }
    }

    /**
     * FXML hook. Sets a routes end
     * 
     * @param event
     *            Trigger event.
     */
    @FXML
    protected void changeEndAction(ActionEvent event) {
        if (itemNode != null && !itemNode) {
            String edge = routes.getSelectionModel().getSelectedItem();
            String end = routeEnd.getValue();
            GraphEdge gEdge = graph.getEdges().get(edge);
            try {
                operations.setEnd(edge, end);
                gEdge.setTo(graph.getNodes().get(end));
            } catch (IllegalOperationException e) {
                setInfo(e.getMessage(), edge, end);
                routeEnd.setValue(gEdge.getTo().getName());
            }
        }
    }

    /**
     * FXML hook. Triggers mode change on shift
     * 
     * @param event
     *            Trigger event.
     */
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

    /**
     * FXML hook. Triggers mode change on shift
     * 
     * @param event
     *            Trigger event.
     */
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

    /**
     * Adds a property
     * 
     * @param s
     *            name
     */
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

    /**
     * Removes a property
     * 
     * @param s
     *            name
     * @param pane
     *            pane to remove
     * @param menu
     *            the menu item for it
     * @return
     */
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

    /**
     * Sets the infobar text
     * 
     * @param template
     *            String template
     * @param values
     *            Values to interpolate into the text
     */
    public void setInfo(String template, String... values) {
        String s = Strings.populateString(template, values);
        Debug.logInfo(s, 3, Debug.SYSTEM_GUI);
        infoBar.setText(s);
    }

    /**
     * Get the operations
     * 
     * @return the operations
     */
    public GUIOperations getOperations() {
        return operations;
    }

    /**
     * Adds the node Id {@code name} to the list of nodes displayed on the left.
     * 
     * @param name
     */
    public void addNode(String name) {
        int i = 0;
        while (i < nodes.getItems().size() && nodes.getItems().get(i).compareTo(name) < 0) {
            i++;
        }
        nodes.getItems().add(i, name);
    }

    /**
     * Adds a new Synch node
     * 
     * @param name
     *            Node id
     * @param x
     *            position-x
     * @param y
     *            position-y
     * @throws GraphElementNotFoundException 
     */
    public void addSynchNode(String name, double x, double y) throws GraphElementNotFoundException {
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

    /**
     * Adds a new Choice node
     * 
     * @param name
     *            node id
     * @param x
     *            position-coordinate
     * @param y
     *            position-coordinate
     * @return
     * @throws GraphElementNotFoundException 
     */
    public GraphNode addChoiceNode(String name, double x, double y) throws GraphElementNotFoundException {
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

    /**
     * Adds a choice node, bisecting an edge
     * 
     * @param name
     *            node id
     * @param edge
     *            edge to bisect
     * @throws GraphElementNotFoundException 
     */
    public void addChoiceNode(String name, GraphEdge edge) throws GraphElementNotFoundException {
        GraphNode node = addChoiceNode(name, edge.getControl().getLayoutX(), edge.getControl().getLayoutY());
        if (node != null) {
            GraphNode end = edge.getTo();
            try {
                operations.setEnd(edge.getName(), name);
            } catch (IllegalOperationException e) {
                setInfo(e.getMessage(), edge.getName(), name);
            }
            edge.setTo(node);
            edge.zeroOffset();
            String s = operations.getUniqueRouteName();
            addRoute(s, node, end);
            graph.updateNode(node);
        }
    }

    /**
     * Adds a new route
     * 
     * @param name
     *            route id
     * @param from
     *            start node
     * @param to
     *            end node
     */
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

    /**
     * Removes a node
     * 
     * @param name
     *            node id
     */
    public void removeNode(String name) {
        operations.deleteNode(name);
        graph.deleteNode(name);
        nodes.getSelectionModel().clearSelection();
        nodes.getItems().remove(name);
        selectTool.resetSelection();

    }

    /**
     * Removes a route
     * 
     * @param name
     *            route id
     */
    public void removeRoute(String name) {
        operations.deleteRoute(name);
        graph.deleteRoute(name);
        routes.getSelectionModel().clearSelection();
        routes.getItems().remove(name);
    }

    /**
     * Assigns a property to a node
     * 
     * @param property
     *            Property name
     * @param type
     *            Property type
     * @param value
     *            Value of property (guiOperations should check type before
     *            adding
     * @throws GraphElementNotFoundException 
     */
    public void assignProperty(String property, String type, String value) throws GraphElementNotFoundException {
        Debug.logInfo("Assigning " + property + ":" + type + "=" + value + " to " + propertiesSource, 4,
                Debug.SYSTEM_GUI);
        if (propertiesSource != null) {
            try {
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
            } catch (IllegalOperationException e) {
                setInfo(e.getMessage(), property, type, value);
            }
        }
    }

    /**
     * Deletes a property value from the selected item
     * 
     * @param propertyAndValue
     */
    public void deleteProperty(String propertyAndValue) {
        Debug.logInfo("Deleting " + propertyAndValue+" from selected item", 4, Debug.SYSTEM_GUI);
        if (itemNode != null) {
            try {
                if (itemNode) {
                    String node = nodes.getSelectionModel().getSelectedItem();
                    operations.deleteProperty(node, propertyAndValue);
                    graph.updateNode(graph.getNodes().get(node));
                    initSelect();
                } else {
                    String route = routes.getSelectionModel().getSelectedItem();
                    operations.deleteProperty(route, propertyAndValue);
                    graph.updateEdge(graph.getEdges().get(route));
                    initSelect();
                }
                propertiesSource = null;
            } catch (IllegalOperationException e) {
                setInfo(e.getMessage(), propertyAndValue);
            } catch (GraphElementNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Select a node for editing
     * 
     * @param name
     */
    public void selectNode(String name) {
        nodes.getSelectionModel().select(name);
        itemNode = true;
        initSelect();
    }

    /**
     * Select a route for editing
     * 
     * @param name
     */
    public void selectRoute(String name) {
        routes.getSelectionModel().select(name);
        itemNode = false;
        selectTool.resetSelection();
        initSelect();
    }

    /**
     * De-selects selected item
     */
    public void deselect() {
        nodes.getSelectionModel().clearSelection();
        routes.getSelectionModel().clearSelection();
        itemNode = null;
        initSelect();
    }

    /**
     * Changes the name of the selection
     * @throws GraphElementNotFoundException 
     */
    public void changeSelectName() throws GraphElementNotFoundException {
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

    /**
     * Removes selected item
     */
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

    /**
     * Initialises components with new selection
     */
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
            String edge = routes.getSelectionModel().getSelectedItem();
            itemNode = null;
            itemName.setText(edge);
            itemNode = false;
            itemProperties.getItems().clear();
            itemProperties.getItems().addAll(operations.getRouteProperties(edge));
            GraphEdge gEdge = graph.getEdges().get(edge);
            if (gEdge != null) {
                routeStart.setValue(gEdge.getFrom().getName());
                routeEnd.setValue(gEdge.getTo().getName());
            }
        }
    }

    /**
     * Gets the context menu for graph components
     * 
     * @return the context menu
     */
    public ContextMenu getContextMenuForGraph() {
        return propertiesMenu;
    }
}
