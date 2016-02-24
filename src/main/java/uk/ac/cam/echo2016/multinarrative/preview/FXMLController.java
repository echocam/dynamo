package uk.ac.cam.echo2016.multinarrative.preview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import uk.ac.cam.echo2016.multinarrative.GameChoice;
import uk.ac.cam.echo2016.multinarrative.GraphElementNotFoundException;
import uk.ac.cam.echo2016.multinarrative.NarrativeInstance;
import uk.ac.cam.echo2016.multinarrative.Node;
import uk.ac.cam.echo2016.multinarrative.Route;
import uk.ac.cam.echo2016.multinarrative.dev.Debug;
import uk.ac.cam.echo2016.multinarrative.preview.preprocessor.FileProcessor;

public class FXMLController implements Initializable {

    @FXML
    private HBox choices;

    @FXML
    private Button next;

    @FXML
    private WebView web;

    @FXML
    private ScrollPane scroll;

    private WebEngine engine;

    private File dir;
    private File index;

    private NarrativeInstance instance;

    private ArrayList<String> pages = new ArrayList<>();
    private ArrayList<Route> options = new ArrayList<>();
    private String routePlaying;

    private FileProcessor proc;

    public void init(File file, NarrativeInstance inst) {
        dir = file;
        index = new File(dir.getAbsolutePath() + File.separator + "index.html");
        instance = inst;
        proc = new FileProcessor(FileProcessor.getDefaultProcessor(inst));
        options = inst.getPlayableRoutes();

        display(inst.getStart().getId());
        inst.getStart().createProperties();
        inst.getStart().getProperties().putBoolean("System.isCompleted", true);
        initChoices(options);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        engine = web.getEngine();
    }

    @FXML
    public void onNext(ActionEvent event) {
        Debug.logInfo("Next Pressed, pages=" + pages, 2, Debug.SYSTEM_PREVIEW);
        if (!pages.isEmpty()) {
            nextPage();
        } else if (routePlaying != null) {
            try {
                GameChoice c = instance.endRoute(routePlaying);
                options = c.getOptions();
                if (c.hasEvent()) {
                    for (Node n : c.getEvents()) {
                        pages.add(n.getId());
                    }
                    c.completeEvents();
                    nextPage();
                } else {
                    initChoices(options);
                }
            } catch (GraphElementNotFoundException e) {
                Debug.logError(e, 2, Debug.SYSTEM_PREVIEW);
                throw new RuntimeException(e);
            }
            routePlaying = null;
        } else {
            initChoices(options);
        }

    }
    
    public void nextPage(){
        String s = pages.remove(0);
        Debug.logInfo("Starting " + s, 2, Debug.SYSTEM_PREVIEW);
        display(s);
        if(pages.isEmpty()){
            initChoices(options);
        }
    }

    public void initChoices(ArrayList<Route> items) {
        choices.getChildren().clear();
        for (Route s : items) {
            Button b = createOption(s.getId());
            choices.getChildren().add(b);
        }
    }

    public void initNext() {
        choices.getChildren().clear();
        choices.getChildren().add(next);
    }

    public void choose(String item) {
        Debug.logInfo("Choosen: " + item, 2, Debug.SYSTEM_PREVIEW);
        try {
            instance.startRoute(item);
            routePlaying = item;
            display(item);
            initNext();
        } catch (GraphElementNotFoundException e) {
            Debug.logError(e, 2, Debug.SYSTEM_PREVIEW);
            throw new RuntimeException(e);
        }
    }

    public void display(String item) {
        parseFrom(item);
        try {
            setContents(index);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setContents() {
        try {

            setContents(index);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setContents(File file) throws MalformedURLException {
        Debug.logInfo("Loading: " + file.getName(), 5, Debug.SYSTEM_PREVIEW);
        if (!file.exists()) {
            Debug.logError(file.getName() + " does not exist", 1, Debug.SYSTEM_PREVIEW);
        }
        String s = file.toURI().toURL().toString();
        engine.load(s);
    }

    public void parseFrom(String file) {
        PrintStream s = null;
        try {
            s = new PrintStream(index);
            File[] c = dir.listFiles((File direc, String name) -> {
                return name.equals(file + ".html");
            });
            if (c.length > 0) {
                proc.process(c[0], s);
            } else {
                Debug.logError("Not Found: " + file, 2, Debug.SYSTEM_PREVIEW);
                s.println("<!DOCTYPE html>");
                s.println("<html>");
                s.println("<body>");
                s.println("<h1>" + file + "</h1>");
                s.println("</body>");
                s.println("</html>");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    public Button createOption(String text) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("button.fxml"));
            Button b = loader.load();
            b.setText(text);
            b.setOnAction(e -> choose(text));
            return b;
        } catch (IOException ioe) {
            // Indicates that fxml files aren't set up properly...
            throw new RuntimeException("FXML files not configured correctly", ioe);
        }
    }
}
