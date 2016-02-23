package uk.ac.cam.echo2016.multinarrative.preview;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import uk.ac.cam.echo2016.multinarrative.GameChoice;
import uk.ac.cam.echo2016.multinarrative.GraphElementNotFoundException;
import uk.ac.cam.echo2016.multinarrative.InvalidGraphException;
import uk.ac.cam.echo2016.multinarrative.NarrativeInstance;
import uk.ac.cam.echo2016.multinarrative.NarrativeTemplate;
import uk.ac.cam.echo2016.multinarrative.Route;
import uk.ac.cam.echo2016.multinarrative.io.SaveReader;
import uk.ac.cam.echo2016.multinarrative.preview.preprocessor.FileProcessor;

public class TextPreview {

    private File directory;
    private NarrativeInstance inst;
    private FileProcessor proc;

    public TextPreview(File f) throws IOException, InvalidGraphException {
        File[] children = f.listFiles((File dir, String name) -> {
            return name.endsWith(".dnm");
        });
        if (children.length != 1) {
            throw new IOException("Could not find unique template file (.dnm)");
        }
        NarrativeTemplate t = SaveReader.loadNarrativeTemplate(children[0].getPath());
        if (t == null) {
            throw new IOException("Could not load graph from file");
        }
        inst = t.generateInstance();
        directory = f;
        proc = new FileProcessor(FileProcessor.getDefaultProcessor(inst));
    }

    public void preview(PrintStream output, InputStream input) {
        ArrayList<Route> items = inst.getPlayableRoutes();
        while (!items.isEmpty()) {
            GameChoice choice;
            String s = choose(items, output, input);
            do {
                choice = doRoute(s, output, input);

                if (choice.hasEvent()) {
                    outputFile(choice.getEventIdentifier(), output, input, "Completed");
                }
                items = choice.getOptions();
                if (items.size() > 0) {
                    switch (choice.getAction()) {
                    case GameChoice.ACTION_CHOOSE_ROUTE:
                        output.println("Choose another narrative to play!");
                        break;
                    case GameChoice.ACTION_MAJOR_DECISION:
                        output.println("You must make a decision!");
                        break;
                    case GameChoice.ACTION_CONTINUE:
                        s = items.get(0).getId();
                        break;
                    }
                }
            } while (choice.getAction() == GameChoice.ACTION_CONTINUE);
        }
    }

    public GameChoice doRoute(String name, PrintStream output, InputStream input) {
        try {
            inst.startRoute(name);
        } catch (GraphElementNotFoundException e) {
            System.err.println("IMPOSSIBLE_ERROR");
            e.printStackTrace();
            System.exit(1);
        }
        outputFile(name, output, input, "Playing");
        try {
            GameChoice choice = inst.endRoute(name);
            return choice;

        } catch (GraphElementNotFoundException e) {
            System.err.println("IMPOSSIBLE_ERROR");
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public static String choose(ArrayList<Route> items, PrintStream output, InputStream input) {
        if (items.size() == 0) {
            return null;
        }
        @SuppressWarnings("resource")
        Scanner sc = new Scanner(input);
        // Don't close! since this closes input
        String r = null;
        do {
            output.println("Choose one of:");
            output.println(items);
            String choice = sc.nextLine();
            for (Route route : items) {
                if (route.getId().equals(choice)) {
                    r = route.getId();
                    break;
                }
            }
        } while (r == null);
        return r;
    }

    public void outputFile(String route, PrintStream output, InputStream input, String prefix) {
        File[] c = directory.listFiles((File dir, String name) -> {
            return name.equals(route + ".txt");
        });
        if (c.length > 0) {
            proc.process(c[0], output, input);
        } else {
            output.println(prefix + " " + route);
        }
    }

    public static void main(String[] args) throws IOException, InvalidGraphException {
        if (args.length != 1) {
            System.out.println("Usage java uk.ac.cam.echo2016.multinarrative.preview.TextPreview <directory>");
            return;
        }
        File f = new File(args[0]);
        if (!f.exists() || !f.isDirectory()) {
            System.out.println("Usage java uk.ac.cam.echo2016.multinarrative.preview.TextPreview <directory>");
            return;
        }
        System.out.println("Loading from: " + f.getPath());

        TextPreview preview = new TextPreview(f);
        preview.preview(System.out, System.in);
    }
}
