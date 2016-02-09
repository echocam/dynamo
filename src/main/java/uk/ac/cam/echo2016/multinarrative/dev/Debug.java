package uk.ac.cam.echo2016.multinarrative.dev;

import com.google.gson.*;

import java.io.Writer;
import java.io.PrintWriter;

/**
 * This is a class that provides several useful methods that can help in debugging.
 * 
 * @author tr395
 *
 */
public class Debug {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    
    public static final class Type {
    	public final int value;
    	public final String name;
    	public Type(int v, String n) {
    		value = v;
    		name = n;
    	}
    }
    
    public static final int TYPE_NONE  = 0b00000000000000000000000000000000;
    public static final int TYPE_ALL   = 0b11111111111111111111111111111111;
    
    public static final int TYPE_ERROR       = 0b00000000000000000000000000000001;

    //The types below are all placeholder!
    public static final int TYPE_GUI_DISPLAY = 0b10000000000000000000000000000000;
    public static final int TYPE_GUI_USE     = 0b01000000000000000000000000000000;
    public static final int TYPE_GUI         = TYPE_GUI_DISPLAY | TYPE_GUI_USE;
    
    public static final int TYPE_ROUTE       = 0b00100000000000000000000000000000;
    
    private static Debug instance = null;
    
    private Debug() {
        //TODO(tr395): initialise and read data from config.json.
        String jsonString = "{\"log\": {\"console\": {\"all\": 3,\"gui\": 5,\"error\": {\"level\": 4,\"colour\":\"red\"}},\"file\": {\"error\": 5,\"all\": 3}}";
        /*
{
    "log": {
        "console": {
            "4": [
                "error"
            ]
        },
        "file": {
            "1": [
              "all"
            ],
            "4": [
                "error",
                "gui"
            ]
        }
    }
}

        */

        //JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();

        //jsonObject.get("name").getAsString(); //John
    }
    
    public static Debug getInstance() {
       if(instance == null) {
           instance = new Debug();
       }
       return instance;
    }
    
    /**
     * Prints out the provided string provided the current PRIORITY_LEVEL for the type provided is at least
     * as great as the priority level provided and the type is one of the types configured to be printed.
     * TODO(tr395): will also similarly log to a file.
     *
     * usage:
     *  printError("Fairies initialised and eating ice-cream.", 2, TYPE_PUDDING | TYPE_FAIRY)
     *
     * Should be used for printing useful debugging information, for printing errors refer to the printError method.
     * 
     * @param s The String you wish to print
     * @param priorityLevel The int describing priority of what is being printed (valued 1-5), the lower the number the 
     *        higher the priority.
     *        Only strings which are less than or equal to the current priority level set will be printed 
     *        <p>
     *        A guidline of what each priority should mean is given below, however it is up to the programmer to abide  
     *        by this.
     *        <ul>
     *          <li> 1: Really important debug info, will only be printed in for major, important events at an average  
     *                  rate at about 1/2 times per hour or on program startup/shutdown.
     *          <li> 2: Slightly less important debug info, maybe printed a bit more often than 1 or even less often,   
     *                  but used for less critical steps in the program.
     *          <li> 3: Important information normally critical for debugging, but not useful assuming program i 
     *                  running normally.
     *          <li> 4: Useful information for debugging, that you really don't want to see during the normal running
     *                  of a program.
     *          <li> 5: Really fine-grained debugging info, useful only for tracking down especially difficult bugs.
     *        </ul>
     * @param type The integer describing the type of debug info being printed (eg, related to gui, an error (special 
     *        case), backend, saving, audio).
     *        Look at all private static final int's that are prefixed with 'TYPE_' for types available.
     *        If a particular piece of information belongs to multiple types, you can bitwise or them together.
     *        eg. TYPE_PUDDING | TYPE FAIRY.
     */
    public void printInfo(String s, int logLevel, int logType) {
        Writer[] logOutputs = new Writer[1];
        logOutputs[0] = new PrintWriter(System.out);
        
        int[] logTypes = { //TEMPORARY CONFIG
            TYPE_ALL,
            TYPE_GUI,
            TYPE_NONE,
            TYPE_GUI_USE,
            TYPE_ERROR
        };
        
        for(Writer logOutput: logOutputs) {
            for(int i = logLevel - 1; i < 5; i++) {
                if((logTypes[i] & logType) != 0) {
                	StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[2];
                    int lineNumber = stackTrace.getLineNumber();
                    String fileName = stackTrace.getFileName();
                    String debugString = lineNumber + " " + fileName +": " + s;
                    System.out.println(debugString);
                    break;
                }
            }
        }
    }
    
    /**
     * Prints out the provided string as an errory provided the current PRIORITY_LEVEL for errors is at least
     * as great as the priority level provided and the type is one of the types configured to be printed.
     * TODO(tr395): will also similarly log to a file.
     *
     * usage:
     *  printError("The trifle has just exploded fairies!", 1, TYPE_PUDDING | TYPE_FAIRY)
     *
     * Should be used for printing useful debugging information, for printing errors refer to the printError method.
     * 
     * @param s The String you wish to print
     * @param priorityLevel The int describing priority of what is being printed (valued 1-9), the lower the number the 
     *        higher the priority.
     *        Only strings which are less than or equal to the current priority level set will be printed 
     *        <p>
     *        A guidline of what each priority should mean is given below, however it is up to the programmer to abide
     *        by this.
     *        <ul>
     *          <li> 1: Critical error, the program has had to abort as a result of it!
     *          <li> 2: Really bad error, the program can continue and/or recover from issue, but there is a glaring 
     *                  inconsistency that should be fixed and program should be stopped as soon as possible.
     *          <li> 3: Bad error, program can continue but problem should really be investigated as soon as possible.
     *          <li> 4: Not a major concern, but still an error that shouldn't be happening. Can be ignored
     *          <li> 5: Really small error that someone should get to eventually, but shouldn't be clogging up terminal
     *                  unless programmer is out to actively fix bugs.
     *        </ul>
     * @param type The integer describing the type of debug info being printed (eg, related to gui, an error (special 
     *        case), backend, saving, audio)
     *        Look at all private static final int's that are prefixed with 'TYPE_' for types available.
     *        If a particular piece of information belongs to multiple types, you can bitwise or them together.
     *        eg. TYPE_PUDDING | TYPE FAIRY.
     */
    public void printError(String s, int logLevel, int type) {
        printInfo(s, logLevel, type | TYPE_ERROR);
    }
    
    public static void main(String[] args) {
        System.out.println("testing debug class");
        Debug d = Debug.getInstance();
        d.printInfo("Testing TYPE_GUI level 5", 5, TYPE_GUI);
        d.printInfo("Testing TYPE_GUI_USE level 5", 5, TYPE_GUI_USE);
        d.printInfo("Testing TYPE_GUI_DISPLAY level 5", 5, TYPE_GUI_DISPLAY);
        d.printInfo("Testing TYPE_GUI_DISPLAY level 2", 2, TYPE_GUI_DISPLAY);
        d.printInfo("Testing TYPE_ROUTE level 5", 5, TYPE_ROUTE);
        d.printInfo("Testing TYPE_ROUTE level 1", 1, TYPE_ROUTE);
        d.printInfo("Testing TYPE_ERROR level 5", 5, TYPE_ERROR);
        d.printInfo("Testing TYPE_ERROR level 4", 4, TYPE_ERROR);
        d.printInfo("Testing TYPE_ERROR level 3", 3, TYPE_ERROR);
        d.printInfo("Testing TYPE_ERROR level 2", 2, TYPE_ERROR);
        d.printInfo("Testing TYPE_ERROR level 1", 1, TYPE_ERROR);
    }
}
