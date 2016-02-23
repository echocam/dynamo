package uk.ac.cam.echo2016.multinarrative.preview;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import uk.ac.cam.echo2016.multinarrative.GameChoice;
import uk.ac.cam.echo2016.multinarrative.GraphElementNotFoundException;
import uk.ac.cam.echo2016.multinarrative.InvalidGraphException;
import uk.ac.cam.echo2016.multinarrative.NarrativeInstance;
import uk.ac.cam.echo2016.multinarrative.NarrativeTemplate;
import uk.ac.cam.echo2016.multinarrative.Route;
import uk.ac.cam.echo2016.multinarrative.io.SaveReader;

public class TextPreview {

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

        File[] children = f.listFiles();
        File template = null;
        for (int i = 0; i < children.length; i++) {
            if (children[i].getName().endsWith(".dnm")) {
                template = children[i];
                break;
            }
        }
        if (template == null) {
            System.out.println("Could not find template file.");
            return;
        }
        NarrativeTemplate t = SaveReader.loadNarrativeTemplate(template.getPath());
        preview(f, t.generateInstance());
    }

    public static void preview(File file, NarrativeInstance inst) {
        ArrayList<Route> items = inst.getPlayableRoutes();
        while (!items.isEmpty()) {
            GameChoice choice;
            String s = choose(items);
            do {
                choice = doRoute(file, inst, s);

                if (choice.hasEvent()) {
                    outputFile(file, choice.getEventIdentifier());
                }
                items = choice.getOptions();
                if (items.size() > 0) {
                    switch (choice.getAction()) {
                    case GameChoice.ACTION_CHOOSE_ROUTE:
                        System.out.println("Choose another narrative to play!");
                        break;
                    case GameChoice.ACTION_MAJOR_DECISION:
                        System.out.println("You must make a decision!");
                        break;
                    case GameChoice.ACTION_CONTINUE:
                        s = items.get(0).getId();
                        break;
                    }
                }
            } while (choice.getAction() == GameChoice.ACTION_CONTINUE);
        }
    }

    public static GameChoice doRoute(File file, NarrativeInstance inst, String name) {
        try {
            inst.startRoute(name);
        } catch (GraphElementNotFoundException e) {
            System.err.println("IMPOSSIBLE_ERROR");
            e.printStackTrace();
            System.exit(1);
        }
        outputFile(file, name);
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

    public static String choose(ArrayList<Route> items) {
        if (items.size() == 0) {
            return null;
        }
        @SuppressWarnings("resource")
        Scanner sc = new Scanner(System.in);
        // Don't close! since this closes system.in
        String r = null;
        do {
            System.out.println("Choose one of:");
            System.out.println(items);
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

    public static void outputFile(File file, String route) {
        File[] c = file.listFiles((File dir, String name) -> {
            return name.equals(route + ".txt");
        });
        if (c.length > 0) {
            try {
                BufferedReader r = new BufferedReader(new FileReader(c[0]));
                String s;
                try {
                    while ((s = r.readLine()) != null) {
                        System.out.println(s);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading file");
                } finally {
                    if (r != null) {
                        try {
                            r.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.err.println("File not found");
            }
        } else {
            System.out.println("Completed " + route);
        }
    }

}
