package uk.ac.cam.echo2016.multinarrative.preview.preprocessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import uk.ac.cam.echo2016.multinarrative.NarrativeInstance;

public class FileProcessor {

    Map<String, Function<String[], Function<String, String>>> commands;

    public FileProcessor(Map<String, Function<String[], Function<String, String>>> commands) {
        this.commands = commands;
    }

    public void process(File f, PrintStream output, InputStream input) {
        try {
            BufferedReader r = new BufferedReader(new FileReader(f));
            String s;
            ArrayList<Function<String, String>> applies = new ArrayList<Function<String, String>>();
            try {
                while ((s = r.readLine()) != null) {
                    if (s.startsWith("%")) {// Comment
                    } else if (s.startsWith("#")) {// Command
                        for (Function<String, String> func : applies) {
                            s = func.apply(s);
                            if (s == null) {
                                break;
                            }
                        }
                        if (s != null) {
                            String[] comm = s.substring(1).split(" ");
                            if (comm.length > 0) {
                                Function<String[], Function<String, String>> command = commands.get(comm[0]);
                                if (command != null) {
                                    Function<String, String> apply = command.apply(comm);
                                    if (apply != null) {
                                        applies.add(0, apply);
                                    }
                                } else {
                                    System.err.println("Invalid Command:\n" + s);
                                }
                            } else {
                                System.err.println("Invalid Command:\n" + s);
                            }
                        }
                    } else {
                        for (Function<String, String> func : applies) {
                            s = func.apply(s);
                            if (s == null) {
                                break;
                            }
                        }
                        if (s != null) {
                            output.println(s);
                        }
                    }
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
    }

    public static Map<String, Function<String[], Function<String, String>>> getDefaultProcessor(
            NarrativeInstance inst) {
        Map<String, Function<String[], Function<String, String>>> r = new HashMap<String, Function<String[], Function<String, String>>>();
        r.put("ifx", s -> {
            if (s.length < 2) {
                System.err.println("Invalid ifx command");
                return null;
            } else {
                String str = "";
                for (int i = 1; i < s.length; i++) {
                    str += s[i];
                }
                boolean b = inst.getNode(str) != null || inst.getRoute(str) != null;
                return b ? new IfProcessor() : Function.identity();
            }
        });
        r.put("ifnx", s -> {
            if (s.length < 2) {
                System.err.println("Invalid ifx command");
                return null;
            } else {
                String str = "";
                for (int i = 1; i < s.length; i++) {
                    str += s[i];
                }
                boolean b = inst.getNode(str) != null || inst.getRoute(str) != null;
                return b ? Function.identity() : new IfProcessor();
            }
        });
        r.put("endif", s -> null);
        return r;
    }

}
